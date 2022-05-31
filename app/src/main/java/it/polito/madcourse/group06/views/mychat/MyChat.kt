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
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import it.polito.madcourse.group06.R
import it.polito.madcourse.group06.activities.TBMainActivity
import it.polito.madcourse.group06.models.mychat.MyChatAdapter
import it.polito.madcourse.group06.viewmodels.MyMessage
import java.text.SimpleDateFormat
import java.util.*

class MyChat : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var chatAdapterCard: MyChatAdapter
    private lateinit var activityTB: TBMainActivity
    private lateinit var chatFullname: TextView
    private lateinit var chatNickname: TextView
    private lateinit var chattingUserProfilePicture: ImageView
    private lateinit var emptyChatMessage: TextView
    private lateinit var inputMessageBox: EditText
    private lateinit var sendMessageButton: ImageView

    private val listOfMessages = mutableListOf<MyMessage>(
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

        this.emptyChatMessage.isVisible = this.listOfMessages.isEmpty()

        this.chatFullname.text = "Bill Gates"
        this.chatNickname.text = "@contocancelli"

        chatAdapterCard = MyChatAdapter(listOfMessages)

        this.sendMessageButton.setOnClickListener {
            chatAdapterCard.addMessage(
                MyMessage(
                    "0", "1", this.inputMessageBox.text.toString(),
                    SimpleDateFormat("dd/MM/yyyy hh:mm",
                        Locale.getDefault()).format(Date()).toString(), "0"
                )
            )
            this.inputMessageBox.setText("")
        }

        this.recyclerView.layoutManager = LinearLayoutManager(this.context)
        this.recyclerView.adapter = chatAdapterCard

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_myChat_to_ShowListOfServices)
                activityTB.supportActionBar?.show()
            }
        })
    }

    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        val anim = AnimationUtils.loadAnimation(requireActivity(), R.anim.slide_in_from_right)
        anim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
                // additional functionality
            }

            override fun onAnimationRepeat(animation: Animation) {
                // additional functionality
            }

            override fun onAnimationEnd(animation: Animation) {
                // additional functionality
                view?.findViewById<ConstraintLayout>(R.id.filterBackground)?.background = resources.getDrawable(R.drawable.semi_transparent_background)
            }
        })
        return anim
    }

}