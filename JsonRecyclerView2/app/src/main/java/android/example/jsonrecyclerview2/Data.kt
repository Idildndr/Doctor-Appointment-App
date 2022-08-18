package android.example.jsonrecyclerview2

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
@Parcelize

data class Data(
    val name: String?,
    var statu: String?,
    val gender: String?,
    val imgurl: String?


):Parcelable