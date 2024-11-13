package com.mobdeve.tighee.samplemysqliteapp

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.tighee.samplemysqliteapp.databinding.ItemLayoutBinding
import java.util.concurrent.Executors

class MyAdapter(private val contacts: ArrayList<Contact>, private val myActivityResultLauncher: ActivityResultLauncher<Intent>) : RecyclerView.Adapter<MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemViewBinding: ItemLayoutBinding = ItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false)
        val myViewHolder = MyViewHolder(itemViewBinding)

        /* Edit Logic
         *      To implement an edit, we're going to extend AddContactActivity to accommodate
         *      for editing as well. First, we'll send an intent to the activity with the info
         *      from the contacts ArrayList. In the AddContactActivity, we'll need to know an
         *      edit operation was performed; hence, we'll use the CONTACT_ID_KEY as a reference.
         *      When we retrieve it, we should have a value because the contact-to-edit should
         *      already be in the DB. If no edit was passed, we're assuming its an add. We'll
         *      also need to modify the DB operation in AddContactActivity so that it doesn't
         *      just perform an ADD.
         * */
        myViewHolder.setEditBtnOnClickListener(View.OnClickListener { view ->
            val i = Intent(view.context, AddContactActivity::class.java)
            i.putExtra(IntentKeys.FIRST_NAME_KEY.name, contacts[myViewHolder.adapterPosition].firstName)
            i.putExtra(IntentKeys.LAST_NAME_KEY.name, contacts[myViewHolder.adapterPosition].lastName)
            i.putExtra(IntentKeys.NUMBER_KEY.name, contacts[myViewHolder.adapterPosition].number)
            i.putExtra(IntentKeys.IMAGE_URI_KEY.name, contacts[myViewHolder.adapterPosition].imageUri)
            i.putExtra(IntentKeys.CONTACT_ID_KEY.name, contacts[myViewHolder.adapterPosition].id)
            myActivityResultLauncher.launch(i)
        })

        /* Delete Logic
         * TODO: Supply the deletion logic in the onClick method
         *       If you'd rather set the listener from the MainActivity, you can modify the
         *       constructor to accommodate the passing of an OnClickListener. Regardless, you have
         *       a copy of the Contacts data in the Adapter, so you can modify the data here.
         * */
        myViewHolder.setDeleteBtnOnClickListener(View.OnClickListener {
            val position = myViewHolder.adapterPosition
            val contactId = contacts[position].id
            // Remove the item from the ArrayList
            contacts.removeAt(myViewHolder.adapterPosition)
            // Notify the adapter that the item was removed
            notifyItemRemoved(myViewHolder.adapterPosition)

            val executorService = Executors.newSingleThreadExecutor()
            executorService.execute {
                val myDbHelper = MyDbHelper.getInstance(myViewHolder.itemView.context)
                myDbHelper!!.deleteContact(contactId.toInt())
            }
        })

        return myViewHolder
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindData(contacts[position])
    }

    override fun getItemCount(): Int {
        return contacts.size
    }
}