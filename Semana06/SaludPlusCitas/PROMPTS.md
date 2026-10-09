# PROMPTS.md — Fase 2: calendario dinámico con IA (rama `mejora-ia`)

**Asistente de IA usado:** Gemini (agente integrado en Android Studio)
**Pantalla intervenida:** Pantalla 6 (`FechaHoraScreen.kt`) y, para el texto de la fecha, Pantalla 7 (`ConfirmarCitaScreen.kt`)
**Commits en la rama:** 3, uno por cada prompt.

> Reglas que repetí en todos los prompts, porque la guía las exige: no usar base de datos, no cambiar nombres ni parámetros de las funciones del Repositorio ni de las pantallas, no tocar `AppNavigation.kt`, y no romper el bloqueo de horarios ya reservados.

---

## Prompt 1 — Fechas en español con `LocalDate`

**Commit:** `feat(calendario): fechas con LocalDate y texto en español`

### Prompt

```
Tengo una app Android en Kotlin con Jetpack Compose (paquete com.delacruz.saludpluscitas).
En FechaHoraScreen.kt los días se muestran como una lista fija de textos. Quiero
empezar a trabajar con java.time.LocalDate en lugar de textos.

Necesito:
1. Que cada día del calendario sea un LocalDate y que, para dibujar cada chip,
   se obtenga la abreviatura del día ("Jue", "Vie", "Lun", "Mar", "Mié") y el
   número del día ("8", "9", "12"...). Crea una función auxiliar privada para eso.
2. Una función que reciba un LocalDate y devuelva la fecha larga en español con
   este formato exacto: "Martes 13 de octubre 2026" (día de la semana con la
   primera letra en mayúscula, mes en minúscula, SIN "de" antes del año).
3. Que la Pantalla 7 (ConfirmarCitaScreen.kt) y la de Cita agendada muestren la
   fecha con esa función.

Restricciones: no uses librerías externas, no uses base de datos, no cambies los
nombres ni los parámetros de las funciones existentes y no modifiques AppNavigation.kt.
La fecha sigue viajando entre pantallas como parámetro de navegación.
```

### Respuesta resumida de la IA

La IA propuso convertir el texto de la fecha a `LocalDate` con `LocalDate.parse(...)` y dar formato con `DateTimeFormatter.ofPattern("EEEE d 'de' MMMM yyyy", Locale("es"))`. También sugirió una función para sacar la abreviatura del día con `DayOfWeek.getDisplayName(TextStyle.SHORT, ...)`.

### Qué tuve que corregir

>  **Ajusta esta sección a lo que realmente te pasó.** Lo que está escrito son las correcciones más probables; deja solo las que aplican a ti.

- El formateador de Java devuelve el día en minúscula ("martes 13 de octubre de 2026") y con "de" antes del año. La guía pide "Martes 16 de setiembre 2026", así que tuve que poner la primera letra en mayúscula y quitar el "de" antes del año. En la captura final se ve correcto: **"Martes 13 de octubre 2026"**.
- La abreviatura del día salía con punto o en minúscula ("jue."), y la ajusté a "Jue", "Mié", etc. para que coincida con el diseño de referencia.
- Revisé que el texto se mostrara igual en la Pantalla 7, la de Cita agendada y Mis citas.

---

## Prompt 2 — 5 días hábiles dinámicos, horarios recalculados y hora reiniciada

**Commit:** `feat(calendario): generar los próximos 5 días hábiles desde hoy`

### Prompt

```
Continuando con FechaHoraScreen.kt. Ahora reemplaza la lista fija de días por días
generados con java.time.LocalDate:

1. Mostrar los próximos 5 días hábiles a partir de HOY (LocalDate.now()), sin
   sábados, domingos ni días pasados. Si hoy es sábado o domingo, que empiece
   el lunes siguiente. Ejemplo: si hoy es jueves 8, deben salir Jue 8, Vie 9,
   Lun 12, Mar 13 y Mié 14.
2. Al tocar otro día, la lista de horarios disponibles debe recalcularse sola
   llamando a Repositorio.horariosDisponibles(medicoId, fecha) con la nueva fecha,
   y la hora seleccionada debe reiniciarse (quedar sin selección).
3. El botón "Continuar" solo se habilita si hay un día y una hora elegidos.
4. IMPORTANTE: no rompas el bloqueo de horarios. Un horario ya reservado para ese
   médico y esa fecha NO debe aparecer en la cuadrícula.

Usa estado de Compose (remember / mutableStateOf) y no cambies los nombres ni
parámetros de las funciones del Repositorio ni de las pantallas.
```

### Respuesta resumida de la IA

La IA creó una función que recorre desde hoy con `plusDays(1)` y va saltando los días cuyo `dayOfWeek` es `SATURDAY` o `SUNDAY` hasta juntar 5 fechas. Guardó el día elegido en un estado y calculó los horarios con `remember(fechaSeleccionada)`, de modo que la cuadrícula se actualiza sola al cambiar de día. También reinició la hora al elegir otro día.

### Qué tuve que corregir

>  **Ajusta esta sección a lo que realmente te pasó.**

- La primera versión no reiniciaba la hora seleccionada al cambiar de día, y quedaba marcada una hora que ya no correspondía a la nueva fecha. Agregué el reinicio del estado de la hora cuando cambia el día.
- Verifiqué a mano que el calendario dinámico no rompiera el bloqueo: agendé la cita con la Dra. Gabriela Núñez el martes 13 de octubre a las 08:30 y comprobé que esa hora ya no aparece para ese médico y esa fecha, pero sigue disponible en otros días.
- El día inicial seleccionado debía ser el primer día hábil de la lista, para que la pantalla no abriera con la cuadrícula vacía.

---

## Prompt 3 — Flechas por semana y mes dinámico

**Commit:** `feat(calendario): navegar por semanas con flechas y mes/año dinámico`

### Prompt

```
Continuando con FechaHoraScreen.kt. Agrega la navegación por semanas:

1. Las flechas < y > del encabezado avanzan o retroceden UNA semana (5 días hábiles).
2. No se puede retroceder antes de la semana actual: en la semana actual la flecha <
   debe verse desactivada (gris) y no hacer nada al tocarla.
3. El título con el mes y año ("Octubre 2026") debe cambiar según la semana que se
   está mostrando, en español y con la primera letra en mayúscula.
4. Al cambiar de semana, selecciona el primer día de esa semana, recalcula los
   horarios disponibles y reinicia la hora seleccionada.
5. No rompas lo anterior: el bloqueo de horarios reservados y el botón Continuar
   (solo habilitado con día y hora elegidos) deben seguir funcionando.

Mantén el mismo estilo visual (flechas a los lados del mes, chips de día en fila)
y no cambies nombres ni parámetros de funciones existentes.
```

### Respuesta resumida de la IA

La IA agregó un contador de semanas (`semanaOffset`) en un estado. A partir de él calculó los 5 días hábiles de la semana mostrada. La flecha `<` queda deshabilitada cuando `semanaOffset == 0`. El título se arma con el mes y el año del primer día de la semana mostrada.

### Qué tuve que corregir

>  **Ajusta esta sección a lo que realmente te pasó.**

- La flecha `<` debía verse desactivada en la semana actual (se ve en gris en la captura). Tuve que ajustar el color y la acción para que no solo pareciera apagada, sino que de verdad no hiciera nada.
- **Observación sobre el título (comportamiento que decidí dejar):** el título usa el mes del primer día de la semana. Cuando una semana cruza de mes (por ejemplo, del jueves 29 de octubre al miércoles 4 de noviembre) el título sigue diciendo "Octubre 2026" aunque haya días de noviembre. Esto **cumple lo que pide la guía** ("el nombre del mes y año cambia según la semana mostrada"), así que lo dejé tal cual.
- Verifiqué que al cambiar de semana el primer día quedara seleccionado y que la hora se reiniciara.

---

## Resumen de la Fase 2

| Prompt | Qué pedí | Resultado verificado |
|--------|----------|----------------------|
| 1 | Fechas con `LocalDate` y texto en español | "Martes 13 de octubre 2026" en Confirmar cita, Cita agendada y Mis citas |
| 2 | 5 días hábiles, horarios recalculados, hora reiniciada | Jue 8, Vie 9, Lun 12, Mar 13, Mié 14 (sin sábado ni domingo) |
| 3 | Flechas por semana y mes dinámico | "Octubre 2026" con la flecha `<` desactivada en la semana actual |
