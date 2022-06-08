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
    private val title: TextView = v.findViewById(R.id.commentTitle)
    private val body: TextView = v.findViewById(R.id.commentBody)

    /**
     * bind:
     * A method to bind the i-th entry of the commentsList to the i-th holder properties.
     * @param comment an object of class Service
     */
    fun bind(comment: String) {
        val list = comment.split("#*#")
        this.title.text = list[0]
        this.body.text = list[1]
    }
}