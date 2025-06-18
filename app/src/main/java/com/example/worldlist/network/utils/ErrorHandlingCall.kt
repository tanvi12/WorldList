package com.example.worldlist.network.utils

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.Request
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class ErrorHandlingCall<T : Any>(
    private val call: Call<T>
) {
    companion object {
        private const val MAX_RETRIES = 3
    }

    /**
     * Suspends the current coroutine, enqueues the OkHttp network call, and validates the response.
     *
     * If the suspended coroutine gets cancelled, then the network call will be cancelled as well.
     * The coroutine is resumed when the network call gets a response or fails.
     *
     * @return The response body that has been deserialized to an object of type <T>
     */
    suspend fun await(): T = validateResponse(sendRequest())

    /**
     * Suspends the current coroutine and enqueues the OkHttp network call with a transformed
     * request. This can be used to inject headers into the request.
     *
     * If the suspended coroutine gets cancelled, then the network call will be cancelled as well.
     * The coroutine is resumed when the network call gets a response or fails.
     *
     * @return Retrofit response
     */
    suspend fun sendRequest(): Response<T> = try {
        executeCancellableCall(call)
    } catch (e: Exception) {
        if (e is CancellationException) throw e
        // Additional exception handling
        else throw e
    }

    /**
     * Suspends the current coroutine and enqueues the given OkHttp network call.
     *
     * If the suspended coroutine gets cancelled, then the network call will be cancelled as well.
     * The coroutine will be resumed when the network call gets a response or fails.
     *
     * @return Retrofit response
     */
    private suspend fun executeCancellableCall(call: Call<T>): Response<T> = suspendCancellableCoroutine { continuation ->

        var retryCount = 0

        continuation.invokeOnCancellation {
            if (continuation.isCancelled) {
                try {
                    // If the continuation got cancelled, then cancel the network call as well
                    call.cancel()
                } catch (throwable: Throwable) {
                    // Ignore cancel exception
                }
            }
        }

        call.enqueue(
            object : Callback<T> {

                override fun onResponse(call: Call<T>, response: Response<T>) {
                    if (continuation.isCancelled) {
                        // If the continuation was cancelled, then ignore the response
                        return
                    }

                    continuation.resume(response)
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    if (continuation.isCancelled) {
                        // If the continuation was cancelled, then ignore the failure
                        return
                    }

                    if (retryCount++ < MAX_RETRIES) {
                        call.clone().enqueue(this)
                    } else {
                        continuation.resumeWithException(t)
                    }
                }
            }
        )
    }


    private fun validateResponse(response: Response<T>): T {

        if (response.isSuccessful) {
            return response.body()
                ?: throw NetworkCommunicationException("Unexpected response content")
        } else {
            throw NetworkCommunicationException("Unsuccessful response from ${(call.request() as Request).url}: ${response.code()} / ${response.message()}")
        }
    }
}
