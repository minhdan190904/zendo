package com.gig.zendo.ui.presentation.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupportScreen(
    onBack: () -> Unit = {},
    navController: NavController,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Hỗ trợ") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Quay lại",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFFF8E53),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Spacer(Modifier.height(12.dp))
            SupportHeroCard()
            Spacer(Modifier.height(16.dp))
            ContactSection()
            Spacer(Modifier.height(16.dp))
            InfoNoteCard()
            Spacer(Modifier.height(12.dp))
            AppInfoCard(
                versionName = "1.0.0",
                supportHours = "08:00 – 22:00 (GMT+7)",
                website = "https://cretisoft.com"
            )
            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun SupportHeroCard() {
    val gradient = Brush.linearGradient(
        listOf(Color(0xFFFF8E53), Color(0xFFFF6B6B))
    )
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .background(gradient)
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0x1AFFFFFF))
            ) {
                Icon(
                    imageVector = Icons.Outlined.Call,
                    contentDescription = null,
                    tint = Color.White
                )
            }
            Spacer(Modifier.width(16.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    "Hỗ trợ khách hàng",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    "Chúng tôi luôn sẵn sàng hỗ trợ bạn",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFFFFF3F3)
                )
            }
        }
    }
}

@Composable
private fun ContactSection() {
    val ctx = LocalContext.current
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(
                text = "Thông tin liên hệ",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF111827)
            )
            Spacer(Modifier.height(12.dp))

            ContactRow(
                title = "Zalo",
                subtitle = "0348580486",
                icon = Icons.Default.Call,
                iconContainerColor = Color(0xFFE6F0FF),
                contactItemContainerColor = Color(0xFFF6FAFF),
                iconColor = Color(0xFF1F6FEB),
                textContactColor = Color(0xFF1F6FEB),
                onClick = { openZalo(ctx, "0348580486") }
            )
            Spacer(Modifier.height(10.dp))

            ContactRow(
                title = "Email",
                subtitle = "realer190904@gmail.com",
                icon = Icons.Default.Email,
                iconContainerColor = Color(0xFFFFEDEA),
                contactItemContainerColor = Color(0xFFFFF6F6),
                iconColor = Color(0xFFF46666),
                textContactColor = Color(0xFF1F2937),
                onClick = { sendEmail(ctx, "realer190904@gmail.com") }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ContactRow(
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconContainerColor: Color,
    contactItemContainerColor: Color,
    iconColor: Color,
    textContactColor: Color,
    onClick: () -> Unit
) {
    val clipboard = LocalClipboardManager.current
    val ctx = LocalContext.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = contactItemContainerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .combinedClickable(
                    onClick = onClick,
                    onLongClick = {
                        clipboard.setText(AnnotatedString(subtitle))
                        Toast
                            .makeText(ctx, "Đã sao chép: $subtitle", Toast.LENGTH_SHORT)
                            .show()
                    }
                )
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(iconContainerColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = iconColor)
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.SemiBold, color = textContactColor)
                Spacer(Modifier.height(2.dp))
                Text(
                    subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF6B7280)
                )
            }
        }
    }
}

@Composable
private fun InfoNoteCard() {
    val bg = Color(0xFFE9F4FF)
    val border = Color(0xFFC7E2FF)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, border, RoundedCornerShape(16.dp))
            .background(bg, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.Top) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFD7EAFF)),
                contentAlignment = Alignment.Center
            ) { Text("i", color = Color(0xFF1F6FEB), fontWeight = FontWeight.Bold) }

            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text("Lưu ý", fontWeight = FontWeight.SemiBold, color = Color(0xFF1F2937))
                Spacer(Modifier.height(6.dp))
                Text(
                    style = MaterialTheme.typography.bodyMedium,
                    text = "• Hỗ trợ các vấn đề khi mua ứng dụng\n" +
                            "• Đội ngũ của chúng tôi sẽ hỗ trợ bạn trong thời gian sớm nhất\n" +
                            "• Thường phản hồi trong vòng 15 phút\n" +
                            "• Cretisoft - Giải pháp phần mềm sáng tạo\n" +
                            "• Website: https://cretisoft.com",
                    color = Color(0xFF1F6FEB)
                )
            }
        }
    }
}

@Composable
private fun AppInfoCard(
    versionName: String,
    supportHours: String,
    website: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F9FC))
    ) {
        Column(Modifier.padding(16.dp)) {
            Text("Thông tin ứng dụng", fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(8.dp))
            Text("Phiên bản: $versionName", style = MaterialTheme.typography.bodyMedium)
            Text("Giờ hỗ trợ: $supportHours", style = MaterialTheme.typography.bodyMedium)
            Text("Website: $website", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@SuppressLint("QueryPermissionsNeeded")
private fun openZalo(ctx: Context, phone: String) {
    // Chuẩn hoá: chỉ lấy chữ số
    val formatted = phone.filter { it.isDigit() }
    if (formatted.isBlank()) {
        Toast.makeText(ctx, "Số điện thoại không hợp lệ", Toast.LENGTH_SHORT).show()
        return
    }

    // Ưu tiên deep link trong app
    val intentZaloScheme = Intent(
        Intent.ACTION_VIEW,
        Uri.parse("zalo://conversation?phone=$formatted")
    ).apply {
        // Buộc mở đúng app Zalo
        setPackage("com.zing.zalo")
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }

    // Fallback: liên kết web (có thể đẩy vào app nếu Zalo bắt app link)
    val intentZaloWeb = Intent(
        Intent.ACTION_VIEW,
        Uri.parse("https://zalo.me/$formatted")
    ).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }

    val pm = ctx.packageManager
    val toLaunch = when {
        intentZaloScheme.resolveActivity(pm) != null -> intentZaloScheme
        intentZaloWeb.resolveActivity(pm) != null -> intentZaloWeb
        else -> null
    }

    try {
        if (toLaunch != null) {
            ctx.startActivity(toLaunch)
        } else {
            Toast.makeText(ctx, "Không tìm thấy Zalo", Toast.LENGTH_SHORT).show()
        }
    } catch (_: Exception) {
        Toast.makeText(ctx, "Không mở được Zalo", Toast.LENGTH_SHORT).show()
    }
}


private fun sendEmail(ctx: Context, to: String, subject: String? = null) {
    val intent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:")
        putExtra(Intent.EXTRA_EMAIL, arrayOf(to))
        putExtra(Intent.EXTRA_SUBJECT, subject ?: "Hỗ trợ Zendo")
    }
    ctx.startActivity(Intent.createChooser(intent, "Gửi email"))
}

@Preview(showBackground = true)
@Composable
private fun SupportScreenPreview() {
    MaterialTheme(colorScheme = lightColorScheme()) {
        // NavController trong preview chỉ để compile; không dùng thực tế
        SupportScreen(navController = NavController(LocalContext.current))
    }
}
