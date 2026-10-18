package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.*
import com.example.ui.components.LanguageToggle
import com.example.ui.components.WidadWordmark
import com.example.ui.theme.*

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun OnboardingScreen(
    currentLanguage: AppLanguage,
    onLanguageChange: (AppLanguage) -> Unit,
    onFinishOnboarding: (UserProfile) -> Unit
) {
    val isAr = currentLanguage == AppLanguage.ARABIC
    var step by remember { mutableIntStateOf(1) }
    val totalSteps = 4

    // State fields
    var displayName by remember { mutableStateOf("Tariq Al-Mansoor") }
    var country by remember { mutableStateOf("United Arab Emirates") }
    var city by remember { mutableStateOf("Dubai") }
    var profession by remember { mutableStateOf("Senior Architect") }
    var bio by remember { mutableStateOf("Architect and urban designer passionate about sustainable Islamic heritage. Seeking a partner who values mutual kindness, shared intellectual curiosity, and building an enduring family together.") }
    var relationshipGoal by remember { mutableStateOf(RelationshipGoal.MARRIAGE_SOON) }
    var religiousPractice by remember { mutableStateOf(ReligiousPractice.MODERATELY_PRACTICING) }

    val allInterests = listOf(
        "Architecture", "Literature", "Travel", "Arabic Calligraphy",
        "Equestrian", "Nature", "Astronomy", "Health & Wellness",
        "Gastronomy", "Design", "Volunteering", "History"
    )
    val selectedInterests = remember {
        mutableStateListOf("Architecture", "Literature", "Travel", "Arabic Calligraphy", "Nature")
    }

    val availableLanguages = listOf("Arabic", "English", "French", "German", "Spanish", "Turkish")
    val selectedLanguages = remember { mutableStateListOf("Arabic", "English") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WarmIvory)
            .statusBarsPadding()
            .padding(horizontal = 20.dp, vertical = 12.dp)
            .testTag("onboarding_screen")
    ) {
        // Top Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            WidadWordmark(showTagline = false, language = currentLanguage)
            LanguageToggle(
                currentLanguage = currentLanguage,
                onLanguageSelected = onLanguageChange
            )
        }

        // Progress Bar
        Column(modifier = Modifier.padding(vertical = 14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${if (isAr) "الخطوة " else "Step "}$step ${if (isAr) "من " else "of "}$totalSteps",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = BurgundyPrimary
                )
                Text(
                    text = when (step) {
                        1 -> if (isAr) "المعلومات الأساسية" else "Basic Background"
                        2 -> if (isAr) "الهدف والشخصية" else "Goals & Interests"
                        3 -> if (isAr) "الصورة والقيم" else "Photo & Values"
                        else -> if (isAr) "المعاينة والتأكيد" else "Pledge & Publish"
                    },
                    fontSize = 12.sp,
                    color = CharcoalMuted
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            LinearProgressIndicator(
                progress = { step.toFloat() / totalSteps.toFloat() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = BurgundyPrimary,
                trackColor = WarmIvoryVariant
            )
        }

        // Step Content inside scrollable box
        Box(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            when (step) {
                1 -> {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = if (isAr) "أخبرنا عن نفسك" else "Tell us about yourself",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = CharcoalText
                        )
                        Text(
                            text = if (isAr) "تساعد هذه المعلومات في إيجاد الشريك الملائم بتوافق حقيقي" else "This ensures sincere compatibility with prospective partners",
                            fontSize = 13.sp,
                            color = CharcoalMuted,
                            modifier = Modifier.padding(top = 2.dp, bottom = 16.dp)
                        )

                        OutlinedTextField(
                            value = displayName,
                            onValueChange = { displayName = it },
                            label = { Text(if (isAr) "الاسم الظاهر" else "Display Name") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedTextField(
                                value = country,
                                onValueChange = { country = it },
                                label = { Text(if (isAr) "الدولة" else "Country") },
                                modifier = Modifier.weight(1f)
                            )
                            OutlinedTextField(
                                value = city,
                                onValueChange = { city = it },
                                label = { Text(if (isAr) "المدينة (اختياري)" else "City (Optional)") },
                                modifier = Modifier.weight(1f)
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = profession,
                            onValueChange = { profession = it },
                            label = { Text(if (isAr) "المهنة / المجال" else "Profession / Field") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = if (isAr) "اللغات التي تتحدثها" else "Languages Spoken",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = CharcoalText
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            availableLanguages.forEach { lang ->
                                val selected = lang in selectedLanguages
                                FilterChip(
                                    selected = selected,
                                    onClick = {
                                        if (selected) selectedLanguages.remove(lang) else selectedLanguages.add(lang)
                                    },
                                    label = { Text(lang) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = BurgundyPrimary,
                                        selectedLabelColor = Color.White
                                    )
                                )
                            }
                        }
                    }
                }
                2 -> {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = if (isAr) "الهدف من التعارف والاهتمامات" else "Relationship Intent & Interests",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = CharcoalText
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = if (isAr) "الهدف الرئيسي من الارتباط:" else "Primary Intent:",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = CharcoalText
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        RelationshipGoal.values().forEach { goal ->
                            val isSelected = relationshipGoal == goal
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (isSelected) GoldContainer else WarmIvorySurface,
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isSelected) ChampagneGold else BorderSubtle
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clickable { relationshipGoal = goal }
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = isSelected,
                                        onClick = { relationshipGoal = goal },
                                        colors = RadioButtonDefaults.colors(selectedColor = BurgundyPrimary)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = if (isAr) goal.labelAr else goal.labelEn,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = CharcoalText
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = if (isAr) "نبذة تعريفية (السيرة الذاتية)" else "About Me (Bio)",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = CharcoalText
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = bio,
                            onValueChange = { bio = it },
                            minLines = 3,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = if (isAr) "اهتماماتك وهواياتك المفضلة" else "Your Interests & Passions",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = CharcoalText
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            allInterests.forEach { interest ->
                                val selected = interest in selectedInterests
                                FilterChip(
                                    selected = selected,
                                    onClick = {
                                        if (selected) selectedInterests.remove(interest) else selectedInterests.add(interest)
                                    },
                                    label = { Text(interest) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = ChampagneGold,
                                        selectedLabelColor = BurgundyDark
                                    )
                                )
                            }
                        }
                    }
                }
                3 -> {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = if (isAr) "الصورة الشخصية والقيم" else "Profile Photo & Values",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = CharcoalText,
                            modifier = Modifier.align(Alignment.Start)
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        Box(
                            modifier = Modifier
                                .size(130.dp)
                                .clip(CircleShape)
                                .border(3.dp, ChampagneGold, CircleShape)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.img_avatar_tariq),
                                contentDescription = "Profile Photo",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                        Text(
                            text = if (isAr) "صورة موثقة وواضحة (جاهزة)" else "Tasteful Portrait Configured",
                            fontSize = 12.sp,
                            color = StatusGreen,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 8.dp)
                        )

                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            text = if (isAr) "الالتزام الديني والقيمي (اختياري بالكامل):" else "Cultural / Religious Values (Optional):",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = CharcoalText,
                            modifier = Modifier.align(Alignment.Start)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        ReligiousPractice.values().forEach { practice ->
                            val isSelected = religiousPractice == practice
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (isSelected) GoldContainer else WarmIvorySurface,
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isSelected) ChampagneGold else BorderSubtle
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clickable { religiousPractice = practice }
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = isSelected,
                                        onClick = { religiousPractice = practice },
                                        colors = RadioButtonDefaults.colors(selectedColor = BurgundyPrimary)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = if (isAr) practice.labelAr else practice.labelEn,
                                        fontSize = 13.sp,
                                        color = CharcoalText
                                    )
                                }
                            }
                        }
                    }
                }
                else -> {
                    // Step 4: Pledge & Review
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = if (isAr) "ميثاق مجتمع مودة وتأكيد النشر" else "Community Pledge & Confirmation",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = CharcoalText
                        )
                        Spacer(modifier = Modifier.height(14.dp))

                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = WarmIvorySurface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.VerifiedUser,
                                        contentDescription = null,
                                        tint = BurgundyPrimary
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = if (isAr) "ميثاق الاحترام والجدية" else "Pledge of Sincerity & Respect",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        color = BurgundyPrimary
                                    )
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = if (isAr)
                                        "• أقر بأنني أبحث عن علاقة شريفة وجادة قائمة على المودة والاحترام المتبادل.\n• ألتزم بعدم طلب أو تقديم أي أموال أو معاملات تجارية أو استثمارية.\n• ألتزم بالاحترام الكامل لخصوصية الأعضاء الآخرين."
                                    else
                                        "• I commit to seeking an honorable, serious relationship rooted in mutual respect.\n• I will never solicit financial transfers, investments, or commercial promotions.\n• I honor the privacy and dignity of every prospective partner.",
                                    fontSize = 13.sp,
                                    color = CharcoalText,
                                    lineHeight = 18.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Preview Summary
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = GoldContainer,
                            border = androidx.compose.foundation.BorderStroke(1.dp, ChampagneGold)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = if (isAr) "معاينة الملف الظاهر للآخرين:" else "Profile Preview Summary:",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = BurgundyDark
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "$displayName, 29 • $city, $country",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = CharcoalText
                                )
                                Text(
                                    text = "$profession • ${if (isAr) relationshipGoal.labelAr else relationshipGoal.labelEn}",
                                    fontSize = 12.sp,
                                    color = CharcoalMuted
                                )
                                Text(
                                    text = bio,
                                    fontSize = 12.sp,
                                    color = CharcoalText,
                                    modifier = Modifier.padding(top = 4.dp),
                                    maxLines = 2
                                )
                            }
                        }
                    }
                }
            }
        }

        // Navigation Buttons at Bottom
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (step > 1) {
                OutlinedButton(
                    onClick = { step-- },
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(if (isAr) "السابق" else "Back")
                }
                Spacer(modifier = Modifier.width(12.dp))
            }

            Button(
                onClick = {
                    if (step < totalSteps) {
                        step++
                    } else {
                        val finalProfile = UserProfile(
                            id = "user_me",
                            displayName = displayName,
                            birthDate = "1996-05-14",
                            calculatedAge = 29,
                            country = country,
                            city = city,
                            languages = selectedLanguages.toList(),
                            bio = bio,
                            interests = selectedInterests.toList(),
                            relationshipGoal = relationshipGoal,
                            religiousPractice = religiousPractice,
                            profession = profession,
                            education = "Higher Education",
                            photoResId = R.drawable.img_avatar_tariq,
                            isVerified = true,
                            isPremium = false
                        )
                        onFinishOnboarding(finalProfile)
                    }
                },
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BurgundyPrimary),
                modifier = Modifier
                    .weight(1.5f)
                    .height(50.dp)
                    .testTag("onboarding_next_button")
            ) {
                Text(
                    text = if (step == totalSteps) {
                        if (isAr) "نشر الملف والبدء" else "Publish Profile & Start"
                    } else {
                        if (isAr) "التالي" else "Next"
                    },
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Color.White
                )
            }
        }
    }
}
