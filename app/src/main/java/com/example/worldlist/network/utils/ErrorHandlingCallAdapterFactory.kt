package com.example.worldlist.network.utils

import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type


class ErrorHandlingCallAdapterFactory : CallAdapter.Factory() {
    override fun get(returnType: Type, annotations: Array<Annotation>, retrofit: Retrofit): CallAdapter<*, *>? {
        val responseBodyType = getParameterUpperBound(0, returnType as ParameterizedType)

        // We only want to apply this Adapter when the return type is ErrorHandlingCall.
        return if (getRawType(returnType) == ErrorHandlingCall::class.java) {
            Adapter<Any>(responseBodyType)
        } else {
            null
        }
    }

    private class Adapter<R : Any>(
        private val responseBodyType: Type
    ) : CallAdapter<R, ErrorHandlingCall<R>> {

        override fun responseType() = responseBodyType

        override fun adapt(call: Call<R>) = ErrorHandlingCall(call)
    }
}
