package com.example.model

enum class AppLanguage(val code: String, val displayName: String, val nativeName: String) {
    ENGLISH("en", "English", "English"),
    ARABIC("ar", "Arabic", "العربية")
}

enum class RelationshipGoal(val labelEn: String, val labelAr: String) {
    MARRIAGE_SOON("Marriage within 1-2 years", "الزواج خلال سنة إلى سنتين"),
    SERIOUS_RELATIONSHIP("Serious long-term partnership", "علاقة جادة طويلة الأمد"),
    MARRIAGE_FOCUSED("Marriage-focused courtship", "خطوبة بهدف الزواج"),
    EXPLORING_SERIOUS("Exploring with serious intent", "تعارف بنيّة جادة")
}

enum class ReligiousPractice(val labelEn: String, val labelAr: String) {
    VERY_PRACTICING("Very practicing", "ملتزم/ة دينياً"),
    MODERATELY_PRACTICING("Moderately practicing", "معتدل/ة"),
    CULTURAL_ONLY("Culturally focused", "اهتمام بالقيم الثقافية"),
    PREFER_NOT_TO_SAY("Prefer not to specify", "أفضل عدم التحديد")
}

data class UserProfile(
    val id: String,
    val displayName: String,
    val birthDate: String, // YYYY-MM-DD
    val calculatedAge: Int,
    val country: String,
    val city: String,
    val languages: List<String>,
    val bio: String,
    val interests: List<String>,
    val relationshipGoal: RelationshipGoal,
    val religiousPractice: ReligiousPractice = ReligiousPractice.MODERATELY_PRACTICING,
    val profession: String,
    val education: String,
    val photoResId: Int? = null,
    val isVerified: Boolean = false,
    val isPremium: Boolean = false,
    val isSuspended: Boolean = false,
    val isProfileHidden: Boolean = false,
    val completionPercentage: Int = 100,
    val lastActive: String = "Online recently"
)

data class MatchRecord(
    val id: String,
    val user1Id: String,
    val user2Id: String,
    val matchedAtTimestamp: Long = System.currentTimeMillis(),
    val lastMessage: String? = null,
    val lastMessageTime: String? = null,
    val unreadCount: Int = 0
)

data class ChatMessage(
    val id: String,
    val conversationId: String,
    val senderId: String,
    val recipientId: String,
    val text: String,
    val timestamp: Long = System.currentTimeMillis(),
    val timeFormatted: String,
    val isRead: Boolean = true
)

enum class PlanInterval(val labelEn: String, val labelAr: String) {
    MONTHLY("Monthly", "شهري"),
    QUARTERLY("3 Months", "٣ أشهر"),
    ANNUAL("Annual (Best Value)", "سنوي (القيمة الأفضل)")
}

data class MembershipPlan(
    val id: String,
    val nameEn: String,
    val nameAr: String,
    val priceUsd: Double,
    val interval: PlanInterval,
    val featuresEn: List<String>,
    val featuresAr: List<String>,
    val isPopular: Boolean = false
)

data class PaystackPayment(
    val reference: String,
    val userId: String,
    val planId: String,
    val amountUsd: Double,
    val currency: String = "USD",
    val status: String, // "success", "pending", "failed"
    val timestamp: Long = System.currentTimeMillis(),
    val authorizationCode: String = "AUTH_DEMO_CARD",
    val customerEmail: String
)

data class SafetyReport(
    val id: String,
    val reporterId: String,
    val reportedUserId: String,
    val reportedUserName: String,
    val reason: String,
    val details: String,
    val timestamp: Long = System.currentTimeMillis(),
    val status: String = "OPEN" // "OPEN", "REVIEWED", "ACTION_TAKEN", "DISMISSED"
)

data class AppNotification(
    val id: String,
    val title: String,
    val message: String,
    val timestampFormatted: String,
    val isRead: Boolean = false,
    val type: String // "MATCH", "MESSAGE", "BILLING", "SAFETY"
)
