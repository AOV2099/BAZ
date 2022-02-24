package com.aov2099.baz.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class ViewPagerAdapter(supportFragmentManager: FragmentManager): FragmentStatePagerAdapter( supportFragmentManager,
    BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val fragmentList = ArrayList<Fragment>()
    private val fragmentListTitle = ArrayList<String>()

    override fun getCount(): Int {
        return fragmentList.size
    }

    override fun getItem(position: Int): Fragment {
        return return fragmentList[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return fragmentListTitle[position]
    }

    fun addFragment( fragment:Fragment, title:String){
        fragmentList.add(fragment)
        fragmentListTitle.add(title)

    }
}