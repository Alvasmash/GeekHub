package com.example.geekhub.View.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.geekhub.View.components.CartItemComponent
import com.example.geekhub.viewmodel.ProductViewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    navController: NavController,
    viewModel: ProductViewModel
) {
    val carrito by viewModel.carrito.collectAsState()
    val total = viewModel.calcularTotalCarrito()
    val usuarioActual by viewModel.usuarioActual.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Carrito de Compras") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        bottomBar = {
            if (carrito.isNotEmpty()) {
                BottomAppBar {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Total:")
                            Text(
                                "$${String.format("%.2f", total)}",
                                style = MaterialTheme.typography.headlineSmall
                            )
                        }

                        Button(
                            onClick = {
                                viewModel.crearOrden()
                                navController.navigate("ordenes")
                            }
                        ) {
                            Text("Confirmar Compra")
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        if (carrito.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Tu carrito está vacío",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Agrega productos desde la tienda",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // Lista de items del carrito
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(carrito, key = { it.productoId + it.esArriendo }) { item ->
                        CartItemComponent(
                            item = item,
                            onAumentarCantidad = {
                                viewModel.actualizarCantidadCarrito(
                                    item.productoId,
                                    item.cantidad + 1,
                                    item.esArriendo
                                )
                            },
                            onDisminuirCantidad = {
                                viewModel.actualizarCantidadCarrito(
                                    item.productoId,
                                    item.cantidad - 1,
                                    item.esArriendo
                                )
                            },
                            onEliminar = {
                                viewModel.eliminarDelCarrito(item.productoId, item.esArriendo)
                            }
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

                // Resumen de compra
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            "Resumen de Compra",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Subtotal:")
                            Text("$${String.format("%.2f", total)}")
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Env��o:")
                            Text("Gratis")
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Divider()

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "Total:",
                                style = MaterialTheme.typography.titleLarge
                            )
                            Text(
                                "$${String.format("%.2f", total)}",
                                style = MaterialTheme.typography.titleLarge
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        if (usuarioActual.rol == com.example.geekhub.model.UserRole.ADMIN) {
                            Button(
                                onClick = { viewModel.limpiarCarrito() },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.errorContainer
                                )
                            ) {
                                Text("Vaciar Carrito")
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
    }
}