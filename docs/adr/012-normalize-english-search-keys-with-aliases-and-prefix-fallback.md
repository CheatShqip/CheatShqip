---
status: accepted
date: 2026-11-21
---

# ADR 012: Normalize English Search Keys with Aliases and Prefix Fallback

## Context

ADR 011 replaced FTS phrase matching with an exact `entry_english.english = :word`
lookup. That fixed wrong translations from fragmentary gloss matches, but made every
spelling variant unreachable:

- OCR-derived terms can contain hyphen/space artifacts (`pi- llow case`, `ear-ache`)
  or split words (`wed ding present`).
- Legitimate compounds exist as hyphenated, spaced, or concatenated forms
  (`letter-box`, `letter box`, `letterbox`, `coffeecup`, `coffee cup`).
- The exact column was only lowercased at build time, while the app folded diacritics
  at query time, so accented English terms could not match.
- The adapter still used `entry_english`, whose rows mirror raw source terms; there was
  no place to attach search-only variants without changing the meaning of that table.

## Decision

1. Compute two normalized search keys for every English term in the `words/` pipeline:
   - **canonical**: NFKD, diacritics removed, lowercase, unicode dashes to spaces,
     punctuation to spaces, whitespace collapsed.
   - **compact**: canonical with all separators removed.
2. Store the aliases in a dedicated `english_search` table with an
   `english_search_fts` FTS5 index. Keep `entry_english` as the source-of-truth term
   list; do not overload it with variants.
3. Query the exact alias table with both keys, preferring the canonical match.
4. If no alias matches, fall back to (a) a quoted canonical phrase in
   `english_search_fts`, then (b) a compact prefix (`coffe*`, `sunglass*`).
5. Bump the bundled database asset revision to `3` in both the pipeline and the app.

This partially supersedes ADR 011: exact normalized lookup remains the primary path,
but limited FTS matching returns for phrase and prefix fallback only — never for
arbitrary gloss fragments.

## Reasons

- **One normalization rule solves several variants.** `letter-box`, `letter box`, and
  `letterbox` share the compact key; a query written in any form reaches the same row.
- **OCR corruption becomes searchable without rewriting source data.** `pi- llow case`
  and `wed ding present` compact to `pillowcase` and `weddingpresent`, so clean queries
  match them. Aggressive source repair is avoided because the Hysa data is unreliable.
- **Dedicated table preserves existing semantics.** `entry_english` stays one row per
  sense for provenance and existing FTS tests. The alias table can grow variants without
  changing that contract.
- **No Room identity-hash churn.** `english_search` and its FTS table are not Room
  entities, so the schema hash does not change; only the asset revision forces a refresh.
- **Prefix fallback handles concatenation and partial input.** A compact prefix finds
  `coffeecup` from `coffee` and `weddingpresent` from `wedding`, which exact keys alone
  cannot do.
- **Query strings stay safe.** Normalization reduces keys to `[a-z0-9' ]`, so FTS match
  expressions cannot contain quotes or FTS operators.

**Rejected alternatives:**
- **Normalize only at query time:** the database still stores one raw spelling, so
  `coffeecup` cannot match `coffee cup` without an alias or substring index.
- **Store aliases in `entry_english`:** simpler, but breaks the “one row per sense”
  invariant and existing row-level tests, and gives no place to distinguish alias kinds.
- **Repair all OCR terms in the pipeline:** word segmentation is heuristic and would
  change JSON goldens and source data for uncertain gains; the compact key already makes
  them searchable.
- **Use a trigram tokenizer or fuzzy Levenshtein fallback:** broader recall for typos,
  but larger index cost and more false positives; defer until users report the need.

## Consequences

- Hyphen, space, concatenation, case, and accent variants resolve to the same
  translations for terms that exist in the dictionary.
- `english_search` adds roughly 151k rows (from 105k source terms) to the 52 MB asset.
- `dictionary.db` must be regenerated for future data changes; both
  `_ASSET_REVISION` and `DICTIONARY_ASSET_REVISION` must stay in sync.
- Source data remains noisy (`wed ding present` is still displayed internally in the
  source term list); the search layer hides it but does not fix it.
- Typos inside a word (`cofee`) and incomplete multi-word queries are still unmatched.
- Revisit if users need typo tolerance or if another variant class appears that the two
  keys cannot express.
