package it.polito.madcourse.group06.views.timeslot

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import it.polito.madcourse.group06.R
import it.polito.madcourse.group06.models.advertisement.AdvAdapterCard
import it.polito.madcourse.group06.models.advertisement.Advertisement
import it.polito.madcourse.group06.utilities.TimeslotTools
import it.polito.madcourse.group06.viewmodels.AdvertisementViewModel
import it.polito.madcourse.group06.viewmodels.SharedViewModel
import it.polito.madcourse.group06.viewmodels.UserProfileViewModel


class ShowListOfTimeslots : Fragment(R.layout.show_timeslots_frag) {

    private val advertisementViewModel: AdvertisementViewModel by activityViewModels()
    private val userProfileViewModel: UserProfileViewModel by activityViewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var newAdvButton: Button
    private lateinit var filterButton: TextView
    private lateinit var sortParam: TextView
    private lateinit var directionButton: ImageView
    private lateinit var barrier: TextView
    private lateinit var searchBar: EditText
    private lateinit var myTimeslotsButton: TextView
    private lateinit var currentAccountID: String
    private val advFilter: TimeslotTools.AdvFilter = TimeslotTools.AdvFilter()
    private var fullListForGivenSkill: List<Advertisement> = listOf()
    private var param: Int = 0
    private var isMyAdv = false
    private var isUp = false
    private var search: CharSequence? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

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
        this.searchBar = view.findViewById(R.id.search_bar)
        this.myTimeslotsButton = view.findViewById(R.id.myTimeslotsButtonID)

        userProfileViewModel.currentUser.observe(viewLifecycleOwner) {
            this.currentAccountID = it.id!!
        }

        registerForContextMenu(sortParam)

        this.newAdvButton.setOnClickListener {
            findNavController().navigate(R.id.action_ShowListTimeslots_to_newTimeSlotDetailsFragment)
        }

        activity?.supportFragmentManager?.findFragmentByTag("filter_window")?.also { frag ->
            activity?.supportFragmentManager?.beginTransaction()?.remove(frag)?.commit()
            sharedViewModel.select(false)
        }

        sharedViewModel.selected.observe(viewLifecycleOwner) {
            enableUI(!it)
        }

        filterButton.setOnClickListener {
            activity?.supportFragmentManager!!.beginTransaction()
                .setCustomAnimations(R.anim.slide_in_up, 0)
                .add(R.id.nav_host_fragment_content_main, FilterTimeslots(), "filter_window").commit()
        }

        sortParam.setOnClickListener {
            activity?.openContextMenu(sortParam)
        }

        searchBar.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                search = searchBar.text
            }
        })

        arguments?.getString("selected_skill")?.let { sharedViewModel.selectSkill(it) }

        advertisementViewModel.listOfAdvertisements.observe(viewLifecycleOwner) { listOfAdv ->
            sharedViewModel.selected_skill.observe(viewLifecycleOwner) { selected_skill ->
                fullListForGivenSkill = listOfAdv.filter {
                    it.listOfSkills.contains(selected_skill) || selected_skill == "All"
                }
            }
        }

        fullListForGivenSkill.apply {
            filter {
                if (isMyAdv) {
                    it.accountID == currentAccountID
                } else true
            }
        }
        if (search != null) {
            fullListForGivenSkill = fullListForGivenSkill.filter { it.advTitle.contains(search!!, true) }
        }

        //compose recycler view
        view.findViewById<TextView>(R.id.defaultTextTimeslotsList).isVisible = fullListForGivenSkill.isEmpty()
        view.findViewById<ImageView>(R.id.create_hint).isVisible = fullListForGivenSkill.isEmpty()
        this.recyclerView.layoutManager = LinearLayoutManager(this.context)
        val advAdapterCard = AdvAdapterCard(fullListForGivenSkill, advertisementViewModel)

        this.myTimeslotsButton.setOnClickListener {
            isMyAdv = !isMyAdv
            if (isMyAdv) {
                this.myTimeslotsButton.backgroundTintList = AppCompatResources.getColorStateList(requireContext(), R.color.orange_poli)
                this.myTimeslotsButton.setTextColor(AppCompatResources.getColorStateList(requireContext(), R.color.black))
            } else {
                this.myTimeslotsButton.backgroundTintList = AppCompatResources.getColorStateList(requireContext(), R.color.darkGray)
                this.myTimeslotsButton.setTextColor(AppCompatResources.getColorStateList(requireContext(), R.color.lightGray))
            }
            advAdapterCard.switchMode(isMyAdv, currentAccountID)
        }

        this.directionButton.setOnClickListener {
            isUp = !isUp
            if (isUp) this.directionButton.setImageResource(R.drawable.sort_up)
            else this.directionButton.setImageResource(R.drawable.sort_down)
            sharedViewModel.sortParam.observe(viewLifecycleOwner) {
                if (it == "Title") param = 0
                else if (it == "Duration") param = 1
                else if (it == "Starting time") param = 2
                else if (it == "Ending time") param = 3
                else if (it == "Date") param = 4
                advAdapterCard.switchSort(isUp, param)
            }

        }

        sharedViewModel.filter.observe(viewLifecycleOwner) {
            advAdapterCard.filterAdvertisementList(fullListForGivenSkill, it)
        }

        // Adapter setting
        this.recyclerView.adapter = advAdapterCard


        // On back pressed
        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigate(R.id.action_ShowListTimeslots_to_showListOfServices)
                }
            })
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
            resources.getString(R.string.title) ->
                sharedViewModel.setSortParam(resources.getString(R.string.title))
            resources.getString(R.string.duration_menu) ->
                sharedViewModel.setSortParam(resources.getString(R.string.duration_menu))
            resources.getString(R.string.starting_time_menu) ->
                sharedViewModel.setSortParam(resources.getString(R.string.starting_time_menu))
            resources.getString(R.string.ending_time_menu) ->
                sharedViewModel.setSortParam(resources.getString(R.string.ending_time_menu))
            resources.getString(R.string.date) ->
                sharedViewModel.setSortParam(resources.getString(R.string.date))
        }
        return true
    }
}

