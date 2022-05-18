package com.kiwipills.kiwipillsapp

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.kiwipills.kiwipillsapp.Utils.CAMERA_CODE
import com.kiwipills.kiwipillsapp.Utils.Globals
import com.kiwipills.kiwipillsapp.Utils.IMAGE_PICK_CODE
import com.kiwipills.kiwipillsapp.Utils.ImageUtilities
import com.kiwipills.kiwipillsapp.service.Models.User
import com.kiwipills.kiwipillsapp.service.RestEngine
import com.kiwipills.kiwipillsapp.service.Service
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.util.*


class ProfileActivity : AppCompatActivity() {

    var img_PicReg: ImageView? = null

    var imgArray:ByteArray? =  null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val toolbar = findViewById<Toolbar>(R.id.toolbarprofile)
        setSupportActionBar(toolbar)
        toolbar.setTitle("Perfil")

        var actionBar = getSupportActionBar()

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
        }

        val iv_userImage = findViewById<ImageView>(R.id.iv_userImage_prof)
        val txt_username = findViewById<TextView>(R.id.lbl_username_prof)
        val txt_username_edit = findViewById<EditText>(R.id.txt_user_prof)
        val txt_pwd = findViewById<EditText>(R.id.txt_pwd_prof)
        val txt_name = findViewById<EditText>(R.id.txt_name_prof)
        val txt_lastname1 = findViewById<EditText>(R.id.txt_lastname1_prof)
        val txt_lastname2 = findViewById<EditText>(R.id.txt_lastname2_prof)
        val txt_email = findViewById<EditText>(R.id.txt_email_prof)
        val txt_phone = findViewById<EditText>(R.id.txt_phone_prof)
        img_PicReg = findViewById(R.id.iv_userImage_prof)

        val prof_layout =findViewById<LinearLayout>(R.id.profLayout)
        val btn_startedit = findViewById<Button>(R.id.btn_startedit)
        val btn_logout = findViewById<Button>(R.id.btn_profile_logout)
        val btn_image = findViewById<Button>(R.id.btn_imgprof)
        val btn_cancel = findViewById<Button>(R.id.btn_cancelprof)
        val btn_update = findViewById<Button>(R.id.btn_saveprof)

        if(Globals.DB){
            var title_string = "Perfil de "+Globals.UserLogged.username
            txt_username.text = title_string
            txt_username_edit.setText(Globals.UserLogged.username)
            txt_pwd.setText(Globals.UserLogged.password)
            txt_name.setText(Globals.UserLogged.name)
            txt_lastname1.setText(Globals.UserLogged.lastname01)
            txt_lastname2.setText(Globals.UserLogged.lastname02)
            txt_email.setText(Globals.UserLogged.email)
            txt_phone.setText(Globals.UserLogged.phone)

            //Imagen de usuario
            if(Globals.UserLogged.image != ""){
                var byteArray:ByteArray? = null
                val strImage:String = Globals.UserLogged.image!!.replace("data:image/png;base64,","")
                byteArray =  Base64.getDecoder().decode(strImage)
                iv_userImage.setImageBitmap(ImageUtilities.getBitMapFromByteArray(byteArray))
            }

        }

        //Boton de editar perfil, muestra/esconde los botones necesarios y activa los inputs
        btn_startedit.setOnClickListener { view ->
            btn_startedit.visibility = View.GONE
            btn_logout.visibility = View.GONE
            btn_update.visibility = View.VISIBLE
            btn_image.visibility = View.VISIBLE
            btn_cancel.visibility = View.VISIBLE
            txt_pwd.text.clear() //Limpia el campo de password para que la tenga que volver a escribir
            prof_layout.background.setTint(view.resources.getColor(R.color.white)) //Cambia fondo a blanco para reflejar comienzo de edit

            //Activar inputs
            startEdit(txt_username_edit)
            startEdit(txt_pwd)
            startEdit(txt_name)
            startEdit(txt_lastname1)
            startEdit(txt_lastname2)
            startEdit(txt_phone)

        }

        btn_cancel.setOnClickListener {
            finish()
            val i = Intent(this@ProfileActivity, ProfileActivity::class.java)
            startActivity(i)
        }

        //SELECCIONAR IMAGEN DE GALERIA O CAMARA
        btn_image.setOnClickListener {
            //inflate el dialogo con el diseño
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

        //Boton de actualizar perfil
        btn_update.setOnClickListener {
            val pwdin = txt_pwd.text.toString()
            val usernamein = txt_username_edit.text.toString()
            val namein = txt_name.text.toString()
            val lastname01in = txt_lastname1.text.toString()
            val lastname02in = txt_lastname2.text.toString()
            val phonein = txt_phone.text.toString()

            var encodedString:String = ""
            var strEncodeImage:String = ""
            if(imgArray != null){
                encodedString =  Base64.getEncoder().encodeToString(imgArray)
                strEncodeImage= "data:image/png;base64," + encodedString
            }

            if(checkfields(usernamein, pwdin)){

                val user = User(
                    Globals.UserLogged.id,
                    Globals.UserLogged.email,
                    pwdin,
                    usernamein,
                    namein,
                    lastname01in,
                    lastname02in,
                    phonein,
                    strEncodeImage
                )

                updateprofile(user)
            }
        }

        //Boton de logout
        btn_logout.setOnClickListener {
            val activityIntent = Intent(this,LogInActivity::class.java)
            Globals.UserLogged = User()
            startActivity(activityIntent)
            finish()
        }


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return super.onOptionsItemSelected(item)
    }

    //Funcion que activa los inputs que le lleguen
    fun startEdit(et: EditText){
        et.isFocusable = true
        et.isClickable = true
        et.isFocusableInTouchMode = true
        et.isEnabled = true
    }

    //Validaciones
    fun checkfields(username: String, password: String) : Boolean {

        if(username == ""){
            Toast.makeText(this@ProfileActivity, "Usuario requerido", Toast.LENGTH_SHORT).show()
            return false
        }

        if(password == ""){
            Toast.makeText(this@ProfileActivity, "Contraseña requerida", Toast.LENGTH_SHORT).show()
            return false
        }

        var regexPw = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}\$")
        if (!password.matches(regexPw)){
            Toast.makeText(this@ProfileActivity, "Contraseña invalida. Debe llevar al menos 8 caracteres, 1 mayúscula, 1 minúscula y 1 dígito", Toast.LENGTH_LONG).show()
            return false
        }

        return true
    }

    //Funcion que actualiza el perfil
    fun updateprofile(userData: User){

        val service: Service =  RestEngine.getRestEngine().create(Service::class.java)
        val result: Call<User> = service.updateprofile(userData)

        result.enqueue(object: Callback<User> {
            override fun onFailure(call: Call<User>, t: Throwable) {
                Toast.makeText(this@ProfileActivity,"Error al actualizar datos del perfil",Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<User>, response: Response<User>) {

                Globals.UserLogged = response.body()!!
                Log.d("Nuevos datos: ", Globals.UserLogged.toString())
                Toast.makeText(this@ProfileActivity, Globals.UserLogged.username, Toast.LENGTH_LONG).show()

                //Resetear la activity (para actualizar datos)
                recreate()

            }

        })
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
            //RESPUESTA DE LA CÁMARA CONTIENE LA IMAGEN
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

}