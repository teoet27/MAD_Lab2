package it.polito.madcourse.group06.views.mychat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import it.polito.madcourse.group06.R
import it.polito.madcourse.group06.models.userforchat.UserForChatAdapterCard
import it.polito.madcourse.group06.viewmodels.UserProfileViewModel

class ShowListOfUsers : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private val userProfileViewModel: UserProfileViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.show_list_of_users, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var userAdapterCard: UserForChatAdapterCard

        this.recyclerView = view.findViewById(R.id.usersRecyclerView)
        this.recyclerView.layoutManager = LinearLayoutManager(this.context)

        userProfileViewModel.listOfUsers.observe(viewLifecycleOwner) { listOfUsers ->
            userAdapterCard = UserForChatAdapterCard(listOfUsers, userProfileViewModel)
            this.recyclerView.adapter = userAdapterCard
        }
    }
}