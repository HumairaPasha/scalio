package com.example.scalio.common.data.model

sealed class Response<T> {
    class Success<T>(val data: T) : Response<T>()
    class Failure<T>(val error: String) : Response<T>()

    override fun toString(): String {
        return when (this) {
            is Success<T> -> "data: [$data]"
            is Failure<T> -> "error: [$error]"
        }
    }
}