package app.lawnchair.util

import android.content.Context
import app.lawnchair.flowerpot.Flowerpot
import com.android.launcher3.model.data.AppInfo
import com.android.launcher3.util.ApplicationInfoWrapper
import com.android.launcher3.util.PackageManagerHelper

/**
 * Categorizes apps into System Apps, Google Apps, and Flowerpot categories.
 *
 * @param apps List of apps to categorize
 * @param context Context for checking system apps and accessing Flowerpot
 * @return Map of category names to lists of apps in that category
 */
fun categorizeAppsWithSystemAndGoogle(
    apps: List<AppInfo>,
    context: Context,
): Map<String, List<AppInfo>> {
    val systemApps = mutableListOf<AppInfo>()
    val googleApps = mutableListOf<AppInfo>()
    val otherApps = mutableListOf<AppInfo>()

    apps.forEach { app ->
        val packageName = app.targetPackage ?: return@forEach
        val intent = app.intent

        // Check if it's a Google app first (Google apps can also be system apps)
        when {
            packageName.startsWith("com.google.") -> googleApps.add(app)
            intent != null && ApplicationInfoWrapper(context, intent).isSystem() -> systemApps.add(app)
            else -> otherApps.add(app)
        }
    }

    // Use flowerpot to categorize other apps (non-system, non-Google)
    val potsManager = Flowerpot.Manager.getInstance(context)
    val categorizedApps = potsManager.categorizeApps(otherApps)

    // Build final categorized apps map
    val finalCategorizedApps = mutableMapOf<String, List<AppInfo>>()

    if (systemApps.isNotEmpty()) {
        finalCategorizedApps["System Apps"] = systemApps
    }

    if (googleApps.isNotEmpty()) {
        finalCategorizedApps["Google Apps"] = googleApps
    }

    // Add flowerpot categorized apps
    finalCategorizedApps.putAll(categorizedApps)

    return finalCategorizedApps
}

/**
 * Custom, loosely-defined category taxonomy used by the "Smart Categorized" app drawer.
 *
 * This deliberately does NOT use Android's standard app-category buckets. Apps are matched by
 * package name against a fixed set of hand-picked groups; anything unmapped falls into "Others".
 * The label set and the ordering below are the source of truth.
 *
 * @param apps List of apps to categorize
 * @return LinkedMap of category names (in display order) to lists of apps in that category
 */
fun categorizeAppsForCardsDrawer(
    apps: List<AppInfo>,
): Map<String, List<AppInfo>> {
    val categories = linkedMapOf(
        "Social" to setOf(
            "com.android.contacts",
            "com.google.android.apps.contacts",
            "com.google.android.gm",
            "com.google.android.apps.messaging",
            "com.google.android.dialer",
        ),
        "Entertainment" to setOf(
            "com.google.android.youtube",
            "com.google.android.apps.youtube.music",
        ),
        "Utilities" to setOf(
            "com.android.calendar",
            "com.android.chrome",
            "com.google.android.deskclock",
            "com.android.documentsui",
            "com.google.android.googlequicksearchbox",
            "com.android.vending",
        ),
        "Travel" to setOf(
            "com.google.android.apps.maps",
        ),
        "Productivity" to setOf(
            "com.google.android.apps.docs",
        ),
        "Multimedia Tools" to setOf(
            "com.google.android.apps.photos",
        ),
    )

    val result = linkedMapOf<String, MutableList<AppInfo>>()
    categories.keys.forEach { result[it] = mutableListOf() }
    val others = mutableListOf<AppInfo>()

    apps.forEach { app ->
        val packageName = app.targetPackage ?: return@forEach
        val category = categories.entries.firstOrNull { (_, packages) ->
            packageName in packages
        }?.key
        if (category != null) {
            result[category]?.add(app)
        } else {
            others.add(app)
        }
    }

    // Drop empty categories, but always keep "Others" last even if it ends up empty.
    return buildMap {
        result.forEach { (category, categoryApps) ->
            if (categoryApps.isNotEmpty()) {
                put(category, categoryApps)
            }
        }
        if (others.isNotEmpty()) {
            put("Others", others)
        }
    }
}
