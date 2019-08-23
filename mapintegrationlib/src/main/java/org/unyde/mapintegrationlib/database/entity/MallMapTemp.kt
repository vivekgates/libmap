package org.unyde.mapintegrationlib.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "mall_map_temp")
class MallMapTemp {

    @ColumnInfo(name = "auto_inc")
    @PrimaryKey(autoGenerate = true)
    var auto_incid: Int = 0

    constructor(id: Int, cluster_id: Int, floor_number: Int, status: Int, floor_alias: String, floor_map: String, floor_json_file: String, local_pathImage: String, local_pathJson: String, isMapDownloaded: Int, isJsonDownloaded: Int,floor_map_date: String,floor_json_date: String,floor_date: String) {
        this.id = id
        this.cluster_id = cluster_id
        this.floor_number = floor_number
        this.status = status
        this.floor_alias = floor_alias
        this.floor_map = floor_map
        this.floor_json_file = floor_json_file
        this.local_pathImage = local_pathImage
        this.local_pathJson=local_pathJson
        this.isMapDownloaded=isMapDownloaded
        this.isJsonDownloaded=isJsonDownloaded
        this.floor_map_date=floor_map_date
        this.floor_json_date=floor_json_date
        this.floor_date=floor_date
    }

    @ColumnInfo(name = "id")
    var id: Int = 0

    @ColumnInfo(name = "cluster_id")
    var cluster_id: Int = 0


    @ColumnInfo(name = "floor_number")
    var floor_number: Int = 0

    @ColumnInfo(name = "status")
    var status: Int = 0

    @ColumnInfo(name = "floor_alias")
    var floor_alias: String = ""

    @ColumnInfo(name = "floor_map")
    var floor_map: String = ""

    @ColumnInfo(name = "floor_json_file")
    var floor_json_file: String = ""

    @ColumnInfo(name = "local_pathImage")
    var local_pathImage: String = ""

    @ColumnInfo(name = "local_pathJson")
    var local_pathJson: String = ""


    @ColumnInfo(name = "isMapDownloaded")
    var isMapDownloaded: Int = 0

    @ColumnInfo(name = "isJsonDownloaded")
    var isJsonDownloaded: Int = 0



    @ColumnInfo(name = "floor_map_date")
    var floor_map_date: String = ""

    @ColumnInfo(name = "floor_json_date")
    var floor_json_date: String = ""

    @ColumnInfo(name = "floor_date")
    var floor_date: String = ""

}
