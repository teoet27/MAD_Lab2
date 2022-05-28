package it.polito.madcourse.group06.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RatingBar
import android.widget.RatingBar.OnRatingBarChangeListener
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import it.polito.madcourse.group06.R
import it.polito.madcourse.group06.activities.TBMainActivity

class RatingFragment: Fragment() {
    private lateinit var activityTB: TBMainActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        activityTB = requireActivity() as TBMainActivity
        return inflater.inflate(R.layout.fragment_rating, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
            val msg = ratingBar.rating.toString()

            if (ratingBar.rating == 0F) {
                Snackbar.make(
                    requireView(), "Please vote your experience.", Snackbar.LENGTH_LONG
                ).show()
            }
            else {
                // TODO: do something with the rating
                Snackbar.make(
                    requireView(), "Rating is: " + msg, Snackbar.LENGTH_LONG
                ).show()
            }
        }

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_ratingFragment_to_showProfileOtherFragment)
            }
        })
    }
}