# E-Commerce Microservices Backend (Spring Boot)

## Descripción general

Este proyecto tiene como objetivo construir una **plataforma e-commerce** modular basada en **arquitectura de microservicios**, con **autenticación Oauth2** gestionada por **Keycloak**.
Actualmente se encuentra en **desarrollo**, con un enfoque en la infraestructura backend y la comunicación entre servicios.

## Arquitectura actual

El sistema está compuesto por los siguientes componentes:

* **Config Server** → Centraliza la configuración de los microservicios.
* **Eureka Server** → Registro y descubrimiento de servicios.
* **API Gateway** → Puerta de entrada al sistema, con balanceador de carga y autenticación por **Keycloak**.
* **Keycloak** → Identity Provider (IdP) para autenticación Oauth2 y gestión de usuarios.
* **PostgreSQL** → Base de datos principal para persistencia.
* **Kafka** → Cola de mensajes usada para comunicación asincrónica entre microservicios.
* **Auth Service** → Servicio temporal para login y registro de usuarios.
* **User Service** → Servicio para consultar información de usuarios.

## Flujo de autenticación

El **gateway** valida las peticiones autenticadas usando **Keycloak**, quien actúa como proveedor Oauth2.
Aunque existe un **Auth Service** que permite realizar **login y registro** para facilitar pruebas, **este será eliminado en el futuro**, ya que el flujo real de autenticación y registro se realizará **directamente desde Keycloak**.

## Sincronización de usuarios

Keycloak es la **fuente de verdad** de los usuarios.
Mediante **event listeners** en Keycloak, cada vez que un usuario es **creado, actualizado o eliminado**, dicho evento se envía a través de **Kafka** para que el **User Service** replique la información en la base de datos **PostgreSQL**.
Esto permite mantener una **copia sincronizada** de los usuarios del lado de la aplicación, facilitando operaciones internas sin comprometer la seguridad ni el modelo central de identidad.

## Variables de entorno (.env)

El archivo `.env` contiene las credenciales y configuraciones necesarias para ejecutar los servicios del proyecto.
Antes de iniciar el entorno, asegúrate de crear este archivo en la raíz del proyecto con las siguientes variables:

```env
DB_NAME=
DB_USER=
DB_PASSWORD=

KEYCLOAK_ADMIN=
KEYCLOAK_PASSWORD=
KEYCLOAK_REALM=
KEYCLOAK_CLIENT_ID=
KEYCLOAK_CLIENT_SECRET=
```

* **DB_NAME, DB_USER, DB_PASSWORD** → Configuración de la base de datos PostgreSQL.
* **KEYCLOAK_ADMIN, KEYCLOAK_PASSWORD** → Credenciales del administrador de Keycloak para interactuar con la API de administración.
* **KEYCLOAK_REALM, KEYCLOAK_CLIENT_ID, KEYCLOAK_CLIENT_SECRET** → Configuración del cliente y realm usados para la autenticación Oauth2.

Estas variables son esenciales para conectar correctamente los servicios y permitir la autenticación mediante Keycloak.

## 🚀 Ejecución del proyecto

Para ejecutar el proyecto localmente, sigue el siguiente orden:

1. **Levantar los servicios base con Docker Compose**
   Ejecuta el siguiente comando desde la raíz del proyecto:

   ```bash
   docker-compose up -d
   ```

   Esto iniciará los contenedores de:

   * **PostgreSQL**
   * **Keycloak**
   * **Zookeeper**
   * **Kafka**

2. **Iniciar los microservicios base**
   Luego de que los contenedores estén activos, ejecuta los servicios en este orden:

   1. `config-server`
   2. `eureka-server`
   3. `api-gateway`

3. **Ejecutar los microservicios adicionales**
   Una vez estén corriendo los servicios base, puedes iniciar cualquier otro microservicio, como `auth-service` o `user-service`, de forma individual o simultánea según lo que desees probar.

> **Nota:**
> Los servicios mínimos necesarios para que la arquitectura funcione correctamente son:
>
> * `config-server`
> * `eureka-server`
> * `api-gateway`
> * Docker Compose con **Postgres**, **Keycloak**, **Zookeeper** y **Kafka**.

## Rutas del sistema

Todas las rutas deben ser accedidas **a través del API Gateway**, el cual gestiona la autenticación y el enrutamiento hacia los microservicios correspondientes.

---

### Auth Service (rutas públicas)

Estas rutas **no requieren token de acceso**, ya que están pensadas para facilitar las pruebas de login y registro.
En el futuro, serán reemplazadas por el flujo directo de **Keycloak**.

#### **POST /api/auth/login**

Realiza la autenticación contra Keycloak y devuelve un **token de acceso** válido para usar en las rutas privadas.

**Body de ejemplo:**

```json
{
  "username": "user",
  "password": "1234"
}
```

**Respuesta:**
Token JWT que puede utilizarse en las peticiones protegidas (header `Authorization: Bearer <token>`).

---

#### **POST /api/auth/register**

Registra un nuevo usuario en Keycloak.

**Body de ejemplo:**

```json
{
  "username": "user",
  "email": "user@example.com",
  "enabled": true,
  "firstName": "edinson",
  "lastName": "noriega",
  "password": "1234"
}
```

**Respuesta:**
Confirmación de creación del usuario en Keycloak.

---

### User Service (rutas privadas)

Estas rutas **requieren un token de acceso válido** generado en el proceso de login.

#### **GET /api/users**

Devuelve la lista completa de usuarios registrados (sin incluir credenciales).

**Header requerido:**

```
Authorization: Bearer <token>
```

---

> **Importante:**
> Si intentas acceder a las rutas privadas sin un token o con un token inválido, el gateway responderá con un error **401 Unauthorized**.

## Estado del proyecto

En desarrollo.
Actualmente se encuentran implementados los servicios base (config, eureka, gateway, keycloak, kafka, auth y user). Próximamente se desarrollarán los servicios principales de productos, pedidos y carrito de compras.
