package it.polito.madcourse.group06.utilities

import it.polito.madcourse.group06.models.advertisement.Advertisement

class TimeslotTools {
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
    private fun computeTimeDifference(startingTime: String, endingTime: String): Pair<Double, Boolean> {
        var timeDifference: Double = 0.0
        if (startingTime.isNullOrEmpty() || endingTime.isNullOrEmpty()) {
            return Pair(-1.0, false)
        }
        /*val startingHour = startingTime.split(":")[0].toInt()
        val startingMinute = startingTime.split(":")[1].toInt()
        val endingHour = endingTime.split(":")[0].toInt()
        val endingMinute = endingTime.split(":")[1].toInt()

        timeDifference += (endingHour - startingHour) + ((endingMinute - startingMinute) / 60.0)*/

        timeDifference = timeStringToDouble(endingTime) - timeStringToDouble(startingTime)

        return Pair(
            String.format("%.2f", timeDifference).toDouble(),
            String.format("%.2f", timeDifference).toDouble() >= 0
        )
    }

    private fun String.isLaterThanTime(time:String):Boolean{
        return computeTimeDifference(this,time).first>=0
    }

    private fun String.isSoonerThanTime(time:String):Boolean{
        return computeTimeDifference(this,time).first<=0
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
    private fun computeDateDifference(startingDate: String, endingDate: String): Pair<Double, Boolean> {
        var dateDifference: Double = 0.0
        if (startingDate.isNullOrEmpty() || endingDate.isNullOrEmpty()) {
            return Pair(-1.0, false)
        }
        dateDifference= (dateStringToInt(endingDate)-dateStringToInt(startingDate)).toDouble()
        return Pair(
            String.format("%.2f", dateDifference).toDouble(),
            String.format("%.2f", dateDifference).toDouble() >= 0
        )
    }

    private fun String.isLaterThanDate(date:String):Boolean{
        return computeDateDifference(this,date).first>=0
    }

    private fun String.isSoonerThanDate(date:String):Boolean{
        return computeDateDifference(this,date).first<=0
    }

    private fun dateStringToInt(date:String):Int{
        var dateInt=0
        date.split("/").forEachIndexed { index, s ->
            when(index){
                0-> dateInt +=s.toInt() //day
                1-> dateInt +=31 - 3*(s.toInt()==2).toInt() - (listOf(4,6,9,11).contains(s.toInt())).toInt() //month
                2-> dateInt += if(s.toInt()%400==0) 366 else 365  //year
            }
        }
        return dateInt
    }

    private fun timeStringToDouble(time:String):Double{
        return time.split(":").fold(0.0){a,b-> (a.toDouble()+b.toDouble())*60.0}
    }

    class AdvFilter(
        val location:String?=null,
        val whole_word:Boolean=false,
        val starting_time:String?=null,
        val ending_time:String?=null,
        val min_duration: String? =null,
        val max_duration: String? =null,
        val starting_date:String?=null,
        val ending_date:String?=null)
    /**
     * filterAdvertisement
     * @param advList list of all available advertisements
     * @param location to be matched to Adv related attribute
     * @param starting_time to be matched to Adv related attribute
     * @param ending_time to be matched to Adv related attribute
     * @param duration to be matched to Adv related attribute
     * @param starting_date to be matched to Adv related attribute
     * @param ending_date to be matched to Adv related attribute
     * @return the list of Advertisements matching the constraints
     */
    fun filterAdvertisementList(
        advList:List<Advertisement>?,
        advFilter: AdvFilter?
    ):List<Advertisement>?{
        return if(advFilter==null) advList else advList?.filter{ adv->
            ((advFilter.location!=null && advFilter.whole_word && advFilter.location.contains(adv.advLocation,true))||
            (advFilter.location!=null && !advFilter.whole_word && advFilter.location==adv.advLocation)||(advFilter.location==null)) &&
            ((advFilter.min_duration!=null && adv.advDuration.toString().isLaterThanTime(advFilter.min_duration))||(advFilter.starting_time==null)) && //wrong
            ((advFilter.max_duration!=null && adv.advDuration.toString().isSoonerThanTime(advFilter.max_duration))||(advFilter.starting_time==null)) &&//wrong
            ((advFilter.starting_time!=null && adv.advStartingTime.isLaterThanTime(advFilter.starting_time))||(advFilter.starting_time==null)) &&
            ((advFilter.ending_time!=null && adv.advEndingTime.isSoonerThanTime(advFilter.ending_time))||(advFilter.ending_time==null))&&
            ((advFilter.starting_date!=null && adv.advDate.isLaterThanDate(advFilter.starting_date))||(advFilter.starting_date==null))&&
            ((advFilter.ending_date!=null && adv.advDate.isSoonerThanDate(advFilter.ending_date))||(advFilter.ending_date==null))
        }
    }

    fun sortAdvertisementList(
        advList:List<Advertisement>?,
        criterion: String?,
        up_flag: Boolean = true
    ): List<Advertisement>? {
        val sortedList = when(up_flag){
            true->when(criterion){
                "Title"-> advList?.sortedBy { it.advTitle.lowercase() }
                "Duration"->advList?.sortedByDescending { it.advDuration }
                "Starting time"->advList?.sortedByDescending { timeStringToDouble(it.advStartingTime) }
                "Ending time"->advList?.sortedByDescending { timeStringToDouble(it.advEndingTime) }
                "Date"->advList?.sortedByDescending { dateStringToInt(it.advDate) }
                else -> null
            }
            else->when(criterion){
                "Title"-> advList?.sortedByDescending { it.advTitle.lowercase() }
                "Duration"->advList?.sortedBy { it.advDuration }
                "Starting time"->advList?.sortedBy { timeStringToDouble(it.advStartingTime) }
                "Ending time"->advList?.sortedBy { timeStringToDouble(it.advEndingTime) }
                "Date"->advList?.sortedBy { dateStringToInt(it.advDate) }
                else -> null
            }
        }
        return sortedList
    }
}

//Useful extension functions
private fun Boolean.toInt() = if (this) 1 else 0
