import { UserProfile, MembershipPlan, MatchRecord, ChatMessage, AppNotification, SafetyReport } from './types';

export const PAYSTACK_SHOP_URL = 'https://paystack.shop/pay/q0bp-vzftt';

export const INITIAL_CURRENT_USER: UserProfile = {
  id: 'user_me',
  displayName: 'Tariq Al-Mansoor',
  birthDate: '1996-05-14',
  calculatedAge: 29,
  country: 'United Arab Emirates',
  city: 'Dubai',
  languages: ['Arabic', 'English'],
  bio: 'Architect and urban designer passionate about sustainable Islamic heritage. Seeking a partner who values mutual kindness, shared intellectual curiosity, and building an enduring family together.',
  interests: ['Architecture', 'Literature', 'Travel', 'Arabic Calligraphy', 'Equestrian', 'Nature'],
  relationshipGoal: 'MARRIAGE_SOON',
  religiousPractice: 'MODERATELY_PRACTICING',
  profession: 'Senior Architect',
  education: 'Master of Architecture',
  avatarUrl: 'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=600&auto=format&fit=crop&q=80',
  isVerified: true,
  isPremium: false,
  isSuspended: false,
  isProfileHidden: false
};

export const INITIAL_PROFILES: UserProfile[] = [
  {
    id: 'user_layla',
    displayName: 'Layla Al-Hashimi',
    birthDate: '1997-09-22',
    calculatedAge: 28,
    country: 'United Arab Emirates',
    city: 'Abu Dhabi',
    languages: ['Arabic', 'English', 'French'],
    bio: 'Literature educator and researcher with a deep love for Andalusian poetry, specialty coffee, and meaningful dialogue. Looking for an educated, grounded partner with shared family values.',
    interests: ['Literature', 'Coffee Brewing', 'Arts', 'Travel', 'Nature', 'Calligraphy'],
    relationshipGoal: 'MARRIAGE_SOON',
    religiousPractice: 'MODERATELY_PRACTICING',
    profession: 'University Lecturer',
    education: 'Ph.D. in Comparative Literature',
    avatarUrl: 'https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=600&auto=format&fit=crop&q=80',
    isVerified: true,
    isPremium: true,
    isSuspended: false,
    isProfileHidden: false
  },
  {
    id: 'user_noor',
    displayName: 'Noor El-Sayed',
    birthDate: '1998-03-12',
    calculatedAge: 27,
    country: 'Egypt',
    city: 'Cairo',
    languages: ['Arabic', 'English'],
    bio: 'Biomedical engineer dedicated to healthcare technology. I cherish family gatherings, weekend hiking trips, and quiet evenings with classic books. Honest communication is paramount.',
    interests: ['Health & Wellness', 'Hiking', 'Literature', 'Volunteering', 'Cooking'],
    relationshipGoal: 'MARRIAGE_FOCUSED',
    religiousPractice: 'MODERATELY_PRACTICING',
    profession: 'Biomedical Engineer',
    education: 'B.Sc. Biomedical Engineering',
    avatarUrl: 'https://images.unsplash.com/photo-1517841905240-472988babdf9?w=600&auto=format&fit=crop&q=80',
    isVerified: true,
    isPremium: false,
    isSuspended: false,
    isProfileHidden: false
  },
  {
    id: 'user_omar',
    displayName: 'Omar Haddad',
    birthDate: '1994-11-04',
    calculatedAge: 31,
    country: 'Jordan',
    city: 'Amman',
    languages: ['Arabic', 'English'],
    bio: 'Software architect and entrepreneur. Passionate about innovation, equestrian traditions, and history. Ready to build a loving and resilient household rooted in respect.',
    interests: ['Entrepreneurship', 'Equestrian', 'History', 'Astronomy', 'Travel'],
    relationshipGoal: 'MARRIAGE_SOON',
    religiousPractice: 'VERY_PRACTICING',
    profession: 'Software Architect',
    education: 'M.Sc. Computer Science',
    avatarUrl: 'https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=600&auto=format&fit=crop&q=80',
    isVerified: true,
    isPremium: true,
    isSuspended: false,
    isProfileHidden: false
  },
  {
    id: 'user_farida',
    displayName: 'Farida Benali',
    birthDate: '1996-07-19',
    calculatedAge: 29,
    country: 'Morocco',
    city: 'Rabat',
    languages: ['Arabic', 'French', 'English'],
    bio: 'Financial analyst with a passion for Moroccan artisanal design and culinary arts. Seeking a warm-hearted companion who believes in teamwork and mutual emotional support.',
    interests: ['Gastronomy', 'Design', 'Economics', 'Travel', 'Museums'],
    relationshipGoal: 'SERIOUS_RELATIONSHIP',
    religiousPractice: 'MODERATELY_PRACTICING',
    profession: 'Senior Financial Analyst',
    education: 'Master of Finance',
    avatarUrl: 'https://images.unsplash.com/photo-1524504388940-b1c1722653e1?w=600&auto=format&fit=crop&q=80',
    isVerified: true,
    isPremium: false,
    isSuspended: false,
    isProfileHidden: false
  },
  {
    id: 'user_zayd',
    displayName: 'Zayd Al-Khatib',
    birthDate: '1993-01-28',
    calculatedAge: 32,
    country: 'Saudi Arabia',
    city: 'Riyadh',
    languages: ['Arabic', 'English'],
    bio: 'Environmental consultant and avid desert photographer. Seeking a life partner who embraces kindness, deep faith, and enthusiasm for personal growth.',
    interests: ['Photography', 'Nature', 'Astronomy', 'Volunteering', 'Literature'],
    relationshipGoal: 'MARRIAGE_FOCUSED',
    religiousPractice: 'VERY_PRACTICING',
    profession: 'Environmental Consultant',
    education: 'M.Sc. Environmental Science',
    avatarUrl: 'https://images.unsplash.com/photo-1492562080023-ab3db95bfbce?w=600&auto=format&fit=crop&q=80',
    isVerified: true,
    isPremium: true,
    isSuspended: false,
    isProfileHidden: false
  }
];

export const MEMBERSHIP_PLANS: MembershipPlan[] = [
  {
    id: 'plan_free',
    nameEn: 'Standard Free',
    nameAr: 'الباقة المجانية',
    priceUsd: 0,
    intervalEn: 'Free Forever',
    intervalAr: 'مجاناً دائماً',
    featuresEn: [
      'Create complete verified profile',
      'Up to 10 daily intentional likes',
      'Mutual match notification',
      'Unlimited direct messaging with matches',
      'Basic country & age filters',
      'Standard safety controls'
    ],
    featuresAr: [
      'إنشاء ملف تعريفي كامل وموثق',
      '١٠ إعجابات يومية هادفة',
      'إشعار فوري عند التوافق المتبادل',
      'محادثات غير محدودة مع المتوافقين',
      'فلاتر أساسية حسب الدولة والسن',
      'أدوات الأمان والحظر والتبليغ'
    ]
  },
  {
    id: 'plan_monthly',
    nameEn: 'Premium Monthly',
    nameAr: 'الاشتراك الشهري',
    priceUsd: 9.99,
    intervalEn: 'Billed monthly',
    intervalAr: 'تجديد شهري',
    featuresEn: [
      'Unlimited daily likes',
      'See who liked you before matching',
      'Advanced cultural & values filters',
      'Read receipts for all conversations',
      'Priority profile discovery boost',
      'Incognito & discreet profile mode'
    ],
    featuresAr: [
      'إعجابات يومية غير محدودة',
      'معرفة من أُعجب بملفك قبل التوافق',
      'فلاتر متقدمة للقيم واللغات والالتزام',
      'مؤشرات قراءة الرسائل في المحادثات',
      'أولوية الظهور في نتائج الاستكشاف',
      'وضع الخصوصية والتصفح الخفي'
    ]
  },
  {
    id: 'plan_quarterly',
    nameEn: 'Quarterly Courting',
    nameAr: 'باقة ٣ أشهر المميزة',
    priceUsd: 24.99,
    intervalEn: 'Billed every 3 months',
    intervalAr: 'تجديد كل ٣ أشهر',
    featuresEn: [
      'Everything in Premium Monthly',
      'Save 17% compared to monthly',
      'Complimentary 3 profile spotlights',
      'Direct relationship advisor guidelines',
      'Priority safety verification badge'
    ],
    featuresAr: [
      'جميع مزايا الباقة الشهرية',
      'توفير ١٧٪ مقارنة بالدفع الشهري',
      '٣ إبرازات مميزة لملفك مجاناً',
      'إرشادات التوفيق والتعارف الجاد',
      'شارة الأولوية والتحقق الموثق'
    ],
    isPopular: true
  },
  {
    id: 'plan_annual',
    nameEn: 'Annual Commitment',
    nameAr: 'الباقة السنوية الكاملة',
    priceUsd: 79.99,
    intervalEn: 'Billed annually (Best Value)',
    intervalAr: 'اشتراك سنوي (أفضل قيمة)',
    featuresEn: [
      'Complete full-year access (33% off)',
      'Full VIP discovery features',
      'Unlimited rewinds on passed profiles',
      'Exclusive verified circle badge',
      'Dedicated email concierge support'
    ],
    featuresAr: [
      'اشتراك سنوي كامل (خصم ٣٣٪)',
      'كافة ميزات الاستكشاف المتقدمة',
      'التراجع غير المحدود عن التخطي',
      'شارة الدائرة الموثقة الحصرية',
      'دعم استشاري مخصص عبر البريد'
    ]
  }
];

export const INITIAL_MATCHES: MatchRecord[] = [
  {
    id: 'match_me_layla',
    user1Id: 'user_me',
    user2Id: 'user_layla',
    matchedAtTimestamp: Date.now() - 86400000,
    lastMessage: 'Assalamu Alaikum Tariq! Thank you for the thoughtful message.',
    lastMessageTime: 'Yesterday',
    unreadCount: 1
  }
];

export const INITIAL_MESSAGES: Record<string, ChatMessage[]> = {
  match_me_layla: [
    {
      id: 'msg_1',
      conversationId: 'match_me_layla',
      senderId: 'user_me',
      recipientId: 'user_layla',
      text: 'Peace be upon you Layla. I was genuinely impressed by your academic research on Andalusian poetry.',
      timestamp: Date.now() - 90000000,
      timeFormatted: 'Yesterday 16:30'
    },
    {
      id: 'msg_2',
      conversationId: 'match_me_layla',
      senderId: 'user_layla',
      recipientId: 'user_me',
      text: 'Assalamu Alaikum Tariq! Thank you for the thoughtful message. It is wonderful to meet an architect who appreciates classical heritage.',
      timestamp: Date.now() - 85000000,
      timeFormatted: 'Yesterday 18:45'
    }
  ]
};

export const INITIAL_NOTIFICATIONS: AppNotification[] = [
  {
    id: 'notif_1',
    title: 'Mutual Match Celebrated! مودة ومحبة',
    message: 'You and Layla Al-Hashimi both expressed interest in each other.',
    timestamp: 'Yesterday',
    isRead: true
  },
  {
    id: 'notif_2',
    title: 'Welcome to Widad (مودة)',
    message: 'Your profile is active. Remember: initial safety and respectful communication lead to blessed partnerships.',
    timestamp: '2 days ago',
    isRead: true
  }
];

export const INITIAL_REPORTS: SafetyReport[] = [
  {
    id: 'rep_101',
    reporterId: 'user_omar',
    reportedUserId: 'user_suspicious_1',
    reportedUserName: 'Crypto Investment Bot',
    reason: 'Commercial / Financial solicitation',
    details: 'User initiated conversation asking to invest in unauthorized trading scheme.',
    timestamp: Date.now() - 172800000,
    status: 'OPEN'
  }
];

export function calculateCompatibility(userA: UserProfile, userB: UserProfile) {
  let score = 65;
  const commonInterests = userA.interests.filter(i => userB.interests.includes(i));
  score += Math.min(20, commonInterests.length * 5);

  const goalMatches = userA.relationshipGoal === userB.relationshipGoal;
  if (goalMatches) score += 10;

  const commonLanguages = userA.languages.filter(l => userB.languages.includes(l));
  if (commonLanguages.length > 0) score += 5;

  return {
    score: Math.min(98, Math.max(50, score)),
    commonInterests,
    goalMatches,
    commonLanguages
  };
}
