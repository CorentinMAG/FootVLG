package fr.epf.footvlg.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class JoinGroup(
    val user:Member,
    val code:String
)