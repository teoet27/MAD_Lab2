package it.polito.madcourse.group06.legacy

import java.util.*

class Profile(
    name: String, email: String, location: String,
    qualification: String, nickname: String, description: String,
    imageLocation: String, phoneNumber: String
) {
    var fullname: String = name
    var skillList: ArrayList<String> = ArrayList<String>()
    var email: String = email
    var location: String = location
    var qualification: String = qualification
    var nickname: String = nickname
    var description: String = description
    var imageLocation: String = imageLocation
    var phoneNumber: String = phoneNumber

    constructor(
        name: String, skills: String, email: String, location: String,
        qualification: String, nickname: String, description: String,
        imageLocation: String, phoneNumber: String
    ) : this(
        name, email, location,
        qualification,
        nickname,
        description,
        imageLocation,
        phoneNumber
    ) {
        if (skills.compareTo("") != 0 && skills.split(" ").size != skills.length + 1) {
            val x: List<String> = skills.split(",")
            for (i in x.indices) {
                if (x[i].compareTo("") != 0) {
                    this.skillList.add(i, x[i].trim())
                }
            }
        }
    }
}