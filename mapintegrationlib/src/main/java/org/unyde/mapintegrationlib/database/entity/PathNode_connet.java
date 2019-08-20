package org.unyde.mapintegrationlib.database.entity;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "pathnode_connect")
public class PathNode_connet {

    @PrimaryKey(autoGenerate = true)
    public int Id;

    @ColumnInfo(name = "clustor_id")
    private String clustor_id;

    @ColumnInfo(name = "floor_level")
    private String floor_level;

    @ColumnInfo(name = "site_id")
    private String site_id;

    @ColumnInfo(name = "site_id_connect")
    private String site_id_connect;


    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getFloor_level() {
        return floor_level;
    }

    public void setFloor_level(String floor_level) {
        this.floor_level = floor_level;
    }

    public String getSite_id() {
        return site_id;
    }

    public void setSite_id(String site_id) {
        this.site_id = site_id;
    }

    public String getClustor_id() {
        return clustor_id;
    }

    public void setClustor_id(String clustor_id) {
        this.clustor_id = clustor_id;
    }

    public String getSite_id_connect() {
        return site_id_connect;
    }

    public void setSite_id_connect(String site_id_connect) {
        this.site_id_connect = site_id_connect;
    }
}
