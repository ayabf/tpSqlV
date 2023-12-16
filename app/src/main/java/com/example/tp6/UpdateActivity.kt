package com.example.tp6

import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.provider.BaseColumns
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast

class UpdateActivity : AppCompatActivity() {

    lateinit var id: String
    lateinit var nomEditText: EditText
    lateinit var prenomEditText: EditText
    lateinit var phoneEditText: EditText
    lateinit var emailEditText: EditText
    lateinit var loginEditText: EditText
    lateinit var mdpEditText: EditText
    lateinit var btnUpdate: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)

        id = intent.getStringExtra("id").toString()
        nomEditText = findViewById(R.id.NomUp)
        prenomEditText = findViewById(R.id.PrenomUp)
        phoneEditText = findViewById(R.id.PhoneUp)
        emailEditText = findViewById(R.id.EmailUp)
        loginEditText = findViewById(R.id.LoginUp)
        mdpEditText = findViewById(R.id.MdpUp)

        btnUpdate = findViewById(R.id.btnUp)
        getStudent()
        btnUpdate.setOnClickListener {
            updateRecord()
        }
    }
    private fun getStudent() {
        val dbHelper = EtudiantDBHelper(this)
        val db = dbHelper.readableDatabase

        val projection = arrayOf(
            EtudiantBC.EtudiantEntry.COLUMN_NAME_NOM,
            EtudiantBC.EtudiantEntry.COLUMN_NAME_PRENOM,
            EtudiantBC.EtudiantEntry.COLUMN_NAME_PHONE,
            EtudiantBC.EtudiantEntry.COLUMN_NAME_EMAIL,
            EtudiantBC.EtudiantEntry.COLUMN_NAME_LOGIN,
            EtudiantBC.EtudiantEntry.COLUMN_NAME_MDP

        )

        val selection = "${BaseColumns._ID} = ?"
        val selectionArgs = arrayOf(id)

        val cursor: Cursor = db.query(
            EtudiantBC.EtudiantEntry.TABLE_NAME,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            null
        )

        if (cursor.moveToFirst()) {
            val nom = cursor.getString(cursor.getColumnIndexOrThrow(EtudiantBC.EtudiantEntry.COLUMN_NAME_NOM))
            val prenom = cursor.getString(cursor.getColumnIndexOrThrow(EtudiantBC.EtudiantEntry.COLUMN_NAME_PRENOM))
            val phone = cursor.getString(cursor.getColumnIndexOrThrow(EtudiantBC.EtudiantEntry.COLUMN_NAME_PHONE))
            val email = cursor.getString(cursor.getColumnIndexOrThrow(EtudiantBC.EtudiantEntry.COLUMN_NAME_EMAIL))
            val login = cursor.getString(cursor.getColumnIndexOrThrow(EtudiantBC.EtudiantEntry.COLUMN_NAME_LOGIN))
            val mdp = cursor.getString(cursor.getColumnIndexOrThrow(EtudiantBC.EtudiantEntry.COLUMN_NAME_MDP))

            nomEditText.setText(nom)
            prenomEditText.setText(prenom)
            phoneEditText.setText(phone)
            emailEditText.setText(email)
            loginEditText.setText(login)
            mdpEditText.setText(mdp)


        }

        cursor.close()
        db.close()
    }
    private fun updateRecord() {
        val nom = nomEditText.text.toString()
        val prenom = prenomEditText.text.toString()
        val phone = phoneEditText.text.toString()
        val email = emailEditText.text.toString()
        val login = loginEditText.text.toString()

        if (nom.isNotEmpty() && prenom.isNotEmpty() && phone.isNotEmpty() && email.isNotEmpty() && login.isNotEmpty()) {
            val dbHelper = EtudiantDBHelper(this)
            val db = dbHelper.writableDatabase

            val values = ContentValues().apply {
                put(EtudiantBC.EtudiantEntry.COLUMN_NAME_NOM, nom)
                put(EtudiantBC.EtudiantEntry.COLUMN_NAME_PRENOM, prenom)
                put(EtudiantBC.EtudiantEntry.COLUMN_NAME_PHONE, phone)
                put(EtudiantBC.EtudiantEntry.COLUMN_NAME_EMAIL, email)
                put(EtudiantBC.EtudiantEntry.COLUMN_NAME_LOGIN, login)
            }

            val selection = "${BaseColumns._ID} = ?"
            val selectionArgs = arrayOf(id)

            val count = db.update(
                EtudiantBC.EtudiantEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs
            )

            if (count > 0) {
                Toast.makeText(this, "Record updated successfully", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@UpdateActivity, ListEtudiant::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Failed to update record", Toast.LENGTH_SHORT).show()
            }
            db.close()
        } else {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
        }
    }
}
