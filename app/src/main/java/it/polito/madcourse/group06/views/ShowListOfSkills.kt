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

class ShowListOfSkills : Fragment(R.layout.service_list) {

    private val advViewModel: AdvertisementViewModel by activityViewModels()
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(
            R.layout.service_list,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        advViewModel.listOfAdvertisements.observe(this.viewLifecycleOwner) { listOfAds ->
            val listOfSkills = listOfAds.map { it.listOfSkills }
            val setOfSkills = mutableSetOf<String>()
            for (skills in listOfSkills) {
                for (s in skills) {
                    setOfSkills.add(s)
                }
            }
            view.findViewById<TextView>(R.id.defaultTextServicesList).isVisible = listOfSkills.isNullOrEmpty()

            setOfSkills.apply {
                add("All")
                sortedWith { a, b -> a.compareTo(b, ignoreCase = true) }
            }
            if (!listOfSkills.isNullOrEmpty()) {
                // setOfSkills.sortedWith { a, b -> a.compareTo(b, ignoreCase = true) }
                this.recyclerView = view.findViewById(R.id.rvServicesFullList)
                this.recyclerView.layoutManager = LinearLayoutManager(this.context)
                this.recyclerView.adapter = SkillAdapterCard(setOfSkills.toList())
            }
        }
    }
}