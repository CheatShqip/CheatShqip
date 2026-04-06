---
status: accepted
date: 2026-04-06
---

# ADR 002: CI/CD Pipeline Design

## Context

CheatShqip requires a CI/CD pipeline that covers unit tests, static analysis, and visual
regression tests (screenshot diffs). The screenshot tests involve a live Android emulator,
Maestro E2E flows, and comparison against known-good baseline images. Baselines are large binary
files that should not live in git.

Several design decisions were made incrementally as the pipeline evolved.

---

## Decision 1: Three-job CI pipeline (unit tests, Detekt, screenshot tests)

**Status**: accepted

The CI workflow is split into three independent jobs: `unit-tests`, `detekt`, and
`screenshot-tests`. The screenshot job has a `needs` dependency on the first two so it only runs
when the cheaper checks pass.

**Reasons**: Unit tests and Detekt are fast and compute-cheap. Running them first avoids spinning
up an Android emulator (slow, expensive) for a build that already fails linting or tests.

---

## Decision 2: Screenshot baselines stored in Cloudflare R2, not in git

**Status**: accepted

Baseline PNG images are not checked into the repository. They are uploaded to Cloudflare R2 via
a dedicated `update-baselines` workflow (triggered manually, tagged by version). The CI
`screenshot-tests` job downloads the latest `v*` tag's baselines from R2 before running diffs.

A reusable composite action (`.github/actions/upload-to-r2`) wraps `aws s3 sync` against R2's
S3-compatible API.

**Reasons**: Binary assets in git inflate clone size and make history noisy. R2 provides
versioned, cheap object storage; the S3-compatible API means no extra tooling beyond the AWS CLI
already available on runners.

**Consequences**:
- `update-baselines` must be run (and tagged) before CI can compare screenshots.
- R2 credentials (`R2_ACCESS_KEY_ID`, `R2_SECRET_ACCESS_KEY`, `R2_BUCKET_NAME`,
  `R2_ENDPOINT_URL`) must be stored as repository secrets.
- `WIREMOCK_VERSION` is a repository variable (defaults to `3.10.0`).

---

## Decision 3: Runner migration — ubuntu-latest → Blacksmith → Shipfox

**Status**: accepted (Shipfox)

The pipeline started on `ubuntu-latest` (GitHub-hosted). It was automatically migrated to
`blacksmith-4vcpu-ubuntu-2404` by the Blacksmith bot, then manually switched to
`shipfox-performance-4cpu-ubuntu-2404`.

**Reasons for leaving GitHub-hosted runners**: Cost and performance on emulator-heavy jobs.
Shipfox was chosen over Blacksmith for performance characteristics on the emulator workload.

**Consequence — ANDROID_HOME not pre-set**: GitHub-hosted Ubuntu runners export `ANDROID_HOME`
automatically. Shipfox runners do not. This caused a `SDK location not found` failure when
`android-emulator-runner` tried to invoke Gradle, because the env var resolved to `undefined`.

**Fix**: Added `android-actions/setup-android@v3` before the emulator runner step in both
`ci.yml` and `update-baselines.yml`. This step installs the Android SDK and exports `ANDROID_HOME`
before any Gradle or emulator work begins.

---

## Decision 4: KVM explicitly enabled on CI runners

**Status**: accepted

Both emulator jobs include an explicit step to configure KVM permissions:

```bash
echo 'KERNEL=="kvm", GROUP="kvm", MODE="0666", OPTIONS+="static_node=kvm"' \
  | sudo tee /etc/udev/rules.d/99-kvm4all.rules
sudo udevadm control --reload-rules && sudo udevadm trigger --name-match=kvm
```

**Reasons**: Without this, `android-emulator-runner` falls back to software rendering only,
which is significantly slower. The step was added after observing emulator instability with
pure swiftshader rendering.

---

## Decision 5: WireMock JAR downloaded at CI time, not checked in

**Status**: accepted

The WireMock standalone JAR is downloaded from Maven Central during each workflow run. The JAR
path (`.wiremock/wiremock-standalone.jar`) is listed in `.gitignore`. The version is controlled
via the `WIREMOCK_VERSION` repository variable (default `3.10.0`).

**Reasons**: JARs are large binaries unsuitable for git. Downloading at CI time keeps the
repo lightweight and makes version upgrades a one-variable change.

---

## Consequences (overall)

- All jobs run on Shipfox runners; switching back to GitHub-hosted runners requires re-adding
  `ANDROID_HOME` awareness (it would work out of the box there, but the `setup-android` step
  is now present and harmless either way).
- The `update-baselines` workflow is a manual gate: intentional UI changes require running it
  with a new version tag before CI screenshot tests will pass.
- Test results (XML) and screenshot diffs (PNG) are uploaded to R2 and as GitHub Actions
  artifacts respectively on every run, regardless of pass/fail.