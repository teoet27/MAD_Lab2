package it.polito.MAD.group06.models.skill

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import it.polito.MAD.group06.R

/**
 * [SkillAdapterCard] extending the Adapter of the [RecyclerView] and implements the required methods.
 */
class SkillAdapterCard(private val skillList: List<String>) : RecyclerView.Adapter<SkillViewHolderCard>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SkillViewHolderCard {
        val vg = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.service_item, parent, false)
        return SkillViewHolderCard(vg)
    }

    /**
     * Bind operations.
     */
    override fun onBindViewHolder(holder: SkillViewHolderCard, position: Int) {
        holder.bind(skillList[position])
    }

    /**
     * Simply returns the size of the list of services provided to the adapter.
     */
    override fun getItemCount(): Int = skillList.size
}