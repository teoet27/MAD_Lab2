package it.polito.madcourse.group06.models.mychat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import it.polito.madcourse.group06.R
import it.polito.madcourse.group06.viewmodels.MyMessage

class MyChatAdapter(private val chat: List<MyMessage>) : RecyclerView.Adapter<MyChatViewHolder>() {
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
            else -> {
                vg = LayoutInflater.from(parent.context)
                    .inflate(R.layout.my_message_layout, parent, false)
                isMyMessage = true
            }
        }
        return MyChatViewHolder(vg, isMyMessage)
    }

    override fun onBindViewHolder(holder: MyChatViewHolder, position: Int) {
        holder.bind(chat[position], getItemViewType(position))
        holder.itemView.setOnClickListener {
            if (holder.msgTimestamp.visibility == View.GONE)
                holder.msgTimestamp.visibility = View.VISIBLE
            else holder.msgTimestamp.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return chat.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (chat[position].senderID == "0") {
            when {
                // if this is the last one
                position + 1 >= chat.size -> R.layout.my_message_layout
                // if there is an incoming change
                chat[position + 1].senderID == "1" -> R.layout.my_message_layout
                else -> R.layout.common_my_message_layout
            }
        } else {
            when {
                // if this is the last one
                position - 1 < 0 -> R.layout.other_message_layout
                // if there is a previous change
                chat[position - 1].senderID == "0" -> R.layout.other_message_layout
                else -> R.layout.common_other_message_layout
            }
        }
    }
}