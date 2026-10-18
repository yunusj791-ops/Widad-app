package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
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
import com.example.ui.theme.*

@Composable
fun MatchesAndChatScreen(
    language: AppLanguage,
    repository: WidadRepository,
    selectedConversationId: String?,
    onSelectConversation: (String?) -> Unit
) {
    val isAr = language == AppLanguage.ARABIC
    val currentUser by repository.currentUser.collectAsState()
    val matches by repository.matches.collectAsState()
    val profiles by repository.profiles.collectAsState()
    val allMessages by repository.messages.collectAsState()

    var showReportDialog by remember { mutableStateOf(false) }
    var reportReason by remember { mutableStateOf("Inappropriate behavior") }
    var reportDetails by remember { mutableStateOf("") }
    var showBlockConfirmation by remember { mutableStateOf(false) }

    if (selectedConversationId != null) {
        // Active Chat View
        val match = matches.find { it.id == selectedConversationId }
        val partnerId = if (match?.user1Id == currentUser.id) match.user2Id else match?.user1Id ?: "user_layla"
        val partnerProfile = profiles.find { it.id == partnerId } ?: UserProfile(
            id = partnerId,
            displayName = "Layla Al-Hashimi",
            birthDate = "1997-09-22",
            calculatedAge = 28,
            country = "United Arab Emirates",
            city = "Abu Dhabi",
            languages = listOf("Arabic", "English"),
            bio = "Literature educator",
            interests = listOf("Literature"),
            relationshipGoal = RelationshipGoal.MARRIAGE_SOON,
            profession = "Lecturer",
            education = "Ph.D.",
            photoResId = R.drawable.img_avatar_layla
        )

        val conversationMessages = allMessages[selectedConversationId] ?: emptyList()
        var messageInput by remember { mutableStateOf("") }
        val listState = rememberLazyListState()

        LaunchedEffect(conversationMessages.size) {
            if (conversationMessages.isNotEmpty()) {
                listState.animateScrollToItem(conversationMessages.size - 1)
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(WarmIvory)
                .statusBarsPadding()
                .testTag("chat_room_screen")
        ) {
            // Chat Top Bar
            Surface(
                color = WarmIvorySurface,
                tonalElevation = 2.dp,
                shadowElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { onSelectConversation(null) },
                        modifier = Modifier.testTag("chat_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = BurgundyPrimary
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .border(1.5.dp, ChampagneGold, CircleShape)
                    ) {
                        Image(
                            painter = painterResource(id = partnerProfile.photoResId ?: R.drawable.img_avatar_layla),
                            contentDescription = partnerProfile.displayName,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = partnerProfile.displayName,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = CharcoalText
                            )
                            if (partnerProfile.isVerified) {
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = Icons.Default.Verified,
                                    contentDescription = "Verified",
                                    tint = ChampagneGoldDark,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                        Text(
                            text = "${partnerProfile.city}, ${partnerProfile.country}",
                            fontSize = 11.sp,
                            color = CharcoalMuted
                        )
                    }

                    // Options menu: Unmatch, Block, Report
                    var menuExpanded by remember { mutableStateOf(false) }
                    Box {
                        IconButton(onClick = { menuExpanded = true }) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "Options",
                                tint = CharcoalMuted
                            )
                        }
                        DropdownMenu(
                            expanded = menuExpanded,
                            onDismissRequest = { menuExpanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text(if (isAr) "إلغاء التوافق" else "Unmatch") },
                                onClick = {
                                    menuExpanded = false
                                    repository.unmatch(selectedConversationId, partnerId)
                                    onSelectConversation(null)
                                },
                                leadingIcon = {
                                    Icon(Icons.Default.HeartBroken, contentDescription = null, tint = CharcoalMuted)
                                }
                            )
                            DropdownMenuItem(
                                text = { Text(if (isAr) "حظر المستخدم" else "Block User", color = StatusRed) },
                                onClick = {
                                    menuExpanded = false
                                    showBlockConfirmation = true
                                },
                                leadingIcon = {
                                    Icon(Icons.Default.Block, contentDescription = null, tint = StatusRed)
                                }
                            )
                            DropdownMenuItem(
                                text = { Text(if (isAr) "إبلاغ عن محادثة" else "Report Conversation", color = StatusRed) },
                                onClick = {
                                    menuExpanded = false
                                    showReportDialog = true
                                },
                                leadingIcon = {
                                    Icon(Icons.Default.ReportProblem, contentDescription = null, tint = StatusRed)
                                }
                            )
                        }
                    }
                }
            }

            // Safety Warning Banner inside Chat
            Surface(
                color = BurgundyContainer.copy(alpha = 0.8f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = null,
                        tint = BurgundyPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isAr) "لحمايتك: لا تحول أموالاً أبداً ولا تشارك بياناتك البنكية. حافظ على التواصل المحترم داخل المنصة." else "For your security: never send money or share bank details. Keep initial conversations respectful on Widad.",
                        fontSize = 11.sp,
                        color = OnBurgundyContainer,
                        lineHeight = 15.sp
                    )
                }
            }

            // Messages List
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(conversationMessages) { msg ->
                    val isMe = msg.senderId == currentUser.id
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = if (isMe) Alignment.End else Alignment.Start
                    ) {
                        Surface(
                            shape = RoundedCornerShape(
                                topStart = 16.dp,
                                topEnd = 16.dp,
                                bottomStart = if (isMe) 16.dp else 4.dp,
                                bottomEnd = if (isMe) 4.dp else 16.dp
                            ),
                            color = if (isMe) BurgundyPrimary else WarmIvorySurface,
                            border = if (!isMe) androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle) else null,
                            shadowElevation = 1.dp
                        ) {
                            Text(
                                text = msg.text,
                                color = if (isMe) Color.White else CharcoalText,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                            )
                        }
                        Text(
                            text = msg.timeFormatted,
                            fontSize = 10.sp,
                            color = CharcoalMuted,
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            // Input Row
            Surface(
                color = WarmIvorySurface,
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = messageInput,
                        onValueChange = { messageInput = it },
                        placeholder = {
                            Text(
                                text = if (isAr) "اكتب رسالة محترمة..." else "Type a respectful message...",
                                fontSize = 13.sp
                            )
                        },
                        maxLines = 3,
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("chat_message_input")
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = {
                            if (messageInput.isNotBlank()) {
                                repository.sendMessage(selectedConversationId, partnerId, messageInput)
                                messageInput = ""
                            }
                        },
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(BurgundyPrimary)
                            .testTag("chat_send_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Send",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }

        // Report Dialog
        if (showReportDialog) {
            AlertDialog(
                onDismissRequest = { showReportDialog = false },
                title = { Text(if (isAr) "إبلاغ عن محادثة أو سلوك" else "Report Conversation / Member") },
                text = {
                    Column {
                        Text(
                            text = if (isAr) "السبب الرئيسي للبلاغ:" else "Primary Reason:",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        val reasons = listOf(
                            "Commercial / Financial solicitation",
                            "Inappropriate or disrespectful behavior",
                            "Suspected fake or impersonation account",
                            "Underage suspicion"
                        )
                        reasons.forEach { r ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { reportReason = r }
                            ) {
                                RadioButton(selected = reportReason == r, onClick = { reportReason = r })
                                Text(r, fontSize = 12.sp)
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = reportDetails,
                            onValueChange = { reportDetails = it },
                            label = { Text(if (isAr) "تفاصيل إضافية (اختياري)" else "Additional evidence / details") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            repository.reportUser(partnerId, reportReason, reportDetails)
                            showReportDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = StatusRed)
                    ) {
                        Text(if (isAr) "إرسال البلاغ للإدارة" else "Submit Report")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showReportDialog = false }) {
                        Text(if (isAr) "إلغاء" else "Cancel")
                    }
                }
            )
        }

        // Block Confirmation Dialog
        if (showBlockConfirmation) {
            AlertDialog(
                onDismissRequest = { showBlockConfirmation = false },
                title = { Text(if (isAr) "تأكيد الحظر" else "Confirm Block") },
                text = {
                    Text(
                        if (isAr) "هل أنت متأكد من رغبتك في حظر ${partnerProfile.displayName}؟ لن تتمكنا من التواصل أو رؤية الملفات نهائياً."
                        else "Are you sure you wish to block ${partnerProfile.displayName}? Neither of you will be able to discover or communicate with each other."
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            repository.blockUser(partnerId)
                            showBlockConfirmation = false
                            onSelectConversation(null)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = StatusRed)
                    ) {
                        Text(if (isAr) "نعم، حظر الآن" else "Yes, Block Now")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showBlockConfirmation = false }) {
                        Text(if (isAr) "إلغاء" else "Cancel")
                    }
                }
            )
        }

    } else {
        // Matches & Conversations List View
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(WarmIvory)
                .padding(horizontal = 16.dp)
                .testTag("matches_list_screen")
        ) {
            Text(
                text = if (isAr) "المطابقات والمحادثات" else "Mutual Matches & Chats",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = BurgundyPrimary,
                modifier = Modifier.padding(top = 12.dp, bottom = 4.dp)
            )
            Text(
                text = if (isAr) "تفتح المحادثات فقط عند إعجاب الطرفين ببعضهما" else "Direct messaging is unlocked only between mutual matches",
                fontSize = 12.sp,
                color = CharcoalMuted,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            if (matches.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(GoldContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.FavoriteBorder,
                                contentDescription = null,
                                tint = BurgundyPrimary,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = if (isAr) "لا توجد مطابقات متبادلة بعد" else "No mutual matches yet",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = CharcoalText
                        )
                        Text(
                            text = if (isAr) "استمر في استكشاف الملفات الموثقة وإبداء الإعجاب الهادف." else "Continue discovering verified profiles and liking compatible partners.",
                            fontSize = 12.sp,
                            color = CharcoalMuted,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(matches) { match ->
                        val partnerId = if (match.user1Id == currentUser.id) match.user2Id else match.user1Id
                        val partnerProfile = profiles.find { it.id == partnerId }

                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = WarmIvorySurface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onSelectConversation(match.id) }
                                .testTag("conversation_item_${match.id}")
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(54.dp)
                                        .clip(CircleShape)
                                        .border(2.dp, ChampagneGold, CircleShape)
                                ) {
                                    Image(
                                        painter = painterResource(id = partnerProfile?.photoResId ?: R.drawable.img_avatar_layla),
                                        contentDescription = partnerProfile?.displayName,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = partnerProfile?.displayName ?: "Matched Member",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 15.sp,
                                            color = CharcoalText
                                        )
                                        Text(
                                            text = match.lastMessageTime ?: "New",
                                            fontSize = 11.sp,
                                            color = CharcoalMuted
                                        )
                                    }
                                    Text(
                                        text = match.lastMessage ?: "You both liked each other! Say hello.",
                                        fontSize = 12.sp,
                                        color = CharcoalMuted,
                                        maxLines = 1,
                                        modifier = Modifier.padding(top = 2.dp)
                                    )
                                }
                                if (match.unreadCount > 0) {
                                    Box(
                                        modifier = Modifier
                                            .padding(start = 8.dp)
                                            .size(20.dp)
                                            .clip(CircleShape)
                                            .background(BurgundyPrimary),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "${match.unreadCount}",
                                            color = Color.White,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold
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
