package it.polito.madcourse.group06.views.setting

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import it.polito.madcourse.group06.R
import it.polito.madcourse.group06.activities.GoogleLoginActivity
import it.polito.madcourse.group06.viewmodels.UserProfileViewModel

class Setting : Fragment() {

    private lateinit var deleteYourAccountButton: TextView
    private val userProfileViewModel: UserProfileViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.setting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.deleteYourAccountButton = view.findViewById(R.id.deleteYourAccountButtonID)
        this.deleteYourAccountButton.setOnClickListener {
            showDeleteYourUserWindow()
        }

        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigate(R.id.action_setting_to_ShowListOfServices)
                }
            })

    }

    private fun showDeleteYourUserWindow() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this.context)
        val input = EditText(this.context)
        val description = TextView(this.context)
        val linearLayout = LinearLayout(this.context)

        userProfileViewModel.currentUser.observe(viewLifecycleOwner) { user ->
            builder.setTitle("Delete your account")

            description.text = "Type your nickname here in order to confirm your will in deleting your account."
            description.inputType = InputType.TYPE_CLASS_TEXT
            description.gravity = Gravity.LEFT

            input.hint = "${user.nickname}"
            input.inputType = InputType.TYPE_CLASS_TEXT
            input.gravity = Gravity.LEFT

            linearLayout.orientation = LinearLayout.VERTICAL
            linearLayout.setPadding(64, 0, 64, 0)
            linearLayout.addView(description)
            linearLayout.addView(input)

            builder.setView(linearLayout)
            builder.setPositiveButton("Delete", DialogInterface.OnClickListener { dialog, which ->
                if (input.text.toString() == user.nickname) {
                    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id_bis))
                        .requestEmail()
                        .build()
                    val googleSignOut = GoogleSignIn.getClient(requireActivity(), gso)
                    googleSignOut.signOut().addOnCompleteListener {
                        userProfileViewModel.removeUserProfileByID(user.id!!)
                        val intent = Intent(requireContext(), GoogleLoginActivity::class.java)
                        startActivity(intent)
                    }
                } else {
                    dialog.cancel()
                }
            })
            builder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which ->
                dialog.cancel()
            })
            builder.show()
        }
    }

}