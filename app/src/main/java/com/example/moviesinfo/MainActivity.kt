package com.example.moviesinfo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.moviesinfo.databinding.ActivityMainBinding
import com.example.moviesinfo.ui.HomePageFrag
import com.example.moviesinfo.ui.SearchPageFrag

class MainActivity : AppCompatActivity() {


    //viewbinding
    private lateinit var binding: ActivityMainBinding

    //fragments
    private lateinit var homeFragment: HomePageFrag
    private lateinit var searchFragment: SearchPageFrag

    //making an array of the home page fragment and search page fragment
    private val fragments: Array<Fragment>
        get() = arrayOf(homeFragment, searchFragment)

    //index of fragment
    private var indexSelected = 0
    private val selectedFragment get() = fragments[indexSelected]


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        //setting up viewbinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)




        if (savedInstanceState == null) {

            //initialize the fragments
            homeFragment = HomePageFrag()
            searchFragment = SearchPageFrag()

            val tran =  supportFragmentManager.beginTransaction()



            tran.add(R.id.fragmentContainerView, homeFragment, HOME_PAGE_FRAGMENT)
                .add(R.id.fragmentContainerView, searchFragment, SEARCH_PAGE_FRAGMENT)

            tran.commit()
        }
        else{
            homeFragment = supportFragmentManager.findFragmentByTag(HOME_PAGE_FRAGMENT) as HomePageFrag
            searchFragment = supportFragmentManager.findFragmentByTag(SEARCH_PAGE_FRAGMENT) as SearchPageFrag

            indexSelected = savedInstanceState.getInt(SELECTED_INDEX, 0)
        }

//        selectFragment(selectedFragment)



//        NAVIGATION MENU ITEMS
        binding.buttonNav.setOnNavigationItemSelectedListener { itemSelected ->
            var frag = when(itemSelected.itemId){
                R.id.home -> homeFragment
                R.id.search -> searchFragment
                else -> throw IllegalArgumentException("error")
            }
            selectFragment(frag)
            true

        }

    }


    private fun selectFragment(selectedFrag:Fragment){
        var transaction = supportFragmentManager.beginTransaction()

        //attach and detach the right fragments.
        fragments.forEachIndexed{ index, fragment ->
            Log.i(TAG, "selectFragment: $index ")
            if (selectedFrag == fragment){
                transaction = transaction.attach(fragment)
                indexSelected=index

            }
            //remove the fragments that don't match
            else{
                transaction= transaction.detach(fragment)
            }
            //commit the changes
            transaction.commit()

        }


    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(SELECTED_INDEX, indexSelected)
    }

}

const val HOME_PAGE_FRAGMENT = "HOME FRAGMENT"
const val SEARCH_PAGE_FRAGMENT = "SEARCH FRAGMENT"
const val TAG = "MAIN ACTIVITY"
const val SELECTED_INDEX = "Selected Index"