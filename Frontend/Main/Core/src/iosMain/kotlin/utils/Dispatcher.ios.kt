package utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

actual fun ioDispatchers(): CoroutineDispatcher {
    return Dispatchers.IO
}