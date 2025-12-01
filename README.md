# Proyecto Ketekura App

Aplicación móvil para Android que se conecta a un microservicio en Flask para la gestión y consulta de pagos de atenciones médicas.

## Integrantes

*   **Javiera Nuñez**

---

## Funcionalidad de la Aplicación

La aplicación cuenta con dos perfiles de usuario (roles) con funcionalidades distintas:

### 1. Rol de Paciente

*   **Login**: El paciente inicia sesión con su usuario y contraseña.
*   **Visualización de Pagos**: Una vez autenticado, puede ver un listado de todos sus pagos históricos y pendientes, incluyendo detalles como el monto, estado y fechas.
*   **Registrar Pago**: (Funcionalidad de backend preparada) El paciente puede registrar un pago para una atención pendiente.

### 2. Rol de Administrador

*   **Login**: El administrador inicia sesión con sus credenciales específicas.
*   **Búsqueda de Atenciones por RUT**: El administrador puede buscar todas las atenciones asociadas a un paciente introduciendo su RUT.
*   **Visualización de Resultados**: La aplicación muestra una lista detallada de las atenciones encontradas para el RUT buscado.
*   **Actualizar Estado de Pago**: (Funcialidad de backend preparada) El administrador tiene la capacidad de marcar una atención como pagada.

---

## Arquitectura y Endpoints

La aplicación móvil se comunica con un microservicio desarrollado en Flask y Python. Los endpoints utilizados son los siguientes:

*   `POST /login`: Autentica a los usuarios (pacientes y administradores).
*   `GET /buscar`: Busca públicamente atenciones por ID (`ate_id`) o `rut`.
*   `GET /mis-pagos`: [PACIENTE] Obtiene la lista de pagos del paciente autenticado.
*   `POST /pago`: [PACIENTE] Permite al paciente registrar un pago.
*   `GET /buscar-atenciones`: [ADMIN] Busca todas las atenciones de un paciente por `rut`.
*   `GET /admin/pagos`: [ADMIN] Obtiene una lista de todos los pagos.
*   `POST /admin/update-pago`: [ADMIN] Permite a un administrador actualizar el estado de un pago.

---

## Instrucciones de Ejecución

Para ejecutar el proyecto completo, es necesario levantar tanto el backend (microservicio) como la aplicación móvil.

### Backend (Microservicio Flask)

1.  **Clonar el repositorio** y navegar a la carpeta que contiene el código de Flask.
2.  **Configurar Oracle Wallet**: Asegúrate de que la ruta al Oracle Wallet y las credenciales en el archivo `app.py` son correctas.
3.  **Instalar dependencias**: Se recomienda usar un entorno virtual. Instala todas las librerías necesarias con pip:
    ```sh
    pip install Flask oracledb bcrypt pyjwt flask-cors
    ```
4.  **Ejecutar el servidor**:
    ```sh
    python app.py
    ```
5.  El servidor se ejecutará por defecto en `http://0.0.0.0:5000`. Anota la dirección IP de la máquina donde se está ejecutando (ej. `192.168.1.10`).

### App Móvil (Android)

1.  **Clonar el repositorio**.
2.  **Abrir el proyecto**: Abre la carpeta del proyecto con Android Studio.
3.  **Actualizar la IP del servidor**: 
    *   Navega al archivo `app/src/main/java/com/example/ketekura/network/RetrofitInstance.kt`.
    *   Modifica la `baseUrl` para que apunte a la dirección IP de tu servidor Flask que anotaste en el paso anterior.
    ```kotlin
    // Ejemplo:
    .baseUrl("http://192.168.1.10:5000/")
    ```
4.  **Sincronizar Gradle**: Espera a que Android Studio sincronice el proyecto y descargue todas las dependencias.
5.  **Ejecutar la App**: Ejecuta la aplicación en un emulador o en un dispositivo físico conectado a la misma red que el servidor.

---

## APK Firmado y Llave de Publicación (.jks)

### Generación del APK

Para generar un APK firmado listo para distribución, puedes usar el asistente de Android Studio:

1.  Ve a `Build > Generate Signed Bundle / APK...`.
2.  Selecciona `APK` y sigue los pasos del asistente.
3.  El APK generado se puede colocar en una carpeta `/apk` en la raíz del proyecto para fácil acceso.

### Archivo de Firma (.jks)

**ADVERTENCIA DE SEGURIDAD MUY IMPORTANTE:**

> El archivo `.jks` (Java KeyStore) es tu clave privada y secreta para firmar la aplicación. **NUNCA DEBES SUBIR ESTE ARCHIVO A GITHUB O A CUALQUIER REPOSITORIO PÚBLICO.**

*   **Almacenamiento**: Guarda tu archivo `.jks` en un lugar seguro y privado en tu máquina local, fuera de la carpeta del proyecto que se sincroniza con Git.
*   **Configuración**: Para que Gradle pueda encontrar la llave al firmar la app, debes configurar la ruta y las contraseñas en el archivo `gradle.properties` de tu proyecto, y asegurarte de que `gradle.properties` esté incluido en tu `.gitignore` para no subirlo accidentalmente.

---

## Código Fuente

Este repositorio contiene el código fuente completo para:

*   **La aplicación móvil Android**.
*   **El microservicio en Flask (Python)**.
