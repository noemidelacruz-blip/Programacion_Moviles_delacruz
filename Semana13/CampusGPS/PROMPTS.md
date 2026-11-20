# PROMPTS.md — Mejoras con IA (Fase 2)

Rama: `con-ia`. Cada mejora se trabajó con un prompt y se cerró en un commit
propio, sin tocar nada fuera de las capas indicadas.

Los prompts están tal como se le pasaron al asistente. Entre corchetes se anotan
las decisiones que se tomaron al implementarlos y los problemas que aparecieron.

---

## Mejora 1 — Mapa en modo oscuro

### Prompt

```text
Mejora: Mapa en modo oscuro.
Qué debe lograr: Estilo oscuro del mapa con MapStyleOptions cuando el tema de la
app sea oscuro.
Capas que cambian: View
```

### Qué se implementó

| Archivo | Cambio |
| --- | --- |
| `ui/theme/Theme.kt` | `darkColorScheme` y `CampusGPSTheme` elige esquema con `isSystemInDarkTheme()` |
| `ui/theme/Color.kt` | Paleta oscura: fondo `0xFF140E11`, superficie `0xFF1E1519`, primario `0xFFF3A7C4`, error `0xFFFFB4A9` |
| `res/raw/mapa_oscuro.json` | Estilo nocturno del mapa con tinte vino |
| `ui/MapaUbicacion.kt` | Carga el JSON con `remember(oscuro)` y lo pasa en `MapProperties(mapStyleOptions = ...)` |
| `res/values-night/` | Tema y `windowBackground` oscuros, para que el arranque no destelle en blanco |

### Decisiones y problemas

- En maps-compose 8.3.1 el composable `GoogleMap` **no** recibe `mapStyleOptions`
  como parámetro: va dentro de `MapProperties`. Se verificó con `javap` sobre el
  AAR que existe `MapProperties.getMapStyleOptions()` y que `MapUpdater` aplica
  `setMapStyle`, así que al cambiar de tema el mapa se actualiza en vivo.
- La app estaba forzada a tema claro (`EsquemaClaro` fijo), así que "cuando el
  tema de la app sea oscuro" exigía primero crear el esquema oscuro.
- El JSON es un recurso de texto: no agrega dependencias ni imágenes.
- Los botones `+` / `−` del mapa los dibuja el SDK de Google y `MapStyleOptions`
  no los alcanza; quedan claros en ambos temas.
- Si el JSON fallara, `runCatching` devuelve `null` y el mapa sigue con su estilo
  normal en lugar de romperse.

**Evidencia:** `docs/capturas/07-modo-oscuro.png`, `08-error-oscuro.png`.

---

## Mejora 2 — Filtrar por categoría

### Prompt

```text
Mejora: Filtrar por categoría.
Qué debe lograr: Chips (Estudio, Comida, Eventos, Servicios) que muestran solo
esos lugares en el mapa y en la lista.
Capas que cambian: ViewModel + View
```

### Qué se implementó

| Archivo | Cambio |
| --- | --- |
| `viewmodel/MapaViewModel.kt` | `_todos` (lista completa) + `_categoria` (`null` = sin filtro); `lugares = combine(_todos, _categoria)`; `categorias` y `filtrarPorCategoria()`; `masCercano` sale de la lista filtrada |
| `ui/ChipsCategoria.kt` | Chips compactos de categoría; el activo va con `primary` y el inactivo solo con borde |
| `ui/PantallaMapa.kt` | Coloca los chips entre los botones y la lista, solo en el panel normal |

### Decisiones y problemas

- Mapa y lista leen **la misma** `StateFlow` filtrada: así no pueden quedar
  desincronizados (el mapa sin un lugar que sí está en la lista, o al revés).
- `filtrarPorCategoria` alterna: tocar el chip activo lo apaga y vuelven los 5.
  No hizo falta un chip "Todos".
- Si el lugar seleccionado deja de verse con el filtro, se limpia la selección
  para que "Ir al más cercano" no apunte a una tarjeta invisible.
- Los `FilterChip` de Material no cabían cuatro en 360 dp ("Servicios" quedaba
  cortado), así que se hicieron chips propios con `Surface` y `bodySmall`.

**Evidencia:** `docs/capturas/09-filtro-comida.png`, `10-filtro-estudio.png`,
`11-filtro-oscuro.png`.

---

## Mejora 3 — Seguimiento en vivo

### Prompt

```text
Mejora: Seguimiento en vivo.
Qué debe lograr: Actualizar la ubicación mientras caminas con
requestLocationUpdates convertido en un Flow (callbackFlow), con botón
Iniciar/Detener.
Capas que cambian: Repository + ViewModel + View
```

### Qué se implementó

| Archivo | Cambio |
| --- | --- |
| `data/UbicacionRepository.kt` | Nueva función `observarUbicacion(): Flow<Posicion>` |
| `data/UbicacionRepositoryGoogle.kt` | `callbackFlow` con `LocationRequest` (3 s, mínimo 2 s) y `awaitClose { removeLocationUpdates(callback) }` |
| `viewmodel/MapaViewModel.kt` | `siguiendo: StateFlow<Boolean>`, `iniciarSeguimiento()`, `detenerSeguimiento()`, `alternarSeguimiento()`; cada posición actualiza `estado` y recalcula distancias; `buscar()` detiene el seguimiento |
| `ui/PantallaMapa.kt` | Botón **Seguir en vivo / Detener** junto a "Ir al más cercano" y línea "Seguimiento en vivo: actualizando cada 3 s" |

### Decisiones y problemas

- El Flow se libera con `awaitClose`, así al cancelar la colección se llama a
  `removeLocationUpdates`: no queda el GPS encendido de fondo.
- El `Job` se guarda en el ViewModel y se cancela al pulsar **Detener**, al
  buscar una posición puntual o al cerrar la pantalla (`viewModelScope`).
- `invokeOnCompletion` apaga el estado del botón tanto si se detiene a mano como
  si el flujo termina por un error.
- El primer toque de prueba cayó fuera del botón porque al activar el
  seguimiento aparece una línea más y el panel crece hacia arriba; se ubicó con
  `uiautomator dump` en vez de suponer las coordenadas.
- Limitación conocida: con el GPS apagado el flujo no emite nada, así que la
  pantalla se queda en "Buscando tu ubicación..." hasta pulsar **Detener**. El
  aviso de "Activa el GPS" sigue saliendo en la búsqueda puntual.

**Evidencia:** `docs/capturas/12-seguimiento-vivo.png` y el Logcat:

```text
D CampusGPS: Lugares ordenados: Laboratorio de Cómputo (9.9 km), ...
D CampusGPS: Lugares ordenados: Laboratorio de Cómputo (9.9 km), ...   <- 3 s después
```

---

## Verificación en el teléfono

| Prueba | Resultado |
| --- | --- |
| Tema claro / oscuro del sistema | Mapa, panel, tarjetas y olas cambian; el mapa usa el estilo nocturno |
| Estado de error en oscuro | El contenido sigue arriba bajo la cabecera, sin panel blanco |
| Chip "Comida" | Mapa con 1 marcador verde y lista con solo la Cafetería |
| Chip "Estudio" | 2 lugares en mapa y lista; el más cercano pasa a ser el Laboratorio |
| Tocar el chip activo otra vez | Vuelven los 5 lugares y el más cercano global |
| **Seguir en vivo** | Latitud/longitud cambian solas (`-12.0332289` → `-12.0332037`) y el Logcat registra el recálculo cada ~3 s |
| **Detener** | Botón vuelve a **Seguir en vivo**, desaparece la línea de seguimiento y no hay más entradas en Logcat en 12 s |

Todo se probó en un teléfono físico (Huawei WDY-LX3, Android 14) con el GPS real,
por eso las distancias rondan los 10 km: el equipo está lejos del punto base del
campus.

---

## Nota sobre el rediseño posterior

Después de cerrar estas tres mejoras se reorganizó la interfaz en dos pestañas
(**Mapa** y **Categorías**), la barra inferior quedó en 56 dp, las olas pasaron a
ser un solo `OlasInferiores` compartido y los chips ganaron la opción **Todos** en
un `FlowRow`. Las capturas de `docs/capturas/` se volvieron a tomar con ese
diseño, así que algunos detalles del texto de arriba (el panel blanco, la línea
"Seguimiento en vivo: actualizando cada 3 s" o la ausencia del chip **Todos**)
describen cómo estaba la pantalla al cerrar cada commit, no la pantalla final.
