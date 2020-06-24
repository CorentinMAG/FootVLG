package fr.epf.footvlg.BDD.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import fr.epf.footvlg.models.Member

@Dao
interface UserDAO {
    @Query("select * from userInfo")
    suspend fun getUser():Member?

    @Insert
    suspend fun addUser(user: Member)

    @Delete
    suspend fun deleteUser(user: Member)

    @Query("select count(*) from userInfo")
    suspend fun CheckUser(): Int
}