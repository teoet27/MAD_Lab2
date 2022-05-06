package it.polito.group06.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import it.polito.group06.R
import it.polito.group06.viewmodels.TimeSlotAdViewModel

class ShowTimeslotsList : Fragment(R.layout.show_timeslots_frag) {

    private val timeslotsVM by viewModels<TimeSlotAdViewModel>()

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
        // timeslotsVM.insertAd(TimeSlotAd(null, "Ciao", "x", "x", "x"))
        timeslotsVM.ads.observe(this.viewLifecycleOwner) { listOfAdv ->
            /**
             * If there are no advertisements in the DB a proper text is shown.
             */
            view.findViewById<TextView>(R.id.defaultTextTimeslotsList).isVisible = listOfAdv == null || listOfAdv.isEmpty()
            view.findViewById<ImageView>(R.id.create_hint).isVisible = listOfAdv == null || listOfAdv.isEmpty()
        }
    }
}
