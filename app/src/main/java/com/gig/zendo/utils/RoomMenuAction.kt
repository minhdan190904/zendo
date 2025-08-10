package com.gig.zendo.utils

import com.gig.zendo.ui.common.MenuAction

sealed class RoomMenuAction(override val label: String): MenuAction {
    data object Invoice : RoomMenuAction("Hoá đơn")
    data object History : RoomMenuAction("Lịch sử")
    data object Delete : RoomMenuAction("Xoá phòng")
    data object Edit : RoomMenuAction("Sửa tên")
    data object CheckOut : RoomMenuAction("Trả phòng")
    data object TenantDetail : RoomMenuAction("Chi tiết")
    data object EditTenant : RoomMenuAction("Sửa thông tin khách thuê")
}