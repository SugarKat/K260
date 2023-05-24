package com.natureclean.proto

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import com.codelab.android.datastore.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import java.io.IOException

class DataStoreRepository(context: Context) {

    private var userStore: DataStore<User> = context.userStore

    val userFlow: Flow<User> = userStore.data
        .catch { exception ->
            // dataStore.data throws an IOException when an error is encountered when reading data
            if (exception is IOException) {
                Log.e("Error", "Error reading sort order preferences.", exception)
                emit(User())
            } else {
                throw exception
            }
        }

    suspend fun updateUser(user: User?) {
        userStore.updateData {
            user ?: User()
        }
    }
}