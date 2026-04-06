---
status: accepted
date: 2026-04-06
---

# ADR 003: Crop System UI from Screenshots

## Context

Maestro screenshot tests were non-deterministic due to system UI (status bar, navigation bar) appearing in captured images. Several compounding issues were discovered during investigation:

**Maestro overlay bug** — Maestro's background driver automatically tries to apply a device emulation overlay using `cmd overlay enable-exclusive --category <name>` without a package name argument. This is invalid syntax; all attempts silently fail. As a result, the active overlay on the `google_apis` image was `pixel_6a` instead of `pixel_6`, causing the clock to be in the wrong position.

**Race condition with `adb root`** — The `enable_demo_mode` function called `adb root`, which restarts adbd and briefly disconnects the device. Maestro's background driver was concurrently trying to use the device during this window, producing `adb: device offline` and `adb: no emulators found` errors. With `set -euo pipefail`, any subsequent failing adb command killed the script before screenshots were taken.

**Resolution of the race** — The fix was to:
1. Wait for Maestro's driver to finish initializing (polling port 7001) before any device configuration.
2. Switch from `adb root` (which restarts adbd) to `su root` inline on each privileged command, avoiding any device disconnect entirely.

**Emulator target** — To eliminate overlay issues and `adb root` disconnect problems, the emulator target was switched from `google_apis` to `default` (AOSP). The `default` image has no Google overlay restrictions, supports `su root` inline, and has a predictable status bar. The mock flavor does not depend on Google APIs.

**Status bar clipping** — On the `google_apis` image with `profile: pixel_6`, the status bar was visually clipped (only half visible) both in screenshots and in the live AVD. This was caused by the Pixel 6 display cutout (punch-hole camera) and rounded corners conflicting with a missing SystemUI overlay. The issue was present locally and in CI.

## Decision

After each Maestro run, crop all screenshots to the stable content area (excluding status bar and navigation bar) using the device's own reported insets, parsed from `dumpsys window displays`.

## Reasons

- **System UI is non-deterministic by nature** — clock, network icons, battery level, and notification dots change between runs. Demo mode mitigates most of this but requires ongoing maintenance as Android adds new status icons.
- **Overlay bug is unresolvable without forking Maestro** — Maestro's broken `enable-exclusive --category` syntax cannot be corrected from outside the tool. Even if it were fixed, overlay behaviour differs across system images.
- **Cropping is simpler than full system UI suppression** — maintaining a hermetic, fully suppressed status bar across AOSP upgrades and Maestro versions is fragile. Excluding the region entirely is more stable.
- **Paparazzi / Roborazzi rejected** — these tools test individual components without a device. Maestro's role here is end-to-end flow testing (type word → tap translate → verify result), not component-level visual regression. Switching would require a separate tool for each concern.
- **Maestro hook system rejected** — Maestro's `runScript` uses GraalJS and has no access to shell commands or image manipulation. Cropping cannot be done inside a flow.
- **`adb shell screencap` with region rejected** — `screencap` does not support capturing a sub-region natively.

## Consequences

- System UI is excluded from all visual regression tests. Status bar and navigation bar regressions will not be caught by screenshot tests.
- Insets are queried from `overrideNonDecorInsets` in `dumpsys window displays` for `ROTATION_0`. If the device rotates, or if a future Android version changes the dumpsys format, inset parsing will break silently (resulting in wrong crop dimensions).
- Switching emulator profile, API level, or target may change inset values. Baselines must be regenerated after any such change.
- The `default` AOSP target has no Google APIs. Any future feature requiring Google APIs in the mock flavor (e.g., Firebase, Maps) would require revisiting the emulator target.
- Revisit if: Maestro adds native screenshot region support; the `dumpsys` format changes in a future API level; or Google APIs are required in the mock flavor.
