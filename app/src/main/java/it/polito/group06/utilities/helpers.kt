package it.polito.group06.utilities

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

/**
 * This utility method changes an array list into a string with a specific format
 *
 * @param list  List of skills
 * @return string of skills
 */
fun fromArrayListToString(list: ArrayList<String>): String {
    var out = ""
    for (i in list.indices) {
        out += list[i]
        if (i != list.size - 1)
            out += ", "
    }
    return out
}

/**
 * This method returns the Bitmap image from a file whose path is provided
 *
 * @param path  The path were to find the file
 * @return Bitmap image results
 */
fun getBitmapFromFile(path: String): Bitmap? {
    return BitmapFactory.decodeFile(path)
}

/**
 * This method creates an image file and throws an exception if the file could not be created
 *
 * @return Created file
 * @throws IOException
 */
@Throws(IOException::class)
fun createImageFile(profilePicturePath:String): File {
    // Create an image file name
    return File(profilePicturePath)
}

/**
 * This method stores the profile picture into a file
 *
 * @param bitmap  Bitmap image of the profile picture
 * @param dir  Directory where to store the profile picture
 */
fun saveProfilePicture(bitmap: Bitmap, dir: String) {
    val imageFile = File(dir, "profile_picture.jpg")
    try {
        // Compress the bitmap and save in jpg format
        val stream: OutputStream = FileOutputStream(imageFile)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream) ?: throw IOException()
        stream.flush()
        stream.close()
    } catch (e: IOException) {
        e.printStackTrace()
    }
}

fun parseSkillString(x: String): String {
    var out = ""
    var first = true
    for (kw in x.split(",")) {
        if (kw.trim().isNotEmpty() && kw.isNotEmpty()) {
            if (first) {
                out += kw.trim()
                first = false
            } else {
                out += ", "
                out += kw.trim()
            }
        }
    }
    return out
}