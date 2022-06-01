package it.polito.madcourse.group06.views.timeslot

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.activity.OnBackPressedCallback
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
import it.polito.madcourse.group06.models.advertisement.AdvAdapterCard
import it.polito.madcourse.group06.models.advertisement.Advertisement
import it.polito.madcourse.group06.utilities.*
import it.polito.madcourse.group06.viewmodels.*


class ShowListOfTimeslots : Fragment(R.layout.show_timeslots_frag) {

    private val advertisementViewModel: AdvertisementViewModel by activityViewModels()
    private val userProfileViewModel: UserProfileViewModel by activityViewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private lateinit var recyclerView: RecyclerView
    private lateinit var newAdvButton: FloatingActionButton
    private lateinit var filterButton: TextView
    private lateinit var sortParam: TextView
    private lateinit var directionButton: ImageView
    private lateinit var barrier: TextView
    private lateinit var filterNotificationDot: TextView
    private lateinit var currentAccountID: String
    private lateinit var bottomNavView: BottomNavigationView
    private var fullListForGivenSkill: List<Advertisement> = listOf()
    private var isMyAdv = false
    private var isUp = false
    private var associatedActiveAdsIdList = listOf<String>()
    private var associatedSavedAdsIdList = listOf<String>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        setHasOptionsMenu(true)
        return inflater.inflate(
            R.layout.show_timeslots_frag,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.newAdvButton = view.findViewById(R.id.newAdvButton)
        this.filterButton = view.findViewById(R.id.filter_button)
        this.sortParam = view.findViewById(R.id.sort_parameter)
        this.directionButton = view.findViewById(R.id.sort_direction_button)
        this.recyclerView = view.findViewById(R.id.rvAdvFullList)
        this.filterNotificationDot = view.findViewById(R.id.filter_notification)
        this.bottomNavView = view.findViewById(R.id.bottomNavigationView)


        // set up bottom nav bar
        bottomNavView.background = null
        bottomNavView.menu.getItem(2).isEnabled = false
        bottomNavView.setOnItemSelectedListener { it ->
            when (it.title) {
                TAB_ACTIVE -> {
                    sharedViewModel.resetSearchState(currentTab = TAB_ACTIVE, activeAdsFlag = true)
                    true
                }
                TAB_SAVED -> {
                    sharedViewModel.resetSearchState(currentTab = TAB_SAVED, savedAdsFlag = true)
                    true
                }
                TAB_MINE -> {
                    sharedViewModel.resetSearchState(currentTab = TAB_MINE, myAdsFlag = true)
                    true
                }
                else -> {
                    sharedViewModel.resetSearchState(currentTab = TAB_SERVICES)
                    true
                }
            }
        }

        sharedViewModel.homePressedTwice.observe(viewLifecycleOwner){
            if(it){
                sharedViewModel.homeTabPressed(false)
                findNavController().navigate(R.id.action_ShowListTimeslots_to_showListOfServices)
            }
        }

        this.newAdvButton.setOnClickListener {
            findNavController().navigate(R.id.action_ShowListTimeslots_to_newTimeSlotDetailsFragment)
        }

        // Context menu for choosing sort parameter
        registerForContextMenu(sortParam)
        sortParam.setOnClickListener {
            activity?.openContextMenu(sortParam)
        }

        // Open filter fragment
        filterButton.setOnClickListener {
            activity?.supportFragmentManager!!.beginTransaction()
                .setCustomAnimations(R.anim.slide_in_up, 0)
                .add(R.id.nav_host_fragment_content_main, FilterTimeslots(), "filter_window")
                .commit()
        }

        // Get current user ID
        userProfileViewModel.currentUser.observe(viewLifecycleOwner) {
            this.currentAccountID = it.id!!
        }

        // Initialize Adapter card for recycler view
        var advAdapterCard: AdvAdapterCard

        // Change sort direction
        this.directionButton.setOnClickListener {
            sharedViewModel.updateSearchState(sortUpFlag = !isUp)
        }

        advertisementViewModel.activeAdsIDs.observe(viewLifecycleOwner) {
            associatedActiveAdsIdList = it!!
            sharedViewModel.updateSearchState()
        }
        advertisementViewModel.savedAdsIDs.observe(viewLifecycleOwner) {
            associatedSavedAdsIdList = it!!
            sharedViewModel.updateSearchState()
        }
        // When some change occurs in the current search, update screen
        sharedViewModel.searchState.observe(viewLifecycleOwner){  ss->
            advertisementViewModel.listOfAdvertisements.observe(viewLifecycleOwner) { listOfAdv ->

                fullListForGivenSkill =
                    listOfAdv.filter { it.listOfSkills.contains(ss.selectedSkill) || ss.selectedSkill == ALL_SERVICES }

                //compose recycler view
                view.findViewById<TextView>(R.id.defaultTextTimeslotsList).isVisible =
                    fullListForGivenSkill.isEmpty() && !ss.selectedSkill.isNullOrEmpty()
                view.findViewById<ImageView>(R.id.create_hint).isVisible =
                    fullListForGivenSkill.isEmpty() && !ss.selectedSkill.isNullOrEmpty()
                this.recyclerView.layoutManager = LinearLayoutManager(this.context)
                advAdapterCard =
                    AdvAdapterCard(listOfAdv, advertisementViewModel, requireActivity())
                when (ss.currentTab) {
                    TAB_ACTIVE -> {
                        setActionBarTitle("Active Timeslots")
                        ss.selectedSkill?.let { skill ->
                            bottomNavView.menu.getItem(0).apply{
                                title = skill
                                setIcon(R.drawable.savetime)
                            }
                        }?:bottomNavView.menu.getItem(0).setIcon(R.drawable.ic_baseline_home_24)
                        bottomNavView.menu.getItem(1).isChecked = true
                        sharedViewModel.homeTabPressed(false)
                    }
                    TAB_SAVED -> {
                        setActionBarTitle("Saved Timeslots")
                        ss.selectedSkill?.let { skill ->
                            bottomNavView.menu.getItem(0).title = skill
                            bottomNavView.menu.getItem(0).setIcon(R.drawable.savetime)
                        }?:bottomNavView.menu.getItem(0).setIcon(R.drawable.ic_baseline_home_24)
                        bottomNavView.menu.getItem(3).isChecked = true
                        sharedViewModel.homeTabPressed(false)

                    }
                    TAB_MINE -> {
                        setActionBarTitle("My Timeslots")
                        ss.selectedSkill?.let { skill ->
                            bottomNavView.menu.getItem(0).title = skill
                            bottomNavView.menu.getItem(0).setIcon(R.drawable.savetime)
                        }?:bottomNavView.menu.getItem(0).setIcon(R.drawable.ic_baseline_home_24)
                        bottomNavView.menu.getItem(4).isChecked = true
                        sharedViewModel.homeTabPressed(false)

                    }

                    else -> {
                        ss.selectedSkill?.let {
                            bottomNavView.menu.getItem(0).apply {
                                setIcon(R.drawable.ic_baseline_arrow_back_ios_24)
                                title = "Services"
                                isChecked = true
                            }
                            setActionBarTitle(it)
                            sharedViewModel.homeTabPressed()
                        }?:findNavController().navigate(R.id.action_ShowListTimeslots_to_showListOfServices)
                    }
                }

                //update views
                this.sortParam.text = paramToString(ss.sortParameter)
                this.isUp = ss.sortUpFlag ?: true
                this.directionButton.setImageResource(if (this.isUp) R.drawable.sort_up else R.drawable.sort_down)
                this.isMyAdv = ss.myAdsFlag ?: false
                if (ss.filter?.isEmpty() != false)
                    filterNotificationDot.visibility = View.GONE
                else
                    filterNotificationDot.visibility = View.VISIBLE


                //update recyclerview
                advAdapterCard.updateDataSet(
                    myAds = ss.myAdsFlag,
                    userID = currentAccountID,
                    savedAdsFlag = ss.savedAdsFlag,
                    activeAdsFlag = ss.activeAdsFlag,
                    activeAdsIDs = associatedActiveAdsIdList,
                    savedAdsIDs = associatedSavedAdsIdList,
                    selectedSkill = if (ss.myAdsFlag != true && ss.activeAdsFlag != true && ss.savedAdsFlag != true) ss.selectedSkill else ALL_SERVICES,
                    advFilter = ss.filter,
                    sortUp = ss.sortUpFlag,
                    sortParam = ss.sortParameter,
                    search = ss.searchedWord
                )
                // Adapter setting
                this.recyclerView.adapter = advAdapterCard
            }
        }

        // On back pressed
        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigate(R.id.action_ShowListTimeslots_to_showListOfServices)
                }
            })
    }

    private fun setActionBarTitle(title: String) {
        (activity as TBMainActivity).supportActionBar?.title = title
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        activity?.menuInflater?.inflate(R.menu.search_menu, menu)

        val menuItem = menu.findItem(R.id.action_search)
        val searchView = menuItem.actionView as SearchView
        searchView.queryHint = resources.getString(R.string.search_hint)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                sharedViewModel.updateSearchState(searchedWord = newText)
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

        })

        return super.onCreateOptionsMenu(menu, inflater)

    }

    /**
     * This method chooses a menu to be inflated for choosing the sorting parameter
     *
     * @param menu  ContextMenu
     * @param v  View
     * @param menuInfo  ContextMenu info
     * @return true
     */
    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater: MenuInflater = requireActivity().menuInflater
        inflater.inflate(R.menu.sort_parameter_context_menu, menu)
    }

    /**
     * This method reacts to menu choice
     *
     * @param item  Chosen menu entry
     * @return true
     */
    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.title) {
            resources.getString(R.string.title) -> sharedViewModel.updateSearchState(sortParameter = 0)
            resources.getString(R.string.duration_menu) -> sharedViewModel.updateSearchState(
                sortParameter = 1
            )
            resources.getString(R.string.starting_time_menu) -> sharedViewModel.updateSearchState(
                sortParameter = 2
            )
            resources.getString(R.string.ending_time_menu) -> sharedViewModel.updateSearchState(
                sortParameter = 3
            )
            resources.getString(R.string.date) -> sharedViewModel.updateSearchState(sortParameter = 4)
        }
        return true
    }

    private fun paramToString(idx: Int?): String {
        return when (idx) {
            1 -> "Duration"
            2 -> "Starting time"
            3 -> "Ending time"
            4 -> "Date"
            else -> "Title"
        }
    }
}

