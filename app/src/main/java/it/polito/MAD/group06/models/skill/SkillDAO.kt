package it.polito.MAD.group06.models.skill

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SkillDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSkill(skill: Skill)

    @Query("SELECT * FROM skillTable")
    fun findAll(): LiveData<List<Skill>>

    @Query("DELETE FROM skillTable WHERE id = :id")
    fun removeSkill(id: Long)

    @Query("SELECT * FROM skillTable WHERE skillCategory = :category")
    fun findByCategory(category: String): LiveData<List<Skill>>

}