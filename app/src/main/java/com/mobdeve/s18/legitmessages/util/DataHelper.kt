package com.mobdeve.s18.legitmessages.util

import com.mobdeve.s18.legitmessages.model.User
import java.util.ArrayList

class DataHelper {

    private val dataList = ArrayList<User>()

    fun initList(): ArrayList<User> {
        dataList.add(User(
            "1",
            "jolonarvaez@gmail.com",
            "Jolo Narvaez",
        "Jolo",
        "Narvaez",
        "jolonarvaez"))

        dataList.add(User(
            "2",
            "manoloenriquez@gmail.com",
            "Manolo Enriquez",
            "Manolo",
            "Enriquez",
        "manoloenriquez"))

        return dataList
    }



}


