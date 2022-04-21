package it.polito.exceptions

class InvalidDateException(date: String) : Exception(date + " is not a valid date.")