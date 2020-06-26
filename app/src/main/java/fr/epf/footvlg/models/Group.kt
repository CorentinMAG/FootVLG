package fr.epf.footvlg.models

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
class Group(
    val groupName:String,
    val joinCode:String,
    val creation_date:String,
    val is_group_admin:Boolean,
    val members:List<Member>
):Parcelable