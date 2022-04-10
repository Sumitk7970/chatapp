package com.example.chat.models

data class Message(
    val message: String = "",
    val senderId: String = "",
    val sendTime: Long = 0L
)