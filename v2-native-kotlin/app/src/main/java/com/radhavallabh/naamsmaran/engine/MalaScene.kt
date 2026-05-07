package com.radhavallabh.naamsmaran.engine

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.delay

/**
 * MalaScene — Phase 2 3D Tulsi Mala Engine.
 *
 * Current state: Compilable placeholder with physics loop running.
 * The full SceneView/Filament 3D implementation will replace this
 * Canvas-based stub once the 3D assets (mala.glb) and PBR materials
 * are ready.
 *
 * The physics engine (MalaPhysics) and input controller (MalaInputController)
 * are fully functional — only the visual rendering is stubbed.
 *
 * श्री राधावल्लभ लाल जु की जय 🙏
 */
@Composable
fun MalaScene(
    modifier: Modifier = Modifier,
    physics: MalaPhysics,
    onBeadPassed: (isSumeru: Boolean) -> Unit
) {
    // Engine loop for physics — this is production-ready
    LaunchedEffect(physics) {
        var lastTime = System.nanoTime()
        while (true) {
            val now = System.nanoTime()
            val dt = (now - lastTime) / 1_000_000_000f
            lastTime = now

            val beadsPassed = physics.update(dt)
            if (beadsPassed != 0) {
                val absoluteIndex = Math.floor(
                    (physics.rotationAngle / physics.anglePerBead).toDouble()
                ).toInt()
                val isSumeru = physics.isSumeruBead(absoluteIndex)
                onBeadPassed(isSumeru)
            }

            delay(16) // ~60 FPS
        }
    }

    // Placeholder visual — simple 2D bead ring
    // Will be replaced by SceneView/Filament 3D render in Phase 2
    Canvas(modifier = modifier.fillMaxSize()) {
        val centerX = size.width / 2
        val centerY = size.height / 2
        val radius = minOf(centerX, centerY) * 0.6f
        val beadRadius = 8f

        for (i in 0 until 108) {
            val angle = (i * (360f / 108f) + physics.rotationAngle)
                .let { Math.toRadians(it.toDouble()) }
            val x = centerX + radius * kotlin.math.cos(angle).toFloat()
            val y = centerY + radius * kotlin.math.sin(angle).toFloat()

            val isSumeru = physics.isSumeruBead(i)
            val color = if (isSumeru) {
                Color(0xFFFFD080) // Saffron gold for Sumeru
            } else {
                Color(0xFF8B6914) // Tulsi wood brown
            }

            drawCircle(
                color = color,
                radius = if (isSumeru) beadRadius * 1.5f else beadRadius,
                center = Offset(x, y)
            )
        }
    }
}
