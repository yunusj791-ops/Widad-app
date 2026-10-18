export type AppLanguage = 'en' | 'ar';

export type RelationshipGoal =
  | 'MARRIAGE_SOON'
  | 'SERIOUS_RELATIONSHIP'
  | 'MARRIAGE_FOCUSED'
  | 'EXPLORING_SERIOUS';

export type ReligiousPractice =
  | 'VERY_PRACTICING'
  | 'MODERATELY_PRACTICING'
  | 'CULTURAL_ONLY'
  | 'PREFER_NOT_TO_SAY';

export interface UserProfile {
  id: string;
  displayName: string;
  birthDate: string;
  calculatedAge: number;
  country: string;
  city: string;
  languages: string[];
  bio: string;
  interests: string[];
  relationshipGoal: RelationshipGoal;
  religiousPractice: ReligiousPractice;
  profession: string;
  education: string;
  avatarUrl: string;
  isVerified: boolean;
  isPremium: boolean;
  isSuspended: boolean;
  isProfileHidden: boolean;
}

export interface MatchRecord {
  id: string;
  user1Id: string;
  user2Id: string;
  matchedAtTimestamp: number;
  lastMessage?: string;
  lastMessageTime?: string;
  unreadCount: number;
}

export interface ChatMessage {
  id: string;
  conversationId: string;
  senderId: string;
  recipientId: string;
  text: string;
  timestamp: number;
  timeFormatted: string;
}

export interface MembershipPlan {
  id: string;
  nameEn: string;
  nameAr: string;
  priceUsd: number;
  intervalEn: string;
  intervalAr: string;
  featuresEn: string[];
  featuresAr: string[];
  isPopular?: boolean;
}

export interface SafetyReport {
  id: string;
  reporterId: string;
  reportedUserId: string;
  reportedUserName: string;
  reason: string;
  details: string;
  timestamp: number;
  status: 'OPEN' | 'REVIEWED' | 'ACTION_TAKEN' | 'DISMISSED';
}

export interface PaystackPayment {
  reference: string;
  userId: string;
  planId: string;
  amountUsd: number;
  currency: string;
  status: 'success' | 'pending' | 'failed';
  timestamp: number;
  customerEmail: string;
}

export interface AppNotification {
  id: string;
  title: string;
  message: string;
  timestamp: string;
  isRead: boolean;
}
