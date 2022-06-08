package it.polito.madcourse.group06.models.userforchat

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import it.polito.madcourse.group06.R
import it.polito.madcourse.group06.models.userprofile.UserProfile
import it.polito.madcourse.group06.viewmodels.UserProfileViewModel

/**
 * [UserForChatViewHolderCard] extends the ViewHolder of the [RecyclerView]
 * and provides the references to each component of the item card.
 */
class UserForChatViewHolderCard(v: View) : RecyclerView.ViewHolder(v) {
    private val fullname: TextView = v.findViewById(R.id.userFullname)
    private val nickname: TextView = v.findViewById(R.id.userNickname)
    private val profilePicture: ImageView = v.findViewById(R.id.userProfilePicture)

    /**
     * bind:
     * A method to bind the i-th entry of the adsList to the i-th holder properties.
     * @param user an object of class [UserProfile]
     */
    fun bind(user: UserProfile, userProfileViewModel: UserProfileViewModel) {
        this.fullname.text = user.fullName
        this.nickname.text = "@${user.nickname}"
        userProfileViewModel.retrieveProfilePicture(this.profilePicture, user.imgPath!!)
    }
}