package com.example.android.birdsdaycounter.presentation.allBirdsFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import androidx.recyclerview.widget.RecyclerView.SmoothScroller
import com.example.android.birdsdaycounter.data.models.Bird
import com.example.android.birdsdaycounter.databinding.FragmentAllBirdsBinding
import com.example.android.birdsdaycounter.globalUse.MyFragmentParentClass
import com.example.android.birdsdaycounter.presentation.AddBirdDialog
import com.example.android.birdsdaycounter.presentation.recyclerViews.recyclerViewAllBirds.AllBirdsAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


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
        binding = FragmentAllBirdsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRV()
        setObservers()
        setOnClickListeners()
    }

    private fun setObservers() {
        viewModel.isSelectToDelete.observe(viewLifecycleOwner){
            if (it){
                binding.deleteSelectedBar.visibility=View.VISIBLE
            }else{
                binding.deleteSelectedBar.visibility=View.GONE
            }
        }
    }

    private fun setupRV() {
        viewModel.isReadyToShow.observe(viewLifecycleOwner) {
            if (it) {
                layoutManager =
                    GridLayoutManager(requireActivity(), 2, GridLayoutManager.VERTICAL, false)

                adapter = AllBirdsAdapter(viewModel.BirdsLiveData.value,
                    AllBirdsAdapter.OnAddClickListener { bird:Bird , position:Int ->
                        if (viewModel.isSelectToDelete.value==false){
                        birdClicked(bird)
                        }else{
                           val markBird = viewModel.checkToInsertToSelectedToBeDeleted(bird)
                            bird.isSelected=markBird
                            adapter.notifyItemChanged(position)
                            binding.numberSelected.text=viewModel.listToDelete.value!!.size.toString()
                        }
                    },
                    AllBirdsAdapter.OnLongClickListener{bird:Bird , position :Int ->
                        viewModel.isSelectToDelete.value=true
                        val markBird = viewModel.checkToInsertToSelectedToBeDeleted(bird)
                        bird.isSelected=markBird
                        adapter.notifyItemChanged(position)
                        binding.numberSelected.text=viewModel.listToDelete.value!!.size.toString()
                        false
                    }
                )

                binding.collectionsRv.adapter = adapter
                binding.collectionsRv.layoutManager = layoutManager

            }
            oldSize = viewModel.birdListSize()
        }

    }

    private fun birdClicked(bird: Bird) {
       findNavController().navigate(AllBirdsFragmentDirections.actionAllBirdsFragmentToBirdFragment(bird))
    }


    private fun setOnClickListeners() {

        viewModel.newBirdWasAdded.observe(viewLifecycleOwner) {
            lifecycleScope.launch(Dispatchers.Main) {
                if (it) {
                    try {
                        if (viewModel.isReadyToShow.value == true) {
                            resetRV()
                        }
                    } catch (E: Exception) {
                    }
                    viewModel.newBirdWasAdded.value = false
                }
            }
        }
        binding.addCollectionButton.setOnClickListener {
            val addBirdDialog = AddBirdDialog(viewModel)
            addBirdDialog.show(childFragmentManager, "TAG")

        }

        binding.cancelDeleteSelected.setOnClickListener {
            viewModel.clearSelectedToBeDeleted()
            adapter.notifyDataSetChanged()
        }
        binding.cancelSelectedTXT.setOnClickListener {
            viewModel.clearSelectedToBeDeleted()
            adapter.notifyDataSetChanged()
        }
        binding.deleteSelectedBtn.setOnClickListener {
            viewModel.deleteSelected()
            adapter.notifyDataSetChanged()
        }
        binding.deleteSelectedTXT.setOnClickListener {
            viewModel.deleteSelected()
            adapter.notifyDataSetChanged()
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

        val smoothScroller: SmoothScroller = object : LinearSmoothScroller(context) {
            override fun getVerticalSnapPreference(): Int {
                return SNAP_TO_START
            }
        }
        smoothScroller.targetPosition = pos
        layoutManager.startSmoothScroll(smoothScroller)


    }

}