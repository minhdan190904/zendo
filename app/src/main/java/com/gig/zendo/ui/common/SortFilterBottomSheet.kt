package com.gig.zendo.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gig.zendo.R

enum class RoomSortOption(val label: String) {
    TimeCreated("Thời gian tạo"),
    Name("Tên phòng"),
    RoomNotEmpty("Phòng không trống"),
    OutstandingAmount("Số tiền nợ"),
}

data class RoomSortState(
    val option: RoomSortOption = RoomSortOption.TimeCreated,
    val ascending: Boolean = true
)

data class RoomFilterState(
    val roomNotEmpty : Boolean = false,
    val roomHaveOutstandingAmount: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortFilterBottomSheet(
    visible: Boolean,
    roomSortState: RoomSortState,
    filters: RoomFilterState,
    onDismiss: () -> Unit,
    onApply: (RoomSortState, RoomFilterState) -> Unit,
    onReset: () -> Unit
) {
    if (!visible) return

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        dragHandle = { },
        containerColor = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Spacer(Modifier.width(48.dp))
            Text(
                "Sắp xếp & Lọc",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
            IconButton(onClick = onDismiss) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = "Close"
                )
            }
        }

        HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)

        // Body
        var selectedSort by remember { mutableStateOf(roomSortState.option) }
        var ascending by remember { mutableStateOf(roomSortState.ascending) }
        var f by remember { mutableStateOf(filters) }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            item { SectionTitle("Sắp xếp theo") }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(if (ascending) "Tăng dần" else "Giảm dần", style = MaterialTheme.typography.bodyMedium)
                    IconButton(onClick = { ascending = !ascending }) {
                        Icon(
                            painter = painterResource(if(ascending) R.drawable.ic_arrow_upward else R.drawable.ic_arrow_downward),
                            contentDescription = "Toggle sort order"
                        )
                    }
                }
            }

            items(RoomSortOption.entries.toTypedArray()) { opt ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = selectedSort == opt,
                        onClick = { selectedSort = opt }
                    )
                    Text(opt.label, modifier = Modifier.padding(start = 8.dp))
                }
            }

            item {
                HorizontalDivider(
                    Modifier.padding(top = 4.dp, bottom = 8.dp),
                    DividerDefaults.Thickness,
                    DividerDefaults.color
                )
            }

            item { SectionTitle("Lọc ") }

            item {
                CheckRow(
                    checked = f.roomNotEmpty,
                    label = "Phòng không trống"
                ) { f = f.copy(roomNotEmpty = it) }
            }
            item {
                CheckRow(
                    checked = f.roomHaveOutstandingAmount,
                    label = "Phòng có tiền nợ"
                ) { f = f.copy(roomHaveOutstandingAmount = it) }
            }
        }

        Surface(tonalElevation = 2.dp, color = Color.White) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .navigationBarsPadding(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onReset,
                    modifier = Modifier.weight(1f)
                ) { Text("Đặt lại") }

                Button(
                    onClick = {
                        onApply(RoomSortState(selectedSort, ascending), f)
                    },
                    modifier = Modifier.weight(2f)
                ) { Text("Áp dụng") }
            }
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text,
        style = MaterialTheme.typography.titleSmall,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
    )
}

@Composable
private fun CheckRow(checked: Boolean, label: String, onChecked: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(checked = checked, onCheckedChange = onChecked)
        Text(label, modifier = Modifier.padding(start = 8.dp))
    }
}


@Preview(showBackground = true)
@Composable
fun SortFilterBottomSheetPreview() {
    SortFilterBottomSheet(
        visible = true,
        roomSortState = RoomSortState(),
        filters = RoomFilterState(),
        onDismiss = {},
        onApply = { _, _ -> },
        onReset = {}
    )
}
