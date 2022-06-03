package it.polito.madcourse.group06.views.mychat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import it.polito.madcourse.group06.R
import it.polito.madcourse.group06.activities.TBMainActivity
import it.polito.madcourse.group06.models.mychat.MyChatAdapter
import it.polito.madcourse.group06.viewmodels.MyMessage
import it.polito.madcourse.group06.viewmodels.UserProfileViewModel
import java.text.SimpleDateFormat
import java.util.*

class MyChat : Fragment() {

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
    private lateinit var myPurposeContainer: LinearLayout
    private lateinit var chatAcceptButton: TextView
    private lateinit var chatRejectButton: TextView
    private lateinit var chatArrowUpButton: ImageView
    private var myID = ""
    private var otherID = ""
    private var chatMenuArrowStartingPositionY = 0.0f
    private var chatMenuArrowStartingPositionX = 0.0f
    private var isAnswerMenuOpen = false

    /**
     * Temporary list of MyMessages to test the front-end
     */
    private val listOfMessages = mutableListOf<MyMessage>(
        MyMessage("0", "1", "yo wyd?", "31/05/2022 14:30", "0"),
        MyMessage("0", "1", "you still interested?", "31/05/2022 14:31", "0"),
        MyMessage("1", "0", "sorry, i've been sleeping till now... :D", "31/05/2022 16:45", "0"),
        MyMessage("1", "0", "can we still make this up?", "31/05/2022 16:47", "0"),
        MyMessage("1", "0", "ayooo?", "01/06/2022 10:44", "0"),
        MyMessage("0", "1", "stop playing bro .-.", "01/06/2022 10:46", "0"),
        MyMessage("0", "1", "yo wyd?", "31/05/2022 14:30", "0"),
        MyMessage("0", "1", "you still interested?", "31/05/2022 14:31", "0"),
        MyMessage("1", "0", "sorry, i've been sleeping till now... :D", "31/05/2022 16:45", "0"),
        MyMessage("1", "0", "can we still make this up?", "31/05/2022 16:47", "0"),
        MyMessage("1", "0", "ayooo?", "01/06/2022 10:44", "0"),
        MyMessage("0", "1", "stop playing bro .-.", "01/06/2022 10:46", "0"),
        MyMessage("0", "1", "yo wyd?", "31/05/2022 14:30", "0"),
        MyMessage("0", "1", "you still interested?", "31/05/2022 14:31", "0"),
        MyMessage("1", "0", "sorry, i've been sleeping till now... :D", "31/05/2022 16:45", "0"),
        MyMessage("1", "0", "can we still make this up?", "31/05/2022 16:47", "0"),
        MyMessage("1", "0", "ayooo?", "01/06/2022 10:44", "0"),
        MyMessage("0", "1", "stop playing bro .-.", "01/06/2022 10:46", "0"),
        MyMessage("0", "1", "yo wyd?", "31/05/2022 14:30", "0"),
        MyMessage("0", "1", "you still interested?", "31/05/2022 14:31", "0"),
        MyMessage("1", "0", "sorry, i've been sleeping till now... :D", "31/05/2022 16:45", "0"),
        MyMessage("1", "0", "can we still make this up?", "31/05/2022 16:47", "0"),
        MyMessage("1", "0", "ayooo?", "01/06/2022 10:44", "0"),
        MyMessage("0", "1", "stop playing bro .-.", "01/06/2022 10:46", "0"),
    )

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
        this.myPurposeContainer = view.findViewById(R.id.myPurposeID)

        this.myPurposeContainer.alpha = 0f
        this.emptyChatMessage.isVisible = this.listOfMessages.isEmpty()
        this.chatMenuArrowStartingPositionY = this.chatArrowUpButton.y
        this.chatMenuArrowStartingPositionX = this.chatArrowUpButton.x

        userProfileViewModel.chattingUser.observe(viewLifecycleOwner) {
            this.chatFullname.text = it.fullName
            this.chatNickname.text = "@${it.nickname}"
            userProfileViewModel.retrieveProfilePicture(this.chattingUserProfilePicture, it.imgPath!!)
        }

        chatAdapterCard = MyChatAdapter(listOfMessages)

        this.sendMessageButton.setOnClickListener {
            if (this.inputMessageBox.text.isNotEmpty()) {
                chatAdapterCard.addMessage(
                    MyMessage(
                        "0", "1", this.inputMessageBox.text.toString(),
                        SimpleDateFormat(
                            "dd/MM/yyyy hh:mm",
                            Locale.getDefault()
                        ).format(Date()).toString(), "0"
                    )
                )
                this.inputMessageBox.setText("")
                val linearLayoutManager: LinearLayoutManager = LinearLayoutManager(this.context)
                linearLayoutManager.stackFromEnd = true
                this.recyclerView.layoutManager = linearLayoutManager
                this.recyclerView.adapter = chatAdapterCard
            }
        }

        this.chatArrowUpButton.setOnClickListener {
            if (!this.isAnswerMenuOpen) {
                this.recyclerView.animate().apply {
                    alpha(0.3f)
                }
                // should be opened
                this.chatArrowUpButton.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
                this.chatArrowUpButton.backgroundTintList = AppCompatResources.getColorStateList(requireContext(),
                    R.color.prussian_blue)
                this.myPurposeContainer.isGone = false
                this.myPurposeContainer.animate().apply {
                    duration = 350
                    alpha(1f)
                    translationY(-25f)
                }.start()
                this.chatArrowUpButton.animate().apply {
                    duration = 350
                    translationY(chatMenuArrowStartingPositionY - 1000f)
                    translationX(chatMenuArrowStartingPositionX - 400f)
                }.start()
                this.myPurposeContainer.
                findViewById<ImageView>(R.id.sendPurposeButtonID)
                    ?.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in_from_right))
            } else {
                this.recyclerView.animate().apply {
                    alpha(1f)
                }
                // should be closed
                this.chatArrowUpButton.setImageResource(R.drawable.ic_add_black_24dp)
                this.chatArrowUpButton.backgroundTintList = AppCompatResources.getColorStateList(requireContext(),
                    R.color.darkGray)
                this.myPurposeContainer.animate().apply {
                    duration = 150
                    alpha(0f)
                    translationY(55f)
                }.start()
                this.myPurposeContainer.isGone = true
                this.chatArrowUpButton.animate().apply {
                    duration = 350
                    translationY(chatMenuArrowStartingPositionY)
                    translationX(chatMenuArrowStartingPositionX)
                }.start()
            }
            this.isAnswerMenuOpen = !this.isAnswerMenuOpen
        }

        this.backArrow.setOnClickListener {
            findNavController().navigate(R.id.action_myChat_to_ShowListOfServices)
            activityTB.supportActionBar?.show()
        }

        val linearLayoutManager: LinearLayoutManager = LinearLayoutManager(this.context)
        linearLayoutManager.stackFromEnd = true
        this.recyclerView.layoutManager = linearLayoutManager
        this.recyclerView.adapter = chatAdapterCard

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_myChat_to_ShowListOfServices)
                activityTB.supportActionBar?.show()
            }
        })
    }

    /**
     * This permits having the right-to-left animation
     */
    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        val anim = AnimationUtils.loadAnimation(requireActivity(), R.anim.slide_in_from_right)
        anim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationRepeat(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                view?.findViewById<ConstraintLayout>(R.id.filterBackground)?.background = resources.getDrawable(R.drawable.semi_transparent_background)
            }
        })
        return anim
    }

}