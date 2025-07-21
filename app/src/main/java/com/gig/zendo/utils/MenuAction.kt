package com.gig.zendo.utils

sealed class MenuAction(val label: String) {
    data object EditHouse : MenuAction("Sửa nhà")
    data object DeleteHouse : MenuAction("Xoá nhà")
    data object ExportExcel : MenuAction("Xuất Excel")
    data object Invoice : MenuAction("Hoá đơn")
    data object History : MenuAction("Lịch sử")
    data object DeleteRoom : MenuAction("Xoá phòng")
    data object EditRoom : MenuAction("Sửa tên")
    data object CheckOut : MenuAction("Trả phòng")
    data object TenantDetail : MenuAction("Chi tiết")
}