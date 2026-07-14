---
status: accepted
date: 2026-07-14
---

# ADR 004: Use the bundled SQLite driver for the dictionary Room database

## Context

The dictionary database (`DictionaryDatabase`) ships as a pre-built SQLite asset and is queried
via an FTS5 virtual table (`entry_fts`) for word-suggestion prefix search. Room was configured
with its default framework driver, which delegates to the OS's on-device SQLite build
(`android.database.sqlite.SQLiteDatabase`).

Running `DictionaryDatabaseAssetTest` on a Pixel_6 emulator (AOSP, API 36 / Android 16) failed
with `android.database.sqlite.SQLiteException: no such module: fts5`. The AOSP system image's
bundled SQLite library was compiled without the FTS5 extension. This isn't test-only: the
production suggestion query (`SqliteWordSuggestionsOutputAdapter`) runs the exact same
`entry_fts MATCH` query, so any device/OS build lacking FTS5 support would crash word
suggestions in the field.

## Decision

Configure Room to use `androidx.sqlite.driver.bundled.BundledSQLiteDriver`
(`androidx.sqlite:sqlite-bundled`) instead of the default framework driver, for both the
production `DictionaryDatabase` (wired in `ApplicationModule.kt`) and the instrumented test.

## Reasons

- The bundled driver ships its own compiled SQLite amalgamation with FTS5 always enabled,
  independent of the OS vendor's SQLite build — removes the dependency on device/API-level
  variance entirely.
- Rejected: requiring a minimum API level or blocklisting AOSP images — doesn't fix the root
  cause and still leaves other vendor images at risk.
- Rejected: falling back to FTS4 or `LIKE`-based prefix search — works everywhere but is a
  functional downgrade (worse ranking/tokenization) to work around an environment quirk rather
  than fixing it.

## Consequences

- **`createFromAsset()` is incompatible with a custom `SQLiteDriver` in Room 2.8.4** —
  `RoomDatabase.Builder.build()` throws `IllegalArgumentException: Pre-Package Database is not
  supported when an SQLiteDriver is configured.` Worked around with a manual copy: a shared
  `createDictionaryDatabase()` factory (`DictionaryDatabaseFactory.kt`) copies the `dictionary.db`
  asset into the app's database directory on first access, then builds Room against the already-
  present file (skipping `createFromAsset` entirely). Both production DI and the instrumented
  test go through this same factory, so the test exercises the real production wiring.
- **`RoomDatabase.openHelper` is unavailable with a custom driver** — raw SQL against
  non-`@Entity` tables (e.g. the `meta` table) must go through `@RawQuery` DAO methods instead of
  `db.openHelper.readableDatabase.query(...)`.
- While fixing the test, found and fixed a related production bug: the FTS query
  (`entry_fts MATCH ?`) was unrestricted and matched the prefix against *any* indexed column
  (`albanian_ascii`, `english`, `gloss`), not just the Albanian headword — verified directly
  against the bundled `dictionary.db` with `sqlite3`. Scoped the match to
  `entry_fts.albanian_ascii` in both `SqliteWordSuggestionsOutputAdapter` and the test.
- Revisit if a future Room release lifts the `createFromAsset` + custom-driver restriction —
  the manual-copy factory could then be simplified back to `.createFromAsset(...)`.
