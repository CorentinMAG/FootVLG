package fr.epf.footvlg.utils

import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import fr.epf.footvlg.BDD.AppDatabase
import fr.epf.footvlg.BDD.DAO.UserDAO
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.security.MessageDigest

fun hashString(input: String, algorithm: String): String {
    return MessageDigest
        .getInstance(algorithm)
        .digest(input.toByteArray())
        .fold("", { str, it -> str + "%02x".format(it) })
}

fun retrofit(): Retrofit {
    val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    return Retrofit.Builder()
        .baseUrl("https://foot.agenda-crna-n.com/")
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(MoshiConverterFactory.create().asLenient())
        .build()
}

fun AppCompatActivity.UserDAO(): UserDAO {
    val database = Room.databaseBuilder(this,
        AppDatabase::class.java,"info_user")
        .build()
    return database.getUserInfo()
}