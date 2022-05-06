package it.polito.group06.views

import android.media.Image
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import it.polito.group06.R

class ShowTimeslotsList: Fragment(R.layout.show_timeslots_frag) {

    private lateinit var defaultText: TextView

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?):View?{

        return inflater.inflate(
            R.layout.show_timeslots_frag,
            container,
            false);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tmpList = mutableListOf<String>("ciao", "sono", "vivi")
        val rv = view.findViewById<RecyclerView>(R.id.rvAdvFullList)
        this.defaultText = view.findViewById(R.id.defaultTextTimeslotsList)
        if(true) {
            this.defaultText.isVisible = true
            view.findViewById<ImageView>(R.id.create_hint).isVisible=true
        }
    }
}
