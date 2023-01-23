package com.example.android.birdsdaycounter.allBirds.recyclerView

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.android.birdsdaycounter.R
import com.example.android.birdsdaycounter.globalUse.MyApp
import com.example.android.birdsdaycounter.globalUse.birdStuff.BirdInformation
import com.example.android.birdsdaycounter.allBirds.models.Bird


class AllBirdsAdapter(
    private var arrayList: ArrayList<Bird>?,
    private val onClickListener:OnAddClickListener,
    private val onRemoveClickListener: OnRemoveClickListener
) : RecyclerView.Adapter<AllBirdsAdapter.ViewHolderSingleBird>() {

    class ViewHolderSingleBird(itemView: View ) :
        RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.bird_name)
        val age : TextView = itemView.findViewById(R.id.bird_age)
        val gender:TextView = itemView.findViewById(R.id.bird_gender)
        val img:ImageView = itemView.findViewById(R.id.bird_img)
        val background : ConstraintLayout = itemView.findViewById(R.id.cardBack)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderSingleBird {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.bird_card, parent, false)
        return ViewHolderSingleBird(view)
    }

    override fun onBindViewHolder(holder: ViewHolderSingleBird, position: Int) {
        val dataObject = arrayList!![position]

        holder.name.text = dataObject.name
        holder.age.text = dataObject.age.toString()
        holder.gender.text = dataObject.gender
        holder.img.setImageResource(dataObject.image ?: R.drawable.bird )


        holder.background.setOnClickListener {
            onClickListener.onAddBirdClick(bird = dataObject)
        }

//        holder.deleteCollection.setOnClickListener {
//            onRemoveClickListener.onRemoveCollectionClick(dataObject)
//        }

    }

    override fun getItemCount(): Int {
        return arrayList?.size ?: 0
    }


    class OnAddClickListener(val clickListener: (bird:Bird) -> Unit) {
        fun onAddBirdClick(bird:Bird) = clickListener(bird)
    }

    class OnRemoveClickListener(val clickListener: (bird:Bird) -> Unit) {
        fun onRemoveCollectionClick(bird:Bird) = clickListener(bird)
    }

}

