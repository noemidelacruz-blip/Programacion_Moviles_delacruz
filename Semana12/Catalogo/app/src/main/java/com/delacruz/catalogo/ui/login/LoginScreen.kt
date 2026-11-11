package com.delacruz.catalogo.ui.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.delacruz.catalogo.R
import com.delacruz.catalogo.domain.model.Sesion
import com.delacruz.catalogo.ui.components.FondoBeautyEmi
import com.delacruz.catalogo.ui.theme.Chocolate
import com.delacruz.catalogo.ui.theme.ChocolateText
import com.delacruz.catalogo.ui.theme.CoralAccent
import com.delacruz.catalogo.ui.theme.CreamSurface
import com.delacruz.catalogo.ui.theme.MutedBrown
import com.delacruz.catalogo.ui.theme.OutlineSoft
import com.delacruz.catalogo.ui.theme.PeachSoft
import com.delacruz.catalogo.ui.theme.TerracottaError
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale

/**
 * Pantalla de login (hito H13).
 *
 * Solo observa el estado del ViewModel y llama a sus funciones.
 * Firebase no se toca desde aquí: lo hace FirebaseAuthRepository.
 */
@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onLoginExitoso: (Sesion) -> Unit,
    modifier: Modifier = Modifier,
    onVolver: (() -> Unit)? = null
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()

    // Cuando el ViewModel obtiene la sesión, se avisa a la navegación.
    LaunchedEffect(estado.sesion) {
        val sesion = estado.sesion
        if (sesion != null) {
            onLoginExitoso(sesion)
            viewModel.sesionConsumida()
        }
    }

    var verClave by remember { mutableStateOf(false) }

    FondoBeautyEmi(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 12.dp)
        ) {
            if (onVolver != null) {
                Surface(
                    shape = CircleShape,
                    color = CreamSurface,
                    shadowElevation = 2.dp
                ) {
                    IconButton(
                        onClick = onVolver,
                        modifier = Modifier.size(44.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_flecha_izquierda),
                            contentDescription = "Volver",
                            tint = Chocolate,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "BeautyEmi",
                fontSize = 42.sp,
                style = MaterialTheme.typography.headlineSmall,
                color = CoralAccent,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Bienvenido",
                style = MaterialTheme.typography.displaySmall,
                color = ChocolateText,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(15.dp))
            //imagen de login

            Image(
                painter = painterResource(id = R.drawable.ic_ilustracion_bienvenida),
                contentDescription = "Ilustración BeautyEmi",
                modifier = Modifier
                    .fillMaxWidth(0.95f)
                    .height(200.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Ingresa con tu cuenta para ver tu catálogo♡",
                style = MaterialTheme.typography.bodyMedium,
                color = MutedBrown
            )

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = estado.correo,
                onValueChange = viewModel::actualizarCorreo,
                label = { Text("Correo") },
                placeholder = { Text("tu@correo.com") },
                singleLine = true,
                enabled = !estado.cargando,
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_correo),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                },
                shape = RoundedCornerShape(18.dp),
                colors = coloresCampo(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(14.dp))

            OutlinedTextField(
                value = estado.clave,
                onValueChange = viewModel::actualizarClave,
                label = { Text("Contraseña") },
                singleLine = true,
                enabled = !estado.cargando,
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_candado),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { verClave = !verClave }) {
                        Icon(
                            painter = painterResource(
                                if (verClave) R.drawable.ic_ojo_off else R.drawable.ic_ojo
                            ),
                            contentDescription = if (verClave) "Ocultar" else "Ver",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                },
                visualTransformation = if (verClave) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                shape = RoundedCornerShape(18.dp),
                colors = coloresCampo(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = { viewModel.ingresar() }),
                modifier = Modifier.fillMaxWidth()
            )

            estado.error?.let { mensaje ->
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = TerracottaError.copy(alpha = 0.12f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 14.dp)
                ) {
                    Text(
                        text = mensaje,
                        style = MaterialTheme.typography.bodySmall,
                        color = TerracottaError,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            Button(
                onClick = viewModel::ingresar,
                enabled = !estado.cargando,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Chocolate,
                    contentColor = CreamSurface,
                    disabledContainerColor = Chocolate.copy(alpha = 0.5f),
                    disabledContentColor = CreamSurface.copy(alpha = 0.7f)
                )
            ) {
                if (estado.cargando) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = CreamSurface
                    )
                } else {
                    Text(
                        text = "Ingresar",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "¿Olvidades la contraseña?",
                    style = MaterialTheme.typography.bodySmall,
                    color = MutedBrown,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

/** Colores compartidos de los campos de texto del login. */
@Composable
private fun coloresCampo() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = Chocolate,
    unfocusedBorderColor = OutlineSoft,
    focusedContainerColor = CreamSurface.copy(alpha = 0.92f),
    unfocusedContainerColor = CreamSurface.copy(alpha = 0.78f),
    focusedLabelColor = Chocolate,
    unfocusedLabelColor = MutedBrown,
    cursorColor = Chocolate,
    focusedTextColor = ChocolateText,
    unfocusedTextColor = ChocolateText,
    focusedLeadingIconColor = Chocolate,
    unfocusedLeadingIconColor = MutedBrown,
    focusedTrailingIconColor = Chocolate,
    unfocusedTrailingIconColor = MutedBrown,
    focusedPlaceholderColor = MutedBrown.copy(alpha = 0.6f),
    unfocusedPlaceholderColor = MutedBrown.copy(alpha = 0.6f)
)
