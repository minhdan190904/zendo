package com.gig.zendo.utils

import android.app.Activity
import android.content.Intent
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.InstallState
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

class InAppUpdateHelper(private val activity: Activity) {

    private val appUpdateManager: AppUpdateManager =
        AppUpdateManagerFactory.create(activity)

    private val _downloaded: MutableSharedFlow<Unit> = MutableSharedFlow(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val downloaded: SharedFlow<Unit> = _downloaded

    private val listener: InstallStateUpdatedListener = InstallStateUpdatedListener { state: InstallState ->
        when (state.installStatus()) {
            InstallStatus.DOWNLOADED -> {
                // Bản cập nhật đã tải xong (Flexible)
                _downloaded.tryEmit(Unit)
            }
            InstallStatus.INSTALLED -> {
                // Hoàn tất
                unregister()
            }
            else -> Unit
        }
    }

    fun checkAndLaunch(@AppUpdateType updateType: Int = AppUpdateType.FLEXIBLE): Unit {
        // Lắng nghe trạng thái download cho Flexible
        if (updateType == AppUpdateType.FLEXIBLE) {
            register()
        }

        appUpdateManager.appUpdateInfo.addOnSuccessListener { info ->
            val available: Boolean = info.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
            val allowed: Boolean = info.isUpdateTypeAllowed(updateType)
            if (available && allowed) {
                appUpdateManager.startUpdateFlowForResult(
                    info,
                    activity,
                    AppUpdateOptions.newBuilder(updateType).build(),
                    REQUEST_CODE
                )
            }
        }
    }

    fun onResumeResumeUpdate(@AppUpdateType updateType: Int = AppUpdateType.IMMEDIATE): Unit {
        // Nếu IMMEDIATE đang dở, resume tiếp khi user quay lại app
        appUpdateManager.appUpdateInfo.addOnSuccessListener { info ->
            val inProgress: Boolean =
                info.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS
            if (inProgress) {
                appUpdateManager.startUpdateFlowForResult(
                    info,
                    activity,
                    AppUpdateOptions.newBuilder(updateType).build(),
                    REQUEST_CODE
                )
            }
        }
    }

    fun completeUpdate(): Unit {
        // Gọi khi Flexible đã tải xong để cài đặt (sẽ khởi động lại app)
        appUpdateManager.completeUpdate()
    }

    fun unregister(): Unit {
        try {
            appUpdateManager.unregisterListener(listener)
        } catch (_: Exception) {
            // ignore
        }
    }

    private fun register(): Unit {
        try {
            appUpdateManager.registerListener(listener)
        } catch (_: Exception) {
            // ignore
        }
    }

    fun handleActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Unit {
        // Không bắt buộc xử lý, nhưng bạn có thể log/telemetry tại đây nếu cần
        // requestCode == REQUEST_CODE
    }

    companion object {
        const val REQUEST_CODE: Int = 1001
    }
}
