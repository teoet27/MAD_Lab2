package it.polito.madcourse.group06.views.mychat

import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import it.polito.madcourse.group06.R
import it.polito.madcourse.group06.activities.TBMainActivity
import it.polito.madcourse.group06.models.mychat.MyChatAdapter
import it.polito.madcourse.group06.viewmodels.MyChatViewModel
import it.polito.madcourse.group06.viewmodels.UserProfileViewModel
import it.polito.madcourse.group06.models.mychat.MyMessage
import it.polito.madcourse.group06.utilities.hoursToCredit
import it.polito.madcourse.group06.utilities.timeDoubleHourToString
import it.polito.madcourse.group06.viewmodels.AdvertisementViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.floor

class MyChat : Fragment() {

    private val myChatViewModel by activityViewModels<MyChatViewModel>()
    private val advertisementViewModel by activityViewModels<AdvertisementViewModel>()
    private val userProfileViewModel by activityViewModels<UserProfileViewModel>()

    private lateinit var recyclerView: RecyclerView
    private lateinit var chatAdapterCard: MyChatAdapter
    private lateinit var activityTB: TBMainActivity
    private lateinit var chatFullname: TextView
    private lateinit var chatNickname: TextView
    private lateinit var chattingUserProfilePicture: ImageView
    private lateinit var emptyChatMessage: TextView
    private lateinit var inputMessageBox: EditText
    private lateinit var sendMessageButton: ImageView
    private lateinit var backArrow: ImageView
    private lateinit var myPurposeContainer: ScrollView
    private lateinit var chatAcceptButton: TextView
    private lateinit var chatRejectButton: TextView
    private lateinit var chatArrowUpButton: ImageView
    private lateinit var myLocation: EditText
    private lateinit var myStartingTime: TextView
    private lateinit var myDuration: TextView
    private lateinit var sendProposalButton: ImageView
    private lateinit var profilePictureBlack: ImageView
    private lateinit var profilePictureExtended: ImageView
    private lateinit var profilePictureClose: ImageView
    private lateinit var profilePictureCloseText: TextView
    private var isProfilePictureShown = false
    private var hasChatEnded = false
    private var fromWhere = 0
    private var startingTimeHourProposal = 0
    private var startingTimeMinuteProposal = 0
    private var durationTimeProposal = 0.0
    private var listOfMessages = arrayListOf<MyMessage>()
    private var currentID = ""
    private var otherID = ""
    private var otherCredit = 0.0
    private var myCredit = 0.0
    private var chatID = ""
    private var chatMenuArrowStartingPositionY = 0.0f
    private var chatMenuArrowStartingPositionX = 0.0f
    private var isAnswerMenuOpen = false
    private var isCurrentUserTheOwner = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activityTB = requireActivity() as TBMainActivity
        return inflater.inflate(R.layout.chat_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activityTB.supportActionBar?.hide()
        this.recyclerView = view.findViewById(R.id.chatContentViewID)
        this.chatFullname = view.findViewById(R.id.chatFullnameID)
        this.chatNickname = view.findViewById(R.id.chatNicknameID)
        this.chattingUserProfilePicture = view.findViewById(R.id.chattingUserProfilePicture)
        this.emptyChatMessage = view.findViewById(R.id.emptyChatMessageID)
        this.inputMessageBox = view.findViewById(R.id.inputMessageBox)
        this.sendMessageButton = view.findViewById(R.id.chatSendMessageButtonID)
        this.backArrow = view.findViewById(R.id.chatBackArrowID)
        this.chatAcceptButton = view.findViewById(R.id.chatAcceptTextViewID)
        this.chatRejectButton = view.findViewById(R.id.chatRejectTextViewID)
        this.chatArrowUpButton = view.findViewById(R.id.chatMenuArrowID)
        this.myPurposeContainer = view.findViewById(R.id.myPurposeScrollViewID)
        this.myLocation = view.findViewById(R.id.myLocationTVID)
        this.myStartingTime = view.findViewById(R.id.myStartingTimeTVID)
        this.myDuration = view.findViewById(R.id.myDurationTVID)
        this.sendProposalButton = view.findViewById(R.id.sendProposalButtonID)
        this.profilePictureBlack = view.findViewById(R.id.profilePictureBlackBack)
        this.profilePictureExtended = view.findViewById(R.id.profilePictureExtended)
        this.profilePictureClose = view.findViewById(R.id.profilePictureCloseButton)
        this.profilePictureCloseText = view.findViewById(R.id.profilePictureCloseTextView)

        this.myPurposeContainer.alpha = 0f
        this.chatMenuArrowStartingPositionY = this.chatArrowUpButton.y
        this.chatMenuArrowStartingPositionX = this.chatArrowUpButton.x

        arguments?.getString("fromWhere")?.also { fromWhere ->
            this.fromWhere = fromWhere.toInt()
        }

        userProfileViewModel.currentUser.observe(viewLifecycleOwner) { user ->
            this.currentID = user.id!!
            this.myCredit = user.credit
            advertisementViewModel.advertisement.observe(viewLifecycleOwner) { adv ->
                this.isCurrentUserTheOwner = adv.accountID == this.currentID
            }
        }

        myChatViewModel.chattingUser.observe(viewLifecycleOwner) {
            this.chatFullname.text = it.fullName
            this.chatNickname.text = "@${it.nickname}"
            userProfileViewModel.retrieveProfilePicture(this.chattingUserProfilePicture, it.imgPath?:"staticuser")
            userProfileViewModel.retrieveProfilePicture(this.profilePictureExtended, it.imgPath?:"staticuser")
            this.otherID = it.id!!
            this.otherCredit = it.credit
        }

        myChatViewModel.myCurrentChat.observe(viewLifecycleOwner) {
            this.chatID = it.chatID
            this.listOfMessages = it.chatContent
            this.hasChatEnded = it.hasEnded
            if (it.hasEnded) {
                this.chatArrowUpButton.setOnClickListener {
                    Toast.makeText(requireContext(), "Congratulations! You and ${this.chatFullname.text} have reached an agreement!", Toast.LENGTH_LONG).show()
                }
                this.chatArrowUpButton.setImageResource(R.drawable.ic_done_black_24dp)
                this.chatArrowUpButton.backgroundTintList = AppCompatResources.getColorStateList(
                    requireContext(),
                    R.color.accept_color
                )
            } else {
                this.chatArrowUpButton.setImageResource(R.drawable.ic_add_black_24dp)
                this.chatArrowUpButton.backgroundTintList = AppCompatResources.getColorStateList(
                    requireContext(),
                    R.color.darkGray
                )
                this.chatArrowUpButton.setOnClickListener {
                    if (!this.isAnswerMenuOpen) {
                        openNewProposal()
                        this.isAnswerMenuOpen = !this.isAnswerMenuOpen
                    } else {
                        closeProposal()
                        this.isAnswerMenuOpen = !this.isAnswerMenuOpen
                    }
                }
            }
        }

        this.profilePictureBlack.isGone = true
        this.profilePictureBlack.alpha = 0f
        this.profilePictureExtended.isGone = true
        this.profilePictureExtended.alpha = 0f
        this.profilePictureClose.isGone = true
        this.profilePictureClose.alpha = 0f
        this.profilePictureCloseText.isGone = true
        this.profilePictureCloseText.alpha = 0f

        this.profilePictureBlack.setOnClickListener { }

        this.profilePictureClose.setOnClickListener {
            if(this.isProfilePictureShown){
                this.profilePictureBlack.isGone = true
                this.profilePictureBlack.alpha = 0f
                this.profilePictureExtended.isGone = true
                this.profilePictureExtended.alpha = 0f
                this.profilePictureClose.isGone = true
                this.profilePictureClose.alpha = 0f
                this.profilePictureCloseText.isGone = true
                this.profilePictureCloseText.alpha = 0f
                this.isProfilePictureShown = false
            }
        }

        this.profilePictureCloseText.setOnClickListener {
            if(this.isProfilePictureShown){
                this.profilePictureBlack.isGone = true
                this.profilePictureBlack.alpha = 0f
                this.profilePictureExtended.isGone = true
                this.profilePictureExtended.alpha = 0f
                this.profilePictureClose.isGone = true
                this.profilePictureClose.alpha = 0f
                this.profilePictureCloseText.isGone = true
                this.profilePictureCloseText.alpha = 0f
                this.isProfilePictureShown = false
            }
        }

        this.chattingUserProfilePicture.setOnClickListener {
            if(!this.isProfilePictureShown){
                this.profilePictureBlack.isGone = false
                this.profilePictureExtended.isGone = false
                this.profilePictureClose.isGone = false
                this.profilePictureCloseText.isGone = false

                this.profilePictureBlack.animate().apply {
                    duration = 150
                    alpha(1f)
                }.start()
                this.profilePictureExtended.animate().apply {
                    duration = 500
                    alpha(1f)
                }.start()
                this.profilePictureClose.animate().apply {
                    duration = 750
                    alpha(1f)
                }.start()
                this.profilePictureCloseText.animate().apply {
                    duration = 750
                    alpha(1f)
                }.start()

                this.isProfilePictureShown = true
            }
        }

        this.sendMessageButton.setOnClickListener {
            if (this.inputMessageBox.text.isNotEmpty()) {
                /**
                 * Add the new message on the remote DB
                 */
                val msg = MyMessage(
                    currentID, otherID,
                    this.inputMessageBox.text.toString(),
                    "", "", "",
                    SimpleDateFormat(
                        "dd/MM/yyyy HH:mm",
                        Locale.getDefault()
                    ).format(Date()).toString(), false, 0
                )
                this.listOfMessages.add(msg)
                myChatViewModel.addNewMessage(this.chatID, this.listOfMessages)

                /**
                 * Add the message to the adapter
                 */
                chatAdapterCard.addMessage(msg)

                // Set the input message box to an empty textview
                this.inputMessageBox.setText("")

                // Set the linear layout of the recycler view
                val linearLayoutManager = LinearLayoutManager(this.context)
                linearLayoutManager.stackFromEnd = true
                this.recyclerView.layoutManager = linearLayoutManager
                this.recyclerView.adapter = chatAdapterCard
            }
        }

        this.myStartingTime.setOnClickListener { popTimePickerStarting(this.myStartingTime) }
        this.myDuration.setOnClickListener { popTimePickerDuration(this.myDuration) }

        this.sendProposalButton.setOnClickListener {
            if (this.myLocation.text.toString().isNotEmpty() &&
                this.myStartingTime.text.toString().isNotEmpty() &&
                this.myDuration.text.toString().isNotEmpty()
            ) {
                val msg = MyMessage(
                    currentID, otherID,
                    "",
                    this.myLocation.text.toString(),
                    this.myStartingTime.text.toString(),
                    timeDoubleHourToString(this.durationTimeProposal),
                    SimpleDateFormat(
                        "dd/MM/yyyy HH:mm",
                        Locale.getDefault()
                    ).format(Date()).toString(), true, 0
                )
                this.listOfMessages.add(msg)
                myChatViewModel.addNewMessage(this.chatID, this.listOfMessages)

                /**
                 * Add the message to the adapter
                 */
                chatAdapterCard.addMessage(msg)

                // Set the input message box to an empty textview
                this.inputMessageBox.setText("")

                // Set the linear layout of the recycler view
                val linearLayoutManager = LinearLayoutManager(this.context)
                linearLayoutManager.stackFromEnd = true
                this.recyclerView.layoutManager = linearLayoutManager
                this.recyclerView.adapter = chatAdapterCard
                closeProposal()
                resetProposalFields()
                this.isAnswerMenuOpen = !this.isAnswerMenuOpen
            } else {
                Toast.makeText(requireContext(), "Incomplete proposal.", Toast.LENGTH_LONG).show()
            }
        }

        this.chatArrowUpButton.setOnClickListener {
            if (!this.isAnswerMenuOpen) {
                openNewProposal()
                this.isAnswerMenuOpen = !this.isAnswerMenuOpen
            } else {
                closeProposal()
                this.isAnswerMenuOpen = !this.isAnswerMenuOpen
            }
        }

        this.backArrow.setOnClickListener {
            if (fromWhere == 0 /*from the show single advertisement*/) {
                myChatViewModel.onBackReset()
                findNavController().navigate(R.id.action_myChat_to_ShowListOfServices)
            } else if (fromWhere == 1 /*from the mychat menu*/) {
                myChatViewModel.onBackReset()
                findNavController().navigate(R.id.action_myChat_to_activeChats)
            }
            activityTB.supportActionBar?.show()
        }

        myChatViewModel.myCurrentChat.observe(viewLifecycleOwner) { chat ->
            this.emptyChatMessage.isVisible = chat.chatContent.isEmpty()
            chatAdapterCard = MyChatAdapter(
                chat.chatContent, currentID, otherID,
                ::acceptProposal, ::rejectProposal, advertisementViewModel, isCurrentUserTheOwner
            )

            val linearLayoutManager = LinearLayoutManager(this.context)
            linearLayoutManager.stackFromEnd = true
            this.recyclerView.layoutManager = linearLayoutManager
            this.recyclerView.adapter = chatAdapterCard
        }

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (fromWhere == 0 /*from the show single advertisement*/) {
                    myChatViewModel.onBackReset()
                    findNavController().navigate(R.id.action_myChat_to_ShowListOfServices)
                } else if (fromWhere == 1 /*from the mychat menu*/) {
                    myChatViewModel.onBackReset()
                    findNavController().navigate(R.id.action_myChat_to_activeChats)
                }
                activityTB.supportActionBar?.show()
            }
        })
    }

    private fun openNewProposal() {
        // should be opened
        this.recyclerView.animate().apply {
            alpha(0.3f)
        }.start()
        this.inputMessageBox.isEnabled = false
        this.sendMessageButton.isEnabled = false
        this.myLocation.isEnabled = true
        this.myStartingTime.isEnabled = true
        this.myDuration.isEnabled = true
        this.sendProposalButton.isEnabled = true
        this.inputMessageBox.animate().apply {
            alpha(0.3f)
        }.start()
        this.sendMessageButton.animate().apply {
            alpha(0.3f)
        }.start()

        this.chatArrowUpButton.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24)
        this.chatArrowUpButton.backgroundTintList = AppCompatResources.getColorStateList(
            requireContext(),
            R.color.prussian_blue
        )
        this.myPurposeContainer.isGone = false
        this.myPurposeContainer.animate().apply {
            duration = 350
            alpha(1f)
            translationY(-25f)
        }.start()
        this.chatArrowUpButton.animate().apply {
            duration = 250
            rotationX(180f)
        }.start()
        this.sendProposalButton.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in_from_right))
        this.myLocation.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in_up))
        this.myStartingTime.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in_up))
        this.myDuration.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in_up))

    }

    private fun closeProposal() {
        // should be closed
        this.recyclerView.animate().apply {
            alpha(1f)
        }.start()
        this.inputMessageBox.isEnabled = true
        this.sendMessageButton.isEnabled = true
        this.myLocation.isEnabled = false
        this.myStartingTime.isEnabled = false
        this.myDuration.isEnabled = false
        this.sendProposalButton.isEnabled = false
        this.inputMessageBox.animate().apply {
            alpha(1f)
        }.start()
        this.sendMessageButton.animate().apply {
            alpha(1f)
        }.start()

        this.chatArrowUpButton.setImageResource(R.drawable.ic_add_black_24dp)
        this.chatArrowUpButton.backgroundTintList = AppCompatResources.getColorStateList(
            requireContext(),
            R.color.darkGray
        )
        this.myPurposeContainer.animate().apply {
            duration = 250
            alpha(0f)
            translationY(55f)
        }.withEndAction {
            this.myPurposeContainer.isGone = true
        }.start()
        this.chatArrowUpButton.animate().apply {
            duration = 250
            rotationX(0f)
        }.start()
    }

    private fun resetProposalFields() {
        this.myLocation.setText("")
        this.myStartingTime.text = ""
        this.myDuration.text = ""
    }

    private fun acceptProposal(duration: Double, messageID: Int, messageState: Long) {
        val cost = hoursToCredit(duration)
        if (!this.isCurrentUserTheOwner) {
            if (this.myCredit >= cost) {
                myChatViewModel.addCreditToChattingUser(cost.toDouble())
                userProfileViewModel.deductCreditToCurrentUser(cost.toDouble())
                myChatViewModel.setProposalState(messageID, messageState)
                myChatViewModel.concludeChat()
                switchToDoneMode()
            } else {
                Toast.makeText(
                    requireContext(),
                    "You can't afford to pay for this.",
                    Toast.LENGTH_LONG
                ).show()
            }
        } else {
            if (this.otherCredit >= cost) {
                myChatViewModel.deductCreditFromChattingUser(cost.toDouble())
                userProfileViewModel.addCreditToCurrentUser(cost.toDouble())
                myChatViewModel.setProposalState(messageID, messageState)
                myChatViewModel.concludeChat()
                switchToDoneMode()
            } else {
                Toast.makeText(
                    requireContext(),
                    "This proposal is now unavailable; contact ${this.chatFullname.text} to set up a new proposal.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

    }

    private fun rejectProposal(messageID: Int, messageState: Long) {
        myChatViewModel.setProposalState(messageID, messageState)
    }

    /**
     * A function to provide feedback to the user after a proposal has been accepted
     */
    private fun switchToDoneMode() {
        this.chatArrowUpButton.setOnClickListener {
            Toast.makeText(requireContext(), "Congratulations! You and ${this.chatFullname.text} have reached an agreement!", Toast.LENGTH_LONG).show()
        }
        this.chatArrowUpButton.animate().apply {
            duration = 500
            rotationX(360f)
            alpha(1f)
        }.start()
        this.chatArrowUpButton.setImageResource(R.drawable.ic_done_black_24dp)
        this.chatArrowUpButton.backgroundTintList = AppCompatResources.getColorStateList(
            requireContext(),
            R.color.accept_color
        )
    }

    /**
     * popTimePickerStarting is the callback to launch the TimePicker for inserting the starting time
     *
     * @param timeBox reference to the TextView of the starting time
     */
    private fun popTimePickerStarting(timeBox: TextView) {
        val onTimeSetListener: TimePickerDialog.OnTimeSetListener = TimePickerDialog.OnTimeSetListener() { _, selectedHour, selectedMinute ->
            this.startingTimeHourProposal = selectedHour
            this.startingTimeMinuteProposal = selectedMinute
            timeBox.text = String.format(Locale.getDefault(), "%02d:%02d", startingTimeHourProposal, startingTimeMinuteProposal)
        }
        val timePickerDialog = TimePickerDialog(this.context, onTimeSetListener, startingTimeHourProposal, startingTimeMinuteProposal, true)
        timePickerDialog.setTitle("Select time")
        timePickerDialog.show()
    }

    /**
     * popTimePickerDuration is the callback to launch the TimePicker for inserting the duration
     *
     * @param timeBox reference to the TextView of the duration
     */
    private fun popTimePickerDuration(timeBox: TextView) {
        val onTimeSetListener: TimePickerDialog.OnTimeSetListener = TimePickerDialog.OnTimeSetListener() { _, selectedHour, selectedMinute ->
            durationTimeProposal = selectedHour.toDouble() + selectedMinute.toDouble() / 60
            timeBox.text = String.format(Locale.getDefault(), "%d h %d min", selectedHour, selectedMinute)
        }
        val timePickerDialog = TimePickerDialog(
            this.context, onTimeSetListener,
            floor(durationTimeProposal).toInt(), ((durationTimeProposal - floor(durationTimeProposal)) * 60).toInt(), true
        )
        timePickerDialog.setTitle("Select Duration")
        timePickerDialog.show()
    }
}