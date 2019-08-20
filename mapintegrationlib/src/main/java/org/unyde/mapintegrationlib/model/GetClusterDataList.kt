package org.unyde.mapintegrationlib.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


import java.util.ArrayList


class GetClusterDataList {

    @SerializedName("success")
    @Expose
    var success: Int? = null
    @SerializedName("response_code")
    @Expose
    var responseCode: Any? = null
    @SerializedName("message")
    @Expose
    var message: String? = null
    @SerializedName("data")
    @Expose
    var data: ArrayList<Data>? = null
}
