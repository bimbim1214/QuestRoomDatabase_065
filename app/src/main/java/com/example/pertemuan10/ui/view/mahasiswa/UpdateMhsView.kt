package com.example.pertemuan10.ui.view.mahasiswa

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pertemuan10.ui.costumwidget.appBar
import com.example.pertemuan10.ui.viewmodel.PenyediaViewModel
import com.example.pertemuan10.ui.viewmodel.UpdateMhsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun UpdateMhsView(
    onBack: () -> Unit,
    onNavigate: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: UpdateMhsViewModel = viewModel(factory = PenyediaViewModel.Factory) //inisialisasi Viewmodek
){
    val uiState = viewModel.updateUIState //ambil ui state dari viewmodel
    val snackbarHostState = remember { SnackbarHostState()} // Snackbar state
    val coroutineScope = rememberCoroutineScope()

    //observasi perubahan snackbarmesage

    LaunchedEffect(uiState.snackbarMessage) {
        println("LaunchedEffect triggered")
        uiState.snackbarMessage?.let { message ->
            println("Snackbar message recived : $message")
            coroutineScope.launch {
                println("Launching coroutine for snackbar")
                snackbarHostState.showSnackbar(
                    message = message,
                    duration = SnackbarDuration.Long
                )
                viewModel.resetSnackBarMessage()
            }
        }
    }

    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }, //tempatkan snackbar di scafold
        topBar = {
            appBar(
                judul = "Edit Mahasiswa",
                showBackButton = true,
                onBack = onBack,
                modifier = modifier
            )
        }
    ) { padding ->
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            //isi Body
            InsertBodyMhs(
                uiState = uiState,
                onValueChange = { updateEvent ->
                                viewModel.updateState(updateEvent) //Update state di viewModel
                },
                onClick = {
                    coroutineScope.launch {
                        if (viewModel.validateField()) {
                            viewModel.updateData()
                            delay(600)
                            withContext(Dispatchers.Main) {
                                onNavigate() //Navigate di main thread
                            }
                        }
                    }
                }
            )
        }
    }
}