package com.gig.zendo.utils

sealed class MenuAction(val label: String) {
    data object Edit : MenuAction("Sửa")
    data object Delete : MenuAction("Xoá")
    data object Export : MenuAction("Xuất Excel")
}