package org.unyde.mapintegrationlib.model.GetMallFloorList

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class MallFloorData {


    @SerializedName("status")
    @Expose
    var status: Int? = null

    @SerializedName("floors")
    @Expose
    var floors: List<MallFloorList>? = null


}
