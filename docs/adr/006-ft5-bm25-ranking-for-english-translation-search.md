---
status: accepted
date: 2026-09-19
---

# ADR 006: Use FTS5 BM25 ranking for English translation search

## Context

ADR 005 shipped the initial English→Albanian translation lookup using a 7-pattern
`LIKE` query against the `english` column of `dictionary.db`. This approach had two
problems:

1. Results returned in arbitrary table order — no relevance ranking.
2. The `entry_fts` FTS5 virtual table (defined in `sqlite_writer.py` since the pipeline's
   inception) already indexes `albanian_ascii`, `english`, and `gloss` with
   `tokenize='unicode61'`, but the Android app never queried it for English search.

The `LIKE` query used `|` and `;` delimiters to avoid substring false positives
(e.g., `"placard"` matching `"card"`), but this produced no ranking — every match
scoring equal weight regardless of how well it matched.

## Decision

Replace the `LIKE`-based query with an FTS5 `MATCH` query ordered by BM25 `rank`:

```sql
SELECT DISTINCT entry.* FROM entry, entry_fts
WHERE entry.rowid = entry_fts.rowid
  AND entry_fts.english MATCH ?
ORDER BY rank
LIMIT 20
```

FTS5 `unicode61` tokenizer treats `|`, `;`, and space as token separators — the same
boundaries the `LIKE` query attempted to replicate. BM25 `rank` returns more negative
values for better matches (e.g., -11.37 for `"car"` matching exactly `"car"`, -7.90
for `"car"` matching `"machine; vehicle, car"`).

## Reasons

- **Deterministic ranking.** `ORDER BY rank` guarantees the same result order every
  time, unlike the previous arbitrary table order.
- **Zero DB changes.** The `entry_fts` table already indexed `english` from the
  pipeline's inception — no schema bump, no asset rebuild, no Room identity-hash change.
- **Token boundaries handled natively.** FTS5 `unicode61` tokenization avoids the
  `LIKE '%card%'` false-positive problem (matches `placard`, `cardboard`) without
  requiring 7 manually-crafted patterns.
- **Exact matches rank highest.** Entries where the query word is the sole or first
  token in `english` (e.g., `"car"`) score better than entries where it appears among
  several glosses (e.g., `"machine; vehicle, car"`).

## Consequences

- `veturë` (english = `"car"`) ranks above `makinë` (english = `"machine; vehicle, car"`)
  for the query `"car"` — the shorter document scores higher in BM25.
- The database contains **no frequency or popularity data** for Albanian headwords.
  Ranking is purely BM25-based on the `english` field.
- Results are capped at 5 in `TranslationService` (MAX_NUMBER_OF_RESULTS), so only the
  top 5 BM25-ranked translations are ever shown to the user.
- **Revisit if**: users consistently see incorrect translations first (e.g., formal
  `veturë` vs colloquial `makinë` for `"car"`). At that point, add a frequency field
  to the `words/` pipeline or introduce a secondary sort preferring shorter english
  fields as primary translations.
