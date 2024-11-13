package com.mobdeve.tighee.samplemysqliteapp

import android.net.Uri
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.tighee.samplemysqliteapp.databinding.ItemLayoutBinding
import com.squareup.picasso.Picasso

class MyViewHolder(private val viewBinding: ItemLayoutBinding) : RecyclerView.ViewHolder(viewBinding.root) {
    fun bindData(c: Contact) {
        viewBinding.nameTv.text = c.lastName + ", " + c.firstName
        viewBinding.numberTv.text = c.number

        Log.d("TAG", "bindData: $c")

        Picasso.get()
            .load(Uri.parse(c.imageUri))
            .into(viewBinding.imageIv)
    }

    fun setDeleteBtnOnClickListener(onClickListener: View.OnClickListener?) {
        viewBinding.deleteBtn.setOnClickListener(onClickListener)
    }

    fun setEditBtnOnClickListener(onClickListener: View.OnClickListener?) {
        viewBinding.editBtn.setOnClickListener(onClickListener)
    }
}