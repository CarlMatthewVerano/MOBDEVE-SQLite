package com.mobdeve.tighee.samplemysqliteapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobdeve.tighee.samplemysqliteapp.databinding.ActivityMainBinding
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    private val executorService = Executors.newSingleThreadExecutor()
    private lateinit var contacts: ArrayList<Contact>
    private lateinit var myAdapter: MyAdapter
    private lateinit var myDbHelper: MyDbHelper

    private val myActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        // Notice that we're checking our own defined result codes for Add and Edit.
        if (result.data != null) {
            if (result.resultCode == ResultCodes.ADD_RESULT.ordinal) { // AdD
                contacts.add(
                    0, Contact(
                        result.data!!.getStringExtra(IntentKeys.LAST_NAME_KEY.name)!!,
                        result.data!!.getStringExtra(IntentKeys.FIRST_NAME_KEY.name)!!,
                        result.data!!.getStringExtra(IntentKeys.NUMBER_KEY.name)!!,
                        result.data!!.getStringExtra(IntentKeys.IMAGE_URI_KEY.name)!!
                    )
                )
                myAdapter.notifyItemInserted(0)
            } else if (result.resultCode == ResultCodes.EDIT_RESULT.ordinal) { // EDIT
                /* DONE: Logic for handling the edit return. Update the RecyclerView.
                 * */
                val contactId = result.data!!.getLongExtra(IntentKeys.CONTACT_ID_KEY.name, -1)
                val updatedContact = Contact(
                    result.data!!.getStringExtra(IntentKeys.LAST_NAME_KEY.name)!!,
                    result.data!!.getStringExtra(IntentKeys.FIRST_NAME_KEY.name)!!,
                    result.data!!.getStringExtra(IntentKeys.NUMBER_KEY.name)!!,
                    result.data!!.getStringExtra(IntentKeys.IMAGE_URI_KEY.name)!!
                )

                Log.i("MainActivity", "Updated Contact: $updatedContact")

                // Check if it exists in the contacts arraylist
                val index = contacts.indexOfFirst { it.id == contactId }            // returns the id that matches the predicate, -1 if not found
                if (index != -1) {
                    contacts[index] = updatedContact
                    myAdapter.notifyItemChanged(index)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewBinding: ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        // this.contacts = new ArrayList<>();
        // We no longer initialized our ArrayList from scratch as we'd want to initialize it with
        // whatever is in the DB -- empty or not.

        // Logic to handle the initialization of our ArrayList
        executorService.execute {
            myDbHelper = MyDbHelper.getInstance(this@MainActivity)!!
            contacts = myDbHelper.getAllContactsDefault()

            printContactsToLog() // Prints to the log

            viewBinding.recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
            // Notice we're passing in the myActivityResultLauncher to the Adapter
            myAdapter = MyAdapter(contacts, myActivityResultLauncher)
            viewBinding.recyclerView.adapter = myAdapter
        }

        viewBinding.addContactBtn.setOnClickListener(View.OnClickListener {
            val i = Intent(this@MainActivity, AddContactActivity::class.java)
            myActivityResultLauncher.launch(i)
        })
    }

    private fun printContactsToLog() {
        for (c in contacts) {
            Log.d("MainActivity", "printAllContacts: $c")
        }
    }
}