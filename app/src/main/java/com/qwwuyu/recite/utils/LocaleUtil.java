package com.qwwuyu.recite.utils;

import java.util.Locale;

/**
 * 地区工具类
 * Created by qiwei on 2016/6/13.
 */
public class LocaleUtil {
    public enum Language {
        /** 英语 */
        ENGLISH("en"),
        /** 中文_大陆 */
        CHINA("zhCN"),
        /** 中文_台湾 */
        TAIWAN("zhTW"),
        /** 法语 */
        FRANCE("fr"),
        /** 意大利语 */
        ITALIAN("it"),
        /** 日语 */
        JAPAN("ja"),
        /** 韩语 */
        KOREA("ko"),
        /** 德语 */
        GERMAN("de");
        private String languageCode;

        Language(String languageCode) {
            this.languageCode = languageCode;
        }

        public String getLanguageCode() {
            return languageCode;
        }
    }

    public static Language getDefaultLanguage() {
        Locale locale = Locale.getDefault();
        if (isIdentical(locale, new Locale[]{Locale.CHINA, Locale.CHINESE, Locale.PRC, Locale.SIMPLIFIED_CHINESE})) {
            return Language.CHINA;
        } else if (isIdentical(locale, new Locale[]{Locale.TAIWAN, Locale.TRADITIONAL_CHINESE})) {
            return Language.TAIWAN;
        } else if (isIdentical(locale, new Locale[]{Locale.FRANCE, Locale.CANADA_FRENCH, Locale.FRENCH})) {
            return Language.FRANCE;
        } else if (isIdentical(locale, new Locale[]{Locale.ITALIAN, Locale.ITALY})) {
            return Language.ITALIAN;
        } else if (isIdentical(locale, new Locale[]{Locale.JAPAN, Locale.JAPANESE})) {
            return Language.JAPAN;
        } else if (isIdentical(locale, new Locale[]{Locale.KOREA, Locale.KOREAN})) {
            return Language.KOREA;
        } else if (isIdentical(locale, new Locale[]{Locale.GERMAN, Locale.GERMANY})) {
            return Language.GERMAN;
        } else {
            return Language.ENGLISH;
        }
    }

    private static boolean isIdentical(Locale locale1, Locale locales[]) {
        for (Locale local : locales) {
            if (locale1.equals(local)) {
                return true;
            }
        }
        return false;
    }
}
