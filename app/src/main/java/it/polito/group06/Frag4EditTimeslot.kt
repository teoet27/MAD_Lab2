package it.polito.group06

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class Frag4EditTimeslot: Fragment(R.layout.frag4_edit_timeslot) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tv = view.findViewById<TextView>(R.id.editTimeslot)
        tv.setOnClickListener { findNavController().navigate(R.id.action_frag4EditTimeslot_to_frag3ShowTimeslot) }
    }

}