package com.movies.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


open class BaseResponse : Serializable {

    @SerializedName("Response")
    @Expose
    var responseReturn: Boolean? = false

}