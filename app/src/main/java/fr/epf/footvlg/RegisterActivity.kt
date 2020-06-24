package fr.epf.footvlg

import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import fr.epf.footvlg.API.APIService
import fr.epf.footvlg.models.Member
import fr.epf.footvlg.utils.UserDAO
import fr.epf.footvlg.utils.hashString
import fr.epf.footvlg.utils.retrofit
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


//cette activité est appelé par LoginActivity

class RegisterActivity : AppCompatActivity() {

    lateinit var encryptPassword:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // affiche l'icone retour en arrière
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        validate_registration.setOnClickListener {
            VALIDATEData()
        }
    }
    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            // si on clique sur l'icone retour en arrière on termine l'activité
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    private fun VALIDATEData(){
        val last_nameTEXT = last_name.text.toString().trim()
        val first_nameTEXT = first_name.text.toString().trim()
        val emailTEXT = Email.text.toString().trim()
        val phoneTEXT = phoneNumber.text.toString().trim()
        val birthdayTEXT = birthday.text.toString().trim()
        val passwordTEXT = password.text.toString().trim()
        val password_confirmTEXT = password2.text.toString().trim()

        if(TextUtils.isEmpty(last_nameTEXT) ||
            TextUtils.isEmpty(first_nameTEXT) ||
            TextUtils.isEmpty(emailTEXT) ||
            TextUtils.isEmpty(phoneTEXT) ||
            TextUtils.isEmpty(birthdayTEXT) ||
            TextUtils.isEmpty(passwordTEXT) ||
            TextUtils.isEmpty(password_confirmTEXT)){

            if(TextUtils.isEmpty(last_nameTEXT)){
                last_name.error = "Ce champ ne peut pas être vide"
            }
            if(TextUtils.isEmpty(first_nameTEXT)){
                first_name.error = "Ce champ ne peut pas être vide"
            }
            if(TextUtils.isEmpty(emailTEXT)){
                Email.error = "Ce champ ne peut pas être vide"
            }
            if(TextUtils.isEmpty(phoneTEXT)){
                phoneNumber.error = "Ce champ ne peut pas être vide"
            }
            if(TextUtils.isEmpty(birthdayTEXT)){
                birthday.error = "Ce champ ne peut pas être vide"
            }
            if(TextUtils.isEmpty(passwordTEXT)){
                password.error = "Ce champ ne peut pas être vide"
            }
            if(TextUtils.isEmpty(password_confirmTEXT)){
                password2.error = "Ce champ ne peut pas être vide"
            }
        }
        else if(!isEmailValid(emailTEXT)){
            Email.error = "L'adresse email n'est pas valide"
        }
        else if(!isValidBirthday(birthdayTEXT)){
            birthday.error = "La date doit être au format dd/mm/yyyy"
        }

        else{
            if(checkPassword(passwordTEXT,password_confirmTEXT)){
                Toast.makeText(this,"Création du compte...",Toast.LENGTH_SHORT).show()
                val member = Member(0,last_nameTEXT,first_nameTEXT,phoneTEXT,emailTEXT,birthdayTEXT,encryptPassword)
                REGISTERMember(member)
            }

        }

    }

    private fun REGISTERMember(member:Member){
        val apiService = retrofit().create(APIService::class.java)
        val requestCall = apiService.createMember(member)

        requestCall.enqueue(object: Callback<String>{
            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(applicationContext,"Erreur",Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<String>, response: Response<String>) {
                if(response.isSuccessful){
                    val UserDAO = UserDAO()
                    runBlocking {
                        val count = UserDAO.CheckUser()
                        if(count==0){
                            UserDAO.addUser(member)
                            Toast.makeText(applicationContext,"${response.body()}",Toast.LENGTH_SHORT).show()
                            finish()
                        }else if(count==1){
                            Toast.makeText(applicationContext,"Il existe déjà un utilisateur dans la base locale, veuillez le supprimer et recommencer cette étape",Toast.LENGTH_LONG).show()
                        }else{
                            Toast.makeText(applicationContext,"Erreur, il existe plusieurs utilisateurs dans la base locale",Toast.LENGTH_LONG).show()
                        }
                    }
                }else{
                    Email.error = "ce numéro et/ou cet email est déjà pris"
                    phoneNumber.error = "ce numéro et/ou cet email est déjà pris"
                    Toast.makeText(applicationContext,"Le compte ne peut être créé car il existe déjà un utilisateur dans la base de données",Toast.LENGTH_SHORT).show()
                }
            }

        })

    }
    private fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    private fun isValidBirthday(birthday:String):Boolean{
        val regex="^[0-9]{2}/[0-9]{2}/[0-9]{4}$"
        return regex.toRegex().matches(birthday)
    }
    private fun isPasswordValid(password:String):Boolean{
        // le mot de passe doit contenir:
        // au moins un chiffre
        // au moins une lettre en minuscule
        //  au moins une lettre en majuscule
        // au moins un caractère spécial
        // pas d'espace dans le mot de passe
        // 8 caractères minimum
        val regex="^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#\$%!\\-_?&])(?=\\S+\$).{8,}"
        return regex.toRegex().matches(password)
    }
    private fun checkPassword(passwordTEXT:String,confirmPassword:String):Boolean{
        if(passwordTEXT != confirmPassword){
            password.error = "Les mots de passe sont différents"
            return false
        }else if(!isPasswordValid(passwordTEXT)){
            password.error = "le mot de passe doit contenir:\n" +
                    "au moins 1 chiffre\n" +
                    "au moins 1 lettre en minuscule\n" +
                    "au moins 1 lettre en majuscule\n" +
                    "au moins 1 caractère spécial\n" +
                    "pas d'espace dans le mot de passe\n" +
                    "8 caractères minimum"
            return false
        }
        else{
            encryptPassword = hashString(passwordTEXT,"SHA-256")
            return true
        }
    }
}
