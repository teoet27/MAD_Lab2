package it.polito.group06

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class Frag1ShowProfile: Fragment(R.layout.frag1_show_profile) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_frag1ShowProfile_to_frag5ShowListTimeslots)
            }
        })


        val tv = view.findViewById<TextView>(R.id.textView5)
        tv.setOnClickListener { findNavController().navigate(R.id.action_frag1ShowProfile_to_frag2EditProfile) }
    }
}