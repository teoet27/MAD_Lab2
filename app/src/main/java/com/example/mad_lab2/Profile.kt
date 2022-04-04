package com.example.mad_lab2

class Profile(
    name: String, email: String, location: String,
    qualification: String, nickname: String, description: String,
    imageLocation: String, phoneNumber: String
) {
    var fullname: String
    var skills: ArrayList<String>
    var email: String
    var location: String
    var qualification: String
    var nickname: String
    var description: String
    var imageLocation: String
    var phoneNumber: String

    init {
        this.fullname = name
        this.skills = ArrayList<String>()
        this.email = email
        this.location = location
        this.qualification = qualification
        this.nickname = nickname
        this.imageLocation = imageLocation
        this.description = description
        this.phoneNumber = phoneNumber
    }

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