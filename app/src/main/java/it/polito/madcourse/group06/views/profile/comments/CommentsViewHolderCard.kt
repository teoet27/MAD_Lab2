package it.polito.madcourse.group06.views.profile.comments

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import it.polito.madcourse.group06.R

/**
 * [CommentsViewHolderCard] extends the ViewHolder of the [RecyclerView]
 * and provides the references to each component of the comment card.
 */
class CommentsViewHolderCard(v: View) : RecyclerView.ViewHolder(v)  {
    val name: TextView = v.findViewById(R.id.commentCardTitle)

    /**
     * bind:
     * A method to bind the i-th entry of the commentsList to the i-th holder properties.
     * @param commentName an object of class Service
     */
    fun bind(commentName: String) {
        this.name.text = commentName
    }
}