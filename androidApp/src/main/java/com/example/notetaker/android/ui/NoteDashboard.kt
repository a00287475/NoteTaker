package com.example.notetaker.android.ui

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.notetaker.android.model.Note
import java.io.File




@Composable
fun NoteDashboard(navController: NavController) {
    val context = LocalContext.current
    var notes by remember { mutableStateOf(loadNotes(context)) }
    var selectedNotes by remember { mutableStateOf<Set<Note>>(emptySet()) } // Track selected notes

    // Function to delete selected notes
    fun deleteSelectedNotes() {
        // Remove selected notes from the UI
        notes = notes.filterNot { it in selectedNotes }

        // Remove selected notes from the storage (notes.txt)
        val updatedNotes = notes.filterNot { it in selectedNotes }
        saveNotesLocally(context, updatedNotes)

        // Clear selection after deletion
        selectedNotes = emptySet()
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .clickable { // Deselect notes when clicking outside
                selectedNotes = emptySet()
            }
    ) {
        Text(
            text = "Your Notes",
            style = MaterialTheme.typography.headlineSmall,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Button to navigate to NoteTakingScreen to add a new note
        Button(
            onClick = { navController.navigate("create_note") }, // Creating a new note
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Create New Note")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Edit and Delete buttons for selected notes
        if (selectedNotes.isNotEmpty()) {
            // Edit button for selected notes
            Button(
                onClick = {
                    // Get the first selected note
                    val noteToEdit = selectedNotes.first()

                    // Navigate to the NoteTakingScreen with the selected note's ID
                    navController.navigate("create_note/${noteToEdit.id}") // Ensure the ID is passed correctly
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Edit Note")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Delete button for selected notes
            Button(
                onClick = { deleteSelectedNotes() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Delete Notes")
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // Show a list of notes or a message if no notes are available
        if (notes.isEmpty()) {
            Text(text = "No notes yet", style = MaterialTheme.typography.bodyMedium)
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(notes) { note ->
                    NoteItem(
                        note = note,
                        isSelected = selectedNotes.contains(note),
                        onSelect = { isSelected ->
                            selectedNotes = if (isSelected) {
                                selectedNotes + note // Add to selected notes
                            } else {
                                selectedNotes - note // Remove from selected notes
                            }
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

fun saveNotesLocally(context: Context, notes: List<Note>) {
    val file = File(context.filesDir, "notes.txt")
    file.writeText("") // Clear the existing content
    notes.forEach { note ->
        file.appendText("Title: ${note.title}\nContent: ${note.content}\n\n")
    }
}




@Composable
fun NoteItem(note: Note, isSelected: Boolean, onSelect: (Boolean) -> Unit) {
    // Corrected: Use colorScheme for accessing colors in the newer Compose versions
    val backgroundColor = if (isSelected) Color.LightGray else MaterialTheme.colorScheme.surface

    Card(
        modifier = Modifier
            .aspectRatio(1f)
            .padding(8.dp)
            .selectable(
                selected = isSelected,
                onClick = { onSelect(!isSelected) } // Toggle selection on click
            ),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = note.title,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            Text(
                text = note.content,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 3,
                overflow = TextOverflow.Clip,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
