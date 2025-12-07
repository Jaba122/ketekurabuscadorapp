# Proyecto Ketekura App

Aplicación móvil para Android que se conecta a un microservicio en Flask para la gestión y consulta de pagos de atenciones médicas. Esta versión incluye mejoras de persistencia local y nuevas herramientas de hardware.

## Integrantes

  * **Javiera Nuñez**

-----

## Funcionalidad de la Aplicación

La aplicación cuenta con dos perfiles de usuario (roles) con funcionalidades distintas, además de características generales nuevas:

### 1\. Rol de Paciente

  * **Login**: El paciente inicia sesión con su usuario y contraseña.
  * **Visualización de Pagos**: Una vez autenticado, puede ver un listado de todos sus pagos históricos y pendientes, incluyendo detalles como el monto, estado y fechas.
  * **Registrar Pago**: El paciente puede registrar un pago para una atención pendiente.
  * **Clínicas Cercanas (NUEVO)**: Utilizando servicios de geolocalización, el paciente puede visualizar una lista de hospitales y clínicas cercanas a su ubicación actual (ej. Hospital Carlos Van Buren, Clínica Reñaca), ordenadas por distancia.

### 2\. Rol de Administrador

  * **Login**: El administrador inicia sesión con sus credenciales específicas.
  * **Búsqueda de Atenciones por RUT**: El administrador puede buscar todas las atenciones asociadas a un paciente introduciendo su RUT.
  * **Visualización de Resultados**: La aplicación muestra una lista detallada de las atenciones encontradas.
  * **Actualizar Estado de Pago**: El administrador tiene la capacidad de marcar una atención como pagada.
  * **Escáner QR (NUEVO)**: Herramienta integrada que utiliza la cámara del dispositivo y ML Kit para escanear códigos QR de forma rápida dentro de la gestión administrativa.

-----

## Arquitectura y Tecnologías

La aplicación móvil sigue una arquitectura MVVM y se comunica con un microservicio en Flask. En esta rama "fix" se han integrado las siguientes tecnologías:

  * **Backend**: Python con Flask y Oracle Database.
  * **Base de Datos Local (Android)**: Implementación de **Room Database** para la persistencia local de datos de usuarios (`User`) y pagos (`Pago`), permitiendo un acceso más fluido a la información previamente cargada.
  * **Servicios de Ubicación**: Integración de `Play Services Location` para obtener la ubicación del usuario en tiempo real.
  * **Cámara y Machine Learning**: Uso de **CameraX** y **ML Kit Barcode Scanning** para la funcionalidad de lectura de códigos QR.
  * **Red**: Retrofit para el consumo de la API REST.

-----

## Endpoints de la API

La aplicación consume los siguientes endpoints del microservicio:

  * `POST /login`: Autentica a los usuarios.
  * `GET /buscar`: Busca atenciones por ID (`ate_id`) o `rut`.
  * `GET /mis-pagos`: [PACIENTE] Obtiene la lista de pagos del usuario.
  * `POST /pago`: [PACIENTE] Registra un pago.
  * `GET /buscar-atenciones`: [ADMIN] Busca atenciones de un paciente por `rut`.
  * `GET /admin/pagos`: [ADMIN] Obtiene una lista de todos los pagos.
  * `POST /admin/update-pago`: [ADMIN] Actualiza el estado de un pago.

-----

## Instrucciones de Ejecución

Para ejecutar el proyecto completo, es necesario levantar tanto el backend como la aplicación móvil.

### Backend (Microservicio Flask)

1.  **Clonar el repositorio** y navegar a la carpeta del código Flask.
2.  **Configurar Oracle Wallet**: Verificar credenciales en `app.py`.
3.  **Instalar dependencias**:
    ```sh
    pip install Flask oracledb bcrypt pyjwt flask-cors
    ```
4.  **Ejecutar el servidor**:
    ```sh
    python app.py
    ```
5.  Anota la dirección IP de la máquina (ej. `192.168.1.10`).

### App Móvil (Android)

1.  **Abrir el proyecto** en Android Studio.
2.  **Configurar IP**: Modifica `RetrofitInstance.kt` con la IP de tu servidor Flask.
3.  **Permisos del Dispositivo**:
      * Al usar las nuevas funcionalidades, la app solicitará permisos de **Cámara** (para el escáner QR) y de **Ubicación Precisa** (para buscar clínicas). Es necesario otorgarlos para que estas funciones operen correctamente.
4.  **Sincronizar Gradle**: Asegúrate de descargar las nuevas dependencias (Room, CameraX, ML Kit, Location).
5.  **Ejecutar la App**: Instalar en un emulador o dispositivo físico.

-----

## APK Firmado y Llave de Publicación

Para generar el APK:

1.  Ve a `Build > Generate Signed Bundle / APK...`.
2.  Utiliza tu archivo `.jks` (Java KeyStore).

> **IMPORTANTE**: Mantén tu archivo `.jks` y las credenciales de `gradle.properties` fuera del repositorio público por seguridad.
