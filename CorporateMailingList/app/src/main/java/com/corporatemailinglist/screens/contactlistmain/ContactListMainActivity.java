package com.corporatemailinglist.screens.contactlistmain;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.corporatemailinglist.R;
import com.corporatemailinglist.repository.datamodels.Contact;
import com.corporatemailinglist.repository.firestore.ContactsFirestoreDbContract;
import com.corporatemailinglist.repository.firestore.ContactsFirestoreManager;
import com.corporatemailinglist.screens.contactdetails.ContactDetailsActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class ContactListMainActivity extends AppCompatActivity {
    private static String TAG = ContactListMainActivity.class.toString();

    /* Widgets */
    private Toolbar toolbar;
    private RecyclerView contactListRecyclerView;

    private FloatingActionButton sendBulkDataFloatingButton;
    private FloatingActionButton addFloatingButton;
    private TextView emptyTextView;

    /* Content objects */
    private List<Contact> contactList;

    /* Repository reference */
    private ContactsFirestoreManager contactsFirestoreManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_list_main_activity);

        // Get a reference of contactsFirestoreManager
        // TODO: 3.1 Getting the Backend Reference
        contactsFirestoreManager = ContactsFirestoreManager.newInstance();

        // Set up the toolbar
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.app_alias));
        setSupportActionBar(toolbar);

        // Set up the contactListRecyclerView
        contactListRecyclerView = findViewById(R.id.contactListRecyclerView);
        contactListRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Set up the emptyTextView
        emptyTextView = findViewById(R.id.emptyTextView);

        // Set up the sendBulkDataFloatingButton
        sendBulkDataFloatingButton = findViewById(R.id.sendBulkDataFloatingButton);
        sendBulkDataFloatingButton.setOnClickListener(new SendContactsBulkFloatingButtonOnClickListener());

        // Set up the addFloatingButton
        addFloatingButton = findViewById(R.id.addFloatingButton);
        addFloatingButton.setOnClickListener(new AddFloatingButtonOnClickListener());
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Populate the ContactListMainActivity with the available data
        // TODO: 3.3 Reading Contacts
        contactsFirestoreManager.getAllContacts(new GetAllContactsOnCompleteListener());

    }

    // Uncomment this class...
    private class GetAllContactsOnCompleteListener implements OnCompleteListener<QuerySnapshot> {

        @Override
        public void onComplete(@NonNull Task<QuerySnapshot> task) {

            // TODO: 3.3 Reading Contacts
            if (task.isSuccessful()) {

                // Get the query snapshot from the task result
                QuerySnapshot querySnapshot = task.getResult();

                if (querySnapshot != null) {

                    // Get the contact list from the query snapshot
                    contactList = querySnapshot.toObjects(Contact.class);
                }

            } else {
                Log.w(TAG, "Error getting documents: ", task.getException());
            }

            // TODO 3.4 Populating the Main Screen
            populateContactRecyclerView(contactList);

            // If the contactList is empty, show the emptyTextView. Otherwise, show the contactListRecyclerView
            if (contactList == null || contactList.size() == 0) {
                contactListRecyclerView.setVisibility(View.GONE);
                emptyTextView.setVisibility(View.VISIBLE);

            } else {
                contactListRecyclerView.setVisibility(View.VISIBLE);
                emptyTextView.setVisibility(View.GONE);
            }
        }
    }

    /** Sets the contact List in the Adapter to populate the RecyclerView */
    private void populateContactRecyclerView(List<Contact> contactList) {

        // TODO: 3.4 Populating the Main Screen

        // Set the contactListMainRecyclerViewAdapter in the contactListRecyclerView
        ContactListMainRecyclerViewAdapter contactListMainRecyclerViewAdapter =
                new ContactListMainRecyclerViewAdapter(contactList, new ContactListRecyclerViewOnItemClickListener());
        contactListRecyclerView.setAdapter(contactListMainRecyclerViewAdapter);

    }

    /** Called when the user clicks on an item of the contact list */
    public class ContactListRecyclerViewOnItemClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {

            int itemIndex = contactListRecyclerView.indexOfChild(view);
            Log.d("ContactListMainActivity", "" + itemIndex);

            Contact contact = contactList.get(itemIndex);

            Intent intent = new Intent();
            intent.setClass(ContactListMainActivity.this, ContactDetailsActivity.class);
            intent.putExtra(ContactDetailsActivity.OPERATION, ContactDetailsActivity.EDITING);

            // TODO: 4.2 Updating a Contact
            intent.putExtra(ContactsFirestoreDbContract.DOCUMENT_ID, contact.getDocumentId());
            intent.putExtra(ContactsFirestoreDbContract.FIELD_FIRST_NAME, contact.getFirstNameString());
            intent.putExtra(ContactsFirestoreDbContract.FIELD_LAST_NAME, contact.getLastNameString());
            intent.putExtra(ContactsFirestoreDbContract.FIELD_EMAIL, contact.getEmailString());

            startActivity(intent);
        }
    }

    /** Called when the user clicks the Send Contacts button */
    private class SendContactsBulkFloatingButtonOnClickListener
            implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            // TODO: 3.2 Sending Bulk Data to Firestore
            contactsFirestoreManager.sendContactsBulk();

            Toast.makeText(ContactListMainActivity.this, "Contacts bulk sent", Toast.LENGTH_LONG).show();
        }
    }

    /** Called when the user clicks the Add button */
    private class AddFloatingButtonOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {

            Intent intent = new Intent();
            intent.setClass(ContactListMainActivity.this, ContactDetailsActivity.class);
            intent.putExtra(ContactDetailsActivity.OPERATION, ContactDetailsActivity.CREATING);

            startActivity(intent);
        }
    }
}
