package com.example.intervaltimer

import android.os.Parcelable
import androidx.lifecycle.MutableLiveData
import androidx.room.RawQuery
import com.example.room.Interval
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue
import java.lang.reflect.Array
import java.util.*

@Parcelize
data class IntervalListData(
    val List:  List<Interval>

 ) : Parcelable