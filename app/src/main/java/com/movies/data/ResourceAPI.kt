package com.movies.data

data class ResourceAPI<out T>(val status: StatusAPI, val data: T?) {
    companion object {

        fun <T> success(data: T?): ResourceAPI<T> {
            return ResourceAPI(StatusAPI.SUCCESS, data)
        }

        fun <T> failed(data: T?): ResourceAPI<T> {
            return ResourceAPI(StatusAPI.FAILED, data)
        }

        fun <T> error(data: T?): ResourceAPI<T> {
            return ResourceAPI(StatusAPI.ERROR, data)
        }

        fun <T> loading(data: T?): ResourceAPI<T> {
            return ResourceAPI(StatusAPI.LOADING, data)
        }

        fun <T> networkError(data: T?): ResourceAPI<T> {
            return ResourceAPI(StatusAPI.NETWORK_ERROR, data)
        }

    }
}
