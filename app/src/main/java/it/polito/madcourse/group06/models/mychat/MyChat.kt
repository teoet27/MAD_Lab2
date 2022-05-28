package it.polito.madcourse.group06.viewmodels

import androidx.room.Entity

@Entity(tableName = "mychatTable")
data class MyChat (
    var chatID: String,
    var senderID: String,
    var receiverID: String,
    var msg: String,
    var timestamp: String,
    var advID: String
)