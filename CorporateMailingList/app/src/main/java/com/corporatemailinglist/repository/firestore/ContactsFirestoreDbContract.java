package com.corporatemailinglist.repository.firestore;

public final class ContactsFirestoreDbContract {

    // TODO: 2.2 Defining the Database Contract

    // Root collection name
    public static final String COLLECTION_NAME = "contacts";

    // Document ID
    public static final String DOCUMENT_ID = "document_id";

    // Document field names
    public static final String FIELD_FIRST_NAME = "first_name";
    public static final String FIELD_LAST_NAME = "last_name";
    public static final String FIELD_EMAIL = "email";

    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private
    private ContactsFirestoreDbContract() {}

}
