package com.example.geekhub.View.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.geekhub.model.Product

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductCard(
    producto: Product,
    onClick: () -> Unit,
    onAgregarCarrito: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = producto.nombre,
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                IconButton(
                    onClick = onAgregarCarrito,
                    enabled = producto.stock > 0,
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        Icons.Default.ShoppingCart,
                        contentDescription = "Agregar al carrito"
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = producto.descripcion,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "$${String.format("%.2f", producto.precio)}",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Text(
                        text = "Stock: ${producto.stock}",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (producto.stock > 0) MaterialTheme.colorScheme.onSurface
                        else MaterialTheme.colorScheme.error
                    )
                }

                if (producto.esArrendable) {
                    Badge(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    ) {
                        Text(
                            text = "ARRIENDO",
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }

            Text(
                text = "Categoría: ${producto.categoria.name}",
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}