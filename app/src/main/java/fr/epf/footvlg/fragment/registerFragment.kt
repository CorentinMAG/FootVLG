package fr.epf.footvlg.fragment

import android.os.Bundle
import android.text.TextUtils
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
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.android.synthetic.main.fragment_register.view.*
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class registerFragment : Fragment() {
    lateinit var encryptPassword:String
     var user:Member? = null

    //enable options menu in this fragment
    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }
    //inflate the menu
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
    //handle item clicks of menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //get item id to handle item clicks
        val id = item.itemId
        //handle item clicks
        if (id == R.id.back_button){
            (activity as NavigationHost).navigateTo(loginFragment(), false) // Navigate to the next Fragment

        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_register, container, false)

        view.validate_registration.setOnClickListener {
            VALIDATEData()
        }
        return view
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
                last_name.error = getString(R.string.emptyField)
            }
            if(TextUtils.isEmpty(first_nameTEXT)){
                first_name.error = getString(R.string.emptyField)
            }
            if(TextUtils.isEmpty(emailTEXT)){
                Email.error = getString(R.string.emptyField)
            }
            if(TextUtils.isEmpty(phoneTEXT)){
                phoneNumber.error = getString(R.string.emptyField)
            }
            if(TextUtils.isEmpty(birthdayTEXT)){
                birthday.error = getString(R.string.emptyField)
            }
            if(TextUtils.isEmpty(passwordTEXT)){
                password.error = getString(R.string.emptyField)
            }
            if(TextUtils.isEmpty(password_confirmTEXT)){
                password2.error = getString(R.string.emptyField)
            }
        }
        else if(!isEmailValid(emailTEXT)){
            Email.error = getString(R.string.invalidEmail)
        }
        else if(!isValidBirthday(birthdayTEXT)){
            birthday.error = getString(R.string.dateFormat)
        }

        else{
            if(checkPassword(passwordTEXT,password_confirmTEXT)){
                Toast.makeText(context,"Création du compte...", Toast.LENGTH_SHORT).show()
                val member = Member(0,last_nameTEXT,first_nameTEXT,phoneTEXT,emailTEXT,birthdayTEXT,encryptPassword)
                REGISTERMember(member)
            }
        }
    }

    private fun REGISTERMember(member: Member){
        val apiService = retrofit().create(APIService::class.java)
        val requestCall = apiService.createMember(member)

        requestCall.enqueue(object: Callback<Member> {
            override fun onFailure(call: Call<Member>, t: Throwable) {
                Toast.makeText(context,"Erreur ${t}", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<Member>, response: Response<Member>) {
                if(response.isSuccessful){
                    val UserDAO = UserDAO()
                    runBlocking {
                        val count = UserDAO.CheckUser()
                        if(count==0){
                            user = response.body()
                            if(user!=null){
                                UserDAO.addUser(user!!)
                                (activity as NavigationHost).navigateTo(mainFragment(), false) // Navigate to the next Fragment
                            }else{
                                Toast.makeText(context,"Impossible de créer un utilisateur",
                                    Toast.LENGTH_LONG).show()
                            }

                        }else if(count==1){
                            Toast.makeText(context,"Il existe déjà un utilisateur dans la base locale, veuillez le supprimer et recommencer cette étape",
                                Toast.LENGTH_LONG).show()
                        }else{
                            Toast.makeText(context,"Erreur, il existe plusieurs utilisateurs dans la base locale",
                                Toast.LENGTH_LONG).show()
                        }
                    }
                }else{
                    Email.error = "ce numéro et/ou cet email est déjà pris"
                    phoneNumber.error = "ce numéro et/ou cet email est déjà pris"
                    Toast.makeText(context,"Le compte ne peut être créé car il existe déjà un utilisateur dans la base de données",
                        Toast.LENGTH_SHORT).show()
                }
            }

        })

    }
    private fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
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
