# TecsupFit - Desarrollo Asistido con Inteligencia Artificial (con-ia)

Este proyecto corresponde a la aplicación móvil TecsupFit desarrollada en Kotlin con Android Jetpack Compose. En esta rama (con-ia), la implementación del flujo dinámico de reservas y la navegación entre pantallas fueron desarrolladas y optimizadas mediante asistencia continua de Inteligencia Artificial.

---

## Cumplimiento de Requisitos Funcionales

La aplicación cumple con el 100% de los requisitos funcionales especificados para la Opción B:

1. Pantalla Inicio:
   - Implementación de LazyRow con chips de filtro horizontales (Hoy / Esta semana).
   - Implementación de LazyColumn para la lista de clases en tarjetas individuales con nombre y horario.

2. Detalle de Clase:
   - Recepción de datos de la clase seleccionada mediante parámetros de navegación.
   - Botón interactivo "Reservar cupo" para iniciar el proceso de reserva.

3. Confirmación:
   - Resumen visual completo de la reserva realizada (nombre de clase y horario).
   - Botón de navegación directa "Ver mis reservas".

4. Barra de Navegación Inferior (bottomBar):
   - Barra visible e interactiva en las pantallas principales (Inicio, Reservas, Rutinas y Perfil).
   - Resaltado dinámico del ícono y texto activo según la pantalla seleccionada.

5. Pantalla Reservas:
   - Lista renderizada mediante LazyColumn con las clases reservadas dinámicamente.
   - Indicador visual diferenciado para el estado de la reserva (Confirmada).
   - Diálogo de confirmación interactivo para la cancelación de reservas.

6. Pantalla Perfil:
   - Visualización de datos del usuario y membresía.
   - Panel de estadísticas simples con métricas de clases tomadas y racha de asistencia.
---

## Capturas de Pantalla

| Pantalla Inicio | Detalle de Clase | Confirmación | Mis Reservas |
| :---: | :---: | :---: | :---: |
| <img width="350" alt="image" src="https://github.com/user-attachments/assets/9bfd6541-8c08-40a1-ab9e-e915f416f746" />|<img width="350" alt="image" src="https://github.com/user-attachments/assets/3ac4ae4d-aa91-407b-a15c-337f6c0955d5" />|<img width="350" alt="image" src="https://github.com/user-attachments/assets/34435d3f-ffd4-4048-a971-c26956652525" />|<img width="350" alt="image" src="https://github.com/user-attachments/assets/287fb02a-6b01-4dab-ab07-edfc494fbc61" />|

| Cancelar Reserva | Rutinas | Perfil |
| :---: | :---: | :---: |
| <img width="350" alt="image" src="https://github.com/user-attachments/assets/f3b19910-8b2a-4174-ba88-35642c786761" />| <img width="350" alt="image" src="https://github.com/user-attachments/assets/63e2b134-635f-428a-b5d2-3f141fdaa3c9" />| <img width="350" alt="image" src="https://github.com/user-attachments/assets/6cf4d36b-adc3-4ad4-a536-7fb57fd61753" />|

## Prompts Utilizados para la Generación de Código

A continuación se detallan los prompts empleados durante la interacción con la Inteligencia Artificial para refactorizar y construir los componentes:

### 1. Refactorización e Integración de Modelos de Datos
"Tengo un problema en NavegacionApp. Al reservar una ClaseFit, necesito convertirla al modelo Reserva e incluirla en una lista global en lugar de tener datos estáticos. Ayúdame a mapear el objeto ClaseFit a Reserva evitando duplicados por nombre de clase y asegurando que la lista de reservas sea accesible globalmente."

### 2. Sincronización de Navegación y Pestañas Dinámicas
"En NavegacionApp necesito que después de dar clic en 'Ver Reservas' en PantallaConfirmacion, el usuario regrese a PantallaPrincipal pero abriendo directamente la segunda pestaña (Reservas). ¿Cómo puedo pasar un parámetro tabInicial y hacer que PantallaPrincipal actualice el indiceSeleccionado usando LaunchedEffect?"

### 3. Convertir PantallaReservas en un Componente Stateless
"Quiero eliminar la lista hardcodeada de PantallaReservas.kt. Necesito que reciba listaReservas: List<Reserva> y un callback onEliminarReserva: (Reserva) -> Unit como parámetros desde PantallaPrincipal. También ayúdame a agregar un diálogo emergente de confirmación cuando el usuario intente cancelar o eliminar una reserva."

### 4. Corrección de Errores de Compilación y Tipos
"Tengo errores de compilación al conectar NavegacionApp, PantallaPrincipal y PantallaReservas debido a desajustes en los parámetros y firmas de funciones composables. Revisa los tres archivos y entrega el código limpio y corregido para que la aplicación compile sin fallas."

### 5. Estructuración del Historial de Commits
"Proporcióname los mensajes de commit estructurados según las normas de Conventional Commits para registrar los cambios de navegación, la lista dinámica de reservas y la refactorización de estados."

---

## Tecnologías y Herramientas

- Lenguaje: Kotlin
- UI Framework: Jetpack Compose (Material 3)
- Navegación: Compose Navigation
- Control de Versiones: Git / GitHub
- Asistencia IA: Prompt Engineering enfocado en arquitectura Android
