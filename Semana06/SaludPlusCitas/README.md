# Clínica SaludPlus — App Paciente

Aplicación Android para agendar citas médicas, hecha con **Kotlin** y **Jetpack Compose**. Es la tarea complementaria al Laboratorio 6 del curso Diseño y Desarrollo de Software — Programación en Móviles, 4.º ciclo.

- **Autora:** Noemí De la Cruz
- **Paquete:** `com.delacruz.saludpluscitas`
- **Ramas:** `sin-ia` (Fase 1, sin IA) y `con-ia` (Fase 2, con IA). La rama `main` se mantiene sin modificaciones.

---
## 1. Pantallas implementadas (las 15)

| # | Pantalla | Archivo | Captura |
|---|----------|---------|---------|
| 1 | Splash | `auth/SplashScreen.kt` | <img width="335" height="732" alt="Splash" src="https://github.com/user-attachments/assets/c7cb5dc0-be0f-4a26-9d1f-6e702b965970" />|
| 2 | Registro | `auth/RegistroScreen.kt` | <img width="335" height="732" alt="Registro" src="https://github.com/user-attachments/assets/d222cf06-caa7-4da0-91a4-11ed7b4db16c" />|
| 3 | Inicio | `home/HomeScreen.kt` | <img width="332" height="728" alt="Inicio" src="https://github.com/user-attachments/assets/f6eba9ce-a804-4619-a5c2-913bf2392925" />|
| 4 | Especialidades | `agendamiento/EspecialidadesScreen.kt` | <img width="334" height="725" alt="Especialidades" src="https://github.com/user-attachments/assets/b2351c1f-ca1c-463f-9498-0e20393effc5" />|
| 5 | Médicos | `agendamiento/MedicosScreen.kt` | <img width="334" height="725" alt="image" src="https://github.com/user-attachments/assets/42cb4f03-15e3-4e7c-9cb9-c56bcca3956f" />|
| 6 | Fecha y hora | `agendamiento/FechaHoraScreen.kt` | <img width="338" height="735" alt="Fecha y hora" src="https://github.com/user-attachments/assets/0f3565b1-30ee-4441-8f75-6b7df0dc5702" />|
| 7 | Confirmar cita | `agendamiento/ConfirmarCitaScreen.kt` | <img width="338" height="718" alt="Confirmar cita" src="https://github.com/user-attachments/assets/3a5264a0-aa00-4259-b770-190a37be5e3a" />|
| 8 | Iniciar sesión | `auth/LoginScreen.kt` | <img width="332" height="728" alt="Iniciar sesión" src="https://github.com/user-attachments/assets/96eff5ae-4edc-4c34-a52d-f4926571149f" />|
| 9 | Cita agendada | `agendamiento/CitaExitosaScreen.kt` | <img width="332" height="728" alt="Cita agendada" src="https://github.com/user-attachments/assets/68d32643-0841-45a2-bd08-06a12b651f00" />|
| 10 | Mis citas | `citas/MisCitasScreen.kt` | <img width="340" height="734" alt="Mis citas" src="https://github.com/user-attachments/assets/c6282096-995a-463e-8051-2cdd9590a760" />|
| 11 | Perfil / Mis datos | `perfil/PerfilScreen.kt` | <img width="720" height="1612" alt="Perfil" src="https://github.com/user-attachments/assets/707b9ef0-2992-4ace-9398-1c676e44c447" />|
| 12 | Detalle de cita (reto) | `citas/DetalleCitaScreen.kt` | <img width="330" height="725" alt="Detalle de cita" src="https://github.com/user-attachments/assets/9994fb83-319c-4b35-b3cf-ef8720f5b2ac" />|
| 13 | Resultados (reto) | `resultados/ResultadosScreen.kt` | <img width="340" height="724" alt="Resultados" src="https://github.com/user-attachments/assets/3aea83e5-169e-438d-ae7a-b9faede0d808" />|
| 14 | Notificaciones (reto) | `notificaciones/NotificacionesScreen.kt` |<img width="340" height="724" alt="Notificaciones" src="https://github.com/user-attachments/assets/d0220e79-8bc2-4c4b-a350-78b6fc94778e" />|
| 15 | Términos y condiciones (reto) | `auth/TerminosScreen.kt` | <img width="338" height="724" alt="Términos y condiciones" src="https://github.com/user-attachments/assets/6e2e3802-bd09-4176-b3b6-308f3b57c4e8" />|

---

## 2. Fase 2 — Mejora con IA (rama `con-ia`)

La mejora obligatoria es el **calendario dinámico con `java.time.LocalDate`** en la Pantalla 6. Se hizo en 3 commits, uno por cada prompt. El detalle de cada prompt, la respuesta de la IA y lo que se corrigió está en [`PROMPTS.md`](PROMPTS.md).

1. **Fechas con `LocalDate` y texto en español:** la fecha se muestra como "Martes 13 de octubre 2026".
2. **Próximos 5 días hábiles:** sin sábados ni domingos, con los horarios recalculados y la hora reiniciada al cambiar de día.
3. **Flechas por semana y mes dinámico:** `<` y `>` cambian de semana, `<` no retrocede antes de la semana actual y el título cambia según la semana.
   
| Calendario dinámico | Fecha en español | Cita agendada |
| :---: | :---: | :---: |
| <img src="https://github.com/user-attachments/assets/794c74de-e3d8-4531-9846-785e8537caaa" width="260" /> | <img src="https://github.com/user-attachments/assets/2cd1f5fc-ecae-4bb1-b39f-b19eb69b0d2a" width="260" /> | <img src="https://github.com/user-attachments/assets/59bc5635-6b60-4a0c-a112-51b991afe6f5" width="260" /> |

**Observación:** cuando la semana cruza de mes (por ejemplo, del 29 de octubre al 4 de noviembre), el título conserva el mes del primer día de la semana. La guía pide que cambie "según la semana mostrada", así que lo dejé así.

---

## 3. Cumplimiento de la rúbrica

| Criterio | Estado | Cómo se cumple |
|----------|--------|----------------|
| Estructura de archivos completada | Cumple | Las 15 pantallas están en su archivo y paquete, sin `PantallaEnConstruccion` (secciones 6.1 y 1) |
| Repositorio con colecciones | Cumple | Todas las funciones de `Repositorio.kt` usan operaciones de colecciones, sin base de datos (sección 6.2) |
| Registro, login y sesión | Cumple | Registro con validaciones, login contra los usuarios registrados, saludo con el nombre y cerrar sesión en Perfil (pantallas Registro, Iniciar sesión, Inicio y Perfil, sección 1) |
| Flujo de agendamiento completo | Cumple | Inicio → Especialidades → Médicos → Fecha y hora → Confirmar → Cita agendada, con los parámetros y `popUpTo` al confirmar (pantallas 3 a 9, sección 1) |
| NavigationBar (menú, Semana 6) | Cumple | `bottomBar` con Inicio, Citas, Resultados y Perfil (pantallas Inicio, Mis citas, Resultados y Perfil, sección 1) |
| LazyRow y LazyColumn | Cumple | `LazyRow` de destacadas (desplazamiento horizontal) y `LazyColumn` de especialidades con búsqueda en tiempo real, médicos y mis citas con estado vacío (pantallas Inicio, Especialidades, Médicos y Mis citas, sección 1) |
| Horarios reactivos | Cumple | `LazyVerticalGrid`; el horario reservado desaparece; "Continuar" solo con día y hora (pantalla Fecha y hora, sección 1) |
| Fase 1 — Commits en `sin-ia` | Cumple | 17 commits descriptivos |
| Fase 2 — Calendario dinámico (con IA) | Cumple | Días hábiles con `LocalDate`, flechas por semana, mes dinámico y horarios recalculados (sección 6.5) |
| Fase 2 — Commits y `PROMPTS.md` | Cumple | 3 commits en `con-ia` y `PROMPTS.md` con los 3 prompts |

---

## 4. Preguntas de reflexión

**1. ¿Por qué los modelos, `Rutas.kt` y `AppNavigation.kt` se entregaron completos y las pantallas no? ¿Qué tienen en común los archivos que sí se dejaron como esqueleto?**

Los archivos completos son la base de la app y no son el objetivo de aprendizaje: los modelos solo describen los datos, y `Rutas.kt` y `AppNavigation.kt` definen el mapa de navegación. Los que quedaron como esqueleto (`Repositorio.kt` y las pantallas) tienen en común que contienen la lógica y la interfaz. Ahí se practican los temas del curso: colecciones, estado, listas, validaciones y paso de parámetros.

**2. ¿Por qué el Repositorio es un `object` y no una clase normal? ¿Qué pasaría con las citas si cada pantalla creara su propia lista?**

Un `object` es un singleton: existe una sola instancia en toda la app, así que todas las pantallas leen y escriben las mismas colecciones. Si cada pantalla creara su propia lista, cada una tendría sus propios datos: una cita agendada en Confirmar cita no aparecería en Mis citas ni en Perfil, y el bloqueo de horarios reservados no funcionaría.

**3. ¿Cómo lograste que la búsqueda de especialidades y los horarios disponibles se actualicen solos, sin que tú "actualices" nada a mano?**

Con estado de Compose. El texto de búsqueda y el día y la hora seleccionados se guardan con `remember { mutableStateOf(...) }`. La lista que se muestra se calcula a partir de ese estado (`filter` sobre las especialidades y `horariosDisponibles(medicoId, fecha)` para los horarios). Cuando el estado cambia, Compose recompone la pantalla y recalcula la lista.

**4. ¿Qué diferencia notaste entre `navigate()` normal (Especialidades → Médicos) y el que usa `popUpTo` (Confirmar cita → Cita agendada)? ¿Qué pasa al presionar Atrás en cada caso?**

Con `navigate()` normal la pantalla nueva se apila sobre la anterior, y al presionar Atrás se vuelve a la pantalla previa. Con `popUpTo` se borran de la pila las pantallas del flujo de agendamiento antes de mostrar la nueva. Al presionar Atrás en Cita agendada no se regresa a Confirmar cita (lo que permitiría duplicar la cita), sino que se sale del flujo.

**5. ¿Qué tuviste que corregir del código que te generó la IA para el calendario dinámico?**

Está detallado en [`PROMPTS.md`](PROMPTS.md). En resumen: ajustar el formato de la fecha en español (primera letra en mayúscula y sin "de" antes del año), reiniciar la hora seleccionada al cambiar de día, dejar la flecha `<` realmente desactivada en la semana actual y comprobar que el bloqueo de horarios reservados siguiera funcionando.

**6. Compara el `NavigationDrawer` del Laboratorio 6 con el `NavigationBar` de esta tarea: ¿en qué caso usarías cada uno en un proyecto propio?**

El `NavigationBar` (barra inferior) sirve para pocos destinos principales (3 a 5) que el usuario cambia seguido, como Inicio, Citas, Resultados y Perfil. El `NavigationDrawer` (menú lateral) sirve cuando hay muchos destinos o secciones secundarias que se usan menos, como configuración, ayuda o cerrar sesión. En esta app usaría `NavigationBar` porque son 4 destinos de uso frecuente; en una app con más de 5 secciones usaría `NavigationDrawer`.

---

## 5. Observaciones y conclusiones

### Observaciones

1. Login, Cita agendada, Mis citas y Perfil no estaban en el diseño de referencia. Las diseñé con el mismo estilo: tarjetas suaves, íconos azules, botones azules y barra superior con flecha.
2. Cuando una semana cruza de mes, el título del calendario sigue mostrando el mes del primer día. Cumple la guía, así que lo dejé así.
3. Los datos viven solo en memoria, por eso al cerrar la app hay que registrarse otra vez. Es intencional en esta tarea.

### Conclusiones

1. En la Fase 1 (`sin-ia`) se desarrolló la app completa: Repositorio, 15 pantallas, navegación y flujo de agendamiento de citas, con 17 commits.
2. En la Fase 2 (`con-ia`) se agregó el calendario dinámico con `java.time.LocalDate`: 5 días hábiles, flechas por semana, mes dinámico y fecha en español, con 3 prompts y 3 commits.
3. El código generado con IA se revisó y se probó en la app antes de aceptarlo.
   
### Comparación entre la Fase 1 y la Fase 2

En la Fase 1 (`sin-ia`) se desarrolló la app completa: el Repositorio, las 15 pantallas, la navegación y el flujo de agendamiento de citas, con 17 commits.

En la Fase 2 (`con-ia`) se agregó el calendario dinámico con `java.time.LocalDate` en la pantalla Fecha y hora, con 3 commits.

---

## 6. Cumplimiento de los requerimientos de la guía

### 6.1 Reglas y alcance

| Requerimiento | Estado | Cómo y dónde se cumple |
|---|---|---|
| No usar base de datos (ni Room, ni SQLite, ni Firebase) | Cumple | No hay base de datos en el proyecto. Los usuarios, especialidades, médicos y citas son listas dentro del `object Repositorio` (`data/repository/Repositorio.kt`) y se pierden al cerrar la app |
| No cambiar nombres ni parámetros de las funciones del Repositorio ni de las pantallas | Cumple | Las funciones y los 15 archivos de pantallas conservan los nombres del esqueleto original (estructura en la sección 7) |
| No modificar `AppNavigation.kt` salvo para agregar pantallas nuevas | Cumple | Solo se agregaron las pantallas para poder llamarlas; no se cambió la lógica de navegación |
| Borrar la llamada a `PantallaEnConstruccion` en cada pantalla terminada | Cumple | Las 15 pantallas muestran su contenido real (sección 1). `PantallaEnConstruccion.kt` solo queda como archivo del componente en `ui/components` |

### 6.2 Funciones del Repositorio (`data/repository/Repositorio.kt`)

| Función | Estado | Cómo y dónde se cumple |
|---|---|---|
| `registrarUsuario` | Cumple | `any` comprueba que el usuario no exista y `add` lo guarda en la lista. La usa `RegistroScreen` |
| `iniciarSesion` y `cerrarSesion` | Cumple | `find` busca al usuario en la lista y lo guarda como `usuarioActual`; `cerrarSesion` lo limpia. Las usan `LoginScreen` y el botón "Cerrar sesión" de `PerfilScreen` |
| `buscarEspecialidades` | Cumple | `filter` con `contains` sobre el nombre de la especialidad. La usa `EspecialidadesScreen` |
| `especialidadesDestacadas` | Cumple | `take` toma las primeras especialidades. Las usa el `LazyRow` de `HomeScreen` |
| `obtenerEspecialidad`, `obtenerMedico` y `obtenerCita` | Cumple | `find` por id. Los datos del médico y de la cita se muestran en `ConfirmarCitaScreen`, `CitaExitosaScreen` y `DetalleCitaScreen` |
| `medicosPorEspecialidad` y `buscarMedicos` | Cumple | `filter` por `especialidadId` y `sortedByDescending` por calificación. En `MedicosScreen` el 4.8 sale antes que el 4.5 |
| `horariosDisponibles` | Cumple | `filter` y `map` parten de los horarios base y quitan los ya reservados de ese médico y esa fecha. La usa `FechaHoraScreen` |
| `agendarCita` | Cumple | `any` evita agendar un horario ya tomado y `add` guarda la cita. Se llama desde el botón "Agendar cita" de `ConfirmarCitaScreen` |
| `citasDelUsuario` | Cumple | `filter` por usuario y `sortedWith` para ordenar. La usan `MisCitasScreen` y el contador "Citas agendadas" de `PerfilScreen` |
| `cancelarCita` (reto) | Cumple | `removeIf` quita la cita de la lista. Se llama desde el `AlertDialog` de `DetalleCitaScreen`; al cancelar, la cita desaparece de Mis citas |

### 6.3 Pantallas y navegación

| Requerimiento | Estado | Cómo y dónde se cumple |
|---|---|---|
| Registro con validaciones | Cumple | `RegistroScreen` valida el nombre, el teléfono de 9 dígitos y la contraseña de mínimo 6 caracteres, y muestra el error bajo cada campo. El correo es opcional |
| Login contra la lista de usuarios | Cumple | `LoginScreen` recibe teléfono o correo y contraseña, y solo deja entrar a un usuario registrado en el Repositorio |
| Saludo con el nombre del usuario | Cumple | `HomeScreen` muestra "¡Hola, Noemí!" con el nombre del `usuarioActual` |
| Cerrar sesión en Perfil | Cumple | Botón "Cerrar sesión" en `PerfilScreen`; cierra la sesión y vuelve al Splash |
| `NavigationBar` con 4 destinos | Cumple | `bottomBar` del `Scaffold` con Inicio, Citas, Resultados y Perfil |
| `LazyRow` de especialidades destacadas | Cumple | `HomeScreen`, lista horizontal "Especialidades destacadas" |
| `LazyColumn` de especialidades, médicos y mis citas | Cumple | `EspecialidadesScreen`, `MedicosScreen` y `MisCitasScreen` |
| Búsqueda de especialidades en tiempo real | Cumple | `EspecialidadesScreen`: la lista se filtra mientras se escribe, sin botón. Al escribir "pe" solo queda Pediatría. Si no hay coincidencias muestra el mensaje "No se encontraron especialidades" |
| Mensaje de lista vacía en Mis citas | Cumple | `MisCitasScreen` muestra un estado vacío con ícono, mensaje y botón cuando el usuario no tiene citas (por ejemplo, después de cancelar la única cita) |
| `LazyVerticalGrid` de horarios | Cumple | `FechaHoraScreen`, cuadrícula de 3 columnas con horarios de 30 minutos |
| Un horario reservado deja de aparecer para ese médico y fecha | Cumple | `horariosDisponibles` excluye las citas ya agendadas; se comprobó con la cita del martes 13 a las 08:30 |
| "Continuar" solo se habilita con día y hora elegidos | Cumple | Botón "Continuar" de `FechaHoraScreen` |
| Parámetros `especialidadId`, `medicoId`, `fecha` y `hora` | Cumple | Especialidades envía `especialidadId` ("Médicos de Oftalmología" en la pantalla Médicos); Médicos envía `medicoId` a Fecha y hora; Fecha y hora envía `medicoId`, `fecha` y `hora` a Confirmar cita |
| `popUpTo` al confirmar la cita | Cumple | `ConfirmarCitaScreen` navega a `CitaExitosaScreen` con `popUpTo`. En Cita agendada solo hay dos opciones ("Ver mis citas" e "Ir al inicio") y, al presionar Atrás, se va directo a Inicio sin volver a Confirmar cita |
| Conexiones extra | Cumple | Cita agendada → Mis citas (botón "Ver mis citas"); Perfil → Splash (cerrar sesión); campana de Inicio → Notificaciones; enlace "Términos y Condiciones" del Registro → Términos |
| Vistas faltantes con el mismo estilo | Cumple | Login, Cita agendada, Mis citas y Perfil usan las mismas tarjetas, íconos azules, botones azules y barra superior con flecha |

### 6.4 Fase 1 — Desarrollo sin IA (rama `sin-ia`)

| Requerimiento | Estado | Cómo y dónde se cumple |
|---|---|---|
| Repositorio en GitHub con el esqueleto como primer commit | Cumple | El primer commit del repositorio sube el código esqueleto inicial del proyecto |
| Trabajar sin IA sobre una rama de la Fase 1 | Cumple | Todo el desarrollo de la Fase 1 está en `sin-ia`; `main` se dejó sin modificar |
| Mínimo 8 commits descriptivos y distribuidos | Cumple | 17 commits en `sin-ia`, repartidos por tema (repositorio, registro y login, inicio, navegación, flujo de agendamiento, mis citas y perfil, retos extra) |
| `git push origin sin-ia` | Cumple | Los commits están subidos a `sin-ia` en GitHub |

### 6.5 Fase 2 — Mejora con IA (rama `con-ia`)

| Requerimiento | Estado | Cómo y dónde se cumple |
|---|---|---|
| Rama de la Fase 2 creada a partir de la rama de la Fase 1 | Cumple | Creada con `git checkout -b con-ia` desde `sin-ia` |
| Próximos 5 días hábiles desde hoy, sin sábados, domingos ni días pasados | Cumple | `FechaHoraScreen` genera los días con `LocalDate`. En la captura de Fecha y hora salen Jue 8, Vie 9, Lun 12, Mar 13 y Mié 14: se saltan el sábado 10 y el domingo 11 |
| Flechas `<` y `>` avanzan o retroceden una semana | Cumple | Flechas del encabezado de `FechaHoraScreen` |
| No se puede retroceder antes de la semana actual | Cumple | En la semana actual la flecha `<` está desactivada y en gris |
| Mes y año ("Octubre 2026") según la semana mostrada | Cumple | El título del encabezado cambia con la semana |
| Al cambiar de día se recalculan los horarios y se reinicia la hora | Cumple | Al elegir otro día, `horariosDisponibles` se calcula con la nueva fecha y la hora seleccionada se borra |
| Pantalla 7 con la fecha en español | Cumple | "Martes 13 de octubre 2026" en `ConfirmarCitaScreen`, `CitaExitosaScreen` y Mis citas |
| El calendario dinámico no rompe el bloqueo de horarios reservados | Cumple | `FechaHoraScreen` sigue usando `horariosDisponibles(medicoId, fecha)`, por lo que un horario reservado sigue sin aparecer en el calendario nuevo |
| Mínimo 3 commits descriptivos en la rama de la Fase 2 | Cumple | 3 commits en `con-ia`, uno por prompt (sección 2) |
| `PROMPTS.md` con prompt, respuesta resumida y qué se corrigió | Cumple | [`PROMPTS.md`](PROMPTS.md): 3 prompts, cada uno con su respuesta resumida y sus correcciones |
| `git push origin con-ia` | Cumple | Los 3 commits están subidos a la rama `con-ia` |

---

## 7. Estructura del proyecto

```
com.delacruz.saludpluscitas
├── MainActivity.kt
├── data
│   ├── model            Cita, Especialidad, Medico, Resultado, Usuario
│   └── repository       Repositorio.kt
├── navigation           Rutas.kt, AppNavigation.kt
└── ui
    ├── components       Componentes.kt, PantallaEnConstruccion.kt
    ├── theme            Color.kt, Theme.kt, Type.kt
    └── screens
        ├── agendamiento CitaExitosaScreen, ConfirmarCitaScreen, EspecialidadesScreen,
        │                FechaHoraScreen, MedicosScreen
        ├── auth         LoginScreen, RegistroScreen, SplashScreen, TerminosScreen
        ├── citas        DetalleCitaScreen, MisCitasScreen
        ├── home         HomeScreen
        ├── notificaciones NotificacionesScreen
        ├── perfil       PerfilScreen
        └── resultados   ResultadosScreen
```
