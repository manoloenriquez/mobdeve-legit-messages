package com.mobdeve.s18.legitmessages.model;

class User (val uid: String?, val email: String?, displayName: String?){
    var username: String = ""
    var firstName: String = ""
    var lastName: String = ""

    constructor(
        uid: String?,
        email: String?,
        displayName: String?,
        firstName: String,
        lastName: String
    ) : this(uid, email, displayName) {
        this.firstName = firstName
        this.lastName = lastName
    }

    constructor(
        uid: String?,
        email: String?,
        displayName: String?,
        firstName: String,
        lastName: String,
        username: String,
    ) : this(uid, email, displayName, firstName, lastName) {
        this.username = username
    }

}
