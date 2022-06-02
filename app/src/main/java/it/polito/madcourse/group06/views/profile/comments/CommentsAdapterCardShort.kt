package it.polito.madcourse.group06.views.profile.comments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.common.collect.Comparators.min
import it.polito.madcourse.group06.R

/**
 * [CommentsAdapterCardShort] extending the Adapter of the [RecyclerView] and implements the required methods.
 * This is the short version: it returns 3 elements at most.
 */
class CommentsAdapterCardShort(private val commentList: ArrayList<String>) : RecyclerView.Adapter<CommentsViewHolderCard>()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsViewHolderCard {
        val vg = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.comment_item, parent, false)
        return CommentsViewHolderCard(vg)
    }

    /**
     * Bind operations.
     */
    override fun onBindViewHolder(holder: CommentsViewHolderCard, position: Int) {
        holder.bind(commentList[position])
        /*holder.itemView.setOnClickListener { view ->
            /*Navigation.findNavController(view).navigate(R.id.action_showListOfSkills_to_ShowListTimeslots,
                bundleOf("selected_skill" to commentList[position]))*/
        }*/
    }

    /**
     * Simply returns the size of the list of comments provided to the adapter.
     * Show a maximum of 3 comments.
     */
    override fun getItemCount(): Int {
        return min(commentList.size, 3)
    }
}
