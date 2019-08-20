package org.unyde.mapintegrationlib.database.entity;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "pathnode")
public class PathNode {

    @PrimaryKey(autoGenerate = true)
    public int Id;

    @ColumnInfo(name = "clustor_id")
    private String clustor_id;

    @ColumnInfo(name = "floor_level")
    private String floor_level;

    @ColumnInfo(name = "floor_name")
    private String floor_name;

    @ColumnInfo(name = "site_id")
    private String site_id;

    @ColumnInfo(name = "store_id")
    private String store_id;

    @ColumnInfo(name = "store_name")
    private String store_name;



    @ColumnInfo(name = "site_type")
    private String site_type;


    @ColumnInfo(name = "site_map_coord_x")
    private String site_map_coord_x;

    @ColumnInfo(name = "site_map_coord_y")
    private String site_map_coord_y;




    @ColumnInfo(name = "site_map_coord_z")
    private String site_map_coord_z;

    @ColumnInfo(name = "node_type")
    private String node_type;

    @ColumnInfo(name = "node_name")
    private String node_name;

    @ColumnInfo(name = "address")
    private String address;

    @ColumnInfo(name = "store_type")
    private String store_type;

    @ColumnInfo(name = "mobile")
    private String mobile;

    @ColumnInfo(name = "phone")
    private String phone;

    @ColumnInfo(name = "cluster_orientation")
    private String cluster_orientation;

    @ColumnInfo(name = "store_logo")
    private String store_logo;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStore_type() {
        return store_type;
    }

    public void setStore_type(String store_type) {
        this.store_type = store_type;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCluster_orientation() {
        return cluster_orientation;
    }

    public void setCluster_orientation(String cluster_orientation) {
        this.cluster_orientation = cluster_orientation;
    }

    public String getFloor_name() {
        return floor_name;
    }

    public void setFloor_name(String floor_name) {
        this.floor_name = floor_name;
    }

    public String getClustor_id() {
        return clustor_id;
    }

    public void setClustor_id(String clustor_id) {
        this.clustor_id = clustor_id;
    }
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

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public String getSite_type() {
        return site_type;
    }

    public void setSite_type(String site_type) {
        this.site_type = site_type;
    }

    public String getSite_map_coord_x() {
        return site_map_coord_x;
    }

    public void setSite_map_coord_x(String site_map_coord_x) {
        this.site_map_coord_x = site_map_coord_x;
    }


    public String getSite_map_coord_z() {
        return site_map_coord_z;
    }

    public void setSite_map_coord_z(String site_map_coord_z) {
        this.site_map_coord_z = site_map_coord_z;
    }

    public String getSite_map_coord_y() {
        return site_map_coord_y;
    }

    public void setSite_map_coord_y(String site_map_coord_y) {
        this.site_map_coord_y = site_map_coord_y;
    }

    public String getNode_type() {
        return node_type;
    }

    public void setNode_type(String node_type) {
        this.node_type = node_type;
    }

    public String getNode_name() {
        return node_name;
    }

    public void setNode_name(String node_name) {
        this.node_name = node_name;
    }

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }


    public String getStore_logo() {
        return store_logo;
    }

    public void setStore_logo(String store_logo) {
        this.store_logo = store_logo;
    }
}
