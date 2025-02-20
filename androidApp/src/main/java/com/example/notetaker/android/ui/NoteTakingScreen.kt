package com.example.notetaker.android.ui

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.graphics.Color
import com.example.notetaker.android.model.Note
import java.io.File

@Composable
fun NoteTakingScreen(navController: NavController, noteId: String? = null) {
    var title by remember { mutableStateOf("") }
    var noteText by remember { mutableStateOf("") }

    val context = LocalContext.current

    // If noteId is provided, load the note for editing
    val noteToEdit = noteId?.let { id ->
        loadNotes(context).find { it.id == id }
    }

    // If noteToEdit exists, populate fields with the existing note's data
    noteToEdit?.let {
        title = it.title
        noteText = it.content
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = if (noteId == null) "Take Notes" else "Edit Note",
            style = MaterialTheme.typography.headlineSmall,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = noteText,
            onValueChange = { noteText = it },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (title.isNotBlank() && noteText.isNotBlank()) {
                    val note = if (noteId == null) {
                        Note(title = title, content = noteText) // Create new note
                    } else {
                        Note(id = noteId, title = title, content = noteText) // Update existing note
                    }
                    saveNoteLocally(context, note) // Save note locally (either new or updated)
                    navController.navigate("dashboard") {
                        popUpTo("dashboard") { inclusive = true }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save")
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

fun saveNoteLocally(context: Context, note: Note) {
    val file = File(context.filesDir, "notes.txt")
    file.appendText("Title: ${note.title}\nContent: ${note.content}\n\n")
}

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
