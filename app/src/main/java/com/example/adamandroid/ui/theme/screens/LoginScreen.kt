package com.example.adamandroid.ui.theme.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.adamandroid.ui.theme.AdamAndroidTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.adamandroid.R

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onLoginSuccess: () -> Unit
) {

    var username by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    var usernameError by remember {
        mutableStateOf("")
    }

    var passwordError by remember {
        mutableStateOf("")
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF5F7FB))
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        // =========================
        // LOGO
        // =========================
        Image(
            painter = painterResource(id = R.drawable.englogoicon),
            contentDescription = "Logo",
            modifier = Modifier
                .size(90.dp)
                .clip(RoundedCornerShape(20.dp)),
            contentScale = ContentScale.Fit
        )

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        // =========================
        // TITLE
        // =========================
        Text(
            text = "Đăng nhập",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1F2937)
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text = "Đăng nhập để tiếp tục",
            fontSize = 14.sp,
            color = Color(0xFF6B7280)
        )

        Spacer(
            modifier = Modifier.height(32.dp)
        )

        // =========================
        // FORM
        // =========================
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 420.dp)
        ) {

            // USERNAME
            Text(
                text = "Username",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF374151),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            OutlinedTextField(
                value = username,
                onValueChange = {
                    username = it

                    usernameError = when {
                        it.isBlank() -> "Username không được để trống"
                        it.length < 4 -> "Username phải có ít nhất 4 ký tự"
                        else -> ""
                    }

                },
                placeholder = {
                    Text(
                        text = "Nhập username",
                        color = Color(0xFF9CA3AF)
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Username",
                        tint = Color(0xFF6B7280)
                    )
                },
                singleLine = true,
                isError = usernameError.isNotEmpty(),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF2563EB),
                    unfocusedBorderColor = Color(0xFFD1D5DB),
                    errorBorderColor = Color(0xFFEF4444),

                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    errorContainerColor = Color.White,

                    focusedTextColor = Color(0xFF111827),
                    unfocusedTextColor = Color(0xFF111827),

                    cursorColor = Color(0xFF2563EB)
                )
            )

            if (usernameError.isNotEmpty()) {
                Text(
                    text = usernameError,
                    color = Color(0xFFEF4444),
                    fontSize = 12.sp,
                    modifier = Modifier.padding(
                        start = 4.dp,
                        top = 6.dp
                    )
                )
            }

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            // =========================
            // PASSWORD
            // =========================
            Text(
                text = "Password",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF374151),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it

                    passwordError = when {
                        it.isBlank() -> "Password không được để trống"
                        it.length < 6 -> "Password phải có ít nhất 6 ký tự"
                        else -> ""
                    }
                },
                placeholder = {
                    Text(
                        text = "Nhập password",
                        color = Color(0xFF9CA3AF)
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Password",
                        tint = Color(0xFF6B7280)
                    )
                },
                singleLine = true,
                isError = passwordError.isNotEmpty(),
                visualTransformation = PasswordVisualTransformation(),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF2563EB),
                    unfocusedBorderColor = Color(0xFFD1D5DB),
                    errorBorderColor = Color(0xFFEF4444),

                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    errorContainerColor = Color.White,

                    focusedTextColor = Color(0xFF111827),
                    unfocusedTextColor = Color(0xFF111827),

                    cursorColor = Color(0xFF2563EB)
                )
            )

            if (passwordError.isNotEmpty()) {
                Text(
                    text = passwordError,
                    color = Color(0xFFEF4444),
                    fontSize = 12.sp,
                    modifier = Modifier.padding(
                        start = 4.dp,
                        top = 6.dp
                    )
                )
            }

            Spacer(
                modifier = Modifier.height(28.dp)
            )

            // =========================
            // LOGIN BUTTON
            // =========================
            Button(
                onClick = {

                    if (
                        usernameError.isEmpty() &&
                        passwordError.isEmpty() &&
                        username.isNotBlank() &&
                        password.isNotBlank()
                    ) {

                        // Đăng nhập thành công
                        onLoginSuccess()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2563EB),
                    contentColor = Color.White
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 2.dp,
                    pressedElevation = 6.dp
                )
            ) {
                Text(
                    text = "ĐĂNG NHẬP",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    AdamAndroidTheme {
//        LoginScreen()
//    }
//}