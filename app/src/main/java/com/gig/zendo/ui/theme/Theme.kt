package com.gig.zendo.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary     = BrandPrimary,
    secondary   = BrandSecondary,
    tertiary    = BrandTertiaryGreen,

    background  = BgLight,
    surface     = SurfaceLight,

    onPrimary   = OnPrimaryLight,
    onSecondary = OnSecondaryLight,
    onTertiary  = OnTertiaryLight,
    onBackground= OnBgLight,
    onSurface   = OnSurfaceLight,
)

private val DarkColorScheme = darkColorScheme(
    primary     = BrandPrimaryYellowDark,
    secondary   = BrandSecondaryYellowDark,
    tertiary    = BrandTertiaryGreenDark,

    background  = BgDark,
    surface     = SurfaceDark,

    onPrimary   = OnPrimaryDark,
    onSecondary = OnSecondaryDark,
    onTertiary  = OnTertiaryDark,
    onBackground= OnBgDark,
    onSurface   = OnSurfaceDark,
)

@Composable
fun ZendoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Để màu thương hiệu nhất quán khi lên CH Play, nên để false.
    // Bật lên nếu muốn theo Material You (Android 12+).
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
