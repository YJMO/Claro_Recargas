package com.example.clarorecargas

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //CAMBIA EL COLOR DE LA BARRA DE STATUS
        if (Build.VERSION.SDK_INT >= 21) {
            val window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = this.resources.getColor(R.color.redclaro)
        }

        val btLlamar : Button = findViewById(R.id.recargar)
        val btSaldo : Button = findViewById(R.id.c_saldo)

        btSaldo.setOnClickListener {
            val comando = "*789*11*2*0953"+ Uri.encode("#")
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.CALL_PHONE) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                val miIntent = Intent(
                    Intent.ACTION_CALL,
                    Uri.parse("tel:"+comando)
                )

                startActivity(miIntent)
            }
            else {
                Toast.makeText(this,
                    "No hay permiso para llamada",
                    Toast.LENGTH_LONG).show()
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CALL_PHONE),
                    123
                )
            }
        }

        btLlamar.setOnClickListener {

            fun validarForm(): Boolean {
                var esValido = true

                if (TextUtils.isEmpty(findViewById<EditText>(R.id.telefono).text.toString())) {
                    // Si la propiedad error tiene valor, se muestra el aviso.
                    findViewById<EditText>(R.id.telefono).error = "Requerido"
                    esValido = false
                } else findViewById<EditText>(R.id.telefono).error = null

                if (TextUtils.isEmpty(findViewById<EditText>(R.id.monto).text.toString())) {
                    // Si la propiedad error tiene valor, se muestra el aviso.
                    findViewById<EditText>(R.id.monto).error = "Requerido"
                    esValido = false
                } else findViewById<EditText>(R.id.telefono).error = null

                if (TextUtils.isEmpty(findViewById<EditText>(R.id.codigo).text.toString())) {
                    // Si la propiedad error tiene valor, se muestra el aviso.
                    findViewById<EditText>(R.id.codigo).error = "Requerido"
                    esValido = false
                } else findViewById<EditText>(R.id.telefono).error = null

                if (findViewById<EditText>(R.id.codigo).text.toString()!="0953") {
                    // Si la propiedad error tiene valor, se muestra el aviso.
                    findViewById<EditText>(R.id.codigo).error = "Codigo Incorrecto"
                    esValido = false
                } else findViewById<EditText>(R.id.telefono).error = null

                if (findViewById<EditText>(R.id.telefono).text.toString().length != 9 ) {
                    // Si la propiedad error tiene valor, se muestra el aviso.
                    findViewById<EditText>(R.id.telefono).error = "Son 9 digitos"
                    esValido = false
                } else findViewById<EditText>(R.id.telefono).error = null

                return esValido
            }

            if(validarForm()==true){
                val num_phone : Editable? = findViewById<EditText>(R.id.telefono).text
                val monto : Editable? = findViewById<EditText>(R.id.monto).text
                val comando = "*789*1*"+num_phone+"*"+monto+"*1*0953"+ Uri.encode("#")

                val dialogo: android.app.AlertDialog? =
                    android.app.AlertDialog.Builder(this@MainActivity) // NombreDeTuActividad.this, o getActivity() si es dentro de un fragmento
                        .setPositiveButton(
                            "Recargar",
                            DialogInterface.OnClickListener { dialog, which ->
                                // Hicieron click en el botón positivo, así que la acción está confirmada

                                if (ContextCompat.checkSelfPermission(this,
                                        Manifest.permission.CALL_PHONE) ==
                                    PackageManager.PERMISSION_GRANTED
                                ) {
                                    val miIntent = Intent(
                                        Intent.ACTION_CALL,
                                        Uri.parse("tel:"+comando)
                                    )

                                    startActivity(miIntent)
                                }
                                else {
                                    Toast.makeText(this,
                                        "No hay permiso para llamada",
                                        Toast.LENGTH_LONG).show()
                                    ActivityCompat.requestPermissions(
                                        this,
                                        arrayOf(Manifest.permission.CALL_PHONE),
                                        123
                                    )
                                }

                                findViewById<EditText>(R.id.telefono).setText("");
                                findViewById<EditText>(R.id.monto).setText("");
                                findViewById<EditText>(R.id.codigo).setText("");

                                finishAffinity()
                            })
                        .setNegativeButton(
                            "Cancelar",
                            DialogInterface.OnClickListener { dialog, which -> // Hicieron click en el botón negativo, no confirmaron
                                // Simplemente descartamos el diálogo
                                dialog.dismiss()
                            })
                        .setTitle("Confirmar") // El título
                        .setMessage("¿Deseas realizar la recarga al Numero: "+num_phone+" por "+monto+" Soles") // El mensaje
                        .create() // No olvides llamar a Create, ¡pues eso crea el AlertDialog!

                if (dialogo != null) {
                    dialogo.show()
                }
            }
        }
    }
}