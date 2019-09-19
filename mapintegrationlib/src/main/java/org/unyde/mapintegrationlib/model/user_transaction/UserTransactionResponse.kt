package org.unyde.mapintegrationlib.model.user_transaction

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


import java.util.ArrayList


class UserTransactionResponse {

    @SerializedName("status")
    @Expose
    var status: String? = null
    @SerializedName("message")
    @Expose
    var message: String? = null
}
