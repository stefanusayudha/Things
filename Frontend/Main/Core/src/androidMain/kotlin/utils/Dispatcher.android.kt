package utils

import kotlinx.coroutines.Dispatchers

actual fun ioDispatchers(): kotlinx.coroutines.CoroutineDispatcher {
    return Dispatchers.IO
}