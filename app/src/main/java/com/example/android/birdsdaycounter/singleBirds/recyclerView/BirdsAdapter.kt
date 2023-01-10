package com.example.android.birdsdaycounter.singleBirds.recyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android.birdsdaycounter.R
import com.example.android.birdsdaycounter.singleBirds.models.Bird

open class BirdsAdapter(var arrayList: ArrayList<Bird> , private val onClickListener: OnClickListener) : RecyclerView
.Adapter<BirdsAdapter.BirdVH>() {


    class BirdVH(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var name: TextView = itemView.findViewById(R.id.bird_name)
        var age: TextView = itemView.findViewById(R.id.bird_age)
        var gender: TextView = itemView.findViewById(R.id.bird_gender)
        val image:ImageView = itemView.findViewById(R.id.bird_img)

        init {

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BirdVH {
        val view: View = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.bird_card, parent, false)

        return BirdVH(view)
    }

    override fun onBindViewHolder(holder: BirdVH, position: Int) {
        val dataObject = arrayList[position]
        holder.age.text = dataObject.age.toString()
        holder.gender.text = dataObject.gender.toString()
        holder.name.text = dataObject.name.toString()
        holder.image.setImageResource(dataObject.image!!)

        holder.itemView.setOnClickListener {
            onClickListener.onClick(bird = dataObject)
        }

    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    class OnClickListener(val clickListener: (bird:Bird) -> Unit) {
        fun onClick(bird:Bird) = clickListener(bird)
    }
}