package it.polito.madcourse.group06.models.mychat

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import it.polito.madcourse.group06.R

class ActiveChatViewHolder(v: View): RecyclerView.ViewHolder(v) {
    private val advName: TextView = v.findViewById(R.id.activeAdvTitle)
    private val advOwner: TextView = v.findViewById(R.id.activeAccountFullname)
    private val goToChatButton: ImageView = v.findViewById(R.id.goToChatButton)

    fun bind(advName: String, advOwner: String, goToChatCallback: ()->Unit) {
        this.advName.text = advName
        this.advOwner.text = advOwner
        this.goToChatButton.setOnClickListener { goToChatCallback() }
    }

}