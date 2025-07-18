package com.gig.zendo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateHouseScreen(
    houseName: String,
    onHouseNameChange: (String) -> Unit,
    houseAddress: String,
    onHouseAddressChange: (String) -> Unit,
    onSaveClick: () -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tạo nhà") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Quay lại",
                            tint = Color.Black,
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black,
            )
                )
        },
        containerColor = Color.White
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Card(
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
            ) {
                Column {
                    // Header màu hồng
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFFFA598))
                            .padding(vertical = 12.dp, horizontal = 16.dp)
                    ) {
                        Text(
                            text = "Thông tin nhà",
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 16.sp),
                            color = Color.Black
                        )
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                            .padding(horizontal = 16.dp, vertical = 24.dp)
                    ) {
                        Text(
                            text = "Tên nhà",
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 16.sp),
                            color = Color.Black
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        // Form nhập tên nhà
                        OutlinedTextField(
                            value = houseName,
                            onValueChange = onHouseNameChange,
                            modifier = Modifier
                                .fillMaxWidth(),
                            placeholder = { Text("Ví dụ: Nhà trọ Tràng Dài") },
                            singleLine = true,
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Địa chỉ",
                            color = Color.Black,
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 16.sp),
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        // Form nhập địa chỉ
                        OutlinedTextField(
                            value = houseAddress,
                            onValueChange = onHouseAddressChange,
                            modifier = Modifier
                                .fillMaxWidth(),
                            placeholder = { Text("Ví dụ: 18/158 Tràng Dài") },
                            singleLine = true,
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        // Nút Lưu lại
                        Button(
                            onClick = onSaveClick,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00796B)),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .height(48.dp)
                                .align(Alignment.CenterHorizontally)
                        ) {
                            Text(
                                "Lưu lại",
                                color = Color.White,
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }
                }
            }
        }
    }
}
