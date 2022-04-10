package com.example.chat.models

data class Chat(
    val chatRoomId: String = "",
    val members: List<String> = listOf(),
    // for last message
    val lastMessage: Message = Message()
)
