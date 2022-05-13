package com.example.chat.data.remote

import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import com.example.chat.data.entity.Chat
import com.example.chat.utils.FirebaseConstants.COLLECTION_CHATS
import com.example.chat.utils.FirebaseConstants.KEY_CHAT_ROOM_MEMBERS
import com.example.chat.utils.FirebaseConstants.KEY_LAST_MESSAGE
import com.example.chat.utils.FirebaseConstants.KEY_MESSAGE_SEND_TIME
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class ChatDatabase {
    private val db = FirebaseFirestore.getInstance()
    private val chatCollection = db.collection(COLLECTION_CHATS)
    private val currentUserId = FirebaseAuth.getInstance().currentUser?.uid

    private val storageRef = FirebaseStorage.getInstance().reference

    fun addNewChatRoom(context: Context, chatRoomName: String, uri: Uri?, members: List<String>) {
        CoroutineScope(Dispatchers.IO).launch {
            val key = chatCollection.document()
            val chat = Chat(
                chatRoomId = key.id,
                chatRoomName = chatRoomName,
                admin = currentUserId!!,
                members = members
            )
            chatCollection.document(key.id).set(chat)
            uri?.let {
                uploadImageToFirebase(context, uri, key.id)
            }
        }
    }

    /**
     *  @return query to get all chat rooms of the current user ordered by sendTime of last message
     */
    fun queryChats() = chatCollection.orderBy(
        "$KEY_LAST_MESSAGE.$KEY_MESSAGE_SEND_TIME",
        Query.Direction.DESCENDING
    ).whereArrayContains(KEY_CHAT_ROOM_MEMBERS, currentUserId!!)

    private fun uploadImageToFirebase(context: Context, uri: Uri, chatRoomId: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val extension = MimeTypeMap.getSingleton()
                .getExtensionFromMimeType(context.contentResolver.getType(uri))
            val fileName = UUID.randomUUID().toString() + "." + extension
            storageRef.child("images/$fileName").putFile(uri)
                .addOnSuccessListener {
                    it?.storage?.downloadUrl?.addOnSuccessListener { url ->
                        chatCollection.document(chatRoomId).update("photoUrl", url.toString())
                    }
                }
        }
    }
}