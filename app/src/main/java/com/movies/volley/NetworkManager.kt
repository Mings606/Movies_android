package com.movies.volley

import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.StringRequest
import com.google.gson.Gson
import com.movies.MyApplication
import com.movies.data.BaseResponse
import com.movies.data.ResourceAPI


class NetworkManager {

    interface OnRequestListener {
        fun onRequest(html: String, repository: ResourceAPI<Any>)
    }

    fun callAPI(
        apiUrl: String,
        header: HashMap<String, String>,
        body: HashMap<String, String>,
        listener: OnRequestListener,
        method: Int = Request.Method.POST
    ) {

        var url = apiUrl

        if (method == Request.Method.GET) {
            url = "$apiUrl&"
            for (item in body) {
                url = "$url${item.key}=${item.value}&"
            }
            url = url.substring(0, url.length - 1)
        }

        Log.d("NetworkManager", "url: $url")

        val volleyEnrollRequest = object : StringRequest(method, url,

            Response.Listener { response ->
                listener.onRequest(response, repositoryResult(url, header.toString(), body.toString(), response))
            },

            Response.ErrorListener { error ->
                listener.onRequest("VolleyError", repositoryErrorResult(url, header.toString(), body.toString(), error))
            }

        ) {

            override fun getHeaders(): Map<String, String> {
                return header
            }

            override fun getParams(): Map<String, String> {
                return body
            }

        }

        VolleySingleton.getInstance(MyApplication.contextApp).addToRequestQueue(volleyEnrollRequest)
    }

    fun repositoryResult(url: String, header: String, body: String, html: String): ResourceAPI<Any> {

        var result: ResourceAPI<Any>

        try {

            result = if (html.isNotEmpty()) {

                val response = Gson().fromJson(html, BaseResponse::class.java)

                if (response.responseReturn == true) {
                    ResourceAPI.success(response)
                } else {
                    ResourceAPI.error(response)
                }

            } else {
                ResourceAPI.error(null)
            }

        } catch (e: Exception) {
            result = ResourceAPI.error(null)
            e.printStackTrace()
        }

        if (result == ResourceAPI.loading(null)) {
            result = ResourceAPI.error(null)
        }

        return result
    }


    fun repositoryErrorResult(url: String, header: String, body: String, error: VolleyError): ResourceAPI<Any> {

        var result: ResourceAPI<Any>

        try {

            val data = java.lang.String(error.networkResponse.data, HttpHeaderParser.parseCharset(error.networkResponse.headers))
                .toString()

            try {
                result = if (data.isNotEmpty()) {

                    val response = Gson().fromJson(data, BaseResponse::class.java)

                    if (response.responseReturn == true) {
                        ResourceAPI.success(response)
                    } else {
                        ResourceAPI.error(response)
                    }

                } else {
                    ResourceAPI.error(null)
                }

            } catch (e: Exception) {
                result = ResourceAPI.networkError(null)
                e.printStackTrace()
            }

        } catch (e: Exception) {

            val message = error.message ?: ""

            e.printStackTrace()
            result = ResourceAPI.networkError(null)
        }

        if (result == ResourceAPI.loading(null)) {
            result = ResourceAPI.networkError(null)
        }

        return result
    }

    companion object {
        val instance: NetworkManager by lazy {
            NetworkManager()
        }
    }

}