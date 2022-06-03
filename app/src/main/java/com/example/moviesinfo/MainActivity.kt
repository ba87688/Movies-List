package com.example.moviesinfo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.moviesinfo.databinding.ActivityMainBinding
import com.example.moviesinfo.ui.HomePageFrag
import com.example.moviesinfo.ui.SearchPageFrag
import java.lang.IllegalArgumentException



class MainActivity : AppCompatActivity() {

    //viewbindind
    private lateinit var binding: ActivityMainBinding

    //declare the different fragments
    private lateinit var homePageFrag: HomePageFrag
    private lateinit var searchPageFrag: SearchPageFrag


    //create a array that will hold the fragments
    private val fragment: Array<Fragment>
        get() =
            arrayOf(
                homePageFrag,
                searchPageFrag,
            )

    private var indexSelected = 0

    private val selectedFragment get() = fragment[indexSelected]





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //assign binding variable
        binding = ActivityMainBinding.inflate(layoutInflater)


        setContentView(binding.root)

//        check if there has been a configuration change.
//        if it is null, there has been no config change

        if (savedInstanceState == null) {

//        initialize the lateinit fragments
            homePageFrag = HomePageFrag()
            searchPageFrag = SearchPageFrag()


            //before we can attach and detach, we have to add the fragments to container.
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, homePageFrag, HOME_PAGE_FRAGMENT)
                .add(R.id.fragment_container, searchPageFrag, SEARCH_PAGE_FRAGMENT)
                .commit()
        }
        else{
//            in case there was a configuration change...
//            fragments are still in memory but the fragment manager is responsible for finding them. restores them
            homePageFrag =
                supportFragmentManager.findFragmentByTag(HOME_PAGE_FRAGMENT) as HomePageFrag
            searchPageFrag =
                supportFragmentManager.findFragmentByTag(SEARCH_PAGE_FRAGMENT) as SearchPageFrag

            //retrieve the selected index which was saved when there is a config change. set default to first fragment,0
            indexSelected = savedInstanceState.getInt(SELECTED_INDEX_VALUE,0)
        }

        //this will select first view because the index is set to 0
        selectedFragment(selectedFragment)





        //NAVIGATION ITEM VIEW
        binding.buttonNav.setOnNavigationItemSelectedListener {itemSelected ->
            var fragment = when (itemSelected.itemId){
                R.id.home -> homePageFrag
                R.id.search -> searchPageFrag
                else -> throw IllegalArgumentException("error")
            }
            selectedFragment(fragment)
            true
        }




    }


    //function responsbile for inserting selected fragment on screen
    private fun selectedFragment(selectedFragment: Fragment) {
        var transaction = supportFragmentManager.beginTransaction()

        //go through the array and see if they match with selectedFragment.
//    if they match, put it on screen. if not, detach fragment from screen
        fragment.forEachIndexed { index, fragment ->
            if (selectedFragment == fragment) {
                transaction = transaction.attach(fragment)
                indexSelected = index
            } else {
                transaction = transaction.detach(fragment)
            }
        }
        transaction.commit()



    }



    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

//        save index when there is a configuration change
        outState.putInt(SELECTED_INDEX_VALUE,indexSelected)
    }
}


private const val HOME_PAGE_FRAGMENT = "HOME_PAGE_FRAG"
private const val SEARCH_PAGE_FRAGMENT = "SEARCH_PAGE_FRAG"
private const val SELECTED_INDEX_VALUE = "SELECTED_INDEX_VALUE"

