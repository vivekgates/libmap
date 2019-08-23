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
    private Object floorMapDate;
    @SerializedName("floor_json_date")
    @Expose
    private Object floorJsonDate;
    @SerializedName("floor_date")
    @Expose
    private Object floorDate;
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

    public Object getFloorMapDate() {
        return floorMapDate;
    }

    public void setFloorMapDate(Object floorMapDate) {
        this.floorMapDate = floorMapDate;
    }

    public Object getFloorJsonDate() {
        return floorJsonDate;
    }

    public void setFloorJsonDate(Object floorJsonDate) {
        this.floorJsonDate = floorJsonDate;
    }

    public Object getFloorDate() {
        return floorDate;
    }

    public void setFloorDate(Object floorDate) {
        this.floorDate = floorDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }


}
