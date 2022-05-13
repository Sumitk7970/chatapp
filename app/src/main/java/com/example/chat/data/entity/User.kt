package com.example.chat.data.entity

data class User(
    val uid: String = "",
    val displayName: String = "",
    val email: String = "",
    val photoUrl: String? = null,
    val status: String = "Hey there! I am using whatsapp"
)