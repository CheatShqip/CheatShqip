---
status: accepted
date: 2026-11-21
---

# ADR 011: Extract Hysa Collocations and Use Exact English Translation Search

## Context

Common English words such as `moonlight`, `sunlight`, and `sunshine` had no usable
translation in the bundled dictionary:

- `moonlight` had zero entries.
- `sunlight` and `sunshine` only matched fragments of long glosses such as
  `bathe in sunlight`, `serving to admit more sunlight`, and `lie in the sunshine`.
  The app displayed the corresponding entries (`diellzoj`, `dritësor`, `gërrellë`) as
  if they were translations.
- The Hysa 1993 source did contain the mappings (`dritë e diellit: sunlight, sunshine`,
  `dritë hëne: moonlight`), but the extractor dropped them:
  - collocations containing `:` were excluded from enrichment;
  - compound extraction accepted only two alphabetic tokens whose first token matched
    the record's own headword. `dritë hëne` is a collocation of `hënë`, so it lost.
- The app's search used an FTS5 phrase match and merely ranked exact matches first.
  When no exact match existed, it still returned any entry whose gloss contained the
  query token. This turned "no translation" into a wrong translation.
- `DictionaryDatabaseFactory` copied the bundled database only when it was missing.
  Existing installs therefore never received dictionary updates after an app update.

## Decision

1. **Extract collocations whose head noun is the first token**, even when that token is
   not the record's headword. This captures `dritë hëne` (from the `hënë` entry) and
   `dritë dielli` (from the `diell` entry) without concatenating words. For these
   cross-headword entries, only trust the head noun's gender and indefinite singular;
   leave definite/plural forms empty so curated entries can provide verified forms.
2. **Curate canonical light collocations** in `supplemental_entries.json`:
   `dritë dielli` (sunlight, sunshine) and `dritë hëne` (moonlight), with correct
   definite forms.
3. **Search only exact English terms.** The adapter queries `ee.english = :word`
   instead of an FTS phrase. When no exact term exists, it returns an empty list and
   the UI displays `No translation found.` This supersedes the FTS ranking path from
   ADRs 006 and 007 for English-to-Albanian translation lookup.
4. **Version the bundled database asset.** The SQLite writer stamps `asset_revision`
   in `meta`; the app compares a sidecar revision file and replaces an installed
   database whose revision is outdated.

## Reasons

- **Correctness over recall.** A missing translation is recoverable by adding data;
  a confidently displayed wrong translation is a correctness bug.
- **Root-cause fix in the pipeline.** A curated-only fix for `moonlight` would not
  handle other collocations structurally. Accepting two-token collocations whose first
  token has known noun morphology also avoids inventing Albanian compounds or
  concatenated headwords.
- **No word concatenation.** `dritë hëne` and `dritë dielli` keep their space; the
  non-standard form `dritëdielli` is never generated.
- **Exact matches still cover compound entries** such as `sunglasses`, `full moon`,
  and `water jug` because those are stored as exact English terms. Only fragmentary
  sentence matches are removed.
- **Asset revision is cheaper than always recopying.** Replacing a 50 MB database on
  every launch would add startup cost; a revision file makes refreshes explicit and
  testable.

**Rejected alternatives:**
- **Keep FTS and filter long matches in Kotlin:** requires carrying the matched English
  term through the DAO and introduces more heuristic thresholds.
- **Add only `moonlight`/`sunlight` to the curated list:** fixes the reported words but
  leaves the Hysa collocation gap and other similar misses.
- **Create entries from `X e Y` and hyphenated collocations:** the source forms are
  inconsistent and partly OCR-damaged (`dritë - hënës`); canonicalization without
  verified morphology would generate invalid headwords.
- **Always recopy the database asset on startup:** correct but unnecessarily expensive
  for a 50 MB asset.

## Consequences

- English lookups for words without an exact term now show the no-result message.
  Variants such as plurals, hyphen/space differences, and typos are not matched.
- The pipeline now produces more collocation entries from Hysa; some forms remain
  noisy because the source is OCR-derived.
- Regenerating `dictionary.db` requires bumping `_ASSET_REVISION` in the words pipeline
  and `DICTIONARY_ASSET_REVISION` in the app so existing installs refresh.
- Revisit if users need approximate suggestions (for example short compound fallback)
  or if variant normalization (hyphens, spaces, plurals) becomes a priority.
