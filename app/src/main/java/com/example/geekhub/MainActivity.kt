package com.example.geekhub

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
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
import com.example.geekhub.View.screens.HomeScreen
import com.example.geekhub.View.screens.CartScreen
import com.example.geekhub.View.screens.ProductDetailScreen
import com.example.geekhub.ui.theme.GeekHubTheme
import com.example.geekhub.viewmodel.ProductViewModel

class MainActivity : ComponentActivity() {

    private val productViewModel: ProductViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createNotificationChannel() // IMPORTANTE

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
        composable("home") {
            HomeScreen(navController = navController, viewModel = viewModel)
        }

        composable("producto") {
            val producto = viewModel.obtenerProductoPorId("1")
            if (producto != null) {
                ProductDetailScreen(
                    navController = navController,
                    viewModel = viewModel,
                    productoId = producto.id
                )
            }
        }

        composable("carrito") {
            CartScreen(navController = navController, viewModel = viewModel)
        }
    }
}

/*  Canal de notificaciones */
private fun ComponentActivity.createNotificationChannel() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            "cart_channel",
            "Carrito",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Notificaciones del carrito de compras"
        }

        val manager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
    }
}
