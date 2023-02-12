package com.example.android.birdsdaycounter.presentation.allBirdsFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import androidx.recyclerview.widget.RecyclerView.SmoothScroller
import com.example.android.birdsdaycounter.data.models.Bird
import com.example.android.birdsdaycounter.databinding.FragmentAllBirdsBinding
import com.example.android.birdsdaycounter.globalUse.MyApp
import com.example.android.birdsdaycounter.globalUse.MyFragmentParentClass
import com.example.android.birdsdaycounter.presentation.AddBirdDialog
import com.example.android.birdsdaycounter.presentation.birdFragment.BirdFragment
import com.example.android.birdsdaycounter.presentation.recyclerViews.recyclerViewAllBirds.AllBirdsAdapter


class AllBirdsFragment : MyFragmentParentClass() {

    private lateinit var binding: FragmentAllBirdsBinding
    private lateinit var layoutManager: LayoutManager
    private val viewModel: AllBirdsViewModel by viewModels()
    private lateinit var adapter: AllBirdsAdapter
    private var oldSize = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        showToast("jhvjlvkb")
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
                    AllBirdsAdapter.OnAddClickListener { bird ->
                        birdClicked(bird)
                    },
                    AllBirdsAdapter.OnRemoveClickListener { bird ->
                        //does nothing
                    }
                )

                binding.collectionsRv.adapter = adapter
                binding.collectionsRv.layoutManager = layoutManager

            }
            oldSize = viewModel.birdListSize()
        }

    }

    private fun birdClicked(bird: Bird) {
        setFragment(this,BirdFragment.newInstance(bird))
        hideBottomBave(true)
    }


    private fun setOnClickListeners() {

        viewModel.newBirdWasAdded.observe(viewLifecycleOwner) {
            if (it) {
                try {
                    viewModel.resetArrayList()
                    if (viewModel.isReadyToShow.value == true) {
                        resetRV()
                    }
                } catch (E: Exception) {
                }
                viewModel.newBirdWasAdded.value = false
            }
        }

        binding.addCollectionButton.setOnClickListener {
            val addBirdDialog = AddBirdDialog(viewModel)
            addBirdDialog.show(childFragmentManager, "TAG")

        }
    }

    private fun resetRV() {
        try {
            val pos = viewModel.birdListSize()
            if (pos != oldSize && pos != 0) {
                adapter.notifyItemInserted(pos - 1)
                smoothScrollToPosition(pos)
            }
        } catch (E: Exception) {
            adapter.notifyDataSetChanged()
        }
    }

    private fun smoothScrollToPosition(pos: Int) {
        //  layoutManager.scrollToPosition(pos)
        val smoothScroller: SmoothScroller = object : LinearSmoothScroller(context) {
            override fun getVerticalSnapPreference(): Int {
                return SNAP_TO_START
            }
        }
        smoothScroller.targetPosition = pos
        layoutManager.startSmoothScroll(smoothScroller)

        //binding.collectionsRv.smoothScrollToPosition()
    }


    companion object {
        fun newInstance(): AllBirdsFragment {
            return AllBirdsFragment()
        }
    }

    override fun onResume() {
        hideBottomBave(false)
        if (viewModel.firstTimeOpen.value==true){
            viewModel.firstTimeOpen.value=false
        }else {
            showFragment(this)
        }
        super.onResume()
    }

}