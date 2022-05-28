package it.polito.madcourse.group06.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import it.polito.madcourse.group06.R
import it.polito.madcourse.group06.models.skill.SkillAdapterCard
import it.polito.madcourse.group06.viewmodels.AdvertisementViewModel
import it.polito.madcourse.group06.viewmodels.SharedViewModel

class ShowListOfSkills : Fragment(R.layout.service_list) {

    private val advViewModel: AdvertisementViewModel by activityViewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var sortButton: Button
    private lateinit var bottomNavView: BottomNavigationView
    private lateinit var newAdvButton: FloatingActionButton

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
        sortButton = view.findViewById(R.id.skill_list_sort_button)

        sharedViewModel.resetSearchState()
        advViewModel.listOfAdvertisements.observe(this.viewLifecycleOwner) { listOfAds ->
            var listOfSkills =
                listOfAds
                    .asSequence()
                    .map { it.listOfSkills }
                    .flatten()
                    .sortedBy { it.lowercase() }
                    .toSet()
                    .toMutableList()

            view.findViewById<TextView>(R.id.defaultTextServicesList).isVisible =
                listOfSkills.isNullOrEmpty()

            this.recyclerView = view.findViewById(R.id.rvServicesFullList)
            this.recyclerView.layoutManager = LinearLayoutManager(this.context)

            sortButton.setOnClickListener {
                listOfSkills = listOfSkills.asReversed()
                val finalList = listOfSkills.toMutableList()
                finalList.add(0, "All")
                this.recyclerView.adapter = SkillAdapterCard(finalList)
            }
            val finalList = listOfSkills.toMutableList()
            finalList.add(0, "All")
            this.recyclerView.adapter = SkillAdapterCard(finalList)
        }

        this.bottomNavView = view.findViewById(R.id.bottomNavigationViewSkillList)
        this.newAdvButton = view.findViewById(R.id.newAdvButton)

        // set up bottom nav bar
        bottomNavView.background = null
        bottomNavView.menu.getItem(2).isEnabled = false
        bottomNavView.setOnItemSelectedListener {
            when (it.title) {
                "Active" -> {findNavController().navigate(R.id.ShowListTimeslots, bundleOf("tab" to "Active"));true}
                "Saved" -> {findNavController().navigate(R.id.ShowListTimeslots, bundleOf("tab" to "Saved"));true}
                "Mine" -> {findNavController().navigate(R.id.ShowListTimeslots, bundleOf("tab" to "Mine"));true}
                else ->true
            }
        }
        this.newAdvButton.setOnClickListener {
            findNavController().navigate(R.id.action_ShowListOfServices_to_newTimeSlotDetailsFragment)
        }

    }
}