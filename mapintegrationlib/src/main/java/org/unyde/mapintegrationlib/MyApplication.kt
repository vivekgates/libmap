package org.unyde.mapintegrationlib

import android.app.Application
import androidx.room.Room
import org.unyde.mapintegrationlib.database.MyDatabase

class MyApplication : Application(){

    //var db: MyDatabase? = null



    override fun onCreate() {
        super.onCreate()
      /*  db = Room.databaseBuilder(applicationContext,
             MyDatabase::class.java, DATABASE_NAME
        )
            .allowMainThreadQueries()
            .build()*/



        INSTANCE = this
        ///var pahNodeDao = INSTANCE!!.db!!.pathNodeList().findClusterOrientation("101")

    }

    companion object {
        var INSTANCE: MyApplication? = null
        private val DATABASE_NAME = "WOOGLYMAP.db"

        fun get(): MyApplication? {
            return INSTANCE
        }

        val instance: MyApplication
            @Synchronized get() {
                if (INSTANCE == null) {
                    INSTANCE = MyApplication()
                }
                return INSTANCE as MyApplication
            }
    }


}
