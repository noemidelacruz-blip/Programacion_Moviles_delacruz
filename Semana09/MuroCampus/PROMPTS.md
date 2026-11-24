# PROMPTS.md — Fase 2 (con IA)

**Nombre:** Noemi De la Cruz
**Curso:** Programación en Móviles — Semana 09
**Rama de trabajo:** `con-ia` (Fase 2, con IA)
**Proyecto:** MuroCampus (`com.delacruz.murocampus`)

Mejoras elegidas de la tabla de la guía (3):

1. **Borrar publicación** — API + Repository + ViewModel + View
2. **Búsqueda** — ViewModel + View
3. **Encabezado con interceptor** — RetrofitClient

Regla cumplida: al menos una mejora cambia el Repository o la interface de la API
(Borrar agrega `eliminar()` a `PostRepository` y `@DELETE` a `PostApi`).

## Tabla de prompts

| Prompt que escribí | Qué respondió la IA | Qué cambié o descarté | Por qué |
|---|---|---|---|
| Agrega el botón **Eliminar** en el detalle con `@DELETE("posts/{id}")` y `Response<Unit>`. Revisa el código de respuesta en el Repository y mantén MVVM + Repository: el ViewModel no debe ver Retrofit ni HttpException y la pantalla no debe llamar a la API. | Propuso el endpoint `@DELETE` que devuelve `Response<Unit>`, un `eliminar(id): Result<Unit>` en la interface del Repository, un `StateFlow` para la eliminación y el botón con diálogo de confirmación. | Dejé la revisión de `isSuccessful` dentro de `PostRepositoryRemote`, que lanza `HttpException` para que el ViewModel solo reciba `Result`. Agregué el `AlertDialog`, el aviso rosa de error, el indicador de progreso y el ícono de basura dibujado con Canvas. Como JSONPlaceholder responde 200 pero no guarda el borrado, quito la publicación de la lista en memoria con `filterNot`. | Mantener las capas: el ViewModel solo conoce la interface y la pantalla nunca arma la petición. No agregar dependencias de íconos. El servidor de práctica no persiste el DELETE, así que el muro debe reflejarlo igual. |
| Agrega un campo de **búsqueda** que filtre por título las publicaciones ya descargadas, sin volver a llamar a la API, y que funcione junto con los chips de usuario. | Propuso filtrar dentro del Composable con `remember` y una lista derivada en la pantalla. | Lo moví al ViewModel: `_busqueda` y `listaVisible` con `combine` + `stateIn`. Agregué el contador de coincidencias, el aviso cuando no hay resultados, el aspa para limpiar y `imePadding` para el teclado. | La pantalla solo debe dibujar: el filtrado es lógica y va en el ViewModel. Así el texto y los resultados se conservan al abrir un detalle y volver. |
| Agrega un **Interceptor** de OkHttp en `RetrofitClient` que ponga el encabezado `Accept: application/json` en todas las peticiones. | Propuso una clase que implementa `Interceptor`, copia la petición con `newBuilder().addHeader("Accept", ...)` y se registra en el `OkHttpClient`. | Usé `header()` en lugar de `addHeader()` para reemplazar y no duplicar el encabezado, y lo registré **antes** del `HttpLoggingInterceptor` para que el registro de OkHttp muestre el `Accept`. No toqué `PostApi`, Repository, ViewModel ni pantallas. | La tabla de la Fase 2 indica que esta mejora cambia solo `RetrofitClient`. Un encabezado duplicado puede confundir al servidor. |
| Rediseña la interfaz con la paleta morada y amarilla y una cabecera de olas, sin agregar dependencias. | Propuso tarjetas redondeadas, chips y una cabecera con degradado y formas decorativas. | Dibujé las olas y todos los íconos con `Canvas`, fijé el tema claro, moví el `Scaffold` y la navegación a `ui/MuroCampusApp.kt` y ajusté la cabecera hasta que el amarillo llegara al borde superior con las dos olas visibles. | No se podían agregar librerías de íconos ni fuentes. `MainActivity` debe quedar solo con Repository, ViewModel y `setContent`. |

## Comprobaciones en el teléfono (Huawei WDY-LX3)

- **Borrar:** el diálogo aparece, al confirmar vuelve al muro y la publicación desaparece de la lista.
  Con el modo avión activo se queda en el detalle y muestra *Sin conexión a internet*; al recuperar la
  red, el reintento borra correctamente.
- **Búsqueda:** `odio` con **Todos** da 6 coincidencias; `odio` con **Usuario 1** da 1; `zzz` muestra
  *Sin resultados para “zzz”*; la X devuelve las 100 publicaciones; el texto se conserva al entrar al
  detalle y volver.
- **Interceptor:** compila e instala, y con el interceptor activo el muro carga la lista, el detalle
  con comentarios y el formulario de publicación sin errores.

## Limitación del servicio de práctica

JSONPlaceholder responde 200 a `DELETE` y a `PUT`/`PATCH` pero no guarda los cambios. Por eso el
borrado se refleja solo en la lista en memoria hasta que se recarga el muro.
