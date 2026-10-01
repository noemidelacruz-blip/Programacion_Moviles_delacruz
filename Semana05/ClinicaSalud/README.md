# Aplicación Móvil - Clínica Salud

**Curso:** Programación en Dispositivos Móviles  
**Estudiante:** Noemí De La Cruz  
**Evaluación:** Semana 05 - Navegación Secundaria y Gestión de Citas Médicas  
**Framework UI:** Jetpack Compose (Material Design 3)  

---

## Índice
1. [Descripción General](#descripción-general)
2. [Arquitectura y Estructura del Código](#arquitectura-y-estructura-del-código)
3. [Detalle de Componentes Desarrollados](#detalle-de-componentes-desarrollados)
4. [Flujo de Pantallas y Navegación](#flujo-de-pantallas-y-navegación)
5. [Manejo de Estados y Persistencia Temporal](#manejo-de-estados-y-persistencia-temporal)
6. [Guía de Instalación y Ejecución](#guía-de-instalación-y-ejecución)
7. [Capturas de Referencia](#capturas-de-referencia)

---
## Capturas de Pantalla

| Menú Lateral (Drawer) | Pantalla de Inicio | Perfil del Doctor | Agendar Cita |
| :---: | :---: | :---: | :---: |
| <img width="420" alt="image" src="https://github.com/user-attachments/assets/a1465f94-0751-48f9-b5d7-158c91fbd137" /> | <img width="420" alt="image" src="https://github.com/user-attachments/assets/9cf08cce-9ee7-40fb-8ad1-010f5a8d0e6f" /> | <img width="420" alt="image" src="https://github.com/user-attachments/assets/7de21927-8dc1-4849-bf56-22b82c7f5b79" /> | <img width="420" alt="image" src="https://github.com/user-attachments/assets/21c62567-b812-4334-818a-4dd96fe5f157" /> |

| Confirmación de Cita | Mis Citas |
| :---: | :---: |
| <img width="420" alt="image" src="https://github.com/user-attachments/assets/db235f5f-629e-4819-9514-0ea0bcb7d738" /> | <img width="420"  alt="image" src="https://github.com/user-attachments/assets/cdc36a52-af77-4696-b2c8-65f1a036afa9" /> |
## Descripción General

La aplicación Clínica Salud es una solución móvil nativa para Android desarrollada bajo las pautas de Material Design 3. Proporciona a los pacientes una interfaz centralizada donde pueden explorar médicos especialistas, agendar nuevas citas médicas, consultar la confirmación detallada de su reserva y gestionar sus citas programadas a través de un menú lateral interactivo (ModalNavigationDrawer).

---

## Arquitectura y Estructura del Código

El proyecto está organizado siguiendo una estructura modular basada en componentes `@Composable` desacoplados para garantizar la mantenibilidad y reutilización del código:

```text
com.delacruz.clinicasalud/
├── MainActivity.kt           # Punto de entrada de la app, inicialización de NavHost y estado global de citas.
├── NavegacionDrawer.kt       # Contenedor del ModalNavigationDrawer y diseño personalizado de la barra lateral.
├── PantallaInicio.kt         # Lista de médicos especialistas disponibles para agendar.
├── PantallaPerfilDoctor.kt   # Vista detallada con información, biografía y especialidad del médico.
├── PantallaAgendarCita.kt    # Formulario para la selección de fecha y horario de consulta.
├── PantallaConfirmacion.kt  # Resumen final de la cita médica agendada.
└── PantallaMisCitas.kt       # Listado dinámico de citas activas e historial del paciente.

