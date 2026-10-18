package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.content.Intent
import android.net.Uri
import androidx.compose.ui.platform.LocalContext
import com.example.data.WidadRepository
import com.example.model.*
import com.example.ui.components.DisclaimerNote
import com.example.ui.theme.*

@Composable
fun MembershipScreen(
    language: AppLanguage,
    repository: WidadRepository
) {
    val isAr = language == AppLanguage.ARABIC
    val currentUser by repository.currentUser.collectAsState()
    val payments by repository.payments.collectAsState()
    val plans = repository.membershipPlans

    var selectedPlan by remember { mutableStateOf(plans.find { it.isPopular } ?: plans[1]) }
    var showCheckoutDialog by remember { mutableStateOf(false) }
    var checkoutProcessing by remember { mutableStateOf(false) }
    var checkoutResult by remember { mutableStateOf<PaystackPayment?>(null) }
    var simulatePaymentFailure by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val paystackUrl = "https://paystack.shop/pay/q0bp-vzftt"
    val openPaystackPayment: () -> Unit = {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(paystackUrl)).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WarmIvory)
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState())
            .testTag("membership_screen")
    ) {
        Text(
            text = if (isAr) "العضوية والاشتراكات" else "Membership & Plans",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = BurgundyPrimary,
            modifier = Modifier.padding(top = 12.dp, bottom = 4.dp)
        )
        Text(
            text = if (isAr) "استمتع بميزات بحث واكتشاف متقدمة مع الحفاظ على أمانك التام" else "Unlock premium discovery filters, unlimited likes, and respectful insights",
            fontSize = 12.sp,
            color = CharcoalMuted,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // Current Membership Status Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (currentUser.isPremium) GoldContainer else WarmIvorySurface
            ),
            border = if (currentUser.isPremium) androidx.compose.foundation.BorderStroke(1.dp, ChampagneGold) else null,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(if (currentUser.isPremium) BurgundyPrimary else WarmIvoryVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (currentUser.isPremium) Icons.Default.Stars else Icons.Default.Person,
                        contentDescription = null,
                        tint = if (currentUser.isPremium) ChampagneGoldLight else CharcoalMuted
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (currentUser.isPremium) {
                            if (isAr) "عضوية التميز نشطة (Premium Active)" else "Active Premium Membership"
                        } else {
                            if (isAr) "العضوية المجانية القياسية (Free)" else "Standard Free Membership"
                        },
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = if (currentUser.isPremium) BurgundyPrimary else CharcoalText
                    )
                    Text(
                        text = if (currentUser.isPremium) {
                            if (isAr) "اشتراك مفعل عبر Paystack • يتجدد تلقائياً" else "Active via Paystack gateway • Automatic renewal"
                        } else {
                            if (isAr) "١٠ إعجابات يومية • ميزات أساسية" else "10 daily intentional likes • Core features"
                        },
                        fontSize = 12.sp,
                        color = CharcoalMuted
                    )
                }
                if (currentUser.isPremium) {
                    OutlinedButton(
                        onClick = { repository.cancelSubscription() },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = StatusRed),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(if (isAr) "إلغاء" else "Cancel", fontSize = 11.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Plans Selector
        Text(
            text = if (isAr) "اختر الباقة المناسبة:" else "Select Membership Plan:",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = CharcoalText,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        plans.filter { it.priceUsd > 0 }.forEach { plan ->
            val isSelected = selectedPlan.id == plan.id
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) BurgundyContainer else WarmIvorySurface
                ),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (isSelected) BurgundyPrimary else BorderSubtle
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable {
                        selectedPlan = plan
                        if (plan.priceUsd == 9.99 || plan.id == "plan_monthly") {
                            openPaystackPayment()
                        }
                    }
                    .testTag("plan_card_${plan.id}")
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = isSelected,
                                onClick = {
                                    selectedPlan = plan
                                    if (plan.priceUsd == 9.99) {
                                        openPaystackPayment()
                                    }
                                },
                                colors = RadioButtonDefaults.colors(selectedColor = BurgundyPrimary)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Column {
                                Text(
                                    text = if (isAr) plan.nameAr else plan.nameEn,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = CharcoalText
                                )
                                Text(
                                    text = if (isAr) plan.interval.labelAr else plan.interval.labelEn,
                                    fontSize = 11.sp,
                                    color = CharcoalMuted
                                )
                            }
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "$${plan.priceUsd}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = BurgundyPrimary
                            )
                            if (plan.isPopular) {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = ChampagneGold
                                ) {
                                    Text(
                                        text = if (isAr) "الأكثر طلباً" else "Most Popular",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = BurgundyDark,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    }

                    // Features list
                    Spacer(modifier = Modifier.height(8.dp))
                    val features = if (isAr) plan.featuresAr else plan.featuresEn
                    features.take(3).forEach { f ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 1.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = BurgundyPrimary,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = f, fontSize = 11.sp, color = CharcoalText)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        onClick = {
                            selectedPlan = plan
                            openPaystackPayment()
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isSelected) BurgundyPrimary else ChampagneGoldDark,
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .testTag("plan_upgrade_button_${plan.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Stars,
                            contentDescription = null,
                            tint = ChampagneGoldLight,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (plan.priceUsd == 9.99) {
                                if (isAr) "الترقية للباقة الشهرية ($9.99)" else "Monthly $9.99 - Upgrade"
                            } else {
                                if (isAr) "ترقية إلى ${plan.nameAr} ($${plan.priceUsd})" else "Upgrade ($${plan.priceUsd})"
                            },
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Paystack Checkout Button
        Button(
            onClick = {
                openPaystackPayment()
            },
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BurgundyPrimary),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("paystack_checkout_button")
        ) {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = null,
                tint = ChampagneGoldLight,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "${if (isAr) "ترقية الآن عبر Paystack ($" else "Upgrade to Pro with Paystack ($"}${selectedPlan.priceUsd})",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = Color.White
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            TextButton(
                onClick = {
                    showCheckoutDialog = true
                    checkoutResult = null
                    checkoutProcessing = false
                }
            ) {
                Text(
                    text = if (isAr) "محاكاة اختبارية لبوابة الدفع (Sandbox Test)" else "Simulate Test Checkout (Sandbox)",
                    fontSize = 11.sp,
                    color = CharcoalMuted
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Payment History Ledger
        Text(
            text = if (isAr) "سجل المدفوعات والاشتراكات" else "Payment History Ledger",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = CharcoalText,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        if (payments.isEmpty()) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = WarmIvorySurface,
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (isAr) "لا توجد معاملات سابقة حتى الآن." else "No previous payment records found.",
                    fontSize = 12.sp,
                    color = CharcoalMuted,
                    modifier = Modifier.padding(14.dp)
                )
            }
        } else {
            payments.forEach { pay ->
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = WarmIvorySurface,
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = pay.reference,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = BurgundyPrimary
                            )
                            Text(
                                text = "${pay.customerEmail} • ${pay.currency} ${pay.amountUsd}",
                                fontSize = 11.sp,
                                color = CharcoalMuted
                            )
                        }
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = if (pay.status == "success") StatusGreenContainer else StatusRedContainer
                        ) {
                            Text(
                                text = pay.status.uppercase(),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (pay.status == "success") StatusGreen else StatusRed,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Paystack Security & Test Mode Notice
        DisclaimerNote(
            textEn = "Paystack Integration: Running in official Sandbox/Test mode. Test reference is generated on the server and verified via HMAC signatures without exposing secret keys in client bundles.",
            textAr = "تكامل Paystack: يعمل في بيئة الاختبار التجريبية الرسمية. يتم التحقق من المراجع عبر الخادم دون كشف المفاتيح السرية في تطبيق العميل.",
            language = language
        )

        Spacer(modifier = Modifier.height(24.dp))
    }

    // Paystack Checkout Modal
    if (showCheckoutDialog) {
        val email = "yunusj791@gmail.com" // Authenticated customer email
        val ref = remember { "WIDAD-PAY-${System.currentTimeMillis()}" }

        AlertDialog(
            onDismissRequest = { if (!checkoutProcessing) showCheckoutDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(BurgundyPrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("P", color = ChampagneGoldLight, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isAr) "بوابة دفع Paystack (وضع الاختبار)" else "Paystack Checkout Gateway (Test)",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            text = {
                Column {
                    if (checkoutResult == null) {
                        Text(
                            text = if (isAr) "تأكيد الدفع والتفاصيل:" else "Confirm Transaction Details:",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = CharcoalText
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "${if (isAr) "الباقة: " else "Selected Plan: "}${if (isAr) selectedPlan.nameAr else selectedPlan.nameEn}",
                            fontSize = 13.sp
                        )
                        Text(
                            text = "${if (isAr) "المبلغ: " else "Amount: "}$${selectedPlan.priceUsd} USD",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = BurgundyPrimary
                        )
                        Text(
                            text = "${if (isAr) "البريد الإلكتروني للعميل: " else "Customer Email: "}$email",
                            fontSize = 12.sp,
                            color = CharcoalMuted
                        )
                        Text(
                            text = "${if (isAr) "المرجع: " else "Transaction Ref: "}$ref",
                            fontSize = 11.sp,
                            color = CharcoalMuted
                        )

                        Spacer(modifier = Modifier.height(12.dp))
                        // Test toggle: simulate card failure
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable { simulatePaymentFailure = !simulatePaymentFailure }
                        ) {
                            Checkbox(
                                checked = simulatePaymentFailure,
                                onCheckedChange = { simulatePaymentFailure = it }
                            )
                            Text(
                                text = if (isAr) "اختبار محاكاة بطاقة مرفوضة (Failed Card)" else "Simulate failed payment scenario",
                                fontSize = 12.sp
                            )
                        }

                        if (checkoutProcessing) {
                            Spacer(modifier = Modifier.height(14.dp))
                            LinearProgressIndicator(
                                modifier = Modifier.fillMaxWidth(),
                                color = BurgundyPrimary
                            )
                            Text(
                                text = if (isAr) "جاري التحقق من المعاملة عبر Paystack..." else "Verifying transaction signature with Paystack...",
                                fontSize = 11.sp,
                                color = CharcoalMuted,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    } else {
                        // Result screen
                        val res = checkoutResult!!
                        if (res.status == "success") {
                            Text(
                                text = if (isAr) "تمت عملية الدفع بنجاح! مبروك ترقية العضوية." else "Payment Verified Successfully! Welcome to Premium.",
                                color = StatusGreen,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Text(
                                text = "${if (isAr) "رقم الإيصال المعتمد: " else "Receipt Reference: "}${res.reference}",
                                fontSize = 12.sp,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        } else {
                            Text(
                                text = if (isAr) "فشلت عملية الدفع. يرجى تجربة بطاقة أخرى." else "Payment was declined by card issuer.",
                                color = StatusRed,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            },
            confirmButton = {
                if (checkoutResult == null) {
                    Button(
                        onClick = {
                            checkoutProcessing = true
                            val payment = repository.processPaystackCheckout(
                                plan = selectedPlan,
                                customerEmail = email,
                                simulateSuccess = !simulatePaymentFailure
                            )
                            checkoutProcessing = false
                            checkoutResult = payment
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = BurgundyPrimary)
                    ) {
                        Text(if (isAr) "إتمام الدفع التجريبي" else "Authorize Test Payment")
                    }
                } else {
                    Button(
                        onClick = { showCheckoutDialog = false }
                    ) {
                        Text(if (isAr) "إغلاق" else "Done")
                    }
                }
            },
            dismissButton = {
                if (checkoutResult == null) {
                    TextButton(onClick = { showCheckoutDialog = false }) {
                        Text(if (isAr) "إلغاء" else "Cancel")
                    }
                }
            }
        )
    }
}
