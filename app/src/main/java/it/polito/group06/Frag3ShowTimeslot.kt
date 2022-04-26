package it.polito.group06

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class Frag3ShowTimeslot: Fragment(R.layout.frag3_show_timeslot) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tv = view.findViewById<TextView>(R.id.textView5)
        tv.setOnClickListener { findNavController().navigate(R.id.action_frag3ShowTimeslot_to_frag4EditTimeslot) }
    }

}