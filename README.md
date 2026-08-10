# Something Launcher

> [!NOTE]
> This is a personal fork of **Lawnchair 16**.
>
> The headline change is a **Nothing Launcher–style app drawer** with **smart app
> category cards**, replacing the traditional alphabetical app list with an
> organized, card-based view of your apps grouped by Google Play category.

## Why this fork exists

I had a **Nothing Phone 3a** for a while and genuinely loved the software
experience on it, especially the **smart sorting in the app drawer**. Nothing
did something rare: it took the pile of apps everyone has and organized it in a
way that was actually useful, turning the drawer into something pleasant to
browse rather than a wall of alphabet soup.

Then the phone died on me. Twice. After **two RMAs**, I lost trust in the hardware and
got a refund.

I switched to a **Pixel 10 Pro XL**, and the stock launcher left me cold. I went
looking for an open-source launcher that replicated that Nothing-style sorted
drawer, and came up empty. So, the way you do, I set out to build it myself.

Rather than start from scratch, I forked **Lawnchair 16**, itself a fork of
the AOSP Launcher3, and implemented the categorized drawer I wanted. This
repository is the result.

## What's in the fork

The main addition is a new **Category cards** drawer mode, modelled on the
Nothing Launcher app drawer:

- **Card-based app drawer** - apps are grouped into a **2-column grid of cards**,
  one per category, instead of a flat alphabetical list.
- **Smart categorization** - apps are matched by package name against a large
  offline database (`AppCategoryDb.kt`) using **Google Play's app taxonomy**
  (Games, Communication, Productivity, Tools, and 30 more). Anything unmapped
  falls into an **Others** card, kept last.

The drawer still supports the classic **Alphabetical** and **Folders** modes,
and you can switch between all three in *Settings → App drawer → Layout*. The
old boolean "list drawer" preference is migrated automatically.

## Everything else

This fork keeps the rest of Lawnchair 16's feature set, including:

- At a Glance widget support, with integration for [Smartspacer](https://github.com/KieronQuinn/Smartspacer).
- QuickSwitch support for Android Recents integration on Android 10-15 (root required).
- Global search for apps, contacts, and web results from the home screen.
- Customization options for icon packs, fonts, and color settings.

## Building

This is a source-first project - there are no published releases yet. Build it
with the standard Lawnchair/Android build toolchain:

```sh
git clone --recurse-submodules https://github.com/pukikiko/Something-Launcher.git
cd Something-Launcher
# Requires the Android SDK (Java 21). See upstream Lawnchair docs for build setup.
./gradlew assembleLawnWithQuickstepGithubRelease
```

The debug APK will be output under `build/outputs/`.

## Acknowledgements

This project stands on the shoulders of the [Lawnchair](https://github.com/LawnchairLauncher/lawnchair)
team, who built the best open-source Android launcher out there and keep
evolving it forward from AOSP's Launcher3. Thanks also to the
[Lawnchair contributors](https://github.com/LawnchairLauncher/lawnchair/graphs/contributors)
and the AOSP community that makes projects like this possible.

And a shoutout to **Nothing** - for showing what a good app drawer looks like.
(Not a financial endorsement. I wish the hardware had lived up to the software.)
