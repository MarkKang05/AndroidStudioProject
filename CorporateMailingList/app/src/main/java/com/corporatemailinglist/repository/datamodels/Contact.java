package com.corporatemailinglist.repository.datamodels;

import com.google.firebase.firestore.DocumentId;

public class Contact {

    // TODO: 2.1 Defining the Data Model

    @DocumentId
    private String documentId;
    private String firstNameString;
    private String lastNameString;
    private String emailString;

    public Contact() {}
    public Contact(String firstNameString, String lastNameString, String emailString) {
        this.firstNameString = firstNameString;
        this.lastNameString = lastNameString;
        this.emailString = emailString;
    }
    public String getDocumentId() {
        return documentId;
    }
    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }
    public String getFirstNameString() {
        return firstNameString;
    }
    public void setFirstNameString(String firstNameString) {
        this.firstNameString = firstNameString;
    }
    public String getLastNameString() {
        return lastNameString;
    }
    public void setLastNameString(String lastNameString) {
        this.lastNameString = lastNameString;
    }
    public String getEmailString() {
        return emailString;
    }
    public void setEmailString(String emailString) {
        this.emailString = emailString;
    }
}
