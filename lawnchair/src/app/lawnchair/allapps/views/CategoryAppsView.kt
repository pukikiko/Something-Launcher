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
import android.graphics.Rect
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.lawnchair.allapps.CategoryInfo
import com.android.launcher3.BubbleTextView
import com.android.launcher3.DeviceProfile
import com.android.launcher3.Insettable
import com.android.launcher3.R
import com.android.launcher3.allapps.AllAppsRecyclerView
import com.android.launcher3.model.data.AppInfo
import com.android.launcher3.views.ActivityContext

/**
 * Full-page view showing every app in a category as a plain grid, opened from a category card.
 * Contains a header with a back button and the category title.
 */
class CategoryAppsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr), Insettable {

    private lateinit var backButton: ImageView
    private lateinit var titleView: TextView
    private lateinit var appsList: AllAppsRecyclerView
    private var categoryAppsAdapter: CategoryAppsAdapter? = null
    private val activityContext: ActivityContext = ActivityContext.lookupContext(context)

    var onBack: Runnable? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        backButton = findViewById(R.id.category_apps_back)
        titleView = findViewById(R.id.category_apps_title)
        appsList = findViewById(R.id.category_apps_list)
        backButton.setOnClickListener { onBack?.run() }
        // The category page is only reachable from the Smart Categorized drawer, which uses a
        // light lavender background, so the header is hard-coded to dark-on-light.
        backButton.setColorFilter(android.graphics.Color.parseColor("#1F1F1F"))
        titleView.setTextColor(android.graphics.Color.parseColor("#1F1F1F"))
        appsList.layoutManager = GridLayoutManager(context, getAppsPerRow())
        categoryAppsAdapter = CategoryAppsAdapter(activityContext)
        appsList.adapter = categoryAppsAdapter
    }

    private fun getAppsPerRow(): Int {
        return activityContext.deviceProfile.numShownAllAppsColumns
    }

    /** The grid list showing the category's apps. */
    fun getAppsRecyclerView(): AllAppsRecyclerView = appsList

    /** Recreates the grid layout manager for the current device profile. */
    fun onDeviceProfileChanged(dp: DeviceProfile) {
        appsList.layoutManager = GridLayoutManager(context, dp.numShownAllAppsColumns)
        appsList.adapter = categoryAppsAdapter
    }

    /** Populates this view with the apps of [category] and displays it. */
    fun showCategory(category: CategoryInfo) {
        titleView.text = context.getString(R.string.all_apps_category_page_title, category.title)
        categoryAppsAdapter?.setApps(category.apps)
        appsList.layoutManager = GridLayoutManager(context, getAppsPerRow())
        appsList.adapter = categoryAppsAdapter
        appsList.scrollToPosition(0)
        visibility = View.VISIBLE
    }

    /** Clears and hides this view. */
    fun clear() {
        categoryAppsAdapter?.setApps(emptyList())
        visibility = View.GONE
    }

    override fun setInsets(insets: Rect) {
        appsList.setPadding(
            0,
            0,
            0,
            insets.bottom,
        )
        requestLayout()
    }

    private class CategoryAppsAdapter(
        private val activityContext: ActivityContext,
    ) : RecyclerView.Adapter<CategoryAppsAdapter.ViewHolder>() {

        private var apps: List<AppInfo> = emptyList()

        fun setApps(apps: List<AppInfo>) {
            this.apps = apps
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val icon = inflater.inflate(R.layout.all_apps_icon, parent, false) as BubbleTextView
            icon.setOnClickListener(activityContext.itemOnClickListener)
            icon.setOnLongClickListener(activityContext.allAppsItemLongClickListener)
            val height = activityContext.deviceProfile.allAppsProfile.cellHeightPx
            icon.layoutParams = icon.layoutParams.apply {
                this.height = height
            }
            return ViewHolder(icon)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val icon = holder.itemView as BubbleTextView
            icon.reset()
            icon.applyFromApplicationInfo(apps[position])
            // Light drawer background → keep app labels dark and readable.
            icon.setTextColor(android.graphics.Color.parseColor("#1F1F1F"))
        }

        override fun getItemCount(): Int = apps.size

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    }
}
