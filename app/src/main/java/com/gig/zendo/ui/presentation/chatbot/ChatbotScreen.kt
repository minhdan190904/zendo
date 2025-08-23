package com.gig.zendo.ui.presentation.chatbot

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.gig.zendo.domain.model.Role

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatbotScreen(
    navController: NavController,
    vm: ChatbotViewModel = viewModel()
) {
    val messages by vm.messages.collectAsState()
    var input by remember { mutableStateOf("") }

    val listState = rememberLazyListState()

    LaunchedEffect(messages) {
        if (messages.isNotEmpty())
            listState.animateScrollToItem(messages.lastIndex)
    }

    Scaffold(
        topBar = { ToolbarMessage { navController.popBackStack() } },

        bottomBar = {
            WriteMessageCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .imePadding()
                    .animateContentSize()
                    .padding(16.dp),
                value = input,
                onValueChange = { input = it },
                onClickSend = {
                    vm.send(input.trim())
                    input = ""
                }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 20.dp)
            ) {
                items(messages) { msg ->
                    if (msg.role == Role.USER) {
                        MessengerItemCard(
                            modifier = Modifier
                                .padding(horizontal = 16.dp, vertical = 4.dp),
                            message = msg.text
                        )
                    } else {
                        ReceiverMessageItemCard(
                            modifier = Modifier
                                .padding(horizontal = 16.dp, vertical = 4.dp),
                            message = msg.text,
                            isLoading = msg.text.isEmpty()
                        )
                    }
                }
            }
        }
    }
}
