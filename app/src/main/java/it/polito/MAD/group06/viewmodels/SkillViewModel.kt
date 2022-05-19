package it.polito.MAD.group06.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import it.polito.MAD.group06.models.advertisement.Advertisement
import it.polito.MAD.group06.models.skill.Skill
import it.polito.MAD.group06.models.userprofile.UserProfile
import it.polito.MAD.group06.repository.SkillRepository
import java.lang.Exception

class SkillViewModel(application: Application) : AndroidViewModel(application) {
    /**
     * Repository
     */
    private val repositorySkill = SkillRepository(application)
    private val db = FirebaseFirestore.getInstance()
    private var listenerRegistration: ListenerRegistration
    private val context = application

    /**
     * Single [Skill]
     */
    private var _singleSkillPH = Skill(-1, "")

    private val _pvtSkill = MutableLiveData<Skill>().also {
        it.value = _singleSkillPH
    }
    val skill: LiveData<Skill> = this._pvtSkill

    /**
     * List of Advertisements
     */
    private val _skills = MutableLiveData<List<Skill>>()
    val listOfSkills: LiveData<List<Skill>> = _skills

    init {
        listenerRegistration = db.collection("Skill")
            .addSnapshotListener { value, error ->
                if (error != null) {
                    _skills.value = emptyList()
                } else {
                    _skills.value = value!!.mapNotNull { elem ->
                        elem.toSkill()
                    }
                }
            }
    }

    private fun DocumentSnapshot.toSkill(): Skill? {
        return try {
            val id = this.get("id") as Long
            val skillName = this.get("skill_name") as String
            Skill(
                id, skillName
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun getAll(): LiveData<List<Skill>> {
        return this.repositorySkill.getAll()
    }

    fun insertSkill(skill: Skill) {
        this.repositorySkill.insertSkill(skill)
    }

    /**
     * Unsubscribe from the Listener Registration
     */
    override fun onCleared() {
        super.onCleared()
        listenerRegistration.remove()
    }
}