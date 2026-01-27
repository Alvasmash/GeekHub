package com.example.geekhub

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.geekhub.View.screens.CartScreen
import com.example.geekhub.View.screens.HomeScreen
import com.example.geekhub.View.screens.ProductDetailScreen
import com.example.geekhub.ui.theme.GeekHubTheme
import com.example.geekhub.view.screens.*
import com.example.geekhub.viewmodel.ProductViewModel

class MainActivity : ComponentActivity() {

    private val productViewModel: ProductViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createNotificationChannel()

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
    val context = LocalContext.current

    // Verificar si ya hay un usuario registrado
    val sharedPreferences = remember {
        context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    }

    val hasUserRegistered = sharedPreferences.contains("username")

    NavHost(
        navController = navController,
        startDestination = if (hasUserRegistered) "login" else "register"
    ) {
        composable("login") {
            LoginScreen(navController = navController)
        }

        composable("register") {
            RegisterScreen(navController = navController)
        }

        composable("home") {
            HomeScreen(navController = navController, viewModel = viewModel)
        }

        // RUTA CON PARÁMETRO: {productId} es un parámetro dinámico
        composable("producto/{productId}") { backStackEntry ->
            // Obtener el parámetro de la URL
            val productId = backStackEntry.arguments?.getString("productId") ?: ""

            ProductDetailScreen(
                navController = navController,
                viewModel = viewModel,
                productoId = productId  // Pasas el ID al screen
            )
        }

        composable("carrito") {
            CartScreen(navController = navController, viewModel = viewModel)
        }
    }
}
private fun ComponentActivity.createNotificationChannel() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            "cart_channel",
            "Carrito",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Notificaciones del carrito de compras"
        }

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
    }
}