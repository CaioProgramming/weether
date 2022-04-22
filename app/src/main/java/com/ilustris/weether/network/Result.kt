package com.ilustris.weether.network

import java.lang.Exception

sealed class Result<out L, out R> {
    data class Error<out L>(val exception: Exception) : Result<L, Nothing>()
    data class Success<out R>(val data : R) : Result<Nothing, R>()
    val success get() = this as Success
    val error get() = this as Error
}
