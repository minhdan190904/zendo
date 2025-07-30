package com.gig.zendo.ui.presentation.invoice


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.gig.zendo.R
import com.gig.zendo.domain.model.Invoice
import com.gig.zendo.domain.model.InvoiceStatus
import com.gig.zendo.ui.common.CustomLoadingProgress
import com.gig.zendo.ui.presentation.tenant.TenantHistoryTable
import com.gig.zendo.ui.theme.DarkGreen
import com.gig.zendo.utils.UiState
import com.gig.zendo.utils.toMoney

@Composable
fun PaymentItem(
    date: String,
    amount: String,
    statusInvoice: InvoiceStatus = InvoiceStatus.NOT_PAID,
    onDetailClick: () -> Unit,
    onDeleteClick: () -> Unit
) {

    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp)
    ) {

        Column(
            modifier = Modifier
                .background(Color(0xFFF8F8F8))
                .padding(12.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        Modifier
                            .background(
                                Color(0xFF42A5F5).copy(0.1f),
                                shape = RoundedCornerShape(24)
                            )
                            .padding(4.dp)
                    ) {
                        Row {
                            Icon(
                                imageVector = Icons.Outlined.DateRange,
                                contentDescription = null,
                                tint = Color(0xFF42A5F5),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = date,
                                color = Color(0xFF42A5F5),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        Modifier
                            .background(
                                Color(if (statusInvoice == InvoiceStatus.PAID) 0xFF4CAF50 else 0xFFD32F2F).copy(
                                    0.1f
                                ), shape = RoundedCornerShape(24)
                            )
                            .padding(4.dp)
                    ) {
                        Icon(
                            imageVector = when (statusInvoice) {
                                InvoiceStatus.PAID -> Icons.Filled.CheckCircle
                                InvoiceStatus.NOT_PAID -> Icons.Filled.Clear
                            },
                            contentDescription = null,
                            tint = Color(if (statusInvoice == InvoiceStatus.PAID) 0xFF4CAF50 else 0xFFD32F2F),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Text(
                    text = amount,
                    color = Color(0xFFD32F2F),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedButton(
                    onClick = onDetailClick,
                    modifier = Modifier
                        .weight(2f)
                        .heightIn(min = 40.dp),
                    shape = RoundedCornerShape(24),
                    border = BorderStroke(1.dp, DarkGreen),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = DarkGreen,
                        containerColor = DarkGreen.copy(alpha = 0.1f)
                    )
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_eye),
                        contentDescription = null,
                        Modifier.size(20.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text("Xem chi tiết")
                }

                OutlinedButton(
                    onClick = onDeleteClick,
                    modifier = Modifier
                        .weight(1f)
                        .heightIn(min = 40.dp),
                    shape = RoundedCornerShape(24),
                    border = BorderStroke(1.dp, Color(0xFFD32F2F)),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color(0xFFD32F2F).copy(alpha = 0.1f),
                        contentColor = Color(0xFFD32F2F)
                    ),
                ) {
                    Icon(Icons.Outlined.Delete, contentDescription = null, Modifier.size(20.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("Xóa")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvoiceHistoryScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    roomId: String,
    viewModel: InvoiceViewModel
) {

    val invoicesState by viewModel.invoices.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getInvoices(roomId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Danh sách hóa đơn") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
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
                .padding(horizontal = 16.dp),
            contentAlignment = if (invoicesState is UiState.Success) Alignment.TopCenter else Alignment.Center
        ) {
            when (invoicesState) {
                is UiState.Loading -> {
                    CustomLoadingProgress()
                }

                is UiState.Success -> {
                    val invoices = (invoicesState as UiState.Success).data
                    if (invoices.isEmpty()) {
                        Text("Không có hóa đơn nào", style = MaterialTheme.typography.bodyMedium)
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(invoices.size) { index ->
                                val invoice = invoices[index]
                                PaymentItem(
                                    date = invoice.date,
                                    amount = toMoney(invoice.totalAmount) + " đ",
                                    statusInvoice = invoice.status,
                                    onDetailClick = {
                                        // Handle detail click
                                    },
                                    onDeleteClick = {
                                        // Handle delete click
                                    }
                                )
                            }
                        }
                    }
                }

                is UiState.Failure -> {
                    Text("Lỗi: ${(invoicesState as UiState.Failure).error}", color = Color.Red)
                }

                UiState.Empty -> {
                    Text("Không có dữ liệu", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }

}