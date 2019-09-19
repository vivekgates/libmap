package org.unyde.mapintegrationlib.viewmodel;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.unyde.mapintegrationlib.model.GetMallFloorList.GetMallFloorList;
import org.unyde.mapintegrationlib.model.user_transaction.UserTransactionResponse;
import org.unyde.mapintegrationlib.repository.MallFloorListRepository;
import org.unyde.mapintegrationlib.repository.UserTransationRepository;

public class UserTransactionViewModel extends ViewModel {


    private MutableLiveData<UserTransactionResponse> data;
    private UserTransationRepository userTransationRepository;
    String token;

    public UserTransactionViewModel() {

        userTransationRepository = new UserTransationRepository();
    }

    public void init(Context context, String token, String node_id, String time, String transaction_type, String type) {
        this.data=null;

        if (this.data != null) {

            return;
        }


        token="$xnNUH!ui:1;m]SArDPgeyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6NjgsImNvbXBhbnlfaWQiOjEsImlzcyI6Imh0dHA6Ly8wLjAuMC4wOjgwMDEvbG9naW4iLCJuYW1lIjoic3VuaWxfdW55ZGUiLCJpYXQiOjE1NjAyNDUxMDR9.oI7860RlEFPNpVcVgyGPa7HdFpna3sdF726SzoNExlA";

        data =  userTransationRepository.setUserTransaction(context,token,node_id,time,transaction_type,type);
    }

    public MutableLiveData<UserTransactionResponse> setUserTransaction() {
        return this.data;
    }

}
