package com.example.devicespecifications.utils

import android.os.Build
import android.text.TextUtils
import android.util.Log
import java.io.File
import java.util.*
import java.util.regex.Pattern

/**
 * Created by Andronicus Kim on 5/31/22
 */
object CpuUtil {

    /**
     * Method that returns the abi supported on runtime cpu, approach adapted from termux api;
     *
     *
     * Note that we cannot use System.getProperty("os.arch") since that may give e.g. "aarch64"
     * while a 64-bit runtime may not be installed (like on the Samsung Galaxy S5 Neo).
     * Instead we search through the supported abi:s on the device, see:
     * http://developer.android.com/ndk/guides/abis.html
     * Note that we search for abi:s in preferred order (the ordering of the
     * Build.SUPPORTED_ABIS list) to avoid e.g. installing arm on an x86 system where arm
     * emulation is available.
     *
     * @return cpuAbi;
     */
    fun cpuAbi(): String {
        for (androidArch in Build.SUPPORTED_ABIS) {
            when (androidArch) {
                "arm64-v8a" -> return "aarch64"
                "armeabi-v7a" -> return "arm"
                "x86_64" -> return "x86_64"
                "x86" -> return "i686"
            }
        }
        throw RuntimeException(
            "Unable to determine arch from Build.SUPPORTED_ABIS =  " +
                    Arrays.toString(Build.SUPPORTED_ABIS)
        )
    }
    /**
     * Parses the output of the /proc/cpuinfo file and maps it. Search for specifics
     * i.e getCpuinfoMap().get('hardware')
     *
     * @return Map
     */
    fun cpuInfo(): Map<String, String> {
        val map: MutableMap<String, String> = HashMap()
        try {
            val scanner = Scanner(File("/proc/cpuinfo"))
            while (scanner.hasNextLine()) {
                val vals = scanner.nextLine().split(": ")
                if (vals.size > 1) map[vals[0].trim { it <= ' ' }] = vals[1].trim { it <= ' ' }
            }
        } catch (e: Exception) {
            Log.e("cpuInfo", Log.getStackTraceString(e))
        }
        return map
    }
    fun bogoMIPS(): String {
        var bogomips = cpuInfo()["bogomips"].toString()
        if (bogomips == "null") {
            bogomips = cpuInfo()["BogoMIPS"].toString()
        }
        return bogomips
    }
    fun cpuArchitecture(): String {
        return if (TextUtils.join(", ", Build.SUPPORTED_ABIS).contains("64")) "64-Bit" else "32-Bit"
    }

    fun cpuCores(): Int{
        return Objects.requireNonNull(File("/sys/devices/system/cpu/").listFiles { file ->
            Pattern.matches("cpu[0-9]", file.name)
        }).size
    }
    fun processor(): String{
        var processor = cpuInfo()["model name"].toString()
        if (processor == "null") {
            processor = cpuInfo()["Processor"].toString()
        }
        return processor
    }
}