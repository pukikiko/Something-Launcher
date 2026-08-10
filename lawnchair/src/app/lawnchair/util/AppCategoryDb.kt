package app.lawnchair.util

/**
 * Offline database mapping known Android package names to their Google Play category.
 *
 * This lets the launcher sort installed apps into Google Play–style categories without any network
 * access or a Play Store account. It is deliberately large so that as many installed apps as
 * possible land in a meaningful category instead of "Others".
 *
 * Package names are matched case-insensitively: entries are stored lower-cased and lookups
 * normalise the target package to lower-case before searching.
 */
object AppCategoryDb {

    private val map: Map<String, AppCategory> = buildMap {
        // ===== Games =====
        games(
            "com.supercell.clashofclans",
            "com.supercell.clashroyale",
            "com.supercell.brawlstars",
            "com.supercell.hayday",
            "com.supercell.boombeach",
            "com.supercell.squadbusters",
            "com.nianticlabs.pokemongo",
            "com.nianticproject.ingress",
            "com.king.candycrushsaga",
            "com.king.candycrushsodasaga",
            "com.king.farmheroessaga",
            "com.rovio.angrybirds",
            "com.rovio.angrybirds2",
            "com.mojang.minecraftpe",
            "com.roblox.client",
            "com.tencent.ig",
            "com.pubg.imobile",
            "com.mobilelegends.mlbb",
            "com.miHoYo.GenshinImpact",
            "com.miHoYo.hkrpg",
            "com.HoYoverse.hkrpgoversea",
            "com.activision.callofduty.shooter",
            "com.epicgames.fortnite",
            "com.ea.game.pvz2_row",
            "com.ea.game.pvzheroes_row",
            "com.ea.game.tetris_row",
            "com.gameloft.android.ANMP.GloftA8HM",
            "com.asphalt.a8",
            "com.gameloft.android.ANMP.GloftN3HM",
            "com.wb.goog.legolego",
            "com.igg.clashofempires",
            "com.plarium.raidlegends",
            "com.funplus.monsterwarlord",
            "com.tencent.tmgp.sgame",
            "com.kakaogames.er.pk",
            "jp.konami.duelmastersplay",
            "com.riotgames.league.wildrift",
            "com.riotgames.league.teamfighttactics",
            "com.riotgames.valorantmobile",
            "com.kinggaming.peaktopus",
            "com.nezteam.bp",
            "com.jumpgate.evony",
            "com.tap4fun.empirefour",
            "com.squareenix.ffpocket",
            "com.gameloft.android.ANMP.GloftGGHM",
            "com.gameloft.android.ANMP.GloftPDHM",
            "com.bandainamcoent.dbzdokkanww",
            "com.bandainamcointernational.dbzlegends",
            "com.androbugs.riding.racer",
            "com.sega.colorfulstage.en",
            "com.nytimes.crossword",
            "com.chess",
            "com.vitastudio.mahjong",
            "jp.pokemon.pokemontcgp",
            "com.block.juggle",
            "com.mobilityware.castlesolitaire",
            "com.jindoblu.offlinegames",
            "color.number.paint.pixle.art.sort.jigsaw",
            "com.loomgames.pixelflow",
            "com.mobile.legends",
            "org.lichess.mobilev2",
            "com.ecffri.arrows",
            "com.productmadness.lightninglink",
            "easy.sudoku.puzzle.solver.free",
            "com.easybrain.block.puzzle.games",
            "com.nytimes.wordgame",
            "com.oakever.meowdoku",
            "com.gamovation.mahjongclub",
            "com.scopely.monopolygo",
            "com.pixel.art.coloring.color.number",
            "com.mergegames.gossipharbor",
            "com.miniclip.eightballpool",
            "jp.co.ponos.battlecatsen",
            "com.kiloo.subwaysurf",
            "com.minutecryptic.app",
            "com.easybrain.sudoku.android",
            "com.hitappsgames.wordsolitaire",
            "com.mobilityware.solitaire",
            "jp.co.goodroid.hyper.busflow",
            "com.firsttouchgames.dls7",
            "com.peoplefun.wordcross",
            "com.playrix.township",
            "com.grandgames.magicsort",
            "com.ea.game.starwarscapital_row",
            "jp.konami.pesam",
            "com.bigbluebubble.singingmonsters.full",
            "com.run.tower.defense",
            "com.robtopx.geometryjumplite",
            "com.dreamgames.royalmatch",
            "com.jagex.oldscape.android",
            "jp.pokemon.pokemonchampions",
            "in.playsimple.crossword.go",
            "com.tripledot.woodoku",
            "com.rovio.baba",
            "com.bigbluebubble.msm2",
            "com.youmusic.magictiles",
            "com.kooapps.pianotiles2gp",
            "harmonium.music.gameg.real.harmoniumfree",
            "com.ubisoft.dance.justdance",
            "com.bushiroad.en.bangdreamgbp",
            "com.orange.kidspiano.music.songs",
            "com.music.ball.hop.tiles.beat.dancing.game",
            "com.ubisoft.dance.justdancecontroller2023",
            "game.qualiarts.hololive.dreams.com",
            "com.rvappstudios.kids.games.music.baby.piano.songs.lucas.and.friends",
            "com.amanotes.beathopper",
            "com.beat.cat.dancing.hop.tiles.music.game",
            "com.bilibili.sirius",
            "com.gismart.realpianofree",
            "br.com.rodrigokolb.realdrum",
            "tiles.song.game.piano.surf",
            "moe.low.arc",
            "com.pianonbeat.rhythm",
            "com.gismart.guitar",
            "com.binaryguilt.completemusicreadingtrainer",
            "com.musicdreamtiles.pianogame",
            "com.dress.skirt.run.dancing.hop.game.beat.piano.hair",
            "com.rayark.pluto",
            "com.bandlegame.bandleguessasong",
            "piano.magic.hop.tiles.dance",
            "beats.color.sing.star.rush",
            "air.com.bartbonte.taptaptap",
            "com.kingcatgames.heardit",
            "air.com.freshplanet.games.songpop2",
            "com.dancing.smash.hop.game.tiles.beat.piano.surfer",
            "sh.ppy.osulazer",
            "com.rayark.cytus2",
            "com.rayark.voez",
            "com.spacea.beatstar",
            "com.miho.dancelinegp",
        )

        // ===== Art & Design =====
        artDesign(
            "com.canva.editor",
            "com.adobe.psmobile",
            "com.adobe.sparkpost",
            "com.adobe.creativeapp",
            "com.adsk.sketchbook",
            "com.procreate.paint",
            "org.medibang.paint",
            "com.getpaint",
            "com.picsart.studio",
            "jp.co.ibis.ibispaintx.app.global",
        )

        // ===== Auto & Vehicles =====
        autoVehicles(
            "com.teslamotors.tesla",
            "com.toyota.oneapp",
            "com.honda.hondalink",
            "com.bmw.connecteddrive",
            "com.mercedesbenz.me",
            "com.audi.myaudi",
            "com.ford.myfordmobile",
            "com.gm.hyundai.hyundai",
            "com.kia.uvo",
            "com.volvo.vcc",
            "com.jaguar.landrover.incontrol.remote",
            "com.google.android.projection.gearhead",
            "com.polaris.rzr",
            "com.carvana.app",
            "com.truecar.app",
        )

        // ===== Beauty =====
        beauty(
            "com.symantec.beautyroom",
            "com.perfectcorp.beautyplus",
            "com.meitu.meipaimv",
            "com.cyberlink.youcammakeup",
            "com.lb.lgb",
        )

        // ===== Books & Reference =====
        booksReference(
            "com.google.android.apps.books",
            "com.amazon.kindle",
            "com.flyersoft.moonreaderp",
            "com.flyersoft.moonreader",
            "org.wikipedia",
            "org.wikipedia.beta",
            "com.wattpad.android",
            "com.audible.application",
            "com.amazon.audible",
            "com.kobobooks.android",
            "com.overdrive.mobile.android.mediaconsole",
            "com.handmark.expressreader",
            "com.wordreference",
            "com.touchtype.swiftkey.beta",
        )

        // ===== Business =====
        business(
            "com.linkedin.android",
            "com.microsoft.office.outlook",
            "com.slack",
            "com.microsoft.teams",
            "com.microsoft.office.word",
            "com.microsoft.office.excel",
            "com.microsoft.office.powerpoint",
            "us.zoom.videomeetings",
            "com.cisco.webex.meetings",
            "com.google.android.apps.adm",
            "com.google.android.apps.inbox",
            "com.salesforce.chatter",
            "com.atlassian.confluence",
            "com.atlassian.android.jira.core",
            "com.trello",
            "com.asana.app",
            "com.monday",
            "com.notion.id",
            "com.mi.globalbrowser",
            "com.microsoft.onenote",
            "com.microsoft.office.onenote",
        )

        // ===== Comics =====
        comics(
            "com.webtoons.global",
            "com.madefire.madefire",
            "com.comixology",
            "com.mangaplus.app",
        )

        // ===== Communication =====
        communication(
            "com.whatsapp",
            "com.whatsapp.w4b",
            "org.telegram.messenger",
            "org.telegram.bot",
            "org.thoughtcrime.securesms",
            "com.skype.raider",
            "com.viber.voip",
            "com.snapchat.android",
            "com.discord",
            "com.facebook.orca",
            "com.tencent.mm",
            "com.linecorp.LGLG",
            "com.kakao.talk",
            "com.imo.android.imoim",
            "com.google.android.apps.messaging",
            "com.google.android.dialer",
            "com.google.android.contacts",
            "com.android.contacts",
            "com.android.dialer",
            "com.microsoft.skydrive",
            "com.microsoft.rdc.android",
            "com.tailscale.android",
            "com.tailscale.ipn",
            "com.google.android.contactkeys",
            "com.wireguard.android",
            "com.openvpn.client.android",
            "org.purevpn",
            "com.mozilla.firefox.vpn",
            "de.mobilej.websocket",
            "com.google.android.gm",
            "com.microsoft.appmanager",
        )

        // ===== Dating =====
        dating(
            "com.tinder",
            "com.bumble.app",
            "com.hinge.app",
            "com.match.mobile.chat",
            "com.okcupid",
            "com.grindrapp.android",
            "com.eharmony",
        )

        // ===== Education =====
        education(
            "com.duolingo",
            "com.khanacademy.android",
            "com.google.android.apps.classroom",
            "org.edx.mobile",
            "com.coursera.android",
            "com.udemy.android",
            "com.quizlet.quizletandroid",
            "com.babbel.mobile.android.en",
            "com.snapwiz.clep",
            "com.byjus.thelearningapp",
            "com.ushahidi.android",
        )

        // ===== Entertainment =====
        entertainment(
            "com.google.android.youtube",
            "com.netflix.mediaclient",
            "com.zhiliaoapp.musically",
            "com.hulu.plus",
            "com.amazon.avod.thirdpartyclient",
            "com.disney.disneyplus",
            "com.hbomax",
            "com.cbs.app",
            "com.peacocktv.brick",
            "com.paramount.tv",
            "com.showtime.cbs.app",
            "com.apple.tv",
            "com.brunchapp",
            "com.mtvn.amp",
            "com.vudu.mobile",
            "com.vudu.airplay",
            "com.crunchyroll.plus",
            "com.funimation.now",
        )

        // ===== Finance =====
        finance(
            "com.paypal.android.p2pmobile",
            "com.cash.app",
            "com.venmo",
            "com.chase.sig.android",
            "com.wf.wellsfargomobile",
            "com.bankofamerica.privacy",
            "com.bankofamerica.BAMobileBanking",
            "com.citi.citimobile",
            "com.usbank",
            "com.capitalone.mobile",
            "com.americanexpress.android.acctsvc",
            "com.bbva.bbvacontigo",
            "com.santander.app",
            "com.coinbase.android",
            "com.blockchain",
            "com.binance.dev",
            "com.binance.app",
            "com.google.android.apps.walletnfcrel",
            "com.google.android.apps.gmoney",
            "com.android.bankapp",
            "com.revolut.revolut",
            "com.transferwise.android",
            "com.squareup.square",
            "au.com.up.money",
            "au.gov.dhs.centrelinkexpressplus",
            "com.commbank.netbank",
            "au.com.nab.mobile",
            "com.anz.android.gomoney",
            "com.samsung.android.spay",
        )

        // ===== Food & Drink =====
        foodDrink(
            "com.ubercab.eats",
            "com.deliveroo.orderapp",
            "com.dominos.app",
            "com.mcdonalds.app",
            "com.starbucks",
            "com.grubbhub.android",
            "com.doorbell",
            "com.pizzahut",
            "com.olo.papajohns",
            "com.wingstop.mobile",
            "com.chipotle.ordering",
            "com.weber.mobile",
            "com.helloFresh.HomeDelivery",
            "com.blueapron.android",
            "com.yummly.android",
            "com.allrecipes.android",
            "com.tastemade",
            "com.webling.hungryjacks",
            "com.kfcaus.ordering",
        )

        // ===== Health & Fitness =====
        healthFitness(
            "com.google.android.apps.fitness",
            "com.fitbit.FitbitMobile",
            "com.nike.plusgps",
            "com.strava",
            "com.myfitnesspal.android",
            "com.sec.android.app.shealth",
            "com.calm.android",
            "com.headspace",
            "com.pelotonandroid",
            "com.zuumapp",
            "com.runtastic.android",
            "com.adidas.runtastic",
            "com.mapmyrun.android2",
            "com.jawbone.up",
            "com.underarmour.run",
            "com.endomondo.android",
            "com.google.android.apps.health",
            "com.google.android.gms.fitness",
            "com.samsung.shealth.tracker.weight",
            "com.samsung.health",
        )

        // ===== House & Home =====
        houseHome(
            "com.ikea.hopp",
            "com.wayfair.wayfair",
            "com.houzz.app",
            "com.zillow.android.zillow",
            "com.zillow.android.rental",
            "com.trulia.android.rental",
            "com.gotrgood",
            "com.nest.android",
            "com.google.android.apps.chromecast.app",
            "com.google.android.homeshell",
            "com.amazon.dee.app",
            "com.samsung.smarthome",
            "com.lifx.lifx",
            "com.philips.hue.simulator",
            "io.homeassistant.companion.android",
        )

        // ===== Lifestyle =====
        lifestyle(
            "com.zwift",
            "com.samsung.android.samsungpass",
        )

        // ===== Maps & Navigation =====
        mapsNavigation(
            "com.google.android.apps.maps",
            "com.waze",
            "com.sygic.aura",
            "com.mapquest.android.ace",
            "com.tomtom.amigo",
            "org.mapsforge.poi",
            "com.sygic.aura.navigate",
            "com.grab",
            "com.gojek.app",
        )

        // ===== Medical =====
        medical(
            "com.webmd.android",
            "com.medscape.android",
            "com.cvs.launcher3",
            "com.goodrx",
            "com.epocrates",
            "com.google.android.apps.mlo",
            "com.sam.android.mdex",
        )

        // ===== Music & Audio =====
        musicAudio(
            "com.spotify.music",
            "com.google.android.apps.youtube.music",
            "com.soundcloud.android",
            "com.pandora.android",
            "com.amazon.mp3",
            "com.shazam.android",
            "com.google.android.music",
            "com.deezer.android",
            "com.apple.android.music",
            "com.kkbox.android",
            "com.pocketcasts.android",
            "fm.anchor.android",
            "com.air.wave",
            "com.simple.radio",
            "com.tiefensuche.soundcrowd",
        )

        // ===== News & Magazines =====
        newsMagazines(
            "com.google.android.apps.magazines",
            "com.bbc.mobile.news.ww",
            "com.cnn.mobile.android.phone",
            "com.nytimes.android",
            "com.fox.news.android",
            "com.reuters",
            "com.ap.news",
            "com.huffpost.android",
            "com.washingtonpost.android",
            "com.twitter.android",
            "com.reddit.frontpage",
            "com.flipboard.app",
            "com.onefootball",
        )

        // ===== Parenting =====
        parenting(
            "com.babycenter",
            "com.webmd.baby",
            "com.whiteboard",
            "com.cartoonnetworkapp",
            "com.lego.duplo",
        )

        // ===== Personalization =====
        personalization(
            "com.google.android.apps.wallpaper",
            "com.android.wallpaper.picker",
            "com.google.android.inputmethod.latin",
            "com.teslacoilsw.launcher",
            "com.sec.android.app.launcher",
            "com.microsoft.launcher",
            "com.android.settings",
            "org.kustom.wallpaper",
            "com.themezilla.pixelgrid",
        )

        // ===== Photography =====
        photography(
            "com.google.android.apps.photos",
            "com.google.android.GoogleCamera",
            "com.sec.android.app.camera",
            "com.adobe.lrmobile",
            "com.lightroom",
            "com.nikon.app",
            "com.google.android.camera.experimental2016",
            "com.android.camera2",
            "com.motorola.camera3",
            "app.alextran.immich",
        )

        // ===== Productivity =====
        productivity(
            "com.google.android.apps.docs",
            "com.google.android.apps.docs.editors.docs",
            "com.google.android.apps.docs.editors.sheets",
            "com.google.android.apps.docs.editors.slides",
            "com.google.android.calendar",
            "com.android.calendar",
            "com.google.android.apps.tasks",
            "com.google.android.keep",
            "com.google.android.apps.tachyon",
            "com.google.android.gm",
            "com.microsoft.office.onenote",
            "com.dropbox.android",
            "com.evernote",
            "com.evernote.editor",
            "com.notion.id",
            "com.google.android.pdfviewer",
            "com.basecamp.calendar",
            "com.any.do",
            "com.ticktick.task",
            "com.microsoft.office.outlook",
        )

        // ===== Shopping =====
        shopping(
            "com.amazon.mShop.android.shopping",
            "com.walmart.android",
            "com.ebay.mobile",
            "com.alibaba.aliexpresshd",
            "com.alibaba.aliexpress",
            "com.etsy.android",
            "com.target.ui",
            "com.shein.mobileapp",
            "com.temu",
            "com.wish.amazon",
            "com.mercari.android",
            "com.poshmark.app",
            "com.finalrealm.shopping",
        )

        // ===== Social =====
        social(
            "com.facebook.katana",
            "com.instagram.android",
            "com.twitter.android",
            "com.pinterest",
            "com.reddit.frontpage",
            "com.tumblr",
            "com.meta.threads",
            "com.bsky.app",
            "com.mastodon.android",
            "com.joinmastodon.android",
            "com.twitter.admin",
            "com.quora.android",
            "com.meta.messenger_composer",
        )

        // ===== Sports =====
        sports(
            "com.espn.score_center",
            "com.yahoo.mobile.client.android.sports",
            "com.bbc.sport",
            "com.foxsports.android",
            "com.onefootball",
            "com.espn.fantasy.football",
            "us.motorsport",
            "com.wildtangent.android",
            "com.king.sports",
            "com.afl.live",
        )

        // ===== Tools =====
        tools(
            "com.android.vending",
            "com.google.android.googlequicksearchbox",
            "com.android.chrome",
            "com.brave.browser",
            "com.android.documentsui",
            "com.google.android.apps.files",
            "com.android.filemanager",
            "com.google.android.deskclock",
            "com.android.deskclock",
            "com.sec.android.app.clockpackage",
            "com.google.android.calculator",
            "com.android.calculator2",
            "com.sec.android.app.popupcalculator",
            "com.google.android.apps.searchlite",
            "com.google.android.apps.translate",
            "com.google.android.apps.authenticator2",
            "com.google.android.apps.pdfviewer",
            "com.termux",
            "com.pluscubed.plustimer",
            "org.fossify.filemanager",
            "com.duckduckgo.mobile.android",
            "com.mozilla.firefox",
            "com.opera.browser",
            "com.microsoft.bing",
            "com.chrome.beta",
            "com.sec.android.app.sbrowser",
            "com.sec.android.app.chromecustomizations",
            "com.samsung.android.app.notes",
            "com.samsung.android.calendar",
            "com.samsung.android.app.clock",
            "io.ente.auth",
            "com.x8bit.bitwarden",
            "com.azure.authenticator",
            "org.localsend.localsend_app",
            "pw.mfc.swagtel",
            "com.boost.mobile.myboost",
            "pw.mfc.metrohop",
            "au.gov.mygov.mygovapp",
            "au.gov.sa.my",
            "com.google.android.safetycore",
            "com.openai.chatgpt",
            "com.anthropic.claude",
        )

        // ===== Travel & Local =====
        travelLocal(
            "com.google.android.apps.maps",
            "com.airbnb.android",
            "com.expedia.bookings",
            "com.booking",
            "com.kayak.android",
            "com.ubercab",
            "com.lyft.android",
            "com.hilton.android.hhonors",
            "com.tripadvisor.tripadvisor",
            "com.hostelworld.app",
            "com.agoda.android",
            "com.skyscanner.android",
            "com.otreva.flight",
        )

        // ===== Video Players & Editors =====
        videoPlayers(
            "com.google.android.videos",
            "org.videolan.vlc",
            "com.mxtech.videoplayer.ad",
            "com.mxtech.videoplayer.pro",
            "com.lemon.lvoverseas",
            "com.capcut.lvoverseas",
            "com.blinkstream.videoeditor",
            "com.google.android.apps.youtube.creator",
            "com.quicinc.videoplayer",
            "com.miui.video",
        )

        // ===== Weather =====
        weather(
            "com.accuweather.android",
            "com.yahoo.mobile.client.android.weather",
            "com.weather.Weather",
            "com.handmark.expressweather",
            "com.weather.forecast",
            "com.vector.app",
            "com.samsung.android.weather",
            "io.weawhere.app",
            "com.mobi.lv.AllWeather",
            "au.gov.bom.metview",
        )
    }

    /**
     * Returns the [AppCategory] for a package name, or `null` if the package is not in the database.
     * Lookup is case-insensitive.
     */
    fun categoryFor(packageName: String): AppCategory? = map[packageName.lowercase()]

    private fun MutableMap<String, AppCategory>.games(vararg pkgs: String) =
        pkgs.forEach { put(it.lowercase(), AppCategory.GAMES) }

    private fun MutableMap<String, AppCategory>.artDesign(vararg pkgs: String) =
        pkgs.forEach { put(it.lowercase(), AppCategory.ART_AND_DESIGN) }

    private fun MutableMap<String, AppCategory>.autoVehicles(vararg pkgs: String) =
        pkgs.forEach { put(it.lowercase(), AppCategory.AUTO_AND_VEHICLES) }

    private fun MutableMap<String, AppCategory>.beauty(vararg pkgs: String) =
        pkgs.forEach { put(it.lowercase(), AppCategory.BEAUTY) }

    private fun MutableMap<String, AppCategory>.booksReference(vararg pkgs: String) =
        pkgs.forEach { put(it.lowercase(), AppCategory.BOOKS_AND_REFERENCE) }

    private fun MutableMap<String, AppCategory>.business(vararg pkgs: String) =
        pkgs.forEach { put(it.lowercase(), AppCategory.BUSINESS) }

    private fun MutableMap<String, AppCategory>.comics(vararg pkgs: String) =
        pkgs.forEach { put(it.lowercase(), AppCategory.COMICS) }

    private fun MutableMap<String, AppCategory>.communication(vararg pkgs: String) =
        pkgs.forEach { put(it.lowercase(), AppCategory.COMMUNICATION) }

    private fun MutableMap<String, AppCategory>.dating(vararg pkgs: String) =
        pkgs.forEach { put(it.lowercase(), AppCategory.DATING) }

    private fun MutableMap<String, AppCategory>.education(vararg pkgs: String) =
        pkgs.forEach { put(it.lowercase(), AppCategory.EDUCATION) }

    private fun MutableMap<String, AppCategory>.entertainment(vararg pkgs: String) =
        pkgs.forEach { put(it.lowercase(), AppCategory.ENTERTAINMENT) }

    private fun MutableMap<String, AppCategory>.finance(vararg pkgs: String) =
        pkgs.forEach { put(it.lowercase(), AppCategory.FINANCE) }

    private fun MutableMap<String, AppCategory>.foodDrink(vararg pkgs: String) =
        pkgs.forEach { put(it.lowercase(), AppCategory.FOOD_AND_DRINK) }

    private fun MutableMap<String, AppCategory>.healthFitness(vararg pkgs: String) =
        pkgs.forEach { put(it.lowercase(), AppCategory.HEALTH_AND_FITNESS) }

    private fun MutableMap<String, AppCategory>.houseHome(vararg pkgs: String) =
        pkgs.forEach { put(it.lowercase(), AppCategory.HOUSE_AND_HOME) }

    private fun MutableMap<String, AppCategory>.lifestyle(vararg pkgs: String) =
        pkgs.forEach { put(it.lowercase(), AppCategory.LIFESTYLE) }

    private fun MutableMap<String, AppCategory>.mapsNavigation(vararg pkgs: String) =
        pkgs.forEach { put(it.lowercase(), AppCategory.MAPS_AND_NAVIGATION) }

    private fun MutableMap<String, AppCategory>.medical(vararg pkgs: String) =
        pkgs.forEach { put(it.lowercase(), AppCategory.MEDICAL) }

    private fun MutableMap<String, AppCategory>.musicAudio(vararg pkgs: String) =
        pkgs.forEach { put(it.lowercase(), AppCategory.MUSIC_AND_AUDIO) }

    private fun MutableMap<String, AppCategory>.newsMagazines(vararg pkgs: String) =
        pkgs.forEach { put(it.lowercase(), AppCategory.NEWS_AND_MAGAZINES) }

    private fun MutableMap<String, AppCategory>.parenting(vararg pkgs: String) =
        pkgs.forEach { put(it.lowercase(), AppCategory.PARENTING) }

    private fun MutableMap<String, AppCategory>.personalization(vararg pkgs: String) =
        pkgs.forEach { put(it.lowercase(), AppCategory.PERSONALIZATION) }

    private fun MutableMap<String, AppCategory>.photography(vararg pkgs: String) =
        pkgs.forEach { put(it.lowercase(), AppCategory.PHOTOGRAPHY) }

    private fun MutableMap<String, AppCategory>.productivity(vararg pkgs: String) =
        pkgs.forEach { put(it.lowercase(), AppCategory.PRODUCTIVITY) }

    private fun MutableMap<String, AppCategory>.shopping(vararg pkgs: String) =
        pkgs.forEach { put(it.lowercase(), AppCategory.SHOPPING) }

    private fun MutableMap<String, AppCategory>.social(vararg pkgs: String) =
        pkgs.forEach { put(it.lowercase(), AppCategory.SOCIAL) }

    private fun MutableMap<String, AppCategory>.sports(vararg pkgs: String) =
        pkgs.forEach { put(it.lowercase(), AppCategory.SPORTS) }

    private fun MutableMap<String, AppCategory>.tools(vararg pkgs: String) =
        pkgs.forEach { put(it.lowercase(), AppCategory.TOOLS) }

    private fun MutableMap<String, AppCategory>.travelLocal(vararg pkgs: String) =
        pkgs.forEach { put(it.lowercase(), AppCategory.TRAVEL_AND_LOCAL) }

    private fun MutableMap<String, AppCategory>.videoPlayers(vararg pkgs: String) =
        pkgs.forEach { put(it.lowercase(), AppCategory.VIDEO_PLAYERS) }

    private fun MutableMap<String, AppCategory>.weather(vararg pkgs: String) =
        pkgs.forEach { put(it.lowercase(), AppCategory.WEATHER) }
}
