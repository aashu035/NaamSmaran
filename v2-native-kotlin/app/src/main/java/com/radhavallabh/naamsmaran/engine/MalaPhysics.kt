package com.radhavallabh.naamsmaran.engine

import kotlin.math.abs

/**
 * Handles the physics of the 3D Mala (exponential decay for fling, bead snap, etc).
 * The Mala has 108 beads.
 */
class MalaPhysics(
    val beadsCount: Int = 108,
    val friction: Float = 0.92f, // Velocity decay factor per frame
    val snapSpeed: Float = 0.1f  // Speed at which it snaps to the nearest bead
) {
    var rotationAngle: Float = 0f
        private set
        
    var velocity: Float = 0f
        private set

    // The angle per bead
    val anglePerBead: Float = 360f / beadsCount

    private var lastPassedBeadIndex: Int = 0

    // Called when the user drags the mala
    fun onDrag(deltaAngle: Float) {
        rotationAngle += deltaAngle
        velocity = 0f // Stop physics when user touches
    }

    // Called when the user lifts finger with some velocity
    fun onFling(initialVelocity: Float) {
        velocity = initialVelocity
    }

    // Call this every frame (e.g., 60fps) to update physics
    // Returns the number of beads passed in this frame (positive or negative)
    fun update(deltaTimeSeconds: Float): Int {
        // Apply friction to velocity
        velocity *= friction

        // If velocity is very small, start snapping to the nearest bead
        if (abs(velocity) < 0.1f) {
            velocity = 0f
            val targetAngle = Math.round(rotationAngle / anglePerBead) * anglePerBead
            val diff = targetAngle - rotationAngle
            rotationAngle += diff * snapSpeed // Soft snap
        } else {
            // Apply velocity
            rotationAngle += velocity * deltaTimeSeconds
        }

        // Keep rotation within 0..360 bounds for simplicity, or just let it grow.
        // Letting it grow is easier for tracking absolute beads passed.
        
        // Calculate current bead index
        val currentBeadIndex = Math.floor((rotationAngle / anglePerBead).toDouble()).toInt()
        
        val beadsPassed = currentBeadIndex - lastPassedBeadIndex
        if (beadsPassed != 0) {
            lastPassedBeadIndex = currentBeadIndex
            return beadsPassed
        }
        
        return 0
    }
    
    // Check if the current passed bead is the Sumeru (108th bead)
    fun isSumeruBead(absoluteIndex: Int): Boolean {
        // Absolute index could be anything, so we modulo it
        val normalizedIndex = (absoluteIndex % beadsCount + beadsCount) % beadsCount
        return normalizedIndex == 0
    }
}
