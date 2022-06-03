package it.polito.madcourse.group06.views.mychat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import it.polito.madcourse.group06.R
import it.polito.madcourse.group06.models.advertisement.Advertisement
import it.polito.madcourse.group06.models.mychat.ActiveChat
import it.polito.madcourse.group06.models.mychat.ActiveChatAdapter
import it.polito.madcourse.group06.viewmodels.AdvertisementViewModel
import it.polito.madcourse.group06.viewmodels.MyChatViewModel
import it.polito.madcourse.group06.viewmodels.UserProfileViewModel

class ActiveChats : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var activeChatAdapter: ActiveChatAdapter
    private val listOfChatsAsCustomer: ArrayList<ActiveChat> = arrayListOf()
    private val listOfChatsAsProvider: ArrayList<ActiveChat> = arrayListOf()
    private val myChatViewModel: MyChatViewModel by activityViewModels()
    private val userProfileViewModel: UserProfileViewModel by activityViewModels()
    private val advertisementViewModel: AdvertisementViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.active_chats_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.recyclerView = view.findViewById(R.id.activeChatsRecyclerViewID)
        this.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        activeChatAdapter = ActiveChatAdapter(listOfChatsAsCustomer)
        this.recyclerView.adapter = activeChatAdapter

        userProfileViewModel.currentUser.observe(viewLifecycleOwner) { currentUser ->
            advertisementViewModel.listOfAdvertisements.observe(viewLifecycleOwner) { listOfAdvertisement ->
                myChatViewModel.listOfChats.observe(viewLifecycleOwner) { listOfChats ->
                    var adv = Advertisement(
                        "", "", "", arrayListOf(),
                        "", "", "", "", 0.0, "",
                        "", "", "", "", 0.0
                    )
                    for (chat in listOfChats) {
                        //if (chat.userID == currentUser.id!!) {
                        // as customer
                        for (elem in listOfAdvertisement) {
                            if (chat.advID == elem.id!!) {
                                adv = elem
                                break
                            }
                        }
                        activeChatAdapter.addActiveChat(
                            ActiveChat(
                                adv.advTitle,
                                adv.advAccount,
                                chat.chatID
                            )
                        )
                        //}
                    }
                }
            }
        }

        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigate(R.id.action_activeChats_to_ShowListOfServices)
                }
            })
    }
}