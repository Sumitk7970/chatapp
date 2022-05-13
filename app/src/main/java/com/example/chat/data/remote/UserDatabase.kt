package com.example.chat.data.remote

import com.example.chat.utils.FirebaseConstants
import com.example.chat.data.entity.User
import com.example.chat.utils.FirebaseConstants.KEY_USER_DISPLAY_NAME
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserDatabase {
    private val db = FirebaseFirestore.getInstance()
    val usersCollection = db.collection(FirebaseConstants.COLLECTION_USERS)

    fun addUser(user: User?) {
        user?.let {
            CoroutineScope(Dispatchers.IO).launch {
                usersCollection.document(it.uid).set(it)
            }
        }
    }

    /** @return query to get all users */
    fun queryUser() = usersCollection.orderBy(KEY_USER_DISPLAY_NAME, Query.Direction.ASCENDING)

    /** @return a task which from which you can get the user */
    fun getUserById(uid: String) = usersCollection.document(uid).get()
}