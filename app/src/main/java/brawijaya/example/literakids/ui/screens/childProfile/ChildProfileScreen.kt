package brawijaya.example.literakids.ui.screens.childProfile

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import brawijaya.example.literakids.R
import brawijaya.example.literakids.ui.components.DatePicker
import brawijaya.example.literakids.ui.screens.childProfile.components.SchoolLevelSelector

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChildProfileScreen(
    viewModel: ChildProfileViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {}
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(state.error) {
        state.error?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF5AD8FF),
                                Color(0xFFDE99FF)
                            )
                        )
                    )
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }

                Text(
                    text = "Profil Anak",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
            ) {
                Box(
                    contentAlignment = Alignment.BottomEnd
                ) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .border(2.dp, Color(0xFFDE99FF), CircleShape)
                            .background(Color.White)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.wizard_avatar),
                            contentDescription = "Avatar",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF5AD8FF))
                            .align(Alignment.BottomEnd),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_edit),
                            contentDescription = "Edit",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Text(
                    text = state.fullName,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0A617A),
                    modifier = Modifier.padding(top = 8.dp)
                )

                Text(
                    text = "@${state.username}",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 4.dp)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, end = 30.dp, bottom = 8.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_crown),
                        contentDescription = "Level",
                        tint = Color(0xFF5AD8FF),
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "Level ${state.level}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF0A617A),
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 30.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(12.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFE0E0E0))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(state.currentXp.toFloat() / state.maxXp)
                                .height(12.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    brush = Brush.horizontalGradient(
                                        colors = listOf(
                                            Color(0xFFDE99FF),
                                            Color(0x855C99CC)
                                        )
                                    )
                                )
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp)
                    ) {
                        Text(
                            text = "${state.currentXp} / ${state.maxXp} XP",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                ) {
                    Text(
                        text = "Nama Lengkap",
                        fontSize = 16.sp,
                        color = Color(0xFF0A617A),
                        modifier = Modifier.padding(bottom = 8.dp, top = 16.dp)
                    )

                    OutlinedTextField(
                        value = state.fullName,
                        onValueChange = { viewModel.updateFullName(it) },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Masukkan nama lengkap") },
                        shape = RoundedCornerShape(20.dp),
                    )

                    Text(
                        text = "Usia",
                        fontSize = 16.sp,
                        color = Color(0xFF0A617A),
                        modifier = Modifier.padding(bottom = 8.dp, top = 8.dp)
                    )

                    OutlinedTextField(
                        value = state.age,
                        onValueChange = { viewModel.updateAge(it) },
                        placeholder = { Text("Masukkan usia") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                    )

                    Text(
                        text = "Jenis Kelamin",
                        fontSize = 16.sp,
                        color = Color(0xFF0A617A),
                        modifier = Modifier.padding(bottom = 8.dp, top = 8.dp)
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        RadioButton(
                            selected = state.gender == "male",
                            onClick = { viewModel.updateGender("male") },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = Color(0xFF5AD8FF),
                                unselectedColor = Color(0xFF5AD8FF)
                            )
                        )
                        Text(
                            text = "Laki-laki",
                            fontSize = 16.sp,
                            modifier = Modifier.padding(start = 4.dp)
                        )

                        RadioButton(
                            selected = state.gender == "female",
                            onClick = { viewModel.updateGender("female") },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = Color(0xFF5AD8FF),
                                unselectedColor = Color(0xFF5AD8FF)
                            )
                        )

                        Text(
                            text = "Perempuan",
                            fontSize = 16.sp,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }

                    Text(
                        text = "Jenjang Sekolah",
                        fontSize = 16.sp,
                        color = Color(0xFF0A617A),
                        modifier = Modifier.padding(bottom = 8.dp, top = 8.dp)
                    )

                    SchoolLevelSelector(
                        currentValue = state.schoolLevel,
                        onValueSelected = { viewModel.updateSchoolLevel(it) }
                    )

                    Text(
                        text = "Tanggal Lahir",
                        fontSize = 16.sp,
                        color = Color(0xFF0A617A),
                        modifier = Modifier.padding(bottom = 8.dp, top = 8.dp)
                    )

                    DatePicker(
                        currentValue = state.birthDate,
                        onDateSelected = { viewModel.updateBirthDate(it) }
                    )

                    Button(
                        onClick = { viewModel.saveProfile() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp)
                            .height(50.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF5AD8FF)
                        )
                    ) {
                        Text(
                            text = "Perbarui Profil",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White
                        )
                    }
                }
            }
        }

        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF5AD8FF)),
                contentAlignment = Alignment.Center
            ) {
                // loading indicator
            }
        }
    }
}