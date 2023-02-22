package com.example.android.birdsdaycounter.presentation.recyclerViews.recyclerViewAllBirds

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.android.birdsdaycounter.R
import com.example.android.birdsdaycounter.data.models.Bird
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File


class AllBirdsAdapter(
    private var arrayList: ArrayList<Bird>?,
    private val onClickListener: OnAddClickListener,
    private val onLongClickListener: OnLongClickListener
) : RecyclerView.Adapter<AllBirdsAdapter.ViewHolderSingleBird>() {

    class ViewHolderSingleBird(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.bird_name)
        val age: TextView = itemView.findViewById(R.id.bird_age)
        val gender: TextView = itemView.findViewById(R.id.bird_gender)
        val img: ImageView = itemView.findViewById(R.id.bird_img)
        val background: CardView = itemView.findViewById(R.id.cardBack)
        val checked: ImageView = itemView.findViewById(R.id.checked)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderSingleBird {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.bird_card, parent, false)
        return ViewHolderSingleBird(view)
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onBindViewHolder(holder: ViewHolderSingleBird, position: Int) {
        val dataObject = arrayList!![position]

        val name = if (dataObject.name.isNullOrEmpty()) dataObject.id else dataObject.name
        holder.name.text = name.toString()
        holder.age.text = dataObject.age.toString()
        holder.gender.text = dataObject.gender

        if (dataObject.isSelected) holder.checked.visibility = View.VISIBLE
        else holder.checked.visibility = View.INVISIBLE

        GlobalScope.launch(Dispatchers.Main) {
            holder.img.loadUrl(Uri.parse(dataObject.imgLocation))
        }


        holder.background.setOnClickListener {
            onClickListener.onAddBirdClick(bird = dataObject, position)
        }

        holder.background.setOnLongClickListener {
            onLongClickListener.onLongCollectionClick(dataObject, position)
        }

    }

    override fun getItemCount(): Int {
        return arrayList?.size ?: 0
    }


    class OnAddClickListener(val clickListener: (bird: Bird, position: Int) -> Unit) {
        fun onAddBirdClick(bird: Bird, position: Int) = clickListener(bird, position)
    }

    class OnLongClickListener(val clickListener: (bird: Bird, position: Int) -> Boolean) {
        fun onLongCollectionClick(bird: Bird, position: Int) = clickListener(bird, position)
    }

    private fun ImageView.loadUrl(uri: Uri) {
        Glide.with(this.context)
            .load(File(uri.path!!)) // Uri of the picture
            .centerCrop()
            .into(this)
    }

}

