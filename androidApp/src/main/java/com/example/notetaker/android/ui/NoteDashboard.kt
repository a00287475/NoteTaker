package com.example.notetaker.android.ui

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.notetaker.android.model.Note

@Composable
fun NoteDashboard(navController: NavController) {
    val context = LocalContext.current
    val notes = remember { mutableStateOf(loadNotes(context)) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Your Notes",
            style = MaterialTheme.typography.headlineSmall,
            color = Color.White // Change font color to blue
        )


        Spacer(modifier = Modifier.height(16.dp))

        // Button to navigate to NoteTakingScreen to add a new note
        Button(
            onClick = { navController.navigate("create_note") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Create New Note")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Show a list of notes or a message if no notes are available
        if (notes.value.isEmpty()) {
            Text(text = "No notes yet", style = MaterialTheme.typography.bodyMedium)
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2), // 2 columns for a chessboard-like layout
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(8.dp), // Add padding around the grid
                verticalArrangement = Arrangement.spacedBy(8.dp), // Vertical spacing between items
                horizontalArrangement = Arrangement.spacedBy(8.dp) // Horizontal spacing between items
            )
            {
                items(notes.value) { note ->
                    NoteItem(note)
                }


            }
        }

        Spacer(modifier = Modifier.height(16.dp))


    }
}


