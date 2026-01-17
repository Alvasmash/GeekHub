package com.example.geekhub.View.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.geekhub.ui.components.ProductCard
import com.example.geekhub.viewmodel.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: ProductViewModel
) {
    val productos by viewModel.productos.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("GeekHub Store") },
                actions = {
                    IconButton(onClick = { navController.navigate("carrito") }) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = "Carrito")
                    }
                }
            )
        }
    ) { paddingValues ->
        if (productos.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("No hay productos disponibles")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(8.dp)
            ) {
                items(productos) { producto ->
                    ProductCard(
                        producto = producto,
                        onClick = {
                            // Navega a pantalla de detalle
                            navController.navigate("producto")
                        },
                        onAgregarCarrito = {
                            viewModel.agregarAlCarrito(producto)
                        }
                    )
                }
            }
        }
    }
}