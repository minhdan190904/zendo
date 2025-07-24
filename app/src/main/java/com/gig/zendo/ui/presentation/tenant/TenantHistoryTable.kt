package com.gig.zendo.ui.presentation.tenant

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gig.zendo.domain.model.Tenant
import com.gig.zendo.ui.theme.DarkGreen

@Composable
fun TenantHistoryTable(
    tenants: List<Tenant>? = null,
    onDetailClick: (Tenant) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            TableHeader("Khách")
            TableHeader("Ngày thuê")
            TableHeader("Ngày trả")
            Spacer(modifier = Modifier.width(60.dp))
        }

        tenants?.forEach { tenant ->
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                color = Color.LightGray
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                TableCell(tenant.name)
                TableCell(tenant.startDate)
                TableCell(tenant.endDate.ifEmpty { "Đang thuê" })

                Text(text = getAnnotatedString("Chi tiết"),
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .clickable { onDetailClick(tenant) },
                    fontSize = 14.sp,
                )

            }
        }
    }
}

fun getAnnotatedString(content: String): AnnotatedString {
    return buildAnnotatedString {
        append(content)
        addStyle(
            style = SpanStyle(
                color = DarkGreen,
                textDecoration = TextDecoration.Underline,
                fontWeight = FontWeight.Bold
            ),
            start = 0,
            end = content.length
        )
    }
}

@Composable
fun RowScope.TableHeader(text: String) {
    Text(
        text = text,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        modifier = Modifier
            .weight(1f)
    )
}

@Composable
fun RowScope.TableCell(text: String) {
    Text(
        text = text,
        fontSize = 14.sp,
        modifier = Modifier
            .weight(1f)
    )
}
