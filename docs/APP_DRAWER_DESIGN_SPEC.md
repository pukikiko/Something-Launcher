# Smart Categorized App Drawer — Design Spec

> Source of truth for the "Smart Categorized" app drawer redesign. This restates the target
> design in text/numeric form so it can be iterated against without screenshots.

## 1. Screen-level layout

- **Background**: full-bleed, very light lavender-grey `#EEEDF3` (NOT pure white, NOT dark).
- **Presentation**: the drawer is a bottom sheet / drawer pulled up over the home screen (not a
  full-screen activity).
  - A small horizontal **drag-handle pill** sits centered at the very top edge, just under the
    status bar: ~36dp wide × 4dp tall, rounded, mid-grey.
  - Content scrolls vertically underneath a **fixed bottom search bar** (see §4). A sliver of
    icons peeks out above the search bar while scrolling — the search bar is a floating overlay,
    not part of the scrolling list.
- **Status bar**: standard system status bar, dark icons on light background.
- **Bottom**: thin dark gesture-nav pill (system-drawn, not part of this feature).

## 2. Category grid

- Content is a **2-column grid of cards**, one card per category.
- Card order: left-to-right, top-to-bottom. An odd last row leaves an empty slot (not stretched).
- Outer screen padding: ~16–20dp left/right and top.
- Gap between cards: ~14–16dp horizontal and vertical.
- Cards are **masonry / staggered height** — each card sizes to its own icon grid (Entertainment
  with 2 icons is visibly shorter than Utilities with 6+).

## 3. Card component

- Background: near-white `#F8F7FB` / `#FFFFFF` @ ~95%, clearly lighter than the screen bg.
- Soft, subtle drop shadow (elevation ≈ 2–4dp, soft/diffuse).
- Corner radius: large, ~24–28dp.
- Internal padding: ~18–20dp all sides.
- **Header**: category name, top-left aligned, single line, medium weight (500), dark grey
  `#1F1F1F`, ~17–18sp, standard sans font. No icon next to it, no chevron, no "View all" link.
- Below header: ~16dp gap, then the icon grid (§4).

## 4. Icon grid inside a card

- Icons: **2 columns**, top-to-bottom, left-to-right, wrapping every 2 icons.
- **Primary icon**: ~64dp diameter circle. Each app icon sits centered on a plain white circular
  background (avatar look); square/adaptive icons are clipped/masked to a circle with white
  padding — the raw launcher icon is never dropped in as-is.
- Spacing: ~20–24dp horizontal between circles, ~16–20dp vertical.
- **Overflow behavior** ("Utilities" card): when a category has more apps than fit as full 64dp
  circles in the visible slots, the **last** grid slot becomes a compact **2×2 mini-cluster**
  (~26–28dp icons, ~4dp gaps, no white circle backgrounds) instead of spawning more full-size rows.
  - Rule: show first `N-1` apps as full avatars; if remaining apps > 1, pack up to 4 of them into
    one 2×2 cluster cell; if there are still more, log it and don't crash.

## 5. Category → app mapping (custom taxonomy)

**These are NOT Android's default category names.** Custom groupings:

| Category | Apps observed |
|---|---|
| Social | Contacts, Gmail, Messages, Phone |
| Entertainment | YouTube, YouTube Music |
| Utilities | Calendar, Chrome, Clock, Files, Google (app), Play Store, + overflow |
| Travel | Google Maps |
| Productivity | Google Drive |
| Multimedia Tools | Google Photos |
| Others | Camera, Gemini (+ more below the fold) |

Grouping logic: match installed apps by package name to these buckets; anything unmapped falls
into **Others**. The label set and grouping logic are fixed; exact app membership adapts to what's
actually installed.

## 6. Bottom search bar (fixed, floats over content)

- Pill-shaped, ~52–56dp tall, full width minus ~16dp side margins, fixed at the bottom above the
  gesture-nav pill, floating over scrolling content.
- Background: light grey `#E3E2E8` (visibly darker than the screen bg so it reads as a control).
- Left: magnifying-glass search icon, dark grey.
- Center-left: placeholder text "Search", dark grey, regular weight.
- Right: vertical 3-dot overflow menu icon, dark grey.
- No border, no shadow (or extremely subtle).

## 7. Explicit diffs from baseline (change list)

1. Theme: dark → light (lavender-grey bg, dark text on white cards).
2. Card layout: single full-width column → 2-column grid.
3. Icon treatment: plain horizontal row + "View all (N)" link → 2-col wrapping grid, circular white
   avatars, overflow mini-cluster, no "View all".
4. Search bar: top-pinned → bottom-fixed floating over content.
5. Top of screen: nothing → drag-handle pill.
6. Category labels: Android buckets → custom labels (§5).
7. Card corner radius/shadow: standard Material → ~24–28dp radius, softer diffuse shadow.

## 8. Values summary

| Token | Value |
|---|---|
| Screen background | `#EEEDF3` |
| Card background | `#F8F7FB` (≈95% white) |
| Header text | `#1F1F1F`, medium, 18sp |
| Card radius | 24dp |
| Card padding | 20dp |
| Card elevation / shadow | 3dp, diffuse |
| Grid columns | 2 |
| Outer padding | 16dp |
| Card gap (h+v) | 14dp |
| Avatar circle | 64dp, white |
| Icon inside avatar | 52dp, clipped to circle |
| Icon spacing (h/v) | 22dp / 18dp |
| Overflow cluster icon | 26dp, 4dp gaps |
| Search bar height | 52dp, pill `#E3E2E8` |
| Drag handle | 36 × 4dp, mid-grey |
