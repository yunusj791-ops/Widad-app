package com.example

import com.example.data.WidadRepository
import com.example.model.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.util.Calendar

class ExampleUnitTest {

    private lateinit var repository: WidadRepository

    @Before
    fun setUp() {
        repository = WidadRepository()
    }

    @Test
    fun testInitialProfiles_containsEligibleProfiles() {
        val profiles = repository.profiles.value
        assertTrue("Profiles should not be empty", profiles.isNotEmpty())
        assertTrue("All initial profiles should be 18+", profiles.all { it.calculatedAge >= 18 })
    }

    @Test
    fun testCompatibilityCalculation_returnsExpectedScores() {
        val userA = UserProfile(
            id = "a",
            displayName = "User A",
            birthDate = "1996-01-01",
            calculatedAge = 29,
            country = "UAE",
            city = "Dubai",
            languages = listOf("Arabic", "English"),
            bio = "Bio A",
            interests = listOf("Literature", "Travel", "Nature"),
            relationshipGoal = RelationshipGoal.MARRIAGE_SOON,
            profession = "Architect",
            education = "Degree"
        )

        val userB = UserProfile(
            id = "b",
            displayName = "User B",
            birthDate = "1998-01-01",
            calculatedAge = 27,
            country = "UAE",
            city = "Abu Dhabi",
            languages = listOf("Arabic", "English"),
            bio = "Bio B",
            interests = listOf("Literature", "Travel", "Art"),
            relationshipGoal = RelationshipGoal.MARRIAGE_SOON,
            profession = "Lecturer",
            education = "Degree"
        )

        val result = repository.calculateCompatibility(userA, userB)
        assertTrue("Score should be higher than baseline 65%", result.percentage >= 80)
        assertTrue("Goal should match", result.goalMatches)
        assertEquals(2, result.commonInterests.size)
    }

    @Test
    fun testMutualMatch_createsMatchAndChatUnlocked() {
        val initialMatchCount = repository.matches.value.size
        // Liking user_noor should trigger mutual match
        val isMatch = repository.likeProfile("user_noor")
        assertTrue("Should result in a mutual match", isMatch)
        assertEquals("Match count should increment", initialMatchCount + 1, repository.matches.value.size)
    }

    @Test
    fun testBlocking_removesProfileAndMatches() {
        val targetId = "user_layla"
        repository.blockUser(targetId)

        assertTrue(repository.blockedUserIds.value.contains(targetId))
        assertTrue("Blocked user should not appear in matches", repository.matches.value.none { it.user1Id == targetId || it.user2Id == targetId })
        assertTrue("Blocked user should not appear in profiles", repository.profiles.value.none { it.id == targetId })
    }

    @Test
    fun testMessaging_appendsMessageToConversation() {
        val conversationId = "match_me_layla"
        repository.sendMessage(conversationId, "user_layla", "Peace be upon you.")

        val msgs = repository.messages.value[conversationId]
        assertNotNull(msgs)
        assertTrue(msgs!!.any { it.text == "Peace be upon you." })
    }

    @Test
    fun testPaystackCheckout_activatesPremiumOnSuccess() {
        val plan = repository.membershipPlans.first { it.id == "plan_monthly" }
        val payment = repository.processPaystackCheckout(plan, "test@widad.app", simulateSuccess = true)

        assertEquals("success", payment.status)
        assertTrue(payment.reference.startsWith("WIDAD-PAY-"))
        assertTrue("Current user should be upgraded to premium", repository.currentUser.value.isPremium)

        // Cancel subscription
        repository.cancelSubscription()
        assertFalse("Current user should no longer be premium", repository.currentUser.value.isPremium)
    }

    @Test
    fun testAgeGateLogic_validates18Plus() {
        val today = Calendar.getInstance()
        val currentYear = today.get(Calendar.YEAR)

        fun calculateAge(year: Int, month: Int, day: Int): Int {
            var age = currentYear - year
            if (today.get(Calendar.MONTH) + 1 < month ||
                (today.get(Calendar.MONTH) + 1 == month && today.get(Calendar.DAY_OF_MONTH) < day)
            ) {
                age--
            }
            return age
        }

        val adultAge = calculateAge(1996, 5, 14)
        val minorAge = calculateAge(currentYear - 15, 1, 1)

        assertTrue("1996 should be adult 18+", adultAge >= 18)
        assertFalse("15 year old should be underage", minorAge >= 18)
    }
}
