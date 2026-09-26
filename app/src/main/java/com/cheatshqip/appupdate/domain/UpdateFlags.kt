package com.cheatshqip.appupdate.domain

data class UpdateFlags(
    val kind: UpdateKind,
    val availableVersionCode: VersionCode,
    val readyToInstall: Boolean,
)
