package org.unyde.mapintegrationlib.InternalNavigation.indoornav;

import android.graphics.Point;

import java.util.ArrayList;
import java.util.List;

public class Path_node {

    public List<Point> node_cordinate;
    public List<String> node_key;

    public Path_node() {
        node_cordinate = new ArrayList<Point>();
        node_key = new ArrayList<String>();
    }

    public void set_node_data(String key, Point p1)
    {
        node_key.add(key);
        node_cordinate.add(p1);
    }



    //<String,Point>
}
