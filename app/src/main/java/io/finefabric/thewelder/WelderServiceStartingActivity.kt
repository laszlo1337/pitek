package io.finefabric.thewelder

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity


class WelderServiceStartingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val serviceIntent = Intent(applicationContext, AreYouAWelderYetService::class.java)

        startService(serviceIntent)

        finish()
    }
}
