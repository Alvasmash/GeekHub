package com.example.geekhub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.geekhub.ui.screens.CartScreen
import com.example.geekhub.ui.screens.HomeScreen
import com.example.geekhub.ui.screens.ProductDetailScreen
import com.example.geekhub.ui.theme.GeekHubTheme
import com.example.geekhub.viewmodel.ProductViewModel

class MainActivity : ComponentActivity() {

    private val productViewModel: ProductViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GeekHubTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    GeekHubApp(viewModel = productViewModel)
                }
            }
        }
    }
}

@Composable
fun GeekHubApp(viewModel: ProductViewModel) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        // Pantalla principal
        composable("home") {
            HomeScreen(navController = navController, viewModel = viewModel)
        }

        // Detalle de producto (versión simplificada - sin parámetros por ahora)
        composable("producto") {
            // Para simplificar, mostramos el primer producto
            val producto = viewModel.obtenerProductoPorId("1")
            if (producto != null) {
                ProductDetailScreen(
                    navController = navController,
                    viewModel = viewModel,
                    productoId = producto.id
                )
            }
        }

        // Carrito de compras
        composable("carrito") {
            CartScreen(navController = navController, viewModel = viewModel)
        }
    }
}