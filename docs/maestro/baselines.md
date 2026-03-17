# Screenshot Baseline Storage

Baselines are the reference PNGs that `screenshot_test.sh` diffs actual screenshots against.
They live in `.maestro/screenshots/baselines/` and currently (~5 screens) are small binary files.

## Comparison

| | Git LFS | S3 / GCS | Git submodule | ORAS / ghcr.io |
|---|---|---|---|---|
| External infra | None (GitHub) | AWS/GCS account + IAM | None (GitHub) | None (GitHub) |
| Auth in CI | `GITHUB_TOKEN` | OIDC role | PAT (private repo) | `GITHUB_TOKEN` |
| Update UX | commit + push | one sync command | two-commit workflow | one `oras push` |
| Rollback | `git revert` | bucket versioning | `git revert` | immutable SHA tag |
| Familiarity | High | High | Medium | Low |
| Cost | Free (1 GB/mo) | Small ($) | Free | Free (within limits) |
| Binary diff in PR | No | No | No | No |

**Recommendation at current scale**: Git LFS — zero infrastructure, baselines are
code-reviewed like any other change, and the update workflow is a single commit.
ORAS/ghcr.io is the best no-infrastructure alternative if you want to keep binaries
out of Git entirely.

---

## Option 1: Git LFS (recommended)

Baselines stay in the repo but their content is stored in GitHub's LFS object store.
Git history tracks only pointers; the actual PNGs are fetched on demand.

### Setup

```bash
git lfs install
git lfs track ".maestro/screenshots/baselines/*.png"
git add .gitattributes
git add .maestro/screenshots/baselines/
git commit -m "chore: migrate baselines to Git LFS"
```

### CI integration

Add `lfs: true` to the checkout step — that's it:

```yaml
- uses: actions/checkout@v4
  with:
    lfs: true
```

For the screenshot workflow (runs `.maestro/screenshot_test.sh`):

```yaml
# .github/workflows/screenshots.yml
name: Screenshot tests

on: [pull_request]

jobs:
  screenshots:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
        with:
          lfs: true

      - uses: actions/setup-java@v4
        with:
          distribution: temurin
          java-version: 25

      - uses: gradle/actions/setup-gradle@v4

      - name: Start WireMock
        run: |
          java -jar .wiremock/wiremock-standalone.jar \
            --port 9090 --root-dir .wiremock &
          for i in $(seq 1 10); do
            curl -sf http://localhost:9090/__admin/health && break; sleep 1
          done

      - uses: reactivecircus/android-emulator-runner@v2
        with:
          api-level: 35
          arch: x86_64
          profile: pixel_6
          script: ./.maestro/screenshot_test.sh

      - name: Upload diffs on failure
        if: failure()
        uses: actions/upload-artifact@v4
        with:
          name: screenshot-diffs
          path: .maestro/screenshots/diffs/
```

### Updating baselines

Baseline updates are intentional and require a PR:

```bash
./.maestro/screenshot_test.sh --update-baselines
git add .maestro/screenshots/baselines/
git commit -m "chore: update screenshot baselines"
git push
```

A separate `workflow_dispatch` workflow can automate this from CI:

```yaml
# .github/workflows/update-baselines.yml
name: Update screenshot baselines

on:
  workflow_dispatch:

jobs:
  update:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
        with:
          lfs: true
          token: ${{ secrets.GITHUB_TOKEN }}

      # ... same emulator + WireMock setup as above ...

      - uses: reactivecircus/android-emulator-runner@v2
        with:
          api-level: 35
          arch: x86_64
          profile: pixel_6
          script: ./.maestro/screenshot_test.sh --update-baselines

      - name: Commit updated baselines
        run: |
          git config user.name "github-actions[bot]"
          git config user.email "github-actions[bot]@users.noreply.github.com"
          git add .maestro/screenshots/baselines/
          git diff --cached --quiet || git commit -m "chore: update screenshot baselines"
          git push
```

### Limits

GitHub free tier: 1 GB storage, 1 GB bandwidth/month. At ~5 PNGs per update cycle
this will not be a concern for a long time.

---

## Option 2: S3 / GCS bucket

Baselines live outside Git, keyed by branch name. No Git history bloat; no LFS quotas.
Requires cloud infrastructure provisioning.

### Storage layout

```
s3://my-bucket/screenshot-baselines/<branch-name>/home_screen_initial.png
s3://my-bucket/screenshot-baselines/main/translate_word_results.png
```

Use `${{ github.head_ref || github.ref_name }}` as the branch key. Note:
`github.ref_name` returns `<pr_number>/merge` for `pull_request` events — always
prefer `github.head_ref` for PR-triggered runs.

### CI download step

```yaml
- name: Configure AWS credentials
  uses: aws-actions/configure-aws-credentials@v4
  with:
    role-to-assume: arn:aws:iam::ACCOUNT_ID:role/ci-screenshot-role
    aws-region: eu-central-1
  # requires: permissions: id-token: write

- name: Download baselines
  run: |
    BRANCH="${{ github.head_ref || github.ref_name }}"
    aws s3 sync \
      "s3://my-bucket/screenshot-baselines/${BRANCH}/" \
      .maestro/screenshots/baselines/ \
      --exact-timestamps
    # Fall back to main if branch has no baselines yet
    if [ -z "$(ls -A .maestro/screenshots/baselines/)" ]; then
      aws s3 sync \
        "s3://my-bucket/screenshot-baselines/main/" \
        .maestro/screenshots/baselines/
    fi
```

GCS equivalent: replace `aws s3 sync` with `gcloud storage rsync --recursive` and
authenticate via `google-github-actions/auth@v3` with Workload Identity Federation.

### Updating baselines

```yaml
- name: Upload updated baselines
  if: github.event_name == 'workflow_dispatch'
  run: |
    BRANCH="${{ github.head_ref || github.ref_name }}"
    aws s3 sync \
      .maestro/screenshots/baselines/ \
      "s3://my-bucket/screenshot-baselines/${BRANCH}/" \
      --delete --exact-timestamps
```

### IAM

The CI role needs `s3:GetObject`, `s3:PutObject`, `s3:DeleteObject`, `s3:ListBucket`
on the bucket prefix. Use OIDC federation — no long-lived keys required.

### Gotchas

- After a fresh `git checkout`, all file timestamps are the checkout time. Use
  `--exact-timestamps` or `--size-only` to avoid re-uploading unchanged PNGs.
- If two branches update the same baseline, the last `sync --delete` wins — no
  conflict detection.

---

## Option 3: Git submodule (dedicated baselines repo)

A separate repository holds only PNGs. The main repo references it as a submodule
pinned to a specific commit. Strongest auditability; most cumbersome update flow.

### Setup

```bash
git submodule add https://github.com/org/cheatshqip-baselines \
  .maestro/screenshots/baselines
```

`.gitmodules`:
```ini
[submodule "screenshot-baselines"]
    path = .maestro/screenshots/baselines
    url = https://github.com/org/cheatshqip-baselines.git
    branch = main
```

### CI download step

```yaml
- uses: actions/checkout@v4
  with:
    submodules: true
    # For private baselines repo, replace with a PAT:
    # token: ${{ secrets.BASELINES_REPO_PAT }}
```

The baselines are checked out at the commit SHA recorded in the main repo's tree —
no extra download step needed.

### Updating baselines

Two commits are always required:

```bash
# 1. Commit in the baselines repo
cd .maestro/screenshots/baselines
git checkout -b update-baselines
git add .
git commit -m "update baselines for <feature>"
git push origin update-baselines
# open PR in baselines repo, merge to main

# 2. Bump the submodule pointer in the main repo
cd ../../../../
git add .maestro/screenshots/baselines
git commit -m "chore: bump baselines submodule"
git push
```

### Gotchas

- `GITHUB_TOKEN` cannot push to or clone private repos outside the current
  workflow's repo. A PAT with `repo` scope is required for private baselines repos.
- The submodule pointer tracks a pinned commit, not the tip of `main`. If you push
  new baselines to the baselines repo without bumping the pointer, CI silently uses
  the old baselines.
- Git is not optimized for binary storage: each PNG version is stored in full.
  The baselines repo will grow unboundedly; periodically squash its history.

---

## Option 4: OCI artifacts via ORAS on ghcr.io

Baselines are stored as OCI artifact blobs in GitHub Container Registry. No Git
history bloat, no external cloud account — `GITHUB_TOKEN` with `packages: write`
is sufficient.

### Storage layout

```
ghcr.io/org/cheatshqip-baselines:main
ghcr.io/org/cheatshqip-baselines:feature-foo
ghcr.io/org/cheatshqip-baselines:main-abc1234   # immutable SHA tag
```

Branch names with `/` must be sanitized (OCI tags disallow `/`):
```bash
TAG=$(echo "${BRANCH}" | tr '/' '-' | tr -cd '[:alnum:]-_.')
```

### CI download step

```yaml
- name: Install ORAS
  uses: oras-project/setup-oras@v1

- name: Authenticate to ghcr.io
  run: echo "${{ secrets.GITHUB_TOKEN }}" | oras login ghcr.io -u ${{ github.actor }} --password-stdin

- name: Pull baselines
  run: |
    BRANCH="${{ github.head_ref || github.ref_name }}"
    TAG=$(echo "${BRANCH}" | tr '/' '-' | tr -cd '[:alnum:]-_.')
    mkdir -p .maestro/screenshots/baselines
    cd .maestro/screenshots/baselines
    oras pull "ghcr.io/${{ github.repository_owner }}/cheatshqip-baselines:${TAG}" \
      || oras pull "ghcr.io/${{ github.repository_owner }}/cheatshqip-baselines:main"
```

`oras pull` extracts all layers into the current directory flat — fine for ~5 PNGs.

### Updating baselines

```yaml
permissions:
  contents: read
  packages: write

- name: Push updated baselines
  if: github.event_name == 'workflow_dispatch'
  run: |
    BRANCH="${{ github.head_ref || github.ref_name }}"
    TAG=$(echo "${BRANCH}" | tr '/' '-' | tr -cd '[:alnum:]-_.')
    cd .maestro/screenshots/baselines
    oras push \
      "ghcr.io/${{ github.repository_owner }}/cheatshqip-baselines:${TAG}" \
      --artifact-type application/vnd.cheatshqip.screenshot-baselines \
      home_screen_initial.png:image/png \
      translate_word_results.png:image/png
```

For immutable history, also push a SHA-tagged copy:
```bash
oras push "ghcr.io/.../cheatshqip-baselines:${TAG}-${{ github.sha }}" ...
```

### Gotchas

- `github.repository_owner` may contain uppercase; ghcr.io requires lowercase image
  names. Explicitly lowercase the owner when building the image reference.
- From a fork-triggered `pull_request` event, `GITHUB_TOKEN` is read-only and cannot
  push packages — this is GitHub's fork security model. Updates must come from
  `workflow_dispatch` or a push to a branch in the main repo.
- OCI tag mutability: pushing to `:main` silently replaces the previous manifest.
  The old blobs remain in storage until GC, but the tag no longer points to them.
  Always push a SHA tag alongside the branch tag if history matters.