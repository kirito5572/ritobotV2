//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package me.kirito5572.objects.main;

import org.jetbrains.annotations.NotNull;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LinkConfirm {
    private static String link;

    private static final String[] webList = new String[] {
            //0
            ".gg", ".edu", ".org", ".net", ".mil", ".com", ".gov", ".int", ".museum", ".info",
            ".coop", ".biz", "aero", "pro", ".mobi", ".travel", ".jobs", ".cat", ".tel", ".asia",
            ".aaa", ".aarp", ".abarth", ".abb", ".abbott", ".abbive", ".abc", ".able", ".abogado", ".abudhabi",
            ".ac", ".academy", ".accenture", ".accountants", ".aco", ".active", ".actor", ".ad", ".adac", ".ads",
            ".adult", ".ae", ".aeg", ".aero", ".aetna", ".af", ".afamilycompany", ".afl", ".africa", ".ag",
            ".agakhan", ".agency", ".ai", ".aig", ".aigo", ".airbus", ".airforce", ".airtel", ".akdn", ".al",
            ".alfaromeo", ".alibaba", ".alipay", ".allfinanz", ".allstate", ".ally", ".alsace", ".alstom", ".am", ".americanexpress",
            ".americanfamily", ".amex", ".amfam", ".amica", ".amsterdam", ".an", ".analytics", ".android", ".anquan", ".anz",
            ".ao", ".aol", ".apartments", ".app", ".apple", ".aq", ".aquarelle", ".ar", ".arab", ".aramco",
            ".archi", ".army", ".arpa", ".art", ".arte", ".as", ".asda", ".asia", ".associates", ".at",
            //100
            ".athleta", ".attorney", ".au", ".auction", ".audi", ".audible", ".audio", ".auspost", ".author", ".auto",
            ".autos", ".avianca", ".aw", ".aws", ".ax", ".axa", ".az", ".azure", ".ba", ".baby",
            ".baidu", ".banamex", ".bananarepublic", ".band", ".bank", ".bar", ".barcelona", ".barclaycard", ".barclays", ".barefoot",
            ".bargains", ".baseball", ".basketball", ".bauhaus", ".bayern", ".bb", ".bbc", ".bbt", ".bbva", ".bcg",
            ".bcn", ".bd", ".be", ".beats", ".beauty", ".beer", ".bentley", ".berlin", ".best", ".bestbuy",
            ".bet", ".bf", ".bg", ".bh", ".bharti", ".bi", ".bible", ".bid", ".bike", ".bing",
            ".bingo", ".bio", ".biz", ".bj", ".bl", ".black", ".blackfriday", ".blanco", ".blockbuster", ".blog",
            ".bloomberg", ".blue", ".bm", ".bms", ".bmw", ".bn", ".bnl", ".bnpparibas", ".bo", ".boats",
            ".boehringer", ".bofa", ".bom", ".bond", ".boo", ".book", ".booking", ".boots", ".bosch", ".bostik",
            ".boston", ".bot", ".boutique", ".box", ".bq", ".br", ".bradesco", ".bridgestone", ".broadway", ".broker",
            //200
            ".brother", ".brussels", ".bs", ".bt", ".budapest", ".bugatti", ".build", ".builders", ".business", ".buy",
            ".buzz", ".bv", ".bw", ".by", ".bz", ".bzh", ".ca", ".cab", ".cafe", ".cal",
            ".call", ".calvinklein", ".cam", ".camera", ".camp", ".cancerresearch", ".canon", ".capetown", ".capital", ".capitalone",
            ".car", ".caravan", ".cards", ".care", ".career", ".careers", ".cars", ".cartier", ".casa", ".case",
            ".caseih", ".cash", ".casino", ".cat", ".catering", ".catholic", ".cba", ".cbn", ".cbre", ".cbs",
            ".cc", ".cd", ".ceb", ".center", ".ceo", ".cern", ".cf", ".cfa", ".cfd", ".cg", ".ch",
            ".chanel", ".channel", ".charity", ".chase", ".chat", ".cheap", ".chintai", ".chloe", ".christmas", ".chrome",
            ".chrysler", ".church", ".ci", ".cipriani", ".circle", ".cisco", ".citadel", ".citi", ".citic", ".city",
            ".cityeats", ".ck", ".cl", ".claims", ".cleaning", ".click", ".clinic", ".clinique", ".clothing", ".cloud",
            ".club", ".clubmed", ".cm", ".cn", ".co", ".coach", ".codes", ".coffee", ".college", ".cologne",
            //300
            ".com", ".comcast", ".commbank", ".community", ".company", ".compare", ".computer", ".comsec", ".condos", ".construction",
            ".consulting", ".contact", ".contractors", ".cooking", ".cookingchannel", ".cool", ".coop", ".corsica", ".country", ".coupon",
            ".coupons", ".courses", ".cr", ".credit", ".creditcard", ".creditunion", ".cricket", ".crown", ".crs", ".cruise",
            ".cruises", ".csc", ".cu", ".cuisinella", ".cv", ".cw", ".cx", ".cy", ".cymru", ".cyou",
            ".cz", ".dabur", ".dad", ".dance", ".data", ".date", ".dating", ".datsun", ".day", ".dclk",
            ".dds", ".de", ".deal", ".dealer", ".deals", ".degree", ".delivery", ".dell", ".deloitte", ".delta",
            ".democrat", ".dental", ".dentist", ".desi", ".design", ".dev", ".dhl", ".diamonds", ".diet", ".digital",
            ".direct", ".directory", ".discount", ".discover", ".dish", ".diy", ".dj", ".dk", ".dm", ".dnp",
            ".do", ".docs", ".doctor", ".dodge", ".dog", ".doha", ".domains", ".doosan", ".dot", ".download",
            ".drive", ".dtv", ".dubai", ".duck", ".dunlop", ".duns", ".dupont", ".durban", ".dvag", ".dvr",
            //400
            ".dz", ".earth", ".eat", ".ec", ".eco", ".edeka", ".edu", ".education", ".ee", ".eg",
            ".eh", ".email", ".emerck", ".energy", ".engineer", ".engineering", ".enterprises", ".epost", ".epson", ".equipment",
            ".er", ".ericsson", ".erni", ".es", ".esq", ".estate", ".esurance", ".et", ".etisalat", ".eu",
            ".eurovision", ".eus", ".events", ".everbank", ".exchange", ".expert", ".exposed", ".express", ".extraspace", ".fage",
            ".fail", ".fairwinds", ".faith", ".family", ".fan", ".fans", ".farm", ".farmers", ".fashion", ".fast",
            ".fedex", ".feedback", ".ferrari", ".ferrero", ".fi", ".fiat", ".fidelity", ".fido", ".film", ".final",
            ".finance", ".financial", ".fire", ".firestone", ".firmdale", ".fish", ".fishing", ".fit", ".fitness", ".fj",
            ".fk", ".flickr", ".flights", ".flir", ".florist", ".flowers", ".flsmidth", ".fly", ".fm", ".fo",
            ".foo", ".food", ".foodnetwork", ".football", ".ford", ".forex", ".forsale", ".forum", ".foundation", ".fox",
            ".fr", ".free", ".fresenius", ".frl", ".frogans", ".frontdoor", ".frontier", ".ftr", ".fujitsu", ".fujixerox",
            //500
            ".fun", ".fund", ".furniture", ".futbol", ".fyi", ".ga", ".gal", ".gallery", ".gallo", ".gallup",
            ".game", ".games", ".gap", ".garden", ".gay", ".gb", ".gbiz", ".gd", ".gdn", ".ge",
            ".gea", ".gent", ".genting", ".george", ".gf", ".gg", ".ggee", ".gh", ".gi", ".gift",
            ".gifts", ".gives", ".giving", ".gl", ".glade", ".glass", ".gle", ".global", ".globo", ".gm",
            ".gmail", ".gmbh", ".gmo", ".gmx", ".gn", ".godaddy", ".gold", ".goldpoint", ".golf", ".goo",
            ".goodhands", ".goodyear", ".goog", ".google", ".gop", ".got", ".gov", ".gp", ".gq", ".gr",
            ".grainger", ".graphics", ".gratis", ".green", ".gripe", ".grocery", ".group", ".gs", ".gt", ".gu",
            ".guardian", ".gucci", ".guge", ".guide", ".guitars", ".guru", ".gw", ".gy", ".hair", ".hamburg",
            ".hangout", ".haus", ".hbo", ".hdfc", ".hdfcbank", ".health", ".healthcare", ".help", ".helsinki", ".here",
            ".hermes", ".hgtv", ".hiphop", ".hisamitsu", ".hitachi", ".hiv", ".hk", ".hkt", ".hm", ".hn",
            //600
            ".hockey", ".holdings", ".holiday", ".homedepot", ".homegoods", ".homes", ".homesense", ".honda", ".honeywell", ".horse",
            ".hospital", ".host", ".hosting", ".hot", ".hoteles", ".hotels", ".hotmail", ".house", ".how", ".hr",
            ".hsbc", ".ht", ".htc", ".hu", ".hughes", ".hyatt", ".hyundai", ".ibm", ".icbc", ".ice",
            ".icu", ".id", ".ie", ".ieee", ".ifm", ".iinet", ".ikano", ".il", ".im", ".imamat",
            ".imdb", ".immo", ".immobilien", ".in", ".inc", ".industries", ".infiniti", ".info", ".ing", ".ink",
            ".institute", ".insurance", ".insure", ".int", ".intel", ".international", ".intuit", ".investments", ".io", ".ipiranga",
            ".iq", ".ir", ".irish", ".is", ".iselect", ".ismaili", ".ist", ".istanbul", ".it", ".itau",
            ".itv", ".iveco", ".iwc", ".jaguar", ".java", ".jcb", ".jcp", ".je", ".jeep", ".jetzt",
            ".jewelry", ".jio", ".jlc", ".jll", ".jm", ".jmp", ".jnj", ".jo", ".jobs", ".joburg",
            ".jot", ".joy", ".jp", ".jpmorgan", ".jprs", ".juegos", ".juniper", ".kaufen", ".kddi", ".ke",
            //700
            ".kerryhotels", ".kerrylogistics", ".kerryproperties", ".kfh", ".kg", ".kh", ".ki", ".kia", ".kim", ".kinder",
            ".kindle", ".kitchen", ".kiwi", ".km", ".kn", ".koeln", ".komatsu", ".kosher", ".kp", ".kpmg",
            ".kpn", ".kr", ".krd", ".kred", ".kuokgroup", ".kw", ".ky", ".kyoto", ".kz", ".la",
            ".lacaixa", ".ladbrokes", ".lamborghini", ".lamer", ".lancaster", ".lancia", ".lancome", ".land", ".landrover", ".lanxess",
            ".lasalle", ".lat", ".latino", ".latrobe", ".law", ".lawyer", ".lb", ".lc", ".lds", ".lease",
            ".leclerc", ".lefrak", ".legal", ".lego", ".lexus", ".lgbt", ".li", ".liaison", ".lidl", ".life",
            ".lifeinsurance", ".lifestyle", ".lighting", ".like", ".lilly", ".limited", ".limo", ".lincoln", ".linde", ".link",
            ".lipsy", ".live", ".living", ".lixil", ".lk", ".llc", ".loan", ".loans", ".locker", ".locus",
            ".loft", ".lol", ".london", ".lotte", ".lotto", ".love", ".lpl", ".lplfinancial", ".lr", ".ls",
            ".lt", ".ltd", ".ltda", ".lu", ".lundbeck", ".lupin", ".luxe", ".luxury", ".lv", ".ly",
            //800
            ".ma", ".macys", ".madrid", ".maif", ".maison", ".makeup", ".man", ".management", ".mango", ".map",
            ".market", ".marketing", ".markets", ".marriott", ".marshalls", ".maserati", ".mattel", ".mba", ".mc", ".mcd",
            ".mcdonalds", ".mckinsey", ".md", ".me", ".med", ".media", ".meet", ".melbourne", ".meme", ".memorial",
            ".men", ".menu", ".meo", ".merckmsd", ".metlife", ".mf", ".mg", ".mh", ".miami", ".microsoft",
            ".mil", ".mini", ".mint", ".mit", ".mitsubishi", ".mk", ".ml", ".mlb", ".mls", ".mm",
            ".mma", ".mn", ".mo", ".mobi", ".mobile", ".mobily", ".moda", ".moe", ".moi", ".mom",
            ".monash", ".money", ".monster", ".montblanc", ".mopar", ".mormon", ".mortgage", ".moscow", ".moto", ".motorcycles",
            ".mov", ".movie", ".movistar", ".mp", ".mq", ".mr", ".ms", ".msd", ".mt", ".mtn",
            ".mtpc", ".mtr", ".mu", ".museum", ".mutual", ".mutuelle", ".mv", ".mw", ".mx", ".my",
            ".mz", ".na", ".nab", ".nadex", ".nagoya", ".name", ".nationwide", ".natura", ".navy",
            //900
            ".nba", ".nc", ".ne", ".nec", ".net", ".netbank", ".netflix", ".network", ".neustar", ".new",
            ".newholland", ".news", ".next", ".nextdirect", ".nexus", ".nf", ".nfl", ".ng", ".ngo", ".nhk",
            ".ni", ".nico", ".nike", ".nikon", ".ninja", ".nissan", ".nissay", ".nl", ".no", ".nokia",
            ".northwesternmutual", ".norton", ".now", ".nowruz", ".nowtv", ".np", ".nr", ".nra", ".nrw", ".ntt",
            ".nu", ".nyc", ".nz", ".obi", ".observer", ".off", ".office", ".okinawa", ".olayan", ".olayangroup",
            ".oldnavy", ".ollo", ".om", ".omega", ".one", ".ong", ".onl", ".online", ".onyourside", ".ooo",
            ".open", ".oracle", ".orange", ".org", ".organic", ".orientexpress", ".origins", ".osaka", ".otsuka", ".ott",
            ".ovh", ".pa", ".page", ".pamperedchef", ".panasonic", ".panerai", ".paris", ".pars", ".partners", ".parts",
            ".party", ".passagens", ".pay", ".pccw", ".pe", ".pet", ".pf", ".pfizer", ".pg", ".ph",
            ".pharmacy", ".phd", ".philips", ".phone", ".photo", ".photography", ".photos", ".physio", ".piaget", ".pics",
            //1000
            ".pictet", ".pictures", ".pid", ".pin", ".ping", ".pink", ".pioneer", ".pizza", ".pk", ".pl",
            ".place", ".play", ".playstation", ".plumbing", ".plus", ".pm", ".pn", ".pnc", ".pohl", ".poker",
            ".politie", ".porn", ".post", ".pr", ".pramerica", ".praxi", ".press", ".prime", ".pro", ".prod",
            ".productions", ".prof", ".progressive", ".promo", ".properties", ".property", ".protection", ".pru", ".prudential", ".ps", ".pt",
            ".pub", ".pw", ".pwc", ".py", ".qa", ".qpon", ".quebec", ".quest", ".qvc", ".racing",
            ".radio", ".raid", ".re", ".read", ".realestate", ".realtor", ".realty", ".recipes", ".red", ".redstone",
            ".redumbrella", ".rehab", ".reise", ".reisen", ".reit", ".reliance", ".ren", ".rent", ".rentals", ".repair",
            ".report", ".republican", ".rest", ".restaurant", ".review", ".reviews", ".rexroth", ".rich", ".richardli", ".ricoh",
            ".rightathome", ".ril", ".rio", ".rip", ".rmit", ".ro", ".rocher", ".rocks", ".rodeo", ".rogers",
            ".room", ".rs", ".rsvp", ".ru", ".rugby", ".ruhr", ".run", ".rw", ".rwe", ".ryukyu",
            //1100
            ".sa", ".saarland", ".safe", ".safety", ".sakura", ".sale", ".salon", ".samsclub", ".samsung", ".sandvik",
            ".sandvikcoromant", ".sanofi", ".sap", ".sapo", ".sarl", ".sas", ".save", ".saxo", ".sb", ".sbi",
            ".sbs", ".sc", ".sca", ".scb", ".schaeffler", ".schmidt", ".scholarships", ".school", ".schule", ".schwarz",
            ".science", ".scjohnson", ".scor", ".scot", ".sd", ".se", ".search", ".seat", ".secure", ".security",
            ".seek", ".select", ".sener", ".services", ".ses", ".seven", ".sew", ".sex", ".sexy", ".sfr",
            ".sg", ".sh", ".shangrila", ".sharp", ".shaw", ".shell", ".shia", ".shiksha", ".shoes", ".shop",
            ".shopping", ".shouji", ".show", ".showtime", ".shriram", ".si", ".silk", ".sina", ".singles", ".site",
            ".sj", ".sk", ".ski", ".skin", ".sky", ".skype", ".sl", ".sling", ".sm", ".smart",
            ".smile", ".sn", ".sncf", ".so", ".soccer", ".social", ".softbank", ".software", ".sohu", ".solar",
            ".solutions", ".song", ".sony", ".soy", ".space", ".spiegel", ".sport", ".spot", ".spreadbetting", ".sr",
            //1200
            ".srl", ".srt", ".ss", ".st", ".stada", ".staples", ".star", ".starhub", ".statebank", ".statefarm",
            ".statoil", ".stc", ".stcgroup", ".stockholm", ".storage", ".store", ".stream", ".studio", ".study", ".style",
            ".su", ".sucks", ".supplies", ".supply", ".support", ".surf", ".surgery", ".suzuki", ".sv", ".swatch",
            ".swiftcover", ".swiss", ".sx", ".sy", ".sydney", ".symantec", ".systems", ".sz", ".tab", ".taipei",
            ".talk", ".taobao", ".target", ".tatamotors", ".tatar", ".tattoo", ".tax", ".taxi", ".tc", ".tci",
            ".td", ".tdk", ".team", ".tech", ".technology", ".tel", ".telecity", ".telefonica", ".temasek", ".tennis",
            ".teva", ".tf", ".tg", ".th", ".thd", ".theater", ".theatre", ".tiaa", ".tickets", ".tienda",
            ".tiffany", ".tips", ".tires", ".tirol", ".tj", ".tjmaxx", ".tjx", ".tk", ".tkmaxx", ".tl",
            ".tm", ".tmall", ".tn", ".to", ".today", ".tokyo", ".tools", ".top", ".toray", ".toshiba",
            ".total", ".tours", ".town", ".toyota", ".toys", ".tp", ".tr", ".trade", ".trading", ".training",
            //1300
            ".travel", ".travelchannel", ".travelers", ".travelersinsurance", ".trust", ".trv", ".tt", ".tube", ".tui",
            ".tunes", ".tushu", ".tv", ".tvs", ".tw", ".tz", ".ua", ".ubank", ".ubs", ".uconnect",
            ".ug", ".uk", ".um", ".unicom", ".university", ".uno", ".uol", ".ups", ".us", ".uy",
            ".uz", ".va", ".vacations", ".vana", ".vanguard", ".vc", ".ve", ".vegas", ".ventures", ".verisign",
            ".versicherung", ".vet", ".vg", ".vi", ".viajes", ".video", ".vig", ".viking", ".villas", ".vin",
            ".vip", ".virgin", ".visa", ".vision", ".vista", ".vistaprint", ".viva", ".vivo", ".vlaanderen", ".vn",
            ".vodka", ".volkswagen", ".volvo", ".vote", ".voting", ".voto", ".voyage", ".vu", ".vuelos", ".wales",
            ".walmart", ".walter", ".wang", ".wanggou", ".warman", ".watch", ".watches", ".weather", ".weatherchannel", ".webcam",
            ".weber", ".website", ".wed", ".wedding", ".weibo", ".weir", ".wf", ".whoswho", ".wien", ".wiki", ".williamhill",
            ".win", ".windows", ".wine", ".winners", ".wme", ".wolterskluwer", ".woodside", ".work", ".works", ".world",
            //1400
            ".wow", ".ws", ".wtc", ".wtf", ".xbox", ".xerox", ".xfinity", ".xihuan", ".xin", ".セル",
            ".佛山", ".慈善", ".集", ".在", ".한국", ".大汽", ".点看", ".八卦", ".公益", ".公司",
            ".香格里拉", ".站", ".移", ".我", ".москва", ".испытание", ".аз", ".католик", ".онлайн", ".сайт",
            ".通", ".срб", ".бг", ".бел", ".微博", ".테스트", ".淡", ".ファッション", ".орг", ".ストア",
            ".삼성", ".商", ".商店", ".商城", ".дети", ".мкд", ".ею", ".ポイント", ".新", ".工行",
            ".家電", ".中文", ".中信", ".中", ".中國", ".谷歌", ".電訊盈科", ".物", ".測試", ".クラウド",
            ".通販", ".店", ".餐", ".ком", ".укр", ".香港", ".基", ".食品", ".δοκιμ", ".利浦",
            ".台", ".台灣", ".手表", ".手机", ".мон", ".澳門", ".닷컴", ".政府", ".机", ".机",
            ".健康", ".招聘", ".рус", ".рф", ".珠", ".大拿", ".みんな", ".ググル", ".ευ", ".ελ",
            ".世界", ".書籍", ".址", ".닷넷", ".コム", ".天主", ".游", ".vermogensberater", ".vermogensberatung", ".企",
            //1500
            ".信息", ".嘉里大酒店", ".嘉里", ".新加坡", ".テスト", ".政", ".xperia", ".xxx", ".xyz", ".yachts",
            ".yahoo", ".yamaxun", ".yandex", ".ye", ".yodobashi", ".yoga", ".yokohama", ".you", ".youtube", ".yt",
            ".yun", ".za", ".zappos", ".zara", ".zero", ".zip", ".zippo", ".zm", ".zone", ".zuerich", ".zw"
            //1530
    };

    public static boolean isLink(@NotNull String rawMessage) {
        String topLevelDomain = null;
        for(String s : webList) {
            if(rawMessage.contains(s)) {
                topLevelDomain = s;
                break;
            }
        }
        if(topLevelDomain == null) {
            return false;
        }
        if (rawMessage.startsWith("http://")) {
            rawMessage = rawMessage.replaceFirst("http://", "");
        }

        if (rawMessage.startsWith("https://")) {
            rawMessage = rawMessage.replaceFirst("https://", "");
        }

        Pattern p = Pattern.compile("[A-Za-z0-9_]+" + topLevelDomain + "$");
        Matcher m = p.matcher(rawMessage);
        if (rawMessage.contains(" ")) {
            p = Pattern.compile("\\s[A-Za-z0-9_]+" + topLevelDomain + "$");
            m = p.matcher(rawMessage);
        }

        boolean hangulFlag = false;
        if (!m.find()) {
            p = Pattern.compile("[가-힣][A-Za-z0-9_]+" + topLevelDomain + "$");
            m = p.matcher(rawMessage);
            hangulFlag = true;
        }

        int i = 0;

        while(m.find()) {
            try {
                String text = m.group(i);
                if (text.contains(" ")) {
                    text = text.replaceFirst(" ", "");
                }

                if (hangulFlag) {
                    text = text.substring(1);
                }

                URL url = new URI("https://" + text).toURL();
                link = text;
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                if (connection.getResponseCode() == 200 || connection.getResponseCode() == 202 || connection.getResponseCode() == 301 || connection.getResponseCode() == 302 || connection.getResponseCode() == 204) {
                    return true;
                }

                ++i;
                if (i < m.groupCount()) {
                    return false;
                }
            } catch (Exception var9) {
                var9.printStackTrace();
                return false;
            }
        }

        return false;
    }

    public static String getLink() {
        return link;
    }
}