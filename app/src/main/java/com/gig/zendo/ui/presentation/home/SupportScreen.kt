package com.gig.zendo.ui.presentation.home

import android.content.ActivityNotFoundException
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.NavController

private const val SUPPORT_EMAIL = "minhdan190904@gmail.com"
private const val PRIVACY_URL = "https://minhdan190904.github.io/zendo-privacy/"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupportScreen(
    navController: NavController,
    onAccountCancellation: (() -> Unit)? = null,
) {
    val context = LocalContext.current
    val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
    val versionName = packageInfo.versionName

    fun openUrl(url: String) {
        try { context.startActivity(Intent(Intent.ACTION_VIEW, url.toUri())) }
        catch (_: ActivityNotFoundException) {
            Toast.makeText(context, "Không có ứng dụng trình duyệt", Toast.LENGTH_SHORT).show()
        }
    }
    fun sendEmail(to: String, subject: String) {
        val i = Intent(Intent.ACTION_SENDTO).apply {
            data = "mailto:".toUri()
            putExtra(Intent.EXTRA_EMAIL, arrayOf(to))
            putExtra(Intent.EXTRA_SUBJECT, subject)
        }
        try { context.startActivity(i) }
        catch (_: ActivityNotFoundException) {
            Toast.makeText(context, "Không có ứng dụng email", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Giới thiệu") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Quay lại")
                    }
                }
            )
        }
    ) { inner ->
        Column(
            modifier = Modifier
                .padding(inner)
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            SettingRow(
                title = "Chính sách bảo mật",
                trailing = { Icon(Icons.AutoMirrored.Outlined.KeyboardArrowRight, null) },
                onClick = { openUrl(PRIVACY_URL) }
            )

            SettingRow(
                title = "Góp ý",
                trailing = { Icon(Icons.Outlined.Email, null) },
                onClick = { sendEmail(SUPPORT_EMAIL, "Góp ý cho ứng dụng") }
            )

            SettingRow(
                title = "Liên hệ",
                supporting = SUPPORT_EMAIL,
                trailing = { Icon(Icons.Outlined.Email, null) },
                onClick = { sendEmail(SUPPORT_EMAIL, "Hỗ trợ kỹ thuật") }
            )

            SettingRow(
                title = "Phiên bản ứng dụng",
                trailing = {
                    Text("v$versionName", style = MaterialTheme.typography.bodyMedium)
                }
            )

            Spacer(Modifier.height(24.dp))

            TextButton(
                onClick = {
                    onAccountCancellation?.invoke()
                        ?: Toast.makeText(context, "Chưa hỗ trợ xóa tài khoản", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) { Text("Hủy tài khoản", fontWeight = FontWeight.SemiBold) }

            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun SettingRow(
    title: String,
    supporting: String? = null,
    trailing: @Composable (() -> Unit)? = null,
    onClick: (() -> Unit)? = null,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = onClick != null) { onClick?.invoke() }
            .padding(vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = 12.dp)
        ) {
            Text(title, style = MaterialTheme.typography.bodyLarge)
            if (supporting != null) {
                Spacer(Modifier.height(2.dp))
                Text(
                    supporting,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        if (trailing != null) trailing()
    }
}