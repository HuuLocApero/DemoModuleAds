package com.demo.data.app

import java.lang.Exception

data class AppError(var messagge: String = "", var ex: Exception? = null)
