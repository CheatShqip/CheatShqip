package com.cheatshqip.appupdate.application.port.input

sealed interface InstallUpdateResult {
    data object Installing : InstallUpdateResult
    data object Failed : InstallUpdateResult
}
