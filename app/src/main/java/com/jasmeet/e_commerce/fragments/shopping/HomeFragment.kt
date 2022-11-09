package com.jasmeet.e_commerce.fragments.shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.jasmeet.e_commerce.adapter.HomeViewPager
import com.jasmeet.e_commerce.databinding.FragmentHomeBinding
import com.jasmeet.e_commerce.fragments.categories.*

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val categoriesFragments = arrayListOf<Fragment>(
            MainCategoryFragment(),
            ChairFragment(),
            CupBoardFragment(),
            FurnitureFragment(),
            TableFragment(),
            AccessoryFragment(),
            BooksFragment(),
            OthersFragment(),
        )

        val viewPagerAdapter = HomeViewPager(categoriesFragments, childFragmentManager, lifecycle)

        binding.viewPagerHome.adapter = viewPagerAdapter
        TabLayoutMediator(binding.tabLayout, binding.viewPagerHome) { tab, position ->
            when (position) {
                0 -> tab.text = "Main"
                1 -> tab.text = "Chair"
                2 -> tab.text = "CupBoard"
                3 -> tab.text = "Furniture"
                4 -> tab.text = "Table"
                5 -> tab.text = "Accessory"
                6 -> tab.text = "Books"
                7 -> tab.text = "Others"
            }
        }.attach()
    }


}