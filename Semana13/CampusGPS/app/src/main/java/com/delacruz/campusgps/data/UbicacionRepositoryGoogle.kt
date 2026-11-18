package com.delacruz.campusgps.data

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Looper
import androidx.core.content.ContextCompat
import com.delacruz.campusgps.model.Posicion
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

// Conecta la app con el sensor GPS real del dispositivo usando Google Play Services
class UbicacionRepositoryGoogle(private val context: Context) : UbicacionRepository {
    // Cliente oficial de Google que combina GPS, Wi-Fi y antenas para dar la posición exacta:
    private val cliente = LocationServices.getFusedLocationProviderClient(context)

    // Revisa si el dispositivo tiene Google Play Services antes de intentar ubicarse
    override fun playServicesDisponible(): Boolean =
        GoogleApiAvailability.getInstance()
            .isGooglePlayServicesAvailable(context) == ConnectionResult.SUCCESS

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

    // Seguimiento en vivo: los callbacks del GPS se convierten en un Flow con callbackFlow.
    // El Flow emite una Posicion cada vez que el teléfono reporta un movimiento y, al
    // cancelarse la colección (botón "Detener"), awaitClose libera las actualizaciones.
    @SuppressLint("MissingPermission")
    override fun observarUbicacion(): Flow<Posicion> = callbackFlow {
        if (!tienePermisoDeUbicacion()) {
            close(SecurityException("Sin permiso de ubicación"))
            return@callbackFlow
        }

        // Cada 3 segundos, sin pedir actualizaciones más seguidas que cada 2 segundos
        val pedido = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 3_000L)
            .setMinUpdateIntervalMillis(2_000L)
            .build()

        val callback = object : LocationCallback() {
            override fun onLocationResult(resultado: LocationResult) {
                resultado.lastLocation?.let { ubicacion ->
                    trySend(Posicion(ubicacion.latitude, ubicacion.longitude))
                }
            }
        }

        cliente.requestLocationUpdates(pedido, callback, Looper.getMainLooper())

        awaitClose {
            cliente.removeLocationUpdates(callback)
        }
    }

    private fun tienePermisoDeUbicacion(): Boolean =
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
}
