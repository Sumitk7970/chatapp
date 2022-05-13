package com.example.chat.data.remote

import com.example.chat.data.entity.Message
import com.example.chat.utils.FirebaseConstants.COLLECTION_CHATS
import com.example.chat.utils.FirebaseConstants.COLLECTION_TEXT_MESSAGES
import com.example.chat.utils.FirebaseConstants.KEY_LAST_MESSAGE
import com.example.chat.utils.FirebaseConstants.KEY_MESSAGE_SEND_TIME
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MessageDatabase {
    private val db = FirebaseFirestore.getInstance()
    private val messagesCollection = db.collection(COLLECTION_CHATS)

    fun sendMessage(chatRoomId: String, message: Message) = CoroutineScope(Dispatchers.IO).launch {
        messagesCollection.document(chatRoomId).collection(COLLECTION_TEXT_MESSAGES)
            .document().set(message)
            .addOnSuccessListener {
                setUpLastMessage(chatRoomId, message)
            }
    }

    /**
     * Adding last message sent to the chat room document for displaying below name of the chat room
     * in chats fragment
     */
    private fun setUpLastMessage(chatRoom: String, message: Message) {
        messagesCollection.document(chatRoom).update(KEY_LAST_MESSAGE, message)
    }

    /** @return query to get messages */
    fun queryMessages(chatRoomId: String) = messagesCollection.document(chatRoomId)
            .collection(COLLECTION_TEXT_MESSAGES)
            .orderBy(KEY_MESSAGE_SEND_TIME, Query.Direction.DESCENDING)
}