package com.oxygensend.shoppinglist

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.oxygensend.shoppinglist.api.ApiClient
import com.oxygensend.shoppinglist.api.context.Context
import com.oxygensend.shoppinglist.api.context.ContextKeys
import com.oxygensend.shoppinglist.api.dto.AuthResponse
import com.oxygensend.shoppinglist.api.dto.LoginRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            setContentView(R.layout.activity_main)
        }
    }

    fun onMatch(view: View) {
        val textId = findViewById<EditText>(R.id.emailText)
        val textPass = findViewById<EditText>(R.id.editTextTextPassword)
        val textTitle = findViewById<TextView>(R.id.textViewId)

        ApiClient.login(LoginRequest(textId.text.toString(), textPass.text.toString()))
            .enqueue(object : Callback<AuthResponse> {
                override fun onResponse(
                    call: Call<AuthResponse>,
                    response: Response<AuthResponse>
                ) {
                    if (response.isSuccessful) {
                        textTitle.text = response.body()?.accessToken
                        Context.create(this@LoginActivity)
                        response.body()?.accessToken?.let { Context.saveString(ContextKeys.ACCESS_TOKEN, it) }
                        response.body()?.refreshToken?.let { Context.saveString(ContextKeys.REFRESH_TOKEN, it) }

                        // Redirect to ShoppingListsActivity
                        startActivity(Intent(this@LoginActivity, ShoppingListsActivity::class.java))

                    } else {
                        textTitle.text = "Bad Credentials"
                    }

                }

                override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                    textTitle.text = t.message
                }
            })
    }

}
