package com.example.android.birdsdaycounter.singleBirds.recyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android.birdsdaycounter.R
import com.example.android.birdsdaycounter.singleBirds.models.Bird


class BirdsAdapter(var arrayList: ArrayList<Bird>) : RecyclerView
.Adapter<BirdsAdapter.BirdVH>() {

    private lateinit var myInterface: RecyclerInterface


    class BirdVH(itemView: View, singleBirdInterface: BirdsAdapter.RecyclerInterface) :
        RecyclerView.ViewHolder(itemView) {
        var name: TextView = itemView.findViewById(R.id.bird_name)
        var age: TextView = itemView.findViewById(R.id.bird_age)
        var gender: TextView = itemView.findViewById(R.id.bird_gender)

        init {
            itemView.setOnClickListener {
                singleBirdInterface.onClick(position = adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BirdVH {
        val view: View = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.bird_card, parent, false)

        return BirdVH(view, myInterface)
    }

    override fun onBindViewHolder(holder: BirdVH, position: Int) {
        val dataObject = arrayList[position]
        holder.age.text = dataObject.age.toString()
        holder.gender.text = dataObject.gender.toString()
        holder.name.text = dataObject.name.toString()

    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    fun setOnClicked(recyclerInterface: RecyclerInterface) {
        myInterface = recyclerInterface
    }

    interface RecyclerInterface {
        fun onClick(position: Int)
    }
}