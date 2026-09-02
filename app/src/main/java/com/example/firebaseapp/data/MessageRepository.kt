package com.example.firebaseapp.data

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MessageRepository @Inject constructor(
    firebaseDatabase: FirebaseDatabase
) {
    private val messagesRef = firebaseDatabase.getReference("messages")

    fun observeMessages(): Flow<List<String>> = callbackFlow {

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val values = snapshot.children.mapNotNull { it.getValue(String::class.java) }
                trySend(values)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("MessageRepository", "Read failed: ${error.message}", error.toException())
                close(error.toException())
            }
        }

        messagesRef.addValueEventListener(listener)
        awaitClose { messagesRef.removeEventListener(listener) }
    }

    fun writeMessage(text: String) {
        messagesRef.push().setValue(text)
    }
}