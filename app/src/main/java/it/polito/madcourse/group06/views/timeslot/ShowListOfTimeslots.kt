package it.polito.madcourse.group06.views.timeslot

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.content.res.AppCompatResources
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
import it.polito.madcourse.group06.viewmodels.AdvertisementViewModel
import it.polito.madcourse.group06.viewmodels.SharedViewModel
import it.polito.madcourse.group06.viewmodels.UserProfileViewModel


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
    private lateinit var myTimeslotsButton: TextView
    private lateinit var currentAccountID: String
    private lateinit var bottomNavView: BottomNavigationView
    private var selectedSkill: String = "All"
    private var fullListForGivenSkill: List<Advertisement> = listOf()
    private var isMyAdv = false
    private var isUp = false

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
        this.barrier = view.findViewById(R.id.barrier)
        this.filterNotificationDot = view.findViewById(R.id.filter_notification)
        this.myTimeslotsButton = view.findViewById(R.id.myTimeslotsButtonID)
        this.bottomNavView=view.findViewById(R.id.bottomNavigationView)

        // set up bottom nav bar
        bottomNavView.background = null
        bottomNavView.menu.getItem(2).isEnabled = false
        bottomNavView.setOnItemSelectedListener {
            when(it.title){
                "Active"->{sharedViewModel.resetSearchState(activeAdsFlag = true);setActionBarTitle("Active Timeslots");true}
                "Saved"->{sharedViewModel.resetSearchState(savedAdsFlag = true);setActionBarTitle("Saved Timeslots");true}
                "Mine"->{sharedViewModel.resetSearchState(myAdsFlag = true);setActionBarTitle("My Timeslots");true}
                else ->{if(bottomNavView.menu.getItem(0).isChecked){
                    findNavController().navigate(R.id.action_ShowListTimeslots_to_showListOfServices)
                }
                    else {
                        sharedViewModel.updateSearchState(myAdsFlag = false,activeAdsFlag = false,savedAdsFlag = false)
                        setActionBarTitle(selectedSkill)
                    }
                    true
                }
            }
        }
        arguments?.getString("tab")?.let{
            when(it) {
                "Active" ->{bottomNavView.menu.performIdentifierAction(R.id.active_time_slots_tab,0);true}
                "Saved" ->{bottomNavView.menu.performIdentifierAction(R.id.saved_time_slots_tab,0);true}
                "Mine"->{bottomNavView.menu.performIdentifierAction(R.id.my_time_slots_tab,0);true}
                else -> true
            }
        }

        // Get current user
        userProfileViewModel.currentUser.observe(viewLifecycleOwner) {
            this.currentAccountID = it.id!!
        }

        this.newAdvButton.setOnClickListener {
            findNavController().navigate(R.id.action_ShowListTimeslots_to_newTimeSlotDetailsFragment)
        }

        // Get and set current selected skill
        arguments?.getString("selected_skill")?.let { selectedSkill = it; setActionBarTitle(it)}

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
        // If filter fragment is open, disable RV UI
        sharedViewModel.selected.observe(viewLifecycleOwner) {
            enableUI(!it)
        }

        // Initialize Adapter card for recycler view
        var advAdapterCard: AdvAdapterCard

        // Modify adapter card when events occur:
        // - MyTimeslots
        this.myTimeslotsButton.setOnClickListener {
            sharedViewModel.updateSearchState(myAdsFlag = !isMyAdv)
        }
        // - Change sort direction
        this.directionButton.setOnClickListener {
            sharedViewModel.updateSearchState(sortUpFlag = !isUp)
        }

        advertisementViewModel.listOfAdvertisements.observe(viewLifecycleOwner) { listOfAdv ->
            fullListForGivenSkill =
                listOfAdv.filter { it.listOfSkills.contains(selectedSkill) || selectedSkill == "All" }

            // Close filter window in case it was left open
            activity?.supportFragmentManager?.findFragmentByTag("filter_window")?.also { frag ->
                activity?.supportFragmentManager?.beginTransaction()?.remove(frag)?.commit()
                sharedViewModel.select(false)
            }

            //compose recycler view
            view.findViewById<TextView>(R.id.defaultTextTimeslotsList).isVisible =
                fullListForGivenSkill.isEmpty()
            view.findViewById<ImageView>(R.id.create_hint).isVisible =
                fullListForGivenSkill.isEmpty()
            this.recyclerView.layoutManager = LinearLayoutManager(this.context)
            advAdapterCard = AdvAdapterCard(listOfAdv, advertisementViewModel)

            // - Filter
            sharedViewModel.searchState.observe(viewLifecycleOwner) {

                //update view
                this.sortParam.text = paramToString(it.sortParameter)
                this.isUp=it.sortUpFlag?:true
                this.directionButton.setImageResource(if(this.isUp) R.drawable.sort_up else R.drawable.sort_down)
                this.isMyAdv=it.myAdsFlag?:false
                if (isMyAdv) {
                    this.myTimeslotsButton.backgroundTintList =
                        AppCompatResources.getColorStateList(requireContext(), R.color.orange_poli)
                    this.myTimeslotsButton.setTextColor(
                        AppCompatResources.getColorStateList(
                            requireContext(),
                            R.color.black
                        )
                    )
                } else {
                    this.myTimeslotsButton.backgroundTintList =
                        AppCompatResources.getColorStateList(requireContext(), R.color.darkGray)
                    this.myTimeslotsButton.setTextColor(
                        AppCompatResources.getColorStateList(
                            requireContext(),
                            R.color.lightGray
                        )
                    )
                }

                if(it.filter?.isEmpty() != false)
                    filterNotificationDot.visibility=View.GONE
                else
                    filterNotificationDot.visibility=View.VISIBLE


                //update recyclerview
                advAdapterCard.updateDataSet(
                    selectedSkill=selectedSkill,
                    advFilter = it.filter,
                    sortUp = it.sortUpFlag,
                    sortParam = it.sortParameter,
                    myAds = it.myAdsFlag,
                    activeAdsFlag=it.activeAdsFlag,
                    savedAdsFlag=it.savedAdsFlag,
                    userID = currentAccountID,
                    search = it.searchedWord
                )
            }

            // Adapter setting
            this.recyclerView.adapter = advAdapterCard
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

    private fun setActionBarTitle(title:String){
        (activity as TBMainActivity).supportActionBar?.title = title
    }

    private fun enableUI(switch: Boolean) {
        when (switch) {
            true -> barrier.visibility = View.GONE
            false -> {
                barrier.visibility = View.VISIBLE
                barrier.bringToFront()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        activity?.menuInflater?.inflate(R.menu.search_menu,menu)

        val menuItem=menu.findItem(R.id.action_search)
        val searchView=menuItem.actionView as SearchView
        searchView.queryHint=resources.getString(R.string.search_hint)

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
            resources.getString(R.string.duration_menu) -> sharedViewModel.updateSearchState(sortParameter = 1)
            resources.getString(R.string.starting_time_menu) -> sharedViewModel.updateSearchState(sortParameter = 2)
            resources.getString(R.string.ending_time_menu) -> sharedViewModel.updateSearchState(sortParameter = 3)
            resources.getString(R.string.date) -> sharedViewModel.updateSearchState(sortParameter = 4)
        }
        return true
    }

    private fun paramToString(idx:Int?):String{
        return when(idx){
            1->"Duration"
            2->"Starting time"
            3->"Ending time"
            4->"Date"
            else->"Title"
        }
    }
}

