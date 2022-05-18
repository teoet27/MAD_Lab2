package it.polito.MAD.group06.remote

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore

class FirestoreDatabase {

    companion object {
        @Volatile private var firestoreDatabase: FirebaseFirestore? = null

        /**
         * There's a single instance of the database and
         * this method checks whether it's already been instantiated and,
         * eventually, returns the reference to the unique object shared among
         * all the callers.
         */
        fun getDatabase(context: Context): FirebaseFirestore {
            if(this.firestoreDatabase == null)
            {
                synchronized(this) {
                    this.firestoreDatabase = FirebaseFirestore.getInstance()
                }
            }
            return this.firestoreDatabase!!
        }

    }
}