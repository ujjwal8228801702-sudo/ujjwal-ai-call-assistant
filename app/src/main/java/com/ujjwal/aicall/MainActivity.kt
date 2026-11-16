package com.ujjwal.aicall

import android.app.Activity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.*
import android.graphics.Color
import android.view.Gravity
import android.view.ViewGroup
import java.util.Locale

class MainActivity : Activity(), TextToSpeech.OnInitListener {
    private var tts: TextToSpeech? = null
    private var hindi = true
    private var assistantOn = false
    private lateinit var status: TextView
    private lateinit var toggle: Button
    private lateinit var language: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = Color.rgb(10, 12, 18)
        window.navigationBarColor = Color.rgb(10, 12, 18)
        tts = TextToSpeech(this, this)
        buildScreen()
    }

    private fun buildScreen() {
        val bg = Color.rgb(10, 12, 18)
        val gold = Color.rgb(225, 184, 92)
        val white = Color.WHITE
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(24, 32, 24, 24)
            setBackgroundColor(bg)
            gravity = Gravity.CENTER_HORIZONTAL
        }

        val brand = TextView(this).apply {
            text = "✦  UJJWAL AI"
            textSize = 27f
            setTextColor(gold)
            gravity = Gravity.CENTER
        }
        root.addView(brand, matchWidth(58))

        val subtitle = TextView(this).apply {
            text = "SMART CALL ASSISTANT"
            textSize = 12f
            letterSpacing = 0.22f
            setTextColor(Color.LTGRAY)
            gravity = Gravity.CENTER
        }
        root.addView(subtitle, matchWidth(38))

        val orb = TextView(this).apply {
            text = "◉"
            textSize = 76f
            setTextColor(gold)
            gravity = Gravity.CENTER
        }
        val orbParams = LinearLayout.LayoutParams(220, 190)
        orbParams.topMargin = 26
        root.addView(orb, orbParams)

        status = TextView(this).apply {
            text = "Assistant is OFF"
            textSize = 19f
            setTextColor(white)
            gravity = Gravity.CENTER
        }
        root.addView(status, matchWidth(42))

        toggle = Button(this).apply {
            text = "TURN ON ASSISTANT"
            setOnClickListener {
                assistantOn = !assistantOn
                status.text = if (assistantOn) "Assistant is ON • Demo mode" else "Assistant is OFF"
                toggle.text = if (assistantOn) "TURN OFF ASSISTANT" else "TURN ON ASSISTANT"
            }
        }
        root.addView(toggle, matchWidth(58))

        language = Button(this).apply {
            text = "Language: हिंदी / English"
            setOnClickListener {
                hindi = !hindi
                text = if (hindi) "Language: हिंदी" else "Language: English"
                speakGreeting()
            }
        }
        val langParams = matchWidth(54)
        langParams.topMargin = 12
        root.addView(language, langParams)

        val info = TextView(this).apply {
            text = "INCOMING CALL • CALL HISTORY • CALLER DETAILS\n\nयह शुरुआती UI डेमो है। यह अभी SIM कॉल अपने आप नहीं उठाता।"
            textSize = 14f
            setTextColor(Color.LTGRAY)
            gravity = Gravity.CENTER
            setPadding(12, 28, 12, 12)
        }
        root.addView(info, matchWidth(150))

        val footer = TextView(this).apply {
            text = "Built for Hindi + English conversations"
            textSize = 12f
            setTextColor(gold)
            gravity = Gravity.CENTER
        }
        root.addView(footer, matchWidth(40))
        setContentView(root)
    }

    private fun speakGreeting() {
        val message = if (hindi) {
            "नमस्कार! आप उज्ज्वल ए आई से बात कर रहे हैं। कृपया बताइए, मैं आपकी क्या सहायता कर सकता हूँ?"
        } else {
            "Hello! You are speaking with Ujjwal AI. How may I help you today?"
        }
        tts?.speak(message, TextToSpeech.QUEUE_FLUSH, null, "ujjwal_greeting")
    }

    override fun onInit(statusCode: Int) {
        if (statusCode == TextToSpeech.SUCCESS) {
            tts?.language = Locale("hi", "IN")
        }
    }

    override fun onDestroy() {
        tts?.stop()
        tts?.shutdown()
        super.onDestroy()
    }

    private fun matchWidth(height: Int): LinearLayout.LayoutParams =
        LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height)
}
