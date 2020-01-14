package com.ifanr.tangzhi.ui.share

import android.os.Parcel
import android.os.Parcelable

/**
 * 分享产品
 */
class ShareProductReq(): Parcelable {

    var id: String = ""
    var coverImage: String = ""
    var title: String = ""

    constructor(id: String, coverImage: String, title: String) : this() {
        this.id = id
        this.coverImage = coverImage
        this.title = title
    }

    constructor(parcel: Parcel) : this() {
        id = parcel.readString() ?: ""
        coverImage = parcel.readString() ?: ""
        title = parcel.readString() ?: ""
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(coverImage)
        parcel.writeString(title)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ShareProductReq> {
        override fun createFromParcel(parcel: Parcel): ShareProductReq {
            return ShareProductReq(parcel)
        }

        override fun newArray(size: Int): Array<ShareProductReq?> {
            return arrayOfNulls(size)
        }
    }

}