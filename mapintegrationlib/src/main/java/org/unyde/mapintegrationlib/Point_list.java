package org.unyde.mapintegrationlib;
import java.util.List;

public class Point_list {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;
    private List<Signal_val> signal_val;

    public void setSignal_val(List<Signal_val> signal_val){
        this.signal_val = signal_val;
    }
    public List<Signal_val> getSignal_val(){
        return this.signal_val;
    }
}
