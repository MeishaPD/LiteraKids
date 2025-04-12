package brawijaya.example.literakids.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import brawijaya.example.literakids.ui.navigation.Screen

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Gradient header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF5DCCF8),
                            Color(0xFFD3BCFF)
                        )
                    )
                ),
            contentAlignment = Alignment.TopStart
        ) {
            IconButton(
                onClick = { navController.navigateUp() },
                modifier = Modifier.padding(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Kembali",
                    tint = Color.White
                )
            }

            Text(
                text = "Login",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Login Form
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Email field
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = "Email",
                        tint = Color(0xFF5DCCF8)
                    )
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF5DCCF8),
                    unfocusedBorderColor = Color.Gray,
                    focusedLabelColor = Color(0xFF5DCCF8)
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password field
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Password",
                        tint = Color(0xFF5DCCF8)
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = if (passwordVisible) "Sembunyikan password" else "Tampilkan password",
                            tint = Color(0xFF5DCCF8)
                        )
                    }
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF5DCCF8),
                    unfocusedBorderColor = Color.Gray,
                    focusedLabelColor = Color(0xFF5DCCF8)
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Forgot password
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                TextButton(
                    onClick = {
                        navController.navigate("forgot_password")
                    }
                ) {
                    Text(
                        text = "Lupa Password?",
                        color = Color(0xFF5DCCF8),
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Login button
            Button(
                onClick = {
                    viewModel.signIn(email, password)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF5DCCF8)
                )
            ) {
                Text(
                    text = "Login",
                    fontSize = 18.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Register option
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Belum memiliki akun?",
                    color = Color.Gray
                )

                TextButton(
                    onClick = {
                        navController.navigate(Screen.Register.route)
                    }
                ) {
                    Text(
                        text = "Daftar",
                        color = Color(0xFF5DCCF8),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }

    // Error dialog
    if (uiState.error.isNotEmpty()) {
        AlertDialog(
            onDismissRequest = { viewModel.clearError() },
            title = { Text(text = "Terjadi Kesalahan") },
            text = { Text(text = uiState.error) },
            confirmButton = {
                TextButton(
                    onClick = { viewModel.clearError() }
                ) {
                    Text("OK")
                }
            }
        )
    }

    // Loading indicator
    if (uiState.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f)),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                color = Color(0xFF5DCCF8)
            )
        }
    }

    // Success navigation
    LaunchedEffect(uiState.isLoggedIn) {
        if (uiState.isLoggedIn) {
            navController.navigate(Screen.Profile.route) {
                popUpTo(Screen.Login.route) { inclusive = true }
            }
        }
    }
}