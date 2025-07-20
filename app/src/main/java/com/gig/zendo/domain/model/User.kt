package com.gig.zendo.domain.model

import com.google.firebase.auth.FirebaseUser

data class User(
    val uid: String,
    val email: String?,
    val isEmailVerified: Boolean
)

fun FirebaseUser.toUser(): User {
    return User(
        uid = this.uid,
        email = this.email,
        isEmailVerified = this.isEmailVerified
    )
}
