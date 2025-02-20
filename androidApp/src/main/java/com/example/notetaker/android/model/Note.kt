package com.example.notetaker.android.model

import java.util.UUID

data class Note(
    val id: String = UUID.randomUUID().toString(), // Unique id for each note
    val title: String = "",
    val content: String = ""
)
