package fr.epf.footvlg.fragment

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import fr.epf.footvlg.API.APIService

import fr.epf.footvlg.R
import fr.epf.footvlg.interfaces.NavigationHost
import fr.epf.footvlg.models.Member
import fr.epf.footvlg.utils.UserDAO
import fr.epf.footvlg.utils.hashString
import fr.epf.footvlg.utils.retrofit
import kotlinx.android.synthetic.main.fragment_account.*
import kotlinx.android.synthetic.main.fragment_account.view.*
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AccountFragment : Fragment() {
    lateinit var encryptPassword:String
    private lateinit var user: Member
    private val apiService = retrofit().create(APIService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_account, container, false)
        runBlocking {
            user = UserDAO().getUser()!!
        }
        view.last_name.setText(user.last_name)
        view.first_name.setText(user.first_name)
        view.Email.setText(user.email)
        view.phoneNumber.setText(user.phone_number)
        view.birthday.setText(user.birthday)


        view.validate_editProfil.setOnClickListener {
            VALIDATEDATA()
        }
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if(user.is_admin || user.is_superuser){
            inflater.inflate(R.menu.admin_menu, menu)
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        when(item.itemId){
            R.id.list_users -> {
                (activity as NavigationHost).navigateTo(AdminFragment(), false)
                return true
            }
            else -> return false
        }
    }
    private fun VALIDATEDATA(){
        val last_nameTEXT = last_name.text.toString().trim()
        val first_nameTEXT = first_name.text.toString().trim()
        val emailTEXT = Email.text.toString().trim()
        val phoneTEXT = phoneNumber.text.toString().trim()
        val birthdayTEXT = birthday.text.toString().trim()
        val passwordTEXT = password.text.toString().trim()
        val password_confirmTEXT = password2.text.toString().trim()

        if(TextUtils.isEmpty(last_nameTEXT) ||
            TextUtils.isEmpty(first_nameTEXT) ||
            TextUtils.isEmpty(phoneTEXT) ||
            TextUtils.isEmpty(emailTEXT) ||
            TextUtils.isEmpty(birthdayTEXT)){

            if(TextUtils.isEmpty(last_nameTEXT)){
                last_name.error = getString(R.string.emptyField)
            }
            if(TextUtils.isEmpty(first_nameTEXT)){
                first_name.error = getString(R.string.emptyField)
            }
            if(TextUtils.isEmpty(phoneTEXT)){
                phoneNumber.error = getString(R.string.emptyField)
            }
            if(TextUtils.isEmpty(birthdayTEXT)){
                birthday.error = getString(R.string.emptyField)
            }
            if(TextUtils.isEmpty(emailTEXT)){
                Email.error = getString(R.string.emptyField)
            }
        }
        else if(!isEmailValid(emailTEXT)){
            Email.error = getString(R.string.invalidEmail)
        }
        else if(!isValidBirthday(birthdayTEXT)){
            birthday.error = getString(R.string.dateFormat)
        }
        else if(!TextUtils.isEmpty(passwordTEXT) && !TextUtils.isEmpty(password_confirmTEXT)){
            if(checkPassword(passwordTEXT,password_confirmTEXT)){
                Toast.makeText(context,"Mise à jour du compte...", Toast.LENGTH_SHORT).show()
                val member = Member(user.id,last_nameTEXT,first_nameTEXT,phoneTEXT,emailTEXT,birthdayTEXT,encryptPassword,user.inscription_date,user.is_admin,user.is_superuser)
                UpdateMember(member)
            }
        }
        else{
            Toast.makeText(context,"Mise à jour du compte...", Toast.LENGTH_SHORT).show()
            val member = Member(user.id,last_nameTEXT,first_nameTEXT,phoneTEXT,emailTEXT,birthdayTEXT,user.password,user.inscription_date,user.is_admin,user.is_superuser)
            UpdateMember(member)
        }

    }
    private fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun UpdateMember(member:Member){
        runBlocking {
            UserDAO().updateUser(member)
        }
        val requestCall = apiService.updateUser(member)

        requestCall.enqueue(object: Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(context,"Erreur ${t}", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<String>, response: Response<String>) {
                val bool = response.isSuccessful
                if(bool){
                    Toast.makeText(context,"${response.body()}", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(context,"Aucune donnée n'a été modifié", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
    private fun isValidBirthday(birthday:String):Boolean{
        val regex="^[0-9]{4}-[0-9]{2}-[0-9]{2}$"
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
