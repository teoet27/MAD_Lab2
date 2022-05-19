package it.polito.MAD.group06.repository

import android.app.Application
import com.google.firebase.firestore.FirebaseFirestore
import it.polito.MAD.group06.models.skill.Skill
import it.polito.MAD.group06.models.skill.SkillDatabase

class SkillRepository(application: Application) {
    private val db = FirebaseFirestore.getInstance()
    private val skillDAO = SkillDatabase.getDatabase(application).skillDao()

    fun insertSkill(skill: Skill) = skillDAO.insertSkill(skill)
    fun getAll() = skillDAO.findAll()
    fun removeSkill(id: Long) = skillDAO.removeSkill(id)
}