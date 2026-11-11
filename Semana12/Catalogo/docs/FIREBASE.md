# Configuración de Firebase

El proyecto usa el proyecto de Firebase **beautyemi-f4049** con el paquete
`com.delacruz.catalogo`. La configuración ya está aplicada: Authentication con
correo y contraseña, Firestore con la colección `usuarios` y el campo `rol`, y
el archivo `google-services.json` en `app/`.

Este documento deja registrado cómo quedó configurado (sirve para el README y
para la sustentación). Los pasos se hicieron una sola vez en la consola y no se
versionan en el repositorio.

## 1. Crear el proyecto y registrar la app

1. Entrar a <https://console.firebase.google.com> y crear un proyecto
   (se puede desactivar Analytics).
2. Agregar una app Android con el **mismo** `applicationId` del proyecto:
   `com.delacruz.catalogo`.
3. Descargar `google-services.json` y colocarlo dentro de `app/`
   (vista Project de Android Studio).

> `google-services.json` contiene una clave de API, por eso está en `.gitignore`
> y **no se sube** al repositorio.

## 2. Activar el ingreso con correo y contraseña

1. Authentication → Comenzar.
2. Método de acceso → **Correo electrónico/contraseña** → Habilitar.
3. Authentication → Usuarios → están creados los dos usuarios de prueba:

| Correo | Uso |
|---|---|
| correo del administrador | rol admin (ve el panel Admin) |
| correo del usuario | rol usuario (solo catálogo y favoritos) |

La contraseña es la que se definió al crearlos. La app no tiene pantalla de
registro: los usuarios se crean aquí.

## 3. Crear la colección de roles en Firestore

1. Firestore Database → Crear base de datos (modo producción).
2. Crear la colección `usuarios`.
3. Un documento por usuario, **con el UID como ID del documento**
   (copiarlo de Authentication → Usuarios → columna UID):

```
usuarios/{uid}
  rol:    "admin"      (o "usuario")
  nombre: "Nombre de la persona"
```

Si el ID del documento no es el UID, el rol siempre saldrá como usuario.

## 4. Reglas de seguridad

Firestore → Reglas → pegar y publicar:

```
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /usuarios/{uid} {
      allow read: if request.auth != null && request.auth.uid == uid;
      allow write: if false;
    }
  }
}
```

Cada persona solo puede leer su propio documento y nadie puede escribir desde
la app. Es un laboratorio: el rol en la app solo decide qué pantallas se
muestran; lo que protege los datos son estas reglas.

## 5. Verificar

1. Sincronizar Gradle (el plugin `com.google.gms.google-services` lee el JSON).
2. Ejecutar la app e ingresar con el correo del administrador o del usuario.
3. Si sale `Default FirebaseApp is not initialized`, el JSON no está en
   `app/`, el plugin no está aplicado o falta un Clean + Rebuild.
