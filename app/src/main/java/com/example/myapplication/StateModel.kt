package com.example.myapplication

data class StateModel(
    val state: String,
    val recovered: Int,
    val deaths: Int,
    val cases: Int
)