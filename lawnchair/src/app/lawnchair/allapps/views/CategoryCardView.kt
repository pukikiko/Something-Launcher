/*
 *     This file is part of Lawnchair Launcher.
 *
 *     Lawnchair Launcher is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Lawnchair Launcher is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Lawnchair Launcher.  If not, see <https://www.gnu.org/licenses/>.
 */

package app.lawnchair.allapps.views

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import app.lawnchair.allapps.CategoryInfo
import com.android.launcher3.BubbleTextView
import com.android.launcher3.R
import kotlin.math.ceil

/**
 * A rounded "category card" shown in the Smart Categorized app drawer. It displays the category
 * title and a 2-column grid of its app icons rendered as circular white avatars. When a category
 * has more apps than fit as full-size avatars, the last grid slot becomes a compact 2x2
 * mini-cluster instead of the grid growing indefinitely. Tapping the card opens the full-page
 * category view; tapping an individual icon launches that app directly.
 */
class CategoryCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : LinearLayout(context, attrs, defStyleAttr) {

    private lateinit var titleView: TextView
    private lateinit var iconsContainer: LinearLayout

    init {
        orientation = VERTICAL
        clipChildren = false
        clipToPadding = false
        setWillNotDraw(false)
        val radius = resources.getDimensionPixelSize(R.dimen.all_apps_category_card_radius).toFloat()
        background = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadius = radius
            setColor(Color.parseColor("#F8F7FB"))
        }
        elevation = resources.getDimension(R.dimen.all_apps_category_card_elevation)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        titleView = TextView(context).apply {
            id = R.id.category_card_title
            setTextColor(Color.parseColor("#1F1F1F"))
            textSize = resources.getDimension(R.dimen.all_apps_category_header_size) /
                resources.displayMetrics.scaledDensity
            typeface = Typeface.create("sans-serif-medium", Typeface.NORMAL)
            setSingleLine(true)
            ellipsize = android.text.TextUtils.TruncateAt.END
        }
        iconsContainer = LinearLayout(context).apply {
            orientation = VERTICAL
        }
        addView(titleView, LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT))
        addView(iconsContainer, LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT))
    }

    /**
     * Binds this card to the given [categoryInfo].
     *
     * @param categoryInfo The category and its apps.
     * @param appsPerRow Unused (kept for the adapter contract); the card grid is always 2 columns.
     * @param iconClickListener Click listener for individual avatars (launches the app).
     * @param iconLongClickListener Long click listener for individual avatars (shows popup).
     * @param openCategory Runnable invoked when the card is tapped to open the full category page.
     */
    @Suppress("UNUSED_PARAMETER")
    fun bind(
        categoryInfo: CategoryInfo,
        appsPerRow: Int,
        iconClickListener: View.OnClickListener,
        iconLongClickListener: View.OnLongClickListener,
        openCategory: Runnable,
    ) {
        titleView.text = categoryInfo.title
        iconsContainer.removeAllViews()

        val apps = categoryInfo.apps
        val overflowApps = apps.drop(MAX_FULL_AVATARS)
        val clusterApps = if (overflowApps.size > 1) {
            if (overflowApps.size > MAX_CLUSTER_ICONS) {
                Log.w(TAG, "Category '${categoryInfo.title}' has ${apps.size} apps; only showing " +
                    "$MAX_FULL_AVATARS avatars + $MAX_CLUSTER_ICONS cluster icons.")
            }
            overflowApps.take(MAX_CLUSTER_ICONS)
        } else {
            emptyList()
        }
        // "Show first N-1 apps as full avatars": N = MAX_FULL_AVATARS + 1.
        val fullApps = if (clusterApps.isNotEmpty()) {
            apps.take(MAX_FULL_AVATARS)
        } else {
            apps
        }

        val avatarSize = resources.getDimensionPixelSize(R.dimen.all_apps_category_avatar_size)
        val hGap = resources.getDimensionPixelSize(R.dimen.all_apps_category_icon_h_gap)
        val vGap = resources.getDimensionPixelSize(R.dimen.all_apps_category_icon_v_gap)
        val inflater = LayoutInflater.from(context)

        // Full-size avatar rows.
        val avatarRows = ceil(fullApps.size / 2.0).toInt()
        for (row in 0 until avatarRows) {
            val rowLayout = rowLayout()
            for (col in 0 until 2) {
                val index = row * 2 + col
                val app = fullApps.getOrNull(index) ?: continue
                val icon = inflater.inflate(
                    R.layout.all_apps_category_icon, rowLayout, false) as BubbleTextView
                bindIcon(icon, app, iconClickListener, iconLongClickListener)
                val lp = LinearLayout.LayoutParams(avatarSize, avatarSize)
                if (col == 0) lp.marginEnd = hGap
                rowLayout.addView(icon, lp)
            }
            iconsContainer.addView(rowLayout, rowLayoutParams(vGap, isLast = row == avatarRows - 1 && clusterApps.isEmpty()))
        }

        // Overflow cluster in the last grid slot.
        if (clusterApps.isNotEmpty()) {
            val rowLayout = rowLayout()
            rowLayout.addView(buildCluster(clusterApps, inflater, iconClickListener, iconLongClickListener),
                LinearLayout.LayoutParams(avatarSize, avatarSize))
            iconsContainer.addView(rowLayout, rowLayoutParams(vGap, isLast = true))
        }

        setOnClickListener { openCategory.run() }
        contentDescription = context.getString(
            R.string.all_apps_category_content_description, categoryInfo.title,
            apps.size)
    }

    private fun rowLayout() = LinearLayout(context).apply {
        orientation = HORIZONTAL
        gravity = Gravity.START
    }

    private fun rowLayoutParams(vGap: Int, isLast: Boolean) = LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT).apply {
        if (!isLast) bottomMargin = vGap
    }

    private fun bindIcon(
        icon: BubbleTextView,
        app: com.android.launcher3.model.data.AppInfo,
        iconClickListener: View.OnClickListener,
        iconLongClickListener: View.OnLongClickListener,
    ) {
        icon.applyFromApplicationInfo(app)
        // Icon-only mini grid, matching the avatar look.
        icon.setTextVisibility(false)
        icon.setOnClickListener(iconClickListener)
        icon.setOnLongClickListener(iconLongClickListener)
    }

    private fun buildCluster(
        clusterApps: List<com.android.launcher3.model.data.AppInfo>,
        inflater: LayoutInflater,
        iconClickListener: View.OnClickListener,
        iconLongClickListener: View.OnLongClickListener,
    ): View {
        val gap = resources.getDimensionPixelSize(R.dimen.all_apps_category_cluster_gap)
        val cluster = LinearLayout(context).apply {
            orientation = VERTICAL
            gravity = Gravity.CENTER
        }
        val rows = ceil(clusterApps.size / 2.0).toInt()
        for (row in 0 until rows) {
            val rowLayout = LinearLayout(context).apply {
                orientation = HORIZONTAL
                gravity = Gravity.CENTER
            }
            for (col in 0 until 2) {
                val index = row * 2 + col
                val app = clusterApps.getOrNull(index) ?: continue
                val icon = inflater.inflate(
                    R.layout.all_apps_category_cluster_icon, rowLayout, false) as BubbleTextView
                bindIcon(icon, app, iconClickListener, iconLongClickListener)
                val lp = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT)
                if (col == 0) lp.marginEnd = gap
                rowLayout.addView(icon, lp)
            }
            val lp = LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
            if (row != rows - 1) lp.bottomMargin = gap
            cluster.addView(rowLayout, lp)
        }
        return cluster
    }

    companion object {
        private const val TAG = "CategoryCardView"
        // Number of full-size avatar slots before the cluster takes over the last cell.
        // This is N-1 from the design spec (N = MAX_FULL_AVATARS + 1).
        private const val MAX_FULL_AVATARS = 4
        private const val MAX_CLUSTER_ICONS = 4
    }
}
