package it.polito.madcourse.group06.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import it.polito.madcourse.group06.R
import it.polito.madcourse.group06.models.skill.SkillAdapterCard
import it.polito.madcourse.group06.viewmodels.AdvertisementViewModel

class ShowListOfSkills : Fragment(R.layout.fragment_service_list) {

    private val advViewModel: AdvertisementViewModel by activityViewModels()
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(
            R.layout.fragment_service_list,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        advViewModel.listOfAdvertisements.observe(this.viewLifecycleOwner) { listOfAds ->
            /**
             * If there are no services in the DB proper texts are shown.
             */
            val listOfSkills = (listOfAds.map { it.listOfSkills }.flatten() as MutableList<String>)
            view.findViewById<TextView>(R.id.defaultTextServicesList).isVisible = listOfSkills.isNullOrEmpty()

            //temporary
            listOfSkills.apply {
                add("All")
                sortWith(compareBy(String.CASE_INSENSITIVE_ORDER, {it})) }
            if (!listOfSkills.isNullOrEmpty()) {
                listOfSkills.sortWith(compareBy(String.CASE_INSENSITIVE_ORDER, { it }))

                this.recyclerView = view.findViewById(R.id.rvServicesFullList)
                this.recyclerView.layoutManager = LinearLayoutManager(this.context)
                this.recyclerView.adapter = SkillAdapterCard(listOfSkills)
            }
        }
    }
}