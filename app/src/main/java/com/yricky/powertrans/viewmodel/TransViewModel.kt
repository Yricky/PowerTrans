package com.yricky.powertrans.viewmodel

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.yricky.powertrans.PTApp
import com.yricky.powertrans.model.DataBaseModel
import com.yricky.powertrans.pojo.youdao.Entries
import com.yricky.powertrans.pojo.youdao.Reply
import okhttp3.*
import java.io.IOException

class TransViewModel:ViewModel() {

    val entryList: MutableLiveData<List<Entries>> = MutableLiveData<List<Entries>>()
    val queryWorld:MutableLiveData<String> = MutableLiveData<String>()
    private val handler:Handler = Handler(Looper.getMainLooper())
    private val client:OkHttpClient = OkHttpClient()
    private fun request(url: String, callback: Callback) {
        val request: Request = Request.Builder()
            .url(url)
            .build()
        client.newCall(request).enqueue(callback)
    }

    fun query(word:String) {
        queryWorld.value = word
        handler.removeMessages(0)
        PTApp.tpe.execute {
            val list = DataBaseModel.query(word)
            handler.post { entryList.value = list }
        }
        handler.postDelayed({
            val url = "https://dict.youdao.com/suggest?q=$word&num=20&doctype=json"
            request(url, object : Callback {
                override fun onFailure(call: Call, e: IOException) {
//                handler.post {
//                    rawJson.value = e.message
//                }
                }
                override fun onResponse(call: Call, response: Response) {
                    val bodyString = response.body?.string()
                    Gson().fromJson(bodyString, Reply::class.java).data.entries.let{
                        DataBaseModel.typeIn(it){
                            val list = DataBaseModel.query(word)
                            if(queryWorld.value == word){
                                handler.post{
                                    entryList.value = list
                                }
                            }

                        }
                    }
                }
            })
        },300)

    }
}