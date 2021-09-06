package com.mobdeve.s18.legitmessages.util

import com.mobdeve.s18.legitmessages.model.Chat
import com.mobdeve.s18.legitmessages.model.User
import java.util.ArrayList

class DataHelper {

    private val dataList = ArrayList<Chat>()

    fun initList(): ArrayList<Chat> {
        dataList.add(Chat(
            "1",
            "jolonarvaez"
        ))

        dataList.add(Chat(
            "2",
            "manoloenriquez"
        ))

        return dataList
    }



}


