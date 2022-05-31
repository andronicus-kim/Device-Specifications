package com.example.devicespecifications.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.devicespecifications.data.ComponentDetail
import com.example.devicespecifications.databinding.ActivityMainBinding
import com.example.devicespecifications.utils.Constants
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
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
    }
    private fun writeToFile(){
        val data = StringBuilder()
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
            putExtra("title", name.lowercase().replaceFirstChar { it.uppercase() })
            putParcelableArrayListExtra("details",filteredList)
        }
        startActivity(intent)
    }
    override fun onDestroy() {
        job.cancel() // cancel the Job
        super.onDestroy()
    }
}