package com.example.devicespecifications.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.devicespecifications.R
import com.example.devicespecifications.data.Component
import com.example.devicespecifications.data.ComponentDetail
import com.example.devicespecifications.data.Components
import com.example.devicespecifications.databinding.ActivityMainBinding
import com.example.devicespecifications.utils.Constants
import com.example.devicespecifications.utils.DeviceInfo
import com.example.devicespecifications.utils.HelperFunctions
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope, ComponentAdapter.OnComponentClick {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private lateinit var job: Job

    private lateinit var binding: ActivityMainBinding
    private lateinit var componentsDetails: ArrayList<ComponentDetail>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        job = Job() // create the Job
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // handle scan button click
        binding.btnScan.setOnClickListener {
            binding.pbLoadingIndicator.visibility = View.VISIBLE
            scanDeviceShowAndSaveDetails()
        }
    }
    private fun setupAdapter(){
        // add list of components/hardware to adapter
        val adapter = ComponentAdapter(
            listOf(
                Component(R.drawable.ic_system, Components.SYSTEM.name),
                Component(R.drawable.ic_device,Components.DEVICE.name),
                Component(R.drawable.ic_processor,Components.PROCESSOR.name),
                Component(R.drawable.ic_memory,Components.STORAGE.name),
                Component(R.drawable.ic_battery,Components.BATTERY.name),
                Component(R.drawable.ic_network,Components.NETWORK.name),
                Component(R.drawable.ic_camera,Components.CAMERA.name),
            ),this)
        binding.rvComponents.adapter = adapter
    }

    private fun scanDeviceShowAndSaveDetails(){
        launch {
            // Scan device details in a background thread to prevent freezing the UI
            componentsDetails = withContext(Dispatchers.IO) {
                HelperFunctions.scanDevice(DeviceInfo(this@MainActivity))
            }
            // set up adapter to render components
            setupAdapter()
            writeToFile()
            binding.tvEmptyList.visibility = View.GONE
            binding.pbLoadingIndicator.visibility = View.GONE
            binding.tvComponentsHeader.visibility = View.VISIBLE
            binding.rvComponents.visibility = View.VISIBLE
        }
    }
    private fun writeToFile(){
        /*
            * Create and Write device specifications into a file(device_specs.txt)
            * */
        val data = StringBuilder()
        // Build a string of all component details
        componentsDetails.forEach {
            data.append(it.title)
            data.append("\n")
            data.append(it.subtitle)
            data.append("\n\n")
        }
        val path = applicationContext.filesDir
        val outPutStream = FileOutputStream(File(path,Constants.FILE_NAME))
        outPutStream.write(data.toString().toByteArray())
        outPutStream.close()
        Snackbar.make(binding.root,"${Constants.FILE_NAME} saved successfully!", Snackbar.LENGTH_LONG).show()
    }
    override fun onClick(name: String) {
        //filter component details
        val filteredList = arrayListOf<ComponentDetail>()
        componentsDetails.forEach { if (it.component.contentEquals(name)) { filteredList.add(it) } }
        val intent = Intent(this,DetailsActivity::class.java).apply {
            putExtra(Constants.COMPONENT_NAME, name.lowercase().replaceFirstChar { it.uppercase() })
            putParcelableArrayListExtra(Constants.COMPONENT_DETAILS,filteredList)
        }
        startActivity(intent)
    }
    override fun onDestroy() {
        job.cancel() // cancel the Job
        super.onDestroy()
    }
}