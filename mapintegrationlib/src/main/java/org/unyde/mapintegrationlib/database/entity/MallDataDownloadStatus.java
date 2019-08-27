package org.unyde.mapintegrationlib.database.entity;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "mall_data_status")
public class MallDataDownloadStatus {

    @PrimaryKey(autoGenerate = true)
    public int Id;

    @ColumnInfo(name = "clustor_id")
    private String clustor_id;

    @ColumnInfo(name = "total_file")
    private String floor_level;

    @ColumnInfo(name = "downloaded_map")
    private String floor_name;

    @ColumnInfo(name = "downloaded_json")
    private String site_id;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getClustor_id() {
        return clustor_id;
    }

    public void setClustor_id(String clustor_id) {
        this.clustor_id = clustor_id;
    }

    public String getFloor_level() {
        return floor_level;
    }

    public void setFloor_level(String floor_level) {
        this.floor_level = floor_level;
    }

    public String getFloor_name() {
        return floor_name;
    }

    public void setFloor_name(String floor_name) {
        this.floor_name = floor_name;
    }

    public String getSite_id() {
        return site_id;
    }

    public void setSite_id(String site_id) {
        this.site_id = site_id;
    }
}
