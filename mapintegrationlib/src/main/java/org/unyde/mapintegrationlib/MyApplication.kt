package org.unyde.mapintegrationlib

import android.app.Application
import androidx.room.Room
import com.polidea.rxandroidble2.LogConstants
import com.polidea.rxandroidble2.LogOptions
import com.polidea.rxandroidble2.RxBleClient
import org.unyde.mapintegrationlib.database.MyDatabase

class MyApplication : Application(){

    var db: MyDatabase? = null



    override fun onCreate() {
        super.onCreate()
        db = Room.databaseBuilder(applicationContext,
             MyDatabase::class.java, DATABASE_NAME
        )
            .allowMainThreadQueries()
            .build()

        rxBleClient = RxBleClient.create(this)
        RxBleClient.updateLogOptions(
            LogOptions.Builder()
            .setLogLevel(LogConstants.INFO)
            .setMacAddressLogSetting(LogConstants.MAC_ADDRESS_FULL)
            .setUuidsLogSetting(LogConstants.UUIDS_FULL)
            .setShouldLogAttributeValues(true)
            .build()
        )


        INSTANCE = this
        var pahNodeDao = INSTANCE!!.db!!.pathNodeList().findClusterOrientation("101")

    }

    companion object {
        lateinit var rxBleClient: RxBleClient
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
