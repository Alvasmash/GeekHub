package com.example.geekhub.View.screens

import ads_mobile_sdk.h6
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
import com.example.geekhub.View.components.ProductCard
import com.example.geekhub.viewmodel.ProductViewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color


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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            val state = viewModel.uiState

            when (state) {
                is ProductViewModel.LocationUiState.Loading -> CircularProgressIndicator()
                is ProductViewModel.LocationUiState.Success -> {
                    Text(text = "Tu ubicación actual:")
                    Text(text = "${state.data.city}, ${state.data.country}")
                    Text(text = "IP: ${state.data.query}")
                    Button(onClick = { viewModel.getLocation() }, Modifier.padding(top = 16.dp)) {
                        Text("Actualizar")
                    }
                }

                is ProductViewModel.LocationUiState.Error -> {
                    Text(text = "Error: ${state.message}", color = Color.Red)
                    Button(onClick = { viewModel.getLocation() }) {
                        Text("Reintentar")
                    }
                }
            }

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
}