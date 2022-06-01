package it.polito.madcourse.group06.models.mychat

import it.polito.madcourse.group06.viewmodels.MyMessage

data class MyChat(
    var chatID: String,
    var chatContent: List<MyMessage>
)