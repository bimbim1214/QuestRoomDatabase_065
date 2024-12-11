package com.example.pertemuan10.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pertemuan10.data.entity.Mahasiswa
import com.example.pertemuan10.repository.RepositoryMhs
import kotlinx.coroutines.launch
import java.lang.Exception

class MahasiswaViewModel (private val repositoryMhs: RepositoryMhs) : ViewModel() {
    var uiState by mutableStateOf(MhsUIState())

    //memperrbarui state berdasarkan input pengguna
    fun updateState(mahasiswaEvent: MahasiswaEvent){
        uiState= uiState.copy(
            mahasiswaEvent = mahasiswaEvent,
        )
    }

    // validasi data input pengguna
    private fun validateField(): Boolean {
        val event = uiState.mahasiswaEvent
        val errorState = FormErrorState(
            nim = if (event.nim.isNotEmpty()) null else "Nim tidak boleh kosong",
            nama = if (event.nama.isNotEmpty())null else "nama tidak boleh kosong",
            jenisKelamin = if (event.nama.isNotEmpty())null else "nama tidak boleh kosong",
            alamat = if (event.nama.isNotEmpty())null else "nama tidak boleh kosong",
            kelas = if (event.nama.isNotEmpty())null else "nama tidak boleh kosong",
            angkatan = if (event.nama.isNotEmpty())null else "nama tidak boleh kosong"
        )
        uiState = uiState.copy(
            isEntryValid = errorState
        )

        return errorState.isValid()
    }

    //menyimpan data ke repository
    fun saveData(){
        val currentEvent = uiState.mahasiswaEvent

        if (validateField()) {
            viewModelScope.launch {
                try {
                    repositoryMhs.insertMhs(currentEvent.toMahasiswaEntity())
                    uiState = uiState.copy(
                        snackbarMessage = "Data berhasil disimpan",
                        mahasiswaEvent = MahasiswaEvent(),//reset input form
                        isEntryValid = FormErrorState() // Reset error state
                    )
                }catch (e: Exception) {
                    uiState = uiState.copy(
                        snackbarMessage = "Data gagal disimpan"
                    )
                }
            }
        }else{
            uiState = uiState.copy(
                snackbarMessage = "input tidak valid, periksa lagi data anda"
            )
        }
    }

    //reset pesan Snackbar setelah di tampilkan
    fun resetSnackBarMessage(){
        uiState = uiState.copy(snackbarMessage = null)
    }

    //bedanya stete dan event?
    //event adalah aksi yang terjadi
    //state adalah keadaan dari event yang terjadi
}

data class MhsUIState(
    val mahasiswaEvent: MahasiswaEvent = MahasiswaEvent(),
    val isEntryValid: FormErrorState = FormErrorState(),
    val snackbarMessage: String? = null,
)

data class FormErrorState(
    val nim: String? = null,
    val nama: String? = null,
    val jenisKelamin: String? = null,
    val alamat: String? = null,
    val kelas: String? = null,
    val angkatan: String? = null
) {
    fun isValid(): Boolean {
        return nim == null && nama == null && jenisKelamin == null && alamat == null && kelas == null && angkatan == null
    }
}


//menyimpan input form ke dalam entity
fun MahasiswaEvent.toMahasiswaEntity(): Mahasiswa = Mahasiswa(
    nim = nim,
    nama = nama,
    jenisKelamin = jenisKelamin,
    alamat = alamat,
    kelas = kelas,
    angkatan = angkatan
)

//data class varibael yang menyimpan data input form
data class MahasiswaEvent(
    val nim: String = "",
    val nama: String= "",
    val jenisKelamin: String= "",
    val alamat: String = "",
    val kelas: String = "",
    val angkatan: String = ""
)
