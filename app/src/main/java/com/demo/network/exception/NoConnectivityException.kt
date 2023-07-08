package com.demo.network.exception

import okio.IOException

class NoConnectivityException(message: String = "No internet connectivity!") : IOException(message)