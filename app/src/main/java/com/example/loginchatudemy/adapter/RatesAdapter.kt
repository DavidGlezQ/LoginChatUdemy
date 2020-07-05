package com.example.loginchatudemy.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.loginchatudemy.R
import com.example.loginchatudemy.inflate
import com.example.loginchatudemy.models.Rate
import com.example.loginchatudemy.utils.CircleTransform
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_rates_item.view.*
import java.text.SimpleDateFormat

//Este ViewHolder sirve para volcar los datos introduciodos en el rateFragment.
class RatesAdapter(private val items: List<Rate>): RecyclerView.Adapter<RatesAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(parent.inflate(R.layout.fragment_rates_item))
    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position])
    override fun getItemCount() = items.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(rate: Rate) = with(itemView) {
            textViewRate.text = rate.text
            textViewStar.text = rate.rate.toString()
            textViewCalendar.text = SimpleDateFormat("dd MMM, yyyy").format(rate.createAt)
            if (rate.profileImgUrl.isEmpty()){
                Picasso.get().load(R.drawable.ic_person_profile).resize(100, 100)
                    .centerCrop().transform(CircleTransform()).into(imageViewProfile)
            } else {
                Picasso.get().load(rate.profileImgUrl).resize(100, 100)
                    .centerCrop().transform(CircleTransform()).into(imageViewProfile)
            }
        }
    }
}