package org.unyde.mapintegrationlib.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "cluster_primary_info")
class Cluster_Primary_Info {

    @ColumnInfo(name = "auto_inc")
    @PrimaryKey(autoGenerate = true)
    var auto_incid: Int = 0

    @ColumnInfo(name = "cluster_id")
    var cluster_id: Int = 0

    @ColumnInfo(name = "cluster_name")
    var cluster_name: String = ""

    @ColumnInfo(name = "cluster_orientation")
    var cluster_orientation: String = ""

    @ColumnInfo(name = "floor_level")
    var floor_level: String = ""

    @ColumnInfo(name = "floor_name")
    var floor_name: String = ""

    @ColumnInfo(name = "floor_map")
    var floor_map: String = ""

    @ColumnInfo(name = "floor_map_height")
    var floor_map_height: String = ""


    @ColumnInfo(name = "floor_map_width")
    var floor_map_width: String = ""

    @ColumnInfo(name = "default_location_site_id")
    var default_location_site_id: String = ""

    @ColumnInfo(name = "default_location_site_name")
    var default_location_site_name: String = ""

    constructor(cluster_id: Int, cluster_name: String, cluster_orientation: String, floor_level: String, floor_name: String, floor_map: String, floor_map_height: String, floor_map_width: String, default_location_site_id: String, default_location_site_name: String) {
        this.cluster_id = cluster_id
        this.cluster_name = cluster_name
        this.cluster_orientation = cluster_orientation
        this.floor_level = floor_level
        this.floor_name = floor_name
        this.floor_map = floor_map
        this.floor_map_height = floor_map_height
        this.floor_map_width = floor_map_width
        this.default_location_site_id = default_location_site_id
        this.default_location_site_name = default_location_site_name
    }
}
