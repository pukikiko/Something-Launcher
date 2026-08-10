package app.lawnchair.util

import android.content.Context
import app.lawnchair.flowerpot.Flowerpot
import java.util.EnumMap
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
 * Categorizes apps for the "Smart Categorized" card drawer using Google Play–style categories.
 *
 * Apps are matched by package name against the offline [AppCategoryDb] database; anything unmapped
 * falls into "Others". Categories are output in Google Play's canonical order and only non-empty
 * categories (plus "Others", kept last) are returned.
 *
 * @param apps List of apps to categorize (all apps, in any order)
 * @return Map of category names (in display order) to lists of apps in that category
 */
fun categorizeAppsForCardsDrawer(
    apps: List<AppInfo>,
): Map<String, List<AppInfo>> {
    val buckets = EnumMap<AppCategory, MutableList<AppInfo>>(AppCategory::class.java)
    val others = mutableListOf<AppInfo>()

    apps.forEach { app ->
        val packageName = app.targetPackage ?: return@forEach
        val category = AppCategoryDb.categoryFor(packageName)
        if (category != null) {
            buckets.getOrPut(category) { mutableListOf() }.add(app)
        } else {
            others.add(app)
        }
    }

    // Categories holding a single app don't get their own card; fold them into "Others".
    AppCategory.entries.forEach { category ->
        buckets[category]?.takeIf { it.size == 1 }?.let { others.add(it.first()) }
    }

    // Output categories in enum (Google Play) order, dropping empty and single-app ones;
    // keep "Others" last.
    return buildMap {
        AppCategory.entries.forEach { category ->
            buckets[category]?.takeIf { it.size > 1 }?.let { put(category.displayName, it) }
        }
        if (others.isNotEmpty()) {
            put("Others", others)
        }
    }
}
