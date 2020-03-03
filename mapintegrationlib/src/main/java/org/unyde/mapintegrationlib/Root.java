package org.unyde.mapintegrationlib;

import java.util.ArrayList;
import java.util.List;
public class Root
{
    private List<Point_list> point_list;

    public void setPoint_list(List<Point_list> point_list){
        this.point_list = point_list;
    }
    public List<Point_list> getPoint_list(){
        return this.point_list;
    }
}