# TECSUP Store App - Informe de Laboratorio (Fase 2)

Aplicación móvil desarrollada en Android con **Jetpack Compose** y **Material Design 3**, que implementa menús contextuales, menús laterales de navegación y gestión reactiva de estado mediante **State Hoisting**.

---

## I. Cumplimiento de Requisitos Funcionales

| Requisito de la Guía | Estado | Detalles de Implementación |
| :--- | :---: | :--- |
| **1. Ícono de 3 puntos (`MoreVert`)** | Cumplido | Ubicado en la parte superior derecha de cada tarjeta en `TarjetaProducto.kt`. |
| **2. DropdownMenu (Mínimo 3 opciones)** | Cumplido | Implementado con las opciones *"Favoritos"*, *"Compartir"* y *"Reportar"*. |
| **3. Ícono en cada opción (`leadingIcon`)** | Cumplido | Asignación de íconos representativos (`Favorite`/`FavoriteBorder`, `Share` y `Report`) en cada ítem. |
| **4. NavigationDrawer (Mínimo 4 destinos)** | Cumplido | Configuración de 5 destinos: *Inicio*, *Mis pedidos*, *Favoritos*, *Perfil* y *Cerrar sesión*. |
| **5. Encabezado con datos del usuario** | Cumplido | Muestra el avatar con las iniciales `"ND"`, el nombre *"Noemi De La Cruz"* y el correo institucional. |
| **6. Resaltado visual de destino activo** | Cumplido | Gestionado dinámicamente mediante la propiedad `selected` de `NavigationDrawerItem`. |

---

# Prompt

> "Mejora obligatoria: agrega un badge con contador en el ítem 'Favoritos' del drawer (mostrando cuántos productos marcó el usuario como favorito desde el DropdownMenu de cada producto). Esto conecta las dos piezas del laboratorio: una acción en el DropdownMenu debe reflejarse visualmente en el Drawer."
---

## IV. Capturas de Pantalla

| Menú Contextual (`TarjetaProducto`) | Badge Contador en Drawer (`AppDrawer`) |
| :---: | :---: |
| <img width="350" alt="image" src="https://github.com/user-attachments/assets/d77f6287-667e-4826-9f98-4988b7ba62da" />| <img width="350" alt="image" src="https://github.com/user-attachments/assets/5337e7c1-5ff0-4390-a4ea-2c9c51e3a6e7" />|

---

## V. Preguntas de Reflexión

* **¿Por qué el `DropdownMenu` se declara dentro de un `Box` junto al ícono que lo activa, y no en cualquier parte de la pantalla?**
  - Porque `DropdownMenu` utiliza el sistema de posicionamiento relativo de Jetpack Compose. Al declararlo dentro del mismo `Box` que el `IconButton` (el botón de tres puntos), Compose toma ese botón como punto de referencia para calcular las coordenadas exactas y desplegar el menú flotante justo al lado o debajo de dicho ícono.

* **¿Qué diferencia de alcance hay entre las opciones del `DropdownMenu` (afectan solo a un producto) y las del `NavigationDrawer` (afectan a toda la app)?**
  - Las opciones del `DropdownMenu` tienen un alcance **local o de entidad individual**, afectando exclusivamente al objeto `Producto` sobre el cual se hizo clic. En cambio, las opciones del `NavigationDrawer` tienen un alcance **global o de aplicación**, controlando el ruteo general entre las pantallas principales (`HomeScreen`, `Perfil`, etc.).

* **¿Cómo tuviste que estructurar tu código para que el contador de favoritos del drawer "se entere" de lo que pasa en el `DropdownMenu` de cada producto?**
  - Se aplicó el patrón **State Hoisting** (Elevación de Estado). El estado `favoritosIds` se definió en el ancestro común más alto (`AppNavegacion`). Desde ahí se pasa el tamaño de la lista al `AppDrawer` para mostrar el `Badge`, y se pasa un callback (`onToggleFavorito`) descendiendo por `HomeScreen` hasta `TarjetaProducto` para actualizar el estado central al interactuar con el menú contextual.

* **¿Qué tuviste que corregir del código que te generó la IA para la mejora del badge de favoritos?**
  - Tuve que corregir el código sugerido por la IA porque agregaba una función lógica adicional innecesaria que no se requería para el laboratorio. Se simplificó la implementación aprovechando directamente el parámetro nativo `badge = { Badge { ... } }` de `NavigationDrawerItem` y conectándolo de forma limpia al estado global `favoritosIds` existente.
---

## VI. Observaciones y Conclusiones

### Observaciones
1. **Flujo de callbacks:** Fue indispensable delegar limpiamente las funciones lambda desde `AppNavegacion` hasta `TarjetaProducto` para mantener la reactividad del estado de favoritos sin acoplar la lógica interna de los componentes.
2. **Uso de slots nativos en Material 3:** Utilizar los parámetros nativos (`icon`, `label`, `badge`) de `NavigationDrawerItem` simplificó la alineación del diseño en lugar de intentar maquetar composables manuales.

### Conclusiones
1. **Eficiencia del State Hoisting:** Mantener una única fuente de verdad en el componente raíz permite que dos elementos independientes (`DropdownMenu` y `AppDrawer`) se sincronicen de forma limpia y transparente.
2. **Evolución del proyecto Fase 1 con Fase 2:** Mientras que en la Fase 1 la interfaz era principalmente estática, en la Fase 2 la integración de estados globales e indicadores reactivos transformó la aplicación en un sistema interactivo y coherente.
