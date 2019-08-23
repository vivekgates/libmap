package org.unyde.mapintegrationlib.network


import org.unyde.mapintegrationlib.model.GetClusterDataList
import org.unyde.mapintegrationlib.model.GetMallFloorList.GetMallFloorList
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

}
