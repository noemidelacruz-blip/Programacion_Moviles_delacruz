# HuellitasVet

Aplicación Android para la gestión de una veterinaria, hecha con **Kotlin**, **Jetpack Compose** y **Material 3** (sin XML). Es el laboratorio *MVVM + Room* del curso de Programación en Móviles (TECSUP, 4to ciclo).

> **Ramas del repositorio**
> - `main`: Fase 1, la app construida paso a paso en 15 commits, sin IA.
> - `con-ia`: Fase 2, con tres mejoras desarrolladas con asistencia de Gemini en Android Studio (carnet de vacunas, recordatorio de próximo control y calendario mensual). Los prompts están documentados en [`PROMPTS.md`](PROMPTS.md).

## Descripción

HuellitasVet tiene dos perfiles que conviven en el mismo dispositivo y comparten la misma base de datos Room:

- **Dueño de mascotas (esmeralda):** registra a sus mascotas, pide citas eligiendo servicio, fecha y hora, cancela citas, consulta el historial clínico y el carnet de vacunas de cada mascota y ve el recordatorio de su próximo control.
- **Veterinario (azul):** ve el panel de la clínica, confirma citas en la agenda (por semana o por mes), registra la atención (diagnóstico, tratamiento, peso y próximo control), consulta a los pacientes, registra vacunas y administra los servicios.

Cuando el veterinario registra una atención, el dueño la ve al instante en el historial de su mascota. Todos los datos son ficticios (Ley 29733).

## Tecnologías

| Tecnología | Uso |
|---|---|
| Kotlin + Jetpack Compose + Material 3 | Interfaz, sin XML, con modo oscuro |
| Room (con KSP) | Base de datos local con 5 tablas y relaciones anidadas |
| DataStore Preferences | Perfil activo, modo oscuro, notificaciones y aceptar citas |
| Navigation Compose | Navegación entre pantallas y barras inferiores |
| ViewModel + StateFlow | Estado de las pantallas |
| Patrón MVVM + Repository (interface) | Arquitectura |
| Gemini en Android Studio | Fase 2: desarrollo de las 3 mejoras |

Versiones usadas: AGP 9.2.1, Kotlin 2.2.10, KSP 2.3.6, Room 2.8.5, Navigation Compose 2.9.8 y DataStore 1.2.1, con `minSdk 26`. Son más nuevas que las del enunciado, porque las anteriores no funcionan con AGP 9. Las librerías son las mismas y el código no cambia.

## Capturas de pantalla

### Inicio de la app

<table>
  <tr>
    <td align="center"><img width="400" alt="image" src="https://github.com/user-attachments/assets/ac76eac3-6934-47a6-9261-1ae2ca7185a4"><br><b>Splash</b></td>
    <td align="center"><img width="400" alt="image" src="https://github.com/user-attachments/assets/4ef86c70-5889-4826-bd8f-46ad2c626f29" /><br><b>Login Dueño</b></td>
    <td align="center"><img width="400" alt="image" src="https://github.com/user-attachments/assets/34be5a25-1a27-44d0-89db-afc39b631fba" /><br><b>Login Veterinario</b></td>
  </tr>
</table>

### Perfil Dueño de mascota

<table>
  <tr>
    <td align="center"><img width="400" alt="image" src="https://github.com/user-attachments/assets/0b304035-1791-4e92-876f-bec366dc9ebe" /><br><b>Inicio</b></td>
    <td align="center"><img width="400" alt="image" src="https://github.com/user-attachments/assets/179012c1-ef38-4995-8e6c-e5748e8c33cc" /><br><b>Mis mascotas (modo oscuro)</b></td>
    <td align="center"><img width="400" alt="image" src="https://github.com/user-attachments/assets/a12dc27b-57a7-4c13-afde-6429c1417089" /><br><b>Citas próximas (modo oscuro)</b></td>
  </tr>
  <tr>
    <td align="center"><img width="400" alt="image" src="https://github.com/user-attachments/assets/ba9257b8-c177-42be-96c1-f1a6a4ab5611" /><br><b>Historial de citas (modo oscuro)</b></td>
    <td align="center"><img width="400" alt="image" src="https://github.com/user-attachments/assets/56d2f03a-0c62-4a98-b781-49e1ec23516d" /><br><b>Perfil de usuario</b></td>
    <td></td>
  </tr>
</table>

### Perfil Veterinario

<table>
  <tr>
    <td align="center"><img width="400" alt="image" src="https://github.com/user-attachments/assets/77c1ada8-1880-487f-8b60-60b03a7b8bcd" /><br><b>Panel / Ingresos del mes</b></td>
    <td align="center"><img width="400" alt="image" src="https://github.com/user-attachments/assets/cd0bc485-903d-4fe4-954d-e93e119f3269" /><br><b>Agenda (semana)</b></td>
    <td align="center"><img width="400" alt="image" src="https://github.com/user-attachments/assets/ee7e84b6-94b9-4370-801a-614b84a1ec6c" /><br><b>Agenda (mes)</b></td>
  </tr>
  <tr>
    <td align="center"><img width="400" alt="image" src="https://github.com/user-attachments/assets/0fcafe75-ae3f-4b52-926a-1cd8dc7fc185" /><br><b>Pacientes</b></td>
    <td align="center"><img width="400" alt="image" src="https://github.com/user-attachments/assets/3d39a368-04d0-416a-b145-df99116f38f8" /><br><b>Gestión de servicios</b></td>
    <td align="center"><img width="400" alt="image" src="https://github.com/user-attachments/assets/9182fae7-4e08-4a78-a48d-a3ec3d96b693" /><br><b>Ajustes</b></td>
  </tr>
</table>

## Mejoras con IA (rama `con-ia`)

Las tres mejoras se pidieron a Gemini con prompts detallados (contexto de la arquitectura, archivos a revisar, qué va en cada capa, reglas de negocio y criterios de aceptación). Todo lo generado se revisó contra la regla de MVVM y lo corregido se anotó en [`PROMPTS.md`](PROMPTS.md).

| Mejora | Entidad y base de datos | DAO | Repository | ViewModel | View |
|---|---|---|---|---|---|
| **Carnet de vacunas** | Nueva entidad `Vacuna` (versión 2 de la base) | `VacunaDao` | `observarVacunas`, `guardarVacuna`, `eliminarVacuna` | `MascotaViewModel` | `FichaMascotaScreen` (pestaña Vacunas) |
| **Recordatorio de próximo control** | Columna `fechaControl` en `Cita` (versión 3) | `CitaDao.registrarAtencion` | `registrarAtencion(..., fechaControl)` | `AtenderCitaViewModel`, `InicioViewModel` | `AtenderCitaScreen`, `InicioScreen`, `HistorialItem` |
| **Calendario mensual** | Sin cambios | `CitaDao.contarPorDia` | `observarCitasPorDia` | `AgendaViewModel` | `AgendaScreen` |

### 1. Carnet de vacunas

El veterinario registra las vacunas aplicadas a cada mascota (nombre y próxima dosis opcional) y el dueño las consulta, en solo lectura, en la pestaña **Vacunas** de la ficha. Cada vacuna muestra un chip de estado calculado en el ViewModel: **Vigente**, **Próxima** (vence en 30 días o menos) o **Vencida**.

<table>
  <tr>
    <td align="center"><img width="400" src="https://github.com/user-attachments/assets/819ddcf2-d5a5-4b2a-8bcc-03be79908176" /><br><b>Carnet de vacunas (dueño)</b></td>
    <td align="center"><img width="400" alt="image" src="https://github.com/user-attachments/assets/8d9d8e6e-cd92-40e4-b5f1-d887f7a466d0" /><br><b>Registrar vacuna (veterinario)</b></td>
  </tr>
</table>

### 2. Recordatorio de próximo control

Al atender una cita, el veterinario puede fijar un control (en 1 semana, 2 semanas, 1 mes o 3 meses). El dueño ve el control más cercano en la tarjeta **Próximo control** de su Inicio, y la fecha también aparece en el historial clínico de la mascota. Se guarda dentro de la misma transacción que ya registraba la atención y actualizaba el peso.

<table>
  <tr>
    <td align="center"><img width="400" alt="image" src="https://github.com/user-attachments/assets/9964da7c-7783-45a4-8357-020f03bfd425" /><br><b>Atender cita con control (veterinario)</b></td>
    <td align="center"><img width="400" alt="image" src="https://github.com/user-attachments/assets/37bf58fc-1681-44b0-a70f-324c15084b15" /><br><b>Próximo control en el Inicio (dueño)</b></td>
  </tr>
</table>

### 3. Calendario mensual

La Agenda del veterinario tiene un botón en la barra superior que alterna entre la vista de semana y una vista de **mes** con puntos en los días que tienen citas (las canceladas no cuentan). Tocar un día muestra sus citas debajo, con los botones Confirmar y Atender.

<table>
  <tr>
    <td align="center"><img width="400"alt="image" src="https://github.com/user-attachments/assets/4d2c2638-d33d-406e-b532-81355624639c" /><br><b>Vista de semana</b></td>
    <td align="center"><img width="400" alt="image" src="https://github.com/user-attachments/assets/181cde17-422c-42d6-9b24-a34635bcf5df" /><br><b>Vista de mes</b></td>
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

El `Repository` es una interface para que, más adelante, se conecte a un servicio web sin cambiar los ViewModels ni las pantallas. En la Fase 2 se respetó esa regla: toda función nueva se declaró primero en `VetRepository` y después en `VetRepositoryRoom`.

## Base de datos (5 tablas, versión 3)

| Tabla | Descripción | Relación |
|---|---|---|
| `usuarios` | Dueños (CLIENTE) y veterinario (ADMIN) | |
| `servicios` | Consulta, vacunación, baño y corte, desparasitación | |
| `mascotas` | Pertenece a un dueño | N mascotas → 1 dueño |
| `citas` | Fecha, hora, estado, precio, diagnóstico, tratamiento, peso y fecha de control | N citas → 1 mascota y N citas → 1 servicio |
| `vacunas` (IA) | Nombre, fecha de aplicación y próxima dosis | N vacunas → 1 mascota (eliminación en cascada) |

Decisiones de diseño: la fecha (`aaaa-MM-dd`) y la hora (`HH:mm`) se guardan como texto, porque se ordenan igual que las fechas y se comparan con `=` en SQL. El precio se guarda en la cita como una "foto" del momento, para que un cambio de precio no altere los ingresos ya registrados. Al subir la versión de la base se usa `fallbackToDestructiveMigration`, porque es un laboratorio en modo práctica: la base se recrea y se vuelven a sembrar los datos de ejemplo.

## Reglas de negocio

- El dueño solo ve **sus** mascotas y las citas de sus mascotas; el veterinario ve todo.
- Registrar mascota: nombre de al menos 2 caracteres, especie y sexo elegidos, año entre 1990 y el actual, peso mayor que 0.
- Una mascota con citas no se puede eliminar.
- Pedir cita: servicio **activo**, desde hoy hasta 7 días, horarios de 09:00 a 17:30 cada 30 minutos (solo horas futuras si es hoy). Estado inicial PENDIENTE y se guarda el precio del servicio.
- **Horario único:** una cita por horario. Si ya existe una cita no cancelada en esa fecha y hora, el horario se muestra deshabilitado (gris y tachado).
- El dueño solo cancela citas PENDIENTE o CONFIRMADA y solo si faltan **más de 2 horas**.
- El veterinario pasa PENDIENTE → CONFIRMADA y puede cancelar desde PENDIENTE o CONFIRMADA. ATENDIDA y CANCELADA son estados finales.
- Registrar atención: diagnóstico de al menos 5 caracteres y peso mayor que 0. En **una sola transacción** se guarda la atención (con el próximo control, si se eligió), la cita pasa a ATENDIDA y se actualiza el peso de la mascota.
- Un servicio con citas no se elimina, solo se desactiva. Un servicio inactivo no aparece al pedir cita.
- Si "Aceptar citas" está apagado, el dueño ve "La clínica no está recibiendo citas".
- Vacunas (IA): el nombre debe tener al menos 3 caracteres. El estado es Vencida si la próxima dosis ya pasó, Próxima si vence en 30 días o menos, y Vigente en los demás casos.

## Estructura del proyecto

Los archivos marcados con `(IA)` son nuevos de la Fase 2.

```
com.delacruz.huellitasvet
├── MainActivity.kt
├── VetApp.kt                     crea la base y el repositorio
│
├── data
│   ├── local
│   │   ├── entity                Servicio, Usuario, Mascota, Cita,
│   │   │                         Vacuna (IA)
│   │   ├── dao                   ServicioDao, UsuarioDao, MascotaDao, CitaDao,
│   │   │                         VacunaDao (IA)
│   │   ├── relation              MascotaConDueno, CitaConDetalle
│   │   ├── VetDatabase.kt
│   │   └── DatosIniciales.kt
│   ├── repository                VetRepository (interface), VetRepositoryRoom
│   └── prefs                     PreferenciasStore (DataStore)
│
├── model                         Rol, EstadoCita, Especie, Sexo, Horarios,
│                                 ConteoServicio, UltimaVisita,
│                                 EstadoVacuna (IA), OpcionControl (IA),
│                                 ConteoFecha (IA)
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
Una lectura que alimenta la pantalla debe avisar cada vez que los datos cambian, y eso lo hace un `Flow`: cuando el veterinario confirma una cita, la lista del dueño se actualiza sola. Una escritura, en cambio, ocurre una vez y termina, así que se modela como una función `suspend`.

**2. ¿Por qué `VetRepository` es una interface?**
Los ViewModels solo conocen el contrato, no a Room. Así, más adelante el repositorio se puede conectar a un servicio web o a la nube sin cambiar ni los ViewModels ni las pantallas.

**3. ¿Por qué el precio se guarda dentro de la cita?**
Porque es una "foto" del momento. Si mañana la clínica cambia el precio de la vacuna, las citas antiguas conservan lo que costaron y los ingresos del panel no se alteran.

**4. ¿Por qué registrar la atención va en una transacción?**
Hay cambios que deben ocurrir juntos: guardar el diagnóstico y el control, pasar la cita a ATENDIDA y actualizar el peso de la mascota. Con `withTransaction`, si algo falla no queda nada a medias.

**5. ¿Por qué la fecha y la hora son texto?**
El formato `aaaa-MM-dd` se ordena igual que las fechas, se compara con `=` en SQL y se convierte con `LocalDate.parse()`. Así no hacen falta convertidores de tipos.

**6. ¿Dónde viven las reglas de negocio y por qué?**
En los ViewModels, no en los Composables ni en los DAOs. La pantalla solo muestra y avisa de los clics, así las reglas se pueden cambiar sin tocar la interfaz.

**7. ¿Qué mejoró la IA y qué hubo que corregir?**
Gemini aceleró el código repetitivo de las tres mejoras (entidades, DAOs y pantallas), pero hubo que revisar que respetara MVVM, las convenciones del proyecto y los datos de prueba. El detalle de cada prompt y de lo corregido está en [`PROMPTS.md`](PROMPTS.md).

## Autora

Noemi De La Cruz — TECSUP, Programación en Móviles.