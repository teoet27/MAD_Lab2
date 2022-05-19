package it.polito.MAD.group06.models.skill

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import it.polito.MAD.group06.R


/**
 * [SkillViewHolderCard] extends the ViewHolder of the [RecyclerView]
 * and provides the references to each component of the service
 * card.
 */
class SkillViewHolderCard(v: View) : RecyclerView.ViewHolder(v) {
    val name: TextView = v.findViewById(R.id.serviceCardTitle)

    /**
     * bind:
     * A method to bind the i-th entry of the serviceList to the i-th holder properties.
     * @param service an object of class Service
     */
    fun bind(skillName: String) {
        this.name.text = skillName
    }
}