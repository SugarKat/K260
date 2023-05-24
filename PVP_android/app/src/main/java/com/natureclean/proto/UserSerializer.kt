package com.natureclean.proto

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.codelab.android.datastore.User
import java.io.InputStream
import java.io.OutputStream

object UserSerializer : Serializer<User> {
    override val defaultValue: User = User()

    override suspend fun readFrom(input: InputStream): User {
        try {
            return User.ADAPTER.decode(input)
        } catch (exception: Exception) {
            throw CorruptionException("Cannot read data", exception)
        }
    }

    override suspend fun writeTo(t: User, output: OutputStream) = User.ADAPTER.encode(output, t)
}

val Context.userStore: DataStore<User> by dataStore(
    fileName = "user_data.pb",
    serializer = UserSerializer
)