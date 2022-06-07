package it.polito.madcourse.group06.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import it.polito.madcourse.group06.models.mychat.MyChatModel
import it.polito.madcourse.group06.models.mychat.MyMessage
import it.polito.madcourse.group06.models.userprofile.UserProfile
import java.lang.Exception

class MyChatViewModel(application: Application) : AndroidViewModel(application) {
    private val db = FirebaseFirestore.getInstance()
    private var listenerRegistration: ListenerRegistration
    private val context = application

    /**
     * [UserProfile] with which the current user is chatting
     */
    private var _chattingUserPH = UserProfile(
        "", "", "", "", "", "", "",
        "", null, 0.0, 0.0, 0.0,
        ArrayList(), ArrayList(), null, ArrayList(), HashMap()
    )
    private val _pvtChattingUser = MutableLiveData<UserProfile>().also { it.value = _chattingUserPH }
    val chattingUser: LiveData<UserProfile> = this._pvtChattingUser

    /**
     * [MyChatModel] live data
     */
    private var _myChatPH = MyChatModel("", "", "", arrayListOf(), "-1", false)
    private val _pvtMyChat = MutableLiveData<MyChatModel>().also { it.value = _myChatPH }
    val myCurrentChat: LiveData<MyChatModel> = this._pvtMyChat

    /**
     * List of Chats
     */

    /**
     * List of Advertisements
     */
    private val _chats = MutableLiveData<List<MyChatModel>>()
    val listOfChats: LiveData<List<MyChatModel>> = _chats

    init {
        listenerRegistration = db.collection("Chat")
            .addSnapshotListener { value, error ->
                if (error != null) {
                    _chats.value = emptyList()
                } else {
                    _chats.value = value!!.mapNotNull { elem ->
                        elem.toMyChatModel()
                    }
                    for (c in _chats.value!!) {
                        if (c.chatID == myCurrentChat.value!!.chatID) {
                            this._myChatPH = c
                            this._pvtMyChat.value = this._myChatPH
                        }
                    }
                }
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

    private fun createNewChat(advID: String, currentUserID: String, otherUserID: String) {
        var chatID = ""
        db
            .collection("Chat")
            .document().also { chatID = it.id }
            .set(
                mapOf(
                    "chat_id" to chatID,
                    "user_id" to currentUserID,
                    "other_user_id" to otherUserID,
                    "chat_content" to mutableListOf<MyMessage>(),
                    "adv_id" to advID,
                    "has_ended" to false
                )
            )
        this._myChatPH = MyChatModel(chatID, currentUserID, otherUserID, arrayListOf(), advID, false)
        this._pvtMyChat.value = this._myChatPH
    }

    fun fetchChatByAdvertisementID(currentUserID: String, otherUserID: String, advertisementID: String) {
        db
            .collection("Chat")
            .whereEqualTo("user_id", currentUserID)
            .whereEqualTo("adv_id", advertisementID)
            .get()
            .addOnSuccessListener { query ->
                if (query.isEmpty) {
                    this.createNewChat(advertisementID, currentUserID, otherUserID)
                } else {
                    query.documents[0].also { docSnap ->
                        this._myChatPH = docSnap.toMyChatModel() ?: MyChatModel("", "", "", arrayListOf<MyMessage>(), "", false)
                        this._pvtMyChat.value = this._myChatPH
                    }
                }
            }
    }

    fun addNewMessage(chatID: String, messages: ArrayList<MyMessage>) {
        db
            .collection("Chat")
            .document(chatID)
            .update(
                "chat_id", chatID,
                "chat_content", messages
            )
            .addOnSuccessListener {
                this._myChatPH.chatContent = messages
                this._pvtMyChat.value = this._myChatPH
            }
    }

    private fun DocumentSnapshot.toMyChatModel(): MyChatModel? {
        return try {
            val advID = this.get("adv_id") as String
            val chatContent: ArrayList<MyMessage>? = (this.get("chat_content") as ArrayList<HashMap<Any, Any>>).toMyMessageArray()
            val chatID = this.get("chat_id") as String
            val currentID = this.get("user_id") as String
            val otherUserID = this.get("other_user_id") as String
            val progress = this.get("has_ended") as Boolean
            MyChatModel(chatID, currentID, otherUserID, chatContent!!, advID, progress)
        } catch (e: Exception) {
            null
        }
    }

    private fun ArrayList<HashMap<Any, Any>>.toMyMessageArray(): ArrayList<MyMessage> {
        val out: ArrayList<MyMessage> = arrayListOf()
        for (value in this) {
            val myMessage = MyMessage("", "", "", "", "", "", "", false, 2)
            myMessage.senderID = value["sender_id"] as String
            myMessage.receiverID = value["receiver_id"] as String
            myMessage.msg = value["msg"] as String
            myMessage.location = value["location"] as String
            myMessage.startingTime = value["starting_time"] as String
            myMessage.duration = value["duration"] as String
            myMessage.timestamp = value["timestamp"] as String
            myMessage.isAnOffer = value["is_an_offer"] as Boolean
            myMessage.propState = value["prop_state"] as Long
            out.add(myMessage)
        }
        return out
    }

    fun addCreditToChattingUser(cost: Double) {
        val updatedUser = _pvtChattingUser.value.also { dumbUser ->
            dumbUser!!.credit += cost
        }

        db
            .collection("UserProfile")
            .document(this._pvtChattingUser.value?.id!!)
            .update(
                "id", updatedUser!!.id,
                "nickname", updatedUser.nickname,
                "fullname", updatedUser.fullName,
                "qualification", updatedUser.qualification,
                "description", updatedUser.description,
                "email", updatedUser.email,
                "phone_number", updatedUser.phoneNumber,
                "location", updatedUser.location,
                "skills", updatedUser.skills,
                "credit", updatedUser.credit,
                "rating_sum", updatedUser.rating_sum,
                "n_ratings", updatedUser.n_ratings,
                "comments_services_rx", updatedUser.comments_services_rx,
                "comments_services_done", updatedUser.comments_services_done,
                "img_path", updatedUser.imgPath,
                "saved_ads_ids", updatedUser.saved_ads_ids
            )
    }

    fun deductCreditFromChattingUser(cost: Double) {
        val updatedUser = _pvtChattingUser.value.also { dumbUser ->
            dumbUser!!.credit -= cost
        }
        db
            .collection("UserProfile")
            .document(this._chattingUserPH.id!!)
            .update(
                "id", updatedUser!!.id,
                "nickname", updatedUser.nickname,
                "fullname", updatedUser.fullName,
                "qualification", updatedUser.qualification,
                "description", updatedUser.description,
                "email", updatedUser.email,
                "phone_number", updatedUser.phoneNumber,
                "location", updatedUser.location,
                "skills", updatedUser.skills,
                "credit", updatedUser.credit,
                "rating_sum", updatedUser.rating_sum,
                "n_ratings", updatedUser.n_ratings,
                "comments_services_rx", updatedUser.comments_services_rx,
                "comments_services_done", updatedUser.comments_services_done,
                "img_path", updatedUser.imgPath,
                "saved_ads_ids", updatedUser.saved_ads_ids
            )
    }

    fun concludeChat() {
        db
            .collection("Chat")
            .document(this._pvtMyChat.value?.chatID!!)
            .get()
            .addOnSuccessListener { doc ->
                val chat = doc.toMyChatModel()
                chat!!.hasEnded = true
                doc.reference.update("has_ended", chat.hasEnded)
                this._myChatPH.hasEnded = true
                this._pvtMyChat.value = this._myChatPH
            }
    }

    fun setProposalState(messageID: Int, messageState: Long) {
        db
            .collection("Chat")
            .document(this._pvtMyChat.value?.chatID!!)
            .get()
            .addOnSuccessListener { doc ->
                val chat = doc.toMyChatModel()
                val messages = chat!!.chatContent
                messages[messageID].propState = messageState
                doc.reference.update("chat_content", messages)
            }
    }

    fun setAllPastProposalToNotAvailable(messageID: Int) {
        db
            .collection("Chat")
            .document(this._pvtMyChat.value?.chatID!!)
            .get()
            .addOnSuccessListener { doc ->
                val chat = doc.toMyChatModel()
                chat!!.chatContent.removeAt(messageID)
                val messages = chat.chatContent
                for ((index, mess) in messages.withIndex()) {
                    if (index != messageID) {
                        mess.propState = 2
                    }
                }
                doc.reference.update("chat_content", messages)
            }
    }

    fun onBackReset() {
        this._pvtMyChat.value!!.hasEnded = false
    }

}
