package fr.epf.footvlg.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName="userInfo")
@JsonClass(generateAdapter = true)
data class Member (
    @PrimaryKey(autoGenerate = false) val id:Int,
    val last_name:String,
    val first_name:String,
    val phone_number:String,
    val email:String,
    val birthday:String,
    val password:String="",
    val inscription_date:String="",
    var is_admin:Boolean=false,
    var is_superuser:Boolean=false
):Parcelable