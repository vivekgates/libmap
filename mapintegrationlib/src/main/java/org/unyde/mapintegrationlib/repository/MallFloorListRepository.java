package org.unyde.mapintegrationlib.repository;

import android.content.Context;
import androidx.lifecycle.MutableLiveData;
import org.unyde.mapintegrationlib.model.GetMallFloorList.GetMallFloorList;
import org.unyde.mapintegrationlib.network.APIInterface;
import org.unyde.mapintegrationlib.network.ApiClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MallFloorListRepository {


    private APIInterface apiInterface;


    public MallFloorListRepository() {

    }

    public MutableLiveData<GetMallFloorList> getMallFloorList(Context context , String token , String mallid) {
        final MutableLiveData<GetMallFloorList> getMallFloorListMutableLiveData = new MutableLiveData<>();
        apiInterface = ApiClient.getClientToken().create(APIInterface.class);
        Call<GetMallFloorList> call = apiInterface.getMallFloorList(token,mallid);

        call.enqueue(new Callback<GetMallFloorList>() {
            @Override
            public void onResponse(Call<GetMallFloorList> call, Response<GetMallFloorList> response) {
                if(response.body()!=null)
                {
                    getMallFloorListMutableLiveData.setValue(response.body());
                }


            }

            @Override
            public void onFailure(Call<GetMallFloorList> call, Throwable t) {

            }
        });

        return getMallFloorListMutableLiveData;
    }









}
