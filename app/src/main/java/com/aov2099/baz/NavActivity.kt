package com.aov2099.baz

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.aov2099.baz.AddressService.TAG
import com.aov2099.baz.adapters.ViewPagerAdapter
import com.aov2099.baz.fragments.ImageFragment
import com.aov2099.baz.fragments.MapsFragment
import com.aov2099.baz.fragments.SearchFragment
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_nav.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

val JOB_INFO_ID = 1

class NavActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nav)

        setUpTabs()
        sendFirstAddress()

    }

    private fun sendFirstAddress() {
        val writter = FirestoreCalls()
        writter.pushAddressData()
    }


    private fun setUpTabs(){
        //pushAddressData()
        val adapter = ViewPagerAdapter(supportFragmentManager)
        //adapter.addFragment( NewsFragment(), "Inicio")
        adapter.addFragment( SearchFragment(), "Inicio")
        adapter.addFragment( ImageFragment(), "Fotos")
        adapter.addFragment( MapsFragment(), "Mapa")

        viewPager.adapter = adapter

        tabs.setupWithViewPager(viewPager)

        tabs.getTabAt(0)!!.setIcon(R.drawable.ic_baseline_home_24)
        //tabs.getTabAt(1)!!.setIcon(R.drawable.ic_baseline_search_24)
        tabs.getTabAt(1)!!.setIcon(R.drawable.ic_baseline_camera_24)
        tabs.getTabAt(2)!!.setIcon(R.drawable.ic_baseline_map_24)
    }

    fun scheduleJob(job: View){
        val componentName  =  ComponentName( this, AddressService::class.java )
        val info = JobInfo.Builder( JOB_INFO_ID, componentName )
            .setRequiresCharging(false)
            .setPersisted(true)
            .setPeriodic( 30 * 60 * 1000) // 30 minutes
            .build()

        val scheduler: JobScheduler = getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
        val result:Int = scheduler.schedule( info )

        if(result == JobScheduler.RESULT_SUCCESS){
            Log.d("NAV ACTIVITY","Job scheduled")
        }else{
            Log.d("NAV ACTIVITY","Job scheduling failed")
        }
    }

    fun cancelJob(v: View){
        val scheduler: JobScheduler = getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
        scheduler.cancel(JOB_INFO_ID)
        Log.d("NAV ACTIVITY","Job CANCELED")
    }






}