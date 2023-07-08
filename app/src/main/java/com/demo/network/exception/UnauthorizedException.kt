package com.demo.network.exception

import java.lang.Exception

class UnauthorizedException(message: String = "Unauthorized!") : Exception(message)