package fr.epf.footvlg.API

import fr.epf.footvlg.models.Member
import retrofit2.Call
import retrofit2.http.*

interface APIService{
    //newMember.php
    @Headers("Content-Type: application/json")
    @POST("newMember.php")
    fun createMember(@Body member:Member): Call<String>

    @Headers("Content-Type: application/json")
    @POST("checkCredentials.php")
    fun checkMember(@Body member:Member):Call<String>

    @FormUrlEncoded
    @POST("checkCredentials.php")
    fun checkMemberManually(@Field("email") email:String,@Field("password") password:String):Call<String>
}