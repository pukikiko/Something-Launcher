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
- **Icon spacing**: spec said ~20–24dp horizontal / ~16–20dp vertical; used **22dp** horizontal /
  **18dp** vertical.
- **Icon inside avatar**: 64dp white circle with the app icon drawn at **52dp** (≈6dp white ring).
  The icon is *clipped to a circle* via the avatar outline.
- **Overflow cluster**: icons at **26dp** with **4dp** gaps inside a 64dp cell (spec: ~26–28dp).

## Overflow formula (N)

Spec: "show first `N-1` apps as full avatars, then if remaining apps > 1, pack up to 4 into a
2×2 cluster." The free parameter `N` is not pinned in the spec. Chosen:
- `MAX_FULL_AVATARS = 4` (2 rows × 2 columns).
- So `N = 5`: first **4** apps render as full avatars; the 5th cell position becomes the cluster
  when there are more than 4 apps.
- Examples: Utilities (7 apps) → 4 full avatars + cluster(3 mini icons). Entertainment (2 apps) →
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

## Light-theme scope

- The light dynamic neutral background is used **only in category-cards drawer mode**. Other drawer modes
  (alphabetical / folders) keep the user's normal theme.
- The cards themselves, search bar, and drag handle use dynamic expressive surfaces. The **search
  results / category-page** sub-screens reuse theme-driven text colours, so on a dark system theme
  their text may not be ideal on the light lavender background — flagged as a known edge case.

## Search bar

- Implemented by repositioning the existing search box to the bottom of the drawer in cards mode
  and restyling it as a grey pill (search icon + "Search" text + 3-dot menu). Tapping it focuses
  the existing search input (all existing search behaviour retained).
- The pill is 52dp tall, ~16dp side margins, resting 12dp above the gesture-nav inset.

## Drag handle

- Widened to 36dp (was 32dp); the shared `bg_rounded_corner_bottom_sheet_handle` drawable colour
  is theme-driven, so in cards mode a dedicated mid-grey colour is applied to the all-apps handle.

## Known limitations / edge cases

1. When a category has > 8 apps (4 avatars + 4 in cluster), the 9th+ apps are logged and hidden.
2. The category full-page view (opened by tapping a card) uses the lavender background with
   hard-coded dark header/labels. Search-results state in the drawer is not re-themed light, so
   on a dark system theme its icons/labels may have lower contrast on the lavender sheet.
3. The 2-column grid applies to category cards; work/private-space UI is untouched.
4. In cards mode the category-page header text/back button and app labels are hard-coded dark
   (#1F1F1F); if the drawer theme is ever made light globally this can be simplified.
