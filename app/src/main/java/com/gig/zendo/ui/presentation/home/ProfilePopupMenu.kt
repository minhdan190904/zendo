package com.gig.zendo.ui.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gig.zendo.R
import com.gig.zendo.domain.model.User

@Composable
fun ProfilePopupMenu(
    onUpgradeProClick: () -> Unit,
    onSupportClick: () -> Unit,
    onLogoutClick: () -> Unit,
    currentUser: User,
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 32.dp, top = 48.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        Column {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Color.Black.copy(0.5f), shape = CircleShape)
                    .border(width = 2.dp, color = Color.LightGray, shape = CircleShape)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { expanded = true }
            ) {
                Text(
                    text = currentUser.email.takeIf { it!!.isNotEmpty() }?.firstOrNull()?.uppercase()
                        ?: "?",
                    fontSize = 20.sp,
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (expanded) {

                DropdownMenu(
                    modifier = Modifier
                        .background(Color.White)
                        .padding(16.dp),
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    Text(
                        text = currentUser.getUsernameByEmail(),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(
                        text = currentUser.email ?: "No email",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    //black alpha background
                    Text(
                        text = "FREE",
                        fontSize = 10.sp,
                        color = Color.Gray,
                        modifier = Modifier
                            .background(
                                Color.Black.copy(alpha = 0.1f),
                                RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 6.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    HorizontalDivider(
                        thickness = 1.dp,
                        color = Color.LightGray,
                        modifier = Modifier.width(224.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    DropdownMenuItem(
                        contentPadding = PaddingValues(0.dp),
                        text = {
                            Row {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_diamond),
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp),
                                    tint = Color.Yellow
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Nâng cấp Pro", fontSize = 14.sp, color = Color.Black)
                            }
                        },
                        onClick = {
                            onUpgradeProClick()
                            expanded = false
                        }
                    )

                    // light blue
                    DropdownMenuItem(
                        contentPadding = PaddingValues(0.dp),
                        text = {
                            Row {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_support),
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp),
                                    tint = Color(0xFF90CAF9) // Light blue color
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Hỗ trợ", fontSize = 14.sp, color = Color.Black)
                            }
                        },
                        onClick = {
                            onSupportClick()
                            expanded = false
                        },
                    )

                    DropdownMenuItem(
                        contentPadding = PaddingValues(0.dp),
                        text = {
                            Row() {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_logout),
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp),
                                    tint = Color.Red
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Đăng xuất", fontSize = 14.sp, color = Color.Red)
                            }
                        },
                        onClick = {
                            onLogoutClick()
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}