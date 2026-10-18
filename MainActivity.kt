package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import android.content.Intent
import android.net.Uri
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.WidadRepository
import com.example.model.AppLanguage
import com.example.ui.components.LanguageToggle
import com.example.ui.components.NotificationsDialog
import com.example.ui.components.WidadWordmark
import com.example.ui.screens.*
import com.example.ui.theme.*

enum class AppDestination {
    LANDING,
    AUTH,
    ONBOARDING,
    MAIN_APP,
    ADMIN,
    LEGAL
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val repository = WidadRepository.instance

        setContent {
            val context = LocalContext.current
            MyApplicationTheme {
                val language by repository.appLanguage.collectAsState()
                val notifications by repository.notifications.collectAsState()
                val isAr = language == AppLanguage.ARABIC

                val layoutDirection = if (isAr) LayoutDirection.Rtl else LayoutDirection.Ltr

                CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
                    var currentDestination by remember { mutableStateOf(AppDestination.LANDING) }
                    var currentMainTab by remember { mutableIntStateOf(0) }
                    var selectedConversationId by remember { mutableStateOf<String?>(null) }
                    var legalViewType by remember { mutableStateOf(LegalViewType.SAFETY) }
                    var showNotificationsDialog by remember { mutableStateOf(false) }

                    // Predictive Back Handling
                    BackHandler(enabled = currentDestination != AppDestination.LANDING) {
                        when (currentDestination) {
                            AppDestination.AUTH, AppDestination.LEGAL -> currentDestination = AppDestination.LANDING
                            AppDestination.ONBOARDING -> currentDestination = AppDestination.AUTH
                            AppDestination.ADMIN -> currentDestination = AppDestination.MAIN_APP
                            AppDestination.MAIN_APP -> {
                                if (selectedConversationId != null) {
                                    selectedConversationId = null
                                } else if (currentMainTab != 0) {
                                    currentMainTab = 0
                                } else {
                                    currentDestination = AppDestination.LANDING
                                }
                            }
                            else -> {}
                        }
                    }

                    Scaffold(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(WarmIvory),
                        topBar = {
                            if (currentDestination == AppDestination.MAIN_APP && selectedConversationId == null) {
                                Surface(
                                    color = WarmIvorySurface,
                                    tonalElevation = 2.dp,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .statusBarsPadding()
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 16.dp, vertical = 8.dp),
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
                                                modifier = Modifier.testTag("header_upgrade_to_pro_button")
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
                                                onLanguageSelected = { repository.setLanguage(it) }
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            IconButton(
                                                onClick = { showNotificationsDialog = true },
                                                modifier = Modifier.testTag("notifications_button")
                                            ) {
                                                BadgedBox(
                                                    badge = {
                                                        if (notifications.any { !it.isRead }) {
                                                            Badge(containerColor = BurgundyPrimary) {
                                                                Text("${notifications.count { !it.isRead }}")
                                                            }
                                                        }
                                                    }
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Outlined.Notifications,
                                                        contentDescription = "Notifications",
                                                        tint = CharcoalText
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        },
                        bottomBar = {
                            if (currentDestination == AppDestination.MAIN_APP && selectedConversationId == null) {
                                NavigationBar(
                                    containerColor = WarmIvorySurface,
                                    modifier = Modifier
                                        .navigationBarsPadding()
                                        .testTag("main_bottom_nav")
                                ) {
                                    NavigationBarItem(
                                        selected = currentMainTab == 0,
                                        onClick = { currentMainTab = 0 },
                                        icon = {
                                            Icon(
                                                imageVector = if (currentMainTab == 0) Icons.Default.Explore else Icons.Outlined.Explore,
                                                contentDescription = "Discover"
                                            )
                                        },
                                        label = {
                                            Text(
                                                text = if (isAr) "استكشاف" else "Discover",
                                                fontWeight = if (currentMainTab == 0) FontWeight.Bold else FontWeight.Normal
                                            )
                                        },
                                        colors = NavigationBarItemDefaults.colors(
                                            selectedIconColor = BurgundyPrimary,
                                            indicatorColor = GoldContainer
                                        )
                                    )
                                    NavigationBarItem(
                                        selected = currentMainTab == 1,
                                        onClick = { currentMainTab = 1 },
                                        icon = {
                                            Icon(
                                                imageVector = if (currentMainTab == 1) Icons.Default.ChatBubble else Icons.Outlined.ChatBubbleOutline,
                                                contentDescription = "Matches & Chat"
                                            )
                                        },
                                        label = {
                                            Text(
                                                text = if (isAr) "المحادثات" else "Matches",
                                                fontWeight = if (currentMainTab == 1) FontWeight.Bold else FontWeight.Normal
                                            )
                                        },
                                        colors = NavigationBarItemDefaults.colors(
                                            selectedIconColor = BurgundyPrimary,
                                            indicatorColor = GoldContainer
                                        )
                                    )
                                    NavigationBarItem(
                                        selected = currentMainTab == 2,
                                        onClick = { currentMainTab = 2 },
                                        icon = {
                                            Icon(
                                                imageVector = if (currentMainTab == 2) Icons.Default.Stars else Icons.Outlined.Stars,
                                                contentDescription = "Membership"
                                            )
                                        },
                                        label = {
                                            Text(
                                                text = if (isAr) "العضوية" else "Membership",
                                                fontWeight = if (currentMainTab == 2) FontWeight.Bold else FontWeight.Normal
                                            )
                                        },
                                        colors = NavigationBarItemDefaults.colors(
                                            selectedIconColor = BurgundyPrimary,
                                            indicatorColor = GoldContainer
                                        )
                                    )
                                    NavigationBarItem(
                                        selected = currentMainTab == 3,
                                        onClick = { currentMainTab = 3 },
                                        icon = {
                                            Icon(
                                                imageVector = if (currentMainTab == 3) Icons.Default.Person else Icons.Outlined.Person,
                                                contentDescription = "Profile"
                                            )
                                        },
                                        label = {
                                            Text(
                                                text = if (isAr) "ملفي" else "Profile",
                                                fontWeight = if (currentMainTab == 3) FontWeight.Bold else FontWeight.Normal
                                            )
                                        },
                                        colors = NavigationBarItemDefaults.colors(
                                            selectedIconColor = BurgundyPrimary,
                                            indicatorColor = GoldContainer
                                        )
                                    )
                                }
                            }
                        }
                    ) { paddingValues ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(paddingValues)
                        ) {
                            when (currentDestination) {
                                AppDestination.LANDING -> {
                                    LandingScreen(
                                        language = language,
                                        onLanguageChange = { repository.setLanguage(it) },
                                        onJoinFreeClick = { currentDestination = AppDestination.AUTH },
                                        onSignInClick = { currentDestination = AppDestination.AUTH },
                                        onExploreMembershipClick = {
                                            currentDestination = AppDestination.MAIN_APP
                                            currentMainTab = 2
                                        },
                                        onViewSafetyClick = {
                                            legalViewType = LegalViewType.SAFETY
                                            currentDestination = AppDestination.LEGAL
                                        },
                                        onViewTermsClick = {
                                            legalViewType = LegalViewType.TERMS
                                            currentDestination = AppDestination.LEGAL
                                        },
                                        onViewPrivacyClick = {
                                            legalViewType = LegalViewType.PRIVACY
                                            currentDestination = AppDestination.LEGAL
                                        }
                                    )
                                }
                                AppDestination.AUTH -> {
                                    AuthScreen(
                                        language = language,
                                        onAuthSuccess = { isNewUser ->
                                            if (isNewUser) {
                                                currentDestination = AppDestination.ONBOARDING
                                            } else {
                                                currentDestination = AppDestination.MAIN_APP
                                            }
                                        },
                                        onBackToLanding = { currentDestination = AppDestination.LANDING },
                                        onAdminLogin = { currentDestination = AppDestination.ADMIN }
                                    )
                                }
                                AppDestination.ONBOARDING -> {
                                    OnboardingScreen(
                                        currentLanguage = language,
                                        onLanguageChange = { repository.setLanguage(it) },
                                        onFinishOnboarding = { profile ->
                                            repository.updateCurrentUser(profile)
                                            currentDestination = AppDestination.MAIN_APP
                                        }
                                    )
                                }
                                AppDestination.MAIN_APP -> {
                                    when (currentMainTab) {
                                        0 -> {
                                            DiscoveryScreen(
                                                language = language,
                                                repository = repository,
                                                onNavigateToChat = { conversationId ->
                                                    selectedConversationId = conversationId
                                                    currentMainTab = 1
                                                },
                                                onNavigateToUpgrade = { currentMainTab = 2 }
                                            )
                                        }
                                        1 -> {
                                            MatchesAndChatScreen(
                                                language = language,
                                                repository = repository,
                                                selectedConversationId = selectedConversationId,
                                                onSelectConversation = { selectedConversationId = it }
                                            )
                                        }
                                        2 -> {
                                            MembershipScreen(
                                                language = language,
                                                repository = repository
                                            )
                                        }
                                        3 -> {
                                            ProfileSettingsScreen(
                                                language = language,
                                                repository = repository,
                                                onLanguageChange = { repository.setLanguage(it) },
                                                onSignOut = { currentDestination = AppDestination.LANDING },
                                                onViewAdmin = { currentDestination = AppDestination.ADMIN }
                                            )
                                        }
                                    }
                                }
                                AppDestination.ADMIN -> {
                                    AdminDashboardScreen(
                                        language = language,
                                        repository = repository,
                                        onBack = { currentDestination = AppDestination.MAIN_APP }
                                    )
                                }
                                AppDestination.LEGAL -> {
                                    SafetyAndLegalScreen(
                                        language = language,
                                        viewType = legalViewType,
                                        onBack = { currentDestination = AppDestination.LANDING }
                                    )
                                }
                            }

                            if (showNotificationsDialog) {
                                NotificationsDialog(
                                    language = language,
                                    notifications = notifications,
                                    onDismiss = { showNotificationsDialog = false }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
