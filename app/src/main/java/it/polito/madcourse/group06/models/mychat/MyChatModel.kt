package it.polito.madcourse.group06.models.mychat

import it.polito.madcourse.group06.viewmodels.MyMessage

data class MyChatModel(
    var chatID: String,
    var chatContent: List<MyMessage>,
    var advID: String
)