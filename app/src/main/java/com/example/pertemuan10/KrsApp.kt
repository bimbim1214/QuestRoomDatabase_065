package com.example.pertemuan10

import android.app.Application
import com.example.pertemuan10.dependenciesinjection.ContainerApp
import com.example.pertemuan10.dependenciesinjection.InterfaceContainerApp

class KrsApp : Application() {
    //Fungsinya untuk menyimpan instance ContainerApp
    lateinit var containerApp: ContainerApp

    override fun onCreate() {
        super.onCreate()
        //membuat instance ContainerApp
        containerApp = ContainerApp(this)
        //instace adalah object yang dibuat dari class
    }
}