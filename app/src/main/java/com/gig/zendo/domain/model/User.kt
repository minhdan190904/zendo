package com.gig.zendo.domain.model

import com.google.firebase.auth.FirebaseUser

data class User(
    val uid: String,
    val email: String? = null,
    val isEmailVerified: Boolean,
    val imageUrl: String? = null
){
    fun getUsernameByEmail(): String {
        return email?.substringBefore('@') ?: "Unknown User"
    }

    companion object {
        const val COLLECTION_NAME = "users"
    }
}

fun FirebaseUser.toUser(): User {
    return User(
        uid = this.uid,
        email = this.email,
        isEmailVerified = this.isEmailVerified,
        imageUrl = this.photoUrl?.toString()
    )
}
