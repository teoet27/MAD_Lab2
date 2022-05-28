package it.polito.madcourse.group06.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RatingBar
import android.widget.RatingBar.OnRatingBarChangeListener
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import it.polito.madcourse.group06.R
import it.polito.madcourse.group06.activities.TBMainActivity
import it.polito.madcourse.group06.models.advertisement.Advertisement
import it.polito.madcourse.group06.viewmodels.AdvertisementViewModel

class RatingFragment: Fragment() {
    private lateinit var activityTB: TBMainActivity
    private val advertisementViewModel: AdvertisementViewModel by activityViewModels()
    private val dumbAdvertisement: Advertisement = Advertisement(
        "", "", "", arrayListOf<String>(),
        "", "", "", "", 0.0,
        "", "", 0.0, ""
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        activityTB = requireActivity() as TBMainActivity
        return inflater.inflate(R.layout.fragment_rating, container, false)
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

        val ratingBar = view.findViewById<RatingBar>(R.id.ratingBar)
        val submitRatingButton = view.findViewById<Button>(R.id.submitRating)

        ratingBar.setRating(0F)
        ratingBar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            if (rating <= 0) {
                // user is forbidden to vote 0 stars
                ratingBar.setRating(0.5F)
            }

            val msg = ratingBar.rating.toString()
            Snackbar.make(
                requireView(), "Rating is " + msg + ".", Snackbar.LENGTH_LONG
            ).show()
        }

        submitRatingButton?.setOnClickListener {
            if (ratingBar.rating == 0F) {
                Snackbar.make(
                    requireView(), "Please vote your experience.", Snackbar.LENGTH_LONG
                ).show()
            } else {
                // save rating and comments
                dumbAdvertisement.rating = ratingBar.rating.toDouble()
                dumbAdvertisement.comment = view.findViewById<TextView>(R.id.comment_rating).text.toString()
                advertisementViewModel.editAdvertisement(dumbAdvertisement)
                // go back to timeslots list
                findNavController().navigate(R.id.action_ratingFragment_to_ShowListTimeslots)
            }
        }

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (ratingBar.rating == 0F) {
                    Snackbar.make(
                        requireView(), "Please vote your experience.", Snackbar.LENGTH_LONG
                    ).show()
                } else {
                    // save rating and comments
                    dumbAdvertisement.rating = ratingBar.rating.toDouble()
                    dumbAdvertisement.comment = view.findViewById<TextView>(R.id.comment_rating).text.toString()
                    advertisementViewModel.editAdvertisement(dumbAdvertisement)
                    // go back to timeslots list
                    findNavController().navigate(R.id.action_ratingFragment_to_ShowListTimeslots)
                }            }
        })
    }
}