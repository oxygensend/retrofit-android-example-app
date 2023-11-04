package com.oxygensend.shoppinglist.api.dto

data class RegisterRequest(val email: String, val password: String, val firstName: String, val lastName: String) {
}