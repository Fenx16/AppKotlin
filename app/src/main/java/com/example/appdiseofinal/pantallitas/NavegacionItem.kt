package com.example.appdiseofinal.pantallitas

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.appdiseofinal.R

sealed class NavegacionItem(var route: String, var icon: Int, var title: String)
{
    object Home : NavegacionItem("Lista Campeon", R.drawable.ic_home, "Lista de campeones")
    object añadecampeon : NavegacionItem("Añade campeon", android.R.drawable.stat_notify_more, "Añade un campeon")
    object borraelcampeon : NavegacionItem("borra la camepon", android.R.drawable.btn_dialog, "Borra un campeon")
    object interneto : NavegacionItem("WebView", android.R.drawable.ic_search_category_default, "WebView")

}