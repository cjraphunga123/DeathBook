package za.co.deathbook.app

import android.app.Activity
import android.os.Bundle
import android.graphics.Color
import android.graphics.Typeface
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast

class MainActivity : Activity() {
    private val ink = Color.rgb(28, 42, 58)
    private val teal = Color.rgb(0, 112, 112)
    private val muted = Color.rgb(100, 116, 130)
    private val warning = Color.rgb(150, 72, 30)

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

    private fun field(hintText: String, password: Boolean = false): EditText =
        EditText(this).apply {
            hint = hintText
            textSize = 16f
            singleLine = true
            inputType = if (password) {
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            } else if (hintText.contains("email", ignoreCase = true)) {
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            } else {
                InputType.TYPE_CLASS_TEXT
            }
            setPadding(14, 10, 14, 10)
            setBackgroundColor(Color.WHITE)
        }

    private fun showHome() {
        val page = basePage()
        page.addView(text("DEATHBOOK", 14f, teal, true))
        page.addView(text("Death records, handled with care.", 28f, ink, true))
        page.addView(text(
            "A secure digital service concept for submitting and managing death-record requests.",
            16f, muted
        ))
        page.addView(text("Your account", 20f, ink, true))
        page.addView(action("Create an account") { showRegister() })
        page.addView(action("Log in") { showLogin() })
        page.addView(text("Your services", 20f, ink, true))
        page.addView(text("Prepare a request\nLearn what to check before contacting the relevant authorities.", 16f))
        page.addView(action("Open preparation checklist") { showPreparation() })
        page.addView(text("Submit a record\nStart a death-record submission request.", 16f))
        page.addView(action("Start a demo submission") { showDemoNotice() })
        page.addView(text("Verification status\nCheck the progress of a submitted request.", 16f))
        page.addView(action("View verification information") { showVerification() })
        page.addView(text("Institution access\nFor authorised organisations and officials.", 16f))
        page.addView(action("Institution information") { showInstitution() })
        page.addView(text(
            "IMPORTANT: This first build is a demonstration only. It is not connected to the Department of Home Affairs and does not verify, register or certify deaths. Do not enter real personal or identity information.",
            13f, warning
        ))
        page.addView(text("DeathBook • Version 1.0.0", 12f, muted))
    }

    private fun showRegister() {
        val page = basePage()
        page.addView(text("Create your account", 26f, ink, true))
        page.addView(text("Public user registration", 17f, teal, true))
        page.addView(text("Enter test details only. Account creation is not active in this prototype.", 14f, warning))
        page.addView(field("Full name"))
        page.addView(field("Email address"))
        page.addView(field("Create password", password = true))
        page.addView(field("Confirm password", password = true))
        page.addView(action("Create account (demo only)") {
            Toast.makeText(this, "Demo only: no account or password was saved.", Toast.LENGTH_LONG).show()
        })
        page.addView(action("Already registered? Log in") { showLogin() })
        page.addView(action("Back to home") { showHome() })
    }

    private fun showLogin() {
        val page = basePage()
        page.addView(text("Welcome back", 26f, ink, true))
        page.addView(text("Log in to DeathBook", 17f, teal, true))
        page.addView(text("Use test details only. Sign-in is not connected in this prototype.", 14f, warning))
        page.addView(field("Email address"))
        page.addView(field("Password", password = true))
        page.addView(action("Log in (demo only)") {
            Toast.makeText(this, "Demo only: sign-in is not active and details were not saved.", Toast.LENGTH_LONG).show()
        })
        page.addView(action("Create a new account") { showRegister() })
        page.addView(action("Back to home") { showHome() })
    }

    private fun showPreparation() {
        val page = basePage()
        page.addView(text("Prepare a request", 26f, ink, true))
        page.addView(text(
            "Use this general checklist to prepare for an enquiry. It is not an official list of documents or a substitute for instructions from the Department of Home Affairs.",
            15f, muted
        ))
        page.addView(text("Before contacting an authority", 19f, ink, true))
        page.addView(text(
            "1. Confirm the correct death-registration process with the Department of Home Affairs.\n\n" +
            "2. Ask which documents are required for your particular circumstances.\n\n" +
            "3. Check whether a medical practitioner, funeral undertaker or other authorised person must complete part of the process.\n\n" +
            "4. Confirm where and how the documents must be submitted.\n\n" +
            "5. Keep original documents secure and share copies only through an official, trusted channel.\n\n" +
            "6. Keep any reference number or receipt provided by the authority.",
            16f, ink
        ))
        page.addView(text("Protect personal information", 19f, ink, true))
        page.addView(text(
            "Do not enter identity numbers, medical details, certificates or other sensitive personal information into this demonstration app. It does not securely submit or store records.",
            14f, warning
        ))
        page.addView(action("Back to home") { showHome() })
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
            14f, warning
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
        page.addView(text("Current status: Not connected", 17f, warning, true))
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
