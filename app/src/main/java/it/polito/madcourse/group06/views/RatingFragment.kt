package it.polito.madcourse.group06.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RatingBar
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.snackbar.Snackbar
import it.polito.madcourse.group06.R
import it.polito.madcourse.group06.activities.TBMainActivity
import it.polito.madcourse.group06.models.advertisement.Advertisement
import it.polito.madcourse.group06.models.userprofile.UserProfile
import it.polito.madcourse.group06.viewmodels.AdvertisementViewModel
import it.polito.madcourse.group06.viewmodels.UserProfileViewModel

class RatingFragment: Fragment() {
    private lateinit var activityTB: TBMainActivity

    private val advertisementViewModel: AdvertisementViewModel by activityViewModels()
    private val userProfileViewModel by activityViewModels<UserProfileViewModel>()

    private val dumbAdvertisement: Advertisement = Advertisement(
        "", "", "", arrayListOf<String>(),
        "", "", "", "", 0.0,
        "", "", 0.0, ""
    )
    private val dumbUser: UserProfile = UserProfile(null, null, null, null,
    null, null, null, null, null, 0.0, 0.0, 0,
        null, null, null)

    private val updatedCommentsServicesDoneList = ArrayList<String>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        activityTB = requireActivity() as TBMainActivity
        return inflater.inflate(R.layout.rating_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        advertisementViewModel.advertisement.observe(viewLifecycleOwner) { singleAdvertisement ->
            dumbAdvertisement.id = singleAdvertisement.id
            dumbAdvertisement.advAccount = singleAdvertisement.advAccount
            dumbAdvertisement.accountID = singleAdvertisement.accountID
            dumbAdvertisement.advTitle = singleAdvertisement.advTitle
            dumbAdvertisement.advLocation = singleAdvertisement.advLocation
            dumbAdvertisement.advDescription = singleAdvertisement.advDescription
            dumbAdvertisement.advDate = singleAdvertisement.advDate
            dumbAdvertisement.advStartingTime = singleAdvertisement.advStartingTime
            dumbAdvertisement.advEndingTime = singleAdvertisement.advEndingTime
            dumbAdvertisement.advDuration = singleAdvertisement.advDuration
            dumbAdvertisement.listOfSkills = singleAdvertisement.listOfSkills
        }

        userProfileViewModel.currentUser.observe(this.viewLifecycleOwner) { otherUser ->
            dumbUser.id = otherUser.id
            dumbUser.nickname = otherUser.nickname
            dumbUser.fullName = otherUser.fullName
            dumbUser.qualification = otherUser.qualification
            dumbUser.description = otherUser.description
            dumbUser.email = otherUser.email
            dumbUser.phoneNumber = otherUser.phoneNumber
            dumbUser.location = otherUser.location
            dumbUser.skills = otherUser.skills
            dumbUser.credit = otherUser.credit
            dumbUser.rating_sum = otherUser.rating_sum
            dumbUser.n_ratings = otherUser.n_ratings
            dumbUser.comments_services_rx = otherUser.comments_services_rx
            dumbUser.comments_services_done = otherUser.comments_services_done
            dumbUser.imgPath = otherUser.imgPath

            if (otherUser.comments_services_done != null) {
                for (comm in otherUser.comments_services_done!!) {
                    updatedCommentsServicesDoneList.add(comm)
                }
            }
        }

        val ratingBar = view.findViewById<RatingBar>(R.id.ratingBar)
        val submitRatingButton = view.findViewById<Button>(R.id.submitRating)

        ratingBar.setRating(0F)
        ratingBar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            if (rating <= 0) {
                // user is forbidden to vote 0 stars
                ratingBar.setRating(0.5F)
            }
        }

        submitRatingButton?.setOnClickListener {
            if (ratingBar.rating == 0F) {
                Snackbar.make(
                    requireView(), "Please vote your experience.", Snackbar.LENGTH_LONG
                ).show()
            } else {
                // save rating and comments
                val rating = ratingBar.rating.toDouble()
                val comment = view.findViewById<TextView>(R.id.comment_rating).text.toString()
                dumbAdvertisement.rating = ratingBar.rating.toDouble()
                dumbAdvertisement.comment = view.findViewById<TextView>(R.id.comment_rating).text.toString()
                advertisementViewModel.editAdvertisement(dumbAdvertisement)

                // save rating and comments in user profile
                dumbUser.rating_sum = dumbUser.rating_sum + rating
                dumbUser.n_ratings = dumbUser.n_ratings + 1
                userProfileViewModel.editUserProfile(dumbUser)
                if (comment != "") {
                    updatedCommentsServicesDoneList.add(comment)
                    userProfileViewModel.updateListOfCommentsServicesDone(updatedCommentsServicesDoneList)
                }

                // go back to timeslots list
                val frag = activity?.supportFragmentManager!!.findFragmentByTag("rating_fragment")
                activity?.supportFragmentManager?.beginTransaction()?.remove(frag!!)?.commit()
            }
        }

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (ratingBar.rating == 0F) {
                    Snackbar.make(
                        requireView(), "Please vote your experience.", Snackbar.LENGTH_LONG
                    ).show()
                } else {
                    // save rating and comments in advertisement
                    val rating = ratingBar.rating.toDouble()
                    val comment = view.findViewById<TextView>(R.id.comment_rating).text.toString()
                    dumbAdvertisement.rating = rating
                    dumbAdvertisement.comment = comment
                    advertisementViewModel.editAdvertisement(dumbAdvertisement)

                    // save rating and comments in user profile
                    dumbUser.rating_sum = dumbUser.rating_sum + rating
                    dumbUser.n_ratings = dumbUser.n_ratings + 1
                    dumbUser.comments_services_done?.add(comment)
                    userProfileViewModel.editUserProfile(dumbUser)

                    // go back to timeslots list
                    val frag = activity?.supportFragmentManager!!.findFragmentByTag("rating_fragment")
                    activity?.supportFragmentManager?.beginTransaction()?.remove(frag!!)?.commit()
                }
            }
        })
    }
}