package org.unyde.mapintegrationlib.InternalNavigation.indoornav.navigation;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class navigation_site_data {
    public String siteid;
    public String site_type;
    // public String site_hotspot;
    public String site_map_coord_x;
    public String site_map_coord_y;
    public String site_map_3dcoord_x;
    public String site_map_3dcoord_y;
    public String site_map_3dcoord_z;



    public String node_name;
    public String node_type;
    public String store_name;
    public String store_id;
    public String store_logo;
    @SerializedName("connection_list")
    public List<navigation_connected_site> connection_list = new ArrayList<navigation_connected_site>();

}