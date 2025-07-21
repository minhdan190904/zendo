package com.gig.zendo.domain.model

data class House(
    val id: String = "",
    val name: String = "",
    val address: String = "",
    val uid: String = ""
){
    companion object {
        const val COLLECTION_NAME = "houses"
        const val FIELD_UID = "uid"
    }
}