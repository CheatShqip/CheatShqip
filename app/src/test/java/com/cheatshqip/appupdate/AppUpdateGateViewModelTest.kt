package com.cheatshqip.appupdate

import com.cheatshqip.appupdate.application.port.input.CheckForUpdateResult
import com.cheatshqip.appupdate.application.port.input.InstallUpdateResult
import com.cheatshqip.appupdate.application.port.input.StartUpdateResult
import com.cheatshqip.appupdate.domain.UpdateFlags
import com.cheatshqip.appupdate.domain.UpdateKind
import com.cheatshqip.appupdate.domain.VersionCode
import com.cheatshqip.appupdate.presentation.AppUpdateGateUIState
import com.cheatshqip.appupdate.presentation.AppUpdateGateViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalCoroutinesApi::class)
class AppUpdateGateViewModelTest {
    private val testDispatcher = UnconfinedTestDispatcher()
    private val checkForUpdateUseCase = FakeCheckForUpdateUseCase()
    private val startUpdateUseCase = FakeStartUpdateUseCase()
    private val installUpdateUseCase = FakeInstallUpdateUseCase()

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `given check UpdateAvailable Blocking, should show BlockingUpdate`() =
        runGateTest {
            checkForUpdateUseCase.result = CheckForUpdateResult.UpdateAvailable(
                flags(kind = UpdateKind.Blocking),
            )
            val viewModel = createViewModel()
            viewModel.state.launchIn(backgroundScope)

            assertEquals(AppUpdateGateUIState.BlockingUpdate(VersionCode(7)), viewModel.state.value)
        }

    @Test
    fun `given check UpdateAvailable Incentive, should show IncentivePrompt`() =
        runGateTest {
            checkForUpdateUseCase.result = CheckForUpdateResult.UpdateAvailable(
                flags(kind = UpdateKind.Incentive),
            )
            val viewModel = createViewModel()
            viewModel.state.launchIn(backgroundScope)

            assertEquals(AppUpdateGateUIState.IncentivePrompt(VersionCode(7)), viewModel.state.value)
        }

    @Test
    fun `given check UpdateAvailable readyToInstall, should show ReadyToInstall`() =
        runGateTest {
            checkForUpdateUseCase.result = CheckForUpdateResult.UpdateAvailable(
                flags(readyToInstall = true),
            )
            val viewModel = createViewModel()
            viewModel.state.launchIn(backgroundScope)

            assertEquals(AppUpdateGateUIState.ReadyToInstall(VersionCode(7)), viewModel.state.value)
        }

    @Test
    fun `given check NoUpdate, should stay Hidden`() =
        runGateTest {
            checkForUpdateUseCase.result = CheckForUpdateResult.NoUpdate
            val viewModel = createViewModel()
            viewModel.state.launchIn(backgroundScope)

            assertEquals(AppUpdateGateUIState.Hidden, viewModel.state.value)
        }

    @Test
    fun `given check CheckFailed, should stay Hidden`() =
        runGateTest {
            checkForUpdateUseCase.result = CheckForUpdateResult.CheckFailed
            val viewModel = createViewModel()
            viewModel.state.launchIn(backgroundScope)

            assertEquals(AppUpdateGateUIState.Hidden, viewModel.state.value)
        }

    @Test
    fun `given blocking start Started, should keep gate BlockingUpdate`() =
        runGateTest {
            val viewModel = givenGate(flags(kind = UpdateKind.Blocking))
            startUpdateUseCase.result = StartUpdateResult.Started

            viewModel.onStartBlockingUpdate()

            assertEquals(AppUpdateGateUIState.BlockingUpdate(VersionCode(7)), viewModel.state.value)
            assertEquals(UpdateKind.Blocking, startUpdateUseCase.lastKind)
        }

    @Test
    fun `given blocking start Declined, should keep gate BlockingUpdate`() =
        runGateTest {
            val viewModel = givenGate(flags(kind = UpdateKind.Blocking))
            startUpdateUseCase.result = StartUpdateResult.Declined

            viewModel.onStartBlockingUpdate()

            assertEquals(AppUpdateGateUIState.BlockingUpdate(VersionCode(7)), viewModel.state.value)
        }

    @Test
    fun `given blocking start NotDeliverable, should show BlockingUpdateNotDeliverable`() =
        runGateTest {
            val viewModel = givenGate(flags(kind = UpdateKind.Blocking))
            startUpdateUseCase.result = StartUpdateResult.NotDeliverable

            viewModel.onStartBlockingUpdate()

            assertEquals(
                AppUpdateGateUIState.BlockingUpdateNotDeliverable(VersionCode(7)),
                viewModel.state.value,
            )
        }

    @Test
    fun `given blocking start Failed, should show BlockingUpdateNotDeliverable`() =
        runGateTest {
            val viewModel = givenGate(flags(kind = UpdateKind.Blocking))
            startUpdateUseCase.result = StartUpdateResult.Failed

            viewModel.onStartBlockingUpdate()

            assertEquals(
                AppUpdateGateUIState.BlockingUpdateNotDeliverable(VersionCode(7)),
                viewModel.state.value,
            )
        }

    @Test
    fun `given blocking gate not deliverable and start retried, should show BlockingUpdate`() =
        runGateTest {
            val viewModel = givenGate(flags(kind = UpdateKind.Blocking))
            startUpdateUseCase.result = StartUpdateResult.NotDeliverable
            viewModel.onStartBlockingUpdate()

            startUpdateUseCase.result = StartUpdateResult.Started
            viewModel.onStartBlockingUpdate()

            assertEquals(AppUpdateGateUIState.BlockingUpdate(VersionCode(7)), viewModel.state.value)
        }

    @Test
    fun `given incentive accepted and Started, should show IncentiveDownloading`() =
        runGateTest {
            val viewModel = givenGate(flags(kind = UpdateKind.Incentive))
            startUpdateUseCase.result = StartUpdateResult.Started

            viewModel.onIncentiveAccepted()

            assertEquals(AppUpdateGateUIState.IncentiveDownloading(VersionCode(7)), viewModel.state.value)
            assertEquals(UpdateKind.Incentive, startUpdateUseCase.lastKind)
        }

    @Test
    fun `given incentive start Declined, should hide`() =
        runGateTest {
            val viewModel = givenGate(flags(kind = UpdateKind.Incentive))
            startUpdateUseCase.result = StartUpdateResult.Declined

            viewModel.onIncentiveAccepted()

            assertEquals(AppUpdateGateUIState.Hidden, viewModel.state.value)
        }

    @Test
    fun `given incentive start NotDeliverable, should hide`() =
        runGateTest {
            val viewModel = givenGate(flags(kind = UpdateKind.Incentive))
            startUpdateUseCase.result = StartUpdateResult.NotDeliverable

            viewModel.onIncentiveAccepted()

            assertEquals(AppUpdateGateUIState.Hidden, viewModel.state.value)
        }

    @Test
    fun `given incentive start Failed, should hide`() =
        runGateTest {
            val viewModel = givenGate(flags(kind = UpdateKind.Incentive))
            startUpdateUseCase.result = StartUpdateResult.Failed

            viewModel.onIncentiveAccepted()

            assertEquals(AppUpdateGateUIState.Hidden, viewModel.state.value)
        }

    @Test
    fun `given incentive declined, should hide for the session`() =
        runGateTest {
            val viewModel = givenGate(flags(kind = UpdateKind.Incentive))

            viewModel.onIncentiveDeclined()
            viewModel.onForeground()

            assertEquals(AppUpdateGateUIState.Hidden, viewModel.state.value)
            assertEquals(2, checkForUpdateUseCase.checkCount)
        }

    @Test
    fun `given IncentiveDownloading and poll sees readyToInstall, should show ReadyToInstall`() =
        runGateTest {
            val viewModel = givenGate(flags(kind = UpdateKind.Incentive))
            startUpdateUseCase.result = StartUpdateResult.Started
            viewModel.onIncentiveAccepted()

            checkForUpdateUseCase.result = CheckForUpdateResult.UpdateAvailable(
                flags(readyToInstall = true),
            )
            advanceUntilIdle()

            assertEquals(AppUpdateGateUIState.ReadyToInstall(VersionCode(7)), viewModel.state.value)
        }

    @Test
    fun `given IncentiveDownloading and poll sees no readyToInstall, should stay IncentiveDownloading`() =
        runGateTest {
            val viewModel = givenGate(flags(kind = UpdateKind.Incentive))
            startUpdateUseCase.result = StartUpdateResult.Started
            viewModel.onIncentiveAccepted()

            advanceTimeBy(2.seconds.inWholeMilliseconds + 1)

            assertEquals(AppUpdateGateUIState.IncentiveDownloading(VersionCode(7)), viewModel.state.value)
        }

    @Test
    fun `given install Failed, should keep ReadyToInstall for retry`() =
        runGateTest {
            val viewModel = givenGate(flags(readyToInstall = true))
            installUpdateUseCase.result = InstallUpdateResult.Failed

            viewModel.onInstallUpdate()

            assertEquals(AppUpdateGateUIState.ReadyToInstall(VersionCode(7)), viewModel.state.value)
            assertEquals(1, installUpdateUseCase.installCount)
        }

    @Test
    fun `given install Installing, should hide and wait for restart`() =
        runGateTest {
            val viewModel = givenGate(flags(readyToInstall = true))
            installUpdateUseCase.result = InstallUpdateResult.Installing

            viewModel.onInstallUpdate()

            assertEquals(AppUpdateGateUIState.Hidden, viewModel.state.value)
        }

    @Test
    fun `given foreground, should re-check`() =
        runGateTest {
            checkForUpdateUseCase.result = CheckForUpdateResult.CheckFailed
            val viewModel = createViewModel()
            viewModel.state.launchIn(backgroundScope)

            checkForUpdateUseCase.result = CheckForUpdateResult.UpdateAvailable(
                flags(kind = UpdateKind.Blocking),
            )
            viewModel.onForeground()

            assertEquals(AppUpdateGateUIState.BlockingUpdate(VersionCode(7)), viewModel.state.value)
            assertEquals(2, checkForUpdateUseCase.checkCount)
        }

    @Test
    fun `given no known version, should ignore update actions`() =
        runGateTest {
            checkForUpdateUseCase.result = CheckForUpdateResult.CheckFailed
            val viewModel = createViewModel()
            viewModel.state.launchIn(backgroundScope)

            viewModel.onStartBlockingUpdate()
            viewModel.onIncentiveAccepted()

            assertNull(startUpdateUseCase.lastKind)
            assertEquals(AppUpdateGateUIState.Hidden, viewModel.state.value)
        }

    private fun runGateTest(testBody: suspend TestScope.() -> Unit) {
        runTest(testDispatcher) {
            try {
                testBody()
            } finally {
                checkForUpdateUseCase.result = CheckForUpdateResult.UpdateAvailable(
                    flags(readyToInstall = true),
                )
                advanceUntilIdle()
            }
        }
    }

    private fun TestScope.givenGate(updateFlags: UpdateFlags): AppUpdateGateViewModel {
        checkForUpdateUseCase.result = CheckForUpdateResult.UpdateAvailable(updateFlags)
        val viewModel = createViewModel()
        viewModel.state.launchIn(backgroundScope)
        return viewModel
    }

    private fun createViewModel() = AppUpdateGateViewModel(
        coroutineDispatcher = testDispatcher,
        checkForUpdateUseCase = checkForUpdateUseCase,
        startUpdateUseCase = startUpdateUseCase,
        installUpdateUseCase = installUpdateUseCase,
    )

    private fun flags(
        kind: UpdateKind = UpdateKind.Incentive,
        readyToInstall: Boolean = false,
    ) = UpdateFlags(
        kind = kind,
        availableVersionCode = VersionCode(7),
        readyToInstall = readyToInstall,
    )
}
