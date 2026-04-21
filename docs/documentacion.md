# 🍽️ Sistema de Gestión de Restaurante - API REST

## 📌 Descripción General

Este sistema permite gestionar las operaciones básicas de un restaurante, incluyendo productos, categorías, pedidos y usuarios.
Está diseñado bajo arquitectura REST y estructurado en capas para facilitar su escalabilidad y mantenimiento.

---

## 🧱 Entidades del Sistema

### 🛒 Producto

Representa un ítem disponible para venta.

| Campo            | Tipo      | Descripción            |
| ---------------- | --------- | ---------------------- |
| id               | Long      | Identificador único    |
| nombre           | String    | Nombre del producto    |
| precio           | double    | Precio del producto    |
| stock            | Integer   | Cantidad disponible    |
| fechaVencimiento | LocalDate | Fecha de vencimiento   |
| activo           | Boolean   | Estado del producto    |
| categoriaId      | Long      | Relación con categoría |

---

### 🗂️ Categoría

Agrupa productos por tipo.

| Campo  | Tipo    | Descripción            |
| ------ | ------- | ---------------------- |
| id     | Long    | Identificador único    |
| nombre | String  | Nombre de la categoría |
| activo | Boolean | Estado de la categoría |

---

### 🧾 Pedido

Representa una orden realizada por un cliente.

| Campo     | Tipo      | Descripción                     |
| --------- | --------- | ------------------------------- |
| id        | Long      | Identificador único             |
| fecha     | LocalDate | Fecha del pedido                |
| total     | double    | Total del pedido                |
| estado    | String    | Estado (PENDIENTE, PAGADO, etc) |
| usuarioId | Long      | Cliente asociado                |

---

### 📦 DetallePedido

Relaciona productos con pedidos.

| Campo      | Tipo    | Descripción         |
| ---------- | ------- | ------------------- |
| id         | Long    | Identificador único |
| pedidoId   | Long    | Relación con pedido |
| productoId | Long    | Producto asociado   |
| cantidad   | Integer | Cantidad solicitada |
| subtotal   | double  | Precio parcial      |

---

### 👤 Usuario

Representa un usuario del sistema.

| Campo    | Tipo    | Descripción           |
| -------- | ------- | --------------------- |
| id       | Long    | Identificador único   |
| nombre   | String  | Nombre del usuario    |
| email    | String  | Correo electrónico    |
| password | String  | Contraseña encriptada |
| rol      | String  | ADMIN / CLIENTE       |
| activo   | Boolean | Estado del usuario    |

---

## 🔄 Procesos del Sistema

### 🛒 Gestión de Productos

* Crear producto
* Listar productos
* Actualizar producto
* Eliminar o desactivar producto
* Control de stock

---

### 🗂️ Gestión de Categorías

* Crear categoría
* Listar categorías
* Asignar productos a categorías
* Activar / desactivar categorías

---

### 🧾 Gestión de Pedidos

* Crear pedido
* Agregar productos al pedido
* Calcular total automáticamente
* Cambiar estado del pedido
* Consultar pedidos por usuario

---

### 📦 Gestión de Detalles de Pedido

* Asociar productos a pedidos
* Calcular subtotales
* Validar stock disponible

---

### 👤 Gestión de Usuarios

* Registro de usuarios
* Autenticación (login)
* Asignación de roles
* Activación/desactivación

---

## 🧩 Arquitectura del Sistema

El sistema sigue una arquitectura en capas:

```id="arc001"
Controller → Service → Repository → Database
```

### 📂 Capas

* **Controller:** Maneja peticiones HTTP
* **Service:** Lógica de negocio
* **Repository:** Acceso a datos
* **DTOs:** Entrada y salida de datos
* **Entities:** Representación de la base de datos

---

## 🔐 Reglas de Negocio

* Un producto debe pertenecer a una categoría
* No se pueden vender productos sin stock
* El total del pedido se calcula automáticamente
* Los usuarios deben tener roles definidos
* Los productos y categorías pueden desactivarse sin eliminarse

---

## ⚠️ Validaciones

* Campos obligatorios (nombre, precio, etc.)
* Precio y stock no pueden ser negativos
* Email debe tener formato válido
* Fecha de vencimiento no puede ser anterior a la actual

---

## 🔐 Seguridad (Futuro)

* Autenticación con JWT
* Protección de endpoints por rol
* Encriptación de contraseñas

---

## 🚀 Mejoras Futuras

* 📊 Reportes de ventas
* 📦 Control avanzado de inventario
* 🔔 Notificaciones
* 🌙 Modo oscuro (frontend)
* 📈 Dashboard administrativo
* 🧪 Testing automatizado

---

## 🧪 Flujo General del Sistema

1. Usuario se registra o inicia sesión
2. Admin gestiona productos y categorías
3. Cliente realiza pedidos
4. Sistema valida stock y calcula totales
5. Pedido cambia de estado según proceso

---

## 🧠 Notas Técnicas

* Uso de DTOs para evitar acoplamiento
* Separación clara de capas
* Uso de buenas prácticas REST
* Manejo de estados (activo/inactivo)
* Preparado para escalabilidad

---

## 📌 Tecnologías Utilizadas

* Java
* Spring Boot
* JPA / Hibernate
* MySQL
* Lombok (opcional)
* Swagger / OpenAPI

---

## 📊 Estado del Proyecto

✔ Estructura base implementada
✔ CRUD de productos funcional
🔧 En desarrollo continuo

---
