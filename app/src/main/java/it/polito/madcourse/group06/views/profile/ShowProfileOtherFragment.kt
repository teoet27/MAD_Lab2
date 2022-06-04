package it.polito.madcourse.group06.views.profile

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.AppCompatRatingBar
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import it.polito.madcourse.group06.R
import it.polito.madcourse.group06.activities.TBMainActivity
import it.polito.madcourse.group06.viewmodels.SharedViewModel
import it.polito.madcourse.group06.viewmodels.UserProfileViewModel
import it.polito.madcourse.group06.views.review.CommentFragment
import it.polito.madcourse.group06.views.profile.comments.CommentsAdapterCardShort


class ShowProfileOtherFragment : Fragment() {
    private lateinit var fullnameOBJ: TextView
    private lateinit var nicknameOBJ: TextView
    private lateinit var qualificationOBJ: TextView
    private lateinit var descriptionOBJ: TextView
    private lateinit var emailOBJ: TextView
    private lateinit var locationOBJ: TextView
    private lateinit var skillsOBJ: TextView
    private lateinit var phoneOBJ: TextView
    private lateinit var profilePictureOBJ: ImageView
    private lateinit var rateOBJ: TextView
    private lateinit var starsOBJ: AppCompatRatingBar
    private lateinit var sentenceCommentDone: TextView
    private lateinit var sentenceCommentRx: TextView
    private var commentsDone: ArrayList<String>? = ArrayList<String>()
    private var commentsRx: ArrayList<String>? = ArrayList<String>()
    private lateinit var showMoreComments: TextView
    private lateinit var confirmedBadge: ImageView
    private lateinit var skillsChips: ChipGroup

    private val userProfileViewModel: UserProfileViewModel by activityViewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private lateinit var recyclerViewCommentsDone: RecyclerView
    private lateinit var recyclerViewCommentsRx: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (requireActivity() as TBMainActivity).supportActionBar?.
            setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
        return inflater.inflate(R.layout.show_profile_other, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.fullnameOBJ = view.findViewById(R.id.fullname_ID_other)
        this.nicknameOBJ = view.findViewById(R.id.nickname_ID_other)
        this.qualificationOBJ = view.findViewById(R.id.qualification_ID_other)
        this.descriptionOBJ = view.findViewById(R.id.description_show_ID_other)
        this.emailOBJ = view.findViewById(R.id.email_show_ID_other)
        this.locationOBJ = view.findViewById(R.id.loc_show_ID_other)
        this.skillsOBJ = view.findViewById(R.id.skillsListID_other)
        this.phoneOBJ = view.findViewById(R.id.phone_show_ID_other)
        this.profilePictureOBJ = view.findViewById(R.id.profilePictureID_other)
        this.rateOBJ = view.findViewById(R.id.rating_title_other)
        this.starsOBJ = view.findViewById(R.id.ratingBar_other)
        this.sentenceCommentDone = view.findViewById(R.id.comments_other_done)
        this.sentenceCommentRx = view.findViewById(R.id.comment_other_rx)
        this.showMoreComments = view.findViewById(R.id.show_more_comments)
        this.confirmedBadge=view.findViewById(R.id.edit_camera_button_other)
        this.skillsChips = view.findViewById(R.id.skill_chips_group_other)

        (requireActivity() as TBMainActivity).supportActionBar?.title="User Profile"

        userProfileViewModel.otherUser.observe(this.viewLifecycleOwner) { userProfile ->

            // Show confirmed badge only if the user has been rated by others (i.e. he's an active user)
            this.confirmedBadge.visibility=if(userProfile.comments_services_done.isNullOrEmpty()) View.GONE else View.VISIBLE

            // Fullname
            this.fullnameOBJ.text = userProfile.fullName

            // Nickname
            if (userProfile.nickname?.compareTo("") == 0) {
                this.nicknameOBJ.text = "No nickname provided."
            } else {
                this.nicknameOBJ.text = "@${userProfile.nickname}"
            }

            // Qualification
            this.qualificationOBJ.text = userProfile.qualification
            // Phone Number
            this.phoneOBJ.text = userProfile.phoneNumber
            // Location
            this.locationOBJ.text = userProfile.location

            // Skills
            if (userProfile.skills.isNullOrEmpty()) {
                this.skillsChips.isVisible = false
                this.skillsOBJ.isVisible = true
                this.skillsOBJ.text = getString(R.string.noskills)
            } else {
                this.skillsOBJ.isVisible = false
                this.skillsChips.isVisible = true
                this.skillsChips.removeAllViews()
                userProfile.skills!!.forEach { sk ->
                    this.skillsChips.addChip(requireContext(), sk)
                    this.skillsChips.setOnCheckedChangeListener { chipGroup, checkedId ->
                        val selectedService = chipGroup.findViewById<Chip>(checkedId)?.text
                        Toast.makeText(
                            chipGroup.context,
                            selectedService ?: "No Choice",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

            // Email
            this.emailOBJ.text = userProfile.email
            // Description
            this.descriptionOBJ.text = userProfile.description

            // Profile Picture
            if (userProfile.imgPath.isNullOrEmpty()) {
                userProfileViewModel.retrieveStaticProfilePicture(profilePictureOBJ)
            } else {
                userProfileViewModel.retrieveProfilePicture(profilePictureOBJ, userProfile.imgPath!!)
            }

            // rating
            if (userProfile.n_ratings != 0.0) {
                this.starsOBJ.rating = (userProfile.rating_sum / userProfile.n_ratings).toFloat()
                this.starsOBJ.visibility = View.VISIBLE
            }
            else {
                this.starsOBJ.visibility = View.GONE
            }

            // reviews -> comments-done (recyclerView)
            this.commentsDone = userProfile.comments_services_done
            recyclerViewCommentsDone = view.findViewById(R.id.commentsDoneRecyclerView)
            recyclerViewCommentsDone.layoutManager = LinearLayoutManager(view.getContext())
            recyclerViewCommentsDone.adapter = userProfile.comments_services_done?.let { CommentsAdapterCardShort(it) }
            if (userProfile.comments_services_done == null || userProfile.comments_services_done!!.size == 0) {
                sentenceCommentDone.visibility = View.GONE
            } else {
                sentenceCommentDone.visibility = View.VISIBLE
            }

            // reviews -> comments-rx (recyclerView)
            this.commentsRx = userProfile.comments_services_rx
            recyclerViewCommentsRx = view.findViewById(R.id.commentsRxRecyclerView)
            recyclerViewCommentsRx.layoutManager = LinearLayoutManager(view.getContext())
            recyclerViewCommentsRx.adapter = userProfile.comments_services_rx?.let { CommentsAdapterCardShort(it) }
            if (userProfile.comments_services_rx == null || userProfile.comments_services_rx!!.size == 0) {
                sentenceCommentRx.visibility = View.GONE
            } else {
                sentenceCommentRx.visibility = View.VISIBLE
            }

            // display the 'show more...' button only if some comments are not shown here because they exceed the limit of 3
            if ((userProfile.comments_services_done != null && userProfile.comments_services_done!!.size > 3) || (userProfile.comments_services_rx != null && userProfile.comments_services_rx!!.size > 3)) {
                this.showMoreComments.visibility = View.VISIBLE
                this.showMoreComments.isClickable = true
            } else {
                this.showMoreComments.visibility = View.GONE
            }

            // show all comments
            this.showMoreComments.setOnClickListener {
                val frag = CommentFragment()
                activity?.supportFragmentManager!!.beginTransaction()
                    .add(R.id.nav_host_fragment_content_main, frag, "comment_fragment")
                    .commit()
                }
            }
        }

        this.starsOBJ.isFocusableInTouchMode = false
        this.starsOBJ.isClickable = false
        this.starsOBJ.stepSize = 0.25F

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                sharedViewModel.updateSearchState()
                val frag = activity?.supportFragmentManager!!.findFragmentByTag("other_user_profile")
                activity?.supportFragmentManager?.beginTransaction()?.remove(frag!!)?.commit()
                (requireActivity() as TBMainActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp)
            }
        })

        // check this option to open onCreateOptionsMenu method
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.findItem(R.id.action_search).isVisible=false
        super.onCreateOptionsMenu(menu, inflater)
    }

    // this event will enable the back
    // function to the button on press
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                sharedViewModel.updateSearchState()
                (requireActivity() as TBMainActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp)
                activity?.supportFragmentManager!!.findFragmentByTag("other_user_profile")?.also {frag->
                    activity?.supportFragmentManager?.beginTransaction()?.remove(frag!!)?.commit()
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
    /**
     * Dinamically create a chip within a chip group
     *
     * @param context       parent view context
     * @param label         chip name
     */
    private fun ChipGroup.addChip(context: Context, label: String) {
        Chip(context).apply {
            id = View.generateViewId()
            text = label
            isClickable = true
            isCheckable = false
            isCheckedIconVisible = false
            isFocusable = true
            addView(this)
        }
    }
}