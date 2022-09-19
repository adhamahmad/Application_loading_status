package com.udacity

import android.animation.Animator
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*
import kotlin.concurrent.thread


class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        val fileName = intent.extras?.getString("fileName")
        val fileStatus = intent.extras?.getString("fileStatus")

        findViewById<TextView>(R.id.file_name).text = fileName
        findViewById<TextView>(R.id.status).text = fileStatus


        findViewById<Button>(R.id.button).setOnClickListener{ // return back to home screen
            if(motion_layout.progress == 0f){ // animation hasn't started
                motion_layout.transitionToEnd()

                val x = object : MotionLayout.TransitionListener {
                    override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {

                    }

                    override fun onTransitionChange(
                        p0: MotionLayout?,
                        p1: Int,
                        p2: Int,
                        p3: Float
                    ) {

                    }

                    override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
                        goBack()
                    }

                    override fun onTransitionTrigger(
                        p0: MotionLayout?,
                        p1: Int,
                        p2: Boolean,
                        p3: Float
                    ) {

                    }
                }
                motion_layout.setTransitionListener(x)
            }

        }

    }

    private fun goBack() {
        val myIntent = Intent(this, MainActivity::class.java)
        this.startActivity(myIntent)
    }

}
