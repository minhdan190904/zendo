package com.gig.zendo.utils

import com.gig.zendo.ui.common.MenuAction

sealed class HouseMenuAction(override val label: String): MenuAction {
    data object Edit : HouseMenuAction("Sửa nhà")
    data object Delete : HouseMenuAction("Xoá nhà")
    data object ExportExcel : HouseMenuAction("Xuất Excel")
    data object AddExpense : HouseMenuAction("Thêm chi phí")
    data object ExpenseDetail : HouseMenuAction("Xem chi phí")
    data object FinancialReport : HouseMenuAction("Báo cáo tài chính")
}