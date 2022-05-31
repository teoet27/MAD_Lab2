package it.polito.madcourse.group06.models.mychat

import android.animation.ValueAnimator
import android.os.Handler
import android.view.Gravity
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import it.polito.madcourse.group06.R
import it.polito.madcourse.group06.viewmodels.MyMessage


class MyChatViewHolder(private val v: View, private val isMyMessage: Boolean) :
    RecyclerView.ViewHolder(v) {
    private lateinit var msgContent: TextView
    private lateinit var msgTimestamp: TextView
    private var startingHeight: Int = 0
    private var isMsgTimestampShown = false

    fun bind(msg: MyMessage, itemViewType: Int) {
        if (isMyMessage) {
            if (itemViewType == R.layout.my_message_layout) {
                this.v.findViewById<LinearLayout>(R.id.myMainContainerID).gravity = Gravity.START
                this.msgTimestamp = this.v.findViewById(R.id.myTimestampID)
                this.msgTimestamp.alpha = 0f
                this.msgContent = this.v.findViewById(R.id.myMessageTextViewID)
                this.msgTimestamp.text = msg.timestamp
                this.msgTimestamp.translationY = 45f
                this.msgContent.text = msg.msg
            } else if (itemViewType == R.layout.common_my_message_layout) {
                this.v.findViewById<LinearLayout>(R.id.myCommonMainContainerID).gravity = Gravity.START
                this.msgTimestamp = this.v.findViewById(R.id.myCommonTimestampID)
                this.msgTimestamp.alpha = 0f
                this.msgContent = this.v.findViewById(R.id.myCommonMessageTextViewID)
                this.msgTimestamp.text = msg.timestamp
                this.msgTimestamp.translationY = 45f
                this.msgContent.text = msg.msg
            }
        } else {
            if (itemViewType == R.layout.other_message_layout) {
                this.v.findViewById<LinearLayout>(R.id.otherMainContainerID).gravity = Gravity.END
                this.msgTimestamp = this.v.findViewById(R.id.otherTimestampID)
                this.msgTimestamp.alpha = 0f
                this.msgContent = this.v.findViewById(R.id.otherMessageTextViewID)
                this.msgTimestamp.text = msg.timestamp
                this.msgTimestamp.translationY = 45f
                this.msgContent.text = msg.msg
            } else if (itemViewType == R.layout.common_other_message_layout) {
                this.v.findViewById<LinearLayout>(R.id.otherCommonMainContainerID).gravity = Gravity.END
                this.msgTimestamp = this.v.findViewById(R.id.otherCommonTimestampID)
                this.msgTimestamp.alpha = 0f
                this.msgContent = this.v.findViewById(R.id.otherCommonMessageTextViewID)
                this.msgTimestamp.text = msg.timestamp
                this.msgTimestamp.translationY = 45f
                this.msgContent.text = msg.msg
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