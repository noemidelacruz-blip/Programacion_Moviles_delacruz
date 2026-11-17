package com.delacruz.campusgps.data

import android.annotation.SuppressLint
import android.content.Context
import com.delacruz.campusgps.model.Posicion
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.tasks.await

// Conecta la app con el sensor GPS real del dispositivo usando Google Play Services
class UbicacionRepositoryGoogle(context: Context) : UbicacionRepository {
    // Cliente oficial de Google que combina GPS, Wi-Fi y antenas para dar la posición exacta:
    private val cliente = LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    override suspend fun obtenerPosicion(): Result<Posicion> = try {
        // Pide la ubicación actual con máxima precisión (PRIORITY_HIGH_ACCURACY)
        val ubicacion = cliente
            .getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
            .await()

        if (ubicacion != null) {
            // Devuelve la latitud y longitud reales obtenidas del teléfono
            Result.success(Posicion(ubicacion.latitude, ubicacion.longitude))
        } else {
            Result.failure(
                IllegalStateException("No se pudo obtener la ubicación. Activa el GPS e intenta de nuevo")
            )
        }
    } catch (e: SecurityException) {
        Result.failure(e)
    } catch (e: Exception) {
        Result.failure(e)
    }
}
