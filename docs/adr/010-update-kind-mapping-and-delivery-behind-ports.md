---
status: accepted
date: 2026-09-26
---

# ADR 010: Map update kind and deliver in-app updates behind ports

## Context

The app needs Play In-App Updates with two delivery modes: a blocking (immediate)
update that gates the app until it is updated, and an incentive (flexible) update that
prompts dismissibly, downloads in the background, and offers a restart when ready.

Play's API only exposes an `AppUpdateInfo` with an integer release priority (0-5) set
in Play Console; the choice of "blocking vs incentive" is ours to derive from it. Play
concepts (`AppUpdateInfo`, `AppUpdateOptions`, `ActivityResult` codes, `Activity`) must
not leak into domain or application code, and the feature may one day be delivered by
something other than Play (e.g. a different store or a self-update mechanism).

## Decision

Speak only `UpdateKind.Blocking` / `UpdateKind.Incentive` in domain and application
code. Map Play's update priority to `UpdateKind` inside the Play output adapter
(`PlayUpdateInfoMapper` + `PlayUpdateKindThresholds`), and put all Play delivery behind
three single-method output ports: `GetUpdateFlagsPort`, `StartUpdateDeliveryPort`,
`InstallUpdateDeliveryPort`.

## Reasons

- **Provider swap cost is bounded**: replacing Play means writing 3 adapters plus DI
  bindings (`appupdate/di/AppUpdateModule.kt`); ports, services, ViewModel, and UI stay
  untouched.
- **Priority is a Play Console implementation detail**: domain speaks update intent
  (`Blocking`/`Incentive`); the threshold mapping (`blockingFromPriority = 4`,
  `incentiveFromPriority = 1`) lives in `PlayUpdateKindThresholds`, bound in Koin as the
  single tunable place. A future provider expresses the same intent differently without
  a domain change.
- **Expected provider failures are result variants, never exceptions**: `Failed`,
  `NotDeliverable`, `Declined` are sealed result states on the ports, so sideloaded
  builds and throttled Play prompts degrade silently (`Hidden`, no nag) instead of
  crashing.
- Rejected: encoding priority thresholds in the domain — rejected, it bakes Play's
  release mechanics into business language.
- Rejected: passing `AppUpdateInfo` through ports — rejected, it leaks the SDK across
  the boundary and forces Play types into tests.

## Consequences

- The mapping is unit-tested at its boundary (`PlayUpdateInfoMapperTest`: priority
  thresholds, `DOWNLOADED` forcing `readyToInstall` + `Incentive`, update-type and
  result-code mapping); the thin Task/`ActivityResultRegistry`/`completeUpdate()`
  plumbing is covered by manual QA on the internal testing track.
- Invariant to preserve: a completed flexible download is never `Blocking`
  (`installStatus == DOWNLOADED` forces `kind = Incentive`), so the restart snackbar
  always wins over the blocking gate.
- Revisit if: a second update provider is actually adopted (then extract the mapping
  strategy per provider), or if Play's flexible-completion watch needs to move from the
  ViewModel's 2 s poll to a `Flow`-based watch port.
