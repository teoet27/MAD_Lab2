package it.polito.MAD.group06.exceptions

class InvalidLocationException(location: String) : Exception(location + " is not a valid location.")