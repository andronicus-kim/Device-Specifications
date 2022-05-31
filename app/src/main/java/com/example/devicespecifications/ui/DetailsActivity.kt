package com.example.devicespecifications.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.devicespecifications.R
import com.example.devicespecifications.data.ComponentDetail
import com.example.devicespecifications.databinding.ActivityDetailsBinding
import com.example.devicespecifications.utils.Constants

class DetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // show back icon
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        // get component name
        val component = intent.getStringExtra(Constants.COMPONENT_NAME) ?: ""
        supportActionBar?.title = component
        // get list of component details and render
        val data = intent.getParcelableArrayListExtra<ComponentDetail>(Constants.COMPONENT_DETAILS)
        val adapter = ComponentDetailsAdapter(data ?: emptyList())
        binding.rvComponentDetails.adapter = adapter
    }
}