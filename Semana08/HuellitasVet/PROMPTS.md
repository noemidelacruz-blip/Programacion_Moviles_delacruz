# PROMPTS.md — Fase 2: carnet de vacunas, próximo control y calendario mensual

**Proyecto:** HuellitasVet — laboratorio MVVM + Room (TECSUP, 4to ciclo, Semana 08)
**Asistente de IA usado:** Gemini (agente integrado en Android Studio)
**Pantallas intervenidas:** `FichaMascotaScreen.kt`, `AtenderCitaScreen.kt`, `InicioScreen.kt` y `AgendaScreen.kt`
**Rama:** `con-ia`
**Commits de la Fase 2:** 3 (uno por mejora), después del commit que importa la base desde `sin-ia`.

> Reglas que repetí en los tres prompts, porque la consigna las exige: patrón MVVM + Repository (la View solo habla con su ViewModel; el ViewModel solo conoce la interface `VetRepository`; cada método se declara primero en la interface y luego se implementa en `VetRepositoryRoom`), sin XML, sin librerías externas ni dependencias nuevas, comentarios solo con `//` y sin tildes, textos visibles en español, y sin tocar entidades ni la versión de la base salvo que la mejora lo justifique.

---

## Mejora 1 — Carnet de vacunas en la ficha de la mascota

**Commit:** `b26c5e3` — *ia: agrega carnet de vacunas en la ficha de la mascota*

### Prompt

```
Proyecto Android en Kotlin con Jetpack Compose y Room (paquete com.delacruz.huellitasvet).
Quiero agregar un carnet de vacunas a la ficha de la mascota, sin dependencias nuevas y
sin XML.

1. Crea una entidad Vacuna (tabla "vacunas") con id autogenerado, mascotaId como
   ForeignKey a Mascota con onDelete CASCADE, índice en mascotaId, nombre, fecha
   (texto "aaaa-MM-dd") y proximaDosis opcional (texto).
2. Crea un VacunaDao con insertar, insertarTodos, actualizar, eliminar y
   observarDeMascota(mascotaId) como Flow ordenado por fecha descendente.
3. Registra la tabla en VetDatabase, sube la versión y usa
   fallbackToDestructiveMigration por ser modo práctica.
4. Declara primero en la interface VetRepository: observarVacunas, guardarVacuna y
   eliminarVacuna. Recién después impleméntalas en VetRepositoryRoom.
5. En MascotaViewModel agrega vacunas(mascotaId) como StateFlow, estadoVacuna(v) con un
   enum EstadoVacuna (VIGENTE; PROXIMA si vence en 30 días o menos; VENCIDA si ya pasó)
   y registrarVacuna con validación de nombre de al menos 3 caracteres.
6. En FichaMascotaScreen agrega una tercera pestaña "Vacunas" con tarjetas por vacuna,
   chip de estado usando los colores del tema, mensaje cuando no hay vacunas y un botón
   "Registrar vacuna" con diálogo y chips de próxima dosis.
7. Siembra 4 vacunas de ejemplo en DatosIniciales.

MVVM: la View solo usa el ViewModel y el ViewModel solo la interface VetRepository.
Comentarios con // y sin tildes; textos visibles en español.
```

### Respuesta resumida de la IA

La IA generó la entidad `Vacuna`, el `VacunaDao` y el enum `EstadoVacuna`, y propuso la consulta `observarDeMascota` como `Flow` ordenado por fecha descendente. En `MascotaViewModel` creó `vacunas(mascotaId)` y un `registrarVacuna`, y en `FichaMascotaScreen` armó la pestaña con una lista de tarjetas y un `AlertDialog`.

### Qué tuve que corregir

- La primera versión leía el DAO/repositorio directamente desde el Composable. Lo moví al ViewModel: la pantalla quedó observando `vm.vacunas(mascotaId)` y `vm.estadoVacuna(v)`.
- Los métodos nuevos no estaban en la interface: los agregué **primero** a `VetRepository` y después a `VetRepositoryRoom`, como pide la consigna.
- A la `ForeignKey` le faltaban `onDelete = ForeignKey.CASCADE` y el `Index("mascotaId")`. Sin el índice, Room avisa por la relación.
- El cálculo del estado comparaba contra la fecha de aplicación en lugar de `proximaDosis`. Lo dejé así: si `proximaDosis` es nula → VIGENTE; si ya pasó → VENCIDA; si vence dentro de 30 días → PROXIMA; en el resto → VIGENTE.
- El mensaje de guardado necesitaba reiniciarse para que el diálogo se cerrara solo; agregué `vacunaGuardada` y `consumirVacunaGuardada()`.

### ¿Respeta MVVM?

Sí. La pestaña Vacunas solo hace `collectAsState()` sobre el ViewModel y dibuja; el registro pasa por `repo.guardarVacuna(...)` desde `MascotaViewModel`. Ningún archivo de `ui/` importa `VacunaDao` ni Room. El flujo queda: entidad → DAO → repositorio → ViewModel → pantalla.

### Reflexión (5 líneas)

1. Agregar una tabla nueva obliga a tocar todas las capas, no solo la pantalla.
2. Declarar primero en la interface me forzó a pensar en el contrato antes de la implementación.
3. El estado de la vacuna depende de la fecha de próxima dosis, no de la de aplicación.
4. Validar en el ViewModel mantiene la pantalla delgada y las reglas en un solo lugar.
5. La IA acelera el esqueleto, pero las decisiones de modelo las tomé yo.

---

## Mejora 2 — Recordatorio de próximo control

**Commit:** `4cdd6de` — *feat(control): implementar Mejora 2 - Recordatorio de próximo control*

### Prompt

```
Continuando con HuellitasVet. Quiero que el veterinario pueda dejar programado un
próximo control al atender una cita, y que el dueño lo vea.

1. Agrega a la entidad Cita un campo opcional fechaControl. Ojo: en este proyecto las
   fechas se guardan como texto "aaaa-MM-dd", no como Long.
2. Crea un enum OpcionControl con etiqueta y dias: sin control, en 1 semana, en 2 semanas,
   en 1 mes y en 3 meses.
3. En AtenderCitaViewModel guarda la opción elegida y, al registrar la atención, calcula
   fechaControl = LocalDate.now().plusDays(dias) cuando la opción no sea "sin control".
4. Actualiza registrarAtencion en CitaDao, VetRepository y VetRepositoryRoom para que
   reciba fechaControl; sube la versión de la base.
5. En AtenderCitaScreen agrega un selector "Próximo control (opcional)" con chips y un
   texto que muestre la fecha calculada en vivo.
6. En InicioViewModel agrega proximoControl: la cita ATENDIDA con fechaControl igual o
   posterior a hoy más cercana. En InicioScreen una tarjeta "Próximo control" con los
   días restantes.
7. Muestra también la fecha de control en el historial (HistorialItem).

Mantén MVVM, no agregues dependencias y no cambies el formato de las fechas del proyecto.
```

### Respuesta resumida de la IA

La IA agregó el campo a la entidad, actualizó el `UPDATE` de `registrarAtencion` y propuso un enum con pocas opciones. En `AtenderCitaViewModel` calculó la fecha y en `InicioViewModel` agregó un flujo nuevo para encontrar el próximo control. En la UI creó un selector de chips y una tarjeta en el Inicio.

### Qué tuve que corregir

- La IA modeló `fechaControl` como `Long` (marca de tiempo). En este proyecto las fechas son texto `"aaaa-MM-dd"`, así que la cambié a `String?` para que sea coherente y se pueda `LocalDate.parse()`.
- El enum venía con tres opciones (sin control, 1 semana, 2 semanas). Lo amplié a cinco (sin control, 1 semana, 2 semanas, 1 mes, 3 meses) con `etiqueta` y `dias`, y así la View no calcula nada: solo recorre `OpcionControl.entries`.
- La pantalla calculaba la fecha con `LocalDate.now()` directamente. El cálculo vive en `AtenderCitaViewModel.registrar(...)`; la View solo muestra el texto de la fecha ya calculada.
- `proximoControl` en la primera versión no filtraba bien: debía ser solo `ATENDIDA`, con `fechaControl` no nula y no pasada, y elegir la más cercana con `minByOrNull`. Lo corregí.
- Subí la versión de la base de 2 a 3 junto con la columna nueva (con `fallbackToDestructiveMigration`).

### ¿Respeta MVVM?

Sí. El cálculo de la fecha vive en `AtenderCitaViewModel` y el filtrado del próximo control en `InicioViewModel`; `InicioScreen` solo lee `vm.proximoControl` y `AtenderCitaScreen` solo lee `vm.opcionControl`. La entidad y el DAO se tocan únicamente a través del repositorio.

### Reflexión (5 líneas)

1. Un campo nuevo en la entidad no es un cambio local: arrastra DAO, repositorio y base.
2. Modelar la fecha como `Long` y no como texto fue el error más fácil de no ver.
3. Las opciones cerradas (enum con etiqueta y días) evitan calcular fechas en la View.
4. El dueño ve el resultado sin pantallas nuevas, solo con un `Flow` más en el Inicio.
5. Elegir "la más cercana" exige filtrar por estado y por fecha futura a la vez.

---

## Mejora 3 — Calendario mensual en la Agenda

**Commit:** `168cf33` — *feat(agenda): implementar Mejora 3 - Calendario mensual en la Agenda*

### Prompt

```
Continuando con HuellitasVet. En la Agenda del veterinario quiero un calendario mensual
además de los chips de la semana, sin librerías y sin LazyVerticalGrid.

1. En CitaDao agrega una consulta que cuente las citas NO canceladas por día entre dos
   fechas (GROUP BY fecha) y devuelva un modelo ConteoFecha con fecha y total.
2. Declara observarCitasPorDia(inicio, fin) en VetRepository e impleméntala en
   VetRepositoryRoom.
3. En AgendaViewModel agrega mesVisible (YearMonth), vistaMes (Boolean) y diasConCitas
   (mapa "aaaa-MM-dd" -> total) usando flatMapLatest para recalcular al cambiar de mes.
   Agrega también mesAnterior, mesSiguiente, irAHoy y alternarVista.
4. En AgendaScreen agrega un botón en la barra que alterne entre chips de semana y
   calendario del mes, con una sola LazyColumn para que todo se deslice junto.
5. El calendario: encabezado con flechas y el mes en español ("Octubre 2026"), iniciales
   L M M J V S D, y cada día con un punto debajo si tiene citas. Al tocar un día deben
   mostrarse sus citas.
6. Sin colores fijos (solo MaterialTheme.colorScheme) y sin DAO ni Repository en la View.
   No cambies entidades ni la versión de la base. No agregues dependencias.
```

### Respuesta resumida de la IA

La IA creó `ConteoFecha` y la consulta `contarPorDia(inicio, fin)`, declaró el método en la interface y lo implementó, y agregó a `AgendaViewModel` el mes visible, la vista semanal/mensual y el mapa de días con citas. Para el calendario propuso una cuadrícula dentro de la lista.

### Qué tuve que corregir

- La IA propuso un `LazyVerticalGrid` dentro de la `LazyColumn`, que la consigna prohíbe. Armé la cuadrícula con `Row` + `Modifier.weight(1f)` y calculé las filas con aritmética de fechas (`vacias + totalDias`, redondeo a 7).
- El título del mes salía en minúscula y sin mayúscula inicial. Lo dejé como `"Octubre 2026"` con `replaceFirstChar { it.uppercase() }` y `Locale("es-PE")`.
- Los puntos se calculaban desde una lista; los pasé a un mapa con `associate { it.fecha to it.total }` para consultar por día sin recorrer toda la lista.
- No se recalculaba al cambiar de mes. Agregué `flatMapLatest` sobre `mesVisible` para volver a consultar el rango del mes mostrado.
- La cuadrícula debía empezar en lunes: dejé `mes.atDay(1).dayOfWeek.value - 1` celdas vacías al inicio.
- Había colores fijos (`Color(0xFF...)`); los cambié por `MaterialTheme.colorScheme`.

### ¿Respeta MVVM?

Sí. `diasConCitas` se arma en `AgendaViewModel` a partir de `repo.observarCitasPorDia(...)`; `AgendaScreen` solo hace `collectAsState()` y dibuja. La pantalla no importa DAO ni Room. *(Nota: `PanelViewModel` ya importaba algunos tipos de `ui/` para los gráficos; es una excepción previa, tolerada por la consigna, que no forma parte de esta mejora.)*

### Reflexión (5 líneas)

1. El calendario se resuelve con aritmética de fechas y sin librerías externas.
2. La restricción de no anidar listas perezosas cambió por completo la cuadrícula.
3. Un solo `flatMapLatest` por mes evita recalcular de más.
4. Un mapa "fecha → total" es más simple de consultar que una lista.
5. La IA propuso el camino fácil (`LazyVerticalGrid`) y la consigna marcó el correcto.

---

## Resumen de la Fase 2

| Mejora | Prompt: qué pedí | Commit | Resultado verificado |
|---|---|---|---|
| 1 | Carnet de vacunas (tabla, DAO, repositorio, estado y pestaña en la ficha) | `b26c5e3` | Pestaña Vacunas con tarjetas, chip Vigente/Próxima/Vencida y registro con validación |
| 2 | Próximo control al atender, recordatorio en el Inicio y en el historial | `4cdd6de` | Selector de control en Atender cita; el dueño ve "Próximo control: …" con los días restantes |
| 3 | Calendario mensual en la Agenda con puntos por día | `168cf33` | Vista semana/mes, puntos en días con citas, flechas de mes y día seleccionado |

## Cómo se verificó

Cada mejora se compiló en modo offline antes de confirmarla:

```powershell
.\gradlew.bat :app:compileDebugKotlin --offline
# BUILD SUCCESSFUL
```

Las tres respetan la arquitectura por capas del proyecto (View → ViewModel → `VetRepository` → DAO → Room). El detalle completo de la Mejora 3, con los archivos íntegros, quedó además en `propuestas.md`.

## Relacionado

- [README.md](README.md) — descripción del proyecto, arquitectura y pruebas de aceptación.
- `propuestas.md` — volcado de la Mejora 3 (calendario mensual).
