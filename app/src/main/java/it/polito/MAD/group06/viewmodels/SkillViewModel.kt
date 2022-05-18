package it.polito.MAD.group06.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import it.polito.MAD.group06.models.skill.Skill
import it.polito.MAD.group06.repository.SkillRepository

class SkillViewModel(application: Application) : AndroidViewModel(application) {
    /**
     * Repository
     */
    private val repositorySkill = SkillRepository(application)

    /**
     * Single [Skill]
     */
    private var _singleSkillPH = Skill(-1, "", "")

    private val _pvtSkill = MutableLiveData<Skill>().also {
        it.value = _singleSkillPH
    }
    val skill: LiveData<Skill> = this._pvtSkill

    fun getAll(): LiveData<List<Skill>> {
        return this.repositorySkill.getAll()
    }

    fun insertSkill(skill: Skill) {
        this.repositorySkill.insertSkill(skill)
    }

}