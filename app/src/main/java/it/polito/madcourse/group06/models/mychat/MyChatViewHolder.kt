package it.polito.madcourse.group06.models.mychat

import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import it.polito.madcourse.group06.R
import it.polito.madcourse.group06.viewmodels.MyMessage

class MyChatViewHolder(private val v: View, private val isMyMessage: Boolean) :
    RecyclerView.ViewHolder(v) {
    private lateinit var msgContent: TextView
    lateinit var msgTimestamp: TextView

    fun bind(msg: MyMessage, itemViewType: Int) {
        if (isMyMessage) {
            if (itemViewType == R.layout.my_message_layout) {
                v.findViewById<LinearLayout>(R.id.myMainContainerID).gravity = Gravity.START
                msgTimestamp = v.findViewById(R.id.myTimestampID)
                msgContent = v.findViewById(R.id.myMessageTextViewID)
                msgTimestamp.text = msg.timestamp
                msgContent.text = msg.msg
            } else if (itemViewType == R.layout.common_my_message_layout) {
                v.findViewById<LinearLayout>(R.id.myCommonMainContainerID).gravity = Gravity.START
                msgTimestamp = v.findViewById(R.id.commonMyTimestampID)
                msgContent = v.findViewById(R.id.myCommonMessageTextViewID)
                msgTimestamp.text = msg.timestamp
                msgContent.text = msg.msg
            }
        } else {
            if (itemViewType == R.layout.other_message_layout) {
                v.findViewById<LinearLayout>(R.id.otherMainContainerID).gravity = Gravity.END
                msgTimestamp = v.findViewById(R.id.otherTimestampID)
                msgContent = v.findViewById(R.id.otherMessageTextViewID)
                msgTimestamp.text = msg.timestamp
                msgContent.text = msg.msg
            } else if (itemViewType == R.layout.common_other_message_layout) {
                v.findViewById<LinearLayout>(R.id.otherCommonMainContainerID).gravity = Gravity.END
                msgTimestamp = v.findViewById(R.id.commonOtherTimestampID)
                msgContent = v.findViewById(R.id.otherCommonMessageTextViewID)
                msgTimestamp.text = msg.timestamp
                msgContent.text = msg.msg
            }
        }
    }
}