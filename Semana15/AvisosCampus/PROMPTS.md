# PROMPTS.md

Registro de los prompts importantes de la **Fase 2** (rama `con-ia`) de **AvisosCampus** y de lo que
hice con cada respuesta. La regla fue siempre la misma: Gemini propone, yo leo el `git diff`, pruebo
en el celular y corrijo todo lo que rompa la arquitectura **MVVM + Repository** o las reglas del
curso: nada de `Context`, `NotificationManager` ni `BroadcastReceiver` en el ViewModel, y todo
receptor registrado se quita con `unregisterReceiver` en `onDispose`.

En todos los prompts limité los archivos que Gemini podía editar y le prohibí tocar el resto. Luego
comprobé con `git --no-pager diff` que solo aparecían esos archivos.

| Prompt que escribí | Qué respondió Gemini | Qué cambié o descarté | Por qué |
| --- | --- | --- | --- |
| «Mejora: **Dos canales**. Agrega un canal *Urgentes* con `IMPORTANCE_HIGH` y mantén *Avisos*; el recordatorio usa el urgente. Solo puedes editar `Notificador.kt`; no cambies la firma de `mostrar` ni nada del permiso, el `PendingIntent` o el ViewModel.» | Creó el canal `urgentes` con `IMPORTANCE_HIGH`, lo registró junto a `avisos` en `crearCanal` y en `mostrar` eligió canal y prioridad según `aviso.origen`: `RECORDATORIO` usa `urgentes` con `PRIORITY_HIGH` y el resto usa `avisos` con `PRIORITY_DEFAULT`. | Acepté el resultado sin cambios. El `git diff` mostró solo `Notificador.kt`, con `CANAL = "avisos"` intacto y el resto del `Builder` igual (ícono, `PendingIntent` con `FLAG_IMMUTABLE`, `setAutoCancel`, `notify(aviso.id, ...)`). | Exigí un id de canal **nuevo** porque Android no permite cambiar la importancia de un canal ya creado. Que el `Notificador` decida el canal según el origen mantiene al ViewModel sin tocar el sistema de notificaciones. |
| «Mejora: **Receptor del cargador**. Escucha `ACTION_POWER_CONNECTED` y `ACTION_POWER_DISCONNECTED` con su propio switch, siguiendo el mismo patrón del modo avión. El receptor nuevo en su propio archivo, un `DisposableEffect` separado, y nada de Context en el ViewModel.» | Creó `CargadorReceiver` con `onReceive` corto, agregó `Origen.CARGADOR`, el estado `escuchandoCargador` con `cambiarEscuchaCargador` y `alCambiarCargador` en el ViewModel, un segundo switch en la tarjeta de receptores y un `DisposableEffect(escuchandoCargador)` propio que registra con `RECEIVER_NOT_EXPORTED` y quita el receptor con `unregisterReceiver` en `onDispose`. | Revisé que el `IntentFilter` tuviera **las dos** acciones y que el efecto fuera independiente del de modo avión. Verifiqué que el `when` de `EtiquetaOrigen` cubriera `CARGADOR` para que siguiera siendo exhaustivo. No hubo que quitar nada. | Dos efectos separados permiten activar cada switch sin afectar al otro, y el receptor solo avisa el cambio: el ViewModel decide qué se registra y la pantalla, que sí tiene Context, lo registra y lo quita. |
| «Mejora: **Historial persistente**. Guarda el historial con DataStore para que no se pierda al cerrar la app, con una clase nueva que implemente la misma interface `AvisosRepository`. La clase NO puede usar Context; el DataStore se crea una sola vez a nivel de archivo en `MainActivity`; los ids deben continuar desde el más alto guardado. No modifiques la interface ni el ViewModel.» | Creó `AvisosRepositoryDataStore`, que recibe el `DataStore<Preferences>` por constructor, guarda hasta 50 avisos como un texto JSON con `org.json`, carga el historial al iniciar tolerando JSON corrupto u orígenes desconocidos, continúa el contador desde el id más alto y guarda en segundo plano. En `MainActivity` creó el DataStore con `preferencesDataStore` a nivel de archivo y lo pasó a la fábrica del ViewModel. | Acepté la solución. Comprobé que `AvisosRepository.kt` y el ViewModel no cambiaron, que el commit solo tocó `MainActivity.kt`, `build.gradle.kts` y el archivo nuevo, y que no se agregó Room ni plugins. | El ViewModel sigue recibiendo la interface, así que no se entera del cambio de Repository: esa es la ventaja de separar capas. El `Context` queda solo en `MainActivity`, la capa que crea las dependencias. Continuar los ids evita que una notificación nueva reemplace a una anterior, porque se muestra con `notify(aviso.id, ...)`. |
| «Cambia **solo el aspecto visual** a una paleta *rosa salmón*: colores, forma y ancho de las etiquetas del historial. No cambies lógica, nombres existentes, textos ni el ViewModel. Solo puedes editar `Color.kt`, `Theme.kt`, `PantallaAvisos.kt` y `HistorialAvisos.kt`.» | Cambió los valores de `Color.kt` conservando los nombres, agregó constantes nuevas, pasó `onPrimary` a un tono oscuro, aplicó un color propio a los títulos de las tarjetas y rediseñó `EtiquetaOrigen` con ancho fijo, esquinas de 8 dp y un color por origen. | Verifiqué que el texto sobre el rosa fuera **oscuro**: con `onPrimary` blanco los botones y la barra no se leían. Confirmé en el `git diff` que no cambió ninguna línea de lógica. | El rosa salmón es un color claro y exige texto oscuro para tener buen contraste. Dar un color distinto a los títulos evita que se confundan con la barra y los botones. Es un cambio extra, no una de las 3 mejoras de la lista. |

## Criterios que apliqué a todas las respuestas

- **No cambiar la arquitectura**: el ViewModel sigue sin `Context`, `NotificationManager` ni
  `BroadcastReceiver`, y el Repository sigue sin `Context`. Se verificó con una búsqueda en
  `viewmodel/` y `data/`.
- **Receptores seguros**: cada receptor se registra en la pantalla con `DisposableEffect` y se quita
  con `unregisterReceiver` en `onDispose`. No se declaró ninguno en el `AndroidManifest.xml`.
- **Cambios mínimos**: cuando el prompt decía «solo este archivo», el `git diff` solo mostró esos
  archivos y se conservaron textos, nombres y lógica existentes.
- **Probar antes de aceptar**: cada mejora se compiló, se instaló en el celular y se probó (canales
  en Ajustes, aviso emergente del recordatorio, aviso del cargador, historial después de cerrar y
  abrir la app).
- **Un commit por mejora**, con un mensaje que dice qué mejora se hizo.
