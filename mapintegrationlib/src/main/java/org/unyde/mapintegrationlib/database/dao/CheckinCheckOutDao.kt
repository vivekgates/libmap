package org.unyde.mapintegrationlib.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import org.unyde.mapintegrationlib.database.entity.CheckInCheckOut


/**
 * Created by Deepak kumar on 01/09/2018
 */

@Dao
interface CheckinCheckOutDao {

   /* @get:Query("SELECT * FROM checkin_checkout where store_type='1' limit 1")
    val all: List<CheckInCheckOut>*/

    @get:Query("SELECT * FROM checkin_checkout limit 1")
    val all: List<CheckInCheckOut>

    @Insert
    fun addCheckinCheckOutData(checkin_checkout: CheckInCheckOut)

    @Update
    fun update(checkin_checkout: CheckInCheckOut)


    @Query("DELETE FROM checkin_checkout")
    fun subnukeTable()

    @Query("DELETE FROM checkin_checkout WHERE store_type in (4,10) ")
    fun deleteOtherData()

    @Query("DELETE FROM checkin_checkout WHERE auto_inc LIKE :id ")
    fun deleteData(id: Int)
}
