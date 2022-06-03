package it.polito.madcourse.group06.models.mychat

import androidx.room.Entity
import com.google.firebase.firestore.PropertyName

@Entity(tableName = "mychatTable")
data class MyMessage (
    @get:PropertyName("sender_id")
    @set:PropertyName("sender_id")
    var senderID: String,

    @get:PropertyName("receiver_id")
    @set:PropertyName("receiver_id")
    var receiverID: String,

    @get:PropertyName("msg")
    @set:PropertyName("msg")
    var msg: String,

    @get:PropertyName("timestamp")
    @set:PropertyName("timestamp")
    var timestamp: String,

    @get:PropertyName("is_an_offer")
    @set:PropertyName("is_an_offer")
    var isAnOffer: Boolean
)