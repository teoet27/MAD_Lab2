package it.polito.madcourse.group06.viewmodels

import androidx.room.Entity

@Entity(tableName = "mychatTable")
data class MyMessage (
    var senderID: String,
    var receiverID: String,
    var msg: String,
    var timestamp: String,
    var isAPurpose: Boolean
)