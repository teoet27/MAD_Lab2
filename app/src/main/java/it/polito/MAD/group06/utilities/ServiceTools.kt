package it.polito.MAD.group06.utilities

import it.polito.MAD.group06.models.advertisement.Advertisement

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
        advList:MutableList<Advertisement>?,
        location:String?=null,
        starting_time:String?=null,
        ending_time:String?=null,
        duration:Double?=null,
        starting_date:String?=null,
        ending_date:String?=null
    ):List<Advertisement>?{
        return advList?.filter{ adv->
            (location!=null && adv.advLocation==location)||(location==null) &&
            (duration!=null && adv.advDuration<=duration)||(duration==null) &&
            (starting_time!=null && adv.advStartingTime.isLaterThanTime(starting_time))||(starting_time==null) &&
            (ending_time!=null && adv.advEndingTime.isSoonerThanTime(ending_time))||(ending_time==null) &&
            (starting_date!=null && adv.advDate.isLaterThanDate(starting_date))||(starting_date==null) &&
            (ending_date!=null && adv.advDate.isSoonerThanDate(ending_date))||(ending_date==null)
        }
    }

    //Useful extension functions

    private fun Boolean.toInt() = if (this) 1 else 0

    fun Advertisement.containsSkill(skill:String):Boolean{
        return this.listOfSkills.contains(skill)
    }
}