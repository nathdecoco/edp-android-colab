package com.example.myapplication.data.network.dto

import com.example.myapplication.data.local.MessageEntity
import com.example.myapplication.domain.Message
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.longOrNull
import java.time.Instant
import java.time.format.DateTimeParseException

fun MessageDto.toDomain(): Message {
    val time = when (val element = createdAt) {
        is JsonPrimitive -> {
            element.longOrNull ?: try {
                Instant.parse(element.content).toEpochMilli()
            } catch (e: DateTimeParseException) {
                0L
            }
        }
        else -> 0L
    }
    return Message(
        id = id ?: "",
        sender = sender ?: "Unknown",
        text = text ?: "",
        createdAt = time
    )
}

fun List<MessageDto>.toDomain(): List<Message> =
    map { it.toDomain() }

fun Message.toEntity(): MessageEntity = MessageEntity(
    id = id,
    sender = sender,
    text = text,
    createdAt = createdAt
)

fun MessageEntity.toDomain(): Message = Message(
    id = id,
    sender = sender,
    text = text,
    createdAt = createdAt
)
