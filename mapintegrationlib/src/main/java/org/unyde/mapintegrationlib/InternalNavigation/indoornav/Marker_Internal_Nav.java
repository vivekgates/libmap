package org.unyde.mapintegrationlib.InternalNavigation.indoornav;

public class Marker_Internal_Nav {

    public Float x;
    public Float y;
    public Float z;
    public String store_id;
    public String site_id;
    public String floor_name;
    public String site_type;





    public Marker_Internal_Nav(float x, float y, float z, String store_id, String floor_name, String site_id, String site_type) {
        this.x = x;
        this.y = y;
        this.z=z;
        this.store_id=store_id;
        this.floor_name=floor_name;
        this.site_id=site_id;
        this.site_type=site_type;
    }

    public Marker_Internal_Nav(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z=z;
    }

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

    public Float getX() {
        return x;
    }

    public void setX(Float x) {
        this.x = x;
    }

    public Float getY() {
        return y;
    }

    public void setY(Float y) {
        this.y = y;
    }

    public Float getZ() {
        return z;
    }

    public void setZ(Float z) {
        this.z = z;
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

    public String getSite_type() {
        return site_type;
    }

    public void setSite_type(String site_type) {
        this.site_type = site_type;
    }
}
