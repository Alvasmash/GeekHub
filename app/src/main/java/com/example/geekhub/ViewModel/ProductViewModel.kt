package com.example.geekhub.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.geekhub.Model.IpLocation
import com.example.geekhub.Repository.LocationRepositorio
import com.example.geekhub.model.*
import com.example.geekhub.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class ProductViewModel : ViewModel() {

    private val Repository = LocationRepositorio()

    var uiState by mutableStateOf<LocationUiState>(LocationUiState.Loading)
        private set

    init {
        getLocation()
    }

    fun getLocation() {
        uiState = LocationUiState.Loading
        viewModelScope.launch {
            val result = Repository.fetchLocation()
            uiState = result.fold(
                onSuccess = { LocationUiState.Success(it) },
                onFailure = { LocationUiState.Error(it.message ?: "Error desconocido") }
            )
        }
    }

    sealed class LocationUiState {
        object Loading : LocationUiState()
        data class Success(val data: IpLocation) : LocationUiState()
        data class Error(val message: String) : LocationUiState()
    }



    // REPOSITORY (Acceso a datos)
    private val repository = ProductRepository()

    // ESTADOS OBSERVABLES para la UI (VIEW)

    private val _productos = MutableStateFlow<List<Product>>(emptyList())
    val productos: StateFlow<List<Product>> = _productos.asStateFlow()

    private val _carrito = MutableStateFlow<List<CartItem>>(emptyList())
    val carrito: StateFlow<List<CartItem>> = _carrito.asStateFlow()

    private val _ordenes = MutableStateFlow<List<Order>>(emptyList())
    val ordenes: StateFlow<List<Order>> = _ordenes.asStateFlow()

    private val _usuarioActual = MutableStateFlow(
        User(
            id = "1",
            email = "admin@geekhub.com",
            nombre = "Administrador",
            rol = UserRole.ADMIN
        )
    )
    val usuarioActual: StateFlow<User> = _usuarioActual.asStateFlow()

    private val _textoBusqueda = MutableStateFlow("")
    val textoBusqueda: StateFlow<String> = _textoBusqueda.asStateFlow()

    private val _categoriaSeleccionada = MutableStateFlow<ProductCategory?>(null)
    val categoriaSeleccionada: StateFlow<ProductCategory?> = _categoriaSeleccionada.asStateFlow()

    private val _productoSeleccionado = MutableStateFlow<Product?>(null)
    val productoSeleccionado: StateFlow<Product?> = _productoSeleccionado.asStateFlow()

    private val _mensajeError = MutableStateFlow<String?>(null)
    val mensajeError: StateFlow<String?> = _mensajeError.asStateFlow()

    init {
        cargarProductos()
    }

    // ========== OPERACIONES DE PRODUCTOS ==========

    private fun cargarProductos() {
        viewModelScope.launch {
            _productos.value = repository.obtenerTodosProductos()
        }
    }

    fun obtenerProductoPorId(id: String): Product? {
        return repository.obtenerProductoPorId(id)
    }

    fun agregarProducto(producto: Product): Boolean {
        return try {
            repository.agregarProducto(producto)
            cargarProductos()
            true
        } catch (e: Exception) {
            _mensajeError.value = "Error al agregar producto: ${e.message}"
            false
        }
    }

    fun actualizarProducto(producto: Product): Boolean {
        return try {
            val resultado = repository.actualizarProducto(producto)
            if (resultado) {
                cargarProductos()
            }
            resultado
        } catch (e: Exception) {
            _mensajeError.value = "Error al actualizar producto: ${e.message}"
            false
        }
    }

    fun eliminarProducto(id: String): Boolean {
        return try {
            val resultado = repository.eliminarProducto(id)
            if (resultado) {
                cargarProductos()
            }
            resultado
        } catch (e: Exception) {
            _mensajeError.value = "Error al eliminar producto: ${e.message}"
            false
        }
    }

    fun actualizarStock(id: String, nuevoStock: Int): Boolean {
        return try {
            val resultado = repository.actualizarStock(id, nuevoStock)
            if (resultado) {
                cargarProductos()
            }
            resultado
        } catch (e: Exception) {
            _mensajeError.value = "Error al actualizar stock: ${e.message}"
            false
        }
    }

    // ========== OPERACIONES DE BÚSQUEDA Y FILTRO ==========

    fun buscarProductos(consulta: String) {
        viewModelScope.launch {
            _textoBusqueda.value = consulta
            if (consulta.isBlank() && _categoriaSeleccionada.value == null) {
                _productos.value = repository.obtenerTodosProductos()
            } else {
                val productosFiltrados = repository.buscarProductos(consulta)
                _productos.value = productosFiltrados
            }
        }
    }

    fun filtrarPorCategoria(categoria: ProductCategory?) {
        viewModelScope.launch {
            _categoriaSeleccionada.value = categoria
            _productos.value = if (categoria == null) {
                repository.obtenerTodosProductos()
            } else {
                repository.obtenerProductosPorCategoria(categoria)
            }
        }
    }

    fun obtenerCategorias(): List<ProductCategory> {
        return repository.obtenerCategorias()
    }

    // ========== OPERACIONES DEL CARRITO ==========

    fun agregarAlCarrito(producto: Product, cantidad: Int = 1, esArriendo: Boolean = false, diasArriendo: Int? = null) {
        viewModelScope.launch {
            val itemExistente = _carrito.value.find { it.productoId == producto.id && it.esArriendo == esArriendo }

            val carritoActualizado = if (itemExistente != null) {
                _carrito.value.map {
                    if (it.productoId == producto.id && it.esArriendo == esArriendo) {
                        it.copy(cantidad = it.cantidad + cantidad)
                    } else {
                        it
                    }
                }
            } else {
                _carrito.value + CartItem(
                    productoId = producto.id,
                    productoNombre = producto.nombre,
                    cantidad = cantidad,
                    precio = if (esArriendo && producto.esArrendable)
                        (producto.precioArriendoPorDia ?: 0.0) * (diasArriendo ?: 1)
                    else producto.precio,
                    esArriendo = esArriendo,
                    diasArriendo = diasArriendo
                )
            }

            _carrito.value = carritoActualizado
        }
    }

    fun eliminarDelCarrito(productoId: String, esArriendo: Boolean = false) {
        viewModelScope.launch {
            _carrito.value = _carrito.value.filter {
                !(it.productoId == productoId && it.esArriendo == esArriendo)
            }
        }
    }

    fun actualizarCantidadCarrito(productoId: String, nuevaCantidad: Int, esArriendo: Boolean = false) {
        viewModelScope.launch {
            if (nuevaCantidad <= 0) {
                eliminarDelCarrito(productoId, esArriendo)
            } else {
                _carrito.value = _carrito.value.map {
                    if (it.productoId == productoId && it.esArriendo == esArriendo) {
                        it.copy(cantidad = nuevaCantidad)
                    } else {
                        it
                    }
                }
            }
        }
    }

    fun limpiarCarrito() {
        viewModelScope.launch {
            _carrito.value = emptyList()
        }
    }

    fun calcularTotalCarrito(): Double {
        return _carrito.value.sumOf { it.precio * it.cantidad }
    }

    fun obtenerCantidadEnCarrito(): Int {
        return _carrito.value.sumOf { it.cantidad }
    }

    // ========== OPERACIONES DE ÓRDENES ==========

    fun crearOrden(direccionEntrega: String? = null): Order? {
        return if (_carrito.value.isNotEmpty()) {
            val fechaActual = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())

            val orden = Order(
                usuarioId = _usuarioActual.value.id,
                items = _carrito.value,
                total = calcularTotalCarrito(),
                direccionEntrega = direccionEntrega
            )

            viewModelScope.launch {
                // Actualizar stock de productos
                _carrito.value.forEach { item ->
                    val producto = repository.obtenerProductoPorId(item.productoId)
                    producto?.let {
                        val nuevoStock = it.stock - item.cantidad
                        if (nuevoStock >= 0) {
                            repository.actualizarStock(item.productoId, nuevoStock)
                        }
                    }
                }

                _ordenes.value = _ordenes.value + orden
                limpiarCarrito()
                cargarProductos() // Recargar productos para actualizar stock
            }

            orden
        } else {
            _mensajeError.value = "El carrito está vacío"
            null
        }
    }

    fun obtenerOrdenPorId(id: String): Order? {
        return _ordenes.value.find { it.id == id }
    }

    fun actualizarEstadoOrden(id: String, nuevoEstado: OrderStatus): Boolean {
        val orden = _ordenes.value.find { it.id == id }
        return if (orden != null) {
            val ordenActualizada = orden.copy(estado = nuevoEstado)
            viewModelScope.launch {
                _ordenes.value = _ordenes.value.map {
                    if (it.id == id) ordenActualizada else it
                }
            }
            true
        } else {
            false
        }
    }

    // ========== UTILIDADES ==========

    fun setProductoSeleccionado(producto: Product?) {
        _productoSeleccionado.value = producto
    }

    fun limpiarMensajeError() {
        _mensajeError.value = null
    }

    fun cambiarUsuario(usuario: User) {
        _usuarioActual.value = usuario
    }

    fun obtenerProductosArrendables(): List<Product> {
        return repository.obtenerProductosArrendables()
    }

    fun obtenerProductosBajoStock(): List<Product> {
        return repository.obtenerProductosBajoStock()
    }
}