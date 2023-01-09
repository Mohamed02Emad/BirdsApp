package com.example.android.birdsdaycounter.singleBirds.recyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.RecycledViewPool
import com.example.android.birdsdaycounter.R
import com.example.android.birdsdaycounter.singleBirds.models.Collection


class CollectionAdapter(
    private var arrayList: ArrayList<Collection>?
) : RecyclerView.Adapter<CollectionAdapter.ViewHolderSingleBird>() {

    private val viewPool = RecycledViewPool()

    class ViewHolderSingleBird(itemView: View ) :
        RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.collectionTitle)
        var recyclerChild: RecyclerView = itemView.findViewById(R.id.child_recycler_view)
        val addBird : ImageView = itemView.findViewById(R.id.add_bird_button)
        val deleteCollection :ImageView = itemView.findViewById(R.id.delete_collection_button)
        init {}
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderSingleBird {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.single_bird_collection_card, parent, false)
        return ViewHolderSingleBird(view)
    }

    override fun onBindViewHolder(holder: ViewHolderSingleBird, position: Int) {
        val dataObject = arrayList!![position]
        holder.title.text = dataObject.collectionName

        val layoutManager = LinearLayoutManager(holder.recyclerChild.getContext(),LinearLayoutManager.HORIZONTAL, false)
        layoutManager.setInitialPrefetchItemCount(dataObject.birdsList!!.size)
        val childAdapter = BirdsAdapter(dataObject.birdsList!!)

        holder.recyclerChild.setLayoutManager(layoutManager)
        holder.recyclerChild.setAdapter(childAdapter)
        holder.recyclerChild.setRecycledViewPool(viewPool)
    }

    override fun getItemCount(): Int {
        return arrayList!!.size
    }


}