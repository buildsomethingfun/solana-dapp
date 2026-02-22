package dapp.buildsomething.common.ui.style

import dapp.buildsomething.common.ui.R
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight

@OptIn(ExperimentalTextApi::class)
val dmSansFamily = FontFamily(
    Font(R.font.dm_sans_light, weight = FontWeight.Light, variationSettings = FontVariation.Settings(FontWeight.Light, FontStyle.Normal)),
    Font(R.font.dm_sans_regular, weight = FontWeight.Normal, variationSettings = FontVariation.Settings(FontWeight.Normal, FontStyle.Normal)),
    Font(R.font.dm_sans_italic, weight = FontWeight.Normal, variationSettings = FontVariation.Settings(FontWeight.Normal, FontStyle.Italic)),
    Font(R.font.dm_sans_medium, weight = FontWeight.Medium, variationSettings = FontVariation.Settings(FontWeight.Medium, FontStyle.Normal)),
    Font(R.font.dm_sans_semibold, weight = FontWeight.SemiBold, variationSettings = FontVariation.Settings(FontWeight.SemiBold, FontStyle.Normal)),
    Font(R.font.dm_sans_bold, weight = FontWeight.Bold, variationSettings = FontVariation.Settings(FontWeight.Bold, FontStyle.Normal)),
    Font(R.font.dm_sans_extrabold, weight = FontWeight.ExtraBold, variationSettings = FontVariation.Settings(FontWeight.ExtraBold, FontStyle.Normal)),
    Font(R.font.dm_sans_black, weight = FontWeight.Black, variationSettings = FontVariation.Settings(FontWeight.Black, FontStyle.Normal)),
)
