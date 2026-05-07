package com.radhavallabh.naamsmaran.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/**
 * Naam Smaran — Shape System
 * From tokens.css Category D:
 * --radius-card:    24dp  (all GlassCard instances)
 * --radius-button:  14dp  (all action buttons)
 * --radius-chip:    999dp (section pills, tags — full pill)
 * --radius-input:   12dp  (text inputs, search)
 * --radius-small:   10dp  (badges, small elements)
 *
 * RULE: No border-radius below 12dp on cards. Buttons 14dp. Chips 999dp.
 */
val NaamSmaranShapes = Shapes(
    // Cards, dialogs, bottom sheets
    large = RoundedCornerShape(24.dp),

    // Buttons
    medium = RoundedCornerShape(14.dp),

    // Inputs
    small = RoundedCornerShape(12.dp),

    // Small elements — badges
    extraSmall = RoundedCornerShape(10.dp)
)

// Additional shapes not in Material3 Shapes
val ChipShape = RoundedCornerShape(999.dp) // Full pill
val CardShape = RoundedCornerShape(24.dp)
val ButtonShape = RoundedCornerShape(14.dp)
val InputShape = RoundedCornerShape(12.dp)
