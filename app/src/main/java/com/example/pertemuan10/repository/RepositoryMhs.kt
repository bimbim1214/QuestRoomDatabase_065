package com.example.pertemuan10.repository

import com.example.pertemuan10.data.entity.Mahasiswa
import com.example.pertemuan10.ui.viewmodel.FormErrorState
import kotlinx.coroutines.flow.Flow

interface RepositoryMhs {
    suspend fun insertMhs(mahasiswa: Mahasiswa)
    //getAllMhs
    fun getAllMhs() : Flow<List<Mahasiswa>>

}