package com.example.mad_lab2

class Profile(name:String , surname:String) {
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
        this.email = "s.sagristano@mail.com"
        this.location = "Via del Poli, 17 - Turin (TO) - Piedmont - Italy"
        this.qualification = "Professional Educator"
        this.nickname = "@sibilla.sagristano"
    }
}