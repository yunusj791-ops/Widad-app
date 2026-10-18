package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.WidadRepository
import com.example.model.*
import com.example.ui.theme.*

@Composable
fun AdminDashboardScreen(
    language: AppLanguage,
    repository: WidadRepository,
    onBack: () -> Unit
) {
    val isAr = language == AppLanguage.ARABIC
    val currentUser by repository.currentUser.collectAsState()
    val profiles by repository.profiles.collectAsState()
    val matches by repository.matches.collectAsState()
    val reports by repository.safetyReports.collectAsState()
    val payments by repository.payments.collectAsState()

    var activeTab by remember { mutableIntStateOf(0) }
    var searchQuery by remember { mutableStateOf("") }

    val filteredProfiles = remember(profiles, searchQuery) {
        if (searchQuery.isBlank()) profiles
        else profiles.filter {
            it.displayName.contains(searchQuery, ignoreCase = true) ||
            it.country.contains(searchQuery, ignoreCase = true) ||
            it.city.contains(searchQuery, ignoreCase = true)
        }
    }

    val totalRevenue = remember(payments) {
        payments.filter { it.status == "success" }.sumOf { it.amountUsd }
    }
    val activePremiumCount = remember(profiles, currentUser) {
        profiles.count { it.isPremium } + (if (currentUser.isPremium) 1 else 0)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WarmIvory)
            .statusBarsPadding()
            .padding(16.dp)
            .testTag("admin_dashboard_screen")
    ) {
        // Top Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = BurgundyPrimary
                )
            }
            Spacer(modifier = Modifier.width(6.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (isAr) "لوحة إدارة وتأسيس منصة مودة" else "Founder & Admin Control Center",
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = BurgundyPrimary
                )
                Text(
                    text = if (isAr) "إدارة الأعضاء والاشتراكات وبلاغات الأمان" else "Member moderation, subscriptions & safety reports",
                    fontSize = 11.sp,
                    color = CharcoalMuted
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Executive Metrics Cards Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AdminMetricCard(
                title = if (isAr) "إجمالي الأعضاء" else "Total Users",
                value = "${profiles.size + 1}",
                color = BurgundyPrimary,
                modifier = Modifier.weight(1f)
            )
            AdminMetricCard(
                title = if (isAr) "المطابقات" else "Matches",
                value = "${matches.size}",
                color = ChampagneGoldDark,
                modifier = Modifier.weight(1f)
            )
            AdminMetricCard(
                title = if (isAr) "مشتركو التميز" else "Premium",
                value = "$activePremiumCount",
                color = BurgundyLight,
                modifier = Modifier.weight(1f)
            )
            AdminMetricCard(
                title = if (isAr) "الإيرادات" else "Revenue",
                value = "$${String.format("%.0f", totalRevenue)}",
                color = StatusGreen,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Tab Row
        TabRow(
            selectedTabIndex = activeTab,
            containerColor = WarmIvorySurface,
            contentColor = BurgundyPrimary
        ) {
            Tab(
                selected = activeTab == 0,
                onClick = { activeTab = 0 },
                text = { Text(if (isAr) "الأعضاء" else "Users", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
            )
            Tab(
                selected = activeTab == 1,
                onClick = { activeTab = 1 },
                text = {
                    Text(
                        text = "${if (isAr) "البلاغات (" else "Reports ("}${reports.count { it.status == "OPEN" }})",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            )
            Tab(
                selected = activeTab == 2,
                onClick = { activeTab = 2 },
                text = { Text(if (isAr) "المدفوعات" else "Payments", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Tab Content
        when (activeTab) {
            0 -> {
                // Users Management
                Column(modifier = Modifier.weight(1f)) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text(if (isAr) "بحث باسم العضو أو الدولة..." else "Search by name or country...") },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        items(filteredProfiles) { prof ->
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = WarmIvorySurface,
                                border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(
                                                text = "${prof.displayName}, ${prof.calculatedAge}",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 14.sp
                                            )
                                            if (prof.isSuspended) {
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Surface(
                                                    shape = RoundedCornerShape(4.dp),
                                                    color = StatusRedContainer
                                                ) {
                                                    Text(
                                                        text = if (isAr) "موقوف" else "SUSPENDED",
                                                        fontSize = 10.sp,
                                                        color = StatusRed,
                                                        fontWeight = FontWeight.Bold,
                                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                                    )
                                                }
                                            }
                                        }
                                        Text(
                                            text = "${prof.city}, ${prof.country} • ${prof.profession}",
                                            fontSize = 11.sp,
                                            color = CharcoalMuted
                                        )
                                    }
                                    if (prof.isSuspended) {
                                        TextButton(onClick = { repository.reinstateUser(prof.id) }) {
                                            Text(if (isAr) "إلغاء التوقيف" else "Reinstate", color = StatusGreen, fontSize = 11.sp)
                                        }
                                    } else {
                                        TextButton(onClick = { repository.suspendUser(prof.id) }) {
                                            Text(if (isAr) "توقيف" else "Suspend", color = StatusRed, fontSize = 11.sp)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            1 -> {
                // Safety Reports Moderation Queue
                Column(modifier = Modifier.weight(1f)) {
                    if (reports.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(
                                text = if (isAr) "لا توجد بلاغات معلقة حالياً. مجتمع مودة آمن." else "No pending reports. Community is healthy.",
                                color = CharcoalMuted,
                                fontSize = 13.sp
                            )
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(reports) { rep ->
                                Card(
                                    shape = RoundedCornerShape(14.dp),
                                    colors = CardDefaults.cardColors(containerColor = WarmIvorySurface),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(14.dp)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = "${if (isAr) "بلاغ ضد: " else "Target: "}${rep.reportedUserName}",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 14.sp,
                                                color = CharcoalText
                                            )
                                            Surface(
                                                shape = RoundedCornerShape(6.dp),
                                                color = if (rep.status == "OPEN") StatusRedContainer else StatusGreenContainer
                                            ) {
                                                Text(
                                                    text = rep.status,
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = if (rep.status == "OPEN") StatusRed else StatusGreen,
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                )
                                            }
                                        }
                                        Text(
                                            text = "${if (isAr) "السبب: " else "Reason: "}${rep.reason}",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = BurgundyPrimary,
                                            modifier = Modifier.padding(top = 4.dp)
                                        )
                                        Text(
                                            text = rep.details.ifBlank { "No additional details provided." },
                                            fontSize = 11.sp,
                                            color = CharcoalMuted,
                                            modifier = Modifier.padding(top = 2.dp)
                                        )

                                        if (rep.status == "OPEN") {
                                            Spacer(modifier = Modifier.height(10.dp))
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.End
                                            ) {
                                                OutlinedButton(
                                                    onClick = { repository.resolveReport(rep.id, "DISMISSED") },
                                                    shape = RoundedCornerShape(8.dp)
                                                ) {
                                                    Text(if (isAr) "تجاهل" else "Dismiss", fontSize = 11.sp)
                                                }
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Button(
                                                    onClick = {
                                                        repository.suspendUser(rep.reportedUserId)
                                                        repository.resolveReport(rep.id, "ACTION_TAKEN")
                                                    },
                                                    colors = ButtonDefaults.buttonColors(containerColor = StatusRed),
                                                    shape = RoundedCornerShape(8.dp)
                                                ) {
                                                    Text(if (isAr) "توقيف الحساب" else "Suspend Account", fontSize = 11.sp)
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            else -> {
                // Paystack Payment Ledger
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (isAr) "سجل معاملات Paystack المباشر" else "Live Paystack Transactions Ledger",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = CharcoalText,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )

                    if (payments.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(
                                text = if (isAr) "لا توجد معاملات بعد." else "No processed payment events yet.",
                                color = CharcoalMuted,
                                fontSize = 13.sp
                            )
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            items(payments) { p ->
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = WarmIvorySurface,
                                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(
                                                text = p.reference,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 12.sp,
                                                color = BurgundyPrimary
                                            )
                                            Text(
                                                text = "${p.currency} ${p.amountUsd}",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 13.sp
                                            )
                                        }
                                        Text(
                                            text = "Customer: ${p.customerEmail} • Plan: ${p.planId}",
                                            fontSize = 11.sp,
                                            color = CharcoalMuted
                                        )
                                        Text(
                                            text = "Auth Code: ${p.authorizationCode} • Status: ${p.status}",
                                            fontSize = 10.sp,
                                            color = if (p.status == "success") StatusGreen else StatusRed
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AdminMetricCard(
    title: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = WarmIvorySurface,
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Text(
                text = title,
                fontSize = 10.sp,
                color = CharcoalMuted,
                maxLines = 1
            )
        }
    }
}
