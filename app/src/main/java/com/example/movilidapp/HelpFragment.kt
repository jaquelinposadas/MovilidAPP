package com.example.movilidapp

import android.app.AlertDialog
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class HelpFragment : Fragment() {

    private lateinit var tvVersionDesc: TextView
    private lateinit var tvDonation20: TextView
    private lateinit var tvDonation50: TextView
    private lateinit var tvDonationOther: TextView
    
    private var currentDonation = 50.0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_help, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        val rlSupport = view.findViewById<View>(R.id.rl_support)
        tvVersionDesc = view.findViewById(R.id.tv_version_desc)
        val rlComments = view.findViewById<View>(R.id.rl_comments)
        
        tvDonation20 = view.findViewById(R.id.tv_donation_20)
        tvDonation50 = view.findViewById(R.id.tv_donation_50)
        tvDonationOther = view.findViewById(R.id.tv_donation_other)
        val btnSupport = view.findViewById<View>(R.id.btn_support_development)
        
        // Dynamically get the version
        try {
            val pInfo = requireContext().packageManager.getPackageInfo(requireContext().packageName, 0)
            val version = pInfo.versionName
            tvVersionDesc.text = "$version - DEMO"
        } catch (e: Exception) {
            tvVersionDesc.text = "1.0.0 - DEMO"
        }
        
        // Chat function navigation
        rlSupport.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_help_to_chatFragment)
        }
        
        // Comments dialog
        rlComments.setOnClickListener {
            val editText = EditText(requireContext()).apply {
                hint = "Escribe tu mensaje aquí..."
                filters = arrayOf(android.text.InputFilter.LengthFilter(500)) // Max 500 chars
                gravity = Gravity.TOP or Gravity.START
                minLines = 3
            }
            val layout = LinearLayout(requireContext()).apply {
                setPadding(50, 40, 50, 10)
                addView(editText, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            }
            AlertDialog.Builder(requireContext())
                .setTitle("Comentarios")
                .setMessage("Tu opinión nos ayuda a mejorar. (Máx 500 caracteres)")
                .setView(layout)
                .setPositiveButton("Enviar") { _, _ ->
                    val comment = editText.text.toString()
                    if (comment.isNotEmpty()) {
                        Toast.makeText(requireContext(), "Comentario enviado exitosamente", Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }
        
        // Donation selection logic
        tvDonation20.setOnClickListener {
            currentDonation = 20.0
            updateDonationUI()
        }
        
        tvDonation50.setOnClickListener {
            currentDonation = 50.0
            updateDonationUI()
        }
        
        tvDonationOther.setOnClickListener {
            val editText = EditText(requireContext()).apply {
                hint = "Ej. 100.00"
                inputType = android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL
            }
            val layout = LinearLayout(requireContext()).apply {
                setPadding(50, 40, 50, 10)
                addView(editText, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            }
            AlertDialog.Builder(requireContext())
                .setTitle("Monto personalizado")
                .setMessage("Ingresa el monto de tu donativo (mínimo \$0.01 MXN):")
                .setView(layout)
                .setPositiveButton("Aceptar") { _, _ ->
                    val input = editText.text.toString().toDoubleOrNull()
                    if (input != null && input >= 0.01) {
                        currentDonation = input
                        updateDonationUI()
                    } else {
                        Toast.makeText(requireContext(), "Por favor ingresa un monto válido", Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }
        
        // Action to donate
        btnSupport.setOnClickListener {
            Toast.makeText(requireContext(), "Gracias por tu donativo de $$currentDonation MXN", Toast.LENGTH_SHORT).show()
        }
        
        // Init state
        updateDonationUI()
    }
    
    private fun updateDonationUI() {
        val colorGray = android.graphics.Color.parseColor("#1A2B4C")
        val colorBlue = android.graphics.Color.parseColor("#4A75E5")
        
        tvDonation20.setBackgroundResource(R.drawable.bg_button_outline_gray)
        tvDonation20.setTextColor(colorGray)
        
        tvDonation50.setBackgroundResource(R.drawable.bg_button_outline_gray)
        tvDonation50.setTextColor(colorGray)
        
        tvDonationOther.setBackgroundResource(R.drawable.bg_button_outline_gray)
        tvDonationOther.setTextColor(colorGray)
        
        when (currentDonation) {
            20.0 -> {
                tvDonation20.setBackgroundResource(R.drawable.bg_button_outline_blue)
                tvDonation20.setTextColor(colorBlue)
                tvDonationOther.text = "Otros"
            }
            50.0 -> {
                tvDonation50.setBackgroundResource(R.drawable.bg_button_outline_blue)
                tvDonation50.setTextColor(colorBlue)
                tvDonationOther.text = "Otros"
            }
            else -> {
                tvDonationOther.setBackgroundResource(R.drawable.bg_button_outline_blue)
                tvDonationOther.setTextColor(colorBlue)
                // Remove decimals if it's a round number to look cleaner
                val displayAmount = if (currentDonation % 1.0 == 0.0) {
                    currentDonation.toInt().toString()
                } else {
                    currentDonation.toString()
                }
                tvDonationOther.text = "$$displayAmount MXN"
            }
        }
    }
}