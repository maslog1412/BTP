package com.example.btp.ui.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.btp.R
import com.example.btp.databinding.FragmentSearchBinding
import com.example.btp.model.Location
import com.example.btp.model.TripParameters
import com.example.btp.ui.common.adapters.BudgetAdapter
import com.example.btp.ui.common.adapters.DestinationAdapter
import com.example.btp.utils.Result
import com.example.btp.utils.formatBudget
import com.example.btp.utils.getSampleDestinations
import com.example.btp.utils.getSelectedDateRange
import com.example.btp.utils.onLoading
import com.example.btp.utils.onLoadingFailure
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker

class SearchFragment : Fragment() {

    private val viewModel: SearchViewModel by viewModels()
    private lateinit var binding: FragmentSearchBinding
    private lateinit var destinationAdapter: DestinationAdapter
    private lateinit var budgetAdapter: BudgetAdapter
    private var destination: Location? = null
    private var budget: Double = 0.0
    private var numGuests: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        setupObservers()
        setupViews()
        return binding.root
    }

    private fun setupObservers() {
        viewModel.destinationList.observe(viewLifecycleOwner) { result ->
            when (result) {
                Result.Loading -> onLoading()
                is Result.Failure -> onLoadingFailure()
                is Result.Success -> destinationAdapter.submitList(result.value)
            }
        }
        viewModel.budgetList.observe(viewLifecycleOwner) { result ->
            when (result) {
                Result.Loading -> onLoading()
                is Result.Failure -> onLoadingFailure()
                is Result.Success -> budgetAdapter.submitList(result.value)
            }
        }
    }

    private fun setupViews() {

        with(binding) {

            datePickerEditText.setOnClickListener {
                datePickerDialog()
            }

            budgetEditText.addTextChangedListener(budgetTextWatcher)

            removeGuestButton.setOnClickListener {
                if (numGuests > 0) {
                    numGuests--
                    if (numGuests == 0) {
                        guestsEditText.setText("")
                    } else {
                        if (numGuests == 1) {
                            guestsEditText.setText(getString(R.string.num_guests, numGuests))
                        } else {
                            guestsEditText.setText(
                                getString(
                                    R.string.num_guests_multiple,
                                    numGuests
                                )
                            )
                        }
                    }
                    validateFields()
                }
            }

            addGuestButton.setOnClickListener {
                numGuests++
                if (numGuests == 1) {
                    guestsEditText.setText(getString(R.string.num_guests, numGuests))
                } else {
                    guestsEditText.setText(getString(R.string.num_guests_multiple, numGuests))
                }
                validateFields()
            }

            planTripButton.setOnClickListener {
                val dates = datePickerEditText.text.toString().split(" - ")
                if (dates.size == 2) {
                    val tripParameters = TripParameters(
                        destination!!,
                        dates[0].trim(),
                        dates[1].trim(),
                        budget,
                        budget,
                        numGuests
                    )
                } else {
                    Toast.makeText(context, "Please select valid date range", Toast.LENGTH_LONG)
                        .show()
                }
            }

            editTextDestination.setOnClickListener {
                showDestinationDialog(getSampleDestinations())
            }

            destinationAdapter = DestinationAdapter { selectedDestinations ->
                val selectedDestinationNames = selectedDestinations.joinToString { it.touristSpot }
                editTextDestination.setText(selectedDestinationNames)
                destination = selectedDestinations.firstOrNull() // Update selected destination
                validateFields() // Trigger validation
            }
            popularDestinationsRecyclerView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            popularDestinationsRecyclerView.adapter = destinationAdapter

            budgetAdapter = BudgetAdapter {
                budgetEditText.setText(it.value.toString())
                Toast.makeText(
                    root.context, getString(R.string.autofill_budget), Toast.LENGTH_SHORT
                ).show()
                budget = it.value.toDouble() // Update budget
                validateFields() // Trigger validation
            }
            suggestedBudgetsRecyclerView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            suggestedBudgetsRecyclerView.adapter = budgetAdapter
        }
    }

    private fun validateFields() {
        val dates = binding.datePickerEditText.text.toString().split(" - ")
        binding.planTripButton.isEnabled =
            (numGuests > 0 && budget > 0 && dates.size == 2 && destination != null)
    }

    private fun showDestinationDialog(destinations: List<Location>) {
        val selectedItems = BooleanArray(destinations.size)
        val destinationNames = destinations.map { it.touristSpot }.toTypedArray()

        AlertDialog.Builder(requireContext(),)
            .setTitle("Select Destinations")
            .setMultiChoiceItems(
                destinationNames,
                selectedItems
            ) { dialog, which, isChecked ->
                selectedItems[which] = isChecked
            }
            .setPositiveButton("OK") { dialog, which ->
                val selectedDestinations = destinations.filterIndexed { index, _ ->
                    selectedItems[index]
                }
                val selectedDestinationNames = selectedDestinations.joinToString { it.touristSpot }
                binding.editTextDestination.setText(selectedDestinationNames)
                // Update selected destinations
                destination = selectedDestinations.firstOrNull()
                validateFields() // Trigger validation
            }
            .setNegativeButton("Cancel", null)
            .show()
    }


    private fun datePickerDialog() {
        val builder = MaterialDatePicker.Builder.dateRangePicker()
        val dateValidator = DateValidatorPointForward.from(System.currentTimeMillis())
        val constraintsBuilder = CalendarConstraints.Builder().setValidator(dateValidator)
        builder.setCalendarConstraints(constraintsBuilder.build())
        builder.setTitleText("Select a date range")
        val datePicker = builder.setTheme(R.style.AppTheme_DatePicker).build()
        datePicker.addOnPositiveButtonClickListener { selection ->
            val selectedDateRange = getSelectedDateRange(selection)
            binding.datePickerEditText.setText(selectedDateRange)
            validateFields()
        }
        datePicker.show(parentFragmentManager, "DATE_PICKER")
    }

    private val budgetTextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            val budgetEditText = binding.budgetEditText
            budgetEditText.removeTextChangedListener(this)
            val text = budgetEditText.text.toString()
            if (text.isNotEmpty()) {
                if (text == "₱") {
                    budgetEditText.setText("")
                    budget = 0.0
                } else {
                    val formattedBudget = formatBudget(text)
                    budget = formattedBudget.first.toDouble()
                    budgetEditText.setText(getString(R.string.budget_text, formattedBudget.second))
                    budgetEditText.setSelection(budgetEditText.text.length)
                }
                validateFields()
            }
            budgetEditText.addTextChangedListener(this)
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }
}