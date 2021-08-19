package com.mobdeve.s18.legitmessages.util

import com.mobdeve.s18.legitmessages.model.User
import java.util.ArrayList

class DataHelper {

    private val dataList = ArrayList<User>()
    val sampleList = ArrayList<String>()

    fun initList(): ArrayList<User> {
        dataList.add(User("jolonarvaez",
                        "Jolo",
                        "Narvaez",))

        dataList.add(User("manoloenriquez",
                        "Manolo",
                        "Enriquez",))

        return dataList
    }

    fun initSample(): ArrayList<String>{
        sampleList.add("Jolo")
        sampleList.add("Manolo")

        return sampleList
    }


}


