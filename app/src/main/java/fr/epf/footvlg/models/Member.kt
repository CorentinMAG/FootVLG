package fr.epf.footvlg.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass

@Entity(tableName="userInfo")
@JsonClass(generateAdapter = true)
data class Member (
     @PrimaryKey(autoGenerate = true) val id:Int,
     val last_name:String,
     val first_name:String,
     val phone_number:String,
     val email:String,
     val birthday:String,
     val password:String="",
     val is_admin:Boolean=false,
     val is_superuser:Boolean=false
)