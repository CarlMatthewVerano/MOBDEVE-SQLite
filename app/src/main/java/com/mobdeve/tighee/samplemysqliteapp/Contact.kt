package com.mobdeve.tighee.samplemysqliteapp

class Contact {
    var id: Long = 0
        private set
    var lastName: String
        private set
    var firstName: String
        private set
    var number: String
        private set
    var imageUri: String
        private set

    // Constructor without ID. This isn't exactly advised as you'll have a hard
    // time trying to update the data without the ID reference
    constructor(lastName: String, firstName: String, number: String, imageUri: String) {
        this.lastName = lastName
        this.firstName = firstName
        this.number = number
        this.imageUri = imageUri
    }

    // This is the more appropriate constructor to use because we have a reference
    // of the Contact's id from the DB
    constructor(lastName: String, firstName: String, number: String, imageUri: String, id: Long) {
        this.lastName = lastName
        this.firstName = firstName
        this.number = number
        this.imageUri = imageUri
        this.id = id
    }

    override fun toString(): String {
        return "Contact{" +
                "id=" + id +
                ", lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", number='" + number + '\'' +
                ", imageUri='" + imageUri + '\'' +
                '}'
    }
}