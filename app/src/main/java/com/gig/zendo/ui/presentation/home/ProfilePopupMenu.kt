package com.gig.zendo.ui.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gig.zendo.R
import com.gig.zendo.domain.model.User


private val AvatarSize = 40.dp
private val MenuWidth = 240.dp
private val MenuRadius = 12.dp
private val PillRadius = 8.dp
private val ItemIconSize = 20.dp

@Composable
fun ProfilePopupMenu(
    onUpgradeProClick: () -> Unit,
    onSupportClick: () -> Unit,
    onLogoutClick: () -> Unit,
    currentUser: User,
) {
    val cs = MaterialTheme.colorScheme
    val tp = MaterialTheme.typography

    var expanded by remember { mutableStateOf(false) }

    // Lấy initial từ email (null-safe)
    val email = currentUser.email.orEmpty()
    val initial = email.firstOrNull()?.uppercase() ?: "?"

    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.CenterEnd
    ) {
        Column(horizontalAlignment = Alignment.End) {

            // Avatar nút mở menu: dùng primary có alpha + viền onSurfaceVariant nhạt
            Box(
                modifier = Modifier
                    .size(AvatarSize)
                    .clip(CircleShape)
                    .background(cs.secondary)
                    .border(width = 3.dp, color = Color.LightGray, shape = CircleShape)
                    .clickable(
                        role = Role.Button,
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { expanded = true },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = initial,
                    style = tp.titleSmall.copy(fontSize = 18.sp),
                    color = cs.onSecondary
                )
            }

            Spacer(Modifier.height(8.dp))

            DropdownMenu(
                modifier = Modifier
                    .width(MenuWidth)
                    .background(cs.surface, RoundedCornerShape(MenuRadius))
                    .padding(12.dp),
                expanded = expanded,
                onDismissRequest = { expanded = false },
                containerColor = cs.surface,
                tonalElevation = 2.dp,
                shadowElevation = 4.dp,
                shape = RoundedCornerShape(MenuRadius)
            ) {
                // Header user
                Text(
                    text = currentUser.getUsernameByEmail(),
                    style = tp.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                    color = cs.onSurface
                )
                Text(
                    text = email.ifEmpty { "No email" },
                    style = tp.bodySmall,
                    color = cs.onSurfaceVariant
                )

                Spacer(Modifier.height(12.dp))
                HorizontalDivider(thickness = 1.dp, color = cs.outlineVariant)
                Spacer(Modifier.height(8.dp))

                // Nâng cấp Pro
                DropdownMenuItem(
                    text = { Text("Chat bot", style = tp.bodyMedium, color = cs.onSurface) },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.ic_robot),
                            contentDescription = null,
                            modifier = Modifier.size(ItemIconSize),
                            tint = cs.primary
                        )
                    },
                    onClick = {
                        expanded = false
                        onUpgradeProClick()
                    }
                )

                // Hỗ trợ
                DropdownMenuItem(
                    text = { Text("Hỗ trợ", style = tp.bodyMedium, color = cs.onSurface) },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.ic_support),
                            contentDescription = null,
                            modifier = Modifier.size(ItemIconSize),
                            tint = cs.secondary
                        )
                    },
                    onClick = {
                        expanded = false
                        onSupportClick()
                    }
                )

                // Đăng xuất (dùng error color)
                DropdownMenuItem(
                    text = { Text("Đăng xuất", style = tp.bodyMedium, color = cs.error) },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.ic_logout),
                            contentDescription = null,
                            modifier = Modifier.size(ItemIconSize),
                            tint = cs.error
                        )
                    },
                    onClick = {
                        expanded = false
                        onLogoutClick()
                    }
                )
            }
        }
    }
}