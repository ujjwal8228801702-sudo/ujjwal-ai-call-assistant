package com.ujjwal.aicall

import android.app.Activity
import android.app.AlertDialog
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
            setTextColor(Color.WHITE)
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
            text = "Language: हिंदी"
            setOnClickListener {
                hindi = !hindi
                text = if (hindi) "Language: हिंदी" else "Language: English"
                speakGreeting()
            }
        }
        val langParams = matchWidth(54)
        langParams.topMargin = 12
        root.addView(language, langParams)

        val voiceButton = Button(this).apply {
            text = "TEST VOICE"
            setOnClickListener { speakGreeting() }
        }
        val voiceParams = matchWidth(54)
        voiceParams.topMargin = 10
        root.addView(voiceButton, voiceParams)

        val callButton = Button(this).apply {
            text = "TRY DEMO INCOMING CALL"
            setOnClickListener { showDemoCall() }
        }
        val callParams = matchWidth(54)
        callParams.topMargin = 8
        root.addView(callButton, callParams)

        val info = TextView(this).apply {
            text = "INCOMING CALL • CALL HISTORY • CALLER DETAILS\n\nयह डेमो केवल स्क्रीन और आवाज़ जाँचता है। यह वास्तविक SIM कॉल नहीं उठाता।"
            textSize = 13f
            setTextColor(Color.LTGRAY)
            gravity = Gravity.CENTER
            setPadding(12, 18, 12, 12)
        }
        root.addView(info, matchWidth(112))

        val footer = TextView(this).apply {
            text = "Hindi + English • Demo build"
            textSize = 12f
            setTextColor(gold)
            gravity = Gravity.CENTER
        }
        root.addView(footer, matchWidth(36))
        val scroll = ScrollView(this).apply {
            setFillViewport(true)
            setBackgroundColor(bg)
            isVerticalScrollBarEnabled = true
            addView(root, ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ))
        }
        setContentView(scroll)
    }

    private fun showDemoCall() {
        val builder = AlertDialog.Builder(this)
            .setTitle(if (hindi) "आने वाली डेमो कॉल" else "Incoming demo call")
            .setMessage(if (hindi) "डेमो कॉलर\nयह वास्तविक फोन कॉल नहीं है।" else "Demo Caller\nThis is not a real phone call.")
            .setNegativeButton(if (hindi) "REJECT" else "REJECT") { dialog, _ ->
                dialog.dismiss()
                status.text = if (hindi) "डेमो कॉल अस्वीकार की गई" else "Demo call rejected"
            }
            .setPositiveButton(if (hindi) "ANSWER" else "ANSWER") { dialog, _ ->
                dialog.dismiss()
                status.text = if (hindi) "डेमो कॉल कनेक्ट हुई" else "Demo call connected"
                speakGreeting()
            }
        builder.show()
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
            val result = tts?.setLanguage(if (hindi) Locale("hi", "IN") else Locale.US)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                status.text = "Selected voice is not available"
            }
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