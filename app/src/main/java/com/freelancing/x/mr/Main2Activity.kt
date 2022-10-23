package com.mr.english

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.freelancing.x.R
import com.google.firebase.messaging.FirebaseMessaging
import fcm.androidtoandroid.FirebasePush
import fcm.androidtoandroid.model.Notification
import kotlinx.android.synthetic.main.activity_main2.*
import kotlinx.android.synthetic.main.activity_mainx.*
import kotlinx.android.synthetic.main.activity_main2x.*
import org.json.JSONException
import org.json.JSONObject

class Main2Activity : AppCompatActivity() {
    private val serverKey0 = "AIzaSyAlEbgi-gTAwEtMLCyNMk_Gm8xg0wVzIHc"
    private val FCM_API = "https://fcm.googleapis.com/fcm/send"
    private val serverKey =
            "key=" + "AAAA3kn5qyU:APA91bGPuXG945NNrqTKKaGRNuf_1WnPJZGAMTS8xdLYYmn3UtH8LO1KZAELxBZ5DMruQQG5IRqWzMtpAmOaEK6g06WT_9CxMKKgOm9_Ifea493E8WT8z4L1UGQ3fs3sseNnVjOUt8Qu"
    private val contentType = "application/json"
    private val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(this.applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
       // FirebaseMessaging.getInstance().subscribeToTopic("/topics/Enter_topic")
    //    FirebaseMessaging.getInstance().subscribeToTopic("test")


findViewById<Button>(R.id.submit).setOnClickListener {
            if (!TextUtils.isEmpty(msg.text)) {
                val topic = "/topics/Enter_topic" //topic has to match what the receiver subscribed to

                val notification= JSONObject()
                val notifcationBody = JSONObject()

                val notification0 = Notification("FCM-AndroidToOtherDevice", msg.text.toString())

                val yourExtraData = JSONObject().put("key", "name")

                val firebasePush = FirebasePush.build(serverKey0)
                        .setNotification(notification0)
                        .setData(yourExtraData)
                        .setOnFinishPush { Log.i("123321","sended") }

// Send to topic
                firebasePush.sendToTopic("test")


//                try {
//                    notifcationBody.put("title", "Firebase Notification")
//                    notifcationBody.put("message", msg.text)
//                    notification.put("to", topic)
//                    notification.put("data", notifcationBody)
//                    Log.e("TAG", ""+notification+"");
//                } catch (e: JSONException) {
//                    Log.e("TAG", "onCreate: " + e.message)
//                }
//
//                sendNotification(notification)
            }
        }
    }


    private fun sendNotification(notification: JSONObject) {
        Log.e("TAG", "sendNotification")
        val jsonObjectRequest = object : JsonObjectRequest(FCM_API, notification,
                Response.Listener<JSONObject> { response ->
                    Log.i("TAG", "onResponse: $response")
                    msg.setText("")
                },
                Response.ErrorListener {
                    Toast.makeText(this@Main2Activity, "Request error", Toast.LENGTH_LONG).show()
                    Log.i("TAG", "onErrorResponse: Didn't work")
                }) {

            override fun getHeaders(): Map<String, String> {
                val params = HashMap<String, String>()
                params["Authorization"] = serverKey
                params["Content-Type"] = contentType
                return params
            }
        }
        requestQueue.add(jsonObjectRequest)
    }
}