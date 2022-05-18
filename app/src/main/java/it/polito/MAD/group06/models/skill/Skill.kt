package it.polito.MAD.group06.models.skill

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "skillTable")
data class Skill(
    @PrimaryKey var id: Long,
    var skillName: String,
    var skillCategory: String
)