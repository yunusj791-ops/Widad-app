package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.example.model.AppLanguage
import com.example.ui.theme.*

enum class LegalViewType {
    SAFETY,
    TERMS,
    PRIVACY,
    COMMUNITY
}

@Composable
fun SafetyAndLegalScreen(
    language: AppLanguage,
    viewType: LegalViewType,
    onBack: () -> Unit
) {
    val isAr = language == AppLanguage.ARABIC

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WarmIvory)
            .statusBarsPadding()
            .padding(16.dp)
            .testTag("safety_legal_screen")
    ) {
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
            Text(
                text = when (viewType) {
                    LegalViewType.SAFETY -> if (isAr) "مركز الأمان ومكافحة الاحتيال" else "Safety & Trust Center"
                    LegalViewType.TERMS -> if (isAr) "شروط الاستخدام والخدمة" else "Terms of Service"
                    LegalViewType.PRIVACY -> if (isAr) "سياسة الخصوصية وحماية البيانات" else "Privacy Policy"
                    LegalViewType.COMMUNITY -> if (isAr) "إرشادات مجتمع مودة" else "Community Guidelines"
                },
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                color = BurgundyPrimary
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = WarmIvorySurface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(18.dp)
            ) {
                when (viewType) {
                    LegalViewType.SAFETY -> {
                        SafetyContent(isAr = isAr)
                    }
                    LegalViewType.TERMS -> {
                        TermsContent(isAr = isAr)
                    }
                    LegalViewType.PRIVACY -> {
                        PrivacyContent(isAr = isAr)
                    }
                    LegalViewType.COMMUNITY -> {
                        CommunityContent(isAr = isAr)
                    }
                }
            }
        }
    }
}

@Composable
private fun SafetyContent(isAr: Boolean) {
    Text(
        text = if (isAr) "إرشادات الأمان والحماية الشخصية" else "Personal Safety & Scam Prevention",
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        color = BurgundyPrimary
    )
    Spacer(modifier = Modifier.height(8.dp))
    Text(
        text = if (isAr)
            "١. لا ترسل أموالاً أو حوالات تحت أي ظرف:\nأي مستخدم يطلب مساعدة مالية، رسوم سفر، استثمارات تجارية أو عملات رقمية هو محتال. قم بإبلاغ الإدارة عنه فوراً.\n\n٢. حافظ على سرية بياناتك الحساسة:\nلا تشارك رقم حسابك البنكي، بطاقتك الائتمانية، أو عنوان منزلك الدقيق.\n\n٣. تواصل عبر منصة مودة:\nابقَ محادثاتك الأولى داخل تطبيق مودة حتى تطمئن لجدية الشخص والتأكد من هويته.\n\n٤. اللقاء في أماكن عامة:\nإذا اتفقتما على لقاء تعارف رسمي، احرص أن يكون في مكان عام ومحترم، وأبلغ أحد أفراد عائلتك أو المقربين منك."
        else
            "1. Never Send Money or Financial Transfers:\nAnyone soliciting financial assistance, travel funds, cryptocurrency, or investment opportunities is violating our community rules. Report them immediately.\n\n2. Guard Sensitive Personal Information:\nNever disclose banking numbers, payment credentials, or your precise private residential address.\n\n3. Keep Initial Conversations on Widad:\nOur in-app messaging protects your personal contact details while establishing sincere compatibility.\n\n4. Public & Chaperoned Meetings:\nWhen meeting for serious courtship, choose reputable public places or include trusted family representatives.",
        fontSize = 13.sp,
        color = CharcoalText,
        lineHeight = 19.sp
    )
}

@Composable
private fun TermsContent(isAr: Boolean) {
    Text(
        text = if (isAr) "شروط استخدام منصة مودة" else "Widad Terms of Service",
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        color = BurgundyPrimary
    )
    Spacer(modifier = Modifier.height(8.dp))
    Text(
        text = if (isAr)
            "١. الأهلية القانونية (١٨+):\nالخدمة مخصصة حصرياً للبالغين الذين أتموا ثمانية عشر (١٨) عاماً فما فوق. يُحظر تماماً تسجيل القاصرين.\n\n٢. الغرض الجاد:\nالمنصة مخصصة للتعارف الجاد بهدف الخطوبة والزواج وتكوين أسرة. يُحظر استخدامها لأغراض العبث أو التسلية غير المسؤولة.\n\n٣. اشتراكات Paystack:\nجميع المدفوعات تتم عبر بوابة Paystack المعتمدة. يمكنك إلغاء التجديد التلقائي في أي وقت من شاشة العضوية.\n\n٤. إنهاء الحساب:\nتحتفظ إدارة المنصة بالحق في تعليق أو حذف أي حساب يخالف المعايير الأخلاقية أو يقوم بسلوك احتيالي."
        else
            "1. Legal Eligibility (Age 18+):\nMembership is strictly limited to adults aged 18 and older. Profiles misrepresenting age will be permanently banned.\n\n2. Serious Relationship Purpose:\nWidad is dedicated to intentional courtship, marriage, and lasting partnerships. Casual harassment or commercial solicitation is prohibited.\n\n3. Paystack Subscriptions:\nPremium upgrades are securely processed through authorized Paystack billing. You may cancel auto-renewal anytime in Membership settings.\n\n4. Account Termination:\nWidad reserves the right to suspend any account violating community dignity or engaging in deceitful practices.",
        fontSize = 13.sp,
        color = CharcoalText,
        lineHeight = 19.sp
    )
}

@Composable
private fun PrivacyContent(isAr: Boolean) {
    Text(
        text = if (isAr) "سياسة الخصوصية وحفظ البيانات" else "Privacy & Data Protection Policy",
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        color = BurgundyPrimary
    )
    Spacer(modifier = Modifier.height(8.dp))
    Text(
        text = if (isAr)
            "• حماية تاريخ الميلاد الدقيق: نحسب عمرك فقط لعرضه، بينما يظل تاريخ ميلادك الكامل مخفياً عن جميع الأعضاء.\n• خصوصية الموقع: نعرض الدولة والمدينة فقط، ولا نشارك أي إحداثيات GPS جغرافية دقيقة.\n• الرسائل الخاصة: محادثاتك مع المتوافقين مشفرة ومحمية، ولا تُتاح إلا لطرفي المحادثة.\n• حق حذف الحساب: يحق لك في أي وقت حذف حسابك وبياناتك بالكامل من إعدادات الحساب دون قيود."
        else
            "• Exact Birthdate Protection: We only display your calculated age; exact birthdates remain confidential.\n• Location Privacy: Only general country and city are shown; precise coordinates are never broadcast.\n• Message Confidentiality: Conversations are private between matched participants.\n• Complete Right of Deletion: You can delete your profile, messages, and uploaded photos anytime.",
        fontSize = 13.sp,
        color = CharcoalText,
        lineHeight = 19.sp
    )
}

@Composable
private fun CommunityContent(isAr: Boolean) {
    Text(
        text = if (isAr) "إرشادات مجتمع مودة" else "Community Guidelines & Cultural Dignity",
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        color = BurgundyPrimary
    )
    Spacer(modifier = Modifier.height(8.dp))
    Text(
        text = if (isAr)
            "• الاحترام والتهذيب في الحديث.\n• الصدق في عرض الأهداف الحياتية والظروف الشخصية.\n• احترام التنوع الثقافي والديني في العالم العربي.\n• الحفاظ على المودة والأخلاق الفاضلة في كل تفاعل."
        else
            "• Treat all members with courtesy and high moral conduct.\n• Be honest and authentic regarding personal expectations and horizons.\n• Honor cultural and religious diversity among Arab communities.\n• Uphold mutual dignity and good character at all times.",
        fontSize = 13.sp,
        color = CharcoalText,
        lineHeight = 19.sp
    )
}
