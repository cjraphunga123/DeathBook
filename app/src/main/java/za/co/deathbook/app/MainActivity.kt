package za.co.deathbook.app

import android.app.Activity
import android.os.Bundle
import android.graphics.Color
import android.graphics.Typeface
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast

class MainActivity : Activity() {
    private val ink = Color.rgb(28, 42, 58)
    private val teal = Color.rgb(0, 112, 112)
    private val muted = Color.rgb(100, 116, 130)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showHome()
    }

    private fun basePage(): LinearLayout {
        val page = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(24, 28, 24, 24)
            setBackgroundColor(Color.rgb(247, 249, 251))
        }
        val scroll = ScrollView(this)
        scroll.addView(page)
        setContentView(scroll)
        return page
    }

    private fun text(value: String, size: Float, color: Int = ink, bold: Boolean = false): TextView =
        TextView(this).apply {
            text = value
            textSize = size
            setTextColor(color)
            if (bold) setTypeface(typeface, Typeface.BOLD)
            setPadding(0, 8, 0, 8)
        }

    private fun action(label: String, onTap: () -> Unit): Button =
        Button(this).apply {
            text = label
            isAllCaps = false
            setTextColor(Color.WHITE)
            setBackgroundColor(teal)
            setOnClickListener { onTap() }
        }

    private fun showHome() {
        val page = basePage()
        page.addView(text("DEATHBOOK", 14f, teal, true))
        page.addView(text("Death records, handled with care.", 28f, ink, true))
        page.addView(text(
            "A secure digital service concept for submitting and managing death-record requests.",
            16f, muted
        ))
        page.addView(text("Your services", 20f, ink, true))
        page.addView(text("Submit a record\nStart a death-record submission request.", 16f))
        page.addView(action("Start a demo submission") { showDemoNotice() })
        page.addView(text("Verification status\nCheck the progress of a submitted request.", 16f))
        page.addView(action("View verification information") { showVerification() })
        page.addView(text("Institution access\nFor authorised organisations and officials.", 16f))
        page.addView(action("Institution information") { showInstitution() })
        page.addView(text(
            "IMPORTANT: This first build is a demonstration only. It is not connected to the Department of Home Affairs and does not verify, register or certify deaths. Do not enter real personal or identity information.",
            13f, Color.rgb(150, 72, 30)
        ))
        page.addView(text("DeathBook • Version 1.0.0", 12f, muted))
    }

    private fun showDemoNotice() {
        val page = basePage()
        page.addView(text("Demo submission", 26f, ink, true))
        page.addView(text(
            "The submission workflow will be added after secure accounts, privacy controls and an approved records process are in place.",
            16f, muted
        ))
        page.addView(text(
            "This prototype does not accept or store death records. Please do not enter real personal information.",
            14f, Color.rgb(150, 72, 30)
        ))
        page.addView(action("Back to home") { showHome() })
    }

    private fun showVerification() {
        val page = basePage()
        page.addView(text("Verification information", 26f, ink, true))
        page.addView(text(
            "Official verification is not yet connected. A future integration will require formal authorisation and an approved Department of Home Affairs interface.",
            16f, muted
        ))
        page.addView(text("Current status: Not connected", 17f, Color.rgb(150, 72, 30), true))
        page.addView(action("Back to home") { showHome() })
    }

    private fun showInstitution() {
        val page = basePage()
        page.addView(text("Institution access", 26f, ink, true))
        page.addView(text(
            "Institution accounts, role-based permissions and audit logging are planned. Access will only be enabled after identity checks and institutional authorisation.",
            16f, muted
        ))
        page.addView(action("Back to home") { showHome() })
    }
}
