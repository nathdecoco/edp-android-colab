package com.example.myapplication.data.repository

import com.example.myapplication.core.AppResult
import com.example.myapplication.data.local.MessageDao
import com.example.myapplication.data.network.ChatApiService
import com.example.myapplication.data.network.dto.NewMessageDto
import com.example.myapplication.data.network.dto.toDomain
import com.example.myapplication.data.network.dto.toEntity
import com.example.myapplication.domain.ChatRepository
import com.example.myapplication.domain.Message
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class ChatRepositoryImpl(
    private val api: ChatApiService,
    private val dao: MessageDao
) : ChatRepository {

    override suspend fun getMessages(): AppResult<List<Message>> {
        val remoteResult = safeCall { api.getMessages().toDomain() }
        
        if (remoteResult is AppResult.Success) {
            safeCall { dao.insertAll(remoteResult.data.map { it.toEntity() }) }
            return remoteResult
        }
        
        val localResult = safeCall { dao.getAll().map { it.toDomain() } }
        return if (localResult is AppResult.Success && localResult.data.isNotEmpty()) {
            localResult
        } else {
            remoteResult
        }
    }

    override suspend fun sendMessage(sender: String, text: String): AppResult<Unit> =
        safeCall {
            val dto = NewMessageDto(
                sender = sender,
                text = text,
                createdAt = System.currentTimeMillis()
            )
            api.sendMessage(dto)
        }

    private suspend inline fun <T> safeCall(crossinline block: suspend () -> T): AppResult<T> =
        try {
            AppResult.Success(block())
        } catch (e: UnknownHostException) {
            AppResult.Failure.NoInternet
        } catch (e: SocketTimeoutException) {
            AppResult.Failure.Timeout
        } catch (e: IOException) {
            AppResult.Failure.NoInternet
        } catch (e: Exception) {
            if (e is kotlinx.coroutines.CancellationException) throw e
            AppResult.Failure.Unknown(e.message)
        }
}
