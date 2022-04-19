package it.polito.exceptions

class InvalidNicknameException(msg: String) : Exception(msg + " is not a valid nickname.")