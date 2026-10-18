package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.ui.theme.*

@Composable
fun WidadWordmark(
    modifier: Modifier = Modifier,
    isDark: Boolean = false,
    showTagline: Boolean = true,
    language: AppLanguage = AppLanguage.ENGLISH
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            listOf(BurgundyPrimary, ChampagneGold)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "م",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "WIDAD",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp,
                color = if (isDark) ChampagneGoldLight else BurgundyPrimary
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "• مودة",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = ChampagneGold
            )
        }
        if (showTagline) {
            Text(
                text = if (language == AppLanguage.ARABIC) "حب ذو معنى. ومستقبل يدوم." else "Meaningful Love. A Lasting Future.",
                fontSize = 12.sp,
                color = if (isDark) Color(0xFFD6C8C5) else CharcoalMuted,
                letterSpacing = 0.5.sp,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}

@Composable
fun LanguageToggle(
    currentLanguage: AppLanguage,
    onLanguageSelected: (AppLanguage) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = WarmIvoryVariant,
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
        modifier = modifier.testTag("language_toggle")
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(if (currentLanguage == AppLanguage.ENGLISH) BurgundyPrimary else Color.Transparent)
                    .clickable { onLanguageSelected(AppLanguage.ENGLISH) }
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "EN",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (currentLanguage == AppLanguage.ENGLISH) Color.White else CharcoalMuted
                )
            }
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(if (currentLanguage == AppLanguage.ARABIC) BurgundyPrimary else Color.Transparent)
                    .clickable { onLanguageSelected(AppLanguage.ARABIC) }
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "العربية",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (currentLanguage == AppLanguage.ARABIC) Color.White else CharcoalMuted
                )
            }
        }
    }
}

@Composable
fun CompatibilityBadge(
    percentage: Int,
    language: AppLanguage,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = GoldContainer,
        border = androidx.compose.foundation.BorderStroke(1.dp, ChampagneGold),
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = null,
                tint = BurgundyPrimary,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "$percentage% ${if (language == AppLanguage.ARABIC) "توافق" else "Match"}",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = BurgundyPrimary
            )
        }
    }
}

@Composable
fun DisclaimerNote(
    textEn: String,
    textAr: String,
    language: AppLanguage,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = WarmIvoryVariant.copy(alpha = 0.7f),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.Info,
                contentDescription = null,
                tint = CharcoalMuted,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (language == AppLanguage.ARABIC) textAr else textEn,
                fontSize = 11.sp,
                color = CharcoalMuted,
                lineHeight = 15.sp
            )
        }
    }
}
