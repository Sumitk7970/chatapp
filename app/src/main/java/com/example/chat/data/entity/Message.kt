package com.example.chat.data.entity

data class Message(
    val message: String = "",
    val senderId: String = "",
    val sendTime: Long = 0L
)