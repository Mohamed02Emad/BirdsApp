package com.example.android.birdsdaycounter.singleBirds.recyclerView

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.RecycledViewPool
import com.example.android.birdsdaycounter.R
import com.example.android.birdsdaycounter.globalUse.PublicVariables
import com.example.android.birdsdaycounter.globalUse.birdStuff.BirdInformation
import com.example.android.birdsdaycounter.singleBirds.models.Bird
import com.example.android.birdsdaycounter.singleBirds.models.Collection


class CollectionAdapter(
    private var arrayList: ArrayList<Collection>?,
    private val onClickListener:OnAddClickListener,
    private val onRemoveClickListener: OnRemoveClickListener
) : RecyclerView.Adapter<CollectionAdapter.ViewHolderSingleBird>() {

    private val viewPool = RecycledViewPool()
    lateinit var childAdapter : BirdsAdapter

    class ViewHolderSingleBird(itemView: View ) :
        RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.collectionTitle)
        var recyclerChild: RecyclerView = itemView.findViewById(R.id.child_recycler_view)
        val addBird : ImageView = itemView.findViewById(R.id.add_bird_button)
        val deleteCollection :ImageView = itemView.findViewById(R.id.delete_collection_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderSingleBird {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.single_bird_collection_card, parent, false)
        return ViewHolderSingleBird(view)
    }

    override fun onBindViewHolder(holder: ViewHolderSingleBird, position: Int) {
        val dataObject = arrayList!![position]
        holder.title.text = dataObject.collectionName

        val layoutManager = LinearLayoutManager(
            holder.recyclerChild.getContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        layoutManager.setInitialPrefetchItemCount(dataObject.birdsList!!.size)
         childAdapter = BirdsAdapter(dataObject.birdsList!!, BirdsAdapter.OnClickListener { bird ->
                onBirdClick(bird)
            })

        holder.recyclerChild.setLayoutManager(layoutManager)
        holder.recyclerChild.setAdapter(childAdapter)
        holder.recyclerChild.setRecycledViewPool(viewPool)

        holder.addBird.setOnClickListener {
            onClickListener.onAddBirdClick(collection = dataObject)
            //Toast.makeText(PublicVariables.getContext(),"tt",Toast.LENGTH_SHORT).show()
           //  childAdapter.notifyItemInserted(dataObject.birdsList!!.size-1)
        }

        holder.deleteCollection.setOnClickListener {
            onRemoveClickListener.onRemoveCollectionClick(dataObject)
        }

    }

    private fun onBirdClick(bird: Bird) {
       var i :Intent= Intent(PublicVariables.getContext(),BirdInformation::class.java)
        i.apply {
            putExtra("name",bird.name)
            putExtra("age",bird.age)
            putExtra("id",bird.id)
            putExtra("color",bird.color)
            putExtra("gender",bird.gender)
            putExtra("countDays",bird.countDays)
            putExtra("daysFromStartDate",bird.daysFromStartDate)
            putExtra("endDate",bird.endDate)
            putExtra("image",bird.image)
            putExtra("note",bird.note)
            putExtra("startDate",bird.startDate)
        }
        PublicVariables.getContext().startActivity(i)
    }

    override fun getItemCount(): Int {
        return arrayList!!.size
    }


    class OnAddClickListener(val clickListener: (collection: Collection) -> Unit) {
        fun onAddBirdClick(collection: Collection) = clickListener(collection)
    }

    class OnRemoveClickListener(val clickListener: (collection: Collection) -> Unit) {
        fun onRemoveCollectionClick(collection: Collection) = clickListener(collection)
    }

}

