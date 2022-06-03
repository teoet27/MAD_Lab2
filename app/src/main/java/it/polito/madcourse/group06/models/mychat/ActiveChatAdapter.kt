package it.polito.madcourse.group06.models.mychat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import it.polito.madcourse.group06.R

class ActiveChatAdapter(private val listOfChat: ArrayList<ActiveChat>) : RecyclerView.Adapter<ActiveChatViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActiveChatViewHolder {
        val vg = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.active_chat_item, parent, false)
        return ActiveChatViewHolder(vg)
    }

    override fun onBindViewHolder(holder: ActiveChatViewHolder, position: Int) {
        holder.bind(listOfChat[position].advTitle, listOfChat[position].userFullname) { }
    }

    override fun getItemCount(): Int {
        return listOfChat.size
    }

    fun addActiveChat(activeChat: ActiveChat) {
        listOfChat.add(activeChat)
        notifyItemInserted(listOfChat.size - 1)
    }
}