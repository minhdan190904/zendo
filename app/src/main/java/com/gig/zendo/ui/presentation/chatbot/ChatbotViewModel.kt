package com.gig.zendo.ui.presentation.chatbot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gig.zendo.data.remote.ChatRemoteDataSource
import com.gig.zendo.domain.model.ChatMessage
import com.gig.zendo.domain.model.Role
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ChatbotViewModel : ViewModel() {

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages

    fun send(prompt: String) {
        if (prompt.isBlank()) return

        _messages.value += ChatMessage(role = Role.USER, text = prompt)
        _messages.value += ChatMessage(role = Role.BOT, text = "")

        viewModelScope.launch {
            val builder = StringBuilder()
            ChatRemoteDataSource.chatStream(prompt).collect { chunk ->
                builder.append(chunk)
                val botMsg = ChatMessage(role = Role.BOT, text = builder.toString())

                //replace the last bot message with the updated text
                _messages.value = _messages.value.map { msg ->
                    if (msg.role == Role.BOT && msg.text.isEmpty()) {
                        botMsg
                    } else {
                        msg
                    }
                }
            }
        }
    }
}
