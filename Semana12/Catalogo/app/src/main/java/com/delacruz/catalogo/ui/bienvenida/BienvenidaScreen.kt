package com.delacruz.catalogo.ui.bienvenida

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.delacruz.catalogo.R
import com.delacruz.catalogo.ui.components.FondoBeautyEmi
import com.delacruz.catalogo.ui.theme.Chocolate
import com.delacruz.catalogo.ui.theme.ChocolateText
import com.delacruz.catalogo.ui.theme.CreamSurface
import com.delacruz.catalogo.ui.theme.MutedBrown

@Composable
fun BienvenidaScreen(
    onIniciarSesion: () -> Unit,
    modifier: Modifier = Modifier
) {
    FondoBeautyEmi(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 36.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Ilustración principal
            Image(
                painter = painterResource(id = R.drawable.ic_ilustracion_bienvenida),
                contentDescription = "Ilustración BeautyEmi",
                modifier = Modifier
                    .fillMaxWidth(0.95f)
                    .height(180.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Título
            Text(
                text = "BeautyEmi",
                fontSize = 42.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif,
                color = ChocolateText
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Subtítulo
            Text(
                text = "Tu belleza, tu estilo ♡ explora el catalogo",
                style = MaterialTheme.typography.bodyMedium,
                color = MutedBrown
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Botón Iniciar Sesión
            Button(
                onClick = onIniciarSesion,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Chocolate,
                    contentColor = CreamSurface
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Iniciar sesión",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Flecha ir a login",
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}