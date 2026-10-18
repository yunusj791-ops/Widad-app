import { useState } from 'react';
import {
  Heart,
  X,
  Star,
  Shield,
  MessageCircle,
  Compass,
  CreditCard,
  User,
  Check,
  Send,
  Lock,
  ChevronDown,
  ChevronUp,
  Sparkles,
  ArrowRight,
  Settings,
  Bell,
  Search,
  AlertTriangle,
  ExternalLink
} from 'lucide-react';
import {
  AppLanguage,
  UserProfile,
  MatchRecord,
  ChatMessage,
  SafetyReport,
  PaystackPayment
} from './types';
import {
  INITIAL_CURRENT_USER,
  INITIAL_PROFILES,
  MEMBERSHIP_PLANS,
  INITIAL_MATCHES,
  INITIAL_MESSAGES,
  INITIAL_NOTIFICATIONS,
  INITIAL_REPORTS,
  PAYSTACK_SHOP_URL,
  calculateCompatibility
} from './data';

export default function App() {
  const [lang, setLang] = useState<AppLanguage>('en');
  const isAr = lang === 'ar';

  const [activeTab, setActiveTab] = useState<'landing' | 'discovery' | 'matches' | 'membership' | 'profile' | 'admin' | 'safety'>('landing');
  const [currentUser, setCurrentUser] = useState<UserProfile>(INITIAL_CURRENT_USER);
  const [profiles, setProfiles] = useState<UserProfile[]>(INITIAL_PROFILES);
  const [matches, setMatches] = useState<MatchRecord[]>(INITIAL_MATCHES);
  const [messages, setMessages] = useState<Record<string, ChatMessage[]>>(INITIAL_MESSAGES);
  const [reports, setReports] = useState<SafetyReport[]>(INITIAL_REPORTS);
  const [payments, setPayments] = useState<PaystackPayment[]>([]);
  const [notifications] = useState(INITIAL_NOTIFICATIONS);

  const [selectedConversationId, setSelectedConversationId] = useState<string | null>(null);
  const [activeCardIndex, setActiveCardIndex] = useState(0);
  const [matchedProfile, setMatchedProfile] = useState<UserProfile | null>(null);
  const [showAuthModal, setShowAuthModal] = useState(false);
  const [authMode, setAuthMode] = useState<'signin' | 'register'>('signin');
  const [messageInput, setMessageInput] = useState('');
  const [expandedFaq, setExpandedFaq] = useState<number | null>(null);
  const [adminTab, setAdminTab] = useState<'users' | 'reports' | 'payments'>('users');
  const [adminSearch, setAdminSearch] = useState('');

  // Open the Paystack URL in a new tab
  const handleUpgradeToPro = () => {
    window.open(PAYSTACK_SHOP_URL, '_blank', 'noopener,noreferrer');
  };

  const handleLike = (profile: UserProfile) => {
    // Check for mutual match (Layla, Noor reciprocate)
    if (['user_layla', 'user_noor', 'user_omar'].includes(profile.id)) {
      const matchId = `match_me_${profile.id}`;
      if (!matches.some(m => m.id === matchId)) {
        const newMatch: MatchRecord = {
          id: matchId,
          user1Id: currentUser.id,
          user2Id: profile.id,
          matchedAtTimestamp: Date.now(),
          lastMessage: isAr ? 'توافق جديد! ابدأ المحادثة المحترمة.' : 'New mutual match! Say hello respectfully.',
          lastMessageTime: 'Just now',
          unreadCount: 1
        };
        setMatches(prev => [newMatch, ...prev]);
      }
      setMatchedProfile(profile);
    }
    setActiveCardIndex(prev => prev + 1);
  };

  const handlePass = () => {
    setActiveCardIndex(prev => prev + 1);
  };

  const handleSendMessage = () => {
    if (!messageInput.trim() || !selectedConversationId) return;
    const activeMatch = matches.find(m => m.id === selectedConversationId);
    if (!activeMatch) return;
    const recipientId = activeMatch.user1Id === currentUser.id ? activeMatch.user2Id : activeMatch.user1Id;

    const newMsg: ChatMessage = {
      id: `msg_${Date.now()}`,
      conversationId: selectedConversationId,
      senderId: currentUser.id,
      recipientId,
      text: messageInput.trim(),
      timestamp: Date.now(),
      timeFormatted: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })
    };

    setMessages(prev => ({
      ...prev,
      [selectedConversationId]: [...(prev[selectedConversationId] || []), newMsg]
    }));

    setMatches(prev =>
      prev.map(m =>
        m.id === selectedConversationId
          ? { ...m, lastMessage: messageInput.trim(), lastMessageTime: 'Just now' }
          : m
      )
    );

    setMessageInput('');
  };

  const currentProfile = profiles.filter(p => !p.isSuspended && !p.isProfileHidden)[activeCardIndex % profiles.length];

  return (
    <div
      dir={isAr ? 'rtl' : 'ltr'}
      className="min-h-screen bg-[#FAF7F2] text-[#231E1E] flex flex-col font-sans"
    >
      {/* Top Application Header */}
      <header className="sticky top-0 z-40 bg-[#FAF7F2]/95 backdrop-blur-md border-b border-[#E6DEC0] shadow-sm">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 h-18 flex items-center justify-between">
          {/* Logo / Wordmark */}
          <div
            onClick={() => setActiveTab('landing')}
            className="flex items-center gap-3 cursor-pointer select-none group"
          >
            <div className="w-10 h-10 rounded-full bg-gradient-to-tr from-[#6B1D2F] to-[#C5A059] flex items-center justify-center text-white font-bold text-xl shadow-md group-hover:scale-105 transition-transform">
              م
            </div>
            <div>
              <div className="flex items-center gap-2">
                <span className="font-serif tracking-widest text-2xl font-bold text-[#6B1D2F]">
                  WIDAD
                </span>
                <span className="font-arabic text-xl font-bold text-[#C5A059]">
                  • مودة
                </span>
              </div>
              <p className="text-[11px] text-[#5E5452] tracking-wider hidden sm:block">
                {isAr ? 'حب ذو معنى. ومستقبل يدوم.' : 'Meaningful Love. A Lasting Future.'}
              </p>
            </div>
          </div>

          {/* Navigation Links (Desktop) */}
          <nav className="hidden md:flex items-center gap-1 lg:gap-2">
            {[
              { id: 'discovery', labelEn: 'Discover', labelAr: 'استكشاف', icon: Compass },
              { id: 'matches', labelEn: 'Matches & Chat', labelAr: 'المحادثات', icon: MessageCircle, badge: matches.length },
              { id: 'membership', labelEn: 'Membership', labelAr: 'العضوية', icon: CreditCard },
              { id: 'safety', labelEn: 'Safety & Trust', labelAr: 'الأمان', icon: Shield },
              { id: 'profile', labelEn: 'My Profile', labelAr: 'ملفي', icon: User },
              { id: 'admin', labelEn: 'Admin Panel', labelAr: 'الإدارة', icon: Settings },
            ].map((item) => {
              const IconComp = item.icon;
              const isActive = activeTab === item.id;
              return (
                <button
                  key={item.id}
                  onClick={() => setActiveTab(item.id as any)}
                  className={`flex items-center gap-1.5 px-3 py-2 rounded-xl text-sm font-semibold transition-all ${
                    isActive
                      ? 'bg-[#6B1D2F] text-white shadow-sm'
                      : 'text-[#5E5452] hover:text-[#231E1E] hover:bg-[#F3ECE0]'
                  }`}
                >
                  <IconComp className="w-4 h-4" />
                  <span>{isAr ? item.labelAr : item.labelEn}</span>
                  {item.badge ? (
                    <span className="bg-[#C5A059] text-[#231E1E] text-xs px-1.5 py-0.2 rounded-full font-bold ml-1">
                      {item.badge}
                    </span>
                  ) : null}
                </button>
              );
            })}
          </nav>

          {/* Header Action Items */}
          <div className="flex items-center gap-3">
            {/* The primary Upgrade to Pro button */}
            <button
              onClick={handleUpgradeToPro}
              className="flex items-center gap-1.5 bg-[#6B1D2F] hover:bg-[#450F1D] text-[#E5C887] px-4 py-2 rounded-xl font-bold text-xs sm:text-sm shadow-md transition-all hover:scale-105 active:scale-95"
            >
              <Sparkles className="w-4 h-4 text-[#C5A059]" />
              <span>{isAr ? 'ترقية للتميز' : 'Upgrade to Pro'}</span>
              <ExternalLink className="w-3.5 h-3.5 text-[#E5C887]/80" />
            </button>

            {/* Language Selector */}
            <div className="flex bg-[#F3ECE0] p-1 rounded-xl border border-[#E6DEC0] text-xs font-bold">
              <button
                onClick={() => setLang('en')}
                className={`px-2.5 py-1 rounded-lg transition-colors ${
                  lang === 'en' ? 'bg-[#6B1D2F] text-white' : 'text-[#5E5452]'
                }`}
              >
                EN
              </button>
              <button
                onClick={() => setLang('ar')}
                className={`px-2.5 py-1 rounded-lg transition-colors ${
                  lang === 'ar' ? 'bg-[#6B1D2F] text-white' : 'text-[#5E5452]'
                }`}
              >
                العربية
              </button>
            </div>
          </div>
        </div>
      </header>

      {/* Main Container Views */}
      <main className="flex-1 pb-16 md:pb-6">
        {/* LANDING PAGE VIEW */}
        {activeTab === 'landing' && (
          <div>
            {/* Hero Section */}
            <div className="relative overflow-hidden bg-gradient-to-b from-[#FAF7F2] to-[#F3ECE0] py-16 sm:py-24 border-b border-[#E6DEC0]">
              <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 grid md:grid-cols-2 gap-12 items-center">
                <div className="space-y-6">
                  <div className="inline-flex items-center gap-2 bg-[#FBECEF] text-[#6B1D2F] px-3.5 py-1.5 rounded-full text-xs font-bold border border-[#6B1D2F]/20">
                    <Shield className="w-3.5 h-3.5" />
                    <span>{isAr ? 'منصة الزواج الجاد الموثقة للبالغين (١٨+)' : 'Verified Serious Relationship Platform (18+)'}</span>
                  </div>
                  <h1 className="text-4xl sm:text-5xl lg:text-6xl font-bold font-serif text-[#231E1E] leading-tight">
                    {isAr ? (
                      <>
                        حب ذو معنى.<br />
                        <span className="text-[#6B1D2F]">ومستقبل يدوم.</span>
                      </>
                    ) : (
                      <>
                        Meaningful Love.<br />
                        <span className="text-[#6B1D2F]">A Lasting Future.</span>
                      </>
                    )}
                  </h1>
                  <p className="text-lg text-[#5E5452] max-w-xl leading-relaxed">
                    {isAr
                      ? 'مودة هي المنصة الأولى المصممة خصيصاً للبالغين العرب الباحثين عن شريك حياة حقيقي وعلاقة جادة قائمة على التوافق القيمي والاحترام المتبادل.'
                      : 'Widad is the premier platform crafted for Arab adults seeking intentional relationships, shared values, and enduring marriage.'}
                  </p>
                  <div className="flex flex-col sm:flex-row gap-4 pt-2">
                    <button
                      onClick={() => setActiveTab('discovery')}
                      className="flex items-center justify-center gap-2 bg-[#6B1D2F] hover:bg-[#450F1D] text-white px-8 py-3.5 rounded-2xl font-bold text-base shadow-lg transition-transform hover:-translate-y-0.5"
                    >
                      <Heart className="w-5 h-5 text-[#E5C887] fill-current" />
                      <span>{isAr ? 'ابدأ الاستكشاف الآن' : 'Start Exploring Profiles'}</span>
                    </button>
                    <button
                      onClick={handleUpgradeToPro}
                      className="flex items-center justify-center gap-2 bg-white hover:bg-[#F3ECE0] text-[#231E1E] px-8 py-3.5 rounded-2xl font-bold text-base border border-[#C5A059] shadow-sm transition-transform hover:-translate-y-0.5"
                    >
                      <Sparkles className="w-5 h-5 text-[#C5A059]" />
                      <span>{isAr ? 'ترقية العضوية ($9.99)' : 'Upgrade to Pro ($9.99)'}</span>
                    </button>
                  </div>
                </div>

                {/* Hero Showcase Card */}
                <div className="relative">
                  <div className="w-full max-w-md mx-auto aspect-[4/5] rounded-3xl overflow-hidden shadow-2xl border-4 border-white relative group">
                    <img
                      src="https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=800&auto=format&fit=crop&q=80"
                      alt="Widad Featured Profile"
                      className="w-full h-full object-cover group-hover:scale-105 transition-transform duration-500"
                    />
                    <div className="absolute inset-0 bg-gradient-to-t from-[#231E1E] via-transparent to-transparent opacity-90" />
                    <div className="absolute bottom-6 left-6 right-6 text-white space-y-1">
                      <div className="flex items-center justify-between">
                        <h3 className="text-2xl font-bold">Layla Al-Hashimi, 28</h3>
                        <span className="bg-[#6B1D2F] text-[#E5C887] text-xs font-bold px-2.5 py-1 rounded-full flex items-center gap-1">
                          <Check className="w-3.5 h-3.5" /> {isAr ? 'موثق' : 'Verified'}
                        </span>
                      </div>
                      <p className="text-sm text-[#F3ECE0]">Abu Dhabi, UAE • University Lecturer</p>
                      <p className="text-xs text-[#E5C887] font-semibold pt-1">
                        {isAr ? 'الهدف: الزواج وتكوين أسرة صالحة' : 'Goal: Marriage & Building a Household'}
                      </p>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            {/* Three Step How it Works */}
            <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-16">
              <div className="text-center max-w-2xl mx-auto mb-12">
                <h2 className="text-3xl font-bold font-serif text-[#6B1D2F]">
                  {isAr ? 'ثلاث خطوات نحو شريك العمر' : 'Three Intentional Steps'}
                </h2>
                <p className="text-[#5E5452] mt-2">
                  {isAr ? 'تعارف جاد قائم على التوافق المتبادل وصيانة الكرامة' : 'Honorable, intentional matchmaking rooted in dignity'}
                </p>
              </div>

              <div className="grid md:grid-cols-3 gap-8">
                {[
                  {
                    num: '1',
                    titleEn: 'Create a Verified Serious Profile',
                    titleAr: 'ملف تعريفي موثق وحقيقي',
                    descEn: 'Age 18+ verified. Express your life goals, cultural background, and values honestly.',
                    descAr: 'التحقق من سن الرشد (١٨+)، وإبراز أهدافك وقيمك وشخصيتك بصدق ووضوح.'
                  },
                  {
                    num: '2',
                    titleEn: 'Discover Mutual Compatibility',
                    titleAr: 'اكتشاف التوافق المتبادل',
                    descEn: 'Filter by country, language, and relationship horizon with real compatibility insight.',
                    descAr: 'فلاتر ذكية حسب الدولة واللغات والأهداف الحياتية ونسبة التوافق المشترك.'
                  },
                  {
                    num: '3',
                    titleEn: 'Direct Respectful Connection',
                    titleAr: 'تواصل محترم وآمن',
                    descEn: 'Chat unlocks only upon mutual match. Built-in blocking and safety moderation.',
                    descAr: 'تبدأ المحادثات فقط عند الإعجاب المتبادل مع حماية الخصوصية ومراقبة الأمان.'
                  }
                ].map((step, idx) => (
                  <div key={idx} className="bg-white p-8 rounded-2xl border border-[#E6DEC0] shadow-sm">
                    <div className="w-12 h-12 rounded-2xl bg-[#F8F3E5] border border-[#C5A059] text-[#6B1D2F] font-bold text-xl flex items-center justify-center mb-6">
                      {step.num}
                    </div>
                    <h3 className="text-xl font-bold text-[#231E1E] mb-2">
                      {isAr ? step.titleAr : step.titleEn}
                    </h3>
                    <p className="text-[#5E5452] text-sm leading-relaxed">
                      {isAr ? step.descAr : step.descEn}
                    </p>
                  </div>
                ))}
              </div>
            </div>

            {/* Pricing Section Preview */}
            <div className="bg-[#F3ECE0] py-16 border-t border-[#E6DEC0]">
              <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 text-center">
                <h2 className="text-3xl font-bold font-serif text-[#6B1D2F] mb-4">
                  {isAr ? 'باقات العضوية المتميزة' : 'Transparent Membership Plans'}
                </h2>
                <p className="text-[#5E5452] max-w-xl mx-auto mb-10">
                  {isAr ? 'عضوية مجانية متكاملة، أو ترقية مميزة لتسريع اكتشاف الشريك المتوافق' : 'Free to browse and match, with premium options for accelerated discovery'}
                </p>

                <div className="grid md:grid-cols-3 gap-8 max-w-5xl mx-auto">
                  {MEMBERSHIP_PLANS.slice(0, 3).map((plan) => (
                    <div
                      key={plan.id}
                      className={`bg-white p-8 rounded-3xl border ${
                        plan.isPopular ? 'border-2 border-[#C5A059] shadow-xl relative' : 'border-[#E6DEC0] shadow-sm'
                      } flex flex-col justify-between`}
                    >
                      {plan.isPopular && (
                        <span className="absolute -top-3.5 left-1/2 -translate-x-1/2 bg-[#C5A059] text-[#231E1E] text-xs font-bold px-4 py-1 rounded-full uppercase tracking-wider">
                          {isAr ? 'الأكثر طلباً' : 'Most Popular'}
                        </span>
                      )}
                      <div>
                        <h3 className="text-xl font-bold text-[#231E1E]">{isAr ? plan.nameAr : plan.nameEn}</h3>
                        <p className="text-xs text-[#5E5452] mt-1">{isAr ? plan.intervalAr : plan.intervalEn}</p>
                        <div className="my-6">
                          <span className="text-4xl font-bold text-[#6B1D2F]">${plan.priceUsd}</span>
                          <span className="text-xs text-[#5E5452] ml-1">/ USD</span>
                        </div>
                        <ul className="text-left space-y-3 text-sm text-[#5E5452] mb-8">
                          {(isAr ? plan.featuresAr : plan.featuresEn).map((f, i) => (
                            <li key={i} className="flex items-center gap-2">
                              <Check className="w-4 h-4 text-[#6B1D2F] shrink-0" />
                              <span>{f}</span>
                            </li>
                          ))}
                        </ul>
                      </div>
                      <button
                        onClick={handleUpgradeToPro}
                        className={`w-full py-3.5 rounded-xl font-bold text-sm transition-all flex items-center justify-center gap-2 ${
                          plan.priceUsd > 0
                            ? 'bg-[#6B1D2F] hover:bg-[#450F1D] text-white shadow-md hover:scale-[1.02]'
                            : 'bg-[#F3ECE0] text-[#231E1E] hover:bg-[#E6DEC0]'
                        }`}
                      >
                        <Sparkles className="w-4 h-4 text-[#E5C887]" />
                        <span>{plan.priceUsd > 0 ? (isAr ? `ترقية عبر Paystack ($${plan.priceUsd})` : `Upgrade to Pro ($${plan.priceUsd})`) : (isAr ? 'الباقة الحالية' : 'Free Forever')}</span>
                      </button>
                    </div>
                  ))}
                </div>
              </div>
            </div>

            {/* FAQs Accordion */}
            <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-16">
              <h2 className="text-3xl font-bold font-serif text-[#6B1D2F] text-center mb-8">
                {isAr ? 'الأسئلة الشائعة' : 'Frequently Asked Questions'}
              </h2>
              <div className="space-y-4">
                {[
                  {
                    qEn: 'How does Widad protect user privacy and photos?',
                    qAr: 'كيف تحمي منصة مودة خصوصية المستخدمين وصورهم؟',
                    aEn: 'We strictly protect exact dates of birth, full street addresses, and contact numbers. Direct chat unlocks exclusively upon mutual matching.',
                    aAr: 'نحمي تاريخ الميلاد الدقيق والعناوين التفصيلية وأرقام الهواتف. لا تفتح المحادثات إلا بالتوافق المتبادل.'
                  },
                  {
                    qEn: 'Can I pay securely with Paystack?',
                    qAr: 'هل الدفع آمن عبر بوابة Paystack؟',
                    aEn: 'Yes! All premium memberships are processed directly through authorized, PCI-DSS compliant Paystack payment endpoints.',
                    aAr: 'نعم! تتم معالجة جميع المدفوعات والاشتراكات بأمان تام عبر بوابة Paystack المعتمدة والمشفرة.'
                  },
                  {
                    qEn: 'What is the minimum age to register?',
                    qAr: 'ما هو الحد الأدنى للسن المسموح به للتسجيل؟',
                    aEn: 'All members must be at least 18 years old. Underage accounts are strictly prohibited and immediately banned.',
                    aAr: 'يشترط أن يكون عمر العضو ١٨ عاماً فأكثر. الحسابات دون السن القانوني تُحظر نهائياً.'
                  }
                ].map((faq, idx) => (
                  <div key={idx} className="bg-white rounded-2xl border border-[#E6DEC0] overflow-hidden">
                    <button
                      onClick={() => setExpandedFaq(expandedFaq === idx ? null : idx)}
                      className="w-full p-5 text-left font-bold text-base flex justify-between items-center text-[#231E1E]"
                    >
                      <span>{isAr ? faq.qAr : faq.qEn}</span>
                      {expandedFaq === idx ? <ChevronUp className="w-5 h-5 text-[#6B1D2F]" /> : <ChevronDown className="w-5 h-5 text-[#6B1D2F]" />}
                    </button>
                    {expandedFaq === idx && (
                      <div className="px-5 pb-5 text-sm text-[#5E5452] border-t border-[#F3ECE0] pt-3 leading-relaxed">
                        {isAr ? faq.aAr : faq.aEn}
                      </div>
                    )}
                  </div>
                ))}
              </div>
            </div>
          </div>
        )}

        {/* DISCOVERY VIEW */}
        {activeTab === 'discovery' && currentProfile && (
          <div className="max-w-2xl mx-auto px-4 py-8">
            {/* Header info */}
            <div className="flex justify-between items-center mb-6">
              <div>
                <h2 className="text-2xl font-bold font-serif text-[#6B1D2F]">
                  {isAr ? 'الاستكشاف والتوافق الهادف' : 'Intentional Discovery'}
                </h2>
                <p className="text-xs text-[#5E5452]">
                  {isAr ? 'ملفات موثقة لراغبي الارتباط الجاد' : 'Verified profiles seeking lasting partnership'}
                </p>
              </div>
              <button
                onClick={handleUpgradeToPro}
                className="bg-[#F8F3E5] hover:bg-[#F3ECE0] text-[#6B1D2F] border border-[#C5A059] px-3.5 py-1.5 rounded-xl text-xs font-bold flex items-center gap-1.5"
              >
                <Sparkles className="w-3.5 h-3.5 text-[#C5A059]" />
                <span>{isAr ? 'ترقية ($9.99)' : 'Upgrade to Pro'}</span>
              </button>
            </div>

            {/* Profile Card */}
            <div className="bg-white rounded-3xl overflow-hidden shadow-xl border border-[#E6DEC0]">
              <div className="relative aspect-[4/4.5] w-full bg-[#231E1E]">
                <img
                  src={currentProfile.avatarUrl}
                  alt={currentProfile.displayName}
                  className="w-full h-full object-cover"
                />
                <div className="absolute inset-0 bg-gradient-to-t from-black/80 via-transparent to-black/30" />
                
                <div className="absolute top-4 right-4 flex gap-2">
                  <span className="bg-[#6B1D2F]/90 backdrop-blur-sm text-white px-3 py-1 rounded-full text-xs font-bold flex items-center gap-1">
                    <Check className="w-3.5 h-3.5 text-[#E5C887]" /> {isAr ? 'موثق' : 'Verified'}
                  </span>
                </div>

                <div className="absolute bottom-6 left-6 right-6 text-white">
                  <div className="flex items-baseline gap-2">
                    <h3 className="text-3xl font-bold">{currentProfile.displayName}</h3>
                    <span className="text-2xl text-[#E5C887] font-semibold">{currentProfile.calculatedAge}</span>
                  </div>
                  <p className="text-sm text-[#F3ECE0] mt-1">{currentProfile.city}, {currentProfile.country}</p>
                </div>
              </div>

              {/* Card Details */}
              <div className="p-6 space-y-5">
                {/* Compatibility badge */}
                {(() => {
                  const comp = calculateCompatibility(currentUser, currentProfile);
                  return (
                    <div className="flex items-center justify-between bg-[#F8F3E5] p-3 rounded-2xl border border-[#C5A059]/40">
                      <div className="flex items-center gap-2">
                        <Heart className="w-5 h-5 text-[#6B1D2F] fill-current" />
                        <span className="font-bold text-[#6B1D2F] text-sm">
                          {comp.score}% {isAr ? 'نسبة التوافق المشترك' : 'Compatibility Match'}
                        </span>
                      </div>
                      <span className="text-xs text-[#5E5452]">
                        {comp.commonInterests.length} {isAr ? 'اهتمامات مشتركة' : 'shared interests'}
                      </span>
                    </div>
                  );
                })()}

                {/* Profession & Goal */}
                <div className="grid grid-cols-2 gap-3 text-sm">
                  <div className="bg-[#FAF7F2] p-3 rounded-xl border border-[#E6DEC0]">
                    <span className="text-xs text-[#5E5452] block">{isAr ? 'المهنة' : 'Profession'}</span>
                    <span className="font-semibold text-[#231E1E]">{currentProfile.profession}</span>
                  </div>
                  <div className="bg-[#FAF7F2] p-3 rounded-xl border border-[#E6DEC0]">
                    <span className="text-xs text-[#5E5452] block">{isAr ? 'الهدف' : 'Relationship Intent'}</span>
                    <span className="font-semibold text-[#6B1D2F] text-xs sm:text-sm">
                      {isAr ? 'الزواج وتكوين أسرة' : 'Marriage within 1-2 yrs'}
                    </span>
                  </div>
                </div>

                {/* Bio */}
                <div>
                  <h4 className="text-xs font-bold text-[#5E5452] uppercase tracking-wider mb-1">
                    {isAr ? 'نبذة عن الشخصية' : 'About'}
                  </h4>
                  <p className="text-sm text-[#231E1E] leading-relaxed">{currentProfile.bio}</p>
                </div>

                {/* Interests tags */}
                <div>
                  <h4 className="text-xs font-bold text-[#5E5452] uppercase tracking-wider mb-2">
                    {isAr ? 'الاهتمامات' : 'Interests'}
                  </h4>
                  <div className="flex flex-wrap gap-2">
                    {currentProfile.interests.map((interest, idx) => (
                      <span
                        key={idx}
                        className="bg-[#F3ECE0] text-[#231E1E] text-xs px-3 py-1.5 rounded-lg font-medium"
                      >
                        {interest}
                      </span>
                    ))}
                  </div>
                </div>

                {/* Action buttons (Pass / Super Like / Like) */}
                <div className="flex items-center justify-center gap-6 pt-4 border-t border-[#F3ECE0]">
                  <button
                    onClick={handlePass}
                    className="w-14 h-14 rounded-full bg-white border border-[#E6DEC0] shadow-md flex items-center justify-center text-red-500 hover:bg-red-50 hover:scale-110 transition-transform"
                    title={isAr ? 'تخطي' : 'Pass'}
                  >
                    <X className="w-7 h-7" />
                  </button>
                  <button
                    onClick={handleUpgradeToPro}
                    className="w-12 h-12 rounded-full bg-[#F8F3E5] border border-[#C5A059] shadow-md flex items-center justify-center text-[#C5A059] hover:bg-[#F3ECE0] hover:scale-110 transition-transform"
                    title={isAr ? 'إعجاب مميز' : 'Super Intent'}
                  >
                    <Star className="w-6 h-6 fill-current" />
                  </button>
                  <button
                    onClick={() => handleLike(currentProfile)}
                    className="w-16 h-16 rounded-full bg-[#6B1D2F] shadow-lg flex items-center justify-center text-[#E5C887] hover:bg-[#450F1D] hover:scale-110 transition-transform"
                    title={isAr ? 'إعجاب' : 'Like'}
                  >
                    <Heart className="w-8 h-8 fill-current" />
                  </button>
                </div>
              </div>
            </div>
          </div>
        )}

        {/* MATCHES & CHAT VIEW */}
        {activeTab === 'matches' && (
          <div className="max-w-4xl mx-auto px-4 py-8">
            <h2 className="text-2xl font-bold font-serif text-[#6B1D2F] mb-6">
              {isAr ? 'المطابقات والمحادثات المباشرة' : 'Mutual Matches & Private Chats'}
            </h2>

            <div className="grid md:grid-cols-3 gap-6 bg-white rounded-3xl border border-[#E6DEC0] shadow-lg overflow-hidden min-h-[500px]">
              {/* Match list sidebar */}
              <div className="border-r border-[#E6DEC0] p-4 bg-[#FAF7F2]/50">
                <h3 className="text-xs font-bold text-[#5E5452] uppercase tracking-wider mb-3">
                  {isAr ? 'المتطابقون معك' : 'Active Matches'} ({matches.length})
                </h3>
                <div className="space-y-2">
                  {matches.map((m) => {
                    const partner = profiles.find(p => p.id === (m.user1Id === currentUser.id ? m.user2Id : m.user1Id)) || profiles[0];
                    const isSelected = selectedConversationId === m.id;
                    return (
                      <div
                        key={m.id}
                        onClick={() => setSelectedConversationId(m.id)}
                        className={`flex items-center gap-3 p-3 rounded-2xl cursor-pointer transition-colors ${
                          isSelected ? 'bg-[#6B1D2F] text-white' : 'hover:bg-[#F3ECE0] text-[#231E1E]'
                        }`}
                      >
                        <img
                          src={partner.avatarUrl}
                          alt={partner.displayName}
                          className="w-12 h-12 rounded-full object-cover border-2 border-[#C5A059]"
                        />
                        <div className="flex-1 min-w-0">
                          <h4 className="font-bold text-sm truncate">{partner.displayName}</h4>
                          <p className={`text-xs truncate ${isSelected ? 'text-white/80' : 'text-[#5E5452]'}`}>
                            {m.lastMessage || 'New match!'}
                          </p>
                        </div>
                      </div>
                    );
                  })}
                </div>
              </div>

              {/* Chat window */}
              <div className="md:col-span-2 flex flex-col justify-between p-6">
                {selectedConversationId ? (
                  <>
                    {/* Chat header */}
                    <div className="flex justify-between items-center pb-4 border-b border-[#E6DEC0]">
                      <div>
                        <h3 className="font-bold text-base text-[#231E1E]">Layla Al-Hashimi</h3>
                        <span className="text-xs text-green-600 font-semibold">• {isAr ? 'متصلة الآن' : 'Active now'}</span>
                      </div>
                      <button
                        onClick={handleUpgradeToPro}
                        className="text-xs bg-[#F8F3E5] text-[#6B1D2F] px-3 py-1 rounded-xl font-bold border border-[#C5A059]"
                      >
                        {isAr ? 'ترقية ($9.99)' : 'Upgrade to Pro'}
                      </button>
                    </div>

                    {/* Messages feed */}
                    <div className="flex-1 py-6 space-y-4 overflow-y-auto max-h-[350px]">
                      {(messages[selectedConversationId] || []).map((msg) => {
                        const isMe = msg.senderId === currentUser.id;
                        return (
                          <div
                            key={msg.id}
                            className={`flex flex-col ${isMe ? 'items-end' : 'items-start'}`}
                          >
                            <div
                              className={`max-w-xs sm:max-w-md px-4 py-3 rounded-2xl text-sm ${
                                isMe
                                  ? 'bg-[#6B1D2F] text-white rounded-br-none'
                                  : 'bg-[#F3ECE0] text-[#231E1E] rounded-bl-none'
                              }`}
                            >
                              {msg.text}
                            </div>
                            <span className="text-[10px] text-[#5E5452] mt-1">{msg.timeFormatted}</span>
                          </div>
                        );
                      })}
                    </div>

                    {/* Chat input */}
                    <div className="pt-4 border-t border-[#E6DEC0] flex gap-2">
                      <input
                        type="text"
                        value={messageInput}
                        onChange={(e) => setMessageInput(e.target.value)}
                        onKeyDown={(e) => e.key === 'Enter' && handleSendMessage()}
                        placeholder={isAr ? 'اكتب رسالة محترمة...' : 'Type a respectful message...'}
                        className="flex-1 bg-[#FAF7F2] border border-[#E6DEC0] rounded-xl px-4 py-2.5 text-sm focus:outline-none focus:ring-2 focus:ring-[#6B1D2F]"
                      />
                      <button
                        onClick={handleSendMessage}
                        className="bg-[#6B1D2F] hover:bg-[#450F1D] text-white px-5 rounded-xl flex items-center justify-center transition-colors"
                      >
                        <Send className="w-4 h-4" />
                      </button>
                    </div>
                  </>
                ) : (
                  <div className="flex-1 flex flex-col items-center justify-center text-center p-8 text-[#5E5452]">
                    <MessageCircle className="w-12 h-12 text-[#C5A059] mb-3" />
                    <p className="font-bold">{isAr ? 'اختر محادثة للبدء' : 'Select a match to start chatting'}</p>
                    <p className="text-xs mt-1">{isAr ? 'المحادثات مشفرة ومحمية بأعلى معايير الخصوصية' : 'Encrypted & protected private conversations'}</p>
                  </div>
                )}
              </div>
            </div>
          </div>
        )}

        {/* MEMBERSHIP & PRICING VIEW */}
        {activeTab === 'membership' && (
          <div className="max-w-6xl mx-auto px-4 py-12">
            <div className="text-center max-w-2xl mx-auto mb-12">
              <h2 className="text-3xl font-bold font-serif text-[#6B1D2F]">
                {isAr ? 'باقات عضوية مودة' : 'Widad Membership & Upgrades'}
              </h2>
              <p className="text-[#5E5452] mt-2">
                {isAr ? 'اختر الباقة المناسبة للوصول لكافة الميزات المتقدمة وتسهيل العثور على شريك حياتك' : 'Unlock full discovery filters, priority visibility, and unlimited respectful likes'}
              </p>
            </div>

            <div className="grid md:grid-cols-4 gap-6">
              {MEMBERSHIP_PLANS.map((plan) => (
                <div
                  key={plan.id}
                  className={`bg-white rounded-3xl p-6 border ${
                    plan.isPopular ? 'border-2 border-[#C5A059] shadow-xl relative' : 'border-[#E6DEC0] shadow-sm'
                  } flex flex-col justify-between`}
                >
                  {plan.isPopular && (
                    <span className="absolute -top-3 left-1/2 -translate-x-1/2 bg-[#C5A059] text-[#231E1E] text-[10px] font-bold px-3 py-1 rounded-full uppercase tracking-wider">
                      {isAr ? 'الأكثر طلباً' : 'Popular'}
                    </span>
                  )}
                  <div>
                    <h3 className="text-lg font-bold text-[#231E1E]">{isAr ? plan.nameAr : plan.nameEn}</h3>
                    <p className="text-xs text-[#5E5452]">{isAr ? plan.intervalAr : plan.intervalEn}</p>
                    <div className="my-5">
                      <span className="text-3xl font-bold text-[#6B1D2F]">${plan.priceUsd}</span>
                      <span className="text-xs text-[#5E5452] ml-1">USD</span>
                    </div>
                    <ul className="space-y-2.5 text-xs text-[#5E5452] mb-6">
                      {(isAr ? plan.featuresAr : plan.featuresEn).map((f, i) => (
                        <li key={i} className="flex items-start gap-2">
                          <Check className="w-3.5 h-3.5 text-[#6B1D2F] shrink-0 mt-0.5" />
                          <span>{f}</span>
                        </li>
                      ))}
                    </ul>
                  </div>

                  {/* Upgrade button: opens Paystack link */}
                  <button
                    onClick={handleUpgradeToPro}
                    className={`w-full py-3 rounded-xl font-bold text-xs flex items-center justify-center gap-2 transition-all ${
                      plan.priceUsd > 0
                        ? 'bg-[#6B1D2F] hover:bg-[#450F1D] text-white shadow-md hover:scale-[1.02]'
                        : 'bg-[#F3ECE0] text-[#231E1E]'
                    }`}
                  >
                    <Sparkles className="w-3.5 h-3.5 text-[#E5C887]" />
                    <span>
                      {plan.priceUsd === 9.99
                        ? (isAr ? 'ترقية - $9.99 شهرياً' : 'Monthly $9.99 - Upgrade')
                        : plan.priceUsd > 0
                        ? (isAr ? `ترقية (${plan.priceUsd}$)` : `Upgrade ($${plan.priceUsd})`)
                        : (isAr ? 'العضوية الحالية' : 'Current Plan')}
                    </span>
                    {plan.priceUsd > 0 && <ExternalLink className="w-3 h-3 text-[#E5C887]" />}
                  </button>
                </div>
              ))}
            </div>

            {/* Paystack integration badge notice */}
            <div className="mt-12 bg-[#F8F3E5] border border-[#C5A059] p-6 rounded-2xl flex flex-col sm:flex-row items-center justify-between gap-4">
              <div className="flex items-center gap-3">
                <div className="w-10 h-10 rounded-xl bg-[#6B1D2F] text-[#E5C887] flex items-center justify-center font-bold">
                  P
                </div>
                <div>
                  <h4 className="font-bold text-sm text-[#231E1E]">
                    {isAr ? 'دفع آمن ومشفر عبر Paystack' : 'Official Paystack Payment Gateway'}
                  </h4>
                  <p className="text-xs text-[#5E5452]">
                    {isAr
                      ? 'جميع العمليات تتم عبر البوابة المشفرة الرسمية بدون رسوم خفية'
                      : 'Card and recurring subscriptions securely handled with PCI-DSS 256-bit encryption'}
                  </p>
                </div>
              </div>
              <button
                onClick={handleUpgradeToPro}
                className="bg-[#6B1D2F] text-white px-6 py-2.5 rounded-xl font-bold text-xs shadow-md hover:bg-[#450F1D] flex items-center gap-2"
              >
                <span>{isAr ? 'فتح صفحة الدفع (Paystack)' : 'Open Paystack Checkout'}</span>
                <ExternalLink className="w-3.5 h-3.5" />
              </button>
            </div>
          </div>
        )}

        {/* ADMIN DASHBOARD VIEW */}
        {activeTab === 'admin' && (
          <div className="max-w-6xl mx-auto px-4 py-8">
            <div className="flex justify-between items-center mb-8">
              <div>
                <h2 className="text-2xl font-bold font-serif text-[#6B1D2F]">
                  {isAr ? 'لوحة تحكم وتأسيس منصة مودة' : 'Founder & Admin Control Center'}
                </h2>
                <p className="text-xs text-[#5E5452]">
                  {isAr ? 'مراقبة الأعضاء، التحقق من السن، مراجعة البلاغات وإيرادات Paystack' : 'Manage members, age compliance, safety reports & Paystack ledger'}
                </p>
              </div>
            </div>

            {/* Stat Cards */}
            <div className="grid grid-cols-2 md:grid-cols-4 gap-4 mb-8">
              {[
                { title: isAr ? 'إجمالي الأعضاء' : 'Total Members', value: profiles.length + 1, color: 'text-[#6B1D2F]' },
                { title: isAr ? 'المطابقات المتبادلة' : 'Mutual Matches', value: matches.length, color: 'text-[#C5A059]' },
                { title: isAr ? 'بلاغات الأمان' : 'Open Reports', value: reports.filter(r => r.status === 'OPEN').length, color: 'text-red-600' },
                { title: isAr ? 'الإيرادات المقدرة' : 'Revenue', value: '$124.90', color: 'text-green-700' },
              ].map((stat, i) => (
                <div key={i} className="bg-white p-5 rounded-2xl border border-[#E6DEC0] shadow-sm">
                  <span className="text-xs text-[#5E5452] block">{stat.title}</span>
                  <span className={`text-2xl font-bold ${stat.color} mt-1 block`}>{stat.value}</span>
                </div>
              ))}
            </div>

            {/* Admin Tabs */}
            <div className="flex gap-2 border-b border-[#E6DEC0] mb-6 pb-2">
              <button
                onClick={() => setAdminTab('users')}
                className={`px-4 py-2 rounded-xl text-xs font-bold ${adminTab === 'users' ? 'bg-[#6B1D2F] text-white' : 'text-[#5E5452]'}`}
              >
                {isAr ? 'الأعضاء' : 'Users Directory'}
              </button>
              <button
                onClick={() => setAdminTab('reports')}
                className={`px-4 py-2 rounded-xl text-xs font-bold ${adminTab === 'reports' ? 'bg-[#6B1D2F] text-white' : 'text-[#5E5452]'}`}
              >
                {isAr ? 'بلاغات الأمان' : 'Safety Reports'} ({reports.length})
              </button>
            </div>

            {adminTab === 'users' && (
              <div className="bg-white rounded-2xl border border-[#E6DEC0] overflow-hidden">
                <div className="p-4 border-b border-[#E6DEC0]">
                  <input
                    type="text"
                    value={adminSearch}
                    onChange={(e) => setAdminSearch(e.target.value)}
                    placeholder={isAr ? 'بحث بالاسم أو الدولة...' : 'Search by name or country...'}
                    className="w-full bg-[#FAF7F2] border border-[#E6DEC0] rounded-xl px-4 py-2 text-sm"
                  />
                </div>
                <div className="divide-y divide-[#E6DEC0]">
                  {profiles.map((p) => (
                    <div key={p.id} className="p-4 flex items-center justify-between">
                      <div className="flex items-center gap-3">
                        <img src={p.avatarUrl} alt={p.displayName} className="w-10 h-10 rounded-full object-cover" />
                        <div>
                          <h4 className="font-bold text-sm">{p.displayName}, {p.calculatedAge}</h4>
                          <p className="text-xs text-[#5E5452]">{p.city}, {p.country} • {p.profession}</p>
                        </div>
                      </div>
                      <div className="flex gap-2">
                        <button
                          onClick={() => {
                            setProfiles(prev => prev.map(u => u.id === p.id ? { ...u, isSuspended: !u.isSuspended } : u));
                          }}
                          className={`text-xs px-3 py-1.5 rounded-lg font-bold ${
                            p.isSuspended ? 'bg-green-100 text-green-700' : 'bg-red-100 text-red-700'
                          }`}
                        >
                          {p.isSuspended ? (isAr ? 'إلغاء التوقيف' : 'Reinstate') : (isAr ? 'توقيف' : 'Suspend')}
                        </button>
                      </div>
                    </div>
                  ))}
                </div>
              </div>
            )}

            {adminTab === 'reports' && (
              <div className="space-y-4">
                {reports.map((r) => (
                  <div key={r.id} className="bg-white p-5 rounded-2xl border border-[#E6DEC0]">
                    <div className="flex justify-between items-center mb-2">
                      <h4 className="font-bold text-sm text-[#231E1E]">{r.reason}</h4>
                      <span className="text-xs bg-red-100 text-red-700 px-2 py-0.5 rounded-full font-bold">{r.status}</span>
                    </div>
                    <p className="text-xs text-[#5E5452]">Reported: <span className="font-semibold">{r.reportedUserName}</span></p>
                    <p className="text-sm text-[#231E1E] mt-2 bg-[#FAF7F2] p-3 rounded-xl border border-[#E6DEC0]">{r.details}</p>
                  </div>
                ))}
              </div>
            )}
          </div>
        )}

        {/* SAFETY & LEGAL VIEW */}
        {activeTab === 'safety' && (
          <div className="max-w-4xl mx-auto px-4 py-12 space-y-8">
            <div className="text-center">
              <h2 className="text-3xl font-bold font-serif text-[#6B1D2F]">
                {isAr ? 'مركز الأمان والخصوصية' : 'Safety, Trust & Compliance Center'}
              </h2>
              <p className="text-[#5E5452] mt-2">
                {isAr ? 'معايير الأمان ومكافحة الاحتيال لحماية كافة الأعضاء' : 'Zero tolerance for harassment, scams, or unverified intentions'}
              </p>
            </div>

            <div className="bg-white p-8 rounded-3xl border border-[#E6DEC0] space-y-6">
              <div className="flex items-center gap-3 text-[#6B1D2F]">
                <Shield className="w-8 h-8" />
                <h3 className="text-xl font-bold">
                  {isAr ? 'إرشادات الأمان ومكافحة الاحتيال' : 'Financial Safety & Scam Warning'}
                </h3>
              </div>
              <p className="text-sm text-[#231E1E] leading-relaxed">
                {isAr
                  ? '١. لا ترسل أموالاً أو حوالات تحت أي ظرف: أي مستخدم يطلب مساعدة مالية، رسوم سفر، استثمارات تجارية أو عملات رقمية هو محتال.\n٢. حافظ على سرية بياناتك الحساسة: لا تشارك رقم حسابك البنكي أو بطاقتك.\n٣. تواصل عبر منصة مودة للحفاظ على خصوصيتك وأمانك.'
                  : '1. Never send money or financial transfers under any circumstances. Anyone soliciting financial assistance, travel funds, or cryptocurrency investments is violating community rules.\n2. Keep your banking credentials and private residential address confidential.\n3. Keep initial conversations strictly on Widad.'}
              </p>
            </div>
          </div>
        )}

        {/* PROFILE VIEW */}
        {activeTab === 'profile' && (
          <div className="max-w-2xl mx-auto px-4 py-12">
            <div className="bg-white rounded-3xl p-8 border border-[#E6DEC0] shadow-sm text-center">
              <img
                src={currentUser.avatarUrl}
                alt={currentUser.displayName}
                className="w-24 h-24 rounded-full object-cover mx-auto border-4 border-[#C5A059] shadow-md"
              />
              <h3 className="text-2xl font-bold text-[#231E1E] mt-4">{currentUser.displayName}, {currentUser.calculatedAge}</h3>
              <p className="text-sm text-[#5E5452]">{currentUser.city}, {currentUser.country} • {currentUser.profession}</p>

              <div className="mt-6 flex justify-center gap-3">
                <button
                  onClick={handleUpgradeToPro}
                  className="bg-[#6B1D2F] text-[#E5C887] hover:bg-[#450F1D] px-6 py-2.5 rounded-xl text-sm font-bold flex items-center gap-2 shadow-md"
                >
                  <Sparkles className="w-4 h-4 text-[#C5A059]" />
                  <span>{isAr ? 'ترقية العضوية ($9.99)' : 'Upgrade to Pro ($9.99)'}</span>
                </button>
                <button
                  onClick={() => setActiveTab('admin')}
                  className="bg-[#F3ECE0] text-[#231E1E] hover:bg-[#E6DEC0] px-4 py-2.5 rounded-xl text-sm font-bold"
                >
                  {isAr ? 'لوحة الإدارة' : 'Admin Center'}
                </button>
              </div>

              <div className="mt-8 text-left border-t border-[#F3ECE0] pt-6 space-y-4">
                <div>
                  <h4 className="text-xs font-bold text-[#5E5452] uppercase tracking-wider">{isAr ? 'النبذة التعريفية' : 'About Me'}</h4>
                  <p className="text-sm text-[#231E1E] mt-1">{currentUser.bio}</p>
                </div>
              </div>
            </div>
          </div>
        )}
      </main>

      {/* Mutual Match Modal */}
      {matchedProfile && (
        <div className="fixed inset-0 z-50 bg-black/70 flex items-center justify-center p-4">
          <div className="bg-white rounded-3xl max-w-md w-full p-8 text-center border-2 border-[#C5A059] shadow-2xl relative">
            <h3 className="text-2xl font-bold font-serif text-[#6B1D2F]">
              {isAr ? 'توافق متبادل! مودة ومحبة' : "It's a Mutual Match!"}
            </h3>
            <p className="text-xs text-[#5E5452] mt-1">
              {isAr ? 'أعجب كلاكما بالآخر بنيّة جادة' : 'You both expressed mutual interest'}
            </p>

            <div className="flex items-center justify-center gap-4 my-6">
              <img src={currentUser.avatarUrl} alt="Me" className="w-20 h-20 rounded-full object-cover border-2 border-[#C5A059]" />
              <Heart className="w-8 h-8 text-[#6B1D2F] fill-current animate-pulse" />
              <img src={matchedProfile.avatarUrl} alt="Match" className="w-20 h-20 rounded-full object-cover border-2 border-[#C5A059]" />
            </div>

            <div className="space-y-3">
              <button
                onClick={() => {
                  setSelectedConversationId(`match_me_${matchedProfile.id}`);
                  setActiveTab('matches');
                  setMatchedProfile(null);
                }}
                className="w-full bg-[#6B1D2F] hover:bg-[#450F1D] text-white py-3.5 rounded-xl font-bold text-sm shadow-md"
              >
                {isAr ? 'بدء محادثة محترمة' : 'Start Respectful Conversation'}
              </button>
              <button
                onClick={() => setMatchedProfile(null)}
                className="w-full text-[#5E5452] py-2 text-xs font-semibold hover:text-[#231E1E]"
              >
                {isAr ? 'متابعة الاستكشاف' : 'Keep Exploring'}
              </button>
            </div>
          </div>
        </div>
      )}

      {/* Mobile Bottom Navigation Bar */}
      <div className="md:hidden fixed bottom-0 left-0 right-0 z-40 bg-white/95 backdrop-blur-md border-t border-[#E6DEC0] px-4 py-2 flex justify-around">
        {[
          { id: 'discovery', labelEn: 'Discover', labelAr: 'استكشاف', icon: Compass },
          { id: 'matches', labelEn: 'Matches', labelAr: 'المحادثات', icon: MessageCircle },
          { id: 'membership', labelEn: 'Upgrade', labelAr: 'العضوية', icon: CreditCard },
          { id: 'profile', labelEn: 'Profile', labelAr: 'ملفي', icon: User },
        ].map((item) => {
          const IconComp = item.icon;
          const isActive = activeTab === item.id;
          return (
            <button
              key={item.id}
              onClick={() => setActiveTab(item.id as any)}
              className={`flex flex-col items-center gap-1 text-[11px] font-semibold py-1 ${
                isActive ? 'text-[#6B1D2F]' : 'text-[#5E5452]'
              }`}
            >
              <IconComp className="w-5 h-5" />
              <span>{isAr ? item.labelAr : item.labelEn}</span>
            </button>
          );
        })}
      </div>
    </div>
  );
}
