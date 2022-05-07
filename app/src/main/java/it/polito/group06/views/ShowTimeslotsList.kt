package it.polito.group06.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import it.polito.group06.R
import it.polito.group06.models.time_slot_adv_database.AdsAdapterCard
import it.polito.group06.viewmodels.AdvertisementViewModel

class ShowTimeslotsList : Fragment(R.layout.show_timeslots_frag) {

    private val advViewModel by viewModels<AdvertisementViewModel>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var newAdvButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(
            R.layout.show_timeslots_frag,
            container,
            false
        );
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        this.newAdvButton = view.findViewById(R.id.newAdvButton)
        this.newAdvButton.setOnClickListener {

        }

        /*
        advViewModel.insertAd(
            Advertisement(
                null, "Help with your homeworks",
                "This is description #1", "Turin",
                "null", 3f, "Alessia", false
            )
        )

        advViewModel.insertAd(
            Advertisement(
                null, "Create the design of your dreams!",
                "This is description #1", "Milan",
                "null", 6f, "Mark", false
            )
        )

        advViewModel.insertAd(
            Advertisement(
                null, "A trip on an original gondola",
                "This is description #1", "Venice",
                "null", 2f, "Laura", false
            )
        )

        advViewModel.insertAd(
            Advertisement(
                null, "How to do a pizza: the ultimate tutorial",
                "This is description #1", "Naples",
                "null", 0.5f, "Maria Anna", false
            )
        )
         */

        advViewModel.ads.observe(this.viewLifecycleOwner) { listOfAdv ->
            /**
             * If there are no advertisements in the DB proper texts are shown.
             */
            view.findViewById<TextView>(R.id.tvListTitle).isVisible = !(listOfAdv == null || listOfAdv.isEmpty())
            view.findViewById<TextView>(R.id.defaultTextTimeslotsList).isVisible = listOfAdv == null || listOfAdv.isEmpty()
            view.findViewById<ImageView>(R.id.create_hint).isVisible = listOfAdv == null || listOfAdv.isEmpty()

            if (!(listOfAdv == null || listOfAdv.isEmpty())) {
                this.recyclerView = view.findViewById(R.id.rvAdvFullList)
                this.recyclerView.layoutManager = LinearLayoutManager(this.context)
                this.recyclerView.adapter = AdsAdapterCard(listOfAdv)
            }
        }
    }
}
