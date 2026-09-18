package edu.liceo.fieldkit.ui
 
import android.Manifest
import android.util.Log
import androidx.camera.core.ImageCapture
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import edu.liceo.fieldkit.hardware.*
import edu.liceo.fieldkit.permissions.rememberPermission
import java.io.File
 
@Composable
fun CameraCard() {
    val context = LocalContext.current
    val camera = rememberPermission(Manifest.permission.CAMERA)
    val capture = remember { ImageCapture.Builder().build() }
    var photo by remember { mutableStateOf<File?>(null) }
 
    Card(Modifier.fillMaxWidth()) {
        Column(
            Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Field photo", style = MaterialTheme.typography.titleMedium)
            PermissionGate(                                      // GIVEN
                state = camera,
                feature = "Camera",
                reason = "We need the camera to photograph the issue you report."
            ) {
                var cam by remember { mutableStateOf<androidx.camera.core.Camera?>(null) }
                var torchOn by remember { mutableStateOf(false) }
 
                val shake = rememberAccelerometer()
                var lastShot by remember { mutableLongStateOf(0L) }
                LaunchedEffect(shake) {
                    val now = System.currentTimeMillis()
                    if (isShake(shake) && now - lastShot > 1500) {
                        lastShot = now
                        takePhoto(context, capture) { photo = it }
                        context.buzz()
                        Log.d("FieldKit", "Shake capture")
                    }
                }
 
                CameraPreview(capture, Modifier.fillMaxWidth().height(240.dp)) { cam = it }
                
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = { takePhoto(context, capture) { saved -> photo = saved } }) { 
                        Text("Take photo") 
                    }
                    if (cam?.cameraInfo?.hasFlashUnit() == true) {
                        Button(onClick = {
                            torchOn = !torchOn
                            cam?.cameraControl?.enableTorch(torchOn)
                        }) {
                            Text(if (torchOn) "Torch off" else "Torch on")
                        }
                    }
                }
                
                photo?.let { Text("Saved: ${it.name}") }
            }
            photo?.let { f ->
                val thumb = remember(f) { loadThumb(f) }
                thumb?.let { Image(bitmap = it, contentDescription = "Last photo",
                    modifier = Modifier.size(96.dp)) }
            }
        }
    }
}
