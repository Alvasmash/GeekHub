package com.example.geekhub.utils

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.example.geekhub.R

object NotificationHelper {

    fun showProductAddedNotification(
        context: Context,
        productName: String
    ) {
        val notification = NotificationCompat.Builder(context, "cart_channel")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Producto agregado 🛒")
            .setContentText("Has agregado \"$productName\" al carrito")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        val manager =
            context.getSystemService(Context.NOTIFICATION_SERVICE)
                    as NotificationManager

        manager.notify(System.currentTimeMillis().toInt(), notification)
    }
}
