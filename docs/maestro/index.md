# Maestro E2E Test Strategy

Flows live in `.maestro/` and target `com.cheatshqip` (plain `debug` build variant).
The app is fully offline — translation and word details come from the bundled SQLite
dictionary, so no network service (WireMock or otherwise) is required.

## Contents

- [ci-android.md](ci-android.md) — GitHub Actions matrix (recommended) + local parallel run
- [ci-browserstack.md](ci-browserstack.md) — BrowserStack App Automate (real devices)
- [ci-ios.md](ci-ios.md) — iOS (future, requires cross-platform rewrite)
- [baselines.md](baselines.md) — Screenshot baseline storage strategies