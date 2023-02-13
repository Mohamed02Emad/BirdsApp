package com.example.android.birdsdaycounter.presentation.recyclerViews.multibird

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.android.birdsdaycounter.R
import com.example.android.birdsdaycounter.data.models.Bird
import com.example.android.birdsdaycounter.presentation.recyclerViews.recyclerViewAllBirds.AllBirdsAdapter
import kotlinx.coroutines.*

class MultiBirdAdapter(
    private var arrayList: ArrayList<Bird>?,
    private val onClickListener: AllBirdsAdapter.OnAddClickListener,
    private val onLongClickListener: AllBirdsAdapter.OnLongClickListener
) : RecyclerView.Adapter<MultiBirdAdapter.ViewHolderMultiBird>() {

    class ViewHolderMultiBird(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.bird_name)
        val age: TextView = itemView.findViewById(R.id.bird_age)
        val gender: TextView = itemView.findViewById(R.id.bird_gender)
        val img: ImageView = itemView.findViewById(R.id.bird_img)
        val background: CardView = itemView.findViewById(R.id.cardBack)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderMultiBird {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.bird_card, parent, false)
        return ViewHolderMultiBird(view)
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onBindViewHolder(holder: ViewHolderMultiBird, position: Int) {

        //todo : Mohamed Emad
        val dataObject = arrayList!![position]

        holder.name.text = dataObject.name
        holder.age.text = dataObject.age.toString()
        holder.gender.text = dataObject.gender



        GlobalScope.launch(Dispatchers.Main) {
            try {
                holder.img.setImageURI(Uri.parse(dataObject.imgLocation))
                this.cancel()
            } catch (E: Exception) {
                Log.i("Moha", "onBindViewHolder: ${E.message}")
            }
        }


//        if(position ==  arrayList!!.size-1 ){
//            val lp = LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.WRAP_CONTENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT
//            )
//            lp.setMargins(8, 8, 8, 8)
//            holder.background.layoutParams = lp
//        }

        holder.background.setOnClickListener {
            onClickListener.onAddBirdClick(bird = dataObject,position)
        }

//        holder.deleteCollection.setOnClickListener {
//            onRemoveClickListener.onRemoveCollectionClick(dataObject)
//        }

    }

    override fun getItemCount(): Int {
        return arrayList?.size ?: 0
    }


    class OnAddClickListener(val clickListener: (bird: Bird) -> Unit) {
        fun onAddBirdClick(bird: Bird) = clickListener(bird)
    }

    class OnRemoveClickListener(val clickListener: (bird: Bird) -> Unit) {
        fun onRemoveCollectionClick(bird: Bird) = clickListener(bird)
    }

}

