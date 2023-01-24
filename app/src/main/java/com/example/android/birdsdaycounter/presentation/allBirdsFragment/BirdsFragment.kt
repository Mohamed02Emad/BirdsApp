package com.example.android.birdsdaycounter.presentation.allBirdsFragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import androidx.recyclerview.widget.RecyclerView.SmoothScroller
import com.example.android.birdsdaycounter.presentation.AddBirdActivity
import com.example.android.birdsdaycounter.presentation.recyclerView.AllBirdsAdapter
import com.example.android.birdsdaycounter.databinding.FragmentAllBirdsBinding
import com.example.android.birdsdaycounter.globalUse.MyFragmentParentClass


class BirdsFragment : MyFragmentParentClass() {

    private lateinit var binding: FragmentAllBirdsBinding
    lateinit var layoutManager : LayoutManager
    private val viewModel: AllBirdsViewModel by viewModels()
    private lateinit var adapter: AllBirdsAdapter
    var oldSize = 0


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
            oldSize = viewModel.birdListSize()
        }

    }


    private fun setOnClickListeners() {
        binding.addCollectionButton.setOnClickListener {
            var i = Intent(requireActivity(), AddBirdActivity::class.java)
            requireActivity().startActivity(i)
        }
    }

    private fun resetRV() {
        var pos = viewModel.birdListSize()
        try {
            if (pos != oldSize && pos != 0) {
                adapter.notifyItemInserted(pos - 1)
                smoothScrollToPosition(pos)
            }
        }catch (E:Exception){}

    }

    private fun smoothScrollToPosition(pos: Int) {
        //  layoutManager.scrollToPosition(pos)
        val smoothScroller: SmoothScroller = object : LinearSmoothScroller(context) {
            override fun getVerticalSnapPreference(): Int {
                return SNAP_TO_START
            }
        }
        smoothScroller.setTargetPosition(pos)
        layoutManager.startSmoothScroll(smoothScroller)
    }

    override fun onResume() {
        super.onResume()
        viewModel.resetArrayList()
        if (viewModel.isReadyToShow.value == true) {
            resetRV()
        }
    }

}