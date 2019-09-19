package org.unyde.mapintegrationlib.repository;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import org.unyde.mapintegrationlib.model.GetMallFloorList.GetMallFloorList;
import org.unyde.mapintegrationlib.model.user_transaction.UserTransactionResponse;
import org.unyde.mapintegrationlib.network.APIInterface;
import org.unyde.mapintegrationlib.network.ApiClient;
import org.unyde.mapintegrationlib.util.Pref_manager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserTransationRepository {


    private APIInterface apiInterface;


    public UserTransationRepository() {

    }

   /* instance_id:483
    node_id:FF11011C000650000007590011002800
    time:2019-01-25 18:52:38
    transaction_type:1
    type:1
    user_id:34*/

    public MutableLiveData<UserTransactionResponse> setUserTransaction(Context context ,String token, String node_id , String time , String transaction_type , String type) {
        final MutableLiveData<UserTransactionResponse> setUserTransactionMutableLiveData = new MutableLiveData<>();
        apiInterface = ApiClient.getClientToken().create(APIInterface.class);
        Call<UserTransactionResponse> call = apiInterface.setUserTransation(token, Pref_manager.Companion.getINSTANCE_ID(),node_id,time,transaction_type,type,Pref_manager.Companion.getUSER_ID());

        call.enqueue(new Callback<UserTransactionResponse>() {
            @Override
            public void onResponse(Call<UserTransactionResponse> call, Response<UserTransactionResponse> response) {
                if(response.body()!=null)
                {
                    setUserTransactionMutableLiveData.setValue(response.body());
                }


            }

            @Override
            public void onFailure(Call<UserTransactionResponse> call, Throwable t) {

            }
        });

        return setUserTransactionMutableLiveData;
    }









}
