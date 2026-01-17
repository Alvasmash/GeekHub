package com.example.geekhub.repository

import com.example.geekhub.model.Product
import com.example.geekhub.model.ProductCategory
import java.util.Date
import java.util.UUID

class ProductRepository {

    private val productos = mutableListOf<Product>()

    init {
        cargarDatosIniciales()
    }

    private fun cargarDatosIniciales() {
        productos.addAll(
            listOf(
                Product(
                    id = UUID.randomUUID().toString(),
                    nombre = "One Piece Vol. 1",
                    descripcion = "Primer volumen del manga de One Piece",
                    precio = 12990.0,
                    categoria = ProductCategory.MANGA,
                    stock = 50,
                    imagenUrl = ""
                ),
                Product(
                    id = UUID.randomUUID().toString(),
                    nombre = "Figura Goku Super Saiyan",
                    descripcion = "Figura coleccionable de Goku Super Saiyan",
                    precio = 89990.0,
                    categoria = ProductCategory.FIGURA,
                    stock = 15,
                    esArrendable = true,
                    precioArriendoPorDia = 5990.0
                ),
                Product(
                    id = UUID.randomUUID().toString(),
                    nombre = "Poster Attack on Titan",
                    descripcion = "Poster tamaño A2 de Attack on Titan",
                    precio = 9990.0,
                    categoria = ProductCategory.POSTER,
                    stock = 100
                ),
                Product(
                    id = UUID.randomUUID().toString(),
                    nombre = "Naruto Shippuden Vol. 25",
                    descripcion = "Manga de Naruto Shippuden",
                    precio = 11990.0,
                    categoria = ProductCategory.MANGA,
                    stock = 30
                ),
                Product(
                    id = UUID.randomUUID().toString(),
                    nombre = "Figura Batman",
                    descripcion = "Figura coleccionable de Batman",
                    precio = 75990.0,
                    categoria = ProductCategory.FIGURA,
                    stock = 10,
                    esArrendable = true,
                    precioArriendoPorDia = 4990.0
                ),
                Product(
                    id = UUID.randomUUID().toString(),
                    nombre = "Poster Dragon Ball Z",
                    descripcion = "Poster coleccionable de Dragon Ball Z",
                    precio = 8990.0,
                    categoria = ProductCategory.POSTER,
                    stock = 80
                )
            )
        )
    }

    // OPERACIONES CRUD

    fun obtenerTodosProductos(): List<Product> {
        return productos.sortedByDescending { it.fechaCreacion }
    }

    fun obtenerProductoPorId(id: String): Product? {
        return productos.find { it.id == id }
    }

    fun agregarProducto(producto: Product) {
        productos.add(producto)
    }

    fun actualizarProducto(productoActualizado: Product): Boolean {
        val indice = productos.indexOfFirst { it.id == productoActualizado.id }
        return if (indice != -1) {
            productos[indice] = productoActualizado
            true
        } else {
            false
        }
    }

    fun eliminarProducto(id: String): Boolean {
        return productos.removeIf { it.id == id }
    }

    fun buscarProductos(consulta: String): List<Product> {
        if (consulta.isBlank()) return obtenerTodosProductos()

        return productos.filter {
            it.nombre.contains(consulta, ignoreCase = true) ||
                    it.descripcion.contains(consulta, ignoreCase = true) ||
                    it.categoria.name.contains(consulta, ignoreCase = true)
        }
    }

    fun obtenerProductosPorCategoria(categoria: ProductCategory): List<Product> {
        return productos.filter { it.categoria == categoria }
    }

    fun obtenerProductosArrendables(): List<Product> {
        return productos.filter { it.esArrendable }
    }

    fun actualizarStock(id: String, nuevoStock: Int): Boolean {
        val producto = productos.find { it.id == id }
        return if (producto != null) {
            val productoActualizado = producto.copy(stock = nuevoStock)
            actualizarProducto(productoActualizado)
        } else {
            false
        }
    }

    fun obtenerProductosBajoStock(limite: Int = 5): List<Product> {
        return productos.filter { it.stock < limite }
    }

    fun obtenerCategorias(): List<ProductCategory> {
        return ProductCategory.values().toList()
    }
}