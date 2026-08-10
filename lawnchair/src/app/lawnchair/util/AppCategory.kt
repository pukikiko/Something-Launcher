package app.lawnchair.util

/**
 * Google Play–style app categories used to sort the app drawer.
 *
 * Order matches Google Play's canonical category listing and defines the display order of the
 * category cards. Each category is matched by package name in [AppCategoryDb]; any app that is not
 * mapped falls into "Others".
 */
enum class AppCategory(val displayName: String) {
    GAMES("Games"),
    ART_AND_DESIGN("Art & Design"),
    AUTO_AND_VEHICLES("Auto & Vehicles"),
    BEAUTY("Beauty"),
    BOOKS_AND_REFERENCE("Books & Reference"),
    BUSINESS("Business"),
    COMICS("Comics"),
    COMMUNICATION("Communication"),
    DATING("Dating"),
    EDUCATION("Education"),
    ENTERTAINMENT("Entertainment"),
    FINANCE("Finance"),
    FOOD_AND_DRINK("Food & Drink"),
    HEALTH_AND_FITNESS("Health & Fitness"),
    HOUSE_AND_HOME("House & Home"),
    LIFESTYLE("Lifestyle"),
    MAPS_AND_NAVIGATION("Maps & Navigation"),
    MEDICAL("Medical"),
    MUSIC_AND_AUDIO("Music & Audio"),
    NEWS_AND_MAGAZINES("News & Magazines"),
    PARENTING("Parenting"),
    PERSONALIZATION("Personalization"),
    PHOTOGRAPHY("Photography"),
    PRODUCTIVITY("Productivity"),
    SHOPPING("Shopping"),
    SOCIAL("Social"),
    SPORTS("Sports"),
    TOOLS("Tools"),
    TRAVEL_AND_LOCAL("Travel & Local"),
    VIDEO_PLAYERS("Video Players & Editors"),
    WEATHER("Weather"),
}
