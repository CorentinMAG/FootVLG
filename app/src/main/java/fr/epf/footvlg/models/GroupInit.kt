package fr.epf.footvlg.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class GroupInit (
    val creator:Member,
    val groupName:String

)