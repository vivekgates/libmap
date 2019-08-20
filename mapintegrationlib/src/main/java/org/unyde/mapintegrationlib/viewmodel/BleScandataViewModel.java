package org.unyde.mapintegrationlib.viewmodel;

import android.content.Context;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import org.unyde.mapintegrationlib.blescan.ScanReceiver;
import org.unyde.mapintegrationlib.model.GetClusterDataList;
import org.unyde.mapintegrationlib.repository.ClusterDetailRepository;

public class BleScandataViewModel extends ViewModel {


    private MutableLiveData<String> data;


    public void init(Context context) {
        this.data=null;

        if (this.data != null) {

            return;
        }


        data = ScanReceiver.Companion.getScanObservable();
    }

    public MutableLiveData<String> getClusterDetails() {
        return this.data;
    }

}
