# NavLab - Aplicación de Navegación en Jetpack Compose (Rama: sin-ia)

Proyecto Android desarrollado en Kotlin y Jetpack Compose para demostrar el flujo de navegación entre pantallas, envío de argumentos y gestión de la pila de navegación.

---

## Requerimientos Funcionales (RF)

1. **RF01 - Menú Principal:** Permitir la navegación desde el inicio hacia la lista de elementos y la pantalla de perfil mediante botones.
2. **RF02 - Listado Dinámico:** Desplegar una lista de elementos interactivos utilizando `LazyColumn`.
3. **RF03 - Paso de Parámetros:** Enviar el identificador del elemento seleccionado (`itemId`) a la pantalla de detalle para mostrar su información específica.
4. **RF04 - Limpieza de Historial:** Permitir regresar al inicio desde el perfil eliminando el historial previo con `popUpTo`.

---

## Capturas de Pantalla (Resultado Base)

| Pantalla Inicio | Lista de Elementos | Detalle de Elemento | Pantalla Perfil |
| :---: | :---: | :---: | :---: |
| <img width="720" height="1612" alt="image" src="https://github.com/user-attachments/assets/2c740627-4d36-4888-b3d9-9d20fe230d4c" /> | <img width="720" height="1612" alt="image" src="https://github.com/user-attachments/assets/878d7eee-192a-4f39-a5ca-42a3035fd08f" /> | <img width="720" height="1612" alt="image" src="https://github.com/user-attachments/assets/98e25274-f32f-4201-aa1d-f581e3c3c71d" /> | <img width="720" height="1612" alt="image" src="https://github.com/user-attachments/assets/08927439-cf7d-4da1-9a68-47ab08e4e091" /> |
