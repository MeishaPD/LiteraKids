package brawijaya.example.literakids.ui.screens.childProfile.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


class SchoolLevelSelector {
}@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SchoolLevelSelector(
    currentValue: String,
    onValueSelected: (String) -> Unit
) {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }
    val schoolLevels = listOf("TK", "SD", "SMP", "SMA")

    OutlinedTextField(
        value = currentValue,
        onValueChange = { },
        placeholder = { Text("Pilih jenjang sekolah") },
        modifier = Modifier
            .fillMaxWidth()
            .clickable { showDialog = true },
        shape = RoundedCornerShape(20.dp),
        readOnly = true,
        enabled = false,
        colors = OutlinedTextFieldDefaults.colors(
            disabledBorderColor = Color.Black,
        )
    )

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Pilih Jenjang Sekolah") },
            text = {
                Column {
                    schoolLevels.forEach { level ->
                        Text(
                            text = level,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onValueSelected(level)
                                    showDialog = false
                                }
                                .padding(vertical = 12.dp),
                            fontSize = 16.sp
                        )
                        Divider()
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Batal")
                }
            }
        )
    }
}