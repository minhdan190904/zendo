package com.gig.zendo

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.gig.zendo.ui.presentation.navigation.AppNavigation
import com.gig.zendo.ui.theme.ZendoTheme
import com.gig.zendo.utils.InAppUpdateHelper
import com.google.android.play.core.install.model.AppUpdateType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var updateHelper: InAppUpdateHelper

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        updateHelper = InAppUpdateHelper(this)

        setContent {
            ZendoTheme(darkTheme = false) {
                val snackbarHostState = remember { SnackbarHostState() }
                val scope = rememberCoroutineScope()

                LaunchedEffect(Unit) {
                    updateHelper.checkAndLaunch(AppUpdateType.FLEXIBLE)
                }

                LaunchedEffect(Unit) {
                    updateHelper.downloaded.collect {
                        val result = snackbarHostState.showSnackbar(
                            message = "Đã tải bản cập nhật",
                            actionLabel = "Khởi động lại",
                            withDismissAction = true,
                            duration = SnackbarDuration.Indefinite
                        )
                        if (result == SnackbarResult.ActionPerformed) {
                            updateHelper.completeUpdate()
                        }
                    }
                }

                // Hủy listener khi composable rời composition
                DisposableEffect(Unit) {
                    onDispose { updateHelper.unregister() }
                }

                Scaffold(
                    snackbarHost = { SnackbarHost(snackbarHostState) }
                ) {
                    AppNavigation()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // Nếu bạn dùng IMMEDIATE ở đâu đó, dòng này sẽ resume flow còn dở
        updateHelper.onResumeResumeUpdate(AppUpdateType.IMMEDIATE)
    }

    @Deprecated("Still fine for Play Core flow result")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        updateHelper.handleActivityResult(requestCode, resultCode, data)
    }
}
