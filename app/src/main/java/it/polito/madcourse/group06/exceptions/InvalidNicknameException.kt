package it.polito.madcourse.group06.exceptions

class InvalidNicknameException(msg: String) : Exception(msg + " is not a valid nickname.")