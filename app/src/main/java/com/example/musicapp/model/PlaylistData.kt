package com.example.musicapp.model

import android.os.Parcel
import android.os.Parcelable

data class PlaylistData(val id: String?, val name: String?, val imageUrl: String?, val items: Long) :
    Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readLong()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeLong(items)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PlaylistData> {
        override fun createFromParcel(parcel: Parcel): PlaylistData {
            return PlaylistData(parcel)
        }

        override fun newArray(size: Int): Array<PlaylistData?> {
            return arrayOfNulls(size)
        }
    }

}