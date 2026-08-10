# Smart Categorized App Drawer — Assumptions

Every place a value/behaviour was guessed because it can't be verified visually in this loop.
All of these are candidates for correction after the next screenshot.

## Visual values (spec hexes/dps, exact choice noted)

- **Card background**: resolved from the active Material/Monet neutral palette as a lighter
  surface than the drawer background.
- **Card shadow**: implemented with a 3dp elevation (Android elevation tint) rather than a custom
  painted soft shadow; a soft/diffuse custom shadow may need tuning once seen on screen.
- **Header text size**: spec said ~17–18sp; used **18sp**, weight **500 (medium)**, colour
  `#1F1F1F`.
- **Avatar size is responsive** (not a fixed 64dp): it is derived from the card's actual width so
  the 2-column grid always fills the card, at any display size. On a ~360dp-wide phone this yields
  ~58dp; on a ~412dp phone ~71dp; the 64dp spec value is the design reference. No fixed dp clamp is
  applied: on large display sizes a 48dp floor would overflow (and clip) the grid, and on small
  display sizes a 72dp ceiling would leave the grid sparse with tiny icons, so the fit size wins.
  Because the card width isn't known at bind time, `CategoryCardView` rebuilds its icon grid from
  `onSizeChanged` using the measured width (the first-layout estimate is display-width based and is
  corrected before the card is drawn).
- **Icon inside avatar**: the app icon is rendered at the full avatar size so the icon itself fills
  the entire circle; it is *clipped to a circle* via the avatar outline.
- **Icon spacing**: **12dp** horizontal / **12dp** vertical (equal h/v spacing), fixed regardless of
  avatar size. The horizontal gap is subtracted when fitting avatars to the card width.
- **Overflow cluster**: icons scale with the avatar (26dp / 64dp baseline ratio) with **4dp** gaps
  (scaled, floored at 2dp) inside the last cell (spec: ~26–28dp).

## Overflow formula (N)

Spec: "show first `N-1` apps as full avatars, then if remaining apps > 1, pack up to 4 into a
2×2 cluster." The free parameter `N` is not pinned in the spec. Chosen:
- `MAX_FULL_AVATARS = 3` (the first row plus the leading slot of the second row).
- So `N = 4`: first **3** apps render as full avatars; the 4th cell position becomes the cluster
  when there are more than 3 apps.
- Examples: Utilities (7 apps) → 3 full avatars + cluster(4 mini icons). Entertainment (2 apps) →
  2 full avatars, no cluster.
- If a category has more than 4 overflow apps, extras are logged (`Log.w`) and dropped (not crashed).

## Category taxonomy mapping

Categories follow **Google Play's category names**, sorted in Play's canonical order. Apps are
matched by **package name** (case-insensitive) against the offline database in
`lawnchair/src/app/lawnchair/util/AppCategoryDb.kt`; anything not in the database falls into
**Others**.

| Category (Play name) | Matched packages (examples) |
|---|---|
| Games | `com.supercell.clashofclans`, `com.mojang.minecraftpe`, `com.nianticlabs.pokemongo` |
| Art & Design | `com.canva.editor`, `com.adobe.psmobile` |
| Auto & Vehicles | `com.teslamotors.tesla`, `com.toyota.oneapp` |
| Beauty | `com.perfectcorp.beautyplus` |
| Books & Reference | `com.google.android.apps.books`, `com.amazon.kindle`, `org.wikipedia` |
| Business | `com.linkedin.android`, `com.slack`, `com.microsoft.office.outlook` |
| Comics | `com.webtoons.global`, `com.mangaplus.app` |
| Communication | `com.whatsapp`, `org.telegram.messenger`, `com.google.android.apps.messaging` |
| Dating | `com.tinder`, `com.bumble.app` |
| Education | `com.duolingo`, `com.khanacademy.android`, `com.google.android.apps.classroom` |
| Entertainment | `com.google.android.youtube`, `com.netflix.mediaclient` |
| Finance | `com.paypal.android.p2pmobile`, `com.coinbase.android` |
| Food & Drink | `com.ubercab.eats`, `com.mcdonalds.app`, `com.starbucks` |
| Health & Fitness | `com.google.android.apps.fitness`, `com.strava`, `com.myfitnesspal.android` |
| House & Home | `com.nest.android`, `com.zillow.android.zillow` |
| Lifestyle | `com.zwift` |
| Maps & Navigation | `com.google.android.apps.maps`, `com.waze` |
| Medical | `com.webmd.android`, `com.goodrx` |
| Music & Audio | `com.spotify.music`, `com.google.android.apps.youtube.music`, `com.shazam.android` |
| News & Magazines | `com.google.android.apps.magazines`, `com.bbc.mobile.news.ww` |
| Parenting | `com.babycenter` |
| Personalization | `com.google.android.inputmethod.latin`, `com.teslacoilsw.launcher` |
| Photography | `com.google.android.apps.photos`, `com.google.android.GoogleCamera` |
| Productivity | `com.google.android.apps.docs`, `com.google.android.keep`, `com.google.android.calendar` |
| Shopping | `com.amazon.mShop.android.shopping`, `com.walmart.android`, `com.ebay.mobile` |
| Social | `com.facebook.katana`, `com.instagram.android`, `com.twitter.android` |
| Sports | `com.espn.score_center`, `com.bbc.sport` |
| Tools | `com.android.vending`, `com.google.android.googlequicksearchbox`, `com.google.android.calculator` |
| Travel & Local | `com.airbnb.android`, `com.booking`, `com.ubercab` |
| Video Players & Editors | `com.google.android.videos`, `org.videolan.vlc`, `com.capcut.lvoverseas` |
| Weather | `com.accuweather.android`, `com.weather.Weather` |
| Others | everything not in the database |

Notes / assumptions:
- The full database is the source of truth (`AppCategoryDb.kt`); it is intentionally large so that
  the fewest possible apps land in **Others**.
- **Play Store** (`com.android.vending`) and the **Google app** (`com.google.android.googlequicksearchbox`)
  are mapped to **Tools**; **Gmail** to **Productivity**; **Google Photos** to **Photography**.
- The ambiguous overflow icon in an overflowing card is **not** hardcoded — it comes from whatever
  app actually overflows into the cluster.
- Categories only appear when at least one installed app maps to them; empty categories are omitted,
  and **Others** is always kept last.
- Category ordering follows the enum order in `AppCategory.kt` (Google Play order).

## Theme scope

- Dynamic expressive surfaces are used **only in category-cards drawer mode**. Other drawer modes
  (alphabetical / folders) keep their existing theme behavior.
- Category cards, search bar, avatars, text, and drag handle all resolve through day/night dynamic
  tokens, so the drawer follows the system theme while retaining the reference's tonal hierarchy.

## Search bar

- Implemented by repositioning the existing search box to the bottom of the drawer in cards mode
  and restyling it as a grey pill (search icon + "Search" text + 3-dot menu). Tapping it focuses
  the existing search input (all existing search behaviour retained).
- The pill is 52dp tall, ~16dp side margins, resting 12dp above the gesture-nav inset.

## Drag handle

- Widened to 36dp (was 32dp); the shared `bg_rounded_corner_bottom_sheet_handle` drawable colour
  is theme-driven, so in cards mode a dedicated mid-grey colour is applied to the all-apps handle.

## Known limitations / edge cases

1. When a category has > 7 apps (3 avatars + 4 in cluster), the 8th+ apps are logged and hidden.
2. The category full-page view (opened by tapping a card) follows the categorized drawer's dynamic
   day/night palette. Search-results state continues to use the existing search theme.
3. The 2-column grid applies to category cards; work/private-space UI is untouched.
4. The reference screenshot shows the light branch; dark mode uses the corresponding expressive
   dark neutral surfaces and contrast-safe text.
