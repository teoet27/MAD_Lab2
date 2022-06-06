package it.polito.madcourse.group06.utilities

import android.icu.util.Calendar
import it.polito.madcourse.group06.models.advertisement.Advertisement
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.floor
import kotlin.math.round
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

fun timeDoubleHourToString(time: Double): String {
    val h = floor(time).toInt()
    val min = round((time - floor(time)) * 60).toInt()
    return if (h == 0)
        "${min} min"
    else if (min == 0)
        "${h} h"
    else
        "${h} h ${min} min"
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

val TAB_SERVICES = "Services"
val TAB_ACTIVE = "Active"
val TAB_SAVED = "Saved"
val TAB_MINE = "Mine"
val ALL_SERVICES = "All"
val NOT_ACTIVE_NOT_SAVED = 0
val ACTIVE = 2
val SAVED = 1
val ACTIVE_AND_SAVED = 3

class SearchState(
    var currentTab: String? = null,
    var selectedSkill: String? = null,
    var searchedWord: String? = null,
    var sortParameter: Int? = null,
    var sortUpFlag: Boolean? = null,
    var myAdsFlag: Boolean? = null,
    var activeAdsFlag: Boolean? = null,
    var savedAdsFlag: Boolean? = null,
    var filter: AdvFilter? = null
)

fun hoursToCredit(hours: Double): Int {
    return round(hours * 4).toInt()
}


fun dateListToString(date: String): String {
    val currentDate = SimpleDateFormat("dd/MM/yyyy").format(Date())
    computeDateDifference(currentDate, date).first.also { distance ->
        return if (distance == 0.0)
            "Today"
        else if (distance == 1.0)
            "Tomorrow"
        else if (distance < 7) {
            "On ${intToDayOfWeek((Calendar.DAY_OF_WEEK - 1 + distance.toInt()) % 7 + 1)}"
        } else {
            val todayList = currentDate.split("/")
            val dateList = date.split("/")
            if ((todayList[1].toInt() < dateList[1].toInt()) && (todayList[2].toInt() == dateList[2].toInt()))
                "On ${dateList[0]} ${intToMonth(dateList[1].toInt())}"
            else
                "On ${dateList[0]} ${intToMonth(dateList[1].toInt())} ${dateList[2]}"
        }
    }
}

fun intToDayOfWeek(n: Int): String {
    return when (n) {
        1 -> "Monday"
        2 -> "Tuesday"
        3 -> "Wednesday"
        4 -> "Thursday"
        5 -> "Friday"
        6 -> "Saturday"
        else -> "Sunday"
    }
}

fun intToMonth(n: Int): String {
    return when (n) {
        1 -> "January"
        2 -> "February"
        3 -> "March"
        4 -> "April"
        5 -> "May"
        6 -> "June"
        7 -> "July"
        8 -> "August"
        9 -> "September"
        10 -> "October"
        11 -> "November"
        else -> "December"
    }
}



//Useful extension functions
fun Boolean.toInt() = if (this) 1 else 0

fun Advertisement.isAvailable(): Boolean {
    return this.rxUserId.isNullOrEmpty() && this.ratingUserId.isNullOrEmpty() && !this.isExpired()
}

fun Advertisement.isEnded(): Boolean {
    return this.rxUserId.isNullOrEmpty() && this.ratingUserId.isNullOrEmpty() && !this.activeAt.isNullOrEmpty()
}

fun Advertisement.isExpired(): Boolean {
    return ((timeStringToDoubleHour(SimpleDateFormat("HH:mm").format(Date())) >= timeStringToDoubleHour(advEndingTime)
            && this.advDate == SimpleDateFormat("dd/MM/yyyy").format(Date()))
            || (computeDateDifference(SimpleDateFormat("dd/MM/yyyy").format(Date()), this.advDate).first < 0))
}

fun Advertisement.isToBeRated(): Boolean {
    return if (!activeAt.isNullOrEmpty()) {
        (timeStringToDoubleHour(SimpleDateFormat("HH:mm").format(Date())) >= timeStringToDoubleHour(
            activeAt!!
        ) + activeFor
                && this.advDate == SimpleDateFormat("dd/MM/yyyy").format(Date())
                || (computeDateDifference(
            SimpleDateFormat("dd/MM/yyyy").format(Date()),
            this.advDate
        ).first < 0))
    } else {
        false
    }
}
