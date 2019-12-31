package org.unyde.mapintegrationlib.viewmodel;

import android.content.Context;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import org.unyde.mapintegrationlib.model.GetMallFloorList.GetMallFloorList;
import org.unyde.mapintegrationlib.repository.MallFloorListRepository;

public class MallFloorListViewModel extends ViewModel {


    private MutableLiveData<GetMallFloorList> data;
    private MallFloorListRepository mallFloorListRepository;
    String token;

    public MallFloorListViewModel() {

        mallFloorListRepository = new MallFloorListRepository();
    }

    public void init(Context context, String token, String mallid) {
       // this.data=null;

      /*  if (this.data != null) {

            return;
        }*/


        token="$xnNUH!ui:1;m]SArDPgeyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6NjgsImNvbXBhbnlfaWQiOjEsImlzcyI6Imh0dHA6Ly8wLjAuMC4wOjgwMDEvbG9naW4iLCJuYW1lIjoic3VuaWxfdW55ZGUiLCJpYXQiOjE1NjAyNDUxMDR9.oI7860RlEFPNpVcVgyGPa7HdFpna3sdF726SzoNExlA";

        data =  mallFloorListRepository.getMallFloorList(context,token,mallid);
    }

    public MutableLiveData<GetMallFloorList> getMallFloorList() {
        return this.data;
    }

}
