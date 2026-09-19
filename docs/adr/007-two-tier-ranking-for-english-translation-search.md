---
status: accepted
date: 2026-09-19
---

# ADR 007: Two-tier ranking for English translation search

## Context

ADR 006 introduced FTS5 BM25 `rank` combined with corpus frequency:

```sql
ORDER BY (rank * 0.7) - (frequency * 3.0)
```

This produced incorrect rankings because the formula breaks down when BM25 `rank` is
near zero (best possible relevance). At rank ≈ 0, the `frequency * 3.0` term dominates
entirely, making frequency the sole ordering factor:

- `kanë` (english = `"water jug"`, frequency = 13.44) ranked above `ujë` (english =
  `"water"`, frequency = 10.17) — the highest-frequency entry in the 51,607-row database
  wins by default, even though `kanë` is a compound meaning "water jug / can", not
  "water".
- `mollët` (english = `"pale orange"`, frequency = 6.50) ranked above `portokall`
  (english = `"orange"`, frequency = 5.70).
- `drite` (english = `"(oil) lamp, candlestick"`, frequency = 9.12) ranked above
  `vaj` (english = `"oil"`, frequency = 8.43).

The root cause: the `entry_fts` FTS5 virtual table indexes `english`, `albanian_ascii`,
and `gloss` equally. Gloss text is 50–200 characters with repeated term mentions, while
english text is 4–20 characters. BM25 equal-weighting lets gloss occurrences dominate
the relevance signal. Additionally, `rank` values cluster near zero for good matches, so
multiplying by 0.7 produces differences of ~0.01–0.5, easily swamped by `frequency * 3.0`
which ranges 0–40.

## Decision

Replace the flat ranking formula with a two-tier priority system:

**Tier 0**: entries where the first token of the `english` field exactly matches the
search word — these are "pure" translations where the English gloss starts with the
search word and contains no additional pipe-delimited meanings (e.g. `english = "water"`,
`english = "oil"`, `english = "orange"`).

**Tier 1**: all other matches — compounds where the search word appears as a modifier
(e.g. `english = "water jug"`, `english = "oil lamp"`) or entries that only match in the
gloss.

Within each tier, the existing BM25 + frequency formula breaks ties.

The SQL uses a CASE expression to extract the first token of the `english` field (everything
before the first `|` delimiter) and compare it to the search word:

```sql
ORDER BY
  CASE
    WHEN (
      CASE
        WHEN INSTR(entry.english, '|') > 0 THEN
          TRIM(SUBSTR(entry.english, 1, INSTR(entry.english, '|') - 1))
        ELSE entry.english
      END
    ) = ? THEN 0
    ELSE 1
  END,
  (rank * 0.7) - (frequency * 3.0)
```

This produces a two-parameter query: `arrayOf<Any>(term, term, limit)`.

### How first-token extraction works

| `english` value | INSTR(`\|`) | First token | Tier |
|---|---|---|---|
| `"water"` | 0 (no `\|`) | `"water"` | 0 — exact match |
| `"water\|to irrigate"` | >0 | `"water"` | 0 — exact match |
| `"water jug"` | 0 (no `\|`) | `"water jug"` | 1 — compound |
| `"pale orange"` | 0 | `"pale orange"` | 1 — compound |
| `"(oil) lamp"` | 0 | `"(oil) lamp"` | 1 — compound |
| `"housekeeper\|housewife"` | >0 | `"housekeeper"` | 1 — not "housewife" |

## Reasons

- **Direct translations surface first.** When a user types "water", they expect the
  translation for the substance, not "water jug" / "can" (`kanë`). The `words/` pipeline
  stores primary meanings as the first pipe-delimited token in `english`, making
  first-token extraction a reliable proxy for "is this the main translation?"
- **Frequency-based fallback preserves quality.** Within each tier, higher-frequency
  entries rank better. `ujë` (frequency 10.17) ranks above `potis` (frequency 0) because
  both are tier 0 (first token = "water") and `ujë` has higher frequency.
- **No schema or asset changes required.** The fix is purely a SQL query change.
  The `entry` table and `entry_fts` index already exist with the expected schema.
- **Avoids tuning magic numbers.** Previously, adjusting `0.7` or `3.0` coefficients
  was a trial-and-error exercise with no principled stopping point — the relative scale
  of rank vs. frequency varies by query and dataset. A tier-based system is explicit
  and predictable.

## Consequences

- **water** → `ujë` (pure, tier 0) ranks above `kanë` (compound, tier 1).
- **orange** → `portokall` (pure, tier 0) ranks above `mollët` (compound, tier 1).
- **oil** → `vaj` (pure, tier 0) ranks above `drite` (compound, tier 1).
- **housewife** → `babicë` and `amvisë` both have first token `"housekeeper"` (not
  `"housewife"`), so both land in tier 1. They tie on tier and frequency is the
  tiebreaker (`babicë` = 5.06, `amvisë` = 4.16), so `babicë` ranks first. This is a
  known limitation: the database stores `"housekeeper\|housewife"` for both entries,
  with no way to distinguish which meaning should take priority.
- **sunglasses** and **waterboard** are not in the database at all — missing data,
  not a ranking issue. These need to be added via the `words/` pipeline.
- **Revisit if**: the database schema changes to store primary meanings separately from
  gloss meanings, or if users report that compounds consistently outrank pure translations
  across a broad range of common words. At that point, consider restructuring the `words/`
  pipeline to produce a dedicated `english_primary` column.
