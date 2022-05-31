package com.example.devicespecifications.utils

/**
 * Created by Andronicus Kim on 5/31/22
 */
/*
* Helper function to convert bytes to Megabytes
* */
fun Long.toMB(): String{
    val mb = 1024L * 1024L
    return "${this/mb} MB"
}