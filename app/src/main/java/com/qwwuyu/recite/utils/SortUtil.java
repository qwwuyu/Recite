package com.qwwuyu.recite.utils;


import com.qwwuyu.recite.bean.Word;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 排序工具类
 */
public class SortUtil {
    public static void sortListByIndex(List<? extends Word> list) {
        Comparator<Word> comparator = new Comparator<Word>() {
            @Override
            public int compare(Word o1, Word o2) {
                if (o1.getIndex() > o2.getIndex())
                    return 1;
                else
                    return -1;
            }
        };
        Collections.sort(list, comparator);
    }

    public static void sortListByTime(List<? extends Word> list) {
        Comparator<Word> comparator = new Comparator<Word>() {
            @Override
            public int compare(Word o1, Word o2) {
                if (o1.getCollectTime() > o2.getCollectTime())
                    return 1;
                else
                    return -1;
            }
        };
        Collections.sort(list, comparator);
    }

    public static void sortListById(List<? extends Word> list) {
        Comparator<Word> comparator = new Comparator<Word>() {
            @Override
            public int compare(Word o1, Word o2) {
                if (o1.getId() > o2.getId())
                    return 1;
                else
                    return -1;
            }
        };
        Collections.sort(list, comparator);
    }
}
