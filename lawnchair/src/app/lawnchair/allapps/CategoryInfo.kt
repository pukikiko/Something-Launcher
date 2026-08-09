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

package app.lawnchair.allapps

import com.android.launcher3.model.data.AppInfo

/**
 * Represents an auto-generated app category (e.g. "System Apps", "Google Apps" or a Flowerpot
 * category) together with the apps assigned to it. Used by the Nothing-style category cards drawer.
 */
class CategoryInfo(
    @JvmField val title: String,
    @JvmField val apps: List<AppInfo>,
)
