package it.polito.madcourse.group06.utilities

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.icu.util.Calendar
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.view.View
import com.google.android.material.snackbar.Snackbar
import it.polito.madcourse.group06.models.advertisement.Advertisement
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.floor
import kotlin.math.round
import kotlin.math.roundToInt


/**
 * This method creates an image file and throws an exception if the file could not be created
 *
 * @return Created file
 * @throws IOException
 */
@Throws(IOException::class)
fun createImageFile(profilePicturePath: String): File {
    // Create an image file name
    return File(profilePicturePath)
}

//SUPPORT METHODS FOR ROTATING IMAGES///////////////////////////////////////////////////////////

/**
 * This method is responsible for solving the rotation issue if exist. Also scale the images to
 * 1024x1024 resolution
 *
 * @param context       The current context
 * @param selectedImage The Image URI
 * @return Bitmap image results
 * @throws IOException
 */
@Throws(IOException::class)
fun handleSamplingAndRotationBitmap(context: Context, selectedImage: Uri?): Bitmap? {
    val MAX_HEIGHT = 1024
    val MAX_WIDTH = 1024

    // First decode with inJustDecodeBounds=true to check dimensions
    val options = BitmapFactory.Options()
    options.inJustDecodeBounds = true
    var imageStream: InputStream? = context.contentResolver.openInputStream(selectedImage!!)
    BitmapFactory.decodeStream(imageStream, null, options)
    imageStream?.close()

    // Calculate inSampleSize
    options.inSampleSize = calculateInSampleSize(options, MAX_WIDTH, MAX_HEIGHT)

    // Decode bitmap with inSampleSize set
    options.inJustDecodeBounds = false
    imageStream = context.contentResolver.openInputStream(selectedImage)
    var img = BitmapFactory.decodeStream(imageStream, null, options)
    img = img?.let { rotateImageIfRequired(context, it, selectedImage) }
    return img
}

/**
 * Calculate an inSampleSize for use in a [BitmapFactory.Options] object when decoding
 * bitmaps using the decode* methods from [BitmapFactory]. This implementation calculates
 * the closest inSampleSize that will result in the final decoded bitmap having a width and
 * height equal to or larger than the requested width and height. This implementation does not
 * ensure a power of 2 is returned for inSampleSize which can be faster when decoding but
 * results in a larger bitmap which isn't as useful for caching purposes.
 *
 * @param options   An options object with out* params already populated (run through a decode*
 * method with inJustDecodeBounds==true
 * @param reqWidth  The requested width of the resulting bitmap
 * @param reqHeight The requested height of the resulting bitmap
 * @return The value to be used for inSampleSize
 */
private fun calculateInSampleSize(
    options: BitmapFactory.Options,
    reqWidth: Int, reqHeight: Int
): Int {
    // Raw height and width of image
    val height = options.outHeight
    val width = options.outWidth
    var inSampleSize = 1
    if (height > reqHeight || width > reqWidth) {

        // Calculate ratios of height and width to requested height and width
        val heightRatio = Math.round(height.toFloat() / reqHeight.toFloat())
        val widthRatio = Math.round(width.toFloat() / reqWidth.toFloat())

        // Choose the smallest ratio as inSampleSize value, this will guarantee a final image
        // with both dimensions larger than or equal to the requested height and width.
        inSampleSize = if (heightRatio < widthRatio) heightRatio else widthRatio

        // This offers some additional logic in case the image has a strange
        // aspect ratio. For example, a panorama may have a much larger
        // width than height. In these cases the total pixels might still
        // end up being too large to fit comfortably in memory, so we should
        // be more aggressive with sample down the image (=larger inSampleSize).
        val totalPixels = (width * height).toFloat()

        // Anything more than 2x the requested pixels we'll sample down further
        val totalReqPixelsCap = (reqWidth * reqHeight * 2).toFloat()
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++
        }
    }
    return inSampleSize
}

/**
 * Rotate an image if required.
 *
 * @param img           The image bitmap
 * @param selectedImage Image URI
 * @return The resulted Bitmap after manipulation
 */
@Throws(IOException::class)
private fun rotateImageIfRequired(context: Context, img: Bitmap, selectedImage: Uri): Bitmap? {
    val input = context.contentResolver.openInputStream(selectedImage)
    val ei: ExifInterface = if (Build.VERSION.SDK_INT > 23)
        ExifInterface(input!!)
    else
        ExifInterface(selectedImage.path!!)
    val orientation =
        ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
    return when (orientation) {
        ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(img, 90)
        ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(img, 180)
        ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(img, 270)
        else -> img
    }
}

private fun rotateImage(img: Bitmap, degree: Int): Bitmap? {
    val matrix = Matrix()
    matrix.postRotate(degree.toFloat())
    val rotatedImg = Bitmap.createBitmap(img, 0, 0, img.width, img.height, matrix, true)
    img.recycle()
    return rotatedImg
}

// TIMESLOT TOOLS
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

fun timeStringToDoubleHour(time: String, pattern:String="HH:mm"): Double {
    return when(pattern){
        "HH:mm"->time.split(":").foldRight(0.0) { a, b -> (a.toDouble() + b.toDouble()) / 60.0 } * 60
        "HH h mm min"->{val timeList=Regex("[0-9]+").findAll(time).map{ it.value.toDouble() }.toList();
                        timeList[0]+timeList[1]/60}
        else->-1.0
    }
}

fun timeDoubleHourToString(time: Double): String {
    val h = floor(time).toInt()
    val min = round((time - floor(time)) * 60).toInt()
    return when(true) {
        (h == 0) -> "$min min"
        (min == 0) -> "$h h"
        else -> "$h h $min min"
    }
}

fun dateListToString(date: String): String {
    val currentDate = SimpleDateFormat("dd/MM/yyyy",Locale.getDefault()).format(Date())
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

fun hoursToCredit(hours: Double): Int {
    return round(hours * 4).toInt()
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

const val TAB_SERVICES = "Services"
const val TAB_ACTIVE = "Active"
const val TAB_SAVED = "Saved"
const val TAB_MINE = "Mine"
const val ALL_SERVICES = "All"

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


//USEFUL EXTENSION FUNCTIONS
fun Boolean.toInt() = if (this) 1 else 0

fun Advertisement.isAvailable(): Boolean {
    return this.rxUserId.isNullOrEmpty() && this.ratingUserId.isNullOrEmpty() && !this.isExpired()
}

fun Advertisement.isEnded(): Boolean {
    return this.rxUserId.isNullOrEmpty() && this.ratingUserId.isNullOrEmpty() && !this.activeAt.isNullOrEmpty()
}

fun Advertisement.isExpired(): Boolean {
    timeStringToDoubleHour(SimpleDateFormat("HH:mm",Locale.getDefault()).format(Date())).also { now ->
        SimpleDateFormat("dd/MM/yyyy",Locale.getDefault()).format(Date()).also { today ->
            return (now >= timeStringToDoubleHour(advEndingTime)
                    && computeDateDifference(today, this.advDate).first == 0.0
                    || (computeDateDifference(today, this.advDate).first < 0))
        }
    }
}

fun Advertisement.isToBeRated(): Boolean {
    timeStringToDoubleHour(SimpleDateFormat("HH:mm",Locale.getDefault()).format(Date())).also { now ->
        SimpleDateFormat("dd/MM/yyyy",Locale.getDefault()).format(Date()).also { today ->
            return if (!activeAt.isNullOrEmpty()) {
                now >= timeStringToDoubleHour(activeAt!!) + activeFor && computeDateDifference(
                    today,
                    this.advDate
                ).first == 0.0
                        || (computeDateDifference(today, this.advDate).first < 0)
            } else {
                false
            }
        }
    }
}

fun checkTimeslotForm(
    view: View,
    title: String?,
    description: String?,
    location: String?,
    startingTime: String?,
    endingTime: String?,
    duration: Double?,
    date: String?
): Boolean {

    //title check
    if (title.isNullOrEmpty()) {
        Snackbar.make(view, "Error: you must provide a title to your advertisement.", Snackbar.LENGTH_SHORT).show()
        return false
    }

    //description check
    if (description.isNullOrEmpty()) {
        Snackbar.make(view, "Error: you must provide a description to your advertisement.", Snackbar.LENGTH_SHORT).show()
        return false
    }

    //location check
    if (location.isNullOrEmpty()) {
        Snackbar.make(view, "Error: you must provide a location for your service.", Snackbar.LENGTH_SHORT).show()
        return false
    }

    //starting time check
    if (startingTime.isNullOrEmpty()) {
        Snackbar.make(view, "Error: you must provide a starting availability time.", Snackbar.LENGTH_SHORT).show()
        return false
    }

    //ending time check
    if (endingTime.isNullOrEmpty()) {
        Snackbar.make(view, "Error: you must provide an ending availability time.", Snackbar.LENGTH_SHORT).show()
        return false
    }

    //date check
    if (date.isNullOrEmpty()) {
        Snackbar.make(view, "Error: you must provide a date for your timeslot!", Snackbar.LENGTH_SHORT).show()
        return false
    } else {
        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date()).also { today ->
            if (computeDateDifference(today, date).first < 0.0) {
                Snackbar.make(view, "Error: you can not create timeslots back in time!", Snackbar.LENGTH_SHORT).show()
                return false
            } else if (computeDateDifference(today, date).first == 0.0) {
                SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date()).also { now ->
                    if (computeTimeDifference(now, startingTime).first < 0.0) {
                        Snackbar.make(view, "Error: you can not create timeslots back in time!", Snackbar.LENGTH_SHORT).show()
                        return false
                    }
                }
            }
            if (computeTimeDifference(startingTime, endingTime).first < 0.0) {
                Snackbar.make(view, "Error: you can not create timeslots back in time!", Snackbar.LENGTH_SHORT).show()
                return false
            }
        }
    }

    //duration check
    if (duration == null) {
        Snackbar.make(view, "Error: you must provide a valid duration for your timeslot.", Snackbar.LENGTH_SHORT).show()
        return false
    } else if (duration <= 0) {
        Snackbar.make(view, "Error: you must provide a valid duration for your timeslot.", Snackbar.LENGTH_SHORT).show()
        return false
    }else if(timeStringToDoubleHour(endingTime)- timeStringToDoubleHour(startingTime)-duration<0){
        Snackbar.make(view, "Error: please provide a duration compatible with the availability time range.", Snackbar.LENGTH_LONG).show()
        return false
    }

    return true
}