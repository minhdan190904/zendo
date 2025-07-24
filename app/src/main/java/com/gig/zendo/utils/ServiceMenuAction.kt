package com.gig.zendo.utils

import com.gig.zendo.ui.common.MenuAction

sealed class ServiceMenuAction(override val label: String): MenuAction {
    data object Delete : ServiceMenuAction("Xoá dịch vụ")
    data object Edit : ServiceMenuAction("Sửa dịch vụ")
}