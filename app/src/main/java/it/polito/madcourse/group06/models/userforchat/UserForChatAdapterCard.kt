package it.polito.madcourse.group06.models.userforchat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import it.polito.madcourse.group06.R
import it.polito.madcourse.group06.models.userprofile.UserProfile
import it.polito.madcourse.group06.viewmodels.UserProfileViewModel

/**
 * [AdvAdapterCard] extending the Adapter of the [RecyclerView] and implements the required methods.
 */
class UserForChatAdapterCard(
    private val userList: List<UserProfile>,
    private val userProfileViewModel: UserProfileViewModel
) : RecyclerView.Adapter<UserForChatViewHolderCard>() {

    private var showedData = userList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserForChatViewHolderCard {
        val vg = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.show_user_list_single_user_item, parent, false)
        return UserForChatViewHolderCard(vg)
    }

    /**
     * Bind operations.
     */
    override fun onBindViewHolder(holder: UserForChatViewHolderCard, position: Int) {
        holder.bind(showedData[position], userProfileViewModel)
    }

    /**
     * Simply returns the size of the list of advertisement provided to the adapter.
     */
    override fun getItemCount(): Int {
        return showedData.size
    }
}