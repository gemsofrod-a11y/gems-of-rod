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
      { he: "נעים מאוד", translit: "na'im me'od", fr: "enchanté" }
    ]
  },
  {
    title: "Nombres (0-10)",
    words: [
      { he: "אפס", translit: "efes", fr: "zéro" },
      { he: "אחד", translit: "ekhad", fr: "un" },
      { he: "שתיים", translit: "shtayim", fr: "deux" },
      { he: "שלוש", translit: "shalosh", fr: "trois" },
      { he: "ארבע", translit: "arba", fr: "quatre" },
      { he: "חמש", translit: "khamesh", fr: "cinq" },
      { he: "שש", translit: "shesh", fr: "six" },
      { he: "שבע", translit: "sheva", fr: "sept" },
      { he: "שמונה", translit: "shmone", fr: "huit" },
      { he: "תשע", translit: "tesha", fr: "neuf" },
      { he: "עשר", translit: "eser", fr: "dix" }
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
      { he: "סגול", translit: "sagol", fr: "violet" }
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
      { he: "בת", translit: "bat", fr: "fille" }
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
      { he: "אתמול", translit: "etmol", fr: "hier" }
    ]
  }
];

/* Chaque phrase est décomposée en mots pour permettre le clic mot-à-mot.
   Quand une phrase change selon le genre de la personne à qui l'on parle
   (ou qui parle), "variants" liste chaque forme avec son "label". Une
   phrase sans variation de genre n'a qu'un seul variant, sans label. */
const PHRASES = [
  {
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
    level: "Intermédiaire",
    fr: "Enchanté(e) (litt. « agréable de connaître »)",
    variants: [
      {
        label: "un homme parle",
        words: [
          { he: "נעים", translit: "na'im", fr: "agréable (masc.)" },
          { he: "להכיר", translit: "lehakir", fr: "de connaître" }
        ]
      },
      {
        label: "une femme parle",
        words: [
          { he: "נעימה", translit: "ne'ima", fr: "agréable (fém.)" },
          { he: "להכיר", translit: "lehakir", fr: "de connaître" }
        ]
      }
    ]
  },
  {
    level: "Intermédiaire",
    fr: "D'où viens-tu ?",
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
          { he: "את", translit: "at", fr: "tu (fém.)" }
        ]
      }
    ]
  },
  {
    level: "Intermédiaire",
    fr: "Je viens de France (identique au masculin et au féminin)",
    variants: [
      {
        words: [
          { he: "אני", translit: "ani", fr: "je" },
          { he: "מצרפת", translit: "mi-Tsarfat", fr: "de France" }
        ]
      }
    ]
  },
  {
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
          { he: "את", translit: "at", fr: "tu (fém.)" },
          { he: "מדברת", translit: "medaberet", fr: "parles (fém.)" },
          { he: "אנגלית", translit: "anglit", fr: "anglais" }
        ]
      }
    ]
  },
  {
    level: "Avancé",
    fr: "Donne-moi, s'il te plaît",
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
          { he: "תני", translit: "tni", fr: "donne (impératif, fém.)" },
          { he: "לי", translit: "li", fr: "à moi" },
          { he: "בבקשה", translit: "bevakasha", fr: "s'il te plaît" }
        ]
      }
    ]
  },
  {
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
  },
  {
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
  }
];
