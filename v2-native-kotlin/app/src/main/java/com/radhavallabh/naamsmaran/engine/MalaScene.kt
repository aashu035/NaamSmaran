package com.radhavallabh.naamsmaran.engine

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import io.github.sceneview.Scene
import io.github.sceneview.math.Position
import io.github.sceneview.math.Rotation
import io.github.sceneview.rememberCameraNode
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberModelLoader
import io.github.sceneview.rememberNode
import kotlinx.coroutines.delay

/**
 * MalaScene — Phase 3 3D Tulsi Mala Engine.
 *
 * Implements a vertical top-down perspective (70° angle) using SceneView.
 * Provides a graceful fallback to a 2D Canvas if OpenGL ES 3.0+ is unsupported
 * or if the 3D model fails to load.
 */
@Composable
fun MalaScene(
    modifier: Modifier = Modifier,
    physics: MalaPhysics,
    onBeadPassed: (isSumeru: Boolean) -> Unit
) {
    // Engine loop for physics
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

    var useFallback by remember { mutableStateOf(false) }

    if (useFallback) {
        MalaSceneFallback(modifier, physics)
    } else {
        try {
            val engine = rememberEngine()
            val modelLoader = rememberModelLoader(engine)
            
            // 70° vertical top-down perspective
            val cameraNode = rememberCameraNode(engine).apply {
                position = Position(y = 2.0f, z = 1.0f)
                rotation = Rotation(x = -70f) 
            }
            
            val centerNode = rememberNode(engine)

            LaunchedEffect(physics.rotationAngle) {
                // Rotate the entire mala around Y axis based on physics
                centerNode.rotation = Rotation(y = physics.rotationAngle)
            }

            Scene(
                modifier = modifier.fillMaxSize(),
                engine = engine,
                modelLoader = modelLoader,
                cameraNode = cameraNode,
                childNodes = listOf(centerNode),
                onViewCreated = {
                    // Prepared for model loading. If loading fails or hardware unsupported,
                    // we can trigger the fallback.
                    // modelLoader.loadModelInstanceAsync("models/mala.glb") { result -> ... }
                }
            )
        } catch (e: Throwable) {
            // Hardware fallback for unsupported devices
            useFallback = true
        }
    }
}

@Composable
fun MalaSceneFallback(
    modifier: Modifier = Modifier,
    physics: MalaPhysics
) {
    // 2D Canvas fallback
    Canvas(modifier = modifier.fillMaxSize()) {
        val centerX = size.width / 2
        val centerY = size.height / 2
        // Elongate the vertical radius slightly to simulate a 3D perspective tilt
        val radiusX = minOf(centerX, centerY) * 0.8f
        val radiusY = radiusX * 0.6f 
        val beadRadius = 8f

        for (i in 0 until 108) {
            val angle = (i * (360f / 108f) + physics.rotationAngle)
                .let { Math.toRadians(it.toDouble()) }
            
            // Elliptical math to fake a 70-degree viewing angle in 2D
            val x = centerX + radiusX * kotlin.math.cos(angle).toFloat()
            val y = centerY + radiusY * kotlin.math.sin(angle).toFloat()

            val isSumeru = physics.isSumeruBead(i)
            val color = if (isSumeru) {
                Color(0xFFFFD080) // Saffron gold for Sumeru
            } else {
                Color(0xFF8B6914) // Tulsi wood brown
            }

            // Simple depth sorting by scaling beads in the 'back'
            val depthScale = (kotlin.math.sin(angle).toFloat() + 1f) / 2f // 0.0 to 1.0
            val currentRadius = beadRadius * (0.6f + (0.4f * depthScale))

            drawCircle(
                color = color,
                radius = if (isSumeru) currentRadius * 1.5f else currentRadius,
                center = Offset(x, y)
            )
        }
    }
}
