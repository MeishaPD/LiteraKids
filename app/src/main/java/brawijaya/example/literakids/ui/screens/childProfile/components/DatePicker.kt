package brawijaya.example.literakids.ui.screens.childProfile.components

import android.app.DatePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.util.Calendar

@Composable
fun DatePicker(
    currentValue: String,
    onDateSelected: (String) -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            onDateSelected("$dayOfMonth/${month + 1}/$year")
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    OutlinedTextField(
        value = currentValue,
        onValueChange = { },
        placeholder = { Text("Pilih tanggal lahir") },
        modifier = Modifier
            .fillMaxWidth()
            .clickable { datePickerDialog.show() },
        shape = RoundedCornerShape(20.dp),
        readOnly = true,
        enabled = false,
        colors = OutlinedTextFieldDefaults.colors(
            disabledBorderColor = Color.Black,
        )
    )
}