package com.gig.zendo.ui.presentation.invoice

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.gig.zendo.domain.model.Invoice
import com.gig.zendo.domain.model.InvoiceStatus
import com.gig.zendo.ui.common.ConfirmDialog
import com.gig.zendo.ui.common.CustomElevatedButton
import com.gig.zendo.ui.common.LoadingScreen
import com.gig.zendo.utils.UiState
import com.gig.zendo.utils.captureComposableAsBitmap
import com.gig.zendo.utils.saveBitmapToPictures
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvoiceDetailScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    viewModel: InvoiceViewModel,
    invoice: Invoice? = null,
) {
    val invoice = invoice


    var invoiceStatusChecked by remember {
        mutableStateOf(
            invoice?.status ?: InvoiceStatus.NOT_PAID
        )
    }

    val updateInvoiceStatusState by viewModel.updateStatusInvoiceState.collectAsStateWithLifecycle()
    var showDeleteDialog by remember { mutableStateOf(false) }
    val deleteInvoiceState by viewModel.deleteInvoiceState.collectAsStateWithLifecycle()

    LaunchedEffect(updateInvoiceStatusState) {
        when (val state = updateInvoiceStatusState) {
            is UiState.Success -> {
                navController.popBackStack()
                navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.set("shouldRefreshRooms", true)
                viewModel.resetStateUpdateStatus()
                navController.popBackStack()
            }

            is UiState.Failure -> {
                snackbarHostState.showSnackbar(
                    state.error ?: "Cập nhật trạng thái hóa đơn thất bại"
                )
            }

            else -> {}
        }

    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Chi tiết hóa đơn") },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
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
            Column {

                CardViewInvoiceDetail(
                    invoice = invoice,
                    onDeleteClick = {
                        showDeleteDialog = true
                    },
                    onStatusChange = { status ->
                        invoiceStatusChecked = status
                    }
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(
                        24.dp,
                        Alignment.CenterHorizontally
                    ),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp, horizontal = 16.dp),

                    ) {

                    val context = LocalContext.current
                    val coroutineScope = rememberCoroutineScope()

                    CustomElevatedButton(
                        onClick = {
                            coroutineScope.launch {
                                val bitmap = context.captureComposableAsBitmap {
                                    CardViewInvoiceDetail(invoice = invoice, isForScreenShot = true)
                                }

                                val myFolder = "Zendo"

                                saveBitmapToPictures(
                                    context = context,
                                    bitmap = bitmap,
                                    folderName = myFolder,
                                    //file name is generated based on the date invoice and tenant id and system timme
                                    fileName = "${invoice?.date}_${invoice?.tenant?.id}_${System.currentTimeMillis()}.png"
                                ).let { file ->
                                    snackbarHostState.showSnackbar("Đã lưu hóa đơn vào: Pictures/$myFolder")
                                }
                            }
                        },
                        text = "Chia sẻ",
                        modifier = Modifier.weight(1f)
                    )
                    CustomElevatedButton(
                        onClick = {
                            viewModel.updateStatusInvoice(
                                invoiceId = invoice?.id ?: "",
                                status = invoiceStatusChecked
                            )
                        }, text = "Cập nhật",
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
    LoadingScreen(isLoading = updateInvoiceStatusState is UiState.Loading)

    if(showDeleteDialog){
        ConfirmDialog(
            title = "Xóa hóa đơn",
            message = "Bạn có chắc chắn muốn xóa nhà trọ này?",
            onConfirm = {
                if(invoice != null) {
                    viewModel.deleteInvoice(invoice.id)
                }
                showDeleteDialog = false
            },
            onDismiss = { showDeleteDialog = false },
        )
    }

    LaunchedEffect(deleteInvoiceState) {
        when (val state = deleteInvoiceState) {
            is UiState.Success -> {
                navController.popBackStack()
                viewModel.resetStateDeleteInvoice()
                navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.set("shouldRefreshRooms", true)
                navController.popBackStack()
                snackbarHostState.showSnackbar("Đã xóa hóa đơn thành công")
            }

            is UiState.Failure -> {
                snackbarHostState.showSnackbar(
                    state.error ?: "Xóa hóa đơn thất bại"
                )
            }

            else -> {}
        }
    }
}