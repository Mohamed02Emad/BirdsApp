package com.example.android.birdsdaycounter.singleBirds.classesMVVM

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.birdsdaycounter.MainActivity
import com.example.android.birdsdaycounter.R
import com.example.android.birdsdaycounter.databinding.FragmentSingleBinding
import com.example.android.birdsdaycounter.globalUse.MyFragmentParentClass
import com.example.android.birdsdaycounter.singleBirds.models.Collection
import com.example.android.birdsdaycounter.singleBirds.recyclerView.CollectionAdapter


class SingleFragment : MyFragmentParentClass()  {

    private lateinit var binding: FragmentSingleBinding

    private val viewModel: SingleBirdViewModel by viewModels()
    private lateinit var adapter: CollectionAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSingleBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRV()
        setOnClickListeners()
    }

    private fun setupRV() {
        val layoutManager = LinearLayoutManager(requireActivity())

        adapter = CollectionAdapter(viewModel.collectionsLiveData.value,CollectionAdapter.OnAddClickListener{collection->
         addNewBirdToCollection(collection)

        },
            CollectionAdapter.OnRemoveClickListener{collection ->
                removeCollection(collection)
            }
        )
        binding.collectionsRv.setAdapter(adapter)
        binding.collectionsRv.setLayoutManager(layoutManager)

    }

    private fun removeCollection(collection: Collection) {
        showToast("collection removed")
        var itemRemovedPosition : Int = viewModel.removeCollection(collection)
        adapter.notifyItemRemoved(itemRemovedPosition)
    }

    private fun addNewBirdToCollection(collection: Collection) {
          viewModel.addFakeBird(collection)
        var pos = viewModel.collectionsLiveData.value!!.indexOf(collection)
        adapter.notifyItemChanged(pos)
    }


    private fun setOnClickListeners() {
        binding.addCollectionButton.setOnClickListener {

            val dialogName: AlertDialog.Builder = AlertDialog.Builder(requireActivity())

            dialogName.setTitle("Collection name")

            val EditTxtName = EditText(requireActivity())

            EditTxtName.apply {
                setTextColor(ContextCompat.getColor(requireActivity(), R.color.black))
                setHintTextColor(ContextCompat.getColor(requireActivity(), R.color.grey))
                hint = "Enter collection name"
            }
            dialogName.setView(EditTxtName)

            dialogName.setPositiveButton(
                "Add",
                DialogInterface.OnClickListener { dialogInterface, i ->
                    val string = EditTxtName.text.toString()
                    if (!string.isEmpty()) {
                        viewModel.addNewCollection(string)
                        resetRV()
                    } else {
                        Toast.makeText(requireActivity(), "Title is required", Toast.LENGTH_SHORT)
                            .show()
                    }
                    dialogInterface.cancel()
                })


            dialogName.setNegativeButton("Cancel",
                DialogInterface.OnClickListener { dialogInterface, i -> dialogInterface.cancel() })
            dialogName.show()
        }



        }

    private fun resetRV() {
        adapter.notifyItemInserted(viewModel.collectionSize()-1)
    }



}