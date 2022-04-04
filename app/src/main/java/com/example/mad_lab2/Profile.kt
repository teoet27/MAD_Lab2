package com.example.mad_lab2

class Profile(
    name: String, email: String, location: String,
    qualification: String, nickname: String, description: String,
    imageLocation: String, phoneNumber: String
) {
    var fullname: String = name
    var skills: ArrayList<String> = ArrayList<String>()
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
        if (skills.compareTo("No skills.") != 0 || skills.compareTo("") != 0) {
            val x: List<String> = skills.split(",")
            for (i in x.indices)
                if (x[i].compareTo("") != 0)
                    this.skills.add(i, x[i])
        }
    }
}