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
- **Icon spacing**: tuned to fit two 64dp avatars inside the card at launcher density: **12dp** horizontal /
  **18dp** vertical.
- **Icon inside avatar**: 64dp circle. The app icon is rendered at the full **64dp** so the icon
  itself fills the entire circle; it is *clipped to a circle* via the avatar outline.
- **Overflow cluster**: icons at **26dp** with **4dp** gaps inside a 64dp cell (spec: ~26–28dp).

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

The custom taxonomy matches apps by **package name prefix/exact match**:

| Category | Matched packages |
|---|---|
| Social | `com.android.contacts`, `com.google.android.gm`, `com.google.android.apps.messaging`, `com.google.android.dialer`, `com.google.android.apps.contacts` |
| Entertainment | `com.google.android.youtube`, `com.google.android.apps.youtube.music` |
| Utilities | `com.android.calendar`, `com.android.chrome`, `com.google.android.deskclock`, `com.google.android.apps.docs`(no), `com.android.documentsui`, `com.google.android.googlequicksearchbox`, `com.android.vending` |
| Travel | `com.google.android.apps.maps` |
| Productivity | `com.google.android.apps.docs` |
| Multimedia Tools | `com.google.android.apps.photos` |
| Others | everything else (Camera `com.google.android.GoogleCamera`, Gemini `com.google.android.apps.gemini`, …) |

Notes / assumptions:
- **Play Store** (`com.android.vending`) and **Google app** (`com.google.android.googlequicksearchbox`)
  are mapped to **Utilities**; **Google Photos** to **Multimedia Tools**; **Gmail** to **Social**.
- The ambiguous overflow icon in the Utilities card (spec §5: "assistant/sparkle", do not hardcode
  a package) is **not** hardcoded — it comes from whatever app actually overflows into the cluster.
- The spec's Travel / Productivity / Multimedia Tools single-app categories depend on those apps
  being installed; on a device without them those cards simply won't appear.
- Category ordering follows the table order (§5). "Others" is last.

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
