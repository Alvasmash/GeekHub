package com.example.geekhub.model

import java.util.Date
import java.util.UUID

// MODELO: Producto
data class Product(
    val id: String = UUID.randomUUID().toString(),
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val categoria: ProductCategory,
    val stock: Int,
    val imagenUrl: String = "",
    val esArrendable: Boolean = false,
    val precioArriendoPorDia: Double? = null,
    val fechaCreacion: Date = Date(),
    val activo: Boolean = true
)

enum class ProductCategory {
    MANGA, FIGURA, POSTER, OTRO
}

// MODELO: Item del Carrito
data class CartItem(
    val productoId: String,
    val productoNombre: String,
    val cantidad: Int,
    val precio: Double,
    val esArriendo: Boolean = false,
    val diasArriendo: Int? = null
)

// MODELO: Orden
data class Order(
    val id: String = UUID.randomUUID().toString(),
    val usuarioId: String,
    val items: List<CartItem>,
    val total: Double,
    val estado: OrderStatus = OrderStatus.PENDIENTE,
    val fechaOrden: Date = Date(),
    val direccionEntrega: String? = null
)

enum class OrderStatus {
    PENDIENTE, CONFIRMADA, ENVIADA, ENTREGADA, CANCELADA
}

// MODELO: Usuario
data class User(
    val id: String,
    val email: String,
    val nombre: String,
    val rol: UserRole
)

enum class UserRole {
    ADMIN, CLIENTE
}