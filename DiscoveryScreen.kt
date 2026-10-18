package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.WidadRepository
import com.example.model.*
import com.example.ui.components.CompatibilityBadge
import com.example.ui.components.DisclaimerNote
import com.example.ui.theme.*

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DiscoveryScreen(
    language: AppLanguage,
    repository: WidadRepository,
    onNavigateToChat: (conversationId: String) -> Unit,
    onNavigateToUpgrade: () -> Unit
) {
    val isAr = language == AppLanguage.ARABIC
    val currentUser by repository.currentUser.collectAsState()
    val profiles by repository.profiles.collectAsState()
    val likedIds by repository.likedUserIds.collectAsState()
    val passedIds by repository.passedUserIds.collectAsState()
    val blockedIds by repository.blockedUserIds.collectAsState()

    var showFilterSheet by remember { mutableStateOf(false) }
    var selectedCountryFilter by remember { mutableStateOf<String?>(null) }
    var selectedGoalFilter by remember { mutableStateOf<RelationshipGoal?>(null) }
    var minAgeFilter by remember { mutableFloatStateOf(18f) }
    var maxAgeFilter by remember { mutableFloatStateOf(45f) }

    // Dialog state for mutual match
    var matchedProfile by remember { mutableStateOf<UserProfile?>(null) }

    // Filter eligible profiles
    val eligibleProfiles = remember(profiles, likedIds, passedIds, blockedIds, selectedCountryFilter, selectedGoalFilter, minAgeFilter, maxAgeFilter) {
        profiles.filter { profile ->
            profile.id != currentUser.id &&
            !profile.isSuspended &&
            !profile.isProfileHidden &&
            profile.id !in blockedIds &&
            profile.id !in passedIds &&
            (selectedCountryFilter == null || profile.country == selectedCountryFilter) &&
            (selectedGoalFilter == null || profile.relationshipGoal == selectedGoalFilter) &&
            profile.calculatedAge in minAgeFilter.toInt()..maxAgeFilter.toInt()
        }
    }

    var currentIndex by remember { mutableIntStateOf(0) }
    val currentCardProfile = if (eligibleProfiles.isNotEmpty() && currentIndex in eligibleProfiles.indices) {
        eligibleProfiles[currentIndex]
    } else null

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WarmIvory)
            .padding(horizontal = 16.dp)
            .testTag("discovery_screen")
    ) {
        // Filter bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = if (isAr) "استكشاف الشريك المتوافق" else "Intentional Discovery",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = BurgundyPrimary
                )
                Text(
                    text = "${eligibleProfiles.size} ${if (isAr) "ملفات موثقة متاحة" else "eligible profiles"}",
                    fontSize = 12.sp,
                    color = CharcoalMuted
                )
            }
            FilledTonalButton(
                onClick = { showFilterSheet = true },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.filledTonalButtonColors(containerColor = GoldContainer),
                modifier = Modifier.testTag("discovery_filter_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Tune,
                    contentDescription = "Filters",
                    tint = BurgundyPrimary,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (isAr) "الفلاتر" else "Filters",
                    color = BurgundyPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
        }

        if (currentCardProfile != null) {
            val compatibility = repository.calculateCompatibility(currentUser, currentCardProfile)

            // Discovery Card
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = WarmIvorySurface),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .testTag("profile_card_${currentCardProfile.id}")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    // Photo Header
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(280.dp)
                    ) {
                        Image(
                            painter = painterResource(id = currentCardProfile.photoResId ?: R.drawable.img_avatar_layla),
                            contentDescription = currentCardProfile.displayName,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        // Gradient
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f)),
                                        startY = 300f
                                    )
                                )
                        )
                        // Verified & Premium badges
                        Row(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            if (currentCardProfile.isVerified) {
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = BurgundyPrimary.copy(alpha = 0.9f)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Verified,
                                            contentDescription = "Verified",
                                            tint = ChampagneGoldLight,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = if (isAr) "موثق" else "Verified",
                                            fontSize = 11.sp,
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }

                        // Name & Age bottom left
                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(16.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${currentCardProfile.displayName}, ${currentCardProfile.calculatedAge}",
                                    color = Color.White,
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Place,
                                    contentDescription = null,
                                    tint = ChampagneGoldLight,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "${currentCardProfile.city}, ${currentCardProfile.country}",
                                    color = Color(0xFFF3ECE0),
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }

                    // Card Body Details
                    Column(modifier = Modifier.padding(16.dp)) {
                        // Compatibility Badge & breakdown
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CompatibilityBadge(
                                percentage = compatibility.percentage,
                                language = language
                            )
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = WarmIvoryVariant
                            ) {
                                Text(
                                    text = currentCardProfile.profession,
                                    fontSize = 12.sp,
                                    color = CharcoalText,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Relationship Goal Banner
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = BurgundyContainer,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.FavoriteBorder,
                                    contentDescription = null,
                                    tint = BurgundyPrimary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "${if (isAr) "الهدف المعلن: " else "Declared Goal: "}${if (isAr) currentCardProfile.relationshipGoal.labelAr else currentCardProfile.relationshipGoal.labelEn}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = OnBurgundyContainer
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Bio
                        Text(
                            text = if (isAr) "عن الشخصية:" else "About:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = CharcoalText
                        )
                        Text(
                            text = currentCardProfile.bio,
                            fontSize = 13.sp,
                            color = CharcoalMuted,
                            lineHeight = 18.sp,
                            modifier = Modifier.padding(top = 2.dp)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Common & Distinct Interests
                        Text(
                            text = if (isAr) "الاهتمامات والهوايات:" else "Interests & Passions:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = CharcoalText
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            currentCardProfile.interests.forEach { interest ->
                                val isCommon = interest in compatibility.commonInterests
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (isCommon) GoldContainer else WarmIvoryVariant,
                                    border = if (isCommon) androidx.compose.foundation.BorderStroke(1.dp, ChampagneGold) else null
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        if (isCommon) {
                                            Icon(
                                                imageVector = Icons.Default.Check,
                                                contentDescription = null,
                                                tint = BurgundyDark,
                                                modifier = Modifier.size(12.dp)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                        }
                                        Text(
                                            text = interest,
                                            fontSize = 11.sp,
                                            fontWeight = if (isCommon) FontWeight.Bold else FontWeight.Normal,
                                            color = if (isCommon) BurgundyDark else CharcoalText
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Disclaimer Note
                        DisclaimerNote(
                            textEn = "Compatibility indicator is estimated from shared profile data; it is not a scientific or guaranteed prediction.",
                            textAr = "مؤشر التوافق تقديري يعتمد على البيانات المدخلة المشتركة، ولا يمثل تنبؤاً علمياً قاطعاً.",
                            language = language
                        )
                    }
                }
            }

            // Interactive Bottom Actions Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Pass Button
                IconButton(
                    onClick = {
                        repository.passProfile(currentCardProfile.id)
                    },
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(WarmIvorySurface)
                        .border(1.dp, BorderSubtle, CircleShape)
                        .testTag("pass_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Pass",
                        tint = StatusRed,
                        modifier = Modifier.size(28.dp)
                    )
                }

                // Super Intent / Compliment
                IconButton(
                    onClick = {
                        val isMatch = repository.likeProfile(currentCardProfile.id)
                        if (isMatch) {
                            matchedProfile = currentCardProfile
                        }
                    },
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(GoldContainer)
                        .border(1.dp, ChampagneGold, CircleShape)
                        .testTag("super_intent_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Super Like",
                        tint = ChampagneGoldDark,
                        modifier = Modifier.size(24.dp)
                    )
                }

                // Like Button (Heart)
                IconButton(
                    onClick = {
                        val isMatch = repository.likeProfile(currentCardProfile.id)
                        if (isMatch) {
                            matchedProfile = currentCardProfile
                        }
                    },
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(BurgundyPrimary)
                        .testTag("like_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Like",
                        tint = ChampagneGoldLight,
                        modifier = Modifier.size(34.dp)
                    )
                }
            }
        } else {
            // Empty State
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = WarmIvorySurface),
                    modifier = Modifier.padding(24.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(GoldContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AllInclusive,
                                contentDescription = null,
                                tint = BurgundyPrimary,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = if (isAr) "شاهدت جميع الملفات المتاحة حالياً!" else "You've reviewed all available profiles!",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = CharcoalText,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        Text(
                            text = if (isAr) "قم بتوسيع فلاتر البحث أو مراجعة المشتركين الجدد قريباً." else "Broaden your discovery filters or check back as new verified members join Widad.",
                            fontSize = 13.sp,
                            color = CharcoalMuted,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            modifier = Modifier.padding(top = 6.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                selectedCountryFilter = null
                                selectedGoalFilter = null
                                minAgeFilter = 18f
                                maxAgeFilter = 50f
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = BurgundyPrimary)
                        ) {
                            Text(if (isAr) "إعادة تعيين الفلاتر" else "Reset Discovery Filters")
                        }
                    }
                }
            }
        }
    }

    // Mutual Match Celebration Dialog
    if (matchedProfile != null) {
        val matched = matchedProfile!!
        val conversationId = "match_me_${matched.id}"

        AlertDialog(
            onDismissRequest = { matchedProfile = null },
            confirmButton = {
                Button(
                    onClick = {
                        val idToNav = conversationId
                        matchedProfile = null
                        onNavigateToChat(idToNav)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = BurgundyPrimary),
                    modifier = Modifier.testTag("match_dialog_message_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.ChatBubble,
                        contentDescription = null,
                        tint = ChampagneGoldLight,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(if (isAr) "بدء محادثة محترمة" else "Start Respectful Chat")
                }
            },
            dismissButton = {
                TextButton(onClick = { matchedProfile = null }) {
                    Text(if (isAr) "متابعة الاستكشاف" else "Keep Exploring")
                }
            },
            title = {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = if (isAr) "توافق متبادل! مودة ومحبة" else "It's a Mutual Match!",
                        fontWeight = FontWeight.Bold,
                        color = BurgundyPrimary,
                        fontSize = 20.sp
                    )
                    Text(
                        text = if (isAr) "أعجب كلاكما بالآخر بنيّة جادة" else "You both expressed mutual interest",
                        fontSize = 12.sp,
                        color = CharcoalMuted
                    )
                }
            },
            text = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(70.dp)
                                .clip(CircleShape)
                                .border(2.dp, ChampagneGold, CircleShape)
                        ) {
                            Image(
                                painter = painterResource(id = currentUser.photoResId ?: R.drawable.img_avatar_tariq),
                                contentDescription = currentUser.displayName,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(BurgundyPrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = null,
                                tint = ChampagneGoldLight,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Box(
                            modifier = Modifier
                                .size(70.dp)
                                .clip(CircleShape)
                                .border(2.dp, ChampagneGold, CircleShape)
                        ) {
                            Image(
                                painter = painterResource(id = matched.photoResId ?: R.drawable.img_avatar_layla),
                                contentDescription = matched.displayName,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                    Text(
                        text = "${currentUser.displayName} & ${matched.displayName}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = CharcoalText
                    )
                    Text(
                        text = if (isAr) "تم فتح نافذة المحادثة الخاصة والمباشرة الآن." else "Direct, secure messaging is now unlocked between your verified accounts.",
                        fontSize = 12.sp,
                        color = CharcoalMuted,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        )
    }

    // Filter Bottom Sheet / Dialog
    if (showFilterSheet) {
        AlertDialog(
            onDismissRequest = { showFilterSheet = false },
            title = {
                Text(
                    text = if (isAr) "فلاتر الاستكشاف والتوافق" else "Discovery & Match Filters",
                    fontWeight = FontWeight.Bold,
                    color = BurgundyPrimary
                )
            },
            text = {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    Text(
                        text = if (isAr) "الفئة العمرية (الحد الأدنى ١٨)" else "Age Range (Min age 18):",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp
                    )
                    Text(
                        text = "${minAgeFilter.toInt()} - ${maxAgeFilter.toInt()} ${if (isAr) "سنة" else "years"}",
                        fontSize = 12.sp,
                        color = BurgundyPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Slider(
                        value = maxAgeFilter,
                        onValueChange = { maxAgeFilter = it },
                        valueRange = 21f..60f,
                        colors = SliderDefaults.colors(thumbColor = BurgundyPrimary, activeTrackColor = BurgundyPrimary)
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = if (isAr) "الدولة:" else "Country:",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp
                    )
                    val countries = listOf("All", "United Arab Emirates", "Egypt", "Jordan", "Morocco", "Saudi Arabia")
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        countries.forEach { c ->
                            val isSelected = (c == "All" && selectedCountryFilter == null) || (selectedCountryFilter == c)
                            FilterChip(
                                selected = isSelected,
                                onClick = {
                                    selectedCountryFilter = if (c == "All") null else c
                                },
                                label = { Text(c) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = if (isAr) "الهدف من العلاقة:" else "Relationship Goal:",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp
                    )
                    RelationshipGoal.values().forEach { goal ->
                        val isSelected = selectedGoalFilter == goal
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedGoalFilter = if (isSelected) null else goal
                                }
                        ) {
                            Checkbox(
                                checked = isSelected,
                                onCheckedChange = { selectedGoalFilter = if (isSelected) null else goal }
                            )
                            Text(
                                text = if (isAr) goal.labelAr else goal.labelEn,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { showFilterSheet = false },
                    colors = ButtonDefaults.buttonColors(containerColor = BurgundyPrimary)
                ) {
                    Text(if (isAr) "تطبيق الفلاتر" else "Apply Filters")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        selectedCountryFilter = null
                        selectedGoalFilter = null
                        minAgeFilter = 18f
                        maxAgeFilter = 50f
                        showFilterSheet = false
                    }
                ) {
                    Text(if (isAr) "مسح الكل" else "Clear All")
                }
            }
        )
    }
}
