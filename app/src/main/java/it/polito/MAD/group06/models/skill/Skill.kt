package it.polito.MAD.group06.models.skill

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.PropertyName

@Entity(tableName = "skillTable")
data class Skill(
    @PrimaryKey(autoGenerate = true)
    @get:PropertyName("id")
    @set:PropertyName("id")
    var id: Long,

    @get:PropertyName("skill_name")
    @set:PropertyName("skill_name")
    var skillName: String
)