package com.example.devicespecifications.utils

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.nfc.NfcAdapter
import android.os.BatteryManager
import android.os.Build
import android.os.Environment
import android.os.StatFs
import android.provider.Settings
import android.telephony.TelephonyManager
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.annotation.RequiresApi
import java.io.File
import java.io.IOException
import java.io.RandomAccessFile
import java.util.*

/**
 * Created by Andronicus Kim on 5/31/22
 */
class DeviceInfo(private val context: Context) {

    private val BATTERY_HEALTH_COLD = "cold"
    private val BATTERY_HEALTH_DEAD = "dead"
    private val BATTERY_HEALTH_GOOD = "good"
    private val BATTERY_HEALTH_OVERHEAT = "Over Heat"
    private val BATTERY_HEALTH_OVER_VOLTAGE = "Over Voltage"
    private val BATTERY_HEALTH_UNKNOWN = "Unknown"
    private val BATTERY_HEALTH_UNSPECIFIED_FAILURE = "Unspecified failure"
    private val BATTERY_PLUGGED_AC = "Charging via AC"
    private val BATTERY_PLUGGED_USB = "Charging via USB"
    private val BATTERY_PLUGGED_WIRELESS = "Wireless"
    private val BATTERY_PLUGGED_UNKNOWN = "Unknown Source"
    private val PHONE_TYPE_GSM = "GSM"
    private val PHONE_TYPE_CDMA = "CDMA"
    private val PHONE_TYPE_NONE = "Unknown"
    private val NETWORK_TYPE_WIFI_WIFIMAX = "WiFi"
    private val NETWORK_TYPE_METERED = "Metered"


    private val NOT_FOUND_VAL = "unknown"

    val deviceName: String
        get() {
            val manufacturer = Build.MANUFACTURER
            val model = Build.MODEL
            return if (model.startsWith(manufacturer)) {
                model
            } else {
                manufacturer + " " + model
            }
        }

    val deviceLocale: String?
        get() {
            var locale: String? = null
            val current = context.resources.configuration.locale
            if (current != null) {
                locale = current.toString()
            }

            return locale
        }

    val manufacturer: String
        get() = Build.MANUFACTURER

    val model: String
        get() = Build.MODEL


    val product: String
        get() = Build.PRODUCT

    val hardware: String
        get() = Build.HARDWARE


    val radioVersion: String
        @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH)
        get() = Build.getRadioVersion()


    val device: String
        get() = Build.DEVICE

    val board: String
        get() = Build.BOARD

    val displayVersion: String
        get() = Build.DISPLAY

    val buildBrand: String
        get() = Build.BRAND

    val serial: String
        get() = Build.SERIAL

    val osVersion: String
        get() = Build.VERSION.RELEASE

    val language: String
        get() = Locale.getDefault().language

    val sdkVersion: Int
        get() = Build.VERSION.SDK_INT

    private val batteryStatusIntent: Intent?
        get() {
            val batFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
            return context.registerReceiver(null, batFilter)
        }

    val batteryPercent: Int
        get() {
            val intent = batteryStatusIntent
            val rawlevel = intent!!.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
            var level = -1
            if (rawlevel >= 0 && scale > 0) {
                level = rawlevel * 100 / scale
            }
            return level
        }

    val isPhoneCharging: Boolean
        get() {
            val intent = batteryStatusIntent
            val plugged = intent!!.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0)
            return plugged == BatteryManager.BATTERY_PLUGGED_AC || plugged == BatteryManager.BATTERY_PLUGGED_USB
        }

    val batteryHealth: String
        get() {
            var health = BATTERY_HEALTH_UNKNOWN
            val intent = batteryStatusIntent
            val status = intent!!.getIntExtra(BatteryManager.EXTRA_HEALTH, 0)
            when (status) {
                BatteryManager.BATTERY_HEALTH_COLD -> health = BATTERY_HEALTH_COLD

                BatteryManager.BATTERY_HEALTH_DEAD -> health = BATTERY_HEALTH_DEAD

                BatteryManager.BATTERY_HEALTH_GOOD -> health = BATTERY_HEALTH_GOOD

                BatteryManager.BATTERY_HEALTH_OVERHEAT -> health = BATTERY_HEALTH_OVERHEAT

                BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE -> health = BATTERY_HEALTH_OVER_VOLTAGE

                BatteryManager.BATTERY_HEALTH_UNKNOWN -> health = BATTERY_HEALTH_UNKNOWN

                BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE -> health = BATTERY_HEALTH_UNSPECIFIED_FAILURE
            }
            return health
        }

    val batteryTechnology: String?
        get() {
            val intent = batteryStatusIntent
            return intent!!.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY)
        }

    val batteryTemperature: Float
        get() {
            val intent = batteryStatusIntent
            val temperature = intent!!.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0)
            return (temperature / 10.0).toFloat()
        }

    val batteryVoltage: Int
        get() {
            val intent = batteryStatusIntent
            return intent!!.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0)
        }

    val chargingSource: String
        get() {
            val intent = batteryStatusIntent
            val plugged = intent!!.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0)
            when (plugged) {
                BatteryManager.BATTERY_PLUGGED_AC -> return BATTERY_PLUGGED_AC
                BatteryManager.BATTERY_PLUGGED_USB -> return BATTERY_PLUGGED_USB
                BatteryManager.BATTERY_PLUGGED_WIRELESS -> return BATTERY_PLUGGED_WIRELESS
                else -> return BATTERY_PLUGGED_UNKNOWN
            }
        }

    val isBatteryPresent: Boolean
        get() {
            val intent = batteryStatusIntent
            return intent!!.getBooleanExtra(BatteryManager.EXTRA_PRESENT, false)
        }

    val isDeviceRooted: Boolean
        get() {
            val paths = arrayOf("/system/app/Superuser.apk", "/sbin/su", "/system/bin/su", "/system/xbin/su", "/data/local/xbin/su", "/data/local/bin/su", "/system/sd/xbin/su", "/system/bin/failsafe/su", "/data/local/su", "/su/bin/su")
            for (path in paths) {
                if (File(path).exists()) return true
            }
            return false
        }

    val androidId: String
        get() = Settings.Secure.getString(context.contentResolver,
            Settings.Secure.ANDROID_ID)

    val userAgent: String
        get() {
            val systemUa = System.getProperty("http.agent")
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                WebSettings.getDefaultUserAgent(context) + "__" + systemUa
            } else WebView(context).settings.userAgentString + "__" + systemUa
        }


    val totalRAM: Long
        get() {
            var totalMemory: Long = 0
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                val mi = ActivityManager.MemoryInfo()
                val activityManager = context.getSystemService(Activity.ACTIVITY_SERVICE) as ActivityManager
                activityManager.getMemoryInfo(mi)
                return mi.totalMem
            }
            try {
                val reader = RandomAccessFile("/proc/meminfo", "r")
                val load = reader.readLine().replace("\\D+".toRegex(), "")
                totalMemory = Integer.parseInt(load).toLong()
                reader.close()
                return totalMemory
            } catch (e: IOException) {
                e.printStackTrace()
                return 0L
            }

        }

    val availableInternalMemorySize: Long
        get() {
            val path = Environment.getDataDirectory()
            val stat = StatFs(path.path)
            val blockSize: Long
            val availableBlocks: Long
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                blockSize = stat.blockSizeLong
                availableBlocks = stat.availableBlocksLong
            } else {
                blockSize = stat.blockSize.toLong()
                availableBlocks = stat.availableBlocks.toLong()
            }
            return availableBlocks * blockSize
        }

    val totalInternalMemorySize: Long
        get() {
            val path = Environment.getDataDirectory()
            val stat = StatFs(path.path)
            val blockSize: Long
            val totalBlocks: Long
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                blockSize = stat.blockSizeLong
                totalBlocks = stat.blockCountLong
            } else {
                blockSize = stat.blockSize.toLong()
                totalBlocks = stat.blockCount.toLong()
            }
            return totalBlocks * blockSize
        }


    val availableExternalMemorySize: Long
        get() {
            if (hasExternalSDCard()) {
                val path = Environment.getExternalStorageDirectory()
                val stat = StatFs(path.path)
                val blockSize: Long
                val availableBlocks: Long
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    blockSize = stat.blockSizeLong
                    availableBlocks = stat.availableBlocksLong
                } else {
                    blockSize = stat.blockSize.toLong()
                    availableBlocks = stat.availableBlocks.toLong()
                }
                return availableBlocks * blockSize
            }
            return 0
        }


    val totalExternalMemorySize: Long
        get() {
            if (hasExternalSDCard()) {
                val path = Environment.getExternalStorageDirectory()
                val stat = StatFs(path.path)
                val blockSize: Long
                val totalBlocks: Long
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    blockSize = stat.blockSizeLong
                    totalBlocks = stat.blockCountLong
                } else {
                    blockSize = stat.blockSize.toLong()
                    totalBlocks = stat.blockCount.toLong()
                }
                return totalBlocks * blockSize
            }
            return 0
        }

    val phoneType: String
        get() {
            val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            when (tm.phoneType) {
                TelephonyManager.PHONE_TYPE_GSM -> return PHONE_TYPE_GSM
                TelephonyManager.PHONE_TYPE_CDMA -> return PHONE_TYPE_CDMA
                TelephonyManager.PHONE_TYPE_NONE -> return PHONE_TYPE_NONE
                else -> return PHONE_TYPE_NONE
            }
        }


    val operator: String
        get() {
            var operatorName: String?
            val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            operatorName = telephonyManager.networkOperatorName
            if (operatorName == null)
                operatorName = telephonyManager.simOperatorName
            return operatorName
        }


    val isNfcPresent: Boolean
        get() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD_MR1) {
                val nfcAdapter = NfcAdapter.getDefaultAdapter(context)
                return nfcAdapter != null
            }
            return false
        }

    val isNfcEnabled: Boolean
        get() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD_MR1) {
                val nfcAdapter = NfcAdapter.getDefaultAdapter(context)
                return nfcAdapter != null && nfcAdapter.isEnabled
            }
            return false
        }

    val isNetworkAvailable: Boolean
        get() {
            val cm = context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val netInfo = cm.activeNetworkInfo
            return netInfo != null && netInfo.isConnected
        }

    val networkType: String
        get() {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = cm.activeNetworkInfo
            if (activeNetwork == null)
                return NOT_FOUND_VAL
            else if (activeNetwork.type == ConnectivityManager.TYPE_WIFI || activeNetwork.type == ConnectivityManager.TYPE_WIMAX) {
                return NETWORK_TYPE_WIFI_WIFIMAX
            } else if (activeNetwork.type == ConnectivityManager.TYPE_MOBILE) {
                return NETWORK_TYPE_METERED
            }
            return NOT_FOUND_VAL
        }

    fun hasExternalSDCard(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }

    /**
     * Returns if the device is using dalvik or any other jvm and its version;
     *
     * @return
     */
    fun systemProperty(): String {
        return "${System.getProperty("java.vm.name")} ${System.getProperty("java.vm.version")}"
    }

    /**
     * Returns the kernel version of the device;
     *
     * @return
     */
    fun kernelVersion(): String {
        return System.getProperty("os.version")
    }
}