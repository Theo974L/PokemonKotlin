package com.example.evalkotlin.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import com.example.evalkotlin.R

private val DarkColorScheme
    @Composable get() =
        darkColorScheme(
            primary = colorResource(id = R.color.primary),
            secondary = colorResource(id = R.color.secondary),
            tertiary = colorResource(id = R.color.tertiary),
            onPrimary = colorResource(id = R.color.onPrimary),
            onSecondary = colorResource(id = R.color.onSecondary),
            onTertiary = colorResource(id = R.color.onTertiary),
            background = colorResource(id = R.color.background),
            onBackground = colorResource(id = R.color.onBackground)

        )

private val LightColorScheme
    @Composable get() =
        lightColorScheme(
            primary = colorResource(id = R.color.primary),
            secondary = colorResource(id = R.color.secondary),
            tertiary = colorResource(id = R.color.tertiary),
            onPrimary = colorResource(id = R.color.onPrimary),
            onSecondary = colorResource(id = R.color.onSecondary),
            onTertiary = colorResource(id = R.color.onTertiary),
            background = colorResource(id = R.color.background),
            onBackground = colorResource(id = R.color.onBackground)

        )

@Composable
fun LeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
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