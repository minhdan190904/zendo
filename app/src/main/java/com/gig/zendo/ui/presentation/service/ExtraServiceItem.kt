package com.gig.zendo.ui.presentation.service

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.gig.zendo.domain.model.Service
import com.gig.zendo.ui.common.MyPopupMenu
import com.gig.zendo.utils.ServiceMenuAction

@Composable
fun ExtraServiceItem(
    service: Service,
    onDeleteExtraService: (Service) -> Unit,
    onEditExtraService: (Service) -> Unit,
) {

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = service.name,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = labelMapperForChargeMethod(service.chargeMethod),
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }

        Text(
            text = service.chargeValue.toString(),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(end = 8.dp)
        )

        MyPopupMenu(actions = listOf(ServiceMenuAction.Edit, ServiceMenuAction.Delete)) { action ->
            when (action) {
                ServiceMenuAction.Delete -> {
                    onDeleteExtraService(service)
                }

                ServiceMenuAction.Edit -> {
                    onEditExtraService(service)
                }
            }

        }
    }
}
