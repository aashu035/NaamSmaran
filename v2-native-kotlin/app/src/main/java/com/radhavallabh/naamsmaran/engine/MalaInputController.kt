package com.radhavallabh.naamsmaran.engine

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import kotlin.math.atan2

/**
 * Creates a modifier that handles thumb-arc drag gestures on the 3D Mala scene 
 * and routes them to the MalaPhysics engine.
 */
fun Modifier.malaInput(
    physics: MalaPhysics,
    centerX: Float = 500f, // Will be updated dynamically via layout coordinates if needed, but relative delta is fine
    centerY: Float = 1000f,
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
        },
        onDragCancel = {
            onDragEnd()
        },
        onDrag = { change, _ ->
            change.consume()
            
            val currentPos = change.position
            val previousPos = change.previousPosition
            
            // Calculate polar angle for current and previous points relative to an assumed center (e.g. bottom center of screen)
            // A typical thumb arc on the bottom half of the screen revolves around a point near the bottom center.
            val currentAngle = atan2(currentPos.y - centerY, currentPos.x - centerX)
            val previousAngle = atan2(previousPos.y - centerY, previousPos.x - centerX)
            
            // Convert radians to degrees
            var deltaAngle = Math.toDegrees((currentAngle - previousAngle).toDouble()).toFloat()
            
            // Handle wrap-around at -180/180
            if (deltaAngle > 180f) deltaAngle -= 360f
            if (deltaAngle < -180f) deltaAngle += 360f
            
            // For a thumb arc, a sweeping motion advances the beads. 
            // We apply a multiplier to make the gesture feel natural.
            physics.onDrag(deltaAngle * 1.5f)
        }
    )
}
