package it.polito.madcourse.group06.models.mychat

import androidx.room.Entity
import com.google.firebase.firestore.PropertyName

@Entity(tableName = "myChatTable")
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

    @get:PropertyName("location")
    @set:PropertyName("location")
    var location: String,

    @get:PropertyName("starting_time")
    @set:PropertyName("starting_time")
    var startingTime: String,

    @get:PropertyName("duration")
    @set:PropertyName("duration")
    var duration: String,

    @get:PropertyName("timestamp")
    @set:PropertyName("timestamp")
    var timestamp: String,

    @get:PropertyName("is_an_offer")
    @set:PropertyName("is_an_offer")
    var isAnOffer: Boolean,

    /**
     * -1: rejected
     * 0: pending
     * 1: accepted
     * 2: not available
     */
    @get:PropertyName("prop_state")
    @set:PropertyName("prop_state")
    var propState: Long
)