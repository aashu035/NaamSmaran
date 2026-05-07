package com.radhavallabh.naamsmaran.engine

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput

/**
 * Creates a modifier that handles drag gestures on the 3D Mala scene 
 * and routes them to the MalaPhysics engine.
 */
fun Modifier.malaInput(
    physics: MalaPhysics,
    sensitivity: Float = 0.5f,
    onDragStart: () -> Unit = {},
    onDragEnd: () -> Unit = {}
): Modifier = this.pointerInput(Unit) {
    detectDragGestures(
        onDragStart = { _ ->
            onDragStart()
            physics.onDrag(0f)
        },
        onDragEnd = {
            onDragEnd()
            // We could track actual pointer velocity here for fling,
            // but for simple drags we just set a default fling based on last delta, 
            // or pass velocity from velocityTracker if implemented.
        },
        onDragCancel = {
            onDragEnd()
        },
        onDrag = { change, dragAmount ->
            change.consume()
            // Typically horizontal drag rotates the mala around Y axis
            // Or vertical drag rotates around X. 
            // Let's map vertical Y drag to rotation for a vertically hanging mala.
            val deltaAngle = dragAmount.y * sensitivity
            physics.onDrag(deltaAngle)
        }
    )
}
