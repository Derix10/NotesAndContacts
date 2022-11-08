package com.example.experiments.ui.fragment.board

import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.viewpager2.widget.ViewPager2
import com.example.experiments.R
import com.example.experiments.base.BaseFragment
import com.example.experiments.databinding.FragmentOnBoardBinding
import com.example.experiments.ui.App
import com.google.android.material.tabs.TabLayoutMediator

class OnBoardFragment : BaseFragment<FragmentOnBoardBinding>(FragmentOnBoardBinding::inflate){

    private lateinit var adapter: BoardAdapter
    private var currentPage = 0
    private lateinit var controller: NavController
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
                NavHostFragment

        controller = navHostFragment.navController
        setupUI()
    }
    override fun setupUI() {
        adapter = BoardAdapter()
        binding.pagerOnBoard.adapter = adapter
        TabLayoutMediator(binding.mDots, binding.pagerOnBoard){ _, _ -> }.attach()

        binding.btnNext.setOnClickListener{
            App.prefs.saveBoardState()
            controller.navigateUp()



            if (currentPage < 2){
                currentPage++
                when(currentPage){
                    0 -> binding.pagerOnBoard.currentItem = 0
                    1 -> binding.pagerOnBoard.currentItem = 1
                    2 -> binding.pagerOnBoard.currentItem = 2
                }
            }
        }

        binding.pagerOnBoard.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {

                if (position == 0) currentPage = 0
                if (position == 1) currentPage = 2
                if (position == 2) {
                    binding.btnNext.visibility = View.VISIBLE
                }else{
                    binding.btnNext.visibility = View.GONE
                }
                super.onPageSelected(position)
            }
        })

    }
}