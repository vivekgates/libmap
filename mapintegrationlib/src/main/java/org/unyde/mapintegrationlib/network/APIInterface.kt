package org.unyde.mapintegrationlib.network


import org.unyde.mapintegrationlib.model.GetClusterDataList
import org.unyde.mapintegrationlib.model.GetMallFloorList.GetMallFloorList
import org.unyde.mapintegrationlib.model.user_transaction.UserTransactionResponse
import retrofit2.http.*


interface APIInterface {


    @FormUrlEncoded
    @POST("getClusterInfoByClusterId")
    fun getClusterDataList(
            @Header("auth-token") Authorization: String,
            @Header("instance-id") instance_id: String,
            @Header("node-id") node_id: String,
            @Field("cluster_id") latitude: String): retrofit2.Call<GetClusterDataList>

    @FormUrlEncoded
    @POST("mallFloorList")
    fun getMallFloorList(
        @Header("token") token: String,
        @Field("id") mall_id: String): retrofit2.Call<GetMallFloorList>


    @FormUrlEncoded
    @POST("setUserTransaction")
    fun setUserTransation(
        @Header("token") token: String,
        @Field("instance_id") instance_id: String,
        @Field("node_id") node_id: String,
        @Field("time") time: String,
        @Field("transaction_type") transaction_type: String,
        @Field("type") type: String,
        @Field("user_id") user_id: String): retrofit2.Call<UserTransactionResponse>

}
