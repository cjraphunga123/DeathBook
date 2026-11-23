package za.co.deathbook.app

import android.app.Activity
import android.os.Bundle
import android.graphics.Color
import android.graphics.Typeface
import android.text.InputType
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : Activity() {
    private val ink = Color.rgb(28, 42, 58)
    private val teal = Color.rgb(0, 112, 112)
    private val muted = Color.rgb(100, 116, 130)
    private val warning = Color.rgb(150, 72, 30)
    private val auth by lazy { FirebaseAuth.getInstance() }
    private val db by lazy { FirebaseFirestore.getInstance() }

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

    private fun field(hintText: String, password: Boolean = false): EditText = EditText(this).apply {
        hint = hintText
        textSize = 16f
        setSingleLine(true)
        inputType = if (password) {
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        } else {
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
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

        val user = auth.currentUser
        if (user == null) {
            page.addView(text("Your account", 20f, ink, true))
            page.addView(action("Create an account") { showRegister() })
            page.addView(action("Log in") { showLogin() })
        } else {
            page.addView(text("Signed in as", 18f, ink, true))
            page.addView(text(user.email ?: "Authenticated user", 16f, teal))
            page.addView(action("Log out") {
                auth.signOut()
                showHome()
                Toast.makeText(this, "You have logged out.", Toast.LENGTH_SHORT).show()
            })
        }

        page.addView(text("Your services", 20f, ink, true))
        page.addView(text("Prepare a request\nLearn what to check before contacting the relevant authorities.", 16f))
        page.addView(action("Open preparation checklist") { showPreparation() })
        page.addView(text("Submit a record\nStart a death-record submission request.", 16f))
        page.addView(action("Prepare a request form") { showRequestForm() })
        page.addView(text("Verification status\nCheck the progress of a submitted request.", 16f))
        page.addView(action("View verification information") { showVerification() })
        page.addView(text("Institution access\nFor authorised organisations and officials.", 16f))
        page.addView(action("Institution information") { showInstitution() })
        page.addView(text(
            "IMPORTANT: This build is a demonstration. It is not connected to the Department of Home Affairs and does not verify, register or certify deaths. Do not enter real personal or identity information.",
            13f, warning
        ))
        page.addView(text("DeathBook • Version 1.1.0", 12f, muted))
    }

    private fun showRegister() {
        val page = basePage()
        page.addView(text("Create your account", 26f, ink, true))
        page.addView(text("Register with your email address", 16f, teal, true))
        page.addView(text("Use an email address you can access. Passwords must contain at least 6 characters.", 14f, muted))
        val email = field("Email address")
        val password = field("Password (minimum 6 characters)", password = true)
        val confirm = field("Confirm password", password = true)
        page.addView(email)
        page.addView(password)
        page.addView(confirm)
        lateinit var register: Button
        register = action("Create account") {
            val emailValue = email.text.toString().trim()
            val passwordValue = password.text.toString()
            val confirmValue = confirm.text.toString()
            when {
                !Patterns.EMAIL_ADDRESS.matcher(emailValue).matches() ->
                    email.error = "Enter a valid email address"
                passwordValue.length < 6 ->
                    password.error = "Use at least 6 characters"
                passwordValue != confirmValue ->
                    confirm.error = "Passwords do not match"
                else -> {
                    register.isEnabled = false
                    auth.createUserWithEmailAndPassword(emailValue, passwordValue)
                        .addOnCompleteListener(this) { task ->
                            register.isEnabled = true
                            if (task.isSuccessful) {
                                Toast.makeText(this, "Account created successfully.", Toast.LENGTH_LONG).show()
                                showHome()
                            } else {
                                Toast.makeText(
                                    this,
                                    task.exception?.localizedMessage ?: "Registration failed. Please try again.",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                }
            }
        }
        page.addView(register)
        page.addView(action("Already have an account? Log in") { showLogin() })
        page.addView(action("Back to home") { showHome() })
    }

    private fun showLogin() {
        val page = basePage()
        page.addView(text("Welcome back", 26f, ink, true))
        page.addView(text("Log in to DeathBook", 17f, teal, true))
        val email = field("Email address")
        val password = field("Password", password = true)
        page.addView(email)
        page.addView(password)
        lateinit var login: Button
        login = action("Log in") {
            val emailValue = email.text.toString().trim()
            val passwordValue = password.text.toString()
            when {
                !Patterns.EMAIL_ADDRESS.matcher(emailValue).matches() ->
                    email.error = "Enter a valid email address"
                passwordValue.isEmpty() ->
                    password.error = "Enter your password"
                else -> {
                    login.isEnabled = false
                    auth.signInWithEmailAndPassword(emailValue, passwordValue)
                        .addOnCompleteListener(this) { task ->
                            login.isEnabled = true
                            if (task.isSuccessful) {
                                Toast.makeText(this, "Login successful.", Toast.LENGTH_SHORT).show()
                                showHome()
                            } else {
                                Toast.makeText(
                                    this,
                                    task.exception?.localizedMessage ?: "Login failed. Check your details.",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                }
            }
        }
        page.addView(login)
        page.addView(action("Forgot password? Send reset email") {
            val emailValue = email.text.toString().trim()
            if (!Patterns.EMAIL_ADDRESS.matcher(emailValue).matches()) {
                email.error = "Enter your account email first"
            } else {
                auth.sendPasswordResetEmail(emailValue).addOnCompleteListener(this) { task ->
                    val message = if (task.isSuccessful) {
                        "If an account exists for this email, a reset link has been sent."
                    } else {
                        task.exception?.localizedMessage ?: "Could not send reset email."
                    }
                    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                }
            }
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

    private fun showRequestForm() {
        val page = basePage()
        page.addView(text("Prepare a request", 26f, ink, true))
        page.addView(text(
            "Enter the details below to prepare a request. This is not an official death registration or Home Affairs submission.",
            15f, muted
        ))
        page.addView(text(
            "For this initial test, use fictional details only. Do not enter an identity number, medical information, certificate details or real next-of-kin information.",
            14f, warning, true
        ))

        val deceasedName = field("Deceased person's full name").apply {
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_WORDS
        }
        val dateOfDeath = field("Date of death (YYYY-MM-DD)").apply {
            inputType = InputType.TYPE_CLASS_DATETIME or InputType.TYPE_DATETIME_VARIATION_DATE
        }
        val placeOfDeath = field("Place of death (town / hospital / area)").apply {
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_WORDS
        }
        val nextOfKinName = field("Next-of-kin name").apply {
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_WORDS
        }
        val nextOfKinContact = field("Next-of-kin contact (email or phone)").apply {
            inputType = InputType.TYPE_CLASS_TEXT
        }

        page.addView(text("Deceased person's details", 19f, ink, true))
        page.addView(deceasedName)
        page.addView(dateOfDeath)
        page.addView(placeOfDeath)
        page.addView(text("Next of kin", 19f, ink, true))
        page.addView(nextOfKinName)
        page.addView(nextOfKinContact)
        page.addView(text(
            "When saved, the request is associated with your signed-in account. It is stored as a preparation record only and is not reviewed by an authority.",
            13f, muted
        ))

        lateinit var saveButton: Button
        saveButton = action("Save preparation request") {
            val user = auth.currentUser
            val nameValue = deceasedName.text.toString().trim()
            val dateValue = dateOfDeath.text.toString().trim()
            val placeValue = placeOfDeath.text.toString().trim()
            val kinNameValue = nextOfKinName.text.toString().trim()
            val kinContactValue = nextOfKinContact.text.toString().trim()

            when {
                user == null -> Toast.makeText(this, "Please log in first.", Toast.LENGTH_LONG).show()
                nameValue.isBlank() -> deceasedName.error = "Enter a name (use fictional details for testing)"
                !dateValue.matches(Regex("\\d{4}-\\d{2}-\\d{2}")) -> dateOfDeath.error = "Use YYYY-MM-DD"
                placeValue.isBlank() -> placeOfDeath.error = "Enter a place"
                kinNameValue.isBlank() -> nextOfKinName.error = "Enter a next-of-kin name"
                kinContactValue.isBlank() -> nextOfKinContact.error = "Enter a contact"
                else -> {
                    saveButton.isEnabled = false
                    val request = hashMapOf(
                        "userId" to user.uid,
                        "deceasedFullName" to nameValue,
                        "dateOfDeath" to dateValue,
                        "placeOfDeath" to placeValue,
                        "nextOfKinName" to kinNameValue,
                        "nextOfKinContact" to kinContactValue,
                        "status" to "PREPARATION_ONLY",
                        "createdAt" to FieldValue.serverTimestamp()
                    )
                    db.collection("death_records").add(request)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Preparation request saved.", Toast.LENGTH_LONG).show()
                            showHome()
                        }
                        .addOnFailureListener { error ->
                            saveButton.isEnabled = true
                            Toast.makeText(
                                this,
                                "Could not save. Check Firestore security rules and internet connection. " +
                                    (error.localizedMessage ?: ""),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                }
            }
        }
        page.addView(saveButton)
        page.addView(action("Cancel and return home") { showHome() })
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
