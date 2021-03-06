package com.kiwipills.kiwipillsapp

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.MediaStore
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.kiwipills.kiwipillsapp.Utils.CAMERA_CODE
import com.kiwipills.kiwipillsapp.Utils.Globals
import com.kiwipills.kiwipillsapp.Utils.IMAGE_PICK_CODE
import com.kiwipills.kiwipillsapp.service.Models.User
import com.kiwipills.kiwipillsapp.service.RestEngine
import com.kiwipills.kiwipillsapp.service.Service
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.util.*
import java.util.regex.Pattern


class RegisterActivity : AppCompatActivity() {

    var img_PicReg: ImageView? = null

    var imgArray:ByteArray? =  null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        //obtener preferencias
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)

        val btnRegister = findViewById<Button>(R.id.btn_Register)
        val btnelectImage = findViewById<Button>(R.id.btn_sleectImage_Reg)

        val txt_email = findViewById<EditText>(R.id.txt_EmailReg)
        val txt_password = findViewById<EditText>(R.id.txt_PswdReg)
        val txt_username = findViewById<EditText>(R.id.txt_UserReg)
        val txt_name = findViewById<EditText>(R.id.txt_NameReg)
        val txt_lastname01 = findViewById<EditText>(R.id.txt_LastNameReg)
        val txt_lastname02 = findViewById<EditText>(R.id.txt_LastNameMReg)
        val txt_phone = findViewById<EditText>(R.id.txt_PhoneReg)

        img_PicReg = findViewById(R.id.img_PicReg)

        //SELECCIONAR IMAGEN DE GALERIA O CAMARA
        btnelectImage.setOnClickListener {
            //inflate el dialogo con el dise??o
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_upload_image, null)
            //Construir la alerta del dialogo
            val mBuilder = AlertDialog.Builder(this)
                .setView(mDialogView)
                .setTitle("Seleccionar")

            val mAlertDialog = mBuilder.show()

            val btnGallery = mDialogView.findViewById<TextView>(R.id.btnGallery)
            val btnCamera = mDialogView.findViewById<TextView>(R.id.btnCamera)

            btnGallery.setOnClickListener {
                mAlertDialog.dismiss()
                openGallery()
            }
            btnCamera.setOnClickListener {
                mAlertDialog.dismiss()
                openCamera()
            }

        }

        //BOTON DE REGISTRARSE
        btnRegister.setOnClickListener {
            var encodedString:String = ""
            var strEncodeImage:String = ""
            if(imgArray != null){
                encodedString =  Base64.getEncoder().encodeToString(imgArray)
                strEncodeImage= "data:image/png;base64," + encodedString
            }

            val email = txt_email.text.toString()
            val password = txt_password.text.toString()
            val username = txt_username.text.toString()
            val name = txt_name.text.toString()
            val lastname01 = txt_lastname01.text.toString()
            val lastname02 = txt_lastname02.text.toString()
            val phone = txt_phone.text.toString()

            val user = User(
                0,
                email,
                password,
                username,
                name,
                lastname01,
                lastname02,
                phone,
                strEncodeImage
            )

            if(Globals.DB){
                if(checkfields(email, password, username)){
                    signup(user,prefs)
                }
            }
        }
    }


    fun openGallery(){
        val galleryintent = Intent()
        galleryintent.setType("image/*")
        galleryintent.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(galleryintent, IMAGE_PICK_CODE)
    }
    fun openCamera(){
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, CAMERA_CODE)
    }

    override fun onActivityResult(requestcode: Int, resultcode: Int, data: Intent?) {
        super.onActivityResult(requestcode, resultcode, intent)

        if (resultcode == Activity.RESULT_OK) {

            //RESPUESTA DE LA GALERIA CONTIENE LA IMAGEN
            if (requestcode == IMAGE_PICK_CODE){

                val uri: Uri? = data?.data
                img_PicReg?.setImageURI(uri)

                val bitmap = (img_PicReg?.drawable as BitmapDrawable).bitmap
                val stream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream)

                imgArray = stream.toByteArray()

            }
            //RESPUESTA DE LA C??MARA CONTIENE LA IMAGEN
            if (requestcode == CAMERA_CODE) {

                val uri = data?.extras
                val photo =  uri?.get("data") as Bitmap

                val stream = ByteArrayOutputStream()
                //Bitmap.CompressFormat agregar el formato desado, estoy usando aqui jpeg
                photo.compress(Bitmap.CompressFormat.JPEG, 80, stream)
                //Variable donde se guarda la imagen para su envio
                imgArray = stream.toByteArray()

                //Mostramos la imagen en la vista
                img_PicReg?.setImageBitmap(photo)

            }

        }
    }

    fun signup(userData: User, prefs:SharedPreferences){

        val service: Service =  RestEngine.getRestEngine().create(Service::class.java)
        val result: Call<User> = service.signup(userData)

        result.enqueue(object: Callback<User> {
            override fun onFailure(call: Call<User>, t: Throwable) {
                Toast.makeText(this@RegisterActivity,"Este correo ya esta registrado",Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<User>, response: Response<User>) {

                    Globals.UserLogged = response.body()!!
                    updatePrefers(prefs)
                    Log.d("Usuario logueado: ", Globals.UserLogged.toString())
                    val activityIntent = Intent(this@RegisterActivity,MainActivity::class.java)
                    startActivity(activityIntent)
                    finish()

            }

        })
    }

    fun checkfields(email: String, password: String, username: String) : Boolean {
        if(email == ""){
            Toast.makeText(this@RegisterActivity, "Correo requerido", Toast.LENGTH_SHORT).show()
            return false
        }

        var regexPw = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}\$")
        if (!password.matches(regexPw)){
            Toast.makeText(this@RegisterActivity, "Contrase??a invalida. Debe llevar al menos 8 caracteres, 1 may??scula, 1 min??scula y 1 d??gito", Toast.LENGTH_LONG).show()
            return false
        }
        if(password == ""){
            Toast.makeText(this@RegisterActivity, "Contrase??a requerida", Toast.LENGTH_SHORT).show()
            return false
        }
        if(username == ""){
            Toast.makeText(this@RegisterActivity, "Usuario requerido", Toast.LENGTH_SHORT).show()
            return false
        }
        if (!validarEmail(email)){
            Toast.makeText(this@RegisterActivity, "Correo no v??lido", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun validarEmail(email: String): Boolean {
        val pattern: Pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }

    fun updatePrefers(prefs:SharedPreferences){
        val edit = prefs.edit()
        edit.putBoolean("session", true)
        edit.putInt("id", Globals.UserLogged.id!!)
        edit.putString("email", Globals.UserLogged.email)
        edit.putString("username", Globals.UserLogged.username)
        edit.putString("password", Globals.UserLogged.password)
        edit.putString("name", Globals.UserLogged.name)
        edit.putString("lastname01", Globals.UserLogged.lastname01)
        edit.putString("lastname02", Globals.UserLogged.lastname02)
        edit.putString("phone", Globals.UserLogged.phone)
        edit.putString("image", Globals.UserLogged.image)

        edit.apply()
    }

}