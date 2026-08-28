/* Contenu pédagogique : alphabet, vocabulaire de base, phrases.
   translit = translittération phonétique (prononciation approximative en français). */

const ALPHABET = [
  { letter: "א", name: "Alef", translit: "muette", desc: "Lettre muette, sert de support à une voyelle.", example: { he: "אבא", translit: "aba", fr: "papa" } },
  { letter: "ב", name: "Bet / Vet", translit: "b / v", desc: "« b » (avec point) ou « v » (sans point).", example: { he: "בית", translit: "bayit", fr: "maison" } },
  { letter: "ג", name: "Guimel", translit: "g (dur)", desc: "Comme le « g » de « gare ».", example: { he: "גמל", translit: "gamal", fr: "chameau" } },
  { letter: "ד", name: "Dalet", translit: "d", desc: "Comme le « d » français.", example: { he: "דג", translit: "dag", fr: "poisson" } },
  { letter: "ה", name: "Hé", translit: "h", desc: "« h » aspiré léger, souvent muet en fin de mot.", example: { he: "הר", translit: "har", fr: "montagne" } },
  { letter: "ו", name: "Vav", translit: "v / ou", desc: "« v », ou voyelle « ou »/« o ».", example: { he: "ורד", translit: "vered", fr: "rose (fleur)" } },
  { letter: "ז", name: "Zayin", translit: "z", desc: "Comme le « z » français.", example: { he: "זהב", translit: "zahav", fr: "or" } },
  { letter: "ח", name: "Het", translit: "kh", desc: "Raclée au fond de la gorge, comme la « jota » espagnole.", example: { he: "חלב", translit: "khalav", fr: "lait" } },
  { letter: "ט", name: "Tet", translit: "t", desc: "Comme le « t » français.", example: { he: "טוב", translit: "tov", fr: "bon" } },
  { letter: "י", name: "Yod", translit: "y", desc: "Comme le « y » de « yaourt ».", example: { he: "ילד", translit: "yeled", fr: "garçon / enfant" } },
  { letter: "כ", name: "Kaf / Khaf", translit: "k / kh", desc: "« k » (avec point) ou « kh » raclé (sans point).", example: { he: "כלב", translit: "kelev", fr: "chien" }, final: { letter: "ך", desc: "Forme finale : s'écrit ainsi en fin de mot.", example: { he: "מלך", translit: "melekh", fr: "roi" } } },
  { letter: "ל", name: "Lamed", translit: "l", desc: "Comme le « l » français.", example: { he: "לב", translit: "lev", fr: "cœur" } },
  { letter: "מ", name: "Mem", translit: "m", desc: "Comme le « m » français.", example: { he: "מים", translit: "mayim", fr: "eau" }, final: { letter: "ם", desc: "Forme finale : s'écrit ainsi en fin de mot.", example: { he: "יום", translit: "yom", fr: "jour" } } },
  { letter: "נ", name: "Noun", translit: "n", desc: "Comme le « n » français.", example: { he: "נר", translit: "ner", fr: "bougie" }, final: { letter: "ן", desc: "Forme finale : s'écrit ainsi en fin de mot.", example: { he: "גן", translit: "gan", fr: "jardin" } } },
  { letter: "ס", name: "Samekh", translit: "s", desc: "Comme le « s » français.", example: { he: "ספר", translit: "sefer", fr: "livre" } },
  { letter: "ע", name: "Ayin", translit: "muette*", desc: "Son guttural très léger, quasi muet en hébreu moderne.", example: { he: "עין", translit: "ayin", fr: "œil" } },
  { letter: "פ", name: "Pé / Fé", translit: "p / f", desc: "« p » (avec point) ou « f » (sans point).", example: { he: "פרח", translit: "perakh", fr: "fleur" }, final: { letter: "ף", desc: "Forme finale : s'écrit ainsi en fin de mot.", example: { he: "כף", translit: "kaf", fr: "cuillère" } } },
  { letter: "צ", name: "Tsadi", translit: "ts", desc: "Comme « ts » dans « tsar ».", example: { he: "צל", translit: "tsel", fr: "ombre" }, final: { letter: "ץ", desc: "Forme finale : s'écrit ainsi en fin de mot.", example: { he: "עץ", translit: "ets", fr: "arbre" } } },
  { letter: "ק", name: "Qof", translit: "k", desc: "Comme le « k » français.", example: { he: "קפה", translit: "kafe", fr: "café" } },
  { letter: "ר", name: "Resh", translit: "r", desc: "« r » roulé/guttural, à l'arrière de la gorge.", example: { he: "ראש", translit: "rosh", fr: "tête" } },
  { letter: "ש", name: "Shin / Sin", translit: "ch / s", desc: "« ch » (point à gauche) ou « s » (point à droite).", example: { he: "שמש", translit: "shemesh", fr: "soleil" } },
  { letter: "ת", name: "Tav", translit: "t", desc: "Comme le « t » français.", example: { he: "תפוח", translit: "tapouakh", fr: "pomme" } }
];

const VOCAB_CATEGORIES = [
  {
    title: "Salutations",
    words: [
      { he: "שלום", translit: "shalom", fr: "bonjour / au revoir / paix" },
      { he: "בוקר טוב", translit: "boker tov", fr: "bonjour (le matin)" },
      { he: "ערב טוב", translit: "erev tov", fr: "bonsoir" },
      { he: "לילה טוב", translit: "laila tov", fr: "bonne nuit" },
      { he: "להתראות", translit: "lehitra'ot", fr: "au revoir" },
      { he: "מה שלומך", translit: "ma shlomkha", fr: "comment vas-tu ?" },
      { he: "טוב, תודה", translit: "tov, toda", fr: "bien, merci" },
      { he: "תודה", translit: "toda", fr: "merci" },
      { he: "תודה רבה", translit: "toda raba", fr: "merci beaucoup" },
      { he: "בבקשה", translit: "bevakasha", fr: "s'il te plaît / je t'en prie" },
      { he: "סליחה", translit: "slikha", fr: "excuse-moi / pardon" },
      { he: "כן", translit: "ken", fr: "oui" },
      { he: "לא", translit: "lo", fr: "non" },
      { he: "נעים מאוד", translit: "na'im me'od", fr: "enchanté" },
      { he: "שלום עליכם", translit: "shalom aleikhem", fr: "salut à tous (traditionnel)" },
      { he: "ברוך הבא", translit: "barukh haba", fr: "bienvenue (à un homme)" },
      { he: "ברוכה הבאה", translit: "brukha haba'a", fr: "bienvenue (à une femme)" },
      { he: "מה קורה", translit: "ma kore", fr: "quoi de neuf (informel)" },
      { he: "מה המצב", translit: "ma hamatsav", fr: "comment ça va (informel)" },
      { he: "כל טוב", translit: "kol tuv", fr: "tout le meilleur (formule de politesse)" },
      { he: "נתראה", translit: "nitra'e", fr: "à bientôt (informel)" },
      { he: "אין בעד מה", translit: "ein be'ad ma", fr: "de rien / il n'y a pas de quoi" },
      { he: "בשמחה", translit: "besimkha", fr: "avec plaisir" },
      { he: "שבת שלום", translit: "shabbat shalom", fr: "bon shabbat" },
      { he: "שנה טובה", translit: "shana tova", fr: "bonne année" },
      { he: "יום טוב", translit: "yom tov", fr: "bonne journée / jour de fête" },
      { he: "בהצלחה", translit: "behatslakha", fr: "bonne chance" },
      { he: "מזל טוב", translit: "mazal tov", fr: "félicitations" }
    ]
  },
  {
    title: "Nombres (0-100)",
    words: [
      { he: "אפס", translit: "efes", fr: "0" },
      { he: "אחד", translit: "ekhad", fr: "1" },
      { he: "שתיים", translit: "shtayim", fr: "2" },
      { he: "שלוש", translit: "shalosh", fr: "3" },
      { he: "ארבע", translit: "arba", fr: "4" },
      { he: "חמש", translit: "khamesh", fr: "5" },
      { he: "שש", translit: "shesh", fr: "6" },
      { he: "שבע", translit: "sheva", fr: "7" },
      { he: "שמונה", translit: "shmone", fr: "8" },
      { he: "תשע", translit: "tesha", fr: "9" },
      { he: "עשר", translit: "eser", fr: "10" },
      { he: "אחת עשרה", translit: "akhat-esreh", fr: "11" },
      { he: "שתים עשרה", translit: "shteim-esreh", fr: "12" },
      { he: "שלוש עשרה", translit: "shlosh-esreh", fr: "13" },
      { he: "ארבע עשרה", translit: "arba-esreh", fr: "14" },
      { he: "חמש עשרה", translit: "khamesh-esreh", fr: "15" },
      { he: "שש עשרה", translit: "shesh-esreh", fr: "16" },
      { he: "שבע עשרה", translit: "shva-esreh", fr: "17" },
      { he: "שמונה עשרה", translit: "shmoneh-esreh", fr: "18" },
      { he: "תשע עשרה", translit: "tsha-esreh", fr: "19" },
      { he: "עשרים", translit: "esrim", fr: "20" },
      { he: "עשרים ואחד", translit: "esrim ve-ekhad", fr: "21" },
      { he: "עשרים ושתיים", translit: "esrim ve-shtayim", fr: "22" },
      { he: "עשרים ושלוש", translit: "esrim ve-shalosh", fr: "23" },
      { he: "עשרים וארבע", translit: "esrim ve-arba", fr: "24" },
      { he: "עשרים וחמש", translit: "esrim ve-khamesh", fr: "25" },
      { he: "עשרים ושש", translit: "esrim ve-shesh", fr: "26" },
      { he: "עשרים ושבע", translit: "esrim ve-sheva", fr: "27" },
      { he: "עשרים ושמונה", translit: "esrim ve-shmone", fr: "28" },
      { he: "עשרים ותשע", translit: "esrim ve-tesha", fr: "29" },
      { he: "שלושים", translit: "shloshim", fr: "30" },
      { he: "שלושים ואחד", translit: "shloshim ve-ekhad", fr: "31" },
      { he: "שלושים ושתיים", translit: "shloshim ve-shtayim", fr: "32" },
      { he: "שלושים ושלוש", translit: "shloshim ve-shalosh", fr: "33" },
      { he: "שלושים וארבע", translit: "shloshim ve-arba", fr: "34" },
      { he: "שלושים וחמש", translit: "shloshim ve-khamesh", fr: "35" },
      { he: "שלושים ושש", translit: "shloshim ve-shesh", fr: "36" },
      { he: "שלושים ושבע", translit: "shloshim ve-sheva", fr: "37" },
      { he: "שלושים ושמונה", translit: "shloshim ve-shmone", fr: "38" },
      { he: "שלושים ותשע", translit: "shloshim ve-tesha", fr: "39" },
      { he: "ארבעים", translit: "arba'im", fr: "40" },
      { he: "ארבעים ואחד", translit: "arba'im ve-ekhad", fr: "41" },
      { he: "ארבעים ושתיים", translit: "arba'im ve-shtayim", fr: "42" },
      { he: "ארבעים ושלוש", translit: "arba'im ve-shalosh", fr: "43" },
      { he: "ארבעים וארבע", translit: "arba'im ve-arba", fr: "44" },
      { he: "ארבעים וחמש", translit: "arba'im ve-khamesh", fr: "45" },
      { he: "ארבעים ושש", translit: "arba'im ve-shesh", fr: "46" },
      { he: "ארבעים ושבע", translit: "arba'im ve-sheva", fr: "47" },
      { he: "ארבעים ושמונה", translit: "arba'im ve-shmone", fr: "48" },
      { he: "ארבעים ותשע", translit: "arba'im ve-tesha", fr: "49" },
      { he: "חמישים", translit: "khamishim", fr: "50" },
      { he: "חמישים ואחד", translit: "khamishim ve-ekhad", fr: "51" },
      { he: "חמישים ושתיים", translit: "khamishim ve-shtayim", fr: "52" },
      { he: "חמישים ושלוש", translit: "khamishim ve-shalosh", fr: "53" },
      { he: "חמישים וארבע", translit: "khamishim ve-arba", fr: "54" },
      { he: "חמישים וחמש", translit: "khamishim ve-khamesh", fr: "55" },
      { he: "חמישים ושש", translit: "khamishim ve-shesh", fr: "56" },
      { he: "חמישים ושבע", translit: "khamishim ve-sheva", fr: "57" },
      { he: "חמישים ושמונה", translit: "khamishim ve-shmone", fr: "58" },
      { he: "חמישים ותשע", translit: "khamishim ve-tesha", fr: "59" },
      { he: "שישים", translit: "shishim", fr: "60" },
      { he: "שישים ואחד", translit: "shishim ve-ekhad", fr: "61" },
      { he: "שישים ושתיים", translit: "shishim ve-shtayim", fr: "62" },
      { he: "שישים ושלוש", translit: "shishim ve-shalosh", fr: "63" },
      { he: "שישים וארבע", translit: "shishim ve-arba", fr: "64" },
      { he: "שישים וחמש", translit: "shishim ve-khamesh", fr: "65" },
      { he: "שישים ושש", translit: "shishim ve-shesh", fr: "66" },
      { he: "שישים ושבע", translit: "shishim ve-sheva", fr: "67" },
      { he: "שישים ושמונה", translit: "shishim ve-shmone", fr: "68" },
      { he: "שישים ותשע", translit: "shishim ve-tesha", fr: "69" },
      { he: "שבעים", translit: "shiv'im", fr: "70" },
      { he: "שבעים ואחד", translit: "shiv'im ve-ekhad", fr: "71" },
      { he: "שבעים ושתיים", translit: "shiv'im ve-shtayim", fr: "72" },
      { he: "שבעים ושלוש", translit: "shiv'im ve-shalosh", fr: "73" },
      { he: "שבעים וארבע", translit: "shiv'im ve-arba", fr: "74" },
      { he: "שבעים וחמש", translit: "shiv'im ve-khamesh", fr: "75" },
      { he: "שבעים ושש", translit: "shiv'im ve-shesh", fr: "76" },
      { he: "שבעים ושבע", translit: "shiv'im ve-sheva", fr: "77" },
      { he: "שבעים ושמונה", translit: "shiv'im ve-shmone", fr: "78" },
      { he: "שבעים ותשע", translit: "shiv'im ve-tesha", fr: "79" },
      { he: "שמונים", translit: "shmonim", fr: "80" },
      { he: "שמונים ואחד", translit: "shmonim ve-ekhad", fr: "81" },
      { he: "שמונים ושתיים", translit: "shmonim ve-shtayim", fr: "82" },
      { he: "שמונים ושלוש", translit: "shmonim ve-shalosh", fr: "83" },
      { he: "שמונים וארבע", translit: "shmonim ve-arba", fr: "84" },
      { he: "שמונים וחמש", translit: "shmonim ve-khamesh", fr: "85" },
      { he: "שמונים ושש", translit: "shmonim ve-shesh", fr: "86" },
      { he: "שמונים ושבע", translit: "shmonim ve-sheva", fr: "87" },
      { he: "שמונים ושמונה", translit: "shmonim ve-shmone", fr: "88" },
      { he: "שמונים ותשע", translit: "shmonim ve-tesha", fr: "89" },
      { he: "תשעים", translit: "tish'im", fr: "90" },
      { he: "תשעים ואחד", translit: "tish'im ve-ekhad", fr: "91" },
      { he: "תשעים ושתיים", translit: "tish'im ve-shtayim", fr: "92" },
      { he: "תשעים ושלוש", translit: "tish'im ve-shalosh", fr: "93" },
      { he: "תשעים וארבע", translit: "tish'im ve-arba", fr: "94" },
      { he: "תשעים וחמש", translit: "tish'im ve-khamesh", fr: "95" },
      { he: "תשעים ושש", translit: "tish'im ve-shesh", fr: "96" },
      { he: "תשעים ושבע", translit: "tish'im ve-sheva", fr: "97" },
      { he: "תשעים ושמונה", translit: "tish'im ve-shmone", fr: "98" },
      { he: "תשעים ותשע", translit: "tish'im ve-tesha", fr: "99" },
      { he: "מאה", translit: "me'a", fr: "100" }
    ]
  },
  {
    title: "Couleurs",
    words: [
      { he: "אדום", translit: "adom", fr: "rouge" },
      { he: "כחול", translit: "kakhol", fr: "bleu" },
      { he: "ירוק", translit: "yarok", fr: "vert" },
      { he: "צהוב", translit: "tsahov", fr: "jaune" },
      { he: "שחור", translit: "shakhor", fr: "noir" },
      { he: "לבן", translit: "lavan", fr: "blanc" },
      { he: "כתום", translit: "katom", fr: "orange" },
      { he: "סגול", translit: "sagol", fr: "violet" },
      { he: "ורוד", translit: "varod", fr: "rose" },
      { he: "חום", translit: "khum", fr: "marron" },
      { he: "אפור", translit: "afor", fr: "gris" },
      { he: "תכלת", translit: "tkhelet", fr: "bleu ciel" },
      { he: "טורקיז", translit: "turkiz", fr: "turquoise" },
      { he: "זהוב", translit: "zahov", fr: "doré" },
      { he: "כסוף", translit: "ksuf", fr: "argenté" },
      { he: "בורדו", translit: "bordo", fr: "bordeaux" }
    ]
  },
  {
    title: "Jours de la semaine",
    words: [
      { he: "יום ראשון", translit: "yom rishon", fr: "dimanche" },
      { he: "יום שני", translit: "yom sheni", fr: "lundi" },
      { he: "יום שלישי", translit: "yom shlishi", fr: "mardi" },
      { he: "יום רביעי", translit: "yom revi'i", fr: "mercredi" },
      { he: "יום חמישי", translit: "yom khamishi", fr: "jeudi" },
      { he: "יום שישי", translit: "yom shishi", fr: "vendredi" },
      { he: "שבת", translit: "shabbat", fr: "samedi" }
    ]
  },
  {
    title: "Famille",
    words: [
      { he: "משפחה", translit: "mishpakha", fr: "famille" },
      { he: "אמא", translit: "ima", fr: "maman" },
      { he: "אבא", translit: "aba", fr: "papa" },
      { he: "אח", translit: "akh", fr: "frère" },
      { he: "אחות", translit: "akhot", fr: "sœur" },
      { he: "סבא", translit: "saba", fr: "grand-père" },
      { he: "סבתא", translit: "savta", fr: "grand-mère" },
      { he: "בן", translit: "ben", fr: "fils" },
      { he: "בת", translit: "bat", fr: "fille" },
      { he: "דוד", translit: "dod", fr: "oncle" },
      { he: "דודה", translit: "doda", fr: "tante" },
      { he: "בן דוד", translit: "ben dod", fr: "cousin" },
      { he: "בת דודה", translit: "bat doda", fr: "cousine" },
      { he: "נכד", translit: "nekhed", fr: "petit-fils" },
      { he: "נכדה", translit: "nekhda", fr: "petite-fille" },
      { he: "בעל", translit: "ba'al", fr: "mari" },
      { he: "אישה", translit: "isha", fr: "femme / épouse" },
      { he: "הורים", translit: "horim", fr: "parents" },
      { he: "ילדה", translit: "yalda", fr: "fille (enfant)" },
      { he: "תינוק", translit: "tinok", fr: "bébé" },
      { he: "גיס", translit: "gis", fr: "beau-frère" },
      { he: "גיסה", translit: "gisa", fr: "belle-sœur" },
      { he: "תאומים", translit: "te'omim", fr: "jumeaux" }
    ]
  },
  {
    title: "Mots du quotidien",
    words: [
      { he: "מים", translit: "mayim", fr: "eau" },
      { he: "לחם", translit: "lekhem", fr: "pain" },
      { he: "אוכל", translit: "okhel", fr: "nourriture" },
      { he: "בית", translit: "bayit", fr: "maison" },
      { he: "כסף", translit: "kesef", fr: "argent" },
      { he: "זמן", translit: "zman", fr: "temps" },
      { he: "היום", translit: "hayom", fr: "aujourd'hui" },
      { he: "מחר", translit: "makhar", fr: "demain" },
      { he: "אתמול", translit: "etmol", fr: "hier" },
      { he: "ספר", translit: "sefer", fr: "livre" },
      { he: "עט", translit: "et", fr: "stylo" },
      { he: "שולחן", translit: "shulkhan", fr: "table" },
      { he: "כיסא", translit: "kise", fr: "chaise" },
      { he: "דלת", translit: "delet", fr: "porte" },
      { he: "חלון", translit: "khalon", fr: "fenêtre" },
      { he: "מכונית", translit: "mekhonit", fr: "voiture" },
      { he: "אוטובוס", translit: "otobus", fr: "bus" },
      { he: "רכבת", translit: "rakevet", fr: "train" },
      { he: "מטוס", translit: "matos", fr: "avion" },
      { he: "טלפון", translit: "telefon", fr: "téléphone" },
      { he: "מחשב", translit: "makhshev", fr: "ordinateur" },
      { he: "עבודה", translit: "avoda", fr: "travail" },
      { he: "בית ספר", translit: "beit sefer", fr: "école" },
      { he: "חבר", translit: "khaver", fr: "ami" },
      { he: "חברה", translit: "khavera", fr: "amie" },
      { he: "שם", translit: "shem", fr: "nom" },
      { he: "מספר", translit: "mispar", fr: "nombre / numéro" },
      { he: "עיר", translit: "ir", fr: "ville" },
      { he: "רחוב", translit: "rekhov", fr: "rue" },
      { he: "דרך", translit: "derekh", fr: "chemin / route" },
      { he: "שעה", translit: "sha'a", fr: "heure" },
      { he: "דקה", translit: "daka", fr: "minute" },
      { he: "שנה", translit: "shana", fr: "année" },
      { he: "שבוע", translit: "shavua", fr: "semaine" },
      { he: "חודש", translit: "khodesh", fr: "mois" }
    ]
  },
  {
    title: "Animaux",
    words: [
      { he: "כלב", translit: "kelev", fr: "chien" },
      { he: "חתול", translit: "khatul", fr: "chat" },
      { he: "ציפור", translit: "tsipor", fr: "oiseau" },
      { he: "סוס", translit: "sus", fr: "cheval" },
      { he: "פרה", translit: "para", fr: "vache" },
      { he: "אריה", translit: "arye", fr: "lion" },
      { he: "פיל", translit: "pil", fr: "éléphant" },
      { he: "דוב", translit: "dov", fr: "ours" },
      { he: "קוף", translit: "kof", fr: "singe" },
      { he: "ארנב", translit: "arnav", fr: "lapin" },
      { he: "נמר", translit: "namer", fr: "tigre / léopard" },
      { he: "זאב", translit: "ze'ev", fr: "loup" },
      { he: "שועל", translit: "shu'al", fr: "renard" },
      { he: "דולפין", translit: "dolfin", fr: "dauphin" },
      { he: "לוויתן", translit: "livyatan", fr: "baleine" },
      { he: "כריש", translit: "karish", fr: "requin" },
      { he: "דג", translit: "dag", fr: "poisson" },
      { he: "צב", translit: "tsav", fr: "tortue" },
      { he: "נחש", translit: "nakhash", fr: "serpent" },
      { he: "תנין", translit: "tanin", fr: "crocodile" },
      { he: "זברה", translit: "zebra", fr: "zèbre" },
      { he: "ג'ירפה", translit: "jirafa", fr: "girafe" },
      { he: "קנגורו", translit: "kangaru", fr: "kangourou" },
      { he: "פינגווין", translit: "pingvin", fr: "pingouin" },
      { he: "תרנגול", translit: "tarnegol", fr: "coq" },
      { he: "תרנגולת", translit: "tarnegolet", fr: "poule" },
      { he: "ברווז", translit: "barvaz", fr: "canard" },
      { he: "עז", translit: "ez", fr: "chèvre" },
      { he: "חזיר", translit: "khazir", fr: "cochon" },
      { he: "עכבר", translit: "akhbar", fr: "souris" },
      { he: "פרפר", translit: "parpar", fr: "papillon" },
      { he: "דבורה", translit: "dvora", fr: "abeille" },
      { he: "נמלה", translit: "nemala", fr: "fourmi" },
      { he: "עכביש", translit: "akavish", fr: "araignée" },
      { he: "תולעת", translit: "tola'at", fr: "ver" }
    ]
  },
  {
    title: "Nature",
    words: [
      { he: "ירח", translit: "yareakh", fr: "lune" },
      { he: "כוכב", translit: "kokhav", fr: "étoile" },
      { he: "שמיים", translit: "shamayim", fr: "ciel" },
      { he: "ים", translit: "yam", fr: "mer" },
      { he: "נהר", translit: "nahar", fr: "rivière" },
      { he: "יער", translit: "ya'ar", fr: "forêt" },
      { he: "אש", translit: "esh", fr: "feu" },
      { he: "אוויר", translit: "avir", fr: "air" },
      { he: "שמש", translit: "shemesh", fr: "soleil" },
      { he: "אדמה", translit: "adama", fr: "terre / sol" },
      { he: "אבן", translit: "even", fr: "pierre" },
      { he: "חול", translit: "khol", fr: "sable" },
      { he: "גשם", translit: "geshem", fr: "pluie" },
      { he: "שלג", translit: "sheleg", fr: "neige" },
      { he: "רוח", translit: "ruakh", fr: "vent" },
      { he: "ענן", translit: "anan", fr: "nuage" },
      { he: "קשת", translit: "keshet", fr: "arc-en-ciel" },
      { he: "אגם", translit: "agam", fr: "lac" },
      { he: "אוקיינוס", translit: "okyanus", fr: "océan" },
      { he: "הר", translit: "har", fr: "montagne" },
      { he: "גבעה", translit: "giv'a", fr: "colline" },
      { he: "עמק", translit: "emek", fr: "vallée" },
      { he: "מדבר", translit: "midbar", fr: "désert" },
      { he: "פרח", translit: "perakh", fr: "fleur" },
      { he: "עלה", translit: "ale", fr: "feuille" },
      { he: "שורש", translit: "shoresh", fr: "racine" },
      { he: "טל", translit: "tal", fr: "rosée" },
      { he: "ברק", translit: "barak", fr: "éclair" },
      { he: "רעם", translit: "ra'am", fr: "tonnerre" }
    ]
  },
  {
    title: "Saisons",
    words: [
      { he: "אביב", translit: "aviv", fr: "printemps" },
      { he: "קיץ", translit: "kayits", fr: "été" },
      { he: "סתיו", translit: "stav", fr: "automne" },
      { he: "חורף", translit: "khoref", fr: "hiver" }
    ]
  }
];

/* Chaque phrase est décomposée en mots pour permettre le clic mot-à-mot.
   "category" range la phrase dans un onglet du même nom. Quand une phrase
   change selon le genre de la personne à qui l'on parle (ou qui parle),
   "variants" liste chaque forme avec son "label". Une phrase sans variation
   de genre n'a qu'un seul variant, sans label. */
const PHRASES = [
  {
    category: "Présentation",
    level: "Intermédiaire",
    fr: "Comment tu t'appelles ? (« לך » s'écrit à l'identique au masculin et au féminin sans les points-voyelles ; ici on l'a vocalisé — לְךָ / לָךְ — pour que l'audio prononce bien la bonne forme)",
    variants: [
      {
        label: "à un homme",
        words: [
          { he: "איך", translit: "eikh", fr: "comment" },
          { he: "קוראים", translit: "korim", fr: "(on) appelle" },
          { he: "לְךָ", translit: "lekha", fr: "à toi (masc.)" }
        ]
      },
      {
        label: "à une femme",
        words: [
          { he: "איך", translit: "eikh", fr: "comment" },
          { he: "קוראים", translit: "korim", fr: "(on) appelle" },
          { he: "לָךְ", translit: "lakh", fr: "à toi (fém.)" }
        ]
      }
    ]
  },
  {
    category: "Présentation",
    level: "Intermédiaire",
    fr: "Je m'appelle... (littéralement « on m'appelle », identique au masculin et au féminin)",
    variants: [
      {
        words: [
          { he: "קוראים", translit: "korim", fr: "(on) appelle" },
          { he: "לי", translit: "li", fr: "à moi" }
        ]
      }
    ]
  },
  {
    category: "Présentation",
    level: "Intermédiaire",
    fr: "Enchanté(e) (litt. « agréable de connaître » ; ici נעים est impersonnel — comme dans « il est agréable de... » — donc invariable, quel que soit le genre du locuteur ou de la personne rencontrée)",
    variants: [
      {
        words: [
          { he: "נעים", translit: "na'im", fr: "agréable" },
          { he: "להכיר", translit: "lehakir", fr: "de connaître" }
        ]
      }
    ]
  },
  {
    category: "Présentation",
    level: "Intermédiaire",
    fr: "D'où viens-tu ? (« את » non vocalisé se confond avec le marqueur d'objet direct « et » — bien plus fréquent — d'où le risque de mauvaise prononciation ; on l'a vocalisé ici : אַתְּ)",
    variants: [
      {
        label: "à un homme",
        words: [
          { he: "מאיפה", translit: "me'eifo", fr: "d'où" },
          { he: "אתה", translit: "ata", fr: "tu (masc.)" }
        ]
      },
      {
        label: "à une femme",
        words: [
          { he: "מאיפה", translit: "me'eifo", fr: "d'où" },
          { he: "אַתְּ", translit: "at", fr: "tu (fém.)" }
        ]
      }
    ]
  },
  {
    category: "Présentation",
    level: "Intermédiaire",
    fr: "Je viens de France (identique au masculin et au féminin ; le préfixe מ- de « מצרפת » est vocalisé pour bien marquer « de/depuis »)",
    variants: [
      {
        words: [
          { he: "אני", translit: "ani", fr: "je" },
          { he: "מִצרפת", translit: "mi-Tsarfat", fr: "de France" }
        ]
      }
    ]
  },

  {
    category: "Interrogation",
    level: "Avancé",
    fr: "Combien ça coûte ?",
    variants: [
      {
        words: [
          { he: "כמה", translit: "kama", fr: "combien" },
          { he: "זה", translit: "ze", fr: "ça / ce" },
          { he: "עולה", translit: "ole", fr: "coûte" }
        ]
      }
    ]
  },
  {
    category: "Interrogation",
    level: "Avancé",
    fr: "Parles-tu anglais ?",
    variants: [
      {
        label: "à un homme",
        words: [
          { he: "אתה", translit: "ata", fr: "tu (masc.)" },
          { he: "מדבר", translit: "medaber", fr: "parles (masc.)" },
          { he: "אנגלית", translit: "anglit", fr: "anglais" }
        ]
      },
      {
        label: "à une femme",
        words: [
          { he: "אַתְּ", translit: "at", fr: "tu (fém.)" },
          { he: "מדברת", translit: "medaberet", fr: "parles (fém.)" },
          { he: "אנגלית", translit: "anglit", fr: "anglais" }
        ]
      }
    ]
  },
  {
    category: "Interrogation",
    level: "Intermédiaire",
    fr: "Comment ça va ? (informel, litt. « quoi s'entend »)",
    variants: [
      {
        words: [
          { he: "מה", translit: "ma", fr: "quoi" },
          { he: "נשמע", translit: "nishma", fr: "s'entend / ça va" }
        ]
      }
    ]
  },
  {
    category: "Interrogation",
    level: "Intermédiaire",
    fr: "Quelle heure est-il ?",
    variants: [
      {
        words: [
          { he: "מה", translit: "ma", fr: "quelle" },
          { he: "השעה", translit: "ha-sha'a", fr: "l'heure" }
        ]
      }
    ]
  },

  {
    category: "Négation",
    level: "Avancé",
    fr: "Je ne comprends pas",
    variants: [
      {
        label: "un homme parle",
        words: [
          { he: "אני", translit: "ani", fr: "je" },
          { he: "לא", translit: "lo", fr: "ne... pas" },
          { he: "מבין", translit: "mevin", fr: "comprends (masc.)" }
        ]
      },
      {
        label: "une femme parle",
        words: [
          { he: "אני", translit: "ani", fr: "je" },
          { he: "לא", translit: "lo", fr: "ne... pas" },
          { he: "מבינה", translit: "mevina", fr: "comprends (fém.)" }
        ]
      }
    ]
  },
  {
    category: "Négation",
    level: "Avancé",
    fr: "Je ne sais pas",
    variants: [
      {
        label: "un homme parle",
        words: [
          { he: "אני", translit: "ani", fr: "je" },
          { he: "לא", translit: "lo", fr: "ne... pas" },
          { he: "יודע", translit: "yodea", fr: "sais (masc.)" }
        ]
      },
      {
        label: "une femme parle",
        words: [
          { he: "אני", translit: "ani", fr: "je" },
          { he: "לא", translit: "lo", fr: "ne... pas" },
          { he: "יודעת", translit: "yoda'at", fr: "sais (fém.)" }
        ]
      }
    ]
  },

  {
    category: "Affirmation",
    level: "Intermédiaire",
    fr: "D'accord",
    variants: [
      {
        words: [
          { he: "בסדר", translit: "beseder", fr: "d'accord / ça va" }
        ]
      }
    ]
  },
  {
    category: "Affirmation",
    level: "Intermédiaire",
    fr: "Bien sûr",
    variants: [
      {
        words: [
          { he: "בטח", translit: "betakh", fr: "bien sûr" }
        ]
      }
    ]
  },
  {
    category: "Affirmation",
    level: "Intermédiaire",
    fr: "Exactement",
    variants: [
      {
        words: [
          { he: "בדיוק", translit: "bediyuk", fr: "exactement" }
        ]
      }
    ]
  },

  {
    category: "Émotions & besoins",
    level: "Avancé",
    fr: "J'ai faim",
    variants: [
      {
        label: "un homme parle",
        words: [
          { he: "אני", translit: "ani", fr: "je" },
          { he: "רעב", translit: "ra'ev", fr: "affamé (masc.)" }
        ]
      },
      {
        label: "une femme parle",
        words: [
          { he: "אני", translit: "ani", fr: "je" },
          { he: "רעבה", translit: "re'eva", fr: "affamée (fém.)" }
        ]
      }
    ]
  },
  {
    category: "Émotions & besoins",
    level: "Avancé",
    fr: "J'ai soif",
    variants: [
      {
        label: "un homme parle",
        words: [
          { he: "אני", translit: "ani", fr: "je" },
          { he: "צמא", translit: "tsame", fr: "assoiffé (masc.)" }
        ]
      },
      {
        label: "une femme parle",
        words: [
          { he: "אני", translit: "ani", fr: "je" },
          { he: "צמאה", translit: "tsme'a", fr: "assoiffée (fém.)" }
        ]
      }
    ]
  },
  {
    category: "Émotions & besoins",
    level: "Avancé",
    fr: "Je suis fatigué(e)",
    variants: [
      {
        label: "un homme parle",
        words: [
          { he: "אני", translit: "ani", fr: "je" },
          { he: "עייף", translit: "ayef", fr: "fatigué (masc.)" }
        ]
      },
      {
        label: "une femme parle",
        words: [
          { he: "אני", translit: "ani", fr: "je" },
          { he: "עייפה", translit: "ayefa", fr: "fatiguée (fém.)" }
        ]
      }
    ]
  },
  {
    category: "Émotions & besoins",
    level: "Avancé",
    fr: "Je suis désolé(e)",
    variants: [
      {
        label: "un homme parle",
        words: [
          { he: "אני", translit: "ani", fr: "je" },
          { he: "מצטער", translit: "mitsta'er", fr: "désolé (masc.)" }
        ]
      },
      {
        label: "une femme parle",
        words: [
          { he: "אני", translit: "ani", fr: "je" },
          { he: "מצטערת", translit: "mitsta'eret", fr: "désolée (fém.)" }
        ]
      }
    ]
  },
  {
    category: "Émotions & besoins",
    level: "Avancé",
    fr: "Je t'aime (le pronom « toi » varie aussi selon qui l'on s'adresse : « otkha » à un homme, « otakh » à une femme — ici on s'adresse à un homme)",
    variants: [
      {
        label: "un homme parle",
        words: [
          { he: "אני", translit: "ani", fr: "je" },
          { he: "אוהב", translit: "ohev", fr: "aime (masc.)" },
          { he: "אותך", translit: "otkha", fr: "toi" }
        ]
      },
      {
        label: "une femme parle",
        words: [
          { he: "אני", translit: "ani", fr: "je" },
          { he: "אוהבת", translit: "ohevet", fr: "aime (fém.)" },
          { he: "אותך", translit: "otkha", fr: "toi" }
        ]
      }
    ]
  },

  {
    category: "Famille",
    level: "Intermédiaire",
    fr: "J'ai une grande famille (« יש לי » = « j'ai », litt. « il y a à moi » — invariable, ne change pas selon le genre du locuteur)",
    variants: [
      {
        words: [
          { he: "יש", translit: "yesh", fr: "il y a" },
          { he: "לי", translit: "li", fr: "à moi" },
          { he: "משפחה", translit: "mishpakha", fr: "famille" },
          { he: "גדולה", translit: "gdola", fr: "grande" }
        ]
      }
    ]
  },
  {
    category: "Famille",
    level: "Intermédiaire",
    fr: "Voici ma mère (« זו » s'accorde avec אמא, qui est féminin — on dirait « זה אבא שלי » pour « voici mon père »)",
    variants: [
      {
        words: [
          { he: "זו", translit: "zo", fr: "voici (fém.)" },
          { he: "אמא", translit: "ima", fr: "maman" },
          { he: "שלי", translit: "sheli", fr: "à moi / ma" }
        ]
      }
    ]
  },

  {
    category: "Direction",
    level: "Avancé",
    fr: "Où sont les toilettes ?",
    variants: [
      {
        words: [
          { he: "איפה", translit: "eifo", fr: "où" },
          { he: "השירותים", translit: "ha-sherutim", fr: "les toilettes" }
        ]
      }
    ]
  },
  {
    category: "Direction",
    level: "Intermédiaire",
    fr: "Tout droit",
    variants: [
      {
        words: [
          { he: "ישר", translit: "yashar", fr: "tout droit" }
        ]
      }
    ]
  },
  {
    category: "Direction",
    level: "Intermédiaire",
    fr: "À droite",
    variants: [
      {
        words: [
          { he: "ימינה", translit: "yamina", fr: "à droite" }
        ]
      }
    ]
  },
  {
    category: "Direction",
    level: "Intermédiaire",
    fr: "À gauche",
    variants: [
      {
        words: [
          { he: "שמאלה", translit: "smola", fr: "à gauche" }
        ]
      }
    ]
  },

  {
    category: "Politesse",
    level: "Avancé",
    fr: "Donne-moi, s'il te plaît (« תני » commence par un groupe de deux consonnes « t-n » peu courant en début de mot : le premier son peut être difficile à entendre, y compris à l'oreille de natifs)",
    variants: [
      {
        label: "à un homme",
        words: [
          { he: "תן", translit: "ten", fr: "donne (impératif, masc.)" },
          { he: "לי", translit: "li", fr: "à moi" },
          { he: "בבקשה", translit: "bevakasha", fr: "s'il te plaît" }
        ]
      },
      {
        label: "à une femme",
        words: [
          { he: "תְּנִי", translit: "tni", fr: "donne (impératif, fém.)" },
          { he: "לי", translit: "li", fr: "à moi" },
          { he: "בבקשה", translit: "bevakasha", fr: "s'il te plaît" }
        ]
      }
    ]
  },
  {
    category: "Politesse",
    level: "Avancé",
    fr: "Joyeuse fête ! (utilisé pour les fêtes juives, identique au masculin et au féminin)",
    variants: [
      {
        words: [
          { he: "חג", translit: "khag", fr: "fête" },
          { he: "שמח", translit: "sameakh", fr: "joyeux" }
        ]
      }
    ]
  },
  {
    category: "Politesse",
    level: "Intermédiaire",
    fr: "Bon appétit !",
    variants: [
      {
        words: [
          { he: "בתאבון", translit: "bete'avon", fr: "bon appétit" }
        ]
      }
    ]
  },
  {
    category: "Politesse",
    level: "Intermédiaire",
    fr: "Bonne chance !",
    variants: [
      {
        words: [
          { he: "בהצלחה", translit: "behatslakha", fr: "bonne chance" }
        ]
      }
    ]
  },
  {
    category: "Politesse",
    level: "Intermédiaire",
    fr: "Félicitations ! (litt. « bonne étoile »)",
    variants: [
      {
        words: [
          { he: "מזל", translit: "mazal", fr: "chance / étoile" },
          { he: "טוב", translit: "tov", fr: "bon" }
        ]
      }
    ]
  }
];
