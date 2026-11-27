package com.delacruz.avisoscampus.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class ModoAvionReceiver(
    private val alCambiar: (Boolean) -> Unit
) : BroadcastReceiver() {

    // onReceive debe ser corto: solo revisa el anuncio y avisa del cambio.
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_AIRPLANE_MODE_CHANGED) {
            alCambiar(intent.getBooleanExtra("state", false))
        }
    }
}
