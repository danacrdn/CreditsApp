package com.example.creditsapp.domain.model


data class EditableProfileData(
    var firstName: String = "",
    var lastName: String = "",
    var degreeName: String = "",
    var email: String = "",
    var password: String = ""
)
