# TecnoAsistencia

Proyecto de la asignatura Programación de Aplicaciones de Escritorio de la Universidad Americana (UAM).

Es un sistema desarrollado con JavaFX y PostgreSQL para registrar clientes y administrar citas de mantenimiento y reparación de equipos informáticos.

## Integrantes y responsabilidades

- **Andy Steven Diaz Montenegro** — GitHub: [BettleJuice-cmd](https://github.com/BettleJuice-cmd). Creó la clase principal de la aplicación, el modelo `Cliente`, `ClienteDAO` y la conexión de Java con PostgreSQL mediante JDBC.
- **Carlos Fernando Torres Gonzalez** — GitHub: [carlostorres2205](https://github.com/carlostorres2205). Creó el modelo `CitaTecnica`, implementó `CitaTecnicaDAO` y diseñó la interfaz inicial de citas con su formulario, tabla, botones y filtros.
- **Alexa Sofia Loasiga Torrez** — GitHub: [asloasiga](https://github.com/asloasiga). Implementó el controlador de citas técnicas, la búsqueda de clientes desde el formulario y la conservación del cliente seleccionado.
- **Adolfo Carlos Ramirez Arauz** — GitHub: [MrNacatamal](https://github.com/MrNacatamal). Implementó el controlador y la interfaz de clientes, sus validaciones, la navegación entre módulos, las pruebas y la documentación.

## Tecnologías utilizadas

- Java 21 y JavaFX 21
- PostgreSQL y JDBC
- Maven
- IntelliJ IDEA
- Git y GitHub

## Funcionalidades

**Clientes:** registrar, listar, actualizar, eliminar y buscar por nombre, apellido o correo. Se validan los datos obligatorios, el formato del correo, los correos duplicados y la restricción para eliminar clientes con citas.

**Citas técnicas:** registrar, listar, actualizar y eliminar citas de un cliente existente. Permite buscar clientes por nombre, seleccionar su cita y filtrar citas por fecha y estado (Pendiente, En proceso, Finalizada o Cancelada).

## Base de datos

La base se llama `gestion_citas` y utiliza dos tablas: `cliente` y `cita_tecnica`. Un cliente puede tener varias citas, pero cada cita pertenece a un cliente. El archivo `gestion_citas.sql` contiene la estructura de las tablas, la clave foránea y las restricciones.

## Cómo ejecutar el proyecto

1. Instalar Java 21 y PostgreSQL.
2. Crear una base de datos llamada `gestion_citas` y ejecutar el archivo `gestion_citas.sql` desde pgAdmin.
3. Abrir la carpeta `TecnoAsistencia` como proyecto Maven en IntelliJ IDEA.
4. Comprobar la configuración local de host, puerto, usuario y base de datos en `conexionBD.java`.
5. Configurar la variable de entorno `DB_PASSWORD` con la contraseña local de PostgreSQL. No subir contraseñas al repositorio.
6. Esperar a que Maven descargue las dependencias y ejecutar `App.java` o `javafx:run`. Si se usa Maven, configurar también `DB_PASSWORD` en esa ejecución.

## Organización del código

- `models`: modelos `Cliente` y `CitaTecnica`.
- `dao`: operaciones SQL con `PreparedStatement` y `ResultSet`.
- `controller`: acciones de los formularios JavaFX.
- `util`: conexión con PostgreSQL.
- `fxml`: menú principal e interfaces de clientes y citas.
- `application`: punto de entrada de la aplicación.

## Pruebas realizadas

Se verificó el registro, actualización, eliminación y búsqueda de clientes; el registro, actualización y eliminación de citas; los filtros por estado y fecha; el rechazo de correos repetidos y campos vacíos; el bloqueo de eliminación de clientes con citas; y la permanencia de datos después de cerrar y abrir el sistema.

## Capturas de funcionamiento

### Menú principal

![Menú principal](evidencias/menu-principal.png)

### Gestión de clientes

![Gestión de clientes](evidencias/gestion-clientes.png)

### Validación de campos

![Validación de campos obligatorios](evidencias/validacion-campos.png)

### Gestión de citas técnicas

![Gestión de citas técnicas](evidencias/gestion-citas.png)
