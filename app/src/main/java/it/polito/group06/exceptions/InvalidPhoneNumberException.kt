package it.polito.group06.exceptions

class InvalidPhoneNumberException(pn: String) : Exception(pn + " is not a valid phone number.")