package com.example.android.birdsdaycounter.allBirds.classesMVVM

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import androidx.recyclerview.widget.RecyclerView.SmoothScroller
import com.example.android.birdsdaycounter.allBirds.recyclerView.AllBirdsAdapter
import com.example.android.birdsdaycounter.databinding.FragmentAllBirdsBinding
import com.example.android.birdsdaycounter.globalUse.MyFragmentParentClass


class BirdsFragment : MyFragmentParentClass() {

    private lateinit var binding: FragmentAllBirdsBinding
    lateinit var layoutManager : LayoutManager
    private val viewModel: AllBirdsViewModel by viewModels()
    private lateinit var adapter: AllBirdsAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAllBirdsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRV()
        setOnClickListeners()
    }

    private fun setupRV() {
        viewModel.isReadyToShow.observe(viewLifecycleOwner) {
            if (it) {
                layoutManager =
                    GridLayoutManager(requireActivity(), 2, GridLayoutManager.VERTICAL, false)

                adapter = AllBirdsAdapter(viewModel.collectionsLiveData.value,
                    AllBirdsAdapter.OnAddClickListener { collection ->
                        //todo
                    },
                    AllBirdsAdapter.OnRemoveClickListener { collection ->
                        //does nothing
                    }
                )

                binding.collectionsRv.setAdapter(adapter)
                binding.collectionsRv.setLayoutManager(layoutManager)
            }
        }
    }


    private fun setOnClickListeners() {
        binding.addCollectionButton.setOnClickListener {
            viewModel.addNewBird(viewModel.createFakeBuird())
                        resetRV()
        }
        }

    private fun resetRV() {
        var pos = viewModel.birdListSize() - 1
        adapter.notifyItemInserted(pos)
      //  layoutManager.scrollToPosition(pos)

        val smoothScroller: SmoothScroller = object : LinearSmoothScroller(context) {
            override fun getVerticalSnapPreference(): Int {
                return SNAP_TO_START
            }
        }
        smoothScroller.setTargetPosition(pos)
        layoutManager.startSmoothScroll(smoothScroller)
    }



}