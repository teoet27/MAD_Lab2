package com.example.mad_lab2

class Profile(name:String , email: String, location: String, qualification: String, nickname: String, imageLocation: String) {
    var fullname: String
    var skills: List<String>
    var email: String
    var location: String
    var qualification: String
    var nickname: String
    var imageLocation: String

    init {
        this.fullname = name
        this.skills = listOf()
        this.email = email
        this.location = location
        this.qualification = qualification
        this.nickname = nickname
        this.imageLocation = imageLocation
    }
}