package fr.epf.footvlg.API

import fr.epf.footvlg.models.Group
import fr.epf.footvlg.models.GroupInit
import fr.epf.footvlg.models.JoinGroup
import fr.epf.footvlg.models.Member
import retrofit2.Call
import retrofit2.http.*

interface APIService{
    //newMember.php
    @Headers("Content-Type: application/json")
    @POST("newMember.php")
    fun createMember(@Body member:Member): Call<Member>

    @Headers("Content-Type: application/json")
    @POST("checkCredentials.php")
    fun checkMember(@Body member:Member):Call<Member>

    @FormUrlEncoded
    @POST("checkCredentials.php")
    fun checkMemberManually(@Field("email") email:String,@Field("password") password:String):Call<String>

    @POST("createGroup.php")
    fun createGroup(@Body credentials: GroupInit) :Call<String>

    @POST("getUserGroups.php")
    fun getUserGroups(@Body member:Member):Call<List<Group>>

    @POST("joinGroup.php")
    fun userJoinGroup(@Body credentials: JoinGroup):Call<String>


    @POST("updateUser.php")
    fun updateUser(@Body user:Member):Call<String>

    @POST("getAllUsers.php")
    fun getAllUsers(@Body member:Member):Call<List<Member>>
}