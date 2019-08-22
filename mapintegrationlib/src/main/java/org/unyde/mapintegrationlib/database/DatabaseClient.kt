package org.unyde.mapintegrationlib.database

import android.content.Context
import androidx.room.Room

class DatabaseClient private constructor(private val mCtx: Context) {
    private val DATABASE_NAME = "WOOGLYMAP.db"
    //our app database object
    val db: MyDatabase

    init {

        //creating the app database with Room database builder
        //MyToDos is the name of the database
        db = Room.databaseBuilder(mCtx, MyDatabase::class.java, DATABASE_NAME).build()
    }

    companion object {
        private var mInstance: DatabaseClient? = null

        @Synchronized
        fun getInstance(mCtx: Context): DatabaseClient {
            if (mInstance == null) {
                mInstance = DatabaseClient(mCtx)
            }
            return mInstance!!
        }
    }
}
