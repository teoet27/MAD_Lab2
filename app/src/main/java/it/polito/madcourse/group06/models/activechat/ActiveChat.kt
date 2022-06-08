package it.polito.madcourse.group06.models.activechat

import it.polito.madcourse.group06.models.userprofile.UserProfile

data class ActiveChat(
    var advTitle: String,
    var advID: String,
    var userFullname: String,
    var chatID: String,
    var userID: String,
    var otherUserID: String,
    var otherUserObj: UserProfile
)