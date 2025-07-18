package com.gig.zendo

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

// Danh sách dữ liệu mẫu
private val instructions = listOf(
    InstructionItem(
        id = 1,
        title = "Vào “Cài đặt”",
        description = "Cài đặt ngày thu tiền, tiền điện nước, tiền phòng. Bước này nên làm đầu tiên trước các thao tác khác",
        iconRes = R.drawable.ic_setting
    ),
    InstructionItem(
        id = 2,
        title = "Thêm phòng và khách",
        description = "Thêm các phòng trong nhà trọ, thêm khách",
        iconRes = R.drawable.ic_add
    ),
    InstructionItem(
        id = 3,
        title = "Ghi điện nước",
        description = "Ghi điện nước cho mỗi phòng, số liệu này có thể dùng để tạo hóa đơn tính tiền",
        iconRes = R.drawable.ic_lightbub
    ),
    InstructionItem(
        id = 4,
        title = "Thu tiền",
        description = "Khi có khách đóng tiền vào phần đóng tiền, tick vào ô của hóa đơn được đóng. Xong cập nhật. Trang này có thể dùng để kiểm soát các hóa đơn đã và chưa đóng tiền của các phòng",
        iconRes = R.drawable.ic_money
    ),
    InstructionItem(
        id = 5,
        title = "Lịch sử",
        description = "Phần này dùng để tra cứu lịch sử của các khách từng thuê một phòng",
        iconRes = R.drawable.ic_history
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InstructionScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Hướng dẫn") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            contentPadding = paddingValues,
            modifier = Modifier.fillMaxSize()
        ) {
            items(instructions) { item ->
                InstructionCard(item = item)
            }
        }
    }
}