package it.polito.madcourse.group06.views.timeslot

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import it.polito.madcourse.group06.R
import it.polito.madcourse.group06.models.advertisement.AdvAdapterCard
import it.polito.madcourse.group06.utilities.TimeslotTools
import it.polito.madcourse.group06.viewmodels.AdvertisementViewModel
import it.polito.madcourse.group06.viewmodels.SharedViewModel
import java.sql.Time

class ShowListOfTimeslots : Fragment(R.layout.show_timeslots_frag) {

    private val advertisementViewModel: AdvertisementViewModel by activityViewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var newAdvButton: Button
    private lateinit var filterButton: TextView
    private lateinit var sortParam: TextView
    private lateinit var directionButton: ImageView
    private lateinit var barrier:TextView

    var sortDirection:Int=1


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

        registerForContextMenu(sortParam)

        this.newAdvButton.setOnClickListener {
            findNavController().navigate(R.id.action_ShowListTimeslots_to_newTimeSlotDetailsFragment)
        }

        enableUI(true)
        sharedViewModel.selected.observe(viewLifecycleOwner) {
            enableUI(!it)
        }
        filterButton.setOnClickListener {
            activity?.supportFragmentManager!!.
            beginTransaction().
            setCustomAnimations(R.anim.slide_in_up, 0).
            add(R.id.nav_host_fragment_content_main, FilterTimeslots()).
            commit()
        }

        sortParam.setOnClickListener {
            activity?.openContextMenu(sortParam)
        }

        directionButton.setOnClickListener{
            sortDirection=(sortDirection+1)%2 //toggle direction
            // change sort direction
        }

        advertisementViewModel.listOfAdvertisements.observe(viewLifecycleOwner) { listOfAdv ->
            /**
             * If there are no advertisements in the DB proper texts are shown.
             */

            val filteredListOfSkills = listOfAdv.filter {
                it.listOfSkills.contains(arguments?.getString("selected_skill")) ||
                        arguments?.getString("selected_skill") == "All"
            }

            view.findViewById<TextView>(R.id.defaultTextTimeslotsList).isVisible = filteredListOfSkills.isEmpty()
            view.findViewById<ImageView>(R.id.create_hint).isVisible = filteredListOfSkills.isEmpty()

            this.recyclerView.layoutManager = LinearLayoutManager(this.context)
            this.recyclerView.adapter = AdvAdapterCard(filteredListOfSkills, advertisementViewModel)

            sharedViewModel.filter.observe(viewLifecycleOwner) { filter ->
                TimeslotTools().filterAdvertisementList(filteredListOfSkills, filter)?.also {
                    view.findViewById<TextView>(R.id.defaultTextTimeslotsList).isVisible = it.isEmpty()
                    view.findViewById<ImageView>(R.id.create_hint).isVisible = it.isEmpty()

                    if (it.isNotEmpty()) {
                        this.recyclerView.layoutManager = LinearLayoutManager(this.context)
                        this.recyclerView.adapter = AdvAdapterCard(it, advertisementViewModel)
                    }
                }
            }

            sharedViewModel.sortParam.observe(viewLifecycleOwner){ parameter->
                TimeslotTools().sortAdvertisementList(listOfAdv,parameter)

                sharedViewModel.sort_up.observe(viewLifecycleOwner){ up_flag->
                    TimeslotTools().sortAdvertisementList(listOfAdv,parameter,up_flag)

                }
            }
        }

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_ShowListTimeslots_to_showListOfServices)
            }
        })
    }

    private fun enableUI(switch: Boolean) {
        when(switch){
            true-> barrier.visibility=View.GONE
            false->{barrier.visibility=View.VISIBLE
                    barrier.bringToFront()}
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
            resources.getString(R.string.title)->this.sortParam.text=resources.getString(R.string.title)
            resources.getString(R.string.duration_menu)->this.sortParam.text=resources.getString(R.string.duration_menu)
            resources.getString(R.string.starting_time_menu)->this.sortParam.text=resources.getString(R.string.starting_time_menu)
            resources.getString(R.string.ending_time_menu)->this.sortParam.text=resources.getString(R.string.ending_time_menu)
            resources.getString(R.string.starting_date_menu)->this.sortParam.text=resources.getString(R.string.starting_date_menu)
            resources.getString(R.string.ending_date_menu)->this.sortParam.text=resources.getString(R.string.ending_date_menu)
        }
        return true
    }
}
