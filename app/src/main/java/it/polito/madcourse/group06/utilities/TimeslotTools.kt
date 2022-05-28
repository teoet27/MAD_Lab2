package it.polito.madcourse.group06.utilities

import it.polito.madcourse.group06.models.advertisement.Advertisement
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt


/**
 * sortSKills
 * @param skillList
 * @param isTopDownOrder
 * @return
 */
fun sortSkills(skillList: List<String>, isTopDownOrder: Boolean): List<String> {
    if (isTopDownOrder)
        return skillList.sorted()
    return skillList.sortedDescending()
}


/**
 * computeTimeDifference is a method which return the time difference from two "time-strings" and whether
 * they are acceptable or not.
 *
 * @param startingTime the starting time
 * @param endingTime the ending time
 * @return a Pair<Float, Boolean> where it's specified the time difference and its acceptability
 *
 * String.isLaterThanTime returns true if the time difference is positive w.r.t. the parameter
 * String.isSoonerThanTime returns true if the time difference is negative w.r.t. the parameter
 */
fun computeTimeDifference(
    startingTime: String,
    endingTime: String
): Pair<Double, Boolean> {
    var timeDifference: Double = 0.0
    if (startingTime.isNullOrEmpty() || endingTime.isNullOrEmpty()) {
        return Pair(-1.0, false)
    }

    timeDifference = timeStringToDoubleSec(endingTime) - timeStringToDoubleSec(startingTime)

    return Pair(
        (timeDifference * 100.0).roundToInt() / 100.0,
        (timeDifference * 100.0).roundToInt() / 100.0 >= 0
    )
}

fun String.isLaterThanTime(time: String): Boolean {
    return computeTimeDifference(time, this).first >= 0
}

fun String.isSoonerThanTime(time: String): Boolean {
    return computeTimeDifference(time, this).first <= 0
}


/**
 * computeDateDifference is a method which return the date difference from two "date-strings" and whether
 * they are acceptable or not.
 * NOTE: the date format is the following dd/mm/yyyy
 *
 * @param startingDate the starting date
 * @param endingDate the ending date
 * @return a Pair<Float, Boolean> where it's specified the time difference and its acceptability
 *
 * String.isLaterThanDate returns true if the date difference is positive w.r.t. the parameter
 * String.isSoonerThanDate returns true if the date difference is negative w.r.t. the parameter
 */
fun computeDateDifference(
    startingDate: String,
    endingDate: String
): Pair<Double, Boolean> {
    var dateDifference: Double = 0.0
    if (startingDate.isNullOrEmpty() || endingDate.isNullOrEmpty()) {
        return Pair(-1.0, false)
    }
    dateDifference = (dateStringToInt(endingDate) - dateStringToInt(startingDate)).toDouble()
    return Pair(
        (dateDifference * 100.0).roundToInt() / 100.0,
        (dateDifference * 100.0).roundToInt() / 100.0 >= 0
    )
}

fun String.isLaterThanDate(date: String): Boolean {
    return computeDateDifference(date, this).first >= 0
}

fun String.isSoonerThanDate(date: String): Boolean {
    return computeDateDifference(date, this).first <= 0
}

fun dateStringToInt(date: String): Int {
    var dateInt = 0
    date.split("/").forEachIndexed { index, s ->
        when (index) {
            0 -> dateInt += s.toInt() //day
            1 -> dateInt += (31 - 3 * (s.toInt() == 2).toInt() - (listOf(
                4,
                6,
                9,
                11
            ).contains(s.toInt())).toInt()) * s.toInt() //month
            2 -> dateInt += (if (s.toInt() % 400 == 0) 366 else 365) * s.toInt() //year
        }
    }
    return dateInt
}

fun timeStringToDoubleSec(time: String): Double {
    return time.split(":").fold(0.0) { a, b -> (a.toDouble() + b.toDouble()) * 60.0 }
}

fun timeStringToDoubleHour(time: String): Double {
    return time.split(":").foldRight(0.0) { a, b -> (a.toDouble() + b.toDouble()) / 60.0 } * 60
}


class AdvFilter(
    val location: String? = null,
    val whole_word: Boolean = false,
    val starting_time: String? = null,
    val ending_time: String? = null,
    val min_duration: String? = null,
    val max_duration: String? = null,
    val starting_date: String? = null,
    val ending_date: String? = null
) {

    fun isEmpty(): Boolean {
        return location.isNullOrEmpty() && !whole_word && starting_time.isNullOrEmpty() &&
                ending_time.isNullOrEmpty() && min_duration.isNullOrEmpty() &&
                max_duration.isNullOrEmpty() && starting_date.isNullOrEmpty() && ending_date.isNullOrEmpty()
    }
}

class SearchState(
    var selectedSkill: String? = null,
    var searchedWord: String? = null,
    var sortParameter: Int? = null,
    var sortUpFlag: Boolean? = null,
    var myAdsFlag: Boolean? = null,
    var activeAdsFlag: Boolean? = null,
    var savedAdsFlag: Boolean? = null,
    var filter: AdvFilter? = null
)

//Useful extension functions
fun Boolean.toInt() = if (this) 1 else 0
fun Advertisement.isExpired(): Boolean {
    return this.advDate.isSoonerThanDate(SimpleDateFormat("dd/MM/yyyy").format(Date()))
}