package com.example.pertemuan10.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.pertemuan10.data.dao.MahasiswaDao
import com.example.pertemuan10.data.entity.Mahasiswa

//mendefinisikan database dengan tabel mahasiswa
@Database(entities = [Mahasiswa::class], version = 1, exportSchema = false)
abstract class KrsDatabase : RoomDatabase(){
    //mendefisikan fungsi untuk mengakses data mahsiswa
    abstract fun mahasiswaDao() : MahasiswaDao

    companion object{
        @Volatile//memastikan bahwa nilai variabel instance sama di semua thread
        private var Instance:KrsDatabase? = null

        fun getDatabase(context: Context):KrsDatabase{
            return (Instance ?: synchronized(this){
                Room.databaseBuilder(
                    context,
                    KrsDatabase::class.java, //class database
                    "KrsDatabase" //nama database
                )
                    .build().also { Instance = it }
            })
        }
    }
}