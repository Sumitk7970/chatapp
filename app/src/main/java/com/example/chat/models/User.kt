package com.example.chat.models

data class User(
    val uid: String = "",
    val displayName: String = "",
    val email: String = "",
    val photoUrl: String? = null,
    val status: String = "Hey there! I am using whatsapp"
)