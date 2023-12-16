package com.example.tp6

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.provider.BaseColumns
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import android.widget.SimpleCursorAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast

class ListEtudiant : AppCompatActivity() {
    private lateinit var adapter: CustomCursorAdapter
    private lateinit var dbHelper: EtudiantDBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_etudiant)

        dbHelper = EtudiantDBHelper(this)

        try {
            adapter = CustomCursorAdapter(
                this,
                R.layout.ligne_etudiant,
                dbHelper.readableDatabase.rawQuery("SELECT * FROM etudiant", null),
                arrayOf(
                    EtudiantBC.EtudiantEntry.COLUMN_NAME_NOM,
                    EtudiantBC.EtudiantEntry.COLUMN_NAME_PRENOM
                ),
                intArrayOf(R.id.nom, R.id.prenom),
                0
            )

            val listEtudiant = findViewById<ListView>(R.id.idlistetu)
            listEtudiant.adapter = adapter
        } catch (e: Exception) {
            Log.e("ListEtudiant", "Error setting up adapter", e)
        }
    }

    inner class CustomCursorAdapter(
        context: Context,
        layout: Int,
        c: Cursor?,
        from: Array<out String>?,
        to: IntArray?,
        flags: Int
    ) : SimpleCursorAdapter(context, layout, c, from, to, flags) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val view = super.getView(position, convertView, parent)

            val cursor = getItem(position) as Cursor
            val btnModify = view.findViewById<Button>(R.id.btnModify)
            val btnDelete = view.findViewById<Button>(R.id.btnDelete)

            val selectedId =
                cursor.getString(cursor.getColumnIndexOrThrow(BaseColumns._ID))

            btnModify.setOnClickListener {
                update(selectedId)
            }

            btnDelete.setOnClickListener {
                delete(selectedId)
            }

            return view
        }

        private fun delete(id: String) {
            try {
                val db = dbHelper.writableDatabase

                val selection = "${BaseColumns._ID} = ?"
                val selectionArgs = arrayOf(id)
                db.delete(EtudiantBC.EtudiantEntry.TABLE_NAME, selection, selectionArgs)

                adapter.changeCursor(dbHelper.getCursor())

                Toast.makeText(applicationContext, "Deleted student", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Log.e("CustomCursorAdapter", "Error deleting item", e)
            }
        }
        private fun update(id: String) {
            val intent = Intent(this@ListEtudiant, UpdateActivity::class.java)
            intent.putExtra("id", id)
            startActivity(intent)        }
    }
}
