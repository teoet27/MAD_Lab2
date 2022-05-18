package it.polito.MAD.group06.repository

import android.app.Application
import it.polito.MAD.group06.models.skill.Skill
import it.polito.MAD.group06.models.skill.SkillDatabase
import it.polito.MAD.group06.remote.FirestoreDatabase

class SkillRepository(application: Application) {
    private val db = FirestoreDatabase.getDatabase(application)
    private val skillDAO = SkillDatabase.getDatabase(application).skillDao()

    fun insertSkill(skill: Skill) = skillDAO.insertSkill(skill)
    fun getAll() = skillDAO.findAll()
    fun removeSkill(id: Long) = skillDAO.removeSkill(id)
    fun findByCategory(category: String) = skillDAO.findByCategory(category)
}