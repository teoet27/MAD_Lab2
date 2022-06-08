package it.polito.madcourse.group06.models.mychat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import it.polito.madcourse.group06.R
import it.polito.madcourse.group06.viewmodels.AdvertisementViewModel

class MyChatAdapter(
    listOfMessages: List<MyMessage>,
    private val currentUserID: String,
    private val otherUserID: String,
    private val acceptCallback: (Double, Int, Long) -> Unit,
    private val rejectCallback: (Int, Long) -> Unit,
    private val advertisementViewModel: AdvertisementViewModel,
    private val isCurrentUserTheOwner: Boolean
) : RecyclerView.Adapter<MyChatViewHolder>() {
    private val chat: MutableList<MyMessage> = listOfMessages.toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyChatViewHolder {
        val vg: View
        val isMyMessage: Boolean
        when (viewType) {
            R.layout.my_message_layout -> {
                vg = LayoutInflater.from(parent.context)
                    .inflate(R.layout.my_message_layout, parent, false)
                isMyMessage = true
            }
            R.layout.other_message_layout -> {
                vg = LayoutInflater.from(parent.context)
                    .inflate(R.layout.other_message_layout, parent, false)
                isMyMessage = false
            }
            R.layout.common_my_message_layout -> {
                vg = LayoutInflater.from(parent.context)
                    .inflate(R.layout.common_my_message_layout, parent, false)
                isMyMessage = true
            }
            R.layout.common_other_message_layout -> {
                vg = LayoutInflater.from(parent.context)
                    .inflate(R.layout.common_other_message_layout, parent, false)
                isMyMessage = false
            }
            R.layout.my_proposal_item -> {
                vg = LayoutInflater.from(parent.context)
                    .inflate(R.layout.my_proposal_item, parent, false)
                isMyMessage = true
            }
            R.layout.other_proposal_item -> {
                vg = LayoutInflater.from(parent.context)
                    .inflate(R.layout.other_proposal_item, parent, false)
                isMyMessage = false
            }
            else -> {
                vg = LayoutInflater.from(parent.context)
                    .inflate(R.layout.my_message_layout, parent, false)
                isMyMessage = true
            }
        }
        return MyChatViewHolder(vg, isMyMessage, advertisementViewModel)
    }

    override fun onBindViewHolder(holder: MyChatViewHolder, position: Int) {
        val clientID = if (!isCurrentUserTheOwner) currentUserID else otherUserID

        holder.bind(
            chat[position], getItemViewType(position),
            ::acceptCallbackWrapper, ::rejectCallbackWrapper,
            this.chat[position].propState, clientID
        )
        holder.itemView.setOnClickListener {
            holder.setTimestampVisibility()
        }
    }

    override fun getItemCount(): Int {
        return chat.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (chat[position].senderID == currentUserID) {
            when {
                // special view for the proposal message
                chat[position].isAnOffer -> R.layout.my_proposal_item
                // if this is the last one
                position + 1 >= chat.size -> R.layout.my_message_layout
                // if there is an incoming change
                chat[position + 1].senderID == otherUserID -> R.layout.my_message_layout
                else -> R.layout.common_my_message_layout
            }
        } else {
            when {
                // special view for the proposal message
                chat[position].isAnOffer -> R.layout.other_proposal_item
                // if this is the last one
                position - 1 < 0 -> R.layout.other_message_layout
                // if there is a previous change
                chat[position - 1].senderID == currentUserID -> R.layout.other_message_layout
                else -> R.layout.common_other_message_layout
            }
        }
    }

    fun addMessage(message: MyMessage) {
        this.chat.add(this.chat.size, message)
        notifyItemInserted(this.chat.size - 1)
    }

    private fun acceptCallbackWrapper(time: Double, position: Int, state: Long) {
        this.acceptCallback(time, position, state)
        notifyDataSetChanged()
    }

    private fun rejectCallbackWrapper(position: Int, state: Long) {
        this.rejectCallback(position, state)
        notifyDataSetChanged()
    }
}