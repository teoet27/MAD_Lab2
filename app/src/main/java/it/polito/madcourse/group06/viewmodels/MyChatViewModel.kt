package it.polito.madcourse.group06.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.storage.FirebaseStorage
import it.polito.madcourse.group06.models.mychat.MyChatModel
import it.polito.madcourse.group06.models.userprofile.UserProfile
import java.lang.Exception

class MyChatViewModel(application: Application) : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val storage: FirebaseStorage = FirebaseStorage.getInstance()
    private var listenerRegistration: ListenerRegistration
    private val context = application

    /**
     * [UserProfile] with which the current user is chatting
     */
    private var _chattingUserPH = UserProfile(
        "", "", "", "", "", "", "",
        "", null, 0.0, 0.0, 0.0,
        ArrayList<String>(), ArrayList<String>(), null, ArrayList<String>(), HashMap<String, String>()
    )
    private val _pvtChattingUser = MutableLiveData<UserProfile>().also { it.value = _chattingUserPH }
    val chattingUser: LiveData<UserProfile> = this._pvtChattingUser

    /**
     * [MyChatModel] live data
     */
    private var _myChatPH = MyChatModel("", "", "", arrayListOf(), "-1")
    private val _pvtMyChat = MutableLiveData<MyChatModel>().also { it.value = _myChatPH }
    val myCurrentChat: LiveData<MyChatModel> = this._pvtMyChat

    init {
        listenerRegistration = db.collection("MyChat")
            .addSnapshotListener { value, error ->

            }
    }

    /**
     * setChattingUserProfile sets the chattingUser in order to retrieve the information
     * of the user with which the current user is chatting and get them in the MyChat fragment
     * @param usr the user profile with which we are going to chat
     */
    fun setChattingUserProfile(usr: UserProfile) {
        this._chattingUserPH = usr
        this._pvtChattingUser.value = this._chattingUserPH
    }

    private fun createNewChat(advID: String, isCurrentChat: Boolean = true) {
        var chatID: String = ""
        db
            .collection("Chat")
            .document().also { chatID = it.id }
            .set(
                mapOf(
                    "chat_id" to chatID,
                    "content" to mutableListOf<MyMessage>(),
                    "adv_id" to advID,
                )
            )
        this._myChatPH = MyChatModel(chatID, "", "", arrayListOf(), advID)
        this._pvtMyChat.value = this._myChatPH
    }

    private fun fetchChat(chatID: String) {
        db
            .collection("Chat")
            .whereEqualTo("id", chatID)
            .get()
            .addOnSuccessListener {
                for (x in it) {
                    this._myChatPH = x.toMyChatModel() ?: MyChatModel("", "", "", arrayListOf(), "")
                    this._pvtMyChat.value = this._myChatPH
                    break
                }
            }
    }

    fun fetchChatByAdvertisementID(currentUserID: String, advertisementID: String) {
        db
            .collection("UserProfile")
            .whereEqualTo("id", currentUserID)
            .whereArrayContains("chats_id", advertisementID)
            .get()
            .addOnSuccessListener { query ->
                if (query.isEmpty) {
                    this.createNewChat(advertisementID)
                } else {
                    query.forEach { docSnap ->
                        this.fetchChat(docSnap.toString())
                    }
                }
            }
    }

    private fun DocumentSnapshot.toMyChatModel(): MyChatModel? {
        return try {
            val id = this.get("id") as String
            val content = this.get("nickname") as ArrayList<MyMessage>?
            MyChatModel(id, "", "", content!!, "-1")
        } catch (e: Exception) {
            null
        }
    }
}