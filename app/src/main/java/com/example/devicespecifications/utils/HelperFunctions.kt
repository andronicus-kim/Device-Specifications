package com.example.devicespecifications.utils

import com.example.devicespecifications.data.ComponentDetail
import com.example.devicespecifications.data.Components

/**
 * Created by Andronicus Kim on 5/31/22
 */
object HelperFunctions {
    fun scanDevice(deviceInfo: DeviceInfo): ArrayList<ComponentDetail>{
        val componentsDetails = arrayListOf<ComponentDetail>()
        // processor details
        componentsDetails.add(ComponentDetail("Processor",CpuUtil.processor(), Components.PROCESSOR.name))
        componentsDetails.add(ComponentDetail("Architecture",CpuUtil.cpuArchitecture(),Components.PROCESSOR.name))
        componentsDetails.add(ComponentDetail("CPU Cores",CpuUtil.cpuCores().toString(),Components.PROCESSOR.name))
        componentsDetails.add(ComponentDetail("SoC Name",deviceInfo.hardware,Components.PROCESSOR.name))
        componentsDetails.add(ComponentDetail("ABI",CpuUtil.cpuAbi(),Components.PROCESSOR.name))
        componentsDetails.add(ComponentDetail("BogoMIPS",CpuUtil.bogoMIPS(),Components.PROCESSOR.name))
        //storage details
        componentsDetails.add(ComponentDetail("Memory (RAM)", deviceInfo.totalRAM.toMB(),Components.STORAGE.name))
        componentsDetails.add(ComponentDetail("Total Internal Storage", deviceInfo.totalInternalMemorySize.toMB(),Components.STORAGE.name))
        componentsDetails.add(ComponentDetail("Available Internal Storage", deviceInfo.availableInternalMemorySize.toMB(),Components.STORAGE.name))
        componentsDetails.add(ComponentDetail("Total External Storage",deviceInfo.totalExternalMemorySize.toMB(),Components.STORAGE.name))
        componentsDetails.add(ComponentDetail("Available External Storage",deviceInfo.availableExternalMemorySize.toMB(),Components.STORAGE.name))
        componentsDetails.add(ComponentDetail("Has SD Card",deviceInfo.hasExternalSDCard().toString(),Components.STORAGE.name))
        // system details
        componentsDetails.add(ComponentDetail("OS Version",deviceInfo.osVersion,Components.SYSTEM.name))
        componentsDetails.add(ComponentDetail("Java VM",deviceInfo.systemProperty(),Components.SYSTEM.name))
        componentsDetails.add(ComponentDetail("Kernel",deviceInfo.kernelVersion(),Components.SYSTEM.name))
        componentsDetails.add(ComponentDetail("Board", deviceInfo.board,Components.SYSTEM.name))
        componentsDetails.add(ComponentDetail("SDK Version",deviceInfo.sdkVersion.toString(),Components.SYSTEM.name))
        componentsDetails.add(ComponentDetail("Display Version",deviceInfo.displayVersion,Components.SYSTEM.name))
        componentsDetails.add(ComponentDetail("Android ID",deviceInfo.androidId,Components.SYSTEM.name))
        componentsDetails.add(ComponentDetail("User Agent",deviceInfo.userAgent,Components.SYSTEM.name))
        // device details
        componentsDetails.add(ComponentDetail("Device Name",deviceInfo.deviceName,Components.DEVICE.name))
        componentsDetails.add(ComponentDetail("Manufacturer",deviceInfo.manufacturer,Components.DEVICE.name))
        componentsDetails.add(ComponentDetail("Product",deviceInfo.product,Components.DEVICE.name))
        componentsDetails.add(ComponentDetail("Radio Version",deviceInfo.radioVersion,Components.DEVICE.name))
        componentsDetails.add(ComponentDetail("Model",deviceInfo.model,Components.DEVICE.name))
        componentsDetails.add(ComponentDetail("Serial",deviceInfo.serial,Components.DEVICE.name))
        componentsDetails.add(ComponentDetail("Build",deviceInfo.buildBrand,Components.DEVICE.name))
        componentsDetails.add(ComponentDetail("Language",deviceInfo.language,Components.DEVICE.name))
        componentsDetails.add(ComponentDetail("Locale",deviceInfo.deviceLocale.toString(),Components.DEVICE.name))
        componentsDetails.add(ComponentDetail("Is Device Rooted",deviceInfo.isDeviceRooted.toString(),Components.DEVICE.name))
        // network details
        componentsDetails.add(ComponentDetail("Phone Type",deviceInfo.phoneType,Components.NETWORK.name))
        componentsDetails.add(ComponentDetail("Operator",deviceInfo.operator,Components.NETWORK.name))
        componentsDetails.add(ComponentDetail("Network Type",deviceInfo.networkType,Components.NETWORK.name))
        componentsDetails.add(ComponentDetail("Is Nfc Enabled",deviceInfo.isNfcEnabled.toString(),Components.NETWORK.name))
        componentsDetails.add(ComponentDetail("Is Nfc Present",deviceInfo.isNfcPresent.toString(),Components.NETWORK.name))
        componentsDetails.add(ComponentDetail("Is Network Available",deviceInfo.isNetworkAvailable.toString(),Components.NETWORK.name))
        componentsDetails.add(ComponentDetail("Operator",deviceInfo.operator,Components.NETWORK.name))
        // battery details
        componentsDetails.add(ComponentDetail("Battery Technology",deviceInfo.batteryTechnology.toString(),Components.BATTERY.name))
        componentsDetails.add(ComponentDetail("Battery Percent",deviceInfo.batteryPercent.toString(),Components.BATTERY.name))
        componentsDetails.add(ComponentDetail("Is Phone Charging",deviceInfo.isPhoneCharging.toString(),Components.BATTERY.name))
        componentsDetails.add(ComponentDetail("Charging Source", deviceInfo.chargingSource,Components.BATTERY.name))
        componentsDetails.add(ComponentDetail("Battery Health", deviceInfo.batteryHealth,Components.BATTERY.name))
        componentsDetails.add(ComponentDetail("Battery Temperature","${deviceInfo.batteryTemperature} C",Components.BATTERY.name))
        componentsDetails.add(ComponentDetail("Is Battery Present",deviceInfo.isBatteryPresent.toString(),Components.BATTERY.name))
        componentsDetails.add(ComponentDetail("Battery Voltage",deviceInfo.batteryVoltage.toString(),Components.BATTERY.name))
        return componentsDetails
    }
}