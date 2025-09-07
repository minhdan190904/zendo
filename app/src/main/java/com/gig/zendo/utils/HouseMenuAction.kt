package com.gig.zendo.utils

import com.gig.zendo.ui.common.MenuAction

sealed class HouseMenuAction(override val label: String): MenuAction {
    data object Edit : HouseMenuAction("Sửa nhà")
    data object Delete : HouseMenuAction("Xoá nhà")
}