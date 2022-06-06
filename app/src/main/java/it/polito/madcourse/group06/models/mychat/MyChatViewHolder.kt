package it.polito.madcourse.group06.models.mychat

import android.animation.ValueAnimator
import android.os.Handler
import android.view.Gravity
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView
import it.polito.madcourse.group06.R


class MyChatViewHolder(
    private val v: View,
    private val isMyMessage: Boolean
) : RecyclerView.ViewHolder(v) {
    private lateinit var msgContent: TextView
    private lateinit var msgTimestamp: TextView
    private lateinit var msgLocation: TextView
    private lateinit var msgStartingTime: TextView
    private lateinit var msgDuration: TextView
    private var msgState: Long = 0
    private var rejectButton: TextView? = this.v.findViewById(R.id.chatRejectID)
    private var acceptButton: TextView? = this.v.findViewById(R.id.chatAcceptID)
    private var pendingState: TextView? = this.v.findViewById(R.id.otherProposalResultPending)
    private var acceptedState: TextView? = this.v.findViewById(R.id.otherProposalResultAccepted)
    private var rejectedState: TextView? = this.v.findViewById(R.id.otherProposalResultRejected)
    private var notAvailableState: TextView? = this.v.findViewById(R.id.otherProposalResultNoMoreAvailable)

    private var startingHeight: Int = 0
    private var isMsgTimestampShown = false

    fun bind(msg: MyMessage, itemViewType: Int, acceptCallback: ((Double, Int, Long) -> Unit), rejectCallback: ((Int, Long) -> Unit), propState: Long) {
        this.msgState = propState
        if (isMyMessage) {
            when (itemViewType) {
                R.layout.my_message_layout -> {
                    this.v.findViewById<LinearLayout>(R.id.myMainContainerID).gravity = Gravity.START
                    this.msgTimestamp = this.v.findViewById(R.id.myTimestampID)
                    this.msgTimestamp.alpha = 0f
                    this.msgContent = this.v.findViewById(R.id.myMessageTextViewID)
                    this.msgTimestamp.text = msg.timestamp
                    this.msgTimestamp.translationY = 45f
                    this.msgContent.text = msg.msg
                }
                R.layout.common_my_message_layout -> {
                    this.v.findViewById<LinearLayout>(R.id.myCommonMainContainerID).gravity = Gravity.START
                    this.msgTimestamp = this.v.findViewById(R.id.myCommonTimestampID)
                    this.msgTimestamp.alpha = 0f
                    this.msgContent = this.v.findViewById(R.id.myCommonMessageTextViewID)
                    this.msgTimestamp.text = msg.timestamp
                    this.msgTimestamp.translationY = 45f
                    this.msgContent.text = msg.msg
                }
                R.layout.my_proposal_item -> {
                    this.v.findViewById<LinearLayout>(R.id.myMainContainerProposalID).gravity = Gravity.START
                    this.msgTimestamp = this.v.findViewById(R.id.myProposalTimestampID)
                    this.msgTimestamp.alpha = 0f
                    when(this.msgState) {
                        (-1).toLong() -> { // REJECTED
                            this.pendingState?.isGone = true
                            this.acceptedState?.isGone = true
                            this.rejectedState?.isGone = false
                            this.notAvailableState?.isGone = true
                            this.acceptButton?.isGone = true
                            this.rejectButton?.isGone = true
                        }
                        (0).toLong() -> { // PENDING
                            this.pendingState?.isGone = false
                            this.acceptedState?.isGone = true
                            this.rejectedState?.isGone = true
                            this.notAvailableState?.isGone = true
                            this.acceptButton?.isGone = false
                            this.rejectButton?.isGone = false
                        }
                        (1).toLong() -> { // ACCEPTED
                            this.pendingState?.isGone = true
                            this.acceptedState?.isGone = false
                            this.rejectedState?.isGone = true
                            this.notAvailableState?.isGone = true
                            this.acceptButton?.isGone = true
                            this.rejectButton?.isGone = true
                        }
                        (2).toLong() -> { // NOT AVAILABLE
                            this.pendingState?.isGone = true
                            this.acceptedState?.isGone = true
                            this.rejectedState?.isGone = true
                            this.notAvailableState?.isGone = false
                            this.acceptButton?.isGone = true
                            this.rejectButton?.isGone = true
                        }
                    }

                    this.msgLocation = this.v.findViewById(R.id.myLocationTVID)
                    this.msgLocation.text = msg.location
                    this.msgStartingTime = this.v.findViewById(R.id.myStartingTimeTVID)
                    this.msgStartingTime.text = msg.startingTime
                    this.msgDuration = this.v.findViewById(R.id.myDurationTVID)
                    this.msgDuration.text = msg.duration.toString()

                    this.msgTimestamp.text = msg.timestamp
                    this.msgTimestamp.translationY = 45f
                }
            }
        } else {
            when (itemViewType) {
                R.layout.other_message_layout -> {
                    this.v.findViewById<LinearLayout>(R.id.otherMainContainerID).gravity = Gravity.END
                    this.msgTimestamp = this.v.findViewById(R.id.otherTimestampID)
                    this.msgTimestamp.alpha = 0f
                    this.msgContent = this.v.findViewById(R.id.otherMessageTextViewID)
                    this.msgTimestamp.text = msg.timestamp
                    this.msgTimestamp.translationY = 45f
                    this.msgContent.text = msg.msg
                }
                R.layout.common_other_message_layout -> {
                    this.v.findViewById<LinearLayout>(R.id.otherCommonMainContainerID).gravity = Gravity.END
                    this.msgTimestamp = this.v.findViewById(R.id.otherCommonTimestampID)
                    this.msgTimestamp.alpha = 0f
                    this.msgContent = this.v.findViewById(R.id.otherCommonMessageTextViewID)
                    this.msgTimestamp.text = msg.timestamp
                    this.msgTimestamp.translationY = 45f
                    this.msgContent.text = msg.msg
                }
                R.layout.other_proposal_item -> {
                    this.v.findViewById<LinearLayout>(R.id.otherMainContainerProposalID).gravity = Gravity.START
                    this.msgTimestamp = this.v.findViewById(R.id.otherProposalTimestampID)
                    this.msgTimestamp.alpha = 0f

                    when(this.msgState) {
                        (-1).toLong() -> { // REJECTED
                            this.pendingState?.isGone = true
                            this.acceptedState?.isGone = true
                            this.rejectedState?.isGone = false
                            this.notAvailableState?.isGone = true
                            this.acceptButton?.isGone = true
                            this.rejectButton?.isGone = true
                        }
                        (0).toLong() -> { // PENDING
                            this.pendingState?.isGone = false
                            this.acceptedState?.isGone = true
                            this.rejectedState?.isGone = true
                            this.notAvailableState?.isGone = true
                            this.acceptButton?.isGone = false
                            this.rejectButton?.isGone = false
                        }
                        (1).toLong() -> { // ACCEPTED
                            this.pendingState?.isGone = true
                            this.acceptedState?.isGone = false
                            this.rejectedState?.isGone = true
                            this.notAvailableState?.isGone = true
                            this.acceptButton?.isGone = true
                            this.rejectButton?.isGone = true
                        }
                        (2).toLong() -> { // NOT AVAILABLE
                            this.pendingState?.isGone = true
                            this.acceptedState?.isGone = true
                            this.rejectedState?.isGone = true
                            this.notAvailableState?.isGone = false
                            this.acceptButton?.isGone = true
                            this.rejectButton?.isGone = true
                        }
                    }

                    this.msgLocation = this.v.findViewById(R.id.otherLocationTVID)
                    this.msgLocation.text = msg.location
                    this.msgStartingTime = this.v.findViewById(R.id.otherStartingTimeTVID)
                    this.msgStartingTime.text = msg.startingTime
                    this.msgDuration = this.v.findViewById(R.id.otherDurationTVID)
                    this.msgDuration.text = msg.duration.toString()

                    this.msgTimestamp.text = msg.timestamp
                    this.msgTimestamp.translationY = 45f

                    /**
                     * Accept and reject buttons setting
                     */
                    this.acceptButton?.setOnClickListener {
                        acceptCallback(this.msgDuration.text.toString().split(":")[0].toDouble()
                                + this.msgDuration.text.toString().split(":")[0].toDouble() / 60.0, this.position, 1)
                        this.pendingState?.isGone = true
                        this.acceptedState?.isGone = false
                        this.rejectedState?.isGone = true
                        this.notAvailableState?.isGone = true
                        this.acceptButton?.isGone = true
                        this.rejectButton?.isGone = true
                    }
                    this.rejectButton?.setOnClickListener {
                        rejectCallback(this.position, -1)
                        this.pendingState?.isGone = true
                        this.acceptedState?.isGone = true
                        this.rejectedState?.isGone = false
                        this.notAvailableState?.isGone = true
                        this.acceptButton?.isGone = true
                        this.rejectButton?.isGone = true
                    }
                }
            }
        }
    }

    fun setTimestampVisibility() {
        if (!this.isMsgTimestampShown) {
            this.startingHeight = this.v.height
            this.isMsgTimestampShown = true
            expandMessageView(this.v, 200, startingHeight + 40)
            this.msgTimestamp.alpha = 0f
            this.msgTimestamp.animate().apply {
                duration = 700
                alpha(1f)
            }.start()
            Handler().postDelayed({
                this.isMsgTimestampShown = false
                collapseMessageView(this.v, 200, startingHeight)
                this.msgTimestamp.alpha = 0f
            }, 2000)
        } else {
            this.isMsgTimestampShown = false
            collapseMessageView(this.v, 200, startingHeight)
        }
    }

    private fun expandMessageView(v: View, duration: Int, targetHeight: Int) {
        val prevHeight = v.height
        v.visibility = View.VISIBLE
        val valueAnimator = ValueAnimator.ofInt(prevHeight, targetHeight)
        valueAnimator.addUpdateListener { animation ->
            v.layoutParams.height = animation.animatedValue as Int
            v.requestLayout()
        }
        valueAnimator.interpolator = DecelerateInterpolator()
        valueAnimator.duration = duration.toLong()
        valueAnimator.start()
    }


    private fun collapseMessageView(v: View, duration: Int, targetHeight: Int) {
        val prevHeight = v.height
        val valueAnimator = ValueAnimator.ofInt(prevHeight, targetHeight)
        valueAnimator.interpolator = DecelerateInterpolator()
        valueAnimator.addUpdateListener { animation ->
            v.layoutParams.height = animation.animatedValue as Int
            v.requestLayout()
        }
        valueAnimator.interpolator = DecelerateInterpolator()
        valueAnimator.duration = duration.toLong()
        valueAnimator.start()
    }
}