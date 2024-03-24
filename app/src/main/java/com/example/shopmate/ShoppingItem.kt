package com.example.shopmate

data class ShoppingItem(val id: Int, var name: String, var quantity: Int, var isEditing: Boolean = false, var isChecked: Boolean = false)
