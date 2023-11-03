package com.example.databaseform


import android.R
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.databaseform.databinding.ActivityDealerRegistrationBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.AuthResult
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class DealerRegistration : AppCompatActivity() {

    private lateinit var binding: ActivityDealerRegistrationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDealerRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupCountrySpinner()
        setupStateSpinner()
        setupCitySpinner()

        binding.submitBtn.setOnClickListener {


            val name = binding.editTextName.text.toString()
            val email = binding.editTextEmail.text.toString()
            val contactNumber = binding.editTextcontactno.text.toString()
            val password = binding.editTextPassword.text.toString()
            val selectedCountry = binding.spinnerCountry.selectedItem?.toString() ?: ""
            val selectedState = binding.stateSpinner.selectedItem?.toString() ?: ""
            val selectedCity = binding.citySpinner.selectedItem?.toString() ?: ""

            val auth = FirebaseAuth.getInstance()

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->

                    if (task.isSuccessful) {
                        val user = task.result.user

                        // Get the user's custom claims.
                        user?.getIdToken(true)?.addOnSuccessListener { idTokenResult ->
                            val customClaims = idTokenResult.claims
                            // You can now use the `customClaims` variable to determine what actions
                            // the user is allowed to perform in your app.
                            customClaims["role"] = "dealer"
                        }
                        // Update the user's authentication token.
                        user?.updateProfile(UserProfileChangeRequest.Builder().build())
                        Toast.makeText(this, "Account created", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Failure", Toast.LENGTH_SHORT).show()
                    }

                }

            val dealerInfo = hashMapOf(

                "country" to selectedCountry,
                "state" to selectedState,
                "city" to selectedCity,
                "full_name" to name,
                "email_address" to email,
                "contact_number" to contactNumber,
                "role" to "dealer"
            )

            val dealerUserDb = Firebase.firestore
            dealerUserDb.collection("Dealer_Database").add(dealerInfo)
                .addOnSuccessListener { documentreference ->
                    Log.d("TAG", "User added with ${documentreference.id}")
                    Toast.makeText(this, "Account Registered", Toast.LENGTH_SHORT).show()
                    intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                }.addOnFailureListener { e ->
                Log.d("TAG", "Failed $e")
                Toast.makeText(this, "Cannot Register", Toast.LENGTH_SHORT).show()
            }


        }


    }

    override fun onBackPressed() {

        intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
        super.onBackPressed()
    }

    private fun setupCountrySpinner() {
        val spinner = binding.spinnerCountry
        val countryListAdapter = ArrayAdapter<String>(this, R.layout.simple_spinner_item, mutableListOf())
        countryListAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        spinner.adapter = countryListAdapter

        // Fetch dealer list and update the spinner adapter
        getCountriesList { countryList ->
            countryListAdapter.clear()
            countryListAdapter.addAll(countryList)
        }
    }

    private fun setupStateSpinner() {
        val spinner = binding.stateSpinner
        val stateListAdapter = ArrayAdapter<String>(this, R.layout.simple_spinner_item, mutableListOf())
        stateListAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        spinner.adapter = stateListAdapter

        // Fetch dealer list and update the spinner adapter
        getStatesList { statesList ->
            stateListAdapter.clear()
            stateListAdapter.addAll(statesList)
        }
    }

    private fun setupCitySpinner() {
        val spinner = binding.citySpinner
        val citiesListAdapter = ArrayAdapter<String>(this, R.layout.simple_spinner_item, mutableListOf())
        citiesListAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        spinner.adapter = citiesListAdapter

        // Fetch dealer list and update the spinner adapter
        getCitiesList { citiesList ->
            citiesListAdapter.clear()
            citiesListAdapter.addAll(citiesList)
        }
    }

    private fun getCountriesList(callback: (List<String>) -> Unit) {
        val list = mutableListOf<String>()

        val countriesArray = resources.getStringArray(com.example.databaseform.R.array.country_names)

        for (country in countriesArray){
            list.add(country)
        }
        callback(list)

    }

    private fun getStatesList(callback: (List<String>) -> Unit) {
        val list = mutableListOf<String>()

        val statesArray = resources.getStringArray(com.example.databaseform.R.array.indian_states)

        for (state in statesArray){
            list.add(state)
        }
        callback(list)

    }

    private fun getCitiesList(callback: (List<String>) -> Unit) {
        val list = mutableListOf<String>()

        val citiesArray = resources.getStringArray(com.example.databaseform.R.array.west_bengal_cities)

        for (city in citiesArray){
            list.add(city)
        }
        callback(list)

    }

}

