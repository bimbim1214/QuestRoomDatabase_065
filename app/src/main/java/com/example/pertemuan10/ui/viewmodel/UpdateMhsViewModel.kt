package com.example.pertemuan10.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pertemuan10.data.entity.Mahasiswa
import com.example.pertemuan10.repository.RepositoryMhs
import com.example.pertemuan10.ui.navigation.DestinasiUpdate
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class UpdateMhsViewModel(
    savedStateHandle: SavedStateHandle,
    private val repositoryMhs: RepositoryMhs
) : ViewModel() {
    var updateUIState by mutableStateOf(MhsUIState())
        private set
    private val _nim: String = checkNotNull(savedStateHandle[DestinasiUpdate.NIM])

    init {
        viewModelScope.launch {
            updateUIState = repositoryMhs.getMhs(_nim)
                .filterNotNull()
                .first()
                .toUIStateMhs()
        }
    }

    fun updateState(mahasiswaEvent: MahasiswaEvent) {
        updateUIState = updateUIState.copy(
            mahasiswaEvent = mahasiswaEvent,
        )
    }

    fun validateField(): Boolean {
        val event = updateUIState.mahasiswaEvent
        val errorState = FormErrorState(
            nim = if (event.nim.isNotEmpty()) null else "Nim tidak boleh kosongn",
            nama = if (event.nama.isNotEmpty()) null else "Nama tidak boleh kosongn",
            jenisKelamin =  if (event.jenisKelamin.isNotEmpty()) null else "jenis kelamin tidak boleh kosongn",
            kelas =  if (event.kelas.isNotEmpty()) null else "kelas tidak boleh kosongn",
            angkatan =  if (event.angkatan.isNotEmpty()) null else "angkatan tidak boleh kosongn",
        )
        updateUIState = updateUIState.copy(isEntryValid = errorState)
        return errorState.isValid()
    }

    fun updateData() {
        val currenEvent = updateUIState.mahasiswaEvent

        if (validateField()) {
            viewModelScope.launch {
                try {
                    repositoryMhs.updateMhs(currenEvent.toMahasiswaEntity())
                    updateUIState = updateUIState.copy(
                        snackbarMessage = "Data berhasil diupdate",
                        mahasiswaEvent = MahasiswaEvent(),
                        isEntryValid = FormErrorState()
                    )
                    println("snackBarMessage diatur: ${updateUIState.snackbarMessage}")
                }catch (e: Exception) {
                   updateUIState = updateUIState.copy(
                       snackbarMessage = " Data gagal diupdate"
                   )
                }
            }
        } else {
            updateUIState = updateUIState.copy(
                snackbarMessage = "Data gagal diupdate"
            )
        }
    }
    fun resetSnackBarMessage() {
        updateUIState = updateUIState.copy(snackbarMessage = null)
    }
}

fun Mahasiswa.toUIStateMhs(): MhsUIState = MhsUIState(
    mahasiswaEvent = this.toDetailUiEvent(),
)