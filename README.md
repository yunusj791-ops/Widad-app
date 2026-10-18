# Widad (مودة) — Web Application

## Meaningful Love. A Lasting Future. (حب ذو معنى. ومستقبل يدوم)

This is the responsive web application for **Widad (مودة)**, a serious dating and marriage platform for Arab adults.

### Features
- **Responsive Web UI**: Optimized for mobile phones, tablets, laptops, and desktop widescreen displays.
- **Bilingual & RTL**: One-tap switching between English (LTR) and Arabic (RTL) with complete native translations.
- **Paystack Integration**: Direct link to the official Paystack payment checkout (`https://paystack.shop/pay/q0bp-vzftt`).
- **Discovery & Matchmaking**: Intentional profile browsing, compatibility score engine, mutual match celebration modal.
- **Messaging**: Direct private conversations between mutual matches.
- **Safety Suite**: Fraud/scam warnings, block, unmatch, and user reporting.
- **Founder & Admin Dashboard**: Live metrics, user suspension, moderation queue.

---

### Local Development

```bash
cd web
npm install
npm run dev
```

The web app runs locally at `http://localhost:3000`.

---

### 1-Click Public Web Deployment

You can deploy this folder directly to any modern web hosting service for free:

#### 1. Vercel
```bash
npm install -g vercel
cd web
vercel
```

#### 2. Netlify
```bash
npm install -g netlify-cli
cd web
npm run build
netlify deploy --prod --dir=dist
```

#### 3. Firebase Hosting
```bash
npm run build
firebase deploy --only hosting
```
