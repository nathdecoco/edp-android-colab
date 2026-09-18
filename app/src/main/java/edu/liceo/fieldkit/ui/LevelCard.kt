package edu.liceo.fieldkit.ui
 
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edu.liceo.fieldkit.hardware.rememberAccelerometer
import kotlin.math.abs
 
// GIVEN (read it, do not change it): flat means x and y are close to 0
fun isLevel(x: Float, y: Float, tolerance: Float = 0.5f) =
    abs(x) < tolerance && abs(y) < tolerance
 
@Composable
fun LevelCard() {
    val v = rememberAccelerometer()
    Card(Modifier.fillMaxWidth()) {
        Column(
            Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text("Level check", style = MaterialTheme.typography.titleMedium)
            Text("x = %.2f   y = %.2f   z = %.2f".format(v[0], v[1], v[2]))
            val level = isLevel(v[0], v[1])
            Text(
                if (level) "LEVEL ✓" else "Tilted, adjust",
                color = if (level) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
            )
        }
    }
}
