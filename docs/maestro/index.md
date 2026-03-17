# Maestro E2E Test Strategy

Flows live in `.maestro/` and target `com.cheatshqip` (mockDebug build variant).
WireMock serves offline REST responses; `FakeAlbanianTranslationOutputAdapter` replaces ML Kit.

## Contents

- [ci-android.md](ci-android.md) — GitHub Actions matrix (recommended) + local parallel run
- [ci-browserstack.md](ci-browserstack.md) — BrowserStack App Automate (real devices)
- [ci-ios.md](ci-ios.md) — iOS (future, requires cross-platform rewrite)
- [baselines.md](baselines.md) — Screenshot baseline storage strategies
