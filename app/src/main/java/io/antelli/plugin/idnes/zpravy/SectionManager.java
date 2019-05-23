package io.antelli.plugin.idnes.zpravy;

import io.antelli.sdk.model.Question;

/**
 * Handcrafted by Štěpán Šonský on 11.11.2017.
 */

public class SectionManager {

    private static final String[] keywords = new String[]{
            "novinky",
            "noviny",
            "zprávy",
            "co",
            "je",
            "nového",
            "novýho"
    };

    private static final String ALL = "all";

    private static final String ZPRAVODAJSTVI = "zpravodaj";
    private static final String KRAJE = "kraje";
    private static final String SPORT = "sport";
    private static final String KULTURA = "kultura";
    private static final String EKONOMIKA = "ekonomikah";
    private static final String FINCENTRUM = "fincentrum";
    private static final String REALITY = "reality";
    private static final String CESTOVANI = "iglobe";
    private static final String AUTOKAT = "autokat";
    private static final String HOBBY = "hobby";
    private static final String MOBIL = "mobil";
    private static final String TECHNET = "technet";
    private static final String ONA = "ona";
    private static final String XMAN = "xman";
    private static final String CAS = "cas";
    private static final String BONUSWEB = "bonusweb";

    private static final String ZPRAVODAJSTVI_DOMACI = "domaci";
    private static final String ZPRAVODAJSTVI_ZAHRANICNI = "zahranicni";
    private static final String ZPRAVODAJSTVI_KRIMI = "krimi";
    private static final String ZPRAVODAJSTVI_KAVARNA = "kavarna";


    private static final String KRAJE_BRNO = "brnoh";
    private static final String KRAJE_BUDEJOVICE = "budejovice";
    private static final String KRAJE_HRADEC = "hradec";
    private static final String KRAJE_JIHLAVA = "jihlava";
    private static final String KRAJE_KARLOVY_VARY = "vary";
    private static final String KRAJE_LIBERC = "liberec";
    private static final String KRAJE_OLOMOUC = "olomouc";
    private static final String KRAJE_OSTRAVA = "ostrava";
    private static final String KRAJE_PARDUBICE = "pardubice";
    private static final String KRAJE_PLZEN = "plzen";
    private static final String KRAJE_PRAHA = "prahah";
    private static final String KRAJE_USTI = "usti";
    private static final String KRAJE_ZLIN = "zlin";


    private static final String SPORT_FOTBAL = "fotbalh";
    private static final String SPORT_HOKEJ = "hokejh";
    private static final String SPORT_LYZOVANI = "lyzovani";
    private static final String SPORT_BIATLON = "biatlon";
    private static final String SPORT_BASKET = "basket";
    private static final String SPORT_VOLEJBAL = "volejbalh";
    private static final String SPORT_HAZENA = "hazena";
    private static final String SPORT_TENIS = "tenis";
    private static final String SPORT_ATLETIKA = "atletika";
    private static final String SPORT_FOFMULE = "formule";
    private static final String SPORT_MOTORSPORT = "motorsport";
    private static final String SPORT_CYKLISTIKA = "cyklistika";
    private static final String SPORT_FLORBAL = "florbal";
    private static final String SPORT_GOLF = "sport-golf";
    private static final String SPORT_SAZENI = "sazenih";

    //private HashMap<String, String> sections = new HashMap<>();

    public String getSectionFrom(Question question){
        String q = question.removeWords(keywords);
        if (q==null)
            return null;
        else if (q.contains("domov") || q.contains("u nás"))
            return ZPRAVODAJSTVI_DOMACI;
        else if (q.contains("svět"))
           return ZPRAVODAJSTVI_ZAHRANICNI;
        else if (q.contains("prah"))
            return KRAJE_PRAHA;
        else if (q.contains("buděj"))
            return KRAJE_BUDEJOVICE;
        else if (q.contains("hrad"))
            return KRAJE_HRADEC;
        else if (q.contains("jihlav"))
            return KRAJE_JIHLAVA;
        else if (q.contains("varů"))
            return KRAJE_KARLOVY_VARY;
        else if (q.contains("liber"))
            return KRAJE_LIBERC;
        else if (q.contains("olomouc"))
            return KRAJE_OLOMOUC;
        else if (q.contains("ostrav"))
            return KRAJE_OSTRAVA;
        else if (q.contains("pardubic"))
            return KRAJE_PARDUBICE;
        else if (q.contains("brno") || q.contains("brna"))
            return KRAJE_BRNO;
        else if (q.contains("plz"))
            return KRAJE_PLZEN;
        else if (q.contains("ústí"))
            return KRAJE_USTI;
        else if (q.contains("zlín"))
            return KRAJE_ZLIN;

        else if (q.contains("ekonomi"))
            return EKONOMIKA;
        else if (q.contains("sport"))
            return SPORT;
        else if (q.contains("kultur"))
            return KULTURA;
        else if (q.contains("mobil"))
            return MOBIL;
        else if (q.contains("techni"))
            return TECHNET;
        else if (q.contains("muž"))
            return XMAN;
        else if (q.contains("cesto"))
            return CESTOVANI;
        else if (q.contains("hry") || q.contains("hrách") || q.contains("herní"))
            return BONUSWEB;
        else if (q.contains("aut"))
            return AUTOKAT;
        else if (q.contains("bydlení"))
            return REALITY;
        else if (q.contains("žen"))
            return ONA;
        else if (q.contains("fotbal"))
            return SPORT_FOTBAL;
        else if (q.contains("hokej"))
            return SPORT_HOKEJ;
        else if (q.contains("golf"))
            return SPORT_GOLF;
        else if (q.contains("cyklisti"))
            return SPORT_CYKLISTIKA;
        else if (q.contains("tenis"))
            return SPORT_TENIS;
        else if (q.contains("moto"))
            return SPORT_MOTORSPORT;
        else if (q.contains("atleti"))
            return SPORT_ATLETIKA;
        /*else if (request.contains("olympiád"))
            link = "https://oh.idnes.cz/";*/
        else
            return null;
    }

}
