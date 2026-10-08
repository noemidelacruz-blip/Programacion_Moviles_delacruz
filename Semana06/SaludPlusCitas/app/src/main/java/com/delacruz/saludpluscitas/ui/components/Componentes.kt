package com.delacruz.saludpluscitas.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.SearchOff
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.foundation.layout.RowScope
import com.delacruz.saludpluscitas.data.model.Medico
import com.delacruz.saludpluscitas.ui.theme.Estrella
import com.delacruz.saludpluscitas.ui.theme.VerdePastel
import com.delacruz.saludpluscitas.ui.theme.VerdeTexto
import com.delacruz.saludpluscitas.ui.theme.FondoClaro
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChildCare
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.Healing
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.delacruz.saludpluscitas.data.model.Cita
import com.delacruz.saludpluscitas.data.model.Especialidad
import com.delacruz.saludpluscitas.data.repository.Repositorio
import com.delacruz.saludpluscitas.navigation.Rutas
import com.delacruz.saludpluscitas.ui.theme.AzulClaro
import com.delacruz.saludpluscitas.ui.theme.AzulPrimario
import com.delacruz.saludpluscitas.ui.theme.AzulPastel
import com.delacruz.saludpluscitas.ui.theme.BordeSuave
import com.delacruz.saludpluscitas.ui.theme.NaranjaPastel
import com.delacruz.saludpluscitas.ui.theme.NaranjaTexto
import com.delacruz.saludpluscitas.ui.theme.RojoPastel
import com.delacruz.saludpluscitas.ui.theme.RojoTexto
import com.delacruz.saludpluscitas.ui.theme.SuperficieBlanca
import com.delacruz.saludpluscitas.ui.theme.TextoSecundario

// Componentes reutilizables de la app.
// Pendientes (se crean junto con la pantalla que los usa):
//

// Botón azul redondeado de ancho completo ("Comenzar", "Registrarme", "Continuar"...).
@Composable
fun BotonPrimario(
    texto: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = AzulPrimario),
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
    ) {
        Text(text = texto, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
    }
}

// Campo de texto con el ícono en un recuadro a la izquierda (Registro y Login).
@Composable
fun CampoTexto(
    etiqueta: String,
    valor: String,
    onValorChange: (String) -> Unit,
    icono: ImageVector,
    modifier: Modifier = Modifier,
    error: String? = null,
    esPassword: Boolean = false,
    tipoTeclado: KeyboardType = KeyboardType.Text
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .padding(top = 8.dp)
                .size(48.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(AzulClaro),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icono,
                contentDescription = null,
                tint = AzulPrimario
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        OutlinedTextField(
            value = valor,
            onValueChange = onValorChange,
            label = { Text(etiqueta) },
            singleLine = true,
            isError = error != null,
            supportingText = error?.let { { Text(it) } },
            visualTransformation = if (esPassword) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = KeyboardOptions(
                keyboardType = if (esPassword) KeyboardType.Password else tipoTeclado
            ),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = AzulPrimario,
                unfocusedBorderColor = BordeSuave,
                focusedLabelColor = AzulPrimario
            ),
            modifier = Modifier.weight(1f)
        )
    }
}

// Texto normal seguido de un enlace azul ("¿Ya tienes cuenta? Iniciar sesión").
@Composable
fun TextoConEnlace(
    texto: String,
    enlace: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = texto,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = enlace,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = AzulPrimario,
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .clickable(onClick = onClick)
                .padding(4.dp)
        )
    }
}

// Tarjeta pastel del Inicio: ícono arriba y título abajo, del mismo color.
@Composable
fun TarjetaAccion(
    titulo: String,
    icono: ImageVector,
    colorFondo: Color,
    colorContenido: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = colorFondo),
        modifier = modifier.height(116.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = icono,
                contentDescription = null,
                tint = colorContenido,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = titulo,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = colorContenido
            )
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

// Ícono y colores de cada especialidad (el modelo no trae ícono).
data class EstiloEspecialidad(
    val icono: ImageVector,
    val colorFondo: Color,
    val colorIcono: Color
)

fun estiloEspecialidad(especialidadId: Int): EstiloEspecialidad {
    return when (especialidadId) {
        1 -> EstiloEspecialidad(Icons.Default.Person, AzulPastel, AzulPrimario)
        2 -> EstiloEspecialidad(Icons.Default.ChildCare, NaranjaPastel, NaranjaTexto)
        3 -> EstiloEspecialidad(Icons.Default.Female, RojoPastel, RojoTexto)
        4 -> EstiloEspecialidad(Icons.Default.Favorite, RojoPastel, RojoTexto)
        5 -> EstiloEspecialidad(Icons.Default.Spa, NaranjaPastel, NaranjaTexto)
        6 -> EstiloEspecialidad(Icons.Default.Healing, AzulPastel, AzulPrimario)
        7 -> EstiloEspecialidad(Icons.Default.Visibility, AzulPastel, AzulPrimario)
        else -> EstiloEspecialidad(Icons.Default.Person, AzulPastel, AzulPrimario)
    }
}

// Ícono de la especialidad dentro de un círculo de color.
@Composable
fun IconoEspecialidad(
    especialidadId: Int,
    modifier: Modifier = Modifier,
    tamano: Int = 48
) {
    val estilo = estiloEspecialidad(especialidadId)
    Box(
        modifier = modifier
            .size(tamano.dp)
            .clip(RoundedCornerShape(50))
            .background(estilo.colorFondo),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = estilo.icono,
            contentDescription = null,
            tint = estilo.colorIcono,
            modifier = Modifier.size((tamano / 2).dp)
        )
    }
}

// Tarjeta pequeña del LazyRow de especialidades destacadas.
@Composable
fun TarjetaEspecialidadDestacada(
    especialidad: Especialidad,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SuperficieBlanca),
        border = BorderStroke(1.dp, BordeSuave),
        modifier = modifier.size(width = 104.dp, height = 116.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(6.dp))
            IconoEspecialidad(especialidadId = especialidad.id)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = especialidad.nombre,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                lineHeight = 16.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

// Destino de la barra inferior.
data class DestinoBarra(
    val titulo: String,
    val icono: ImageVector,
    val ruta: String
)

private val destinosBarra = listOf(
    DestinoBarra("Inicio", Icons.Default.Home, Rutas.Home.ruta),
    DestinoBarra("Citas", Icons.Default.CalendarMonth, Rutas.MisCitas.ruta),
    DestinoBarra("Resultados", Icons.Default.Description, Rutas.Resultados.ruta),
    DestinoBarra("Perfil", Icons.Default.Person, Rutas.Perfil.ruta)
)

// NavigationBar con los 4 destinos principales. rutaActual marca cuál está activo.
@Composable
fun BarraNavegacion(
    navController: NavController,
    rutaActual: String
) {
    NavigationBar(containerColor = SuperficieBlanca) {
        destinosBarra.forEach { destino ->
            NavigationBarItem(
                selected = destino.ruta == rutaActual,
                onClick = {
                    if (destino.ruta != rutaActual) {
                        navController.navigate(destino.ruta) {
                            // Sin copias del mismo destino en la pila; Atrás vuelve al Inicio.
                            popUpTo(Rutas.Home.ruta)
                            launchSingleTop = true
                        }
                    }
                },
                icon = { Icon(imageVector = destino.icono, contentDescription = null) },
                label = { Text(text = destino.titulo, fontSize = 12.sp) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = AzulPrimario,
                    selectedTextColor = AzulPrimario,
                    indicatorColor = AzulClaro,
                    unselectedIconColor = TextoSecundario,
                    unselectedTextColor = TextoSecundario
                )
            )
        }
    }
}

// Barra superior de las vistas internas: flecha atrás, título centrado y acciones.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarraSuperior(
    titulo: String,
    onAtras: () -> Unit,
    acciones: @Composable RowScope.() -> Unit = {}
) {
    CenterAlignedTopAppBar(
        title = {
            Text(text = titulo, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        },
        navigationIcon = {
            IconButton(onClick = onAtras) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Atrás"
                )
            }
        },
        actions = acciones,
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = SuperficieBlanca
        )
    )
}

// Buscador con lupa y fondo gris claro ("Buscar especialidad...").
@Composable
fun CampoBusqueda(
    valor: String,
    onValorChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = valor,
        onValueChange = onValorChange,
        placeholder = { Text(placeholder, color = TextoSecundario) },
        leadingIcon = {
            Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = TextoSecundario)
        },
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = AzulPrimario,
            unfocusedBorderColor = BordeSuave,
            focusedContainerColor = FondoClaro,
            unfocusedContainerColor = FondoClaro
        ),
        modifier = modifier.fillMaxWidth()
    )
}

// Fila de la lista de especialidades: ícono de color, nombre, descripción y chevron.
@Composable
fun TarjetaEspecialidad(
    especialidad: Especialidad,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconoEspecialidad(especialidadId = especialidad.id)
        Spacer(modifier = Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = especialidad.nombre,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = especialidad.descripcion,
                fontSize = 13.sp,
                color = TextoSecundario
            )
        }
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = TextoSecundario
        )
    }
}

// Mensaje centrado cuando una lista no tiene elementos.
@Composable
fun EstadoVacio(
    mensaje: String,
    modifier: Modifier = Modifier,
    icono: ImageVector = Icons.Outlined.SearchOff
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icono,
            contentDescription = null,
            tint = TextoSecundario,
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = mensaje,
            fontSize = 15.sp,
            color = TextoSecundario,
            textAlign = TextAlign.Center
        )
    }
}