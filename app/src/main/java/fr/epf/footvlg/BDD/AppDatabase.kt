package fr.epf.footvlg.BDD

import androidx.room.Database
import androidx.room.RoomDatabase
import fr.epf.footvlg.BDD.DAO.UserDAO
import fr.epf.footvlg.models.Member

@Database(entities= arrayOf(
    Member::class
    ),version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun getUserInfo(): UserDAO
}