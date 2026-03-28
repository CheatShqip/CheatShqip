---
status: accepted
date: 2026-03-28
---

# ADR 001: Use ImageMagick for screenshot diffing instead of Maestro `assertScreenshot`

## Context

We use Maestro for E2E flows and take screenshots at key points to catch visual regressions.
We evaluated replacing our custom ImageMagick-based diffing pipeline with Maestro's built-in
`assertScreenshot` command (available since Maestro CLI 2.2.0).

## Decision

Keep ImageMagick (`magick compare`) for screenshot diffing. Do not adopt `assertScreenshot`.

## Reasons

**No diff image output on failure.** `assertScreenshot` only reports a percentage match and fails
the test. It does not produce a visual diff image (highlighted pixel differences). Our current
pipeline uses `magick compare -metric AE` which generates a diff PNG uploaded as a CI artifact,
making it immediately obvious what changed without requiring manual baseline/actual comparison.

**Less control over thresholding.** Our threshold is expressed in absolute pixel count (default
100 px), which is more intuitive and stable across screen sizes than a percentage match.

## Consequences

- ImageMagick remains a CI dependency (`sudo apt-get install imagemagick`).
- Screenshot diff images continue to be uploaded as GitHub Actions artifacts on every run.
- If Maestro adds diff image output in a future release, this decision should be revisited.
