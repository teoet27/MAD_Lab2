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
     * filterAdvertisement
     * @param advList
     * @param skillList
     * @return
     */
    /*
    fun filterAdvertisement(advList: List<Advertisement>, skillList: List<String>) : List<Advertisement> {
        val out = mutableListOf<Advertisement>()
        for(adv in advList) {
            for(skill in skillList) {
                if(adv.skills.contains(skill))
                {
                    out.add(adv)
                }
            }
        }
        return out
    }
    */
}