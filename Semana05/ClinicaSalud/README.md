# Clínica Salud - App Móvil en Jetpack Compose

Aplicación móvil para la gestión de citas médicas, consulta de historial clínico y gestión de perfil de usuario. Desarrollada de manera nativa para Android utilizando **Kotlin** y **Jetpack Compose**.

---

## Capturas de Pantalla

| Menú Lateral  | Perfil del Doctor | Agendar Cita | Confirmación de Cita |
| :---: | :---: | :---: | :---: |
| <img width="420" alt="Menú Lateral" src="https://github.com/user-attachments/assets/96098287-9c59-44f9-a345-d3f80c8d4654" /> | <img width="420" alt="Perfil del Doctor" src="https://github.com/user-attachments/assets/efd1faf9-3950-4da4-8d31-d1b3a940f5d4"/> | <img width="420" alt="Agendar Cita" src="https://github.com/user-attachments/assets/73ef0d81-8203-4fb0-bc5f-6b19a9678c7f" /> | <img width="420" alt="Confirmación de Cita" src="https://github.com/user-attachments/assets/65ff8dd8-77ae-42ca-baee-a4aebbbd540f" /> |

| Mis Citas | Cancelar Cita | Historial Médico | Perfil de Usuario |
| :---: | :---: | :---: | :---: |
| <img width="420" alt="Mis Citas" src="https://github.com/user-attachments/assets/c0b0dfdf-d02f-42d2-9739-bc7ac8af5cd1" /> | <img width="420" alt="Cancelar Cita" src="https://github.com/user-attachments/assets/9a5a21ca-3027-4888-ae96-cd5050d9236d" /> | <img width="420" alt="Historial Médico" src="https://github.com/user-attachments/assets/0eb506da-a741-4a88-8ada-b68ef5212507" /> | <img width="420" alt="Perfil de Usuario" src="https://github.com/user-attachments/assets/6caa4c10-17a7-4355-9f47-262d3ee29100" /> |

---

## Descripción General

**Clínica Salud** es una solución móvil diseñada para facilitar la interacción entre pacientes y servicios médicos. La aplicación permite a los usuarios explorar los médicos disponibles, agendar citas, consultar su historial médico y gestionar su información personal mediante un perfil interactivo.

---

## Funcionalidades Principales

1. **Pantalla de Inicio (`HomeScreen`)**: Lista de médicos disponibles con especialidades, calificación y experiencia.
2. **Detalle del Médico (`DoctorDetailScreen`)**: Información detallada, biografía y acceso a la agenda.
3. **Agendamiento de Citas (`AppointmentScreen`)**: Selección de fecha y horario para reservar una consulta.
4. **Confirmación de Cita (`ConfirmationScreen`)**: Resumen de la reserva con opciones de navegación.
5. **Mis Citas (`AppointmentsScreen`)**: Gestión de citas agendadas y opción para cancelar reservas.
6. **Historial Médico (`MedicalHistoryScreen`)**: Registro de consultas previas y diagnósticos del paciente.
7. **Mi Perfil (`ProfileScreen`)**: Datos personales (DNI, teléfono, dirección, tipo de sangre) y formulario modal para edición en tiempo real.
8. **Menú Lateral (`AppDrawer`)**: Menú deslizable para la navegación fluida entre las secciones principales.

---

## Tecnologías Utilizadas

* **Lenguaje:** Kotlin
* **UI Framework:** Jetpack Compose (Material Design 3)
* **Navegación:** Jetpack Navigation Compose (`NavHost`, `NavController`)
* **Gestión de Estado:** `mutableStateOf`, `remember`

---
---

## Cumplimiento de Requisitos Funcionales (

La aplicación cumple al **100% (6 de 6)** con todos los requisitos funcionales establecidos para el proyecto:

| Requisito | Estado | Detalles de Implementación |
| :--- | :---: | :--- |
| **1. Pantalla de Inicio** |  **Cumplido** | Incluye `LazyRow` con chips para filtrar especialidades y `LazyColumn` con tarjetas de médicos que muestran nombre, especialidad, experiencia y calificación. |
| **2. Perfil del Médico** |  **Cumplido** | Recibe la información del médico mediante parámetros de navegación y presenta la opción principal con el botón *"Agendar cita"*. |
| **3. Agendar Cita** |  **Cumplido** | Dispone de selección única para fechas (3 opciones) y horas (3 opciones). |
| **4. Confirmación de Cita** |  **Cumplido** | Presenta la pantalla de resumen con el médico, fecha y hora agendada, junto con el botón *"Volver al inicio"*. |
| **5. Menú Lateral (Drawer)** |  **Cumplido** | Accesible mediante el ícono `≡` en la `topBar` e integra los destinos principales (*Inicio*, *Mis citas*, *Historial médico* y *Perfil*). |
| **6. Mis Citas** |  **Cumplido** | Lista las citas mediante `LazyColumn` y diferencia visualmente sus estados (*Confirmada* / *Completada*). |
## Prompts Utilizados para el Desarrollo Asistido por IA

### **1. Integración del Menú Lateral y Navegación**
> *"Ayúdame a conectar las pantallas de la app mediante un `NavigationDrawer` en Jetpack Compose. Necesito que las opciones 'Inicio', 'Mis citas', 'Historial médico' y 'Perfil' permitan cambiar de pantalla de manera fluida utilizando `NavController`, reemplazando los mensajes de prueba por acciones reales de navegación."*

### **2. Rediseño Visual de la Pantalla de Perfil**
> *"La pantalla de perfil luce muy básica. Ayúdame a rediseñarla en Jetpack Compose utilizando `Material3`. Quiero una tarjeta que organice la información personal (DNI, teléfono, dirección y tipo de sangre) con sus respectivos íconos, un avatar con iniciales y una presentación visual más limpia y profesional."*

### **3. Implementación de Estado Dinámico para Edición de Perfil**
> *"En la pantalla de perfil, añade la funcionalidad para que al presionar el botón 'Editar información' se abra un `AlertDialog`. Este diálogo debe contener campos de texto para modificar Nombre, Correo, Teléfono y Dirección, actualizando los datos en la pantalla de forma instantánea mediante variables de estado (`mutableStateOf`)."*

---
