package brawijaya.example.literakids.ui.screens.childProfile.components

import androidx.compose.runtime.Composable
import brawijaya.example.literakids.ui.components.GenericSelector

@Composable
fun SchoolLevelSelector(
    currentValue: String,
    onValueSelected: (String) -> Unit
) {
    val schoolLevels = listOf("TK", "SD", "SMP", "SMA")

    GenericSelector(
        currentValue = currentValue,
        options = schoolLevels,
        onValueSelected = onValueSelected,
        placeholder = "Pilih jenjang sekolah",
        dialogTitle = "Pilih Jenjang Sekolah",
        cancelButtonText = "Batal"
    )
}