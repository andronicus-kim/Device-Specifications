package com.example.devicespecifications.data

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Andronicus Kim on 5/31/22
 */
data class ComponentDetail(val title: String, val subtitle: String,val component: String):
    Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(subtitle)
        parcel.writeString(component)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ComponentDetail> {
        override fun createFromParcel(parcel: Parcel): ComponentDetail {
            return ComponentDetail(parcel)
        }

        override fun newArray(size: Int): Array<ComponentDetail?> {
            return arrayOfNulls(size)
        }
    }
}
