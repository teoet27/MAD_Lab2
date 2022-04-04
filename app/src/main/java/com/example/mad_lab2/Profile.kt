package com.example.mad_lab2

class Profile(
    name: String, skills: String, email: String, location: String,
    qualification: String, nickname: String, description: String,
    imageLocation: String, phoneNumber: String
) {
    var fullname: String
    var skills: String
    var email: String
    var location: String
    var qualification: String
    var nickname: String
    var description: String
    var imageLocation: String
    var phoneNumber: String


    init {
        this.fullname = name
        this.skills = skills
        this.email = email
        this.location = location
        this.qualification = qualification
        this.nickname = nickname
        this.imageLocation = imageLocation
        this.description = description
        this.phoneNumber = phoneNumber
    }
}