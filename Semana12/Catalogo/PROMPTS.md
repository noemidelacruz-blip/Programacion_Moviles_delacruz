# PROMPTS.md

Consultas hechas a la IA en la Fase 2 (proyecto Catalogo, tema Productos,
variante beauty).

---

## Consulta 1 - Revisión del proyecto contra el PDF

- **Fecha:** 10/11/2026
- **Herramienta:** Gemini
- **Prompt:** «quiero que veas si cumple con todo lo que pide el pdf
  porfavor», con el enlace al repositorio y el PDF del laboratorio.
- **Qué respondió (resumen):** La Fase 1 cumple R01 a R11. Faltan las pruebas
  unitarias (R12), el `PROMPTS.md`, el README, el Pull Request y el tag `v1.0`.
  También señaló que `refrescar()` solo captura `IOException` y
  `HttpException`.
- **Qué usamos / qué cambiamos y por qué:** Usamos la lista de pendientes para
  planear la Fase 2.
- **Qué aprendimos:** Qué pide cada requisito y qué pertenece a la Fase 2.

---

## Consulta 2 - Qué va en cada commit de la Fase 2

- **Fecha:** 10/11/2026
- **Herramienta:** Gemini
- **Prompt:** Pregunté en qué commit iba cada cosa que faltaba y qué cambios
  podía hacer en la rama con IA, ya que el PDF no lo especifica.
- **Qué respondió (resumen):** Propuso commits separados: pruebas del
  `ListaViewModel`, captura de errores inesperados en `ItemRepositoryImpl`,
  botón Reintentar en la lista vacía (con un mockup) y `PROMPTS.md`.
- **Qué usamos / qué cambiamos y por qué:** Usamos las pruebas, el arreglo de
  errores y el `PROMPTS.md`. Descartamos el botón Reintentar porque no lo
  exige el PDF.
- **Qué aprendimos:** Cada commit debe tener una sola idea y compilar.

---

## Consulta 3 - Cómo controla la app las caídas

- **Fecha:** 10/11/2026
- **Herramienta:** gemini
- **Prompt:** «Como controla las caidas de la aplicacion?»
- **Qué respondió (resumen):** La app maneja los errores esperados (sin
  internet, error HTTP, credenciales incorrectas, permiso de notificaciones
  negado), pero no otras excepciones, como un JSON mal formado.
- **Qué usamos / qué cambiamos y por qué:** En `ItemRepositoryImpl` agregamos
  un `catch` de `Exception`, precedido por uno de `CancellationException` que
  vuelve a lanzar la excepción.
- **Qué aprendimos:** La diferencia entre errores esperados e inesperados, y
  que `CancellationException` va antes del `catch` general.

---

## Consulta 4 - Instrucciones para el agente de programación

- **Fecha:** 10/11/2026
- **Herramienta:** Gemini
- **Prompt:** Pedí instrucciones claras y cortas para mi agente: pruebas del
  `ListaViewModel`, corrección de errores y `PROMPTS.md`.
- **Qué respondió (resumen):** Una lista corta de pasos, uno por commit:
  3 pruebas con un `FakeItemRepository` y un `FakeNotificador`, los `catch`
  adicionales en el repositorio y la plantilla del `PROMPTS.md`.
- **Qué usamos / qué cambiamos y por qué:** Se las pasamos al agente. Traer la
  base de Catalogo a la rama `con-ia` (clonar y cambiar de rama) lo hicimos
  nosotros a mano, no el agente.
- **Qué aprendimos:** Las pruebas funcionan porque el repositorio es una
  interface y se puede reemplazar por uno falso.

---

## Reflexión final

La IA nos ayudó a revisar el proyecto contra la rúbrica y a plantear las
pruebas y el manejo de errores. Nosotros decidimos qué aplicar y descartamos
lo que el PDF no exige. Aprendimos que usar interfaces facilita probar el
ViewModel y que conviene separar los errores esperados de los inesperados.