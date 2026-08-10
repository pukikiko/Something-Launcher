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
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.LinearLayout
import android.widget.TextView
import app.lawnchair.allapps.CategoryInfo
import app.lawnchair.theme.color.tokens.ColorTokens
import com.android.launcher3.BubbleTextView
import com.android.launcher3.R
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.roundToInt

/**
 * A rounded "category card" shown in the Smart Categorized app drawer. It displays the category
 * title and a 2-column grid of its app icons rendered as circular white avatars. When a category
 * has more apps than fit as full-size avatars, the last grid slot becomes a compact 2x2
 * mini-cluster instead of the grid growing indefinitely. Tapping the card opens the full-page
 * category view; tapping a full avatar launches that app directly, and tapping any mini-icon in
 * the overflow cluster also opens the full-page category view.
 *
 * Icon sizing is responsive: avatars are derived from the card's actual width so the 2-column grid
 * always fills the card at any display size. The cluster icons scale proportionally.
 */
class CategoryCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : LinearLayout(context, attrs, defStyleAttr) {

    private lateinit var titleView: TextView
    private lateinit var iconsContainer: LinearLayout

    private var boundCategory: CategoryInfo? = null
    private var iconClickListener: View.OnClickListener? = null
    private var iconLongClickListener: View.OnLongClickListener? = null
    private var openCategory: Runnable? = null
    private var avatarSizePx: Int = 0

    init {
        orientation = VERTICAL
        clipChildren = false
        clipToPadding = false
        setWillNotDraw(false)
        val radius = resources.getDimensionPixelSize(R.dimen.all_apps_category_card_radius).toFloat()
        background = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadius = radius
            setColor(ColorTokens.ExpressiveAllAppsCard.resolveColor(context))
        }
        elevation = resources.getDimension(R.dimen.all_apps_category_card_elevation)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        titleView = TextView(context).apply {
            id = R.id.category_card_title
            setTextColor(ColorTokens.ExpressiveAllAppsText.resolveColor(context))
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
            ViewGroup.LayoutParams.WRAP_CONTENT).apply {
                bottomMargin = resources.getDimensionPixelSize(
                    R.dimen.all_apps_category_header_bottom_gap)
            })
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
        boundCategory = categoryInfo
        this.iconClickListener = iconClickListener
        this.iconLongClickListener = iconLongClickListener
        this.openCategory = openCategory

        titleView.text = categoryInfo.title
        setOnClickListener { openCategory.run() }
        contentDescription = context.getString(
            R.string.all_apps_category_content_description, categoryInfo.title,
            categoryInfo.apps.size)

        rebuildIconGrid()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (w > 0 && boundCategory != null) {
            val size = computeAvatarSize(w)
            if (size != avatarSizePx) {
                // Defer the rebuild out of the layout pass: rebuilding the icon grid while the
                // view is being laid out can leave the card unmeasured and its icons unrendered.
                post { rebuildIconGrid() }
            }
        }
    }

    /** Rebuilds the icon grid, deriving the avatar size from the current card width. */
    private fun rebuildIconGrid() {
        val category = boundCategory ?: return
        val cardWidth = if (width > 0) width else fallbackCardWidth()
        avatarSizePx = computeAvatarSize(cardWidth)
        buildIconGrid(category, avatarSizePx)
    }

    /**
     * Estimates the card width before the view has been measured. A card occupies roughly half
     * the drawer width, so the display width minus the grid side paddings is a close enough start;
     * [onSizeChanged] corrects it before the card is first drawn.
     */
    private fun fallbackCardWidth(): Int {
        val gridPadding = resources.getDimensionPixelSize(R.dimen.all_apps_category_grid_padding)
        return (resources.displayMetrics.widthPixels - gridPadding * 2) / 2
    }

    /**
     * Fits two avatars (plus the horizontal gap) into the card's content width so the 2-column
     * grid always fills the card at any display size. The avatar size is derived purely from the
     * measured card width; fixed dp bounds are not applied because they fight the card width at
     * extreme display sizes: on large display sizes the 48dp minimum overflowed and clipped the
     * icons, and on small display sizes the 72dp maximum left the grid sparse with tiny icons.
     */
    private fun computeAvatarSize(cardWidthPx: Int): Int {
        val hGap = resources.getDimensionPixelSize(R.dimen.all_apps_category_icon_h_gap)
        val availableWidth = cardWidthPx - paddingLeft - paddingRight
        return max(1, (availableWidth - hGap) / 2)
    }

    private fun buildIconGrid(
        categoryInfo: CategoryInfo,
        avatarSize: Int,
    ) {
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

        val hGap = resources.getDimensionPixelSize(R.dimen.all_apps_category_icon_h_gap)
        val vGap = resources.getDimensionPixelSize(R.dimen.all_apps_category_icon_v_gap)
        val clusterIconSize = (avatarSize * CLUSTER_ICON_SCALE).roundToInt()
        val minClusterGapPx =
            (MIN_CLUSTER_GAP_DP * resources.displayMetrics.density).roundToInt()
        val clusterGap = max(minClusterGapPx, (avatarSize * CLUSTER_GAP_SCALE).roundToInt())
        val inflater = LayoutInflater.from(context)

        // Inner container constrained to exactly two icons side by side. It is centered
        // horizontally in the card while its cells stay start-aligned, so the whole icon block
        // sits in the middle of the card and an odd third icon wraps to the bottom-left.
        val grid = GridLayout(context).apply {
            columnCount = 2
            useDefaultMargins = false
        }

        // Full-size avatars and the overflow cluster share one grid. The cluster is the final
        // cell, rather than an extra row, so Utilities stays the same height as Social.
        val cellCount = fullApps.size + if (clusterApps.isNotEmpty()) 1 else 0
        val rows = ceil(cellCount / 2.0).toInt()
        var index = 0
        for (row in 0 until rows) {
            for (col in 0 until 2) {
                val lp = GridLayout.LayoutParams().apply {
                    width = avatarSize
                    height = avatarSize
                    rowSpec = GridLayout.spec(row)
                    columnSpec = GridLayout.spec(col)
                    if (row > 0) topMargin = vGap
                    if (col == 0) marginEnd = hGap
                }
                if (index < fullApps.size) {
                    val icon = inflater.inflate(
                        R.layout.all_apps_category_icon, grid, false) as BubbleTextView
                    bindIcon(
                        icon,
                        fullApps[index],
                        iconClickListener,
                        iconLongClickListener,
                        withAvatar = true,
                        iconSize = avatarSize,
                    )
                    grid.addView(icon, lp)
                } else if (clusterApps.isNotEmpty() && index == fullApps.size) {
                    // The overflow cluster represents apps that don't fit as full avatars, so
                    // tapping any of its mini-icons opens the category page instead of launching
                    // the specific app.
                    grid.addView(
                        buildCluster(
                            clusterApps,
                            inflater,
                            View.OnClickListener { openCategory?.run() },
                            iconLongClickListener,
                            clusterIconSize,
                            clusterGap,
                        ),
                        lp)
                }
                index++
            }
        }

        iconsContainer.addView(grid, LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT).apply {
                gravity = Gravity.CENTER_HORIZONTAL
            })
    }

    private fun bindIcon(
        icon: BubbleTextView,
        app: com.android.launcher3.model.data.AppInfo,
        iconClickListener: View.OnClickListener?,
        iconLongClickListener: View.OnLongClickListener?,
        withAvatar: Boolean,
        iconSize: Int,
    ) {
        icon.applyFromApplicationInfo(app)
        icon.setIconSizeOverride(iconSize)
        if (withAvatar) {
            icon.background = GradientDrawable().apply {
                shape = GradientDrawable.OVAL
                setColor(ColorTokens.ExpressiveAllAppsAvatar.resolveColor(context))
            }
        } else {
            icon.background = null
        }
        // BubbleTextView normally reserves a label line, which shifts compound drawables upward.
        icon.setCenterVertically(false)
        icon.setPadding(0, 0, 0, 0)
        icon.setCompoundDrawablePadding(0)
        icon.text = ""
        icon.setTextVisibility(false)
        icon.setOnClickListener(iconClickListener)
        icon.setOnLongClickListener(iconLongClickListener)
    }

    private fun buildCluster(
        clusterApps: List<com.android.launcher3.model.data.AppInfo>,
        inflater: LayoutInflater,
        clusterClickListener: View.OnClickListener?,
        clusterLongClickListener: View.OnLongClickListener?,
        iconSize: Int,
        gap: Int,
    ): View {
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
                bindIcon(icon, app, clusterClickListener, clusterLongClickListener,
                    withAvatar = false, iconSize = iconSize)
                val lp = LinearLayout.LayoutParams(iconSize, iconSize)
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
        private const val MAX_FULL_AVATARS = 3
        private const val MAX_CLUSTER_ICONS = 4
        // Cluster icons scale with the avatar (26dp / 64dp design baseline).
        private const val CLUSTER_ICON_SCALE = 13f / 32f
        // Cluster gap scales with the avatar too (4dp / 64dp design baseline).
        private const val CLUSTER_GAP_SCALE = 4f / 64f
        // Minimum gap between cluster icons, so they never visually merge on tiny screens.
        private const val MIN_CLUSTER_GAP_DP = 2f
    }
}
