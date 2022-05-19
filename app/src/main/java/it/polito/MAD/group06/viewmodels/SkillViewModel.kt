package it.polito.MAD.group06.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class SkillViewModel(application: Application) : AndroidViewModel(application) {
    /**
     * Single [Skill]
     */
    private var _singleSkillPH: String = ""

    private val _pvtSkill = MutableLiveData<String>().also {
        it.value = _singleSkillPH
    }
    val skill: LiveData<String> = this._pvtSkill

    fun setSkill(skill: String) {
        this._singleSkillPH = skill
        this._pvtSkill.value = this._singleSkillPH
    }

}