package com.example.loginchatudemy.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.viewpager.widget.ViewPager
import com.example.loginchatudemy.R
import com.example.loginchatudemy.adapter.PagerAdapter
import com.example.loginchatudemy.fragments.ChatFragment
import com.example.loginchatudemy.fragments.InfoFragment
import com.example.loginchatudemy.fragments.RatesFragment
import com.example.loginchatudemy.goActivity
import com.example.loginchatudemy.toast
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    //INICIO VARIABLES
    private var prevButtonSelected: MenuItem? = null
    private val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    //FIN VARIABLES

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Toolbar
        setSupportActionBar(findViewById(R.id.toolbar))
        //INICIO LLAMADO DE FUNCIONES
        setUpViewPager(getPagerAdapter())
        setUpBottomNavigationBar()
        //FIN LLAMADO DE FUNCIONES


    }
    //INICIO METODOS PARA EL VIEWPAGER ADAPTER
    private fun getPagerAdapter(): PagerAdapter {
        val adapter = PagerAdapter(supportFragmentManager)
        adapter.addAFragment(InfoFragment())
        adapter.addAFragment(RatesFragment())
        adapter.addAFragment(ChatFragment())
        return adapter
    }
    private fun setUpViewPager(adapter: PagerAdapter){
        ViewPager.adapter = adapter
        ViewPager.offscreenPageLimit = adapter.count//RxBus para y el contador de totalmessages, onDestroy
        ViewPager.addOnPageChangeListener(object: ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {
                if (prevButtonSelected == null){
                    bottomNavigationView.menu.getItem(0).isChecked = false
                } else {
                    prevButtonSelected!!.isChecked = false
                }
                bottomNavigationView.menu.getItem(0).isChecked = true
                prevButtonSelected = bottomNavigationView.menu.getItem(position)
            }
        })
    }
    private fun setUpBottomNavigationBar(){
        bottomNavigationView.setOnNavigationItemSelectedListener{ item ->
            when(item.itemId) {
                R.id.bottom_nav_info -> { ViewPager.currentItem = 0; true }
                R.id.bottom_nav_rates -> { ViewPager.currentItem = 1; true }
                R.id.bottom_nav_chat -> { ViewPager.currentItem = 2; true }
                else -> false
            }
        }
    }

    //INICIO METODOS PARA LOG OUT
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.general_option_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_log_out -> {
                FirebaseAuth.getInstance().signOut()
                goActivity<LoginActivity> {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
    //FIN METODOS PARA LOG OUT
    //FIN METODOS PARA EL VIEWPAGER ADAPTER
}
