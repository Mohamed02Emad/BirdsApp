package com.example.android.birdsdaycounter.presentation.allBirdsFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.android.birdsdaycounter.R
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
        setObservers()
        setOnClickListeners()
        setupRV()
    }

    private fun setObservers() {
        viewModel.newBirdWasAdded.observe(viewLifecycleOwner) {
            if (it) {
                lifecycleScope.launch {
                    viewModel.getDataFromRoom()
                    resetRvAfterBirdInserted()
                    viewModel.newBirdWasAdded.value = false
                }
            }
        }

        viewModel.isSelectToDelete.observe(viewLifecycleOwner) {
            if (it) {
                binding.addCollectionButton.setImageResource(R.drawable.ic_baseline_delete_forever_24)
                binding.cancelDeleteSelected.visibility = View.VISIBLE
                binding.numberSelected.visibility = View.VISIBLE
            } else {
                binding.addCollectionButton.setImageResource(R.drawable.add_icon)
                binding.cancelDeleteSelected.visibility = View.GONE
                binding.numberSelected.visibility = View.GONE
            }
        }
    }

    private fun resetRvAfterBirdInserted() {
        val size = viewModel.birdListSize()
        if (size > oldSize) {
            adapter.notifyItemInserted(size - 1)
            smoothScrollToPosition(size - 1)
        }
    }

    private fun setupRV() {
        viewModel.isReadyToShow.observe(viewLifecycleOwner) {
            if (it) {
                layoutManager =
                    GridLayoutManager(requireActivity(), 2, GridLayoutManager.VERTICAL, false)
                adapter = AllBirdsAdapter(viewModel.birdsLiveData.value,
                    AllBirdsAdapter.OnAddClickListener { bird: Bird, position: Int ->
                        if (viewModel.isSelectToDelete.value == false) {
                            birdClicked(bird)
                        } else {
                            selectBirdLogic(bird, position)
                            binding.numberSelected.text =
                                viewModel.listToDelete.value!!.size.toString()
                        }
                    },
                    AllBirdsAdapter.OnLongClickListener { bird: Bird, position: Int ->
                        viewModel.isSelectToDelete.value = true
                        selectBirdLogic(bird, position)
                        binding.numberSelected.text = viewModel.listToDelete.value!!.size.toString()
                        false
                    }
                )
                binding.collectionsRv.adapter = adapter
                binding.collectionsRv.layoutManager = layoutManager
            }
        }
    }

    private fun selectBirdLogic(bird: Bird, position: Int) {
        // false deSelect ---- true select
        val markBird = viewModel.checkIfBirdIsSelected(bird,position)
        viewModel.markTheBird(markBird, position)
        adapter.notifyItemChanged(position)
    }

    private fun birdClicked(bird: Bird) {
        if (viewModel.isReadyToShow.value == true)
            findNavController().navigate(
                AllBirdsFragmentDirections.actionAllBirdsFragmentToBirdFragment(
                    bird
                )
            )
    }

    private fun setOnClickListeners() {
        binding.addCollectionButton.setOnClickListener {
            if (viewModel.isSelectToDelete.value == true) {
                lifecycleScope.launch(Dispatchers.IO) {
                    viewModel.deleteSelected()
                    adapter.notifyDataSetChanged()
                }
            } else {
                oldSize = viewModel.birdListSize()
                val addBirdDialog = AddBirdDialog(viewModel)
                addBirdDialog.show(childFragmentManager, "TAG")
            }
        }

        binding.cancelDeleteSelected.setOnClickListener {
            lifecycleScope.launch {
                viewModel.clearSelectedToBeDeleted()
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun smoothScrollToPosition(pos: Int) {
        binding.collectionsRv.smoothScrollToPosition(pos)
    }

}