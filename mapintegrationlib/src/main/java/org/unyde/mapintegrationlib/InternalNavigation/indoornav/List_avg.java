package org.unyde.mapintegrationlib.InternalNavigation.indoornav;

import java.util.ArrayList;
import java.util.List;

public class List_avg {

    private List<Integer> val_list;

    public List_avg() {
        val_list = new ArrayList<Integer>();
    }

    public  void set_latest_val(int val)
    {

            if (val_list.size() == 1 && val_list.get(0) == -1000) {
                val_list.remove(0);
            }
            if (val_list.size() <=5) {
                val_list.add(val);
            } else {
                val_list.remove(0);
                val_list.add(val);
            }


    }

    public double get_latest_val()
    {


        double avg,sum=0;
        for(int i =0;i<val_list.size();i++)
        {
            sum +=val_list.get(i);
        }

        avg = sum/val_list.size();

        double power = (((double) (-67 - (avg))) / (double) 25);
        //distance = Math.pow(10, power);
        return avg;
        //return avg;
        //return Math.pow(10, power);
    }

    public double reset_latest_val()
    {
        val_list.clear();
        set_latest_val(-1000);
        return get_latest_val();

    }

}
