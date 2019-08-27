package org.unyde.mapintegrationlib.model.GetMallFloorList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MallFloorList {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("cluster_id")
    @Expose
    private Integer clusterId;
    @SerializedName("floor_number")
    @Expose
    private String floorNumber;
    @SerializedName("floor_alias")
    @Expose
    private String floorAlias;
    @SerializedName("floor_map")
    @Expose
    private String floorMap;
    @SerializedName("floor_json")
    @Expose
    private String floorJson;
    @SerializedName("floor_map_date")
    @Expose
    private String floorMapDate;
    @SerializedName("floor_json_date")
    @Expose
    private String floorJsonDate;
    @SerializedName("floor_date")
    @Expose
    private String floorDate;
    @SerializedName("status")
    @Expose
    private Integer status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getClusterId() {
        return clusterId;
    }

    public void setClusterId(Integer clusterId) {
        this.clusterId = clusterId;
    }

    public String getFloorNumber() {
        return floorNumber;
    }

    public void setFloorNumber(String floorNumber) {
        this.floorNumber = floorNumber;
    }

    public String getFloorAlias() {
        return floorAlias;
    }

    public void setFloorAlias(String floorAlias) {
        this.floorAlias = floorAlias;
    }

    public String getFloorMap() {
        return floorMap;
    }

    public void setFloorMap(String floorMap) {
        this.floorMap = floorMap;
    }

    public String getFloorJson() {
        return floorJson;
    }

    public void setFloorJson(String floorJson) {
        this.floorJson = floorJson;
    }


    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }


    public String getFloorMapDate() {
        return floorMapDate;
    }

    public void setFloorMapDate(String floorMapDate) {
        this.floorMapDate = floorMapDate;
    }

    public String getFloorJsonDate() {
        return floorJsonDate;
    }

    public void setFloorJsonDate(String floorJsonDate) {
        this.floorJsonDate = floorJsonDate;
    }

    public String getFloorDate() {
        return floorDate;
    }

    public void setFloorDate(String floorDate) {
        this.floorDate = floorDate;
    }
}
