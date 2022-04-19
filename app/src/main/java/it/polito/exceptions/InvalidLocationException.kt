package it.polito.exceptions

class InvalidLocationException(location: String) : Exception(location + " is not a valid location.")