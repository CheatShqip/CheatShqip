---
status: accepted
date: 2026-07-14
---

# ADR 005: Remove ML Kit translator, use bundled dictionary DB as sole translation source

## Context

The home-screen search flow (`TranslationService`) currently does English→Albanian
translation in two steps:

1. `GetAlbanianTranslationOfEnglishWordPort` → `MlKitTranslator` (prod flavor) runs an
   on-device ML Kit neural model to turn the typed English word into a single Albanian
   word.
2. `GetWordSuggestionsPort` → `SqliteWordSuggestionsOutputAdapter` prefix-searches the
   bundled Room/SQLite dictionary (`dictionary.db`, FTS5 over `albanian_ascii`) using
   that Albanian word, returning up to 5 suggestions.

ML Kit is a large, opaque, network-dependent-at-first-use dependency for a step that
should be answerable from data the app already ships: `dictionary.db` bundles 51,607
entries with an `english` column containing pipe-separated English glosses per
Albanian headword (e.g. `kartë` → `english = "paper"`, and entries where `card`
appears as a token: `gërhanoj`, `kartushkë`, `lënur`).

Inspecting the bundled asset showed two gaps versus what ML Kit currently provides:

- There is no index on `english` — only `idx_entry_ascii` on `albanian_ascii`. Every
  English lookup would be a full 51k-row scan (still sub-100ms, but no ranking).
- `english = 'card'` (exact match) returns zero rows — English content is packed as
  `token|token|token` gloss strings, not one word per row, so lookups must match
  word-boundaries within that packed string, not the whole column.

Two ways to close this gap were considered:

- **Rebuild the DB with an English FTS5 index** (mirroring the existing
  `entry_fts` table over `albanian_ascii`), via the `words/` Python pipeline. Gives
  bm25-ranked results and prefix matching, but requires regenerating the 37MB asset,
  requires the `words/` pipeline sources (`uniparser` paradigms + `kaikki-Albanian.jsonl`)
  to be available, and requires a Room identity-hash bump — a much larger, slower,
  more fragile change to bundle into a straight adapter swap.
- **Exact word-token `LIKE` match** against the existing `english` column, no DB
  rebuild: `english = :w OR english LIKE :w||'|%' OR english LIKE '%|'||:w OR
  english LIKE '%|'||:w||'|%'`. Ships against the asset as it exists today.

A third variant, plain substring `LIKE '%card%'`, was rejected outright — it also
matches unrelated words containing the substring (`placard`, `discarded`,
`cardboard`), which is worse than useless for a translation lookup.

## Decision

Remove `MlKitTranslator` and the ML Kit dependency entirely. Collapse the two-step
translate-then-suggest flow into a single DB-backed lookup: replace
`GetAlbanianTranslationOfEnglishWordPort` + `GetWordSuggestionsPort` composition in
`TranslationService` with one port that takes the typed English `Word` and returns
`List<Translation>` directly from `dictionary.db`, using exact word-token `LIKE`
matching against the `english` column (no ranking, table order).

An English FTS5 index (bm25-ranked, prefix-matching) is an explicit fast-follow, not
part of this change — either via a `words/` pipeline rebuild or a first-launch Room
migration that builds the FTS table from the existing `english` column at runtime.

## Reasons

- The stated goal is "the DB is the source of truth" — keeping a port named
  `GetAlbanianTranslationOfEnglishWordPort` that no longer translates, feeding into a
  separate Albanian-only suggestions port, is an architecture built around ML Kit's
  shape rather than the DB's. One English-in, Albanian-out port matches the actual
  data model.
- Exact-token `LIKE` ships today with zero pipeline dependency and zero risk of a bad
  asset regen. The FTS rebuild pulls in the entire `words/` sources pipeline
  (`uniparser` + `kaikki` data acquisition, per `words/AGENTS.md`) and a Room
  identity-hash bump — disproportionate to a translation-source swap.
- Substring `LIKE` was rejected: it returns false positives from unrelated words
  that merely contain the query as a substring, which is a regression from ML Kit's
  actual-translation behavior, not a lateral move.
- Ranking (bm25) is the real gap versus ML Kit/FTS — without it, common words with
  many matches (e.g. `water` → ujë, potis, randis, sharnjej) come back in arbitrary
  table order with no "most common translation first." This is accepted as a known
  limitation of the fast-shipped version, not silently ignored.

## Consequences

- `MlKitTranslator.kt`, the `mlkit-translate` Gradle dependency
  (`app/build.gradle.kts`, `gradle/libs.versions.toml`), and both
  `FakeAlbanianTranslationOutputAdapter` copies (mock flavor + test source set) are
  removed.
- `GetAlbanianTranslationOfEnglishWordPort` and `GetWordSuggestionsPort` are replaced
  by a single output port (English `Word` → `List<Translation>`); `TranslationService`
  no longer composes two ports.
- Translation results will differ from ML Kit's neural output for many words — some
  English words that ML Kit could translate freely may not appear as a literal token
  in any gloss, and some matches (e.g. archaic/dialectal entries) will surface that a
  neural translator would not have chosen. This is an accepted trade-off of moving to
  a static dictionary as the source of truth.
- The REST `/define/{word}` path (`RESTWordSuggestionsOutputAdapter`,
  `ShqipRESTService`) was already dormant (not bound in prod/mock DI, only used in one
  integration test) — this change does not depend on or revive it.
- **Revisit if**: result ordering becomes a user-visible problem (common word buried
  under archaic ones) — at that point, build the English FTS index (pipeline rebuild
  or runtime migration) rather than tuning the `LIKE` query further.
