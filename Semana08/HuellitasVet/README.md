# HuellitasVet

Aplicación Android para la gestión de una veterinaria, hecha con **Kotlin**, **Jetpack Compose** y **Material 3** (sin XML). Es el laboratorio *MVVM + Room* del curso de Programación en Móviles (TECSUP, 4to ciclo).

## Descripción

HuellitasVet tiene dos perfiles que conviven en el mismo dispositivo y comparten la misma base de datos Room:

- **Dueño de mascotas (esmeralda):** registra a sus mascotas, pide citas eligiendo servicio, fecha y hora, cancela citas y consulta el historial clínico de cada mascota.
- **Veterinario (azul):** ve el panel de la clínica, confirma citas en la agenda, registra la atención (diagnóstico, tratamiento y peso), consulta a los pacientes y administra los servicios.

Cuando el veterinario registra una atención, el dueño la ve al instante en el historial de su mascota. Todos los datos son ficticios (Ley 29733).

## Tecnologías

| Tecnología | Uso |
|---|---|
| Kotlin + Jetpack Compose + Material 3 | Interfaz, sin XML |
| Room (con KSP) | Base de datos local con 4 tablas y relaciones anidadas |
| DataStore Preferences | Perfil activo, modo oscuro, notificaciones y aceptar citas |
| Navigation Compose | Navegación entre pantallas y barras inferiores |
| ViewModel + StateFlow | Estado de las pantallas |
| Patrón MVVM + Repository (interface) | Arquitectura |

Versiones usadas: AGP 9.2.1, Kotlin 2.2.10, KSP 2.3.6, Room 2.8.5, Navigation Compose 2.9.8 y DataStore 1.2.1, con `minSdk 26`. Son más nuevas que las del enunciado, porque las anteriores no funcionan con AGP 9. Las librerías son las mismas y el código no cambia.

## Capturas de pantalla

### Perfil Veterinario

<table>
  <tr>
    <td align="center"><img width="400" alt="image" src="https://github.com/user-attachments/assets/bfb18087-cf18-45a8-87bb-182c8a6dc9c0" /><br><b>Panel / Ingresos del mes</b></td>
    <td align="center"><img width="400" alt="image" src="https://github.com/user-attachments/assets/2e47370e-5c4c-48b4-ad5c-18058b9b6070" /><br><b>Agenda de citas</b></td>
    <td align="center"><img width="400"alt="image" src="https://github.com/user-attachments/assets/f102bb86-3b11-48a5-8606-52de2e6f4089" /><br><b>Gestión de servicios</b></td>
  </tr>
</table>

### Perfil Dueño de mascota

<table>
  <tr>
    <td align="center"><img width="400"alt="image" src="https://github.com/user-attachments/assets/35106f63-fed4-4601-9324-d87613b7f6c9" /><br><b>Reserva de cita</b></td>
    <td align="center"><img width="400" alt="image" src="https://github.com/user-attachments/assets/b92a4f7c-dc7d-4aef-a2bb-d9e00cfe4985" /><br><b>Perfil de usuario</b></td>
    <td align="center"><img width="400" alt="image" src="https://github.com/user-attachments/assets/254fa695-bc82-4c2e-8f75-9d7f60767d59" /><br><b>Historial de citas</b></td>
  </tr>
</table>

## Arquitectura: MVVM + Repository

```
 VIEW (Composable)
   │ eventos (clics)            ▲ collectAsState()
   ▼                            │
 VIEWMODEL (StateFlow + reglas de negocio)
   │ llama                      ▲ Flow / resultados
   ▼                            │
 VetRepository (interface)
   │ la implementa
   ▼
 VetRepositoryRoom ──► DAO ──► SQLite (huellitasvet.db)

 DataStore (solo preferencias) ──► ViewModel
```

| Capa | Conoce a | No debe |
|---|---|---|
| View (`ui/`) | Solo a su ViewModel | Usar DAO, Repository ni reglas de negocio |
| ViewModel | Solo al Repository (interface) | Conocer Composables ni el DAO |
| Repository | Los DAOs (en la implementación) | Conocer pantallas ni ViewModels |
| DAO / Room | SQL y entidades | Conocer ViewModels |

**Regla de oro:** una lectura que debe actualizar la pantalla es un `Flow` (sin `suspend`); una escritura o acción única es `suspend`. No se reemplazan, se complementan.

El `Repository` es una interface para que, más adelante, se conecte a un servicio web sin cambiar los ViewModels ni las pantallas.

## Base de datos (4 tablas)

| Tabla | Descripción | Relación |
|---|---|---|
| `usuarios` | Dueños (CLIENTE) y veterinario (ADMIN) | |
| `servicios` | Consulta, vacunación, baño y corte, desparasitación | |
| `mascotas` | Pertenece a un dueño | N mascotas → 1 dueño |
| `citas` | Fecha, hora, estado, precio, diagnóstico, tratamiento y peso | N citas → 1 mascota y N citas → 1 servicio |

Decisiones de diseño: la fecha (`aaaa-MM-dd`) y la hora (`HH:mm`) se guardan como texto, porque se ordenan igual que las fechas y se comparan con `=` en SQL. El precio se guarda en la cita como una "foto" del momento, para que un cambio de precio no altere los ingresos ya registrados.

## Reglas de negocio

- El dueño solo ve **sus** mascotas y las citas de sus mascotas; el veterinario ve todo.
- Registrar mascota: nombre de al menos 2 caracteres, especie y sexo elegidos, año entre 1990 y el actual, peso mayor que 0.
- Una mascota con citas no se puede eliminar.
- Pedir cita: servicio **activo**, desde hoy hasta 7 días, horarios de 09:00 a 17:30 cada 30 minutos (solo horas futuras si es hoy). Estado inicial PENDIENTE y se guarda el precio del servicio.
- **Horario único:** una cita por horario. Si ya existe una cita no cancelada en esa fecha y hora, el horario se muestra deshabilitado (gris y tachado).
- El dueño solo cancela citas PENDIENTE o CONFIRMADA y solo si faltan **más de 2 horas**.
- El veterinario pasa PENDIENTE → CONFIRMADA y puede cancelar desde PENDIENTE o CONFIRMADA. ATENDIDA y CANCELADA son estados finales.
- Registrar atención: diagnóstico de al menos 5 caracteres y peso mayor que 0. En **una sola transacción** se guarda la atención, la cita pasa a ATENDIDA y se actualiza el peso de la mascota.
- Un servicio con citas no se elimina, solo se desactiva. Un servicio inactivo no aparece al pedir cita.
- Si "Aceptar citas" está apagado, el dueño ve "La clínica no está recibiendo citas".

## Estructura del proyecto

```
com.delacruz.huellitasvet
├── MainActivity.kt
├── VetApp.kt                     crea la base y el repositorio
│
├── data
│   ├── local
│   │   ├── entity                Servicio, Usuario, Mascota, Cita
│   │   ├── dao                   ServicioDao, UsuarioDao, MascotaDao, CitaDao
│   │   ├── relation              MascotaConDueno, CitaConDetalle
│   │   ├── VetDatabase.kt
│   │   └── DatosIniciales.kt
│   ├── repository                VetRepository (interface), VetRepositoryRoom
│   └── prefs                     PreferenciasStore (DataStore)
│
├── model                         Rol, EstadoCita, Especie, Sexo, Horarios,
│                                 ConteoServicio, UltimaVisita
│
├── viewmodel                     VetViewModelFactory, SesionViewModel,
│                                 InicioViewModel, MascotaViewModel,
│                                 PedirCitaViewModel, MisCitasViewModel,
│                                 PerfilViewModel, PanelViewModel,
│                                 AgendaViewModel, AtenderCitaViewModel,
│                                 PacientesViewModel, ServiciosViewModel
│
└── ui
    ├── theme                     Color, Type, Theme (VetTheme)
    ├── components                AppTopBar, VetButton, VetTextField,
    │   │                         EstadoCitaChip, MascotaAvatar, CitaCard,
    │   │                         FechaChip, HoraChip, HistorialItem,
    │   │                         IconosServicio, FormatoTexto,
    │   │                         ComponentesPerfil
    │   └── graficos              DatoGrafico, GraficoDona, GraficoBarras
    ├── navigation                Rutas, NavVet, BarraDueno, BarraVet
    ├── dueno                     SplashScreen, LoginScreen, InicioScreen,
    │                             MascotasScreen, FormularioMascotaScreen,
    │                             FichaMascotaScreen, PedirCitaScreen,
    │                             MisCitasScreen, PerfilScreen
    └── veterinario               PanelScreen, AgendaScreen, AtenderCitaScreen,
                                  PacientesScreen, ServiciosScreen,
                                  FormularioServicioScreen, AjustesScreen
```

## Cómo ejecutarlo

1. Abre el proyecto en Android Studio y espera a que termine la sincronización de Gradle.
2. Ejecuta en un emulador o celular con Android 8.0 (API 26) o superior.
3. Los datos de ejemplo se cargan solo la primera vez. Si cambias una entidad, sube la `version` de `VetDatabase` o desinstala la app.

### Cuentas de prueba (datos ficticios)

| Perfil | Pestaña del login | Correo | Contraseña |
|---|---|---|---|
| Dueño | Dueño | `noemi@correo.test` | `1234` |
| Veterinario | Veterinario | `vet@huellitas.test` | `admin` |

También existen `luis@correo.test` y `marta@correo.test`, ambas con la contraseña `1234`.

## Pruebas de aceptación

| Prueba | Resultado esperado | OK |
|---|---|---|
| Login con clave incorrecta | Mensaje de error, no entra | ✅ |
| Dueño: registrar mascota sin nombre | Errores junto a cada campo, no se guarda | ✅ |
| Dueño: eliminar una mascota con citas | Mensaje de aviso y no se elimina | ✅ |
| Dueño: pedir cita | Aparece en Mis citas como PENDIENTE y con el precio del servicio | ✅ |
| Dueño: elegir un horario ocupado | El horario aparece gris y no se puede elegir | ✅ |
| Veterinario: agenda | La cita aparece en su hora y puede confirmarla; el dueño la ve CONFIRMADA | ✅ |
| Veterinario: atender una cita | Pasa a ATENDIDA, el dueño la ve en el historial y el peso de la mascota se actualizó | ✅ |
| Dueño: cancelar una cita a menos de 2 horas | No se permite cancelar | ✅ |
| Veterinario: ingresos del mes | Coinciden con la suma del precio de las citas atendidas | ✅ |
| Veterinario: desactivar un servicio | Ya no se puede elegir al pedir una cita | ✅ |
| Veterinario: eliminar un servicio con citas | No se elimina; se muestra un mensaje | ✅ |
| Cerrar y abrir la app | Las citas siguen ahí y el perfil y el modo oscuro se recuerdan | ✅ |

## Para pensar

**1. ¿Por qué las lecturas son `Flow` y las escrituras `suspend`?**
Una lectura que alimenta la pantalla debe avisar cada vez que los datos cambian, y eso lo hace un `Flow`: cuando el veterinario confirma una cita, la lista del dueño se actualiza sola. Una escritura, en cambio, es una acción que ocurre una vez y termina, así que se modela como una función `suspend`. No se reemplazan: se complementan.

**2. ¿Por qué `VetRepository` es una interface?**
Los ViewModels solo conocen el contrato, no a Room. Así, más adelante el repositorio se puede conectar a un servicio web o a la nube sin cambiar ni los ViewModels ni las pantallas. También facilita probar los ViewModels con un repositorio falso.

**3. ¿Por qué el precio se guarda dentro de la cita?**
Porque es una "foto" del momento. Si mañana la clínica cambia el precio de la vacuna, las citas antiguas conservan lo que costaron y los ingresos del panel no se alteran.

**4. ¿Por qué registrar la atención va en una transacción?**
Hay dos cambios que deben ocurrir juntos: guardar el diagnóstico y pasar la cita a ATENDIDA, y actualizar el peso de la mascota. Con `withTransaction`, si algo falla no queda nada a medias.

**5. ¿Por qué la fecha y la hora son texto?**
El formato `aaaa-MM-dd` se ordena igual que las fechas (`2026-10-21` va antes que `2026-10-22`), se compara con `=` en SQL y se convierte con `LocalDate.parse()`. Así no hacen falta convertidores de tipos y el foco queda en Room.

**6. ¿Dónde viven las reglas de negocio y por qué?**
En los ViewModels, no en los Composables ni en los DAOs. La pantalla solo muestra y avisa de los clics; así las reglas (regla de las 2 horas, horario único, validaciones) se pueden cambiar sin tocar la interfaz.

## Autora

Noemi De La Cruz — TECSUP, Programación en Móviles.
