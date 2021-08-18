package com.mobdeve.s18.legitmessages.util

import com.mobdeve.s18.legitmessages.model.User
import java.util.ArrayList

class DataHelper {

    val dataList = ArrayList<User>()

    fun initList(): ArrayList<User> {
        dataList.add(User("jolonarvaez",
                        "Jolo",
                        "Narvaez",))

        dataList.add(User("manoloenriquez",
                        "Manolo",
                        "Enriquez",))

        return dataList
    }

}


