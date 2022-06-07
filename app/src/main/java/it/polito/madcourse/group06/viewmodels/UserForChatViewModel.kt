package it.polito.madcourse.group06.viewmodels

import android.app.Application
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.storage.FirebaseStorage

class UserForChatViewModel(application: Application) {
    private val db = FirebaseFirestore.getInstance()
    private val storage: FirebaseStorage = FirebaseStorage.getInstance()
    private var listenerRegistration: ListenerRegistration
    private val context = application

    init {
        listenerRegistration = db.collection("MyChat")
            .addSnapshotListener { value, error ->  }
    }

}