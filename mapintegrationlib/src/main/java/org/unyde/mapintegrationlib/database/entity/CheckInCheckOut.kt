package org.unyde.mapintegrationlib.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "checkin_checkout")
class CheckInCheckOut {

    @ColumnInfo(name = "auto_inc")
    @PrimaryKey(autoGenerate = true)
    var auto_inc: Int = 0

    @ColumnInfo(name = "beacon_id")
    var beacon_id: String = ""

    @ColumnInfo(name = "type_in_out")
     var type_in_out: Int = 0

    @ColumnInfo(name = "time_in_out")
    var time_in_out: String = ""

    @ColumnInfo(name = "created_date")
    var createDate: String = ""

    @ColumnInfo(name = "transaction_type")
    var transaction_type: String = ""

    @ColumnInfo(name = "store_type")
    var store_type: String = ""

    constructor(beacon_id: String, type_in_out: Int, time_in_out: String, createDate: String,transaction_type: String,store_type: String) {
        this.beacon_id = beacon_id
        this.type_in_out = type_in_out
        this.time_in_out = time_in_out
        this.createDate = createDate
        this.transaction_type=transaction_type
        this.store_type=store_type
    }
}
