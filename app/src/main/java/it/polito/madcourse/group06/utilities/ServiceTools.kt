package it.polito.madcourse.group06.utilities

import it.polito.madcourse.group06.models.advertisement.Advertisement

class ServiceTools {
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

        timeDifference =
            endingTime.split(":").fold(0.0){a,b-> (a.toDouble()+b.toDouble())*60.0} -
            startingTime.split(":").fold(0.0){a,b-> (a.toDouble()+b.toDouble())*60.0}

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

        endingDate.split("/").forEachIndexed { index, s ->
            when(index){
                0-> dateDifference +=s.toInt() //day
                1-> dateDifference +=31 - 3*(s.toInt()==2).toInt() - (listOf(4,6,9,11).contains(s.toInt())).toInt() //month
                2-> dateDifference += if(s.toInt()%400==0) 366 else 365  //year
            }
        }
        startingDate.split("/").forEachIndexed { index, s ->
            when(index){
                0-> dateDifference -=s.toInt() //day
                1-> dateDifference -=31 - 3*(s.toInt()==2).toInt() - (listOf(4,6,9,11).contains(s.toInt())).toInt() //month
                2-> dateDifference -= if(s.toInt()%400==0) 366 else 365  //year
            }
        }

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

    class AdvFilter(val location:String?=null,
                    val starting_time:String?=null,
                    val ending_time:String?=null,
                    val duration:Double?=null,
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
            (advFilter.location!=null && adv.advLocation==advFilter.location)||(advFilter.location==null) &&
            (advFilter.duration!=null && adv.advDuration<=advFilter.duration)||(advFilter.duration==null) &&
            (advFilter.starting_time!=null && adv.advStartingTime.isLaterThanTime(advFilter.starting_time))||(advFilter.starting_time==null) &&
            (advFilter.ending_time!=null && adv.advEndingTime.isSoonerThanTime(advFilter.ending_time))||(advFilter.ending_time==null) &&
            (advFilter.starting_date!=null && adv.advDate.isLaterThanDate(advFilter.starting_date))||(advFilter.starting_date==null) &&
            (advFilter.ending_date!=null && adv.advDate.isSoonerThanDate(advFilter.ending_date))||(advFilter.ending_date==null)
        }
    }

    //Useful extension functions
    private fun Boolean.toInt() = if (this) 1 else 0

}
