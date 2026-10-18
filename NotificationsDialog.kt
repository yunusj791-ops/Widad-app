package com.example.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.model.AppNotification
import com.example.ui.theme.*

@Composable
fun NotificationsDialog(
    language: AppLanguage,
    notifications: List<AppNotification>,
    onDismiss: () -> Unit
) {
    val isAr = language == AppLanguage.ARABIC

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = null,
                    tint = BurgundyPrimary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isAr) "الإشعارات والتنبيهات" else "Notifications & Alerts",
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = BurgundyPrimary
                )
            }
        },
        text = {
            if (notifications.isEmpty()) {
                Text(
                    text = if (isAr) "لا توجد إشعارات جديدة حالياً." else "No new notifications.",
                    fontSize = 13.sp,
                    color = CharcoalMuted
                )
            } else {
                LazyColumn(
                    modifier = Modifier.heightIn(max = 340.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(notifications) { notif ->
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = WarmIvoryVariant,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(
                                    text = notif.title,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = CharcoalText
                                )
                                Text(
                                    text = notif.message,
                                    fontSize = 12.sp,
                                    color = CharcoalMuted,
                                    modifier = Modifier.padding(top = 2.dp)
                                )
                                Text(
                                    text = notif.timestampFormatted,
                                    fontSize = 10.sp,
                                    color = BurgundyLight,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = BurgundyPrimary)
            ) {
                Text(if (isAr) "إغلاق" else "Close")
            }
        }
    )
}
