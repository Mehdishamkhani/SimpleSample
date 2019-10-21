package org.sana.simpleapp.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.sana.simpleapp.model.FeedDataModel
import org.sanasimpleapp.R

/**
 * Created by mehdi on 19/10/2019.
 */

class UserDetailsAdapter(private val feedDataModel: MutableList<FeedDataModel>?) : RecyclerView.Adapter<UserDetailsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.user_details_row, parent, false))
    }

    fun addData(data: List<FeedDataModel>) {

        feedDataModel!!.addAll(data)
        notifyDataSetChanged()

    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val feed = feedDataModel!![position]

        holder.addressTextView.text = feed.address!!.toString()

        holder.phoneTextView.text = feed.coordinate_mobile
        holder.familyTextView.text = feed.first_name!!.plus(" ").plus(feed.last_name)
        holder.familyTextView.isSelected = true
        holder.phoneTextView.isSelected = true

    }

    override fun getItemCount(): Int {
        return feedDataModel!!.size
    }


    inner class ViewHolder(itemLayoutView: View) : RecyclerView.ViewHolder(itemLayoutView) {

        var addressTextView: TextView = itemLayoutView.findViewById(R.id.address_textview)
        var familyTextView: TextView = itemLayoutView.findViewById(R.id.family_textview)
        var phoneTextView: TextView = itemLayoutView.findViewById(R.id.phone_textview)

    }
}
