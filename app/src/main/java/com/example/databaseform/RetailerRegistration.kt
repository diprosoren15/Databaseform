package com.example.databaseform

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.databaseform.databinding.ActivityRetailerRegistrationBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RetailerRegistration : AppCompatActivity() {

    private lateinit var binding: ActivityRetailerRegistrationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRetailerRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupCountrySpinner()
        setupStateSpinner()
        setupCitySpinner()

        binding.citySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedCity = parent.getItemAtPosition(position).toString()
                setupDealersSpinner(selectedCity)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        binding.submitBtn.setOnClickListener {
            val name = binding.editTextName.text.toString()
            val email = binding.editTextEmail.text.toString()
            val contactNumber = binding.editTextcontactno.text.toString()
            val selectedDealer = binding.localDealersSpinner.selectedItem?.toString() ?: ""
            val selectedCountry = binding.spinnerCountry.selectedItem?.toString() ?: ""
            val selectedState = binding.stateSpinner.selectedItem?.toString() ?: ""
            val selectedCity = binding.citySpinner.selectedItem?.toString() ?: ""

            // Validate user input here

            val password = binding.editTextPassword.text.toString() // Declare and initialize password
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
                            customClaims["role"] = "retailer"
                        }
                        // Update the user's authentication token.
                        user?.updateProfile(UserProfileChangeRequest.Builder().build())
                        Toast.makeText(this, "Account created", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Failure", Toast.LENGTH_SHORT).show()
                    }

                }

            setupDealersSpinner(selectedCity)
            addToDatabase(name, email, contactNumber, selectedDealer, selectedCountry, selectedState, selectedCity)
        }
    }

    private fun setupDealersSpinner(selectedCity: String) {
        val spinner = binding.localDealersSpinner
        val dealerListAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mutableListOf())
        dealerListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = dealerListAdapter

        // Get the selected country and state from the spinners
        val selectedCountry = binding.spinnerCountry.selectedItem?.toString() ?: ""
        val selectedState = binding.stateSpinner.selectedItem?.toString() ?: ""

        // Clear the dealerListAdapter before adding the new dealers from the database
        dealerListAdapter.clear()

        // Query the database for dealers in the selected city, state, and country
        val dealerUserDb = Firebase.firestore
        dealerUserDb.collection("Dealer_Database")
            .whereEqualTo("city", selectedCity)
            .whereEqualTo("state", selectedState)
            .whereEqualTo("country", selectedCountry)
            .get()
            .addOnSuccessListener { query ->
                for (document in query) {
                    val dealerName = document.getString("full_name")
                    if (dealerName != null) {
                        dealerListAdapter.add(dealerName)
                    } else {
                        dealerListAdapter.add("No Dealer Available")
                    }
                }
            }
    }

    override fun onBackPressed() {

        intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
        super.onBackPressed()
    }


    private fun addToDatabase(name: String, email: String, contactNumber: String, selectedDealer: String, selectedCountry: String, selectedState: String, selectedCity: String) {
        val retailerInfo = hashMapOf(
            "full_name" to name,
            "country" to selectedCountry,
            "state" to selectedState,
            "city" to selectedCity,
            "email_address" to email,
            "contact_number" to contactNumber,
            "role" to "retailer",
            "Dealer" to selectedDealer
        )

        val retailerDb = Firebase.firestore
        retailerDb.collection("Retailer_database").add(retailerInfo)
            .addOnSuccessListener { docref ->
                Log.d("TAG", "User added with ${docref.id}")
                Toast.makeText(this, "Account Created and data added to database", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, RetailerHomeActivity::class.java)
                startActivity(intent)
                finish()
            }.addOnFailureListener { e ->
                Log.e("Database", "Database addition failed: $e")
                Toast.makeText(this, "Database addition failed", Toast.LENGTH_SHORT).show()
            }
    }

    private fun setupCountrySpinner() {
        val spinner = binding.spinnerCountry
        val countryListAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mutableListOf())
        countryListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = countryListAdapter

        getCountriesList { countryList ->
            countryListAdapter.clear()
            countryListAdapter.addAll(countryList)
        }
    }

    private fun setupStateSpinner() {
        val spinner = binding.stateSpinner
        val stateListAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mutableListOf())
        stateListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = stateListAdapter

        getStatesList { statesList ->
            stateListAdapter.clear()
            stateListAdapter.addAll(statesList)
        }
    }

    private fun setupCitySpinner() {
        val spinner = binding.citySpinner
        val citiesListAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mutableListOf())
        citiesListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = citiesListAdapter

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