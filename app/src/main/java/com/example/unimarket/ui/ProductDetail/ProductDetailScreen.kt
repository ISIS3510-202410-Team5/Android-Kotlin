package com.example.unimarket.ui.ProductDetail

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.resolveDefaults
import androidx.compose.ui.unit.dp

@Composable
fun BookDetailScreen() {

    var title by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ){
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                value = title,
                onValueChange = { title = it},
                label = {
                    Text(text = "Title")
                }
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                value = author,
                onValueChange = { author = it},
                label = {
                    Text(text = "Author")
                }
            )
        }

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            onClick = {
                // TODO("ADD NEW BOOK")
            },
        ) {
            Text(
                text = "Add New Book",
                color = Color.White
            )
        }
    }
}