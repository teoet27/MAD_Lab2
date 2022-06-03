package it.polito.madcourse.group06.models.mychat

import androidx.room.PrimaryKey
import com.google.firebase.firestore.PropertyName
import it.polito.madcourse.group06.viewmodels.MyMessage

data class MyChatModel(
    @PrimaryKey(autoGenerate = true)
    @get:PropertyName("chat_id")
    @set:PropertyName("chat_id")
    var chatID: String,

    @get:PropertyName("user_id")
    @set:PropertyName("user_id")
    var userID: String,

    @get:PropertyName("other_user_id")
    @set:PropertyName("other_user_id")
    var otherUserID: String,

    @get:PropertyName("chat_content")
    @set:PropertyName("chat_content")
    var chatContent: ArrayList<MyMessage>,

    @get:PropertyName("adv_id")
    @set:PropertyName("adv_id")
    var advID: String
)