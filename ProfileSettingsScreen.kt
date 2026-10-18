package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.example.data.WidadRepository
import com.example.model.*
import com.example.ui.components.LanguageToggle
import com.example.ui.theme.*

@Composable
fun ProfileSettingsScreen(
    language: AppLanguage,
    repository: WidadRepository,
    onLanguageChange: (AppLanguage) -> Unit,
    onSignOut: () -> Unit,
    onViewAdmin: () -> Unit
) {
    val isAr = language == AppLanguage.ARABIC
    val currentUser by repository.currentUser.collectAsState()

    var isEditing by remember { mutableStateOf(false) }
    var editBio by remember { mutableStateOf(currentUser.bio) }
    var editCity by remember { mutableStateOf(currentUser.city) }
    var editProfession by remember { mutableStateOf(currentUser.profession) }
    var hideProfile by remember { mutableStateOf(currentUser.isProfileHidden) }
    var showDeleteConfirm by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WarmIvory)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .testTag("profile_settings_screen")
    ) {
        // Top Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (isAr) "الملف الشخصي والإعدادات" else "My Profile & Settings",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = BurgundyPrimary
            )
            LanguageToggle(
                currentLanguage = language,
                onLanguageSelected = onLanguageChange
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Profile Avatar Card
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = WarmIvorySurface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(96.dp)
                        .clip(CircleShape)
                        .border(3.dp, ChampagneGold, CircleShape)
                ) {
                    Image(
                        painter = painterResource(id = currentUser.photoResId ?: R.drawable.img_avatar_tariq),
                        contentDescription = currentUser.displayName,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${currentUser.displayName}, ${currentUser.calculatedAge}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = CharcoalText
                    )
                    if (currentUser.isVerified) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = "Verified",
                            tint = ChampagneGoldDark,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
                Text(
                    text = "${currentUser.city}, ${currentUser.country} • ${currentUser.profession}",
                    fontSize = 12.sp,
                    color = CharcoalMuted
                )

                if (currentUser.isPremium) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = GoldContainer,
                        border = androidx.compose.foundation.BorderStroke(1.dp, ChampagneGold)
                    ) {
                        Text(
                            text = if (isAr) "★ عضوية التميز نشطة" else "★ Premium Member",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = BurgundyDark,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Editable Bio & Information
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = WarmIvorySurface),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isAr) "البيانات الشخصية" else "Personal Details",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = BurgundyPrimary
                    )
                    TextButton(onClick = {
                        if (isEditing) {
                            repository.updateCurrentUser(
                                currentUser.copy(
                                    bio = editBio,
                                    city = editCity,
                                    profession = editProfession,
                                    isProfileHidden = hideProfile
                                )
                            )
                        }
                        isEditing = !isEditing
                    }) {
                        Text(
                            text = if (isEditing) (if (isAr) "حفظ التغييرات" else "Save") else (if (isAr) "تعديل" else "Edit"),
                            fontWeight = FontWeight.Bold,
                            color = BurgundyPrimary
                        )
                    }
                }

                if (isEditing) {
                    OutlinedTextField(
                        value = editProfession,
                        onValueChange = { editProfession = it },
                        label = { Text(if (isAr) "المهنة" else "Profession") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = editCity,
                        onValueChange = { editCity = it },
                        label = { Text(if (isAr) "المدينة" else "City") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = editBio,
                        onValueChange = { editBio = it },
                        label = { Text(if (isAr) "النبذة التعريفية" else "Bio") },
                        minLines = 3,
                        modifier = Modifier.fillMaxWidth()
                    )
                } else {
                    Text(
                        text = currentUser.bio,
                        fontSize = 13.sp,
                        color = CharcoalText,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Privacy Controls
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = WarmIvorySurface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = if (isAr) "إعدادات الخصوصية والظهور" else "Privacy & Visibility",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = BurgundyPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (isAr) "إخفاء الملف مؤقتاً" else "Pause / Hide Profile",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = CharcoalText
                        )
                        Text(
                            text = if (isAr) "لن يظهر ملفك للمشتركين الجدد في الاستكشاف" else "Your profile will not be discovered by new members",
                            fontSize = 11.sp,
                            color = CharcoalMuted
                        )
                    }
                    Switch(
                        checked = hideProfile,
                        onCheckedChange = {
                            hideProfile = it
                            repository.updateCurrentUser(currentUser.copy(isProfileHidden = it))
                        },
                        colors = SwitchDefaults.colors(checkedThumbColor = BurgundyPrimary)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Admin & Founder shortcut
        OutlinedButton(
            onClick = onViewAdmin,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.AdminPanelSettings, contentDescription = null, tint = ChampagneGoldDark)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (isAr) "فتح لوحة إدارة المنصة والمراقبة" else "Founder & Admin Control Center",
                fontWeight = FontWeight.Bold,
                color = CharcoalText
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Sign Out Button
        OutlinedButton(
            onClick = onSignOut,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = CharcoalMuted)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (isAr) "تسجيل الخروج" else "Sign Out",
                color = CharcoalText
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Delete Account
        TextButton(
            onClick = { showDeleteConfirm = true },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(
                text = if (isAr) "حذف الحساب والبيانات نهائياً" else "Delete Account & All Data",
                color = StatusRed,
                fontSize = 12.sp
            )
        }
    }

    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text(if (isAr) "تأكيد حذف الحساب" else "Confirm Account Deletion") },
            text = {
                Text(
                    if (isAr) "هل أنت متأكد؟ سيتم مسح ملفك ومطابقاتك ورسائلك بالكامل ولن تتمكن من استرجاعها."
                    else "Are you sure? Your entire profile, matches, messages, and photos will be permanently deleted."
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteConfirm = false
                        onSignOut()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = StatusRed)
                ) {
                    Text(if (isAr) "نعم، احذف حسابي" else "Yes, Delete My Account")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = false }) {
                    Text(if (isAr) "إلغاء" else "Cancel")
                }
            }
        )
    }
}
