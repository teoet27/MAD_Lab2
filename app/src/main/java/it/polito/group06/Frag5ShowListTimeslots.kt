package it.polito.group06

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class Frag5ShowListTimeslots: Fragment(R.layout.frag5_show_list_timeslots) {

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?):View?{

        return inflater.inflate(R.layout.frag5_show_list_timeslots,
            container,
            false);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tv = view.findViewById<TextView>(R.id.textView5)
        tv.setOnClickListener { findNavController().navigate(R.id.action_frag5ShowListTimeslots_to_frag3ShowTimeslot) }
    }

}