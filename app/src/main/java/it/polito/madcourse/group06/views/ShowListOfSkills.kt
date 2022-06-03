package it.polito.madcourse.group06.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import it.polito.madcourse.group06.R
import it.polito.madcourse.group06.activities.TBMainActivity
import it.polito.madcourse.group06.models.skill.SkillAdapterCard
import it.polito.madcourse.group06.utilities.*
import it.polito.madcourse.group06.viewmodels.AdvertisementViewModel
import it.polito.madcourse.group06.viewmodels.SharedViewModel
import it.polito.madcourse.group06.viewmodels.UserProfileViewModel

class ShowListOfSkills : Fragment(R.layout.service_list) {

    private val advViewModel: AdvertisementViewModel by activityViewModels()
    private val userViewModel: UserProfileViewModel by activityViewModels()
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
        this.bottomNavView = view.findViewById(R.id.bottomNavigationViewSkillList)
        this.newAdvButton = view.findViewById(R.id.newAdvButton)

        // set up bottom nav bar
        bottomNavView.background = null
        bottomNavView.menu.getItem(2).isEnabled = false
        bottomNavView.setOnItemSelectedListener {
            when (it.title) {
                TAB_ACTIVE -> {
                    findNavController().navigate(R.id.ShowListTimeslots)
                    sharedViewModel.resetSearchState(currentTab = TAB_ACTIVE, activeAdsFlag = true)
                    true
                }
                TAB_SAVED -> {
                    findNavController().navigate(R.id.ShowListTimeslots)
                    sharedViewModel.resetSearchState(currentTab = TAB_SAVED, savedAdsFlag = true)
                    true
                }
                TAB_MINE -> {
                    findNavController().navigate(R.id.ShowListTimeslots)
                    sharedViewModel.resetSearchState(currentTab = TAB_MINE, myAdsFlag = true)
                    true
                }
                else -> {
                    true
                }
            }
        }
        this.newAdvButton.setOnClickListener {
            findNavController().navigate(R.id.action_ShowListOfServices_to_newTimeSlotDetailsFragment)
        }

        (activity as TBMainActivity).supportActionBar?.title = "Offered Services"
        sharedViewModel.resetSearchState()
        userViewModel.currentUser.observe(viewLifecycleOwner) { user ->
            advViewModel.listOfAdvertisements.observe(this.viewLifecycleOwner) { listOfAdv ->
                var listOfSkills =
                    listOfAdv
                        .asSequence()
                        .filter { it.isAvailable }
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
                    finalList.add(0, ALL_SERVICES)
                    this.recyclerView.adapter = SkillAdapterCard(finalList, sharedViewModel)
                }
                val finalList = listOfSkills.toMutableList()
                finalList.add(0, ALL_SERVICES)
                this.recyclerView.adapter = SkillAdapterCard(finalList, sharedViewModel)

                // Create notification badges for expired ads among my and active timeslots
                listOfAdv.count { adv ->
                    // current user owns the ad
                    (user.id == adv.accountID && adv.isToBeRated() && !adv.rxUserId.isNullOrEmpty()) ||
                            // current user is the client of the ad
                            (user.id == adv.ratingUserId && adv.isToBeRated())
                }
                    .also { n ->
                        when (n) {
                            0 -> bottomNavView.removeBadge(R.id.active_time_slots_tab)
                            else -> bottomNavView.getOrCreateBadge(R.id.active_time_slots_tab)
                                .number = n
                        }

                    }
                listOfAdv.count {
                    it.accountID == user.id &&
                            it.isExpired()
                }
                    .also { n ->
                        when (n) {
                            0 -> bottomNavView.removeBadge(R.id.my_time_slots_tab)
                            else -> bottomNavView.getOrCreateBadge(R.id.my_time_slots_tab)
                                .number = n
                        }
                    }
            }
        }
    }
}