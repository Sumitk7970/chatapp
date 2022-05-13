package com.example.chat.data.entity

data class Chat(
    val chatRoomId: String = "",
    val chatRoomName: String = "",
    val photoUrl: String = "",
    val admin: String = "",
    val members: List<String> = listOf(),
    val lastMessage: Message = Message()
)
