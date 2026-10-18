package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import android.content.Intent
import android.net.Uri
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.AppLanguage
import com.example.ui.components.DisclaimerNote
import com.example.ui.components.LanguageToggle
import com.example.ui.components.WidadWordmark
import com.example.ui.theme.*

data class FaqItem(val questionEn: String, val questionAr: String, val answerEn: String, val answerAr: String)

@Composable
fun LandingScreen(
    language: AppLanguage,
    onLanguageChange: (AppLanguage) -> Unit,
    onJoinFreeClick: () -> Unit,
    onSignInClick: () -> Unit,
    onExploreMembershipClick: () -> Unit,
    onViewSafetyClick: () -> Unit,
    onViewTermsClick: () -> Unit,
    onViewPrivacyClick: () -> Unit
) {
    val isAr = language == AppLanguage.ARABIC
    val context = LocalContext.current

    val faqs = listOf(
        FaqItem(
            questionEn = "How is Widad different from casual dating apps?",
            questionAr = "كيف يختلف تطبيق مودة عن تطبيقات التعارف العشوائية؟",
            answerEn = "Widad is strictly designed for adults seeking serious relationships, courtship, and marriage. We reject casual hookup culture in favor of respectful profile depth, shared values, and mutual family-oriented goals.",
            answerAr = "صُمم تطبيق مودة خصيصاً للبالغين الراغبين في علاقات جادة والزواج والاستقرار الأسري، بعيداً عن السطحية والعبث، وبتركيز عالٍ على التوافق القيمي والاحترام."
        ),
        FaqItem(
            questionEn = "Is there an age requirement?",
            questionAr = "هل يوجد حد أدنى للسن؟",
            answerEn = "Yes. Widad requires all users to be at least 18 years old. We strictly verify date of birth during registration to protect our community.",
            answerAr = "نعم، يشترط أن يكون عمر المستخدم ١٨ عاماً فأكثر. نتحقق بدقة من تاريخ الميلاد لمنع الحسابات دون السن القانوني."
        ),
        FaqItem(
            questionEn = "Are cultural and religious preferences mandatory?",
            questionAr = "هل التفضيلات الدينية والثقافية إلزامية؟",
            answerEn = "No, they are completely voluntary and user-controlled. We recognize that Arab individuals and communities hold diverse backgrounds and expectations.",
            answerAr = "لا، فهي اختيارية بالكامل وتحت تحكمك. ندرك التنوع الثقافي والاجتماعي الواسع في المجتمعات العربية."
        ),
        FaqItem(
            questionEn = "How do mutual matches and messaging work?",
            questionAr = "كيف تعمل المطابقة المتبادلة والرسائل؟",
            answerEn = "Direct messaging is unlocked only when both members express mutual interest (like). This protects members from unsolicited messages and ensures mutual dignity.",
            answerAr = "تُفتح نافذة المحادثة الخاصة فقط عند وجود إعجاب متبادل بين الطرفين، مما يضمن كرامة التواصل والحماية من الإزعاج."
        )
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(WarmIvory)
            .testTag("landing_screen"),
        contentPadding = PaddingValues(bottom = 48.dp)
    ) {
        // Top Navigation Bar
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                WidadWordmark(
                    isDark = false,
                    showTagline = false,
                    language = language
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Button(
                        onClick = {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://paystack.shop/pay/q0bp-vzftt")).apply {
                                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            }
                            context.startActivity(intent)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = BurgundyPrimary,
                            contentColor = ChampagneGoldLight
                        ),
                        shape = RoundedCornerShape(16.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                        modifier = Modifier.testTag("landing_upgrade_pro_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Stars,
                            contentDescription = null,
                            tint = ChampagneGoldLight,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isAr) "ترقية للتميز" else "Upgrade to Pro",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    LanguageToggle(
                        currentLanguage = language,
                        onLanguageSelected = onLanguageChange
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    TextButton(
                        onClick = onSignInClick,
                        modifier = Modifier.testTag("signin_nav_button")
                    ) {
                        Text(
                            text = if (isAr) "تسجيل الدخول" else "Sign In",
                            color = BurgundyPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Hero Banner with Image & Tagline
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(24.dp))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_hero_banner),
                    contentDescription = "Widad Hero Banner",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                // Gradient overlay
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, BurgundyDark.copy(alpha = 0.85f))
                            )
                        )
                )
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(20.dp)
                ) {
                    Text(
                        text = if (isAr) "مودة ورحمة" else "WIDAD (مودة)",
                        color = ChampagneGoldLight,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    )
                    Text(
                        text = if (isAr) "حب ذو معنى.\nومستقبل يدوم." else "Meaningful Love.\nA Lasting Future.",
                        color = Color.White,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 32.sp
                    )
                    Text(
                        text = if (isAr) "منصة التعارف والزواج الجاد للبالغين العرب حول العالم" else "The premium relationship platform for Arab adults seeking commitment & marriage",
                        color = Color(0xFFF3ECE0),
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }

        // Call-to-Action Buttons
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 20.dp)
            ) {
                Button(
                    onClick = onJoinFreeClick,
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BurgundyPrimary),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .testTag("join_free_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = null,
                        tint = ChampagneGoldLight
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isAr) "انضم مجاناً وابدأ رحلتك" else "Join Free & Begin Journey",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedButton(
                    onClick = onExploreMembershipClick,
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, ChampagneGold),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("explore_membership_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Stars,
                        contentDescription = null,
                        tint = ChampagneGoldDark
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isAr) "استكشف باقات العضوية المتميزة" else "Explore Premium Membership",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = CharcoalText
                    )
                }
            }
        }

        // 3-Step "How It Works" Section
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = WarmIvorySurface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = if (isAr) "كيف تعمل منصة مودة؟" else "How Widad Works",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = BurgundyPrimary
                    )
                    Text(
                        text = if (isAr) "ثلاث خطوات بسيطة ومحترمة نحو شريك العمر" else "Three intentional, respectful steps toward lasting partnership",
                        fontSize = 13.sp,
                        color = CharcoalMuted,
                        modifier = Modifier.padding(top = 2.dp, bottom = 16.dp)
                    )

                    StepRow(
                        number = "١" ,
                        numEn = "1",
                        titleEn = "Create a Verified Profile",
                        titleAr = "ملف تعريفي موثق وحقيقي",
                        descEn = "Age 18+ verified. Express your authentic background, life goals, and values.",
                        descAr = "التحقق من سن الرشد (١٨+)، وإبراز أهدافك وقيمك وشخصيتك بصدق.",
                        isAr = isAr
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    StepRow(
                        number = "٢",
                        numEn = "2",
                        titleEn = "Discover Mutual Compatibility",
                        titleAr = "اكتشاف التوافق المتبادل",
                        descEn = "Filter by country, language, and relationship horizon with real compatibility insight.",
                        descAr = "فلاتر ذكية حسب الدولة واللغات والأهداف الحياتية ونسبة التوافق المشترك.",
                        isAr = isAr
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    StepRow(
                        number = "٣",
                        numEn = "3",
                        titleEn = "Direct Respectful Connection",
                        titleAr = "تواصل محترم وآمن",
                        descEn = "Chat unlocks only upon mutual match. Built-in blocking and safety moderation.",
                        descAr = "تبدأ المحادثات فقط عند الإعجاب المتبادل مع حماية الخصوصية ومراقبة الأمان.",
                        isAr = isAr
                    )
                }
            }
        }

        // Safety & Trust Assurance
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = BurgundyContainer),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(BurgundyPrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = null,
                            tint = ChampagneGoldLight,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (isAr) "أمانك وكرامتك أولاً" else "Safety & Dignity First",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = OnBurgundyContainer
                        )
                        Text(
                            text = if (isAr) "لا حسابات وهمية، حظر فوري للإساءة، وتشفير لبياناتك الخاصة." else "Zero tolerance for fake accounts, instant abuse reporting, and strict privacy.",
                            fontSize = 12.sp,
                            color = CharcoalMuted
                        )
                    }
                    IconButton(onClick = onViewSafetyClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "View Safety",
                            tint = BurgundyPrimary
                        )
                    }
                }
            }
        }

        // FAQ Section
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Text(
                    text = if (isAr) "الأسئلة الشائعة" else "Frequently Asked Questions",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = CharcoalText,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                faqs.forEach { faq ->
                    FaqAccordionItem(faq = faq, isAr = isAr)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }

        // Footer & Legal links
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp, bottom = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onViewTermsClick) {
                        Text(
                            text = if (isAr) "شروط الاستخدام" else "Terms of Service",
                            fontSize = 12.sp,
                            color = CharcoalMuted
                        )
                    }
                    Text("•", color = CharcoalMuted, fontSize = 12.sp)
                    TextButton(onClick = onViewPrivacyClick) {
                        Text(
                            text = if (isAr) "سياسة الخصوصية" else "Privacy Policy",
                            fontSize = 12.sp,
                            color = CharcoalMuted
                        )
                    }
                    Text("•", color = CharcoalMuted, fontSize = 12.sp)
                    TextButton(onClick = onViewSafetyClick) {
                        Text(
                            text = if (isAr) "إرشادات المجتمع" else "Guidelines",
                            fontSize = 12.sp,
                            color = CharcoalMuted
                        )
                    }
                }
                Text(
                    text = if (isAr) "جميع الحقوق محفوظة لمنصة مودة © 2026" else "© 2026 Widad Platform. All rights reserved.",
                    fontSize = 11.sp,
                    color = CharcoalMuted.copy(alpha = 0.8f),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

@Composable
fun StepRow(
    number: String,
    numEn: String,
    titleEn: String,
    titleAr: String,
    descEn: String,
    descAr: String,
    isAr: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(GoldContainer)
                .border(1.dp, ChampagneGold, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (isAr) number else numEn,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = BurgundyPrimary
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = if (isAr) titleAr else titleEn,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = CharcoalText
            )
            Text(
                text = if (isAr) descAr else descEn,
                fontSize = 12.sp,
                color = CharcoalMuted,
                lineHeight = 16.sp
            )
        }
    }
}

@Composable
fun FaqAccordionItem(faq: FaqItem, isAr: Boolean) {
    var expanded by remember { mutableStateOf(false) }

    Surface(
        shape = RoundedCornerShape(14.dp),
        color = WarmIvorySurface,
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded }
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = if (isAr) faq.questionAr else faq.questionEn,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = CharcoalText,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = BurgundyPrimary
                )
            }
            AnimatedVisibility(visible = expanded) {
                Text(
                    text = if (isAr) faq.answerAr else faq.answerEn,
                    fontSize = 13.sp,
                    color = CharcoalMuted,
                    lineHeight = 18.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}
