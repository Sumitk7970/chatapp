package com.example.chat.daos

import com.example.chat.models.Chat
import com.example.chat.utils.FirebaseConstants
import com.example.chat.models.Message
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MessageDao {
    private val db = FirebaseFirestore.getInstance()
    private val messagesCollection = db.collection(FirebaseConstants.CHATS)

    @DelicateCoroutinesApi
    fun sendMessage(senderId: String, receiverId: String, chatRoom: String, message: Message) {
        GlobalScope.launch(Dispatchers.IO) {
            messagesCollection.document(chatRoom).get().addOnCompleteListener {
                if (it.isSuccessful && it.result == null) {
                    addChatRoom(senderId, receiverId, chatRoom = chatRoom)
                }
            }
            addMessage(chatRoom, message)
        }
    }

    private fun addChatRoom(vararg members: String, chatRoom: String) {
        val chat = Chat(chatRoom, members.asList())
        messagesCollection.document(chatRoom).set(chat)
    }

    private fun addMessage(chatRoom: String, message: Message) {
        messagesCollection.document(chatRoom).collection(FirebaseConstants.TEXT_MESSAGES)
            .document().set(message)
            .addOnSuccessListener {
                setUpLastMessage(chatRoom, message)
            }
    }

    /**
     * Adding last message sent to the chat room document for displaying below name of the person
     * in chats fragment
     */
    private fun setUpLastMessage(chatRoom: String, message: Message) {
        messagesCollection.document(chatRoom).set(message)
    }

    fun getMessages(chatRoom: String): Query {
        return messagesCollection.document(chatRoom).collection(FirebaseConstants.TEXT_MESSAGES)
            .orderBy("sendTime", Query.Direction.DESCENDING)
    }
    /**
     * Generate a unique chat room id from senderId and receiverId
     */
    fun generateChatRoomId(senderId: String, receiverId: String): String {
        return if (senderId.compareTo(receiverId) == -1) {
            senderId + receiverId
        } else {
            receiverId + senderId
        }
    }
}