package it.polito.exceptions

class InvalidPhoneNumberException(pn: String) : Exception(pn + " is not a valid phone number.")