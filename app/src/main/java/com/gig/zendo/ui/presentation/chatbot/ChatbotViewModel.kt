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

    fun send(prompt: String, houseId: String) {
        if (prompt.isBlank()) return

        // 1) Append user + bot placeholder
        val userMsg = ChatMessage(role = Role.USER, text = prompt)
        val botPlaceholder = ChatMessage(role = Role.BOT, text = "")

        _messages.value = _messages.value + userMsg + botPlaceholder

        viewModelScope.launch {
            // 2) Gọi non-stream (trả về String), có try/catch trong ChatRemoteDataSource
            val reply = ChatRemoteDataSource.chatOnce(prompt, houseId)

            // 3) Tìm placeholder BOT rỗng cuối cùng để thay nội dung
            val current = _messages.value.toMutableList()
            val idx = current.indexOfLast { it.role == Role.BOT && it.text.isEmpty() }
            if (idx >= 0) {
                current[idx] = ChatMessage(role = Role.BOT, text = reply)
                _messages.value = current
            } else {
                // Phòng hờ: nếu không tìm thấy placeholder (đã bị thay đổi do race condition)
                _messages.value = _messages.value + ChatMessage(role = Role.BOT, text = reply)
            }
        }
    }
}
