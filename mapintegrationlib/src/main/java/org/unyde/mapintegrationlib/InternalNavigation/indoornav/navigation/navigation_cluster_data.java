package org.unyde.mapintegrationlib.InternalNavigation.indoornav.navigation;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class navigation_cluster_data {
    public String cluster_id;
    public String cluster_name;
    public String cluster_orientation;

    @SerializedName("floor")
    public List<navigation_floor_data> floor_list = new ArrayList<navigation_floor_data>();



}
