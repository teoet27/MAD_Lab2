package it.polito.madcourse.group06.exceptions

class InvalidDateException(date: String) : Exception(date + " is not a valid date.")