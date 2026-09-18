package edu.liceo.fieldkit.hardware
 
import androidx.camera.compose.CameraXViewfinder
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.core.SurfaceRequest
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LocalLifecycleOwner
import kotlinx.coroutines.awaitCancellation
import kotlin.coroutines.resume
import kotlinx.coroutines.suspendCancellableCoroutine
import androidx.core.content.ContextCompat

@Composable
fun CameraPreview(
    capture: ImageCapture,
    modifier: Modifier = Modifier,
    onCameraReady: (Camera) -> Unit = {}          // used by the bonus
) {
    val context = LocalContext.current
    val owner = LocalLifecycleOwner.current
    var request by remember { mutableStateOf<SurfaceRequest?>(null) }
 
    LaunchedEffect(owner) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        val provider = suspendCancellableCoroutine { cont ->
            cameraProviderFuture.addListener({
                try {
                    cont.resume(cameraProviderFuture.get())
                } catch (t: Throwable) {
                    cont.resumeWith(Result.failure(t))
                }
            }, ContextCompat.getMainExecutor(context))
        }

        val preview = Preview.Builder().build()
        preview.setSurfaceProvider { req -> 
            request = req 
        }
        provider.unbindAll()
        val camera = provider.bindToLifecycle(owner, CameraSelector.DEFAULT_BACK_CAMERA, preview, capture)
        onCameraReady(camera)
        try { awaitCancellation() } finally { provider.unbindAll() }   // GIVEN
    }
    request?.let {
        CameraXViewfinder(surfaceRequest = it, modifier = modifier)   // GIVEN
    }
}
