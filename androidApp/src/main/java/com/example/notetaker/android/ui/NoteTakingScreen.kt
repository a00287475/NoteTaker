package com.example.notetaker.android.ui

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.notetaker.android.model.Note
import java.io.File

@Composable
fun NoteTakingScreen() {
    var title by remember { mutableStateOf("") } // State for title
    var noteText by remember { mutableStateOf("") } // State for note content

    val context = LocalContext.current // Get the context for file operations

    // Load saved notes when the screen is first displayed
    var notes by remember { mutableStateOf(loadNotes(context)) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Take Notes", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(8.dp))

        // Title Input Field
        TextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Note Content Input
        TextField(
            value = noteText,
            onValueChange = { noteText = it },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Save Button
        Button(
            onClick = {
                if (title.isNotBlank() && noteText.isNotBlank()) { // Ensure both fields are filled
                    val note = Note(title, noteText) // Create a Note object
                    saveNoteLocally(context, note) // Save note locally
                    notes = loadNotes(context) // Reload notes to update the UI
                    title = "" // Clear title
                    noteText = "" // Clear content
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Display saved notes
        LazyColumn {
            items(notes) { note ->
                NoteItem(note)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun NoteItem(note: Note) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(text = note.title, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = note.content, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

// Function to save note locally
fun saveNoteLocally(context: Context, note: Note) {
    val file = File(context.filesDir, "notes.txt")
    file.appendText("Title: ${note.title}\nContent: ${note.content}\n\n")
}

// Function to load saved notes
fun loadNotes(context: Context): List<Note> {
    val file = File(context.filesDir, "notes.txt")
    if (!file.exists()) return emptyList()

    return file.readText().trim().split("\n\n").mapNotNull { entry ->
        val lines = entry.split("\n")
        if (lines.size >= 2) {
            val title = lines[0].removePrefix("Title: ")
            val content = lines[1].removePrefix("Content: ")
            Note(title, content)
        } else null
    }
}
