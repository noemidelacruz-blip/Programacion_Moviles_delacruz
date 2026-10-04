# TECSUP Store - Menú Lateral y Opciones de Productos

Aplicación Android desarrollada con Jetpack Compose y Material Design 3.

## Estudiante
- Nombre: Noemi De La Cruz
- Curso: Desarrollo de Aplicaciones Móviles

---

## Funcionalidades
1. TopAppBar Custom: Barra superior púrpura con título "TECSUP Store", subtítulo "Mas vendidos" y menú hamburguesa.
2. AppDrawer: Menú lateral con datos de la estudiante (Noemi) y opciones de navegación (Inicio, Mis pedidos, Favoritos, Perfil, Cerrar sesion).
3. TarjetaProducto: Tarjeta con icono de bolsa, precio en Soles (S/) y menú contextual de 3 puntos (Favoritos, Compartir, Reportar).
4. HomeScreen: Listado de productos desplegado dinámicamente mediante LazyColumn.

---
## Capturas de Pantalla

| Pantalla Principal y Catálogo de Productos (HomeScreen)  | Menú Contextual del Producto(DropdownMenu ) | Menú Lateral de Navegación (AppDrawer) |
| :---: | :---: | :---: | 
| <img width="350" alt="image" src="https://github.com/user-attachments/assets/c536c595-d3ce-4faa-a8f5-c711ebd1381f" />| <img width="350" alt="image" src="https://github.com/user-attachments/assets/f969c486-337b-415c-a1a8-9ee99cab0afe" />| <img width="400" alt="image" src="https://github.com/user-attachments/assets/fa91b962-5125-4e99-984c-e20b2a0ccbda" />|
## Estructura del Proyecto

```text
com.delacruz.tecsupstore/
├── components/
│   └── TarjetaProducto.kt
├── model/
│   └── Producto.kt
├── navigation/
│   ├── AppDrawer.kt
│   └── AppNavegacion.kt
├── screens/
│   └── HomeScreen.kt
└── MainActivity.kt
