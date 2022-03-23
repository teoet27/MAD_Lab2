package com.example.mad_lab2

class Profile(name:String , surname:String, email: String, location: String, qualification: String, nickname: String) {
    var name: String
    var surname: String
    var skills: List<String>
    var email: String
    var location: String
    var qualification: String
    var nickname: String

    init {
        this.name = name
        this.surname = surname
        this.skills = listOf()
        this.email = email
        this.location = location
        this.qualification = qualification
        this.nickname = nickname
    }
}