# Semana 05: Refactorizacion a Interfaz de Alta Fidelidad con Jetpack Compose y Asistencia de IA

En este laboratorio se realizo la refactorizacion completa de la aplicacion movil NavLab utilizando Jetpack Compose, elevando la interfaz a un nivel de alta fidelidad mediante el uso de asistentes de Inteligencia Artificial para el diseno UI/UX y la estructura de navegacion.

## Modulos Refactorizados

- LoginScreen: Implementacion de validacion de campos vacios para correo y contrasena con mensajes de error visuales.
- HomeScreen: Gradiente vertical lila/blanco, saludo dinamico centrado y tarjetas estilizadas para navegacion rapida.
- DirectoryScreen: Lista optimizada con 5 estudiantes, avatares circulares y fondo claro tipo tarjeta.
- StudentDetailScreen: Cabecera curva morada con foto superpuesta y tarjeta de expediente academico detallada.
- ProfileConfigScreen: Cabecera degradada y organizacion de tarjetas para datos personales y configuracion.

## Prompt Global Utilizado para la Refactorizacion
Por favor, TRANSFORMA Y REESTRUCTURA nuestro proyecto base simple de Jetpack Compose aplicando de forma estricta las siguientes especificaciones exactas de diseño y flujo:

1. PANTALLA DE LOGIN (`LoginScreen.kt`):
   - TÍTULO: "Portal Académico" en morado oscuro.
   - SUBTÍTULO: Exactamente "Accede a tu cuenta" en gris (NO poner "Ingrese sus credenciales para continuar").
   - CAMPOS DE TEXTO: 
     * "Correo Institucional" (con icono de sobre).
     * "Contraseña" (con icono de candado y ojo para ver/ocultar).
     * AMBOS CAMPOS DEBEN ESTAR COMPLETAMENTE VACÍOS POR DEFECTO.
   - VALIDACIÓN OBLIGATORIA: El botón "INICIAR SESIÓN" NO debe permitir ingresar si los campos están vacíos. Debe requerir correo y contraseña para dar paso a la pantalla principal.
   - FONDO: Degradado lila/violeta pastel SUMAMENTE tenue y claro (casi blanco).

2. PANTALLA DE MENÚ PRINCIPAL (`HomeScreen.kt`):
   - FONDO GENERAL: Degradado vertical completo y continuo que comienza en la parte más alta como un morado pastel o lila suave y se va difuminando suavemente hacia abajo hasta convertirse en blanco puro en la base.
   - ALINEACIÓN Y TEXTOS SUPERIORES: Todo el texto superior debe estar ESTRICTAMENTE CENTRADO.
   - SALUDO DINÁMICO LIMPIO EN DOS LÍNEAS (Sin signos adicionales como '+'): Tipografía sans-serif blanca y en negrita que muestra el saludo en dos líneas con el mismo tamaño grande, tomando el nombre del usuario autenticado que inicia sesión (ejemplo: si ingresa Minji, dirá "Minji"):
     * Línea 1: "Bienvenido,"
     * Línea 2 (justo abajo): "[Nombre del usuario que ingresó]" (ejemplo: "Minji", "Noemí de la Cruz", etc.).
   - ESPACIADO Y SUBTÍTULO: Incluir un espacio vertical amplio debajo del nombre, seguido por el subtítulo más pequeño en blanco semi-transparente que dice: "¿Qué deseas gestionar hoy?".
   - TARJETAS DE MENÚ: Centradas abajo, dos tarjetas blancas rectangulares dispuestas verticalmente con esquinas redondeadas y sombra suave:
     1. "Directorio de Alumnos" (subtítulo: "Ver y gestionar estudiantes").
     2. "Mi Perfil Académico" (subtítulo: "Datos personales y progreso").
   - PIE DE PÁGINA: En la parte inferior central, sobre el fondo blanco, un icono de salida delgado junto al texto en color rojo "Cerrar Sesión Segura".

3. PANTALLA DE DIRECTORIO DE ALUMNOS (`DirectoryScreen.kt`):
   - FONDO: Blanco lavanda claro (sin fondos negros ni oscuros).
   - BARRA SUPERIOR: Morado pastel suave con "<- Directorio de Alumnos".
   - LISTA CON FOTOS REALES (Usar marcador de imagen/Avatar circular con foto real para cada estudiante, NINGUNA inicial):
     1. Foto de perfil 1 - Juan León - Ingeniería de Sistemas
     2. Foto de perfil 2 - Maria Garcia - Arquitectura
     3. Foto de perfil 3 - Carlos Perez - Medicina
     4. Foto de perfil 4 - Ana Lopez - Derecho
     5. Foto de perfil 5 - Luis Ramirez - Administración
   - NAVEGACIÓN: Al hacer clic en una tarjeta, abrir su Expediente Académico.

4. PANTALLA DE EXPEDIENTE ACADÉMICO (`StudentDetailScreen.kt`):
   - NAVEGACIÓN: Accesible únicamente desde la lista del Directorio.
   - BARRA SUPERIOR: "<- Expediente Académico".
   - CABECERA Y ESTÉTICA (MOCKUP ALTA FIDELIDAD):
     * Bloque de cabecera superior con degradado morado intenso y esquinas inferiores muy redondeadas (efecto curvo/onda).    * Fotografía de perfil circular grande superpuesta en el borde inferior del bloque morado, con un borde exterior blanco delgado.
     * Directamente debajo de la foto, el nombre "Juan León" en letra oscura negrita y su carrera "Ingeniería de Sistemas" en un tono morado más claro.
   - TARJETA ÚNICA DE DATOS:
     * Una sola tarjeta grande de color gris claro/lavanda con esquinas muy redondeadas.
     * Cada elemento académico inicia con un pequeño icono cuadrado morado sólido, seguido de la etiqueta y valor:
       - "ID Estudiante": "2024-0001"
       - "Correo": "juan.leon@example.com"
       - "Facultad": "Ingeniería y Tecnología" (Sin repetir la palabra Facultad en el texto del valor).
     * Línea divisoria horizontal delgada.
     * Sección "Biografía" en la parte inferior: Título en negrita y el párrafo exacto: "Estudiante destacado por interés en desarrollo Android."

5. PANTALLA DE CONFIGURACIÓN DE PERFIL (`ProfileConfigScreen.kt`):
   - BARRA SUPERIOR: "<- Configuración de Perfil".
   - CABECERA: Tarjeta con degradado horizontal (morado a caoba/vino rojizo) que contiene únicamente la FOTO DE PERFIL REAL circular de Juan León y debajo su nombre completo "Juan León Suiyon".
   - REGLA STRICTA: NO incluir ningún texto de rol o subtítulo como "perfil académico de usuario" ni "Estudiante Institucional" debajo del nombre. Solo mostrar el nombre.
   - SECCIONES EN TARJETAS BLANCAS:
     * INFORMACIÓN PERSONAL: Nombre ("Juan León Suiyon"), Correo ("juan.leon@tecsup.edu.pe"), Teléfono ("+51 987 654 321").
     * ACADÉMICO: Carrera ("Ingeniería de Software"), Ciclo Actual ("VI Ciclo").
   - CERRAR SESIÓN: Botón plano inferior en texto e icono rojo "Cerrar Sesión".

Aplica esta reestructuración directamente sobre los composables existentes.
## Tecnologias Utilizadas

- Lenguaje: Kotlin
- Framework UI: Jetpack Compose (Material Design 3)
- Navegacion: Jetpack Navigation Compose
- Arquitectura: Clean Architecture / UI State Pattern

## Captura de pantalla del resultado:
| Inicio de Sesión | Menú Principal | Directorio de Alumnos | Expediente Académico |
| :---: | :---: | :---: | :---: |
| <img width="720" height="1612" alt="image" src="https://github.com/user-attachments/assets/ebaa54a1-5009-4f9b-8718-4e8c931695ae" /> | <img width="720" height="1612" alt="image" src="https://github.com/user-attachments/assets/8002af09-0cd0-4039-8bc7-f0aabc03c644" /> | <img width="720" height="1612" alt="image" src="https://github.com/user-attachments/assets/4ee685f6-fef3-4dfc-813c-472d1be45e7e" /> | <img width="720" height="1612" alt="image" src="https://github.com/user-attachments/assets/49f2d791-3a12-40dd-97b0-d2f775e285a6" /> |

| Configuración de Perfil |
| :---: |
| <img width="260" alt="image" src="https://github.com/user-attachments/assets/56b00a6a-03f1-4502-a093-c5f11780e3aa" /> |


