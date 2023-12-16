package com.example.tp6

import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var Nom: EditText
    private lateinit var Prenom: EditText
    private lateinit var Tel: EditText
    private lateinit var Email: EditText
    private lateinit var Login: EditText
    private lateinit var MDP: EditText
    private lateinit var btnValider: Button
    private lateinit var btnAnnuler: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Nom = findViewById(R.id.Nom)
        Prenom = findViewById(R.id.Prenom)
        Tel = findViewById(R.id.Phone)
        Email = findViewById(R.id.Email)
        Login = findViewById(R.id.Login)
        MDP = findViewById(R.id.Mdp)
        btnValider = findViewById(R.id.btnValider)
        btnAnnuler = findViewById(R.id.btnAnnuler)

        btnValider.setOnClickListener {
            if (isAnyFieldEmpty()) {
                showAlertDialog("Attention", "Tous les champs doivent être remplis")
            } else {
                showConfirmationDialog()
            }
        }

        btnAnnuler.setOnClickListener {
            showConfirmationResetDialog()
        }
    }

    private fun isAnyFieldEmpty(): Boolean {
        return Nom.text.isNullOrEmpty() || Prenom.text.isNullOrEmpty() ||
                Tel.text.isNullOrEmpty() || Email.text.isNullOrEmpty() ||
                Login.text.isNullOrEmpty() || MDP.text.isNullOrEmpty()
    }

    private fun showAlertDialog(title: String, message: String) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK") { dialogInterface: DialogInterface, _: Int ->
                dialogInterface.dismiss()
            }
            .create()
            .show()
    }

    private fun showConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Confirmation")
            .setMessage("Voulez-vous vraiment valider?")
            .setPositiveButton("Oui") { _, _ -> saveDataAndNavigateToList() }
            .setNegativeButton("Non") { dialogInterface: DialogInterface, _: Int ->
                dialogInterface.dismiss()
            }
            .create()
            .show()
    }

    private fun showConfirmationResetDialog() {
        AlertDialog.Builder(this)
            .setTitle("Attention")
            .setMessage("Voulez-vous vraiment remettre à zéro les champs?")
            .setPositiveButton("Oui") { _, _ -> resetFields() }
            .setNegativeButton("Non") { dialogInterface: DialogInterface, _: Int ->
                dialogInterface.dismiss()
            }
            .create()
            .show()
    }

    private fun saveDataAndNavigateToList() {
        val values = ContentValues().apply {
            put(EtudiantBC.EtudiantEntry.COLUMN_NAME_NOM, Nom.text.toString())
            put(EtudiantBC.EtudiantEntry.COLUMN_NAME_PRENOM, Prenom.text.toString())
            put(EtudiantBC.EtudiantEntry.COLUMN_NAME_PHONE, Tel.text.toString())
            put(EtudiantBC.EtudiantEntry.COLUMN_NAME_EMAIL, Email.text.toString())
            put(EtudiantBC.EtudiantEntry.COLUMN_NAME_LOGIN, Login.text.toString())
            put(EtudiantBC.EtudiantEntry.COLUMN_NAME_MDP, MDP.text.toString())
        }

        val mDbHelper = EtudiantDBHelper(applicationContext)
        val db = mDbHelper.writableDatabase
        db.insert("etudiant", null, values)
        val intent = Intent(this, ListEtudiant::class.java)
        startActivity(intent)
        db.close()
        mDbHelper.close()
    }

    private fun resetFields() {
        Nom.setText("")
        Prenom.setText("")
        Tel.setText("")
        Email.setText("")
        Login.setText("")
        MDP.setText("")
    }
}
