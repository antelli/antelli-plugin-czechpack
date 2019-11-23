package io.antelli.plugin.base.util

import java.util.Calendar

import io.antelli.sdk.model.Question

/**
 * Handcrafted by Štěpán Šonský on 21.11.2017.
 */

object DateTimeCortex {

    fun getDateTimeFromString(input: Question): Calendar {
        val result = Calendar.getInstance()
        var dayOfWeek = -1

        val month = when{
            input.containsOneWord("leden", "ledna") -> 0
            input.containsOneWord("únor", "února") -> 1
            input.containsOneWord("březen", "března") -> 2
            input.containsOneWord("duben", "dubna") -> 3
            input.containsOneWord("květen", "května") -> 4
            input.containsOneWord("červen", "června") -> 5
            input.containsOneWord("červenec", "července") -> 6
            input.containsOneWord("srpen", "srpna") -> 7
            input.containsOneWord("září") -> 8
            input.containsOneWord("říjen", "října") -> 9
            input.containsOneWord("listopad", "listopadu") -> 10
            input.containsOneWord("prosinec", "prosince") -> 11
            else -> null
        }
        if (input.numbers.size == 1 && month != null){
            result.set(Calendar.DAY_OF_MONTH, input.numbers[0])
            result.set(Calendar.MONTH, month)
        } else {
            when {
                input.contains("zítra") -> result.add(Calendar.DATE, 1)
                input.contains("pozítří") -> result.add(Calendar.DATE, 2)
                input.contains("včera") -> result.add(Calendar.DATE, -1)
                input.contains("neděl") -> dayOfWeek = Calendar.SUNDAY
                input.contains("pondělí") -> dayOfWeek = Calendar.MONDAY
                input.contains("úterý") -> dayOfWeek = Calendar.TUESDAY
                input.contains("střed") -> dayOfWeek = Calendar.WEDNESDAY
                input.contains("čtvrtek") -> dayOfWeek = Calendar.THURSDAY
                input.contains("pátek") -> dayOfWeek = Calendar.FRIDAY
                input.contains("sobot") -> dayOfWeek = Calendar.SATURDAY
            }

            if (dayOfWeek != -1) {
                while (result.get(Calendar.DAY_OF_WEEK) != dayOfWeek) {
                    result.add(Calendar.DATE, 1)
                }
            }

            result.set(Calendar.MINUTE, 0)
            when {
                input.contains("večer") -> result.set(Calendar.HOUR_OF_DAY, 20)
                input.contains("ráno") -> result.set(Calendar.HOUR_OF_DAY, 8)
                input.contains("odpoledne") -> result.set(Calendar.HOUR_OF_DAY, 12)
            }

            val time = getTime(input)
            if (time[0] != -1 && time[1] != -1) {
                result.set(Calendar.HOUR_OF_DAY, time[0])
                result.set(Calendar.MINUTE, time[1])
            }
        }
        return result
    }

    fun getTime(question: Question): IntArray {
        val time = intArrayOf(-1, -1)
        for (word in question.words) {
            try {
                if (word.contains(":")) {
                    val timeStrings = word.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    time[0] = Integer.parseInt(timeStrings[0])
                    time[1] = Integer.parseInt(timeStrings[1])
                    return time
                }
                val number = Integer.parseInt(word)
                if (time[0] == 0) {
                    time[0] = number
                } else {
                    time[1] = number
                    return time
                }
            } catch (e: NumberFormatException) {
                //continue
            }

        }
        return time
    }

    fun daysBetween(startDate: Calendar, endDate: Calendar): Int {
        val date = startDate.clone() as Calendar
        var daysBetween = 0
        while (date.before(endDate)) {
            date.add(Calendar.DAY_OF_MONTH, 1)
            daysBetween++
        }
        return daysBetween
    }

}
