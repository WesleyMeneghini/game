package com.example.myapplication.model

data class ApiResponse(
    val header: List<String>,
    val lines: List<Line>
)

data class Line(
    val info: List<String>
)