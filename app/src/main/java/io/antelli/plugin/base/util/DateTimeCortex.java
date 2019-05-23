package io.antelli.plugin.base.util;

import java.util.Calendar;

import io.antelli.sdk.model.Question;

/**
 * Handcrafted by Štěpán Šonský on 21.11.2017.
 */

public class DateTimeCortex {

    public static Calendar getDateTimeFromString(Question input) {
        Calendar result = Calendar.getInstance();
        int dayOfWeek = -1;

        if (input.contains("zítra")) {
            result.add(Calendar.DATE, 1);
        } else if (input.contains("pozítří")) {
            result.add(Calendar.DATE, 2);
        } else if (input.contains("včera")) {
            result.add(Calendar.DATE, -1);
        } else if (input.contains("neděl")) {
            dayOfWeek = Calendar.SUNDAY;
        } else if (input.contains("pondělí")) {
            dayOfWeek = Calendar.MONDAY;
        } else if (input.contains("úterý")) {
            dayOfWeek = Calendar.TUESDAY;
        } else if (input.contains("střed")) {
            dayOfWeek = Calendar.WEDNESDAY;
        } else if (input.contains("čtvrtek")) {
            dayOfWeek = Calendar.THURSDAY;
        } else if (input.contains("pátek")) {
            dayOfWeek = Calendar.FRIDAY;
        } else if (input.contains("sobot")) {
            dayOfWeek = Calendar.SATURDAY;
        }

        if (dayOfWeek != -1) {
            while (result.get(Calendar.DAY_OF_WEEK) != dayOfWeek) {
                result.add(Calendar.DATE, 1);
            }
        }

        result.set(Calendar.MINUTE, 0);
        if (input.contains("večer")){
            result.set(Calendar.HOUR_OF_DAY, 20);
        } else if (input.contains("ráno")){
            result.set(Calendar.HOUR_OF_DAY, 8);
        } else if (input.contains("odpoledne")){
            result.set(Calendar.HOUR_OF_DAY, 12);
        }

        int[] time = getTime(input);
        if (time[0] != -1 && time[1] != -1){
            result.set(Calendar.HOUR_OF_DAY, time[0]);
            result.set(Calendar.MINUTE, time[1]);
        }

        return result;
    }

    public static int[] getTime(Question question) {
        int[] time = new int[]{-1, -1};
        for (String word : question.getWords()) {
            try {
                if (word.contains(":")) {
                    String[] timeStrings = word.split(":");
                    time[0] = Integer.parseInt(timeStrings[0]);
                    time[1] = Integer.parseInt(timeStrings[1]);
                    return time;
                }
                int number = Integer.parseInt(word);
                if (time[0] == 0) {
                    time[0] = number;
                } else {
                    time[1] = number;
                    return time;
                }
            } catch (NumberFormatException e) {
                //continue
            }
        }
        return time;
    }

}
