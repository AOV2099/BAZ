package com.aov2099.baz.fragments

import android.app.Activity.RESULT_OK
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.aov2099.baz.Network
import com.aov2099.baz.databinding.FragmentImageBinding
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.*


class ImageFragment : Fragment() {

    private var _binding: FragmentImageBinding? = null
    private val binding get() = _binding!!
    private var imgUrl: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentImageBinding.inflate(inflater, container, false)

        binding.btnSelectImg.setOnClickListener {
            selectImage()
        }

        binding.btnUploadImg.setOnClickListener {

            if (Network.conExists( requireActivity() )) {
                uploadImage()
            } else {
                Toast.makeText(activity, "Error de conexión", Toast.LENGTH_LONG).show()
            }

        }

        return binding.root
    }

    private fun uploadImage() {

        val progress = ProgressDialog(activity)
        progress.setMessage("Subiendo Imagen a Firebase Storage...")
        progress.setCancelable(false)
        progress.show()

        val imgDateFormatter = SimpleDateFormat("dd_MM_yyyy_HH_mm_ss", Locale.getDefault())
        val now = Date()
        val imgName = imgDateFormatter.format(now)
        val firebaseStorage = FirebaseStorage.getInstance().getReference("appImages/${imgName}")

        firebaseStorage.putFile(imgUrl!!).addOnSuccessListener {

            if (progress.isShowing) {
                progress.dismiss()
            }
            Toast.makeText(
                activity,
                "La imagen se ha subido correctamente a Firebase :D",
                Toast.LENGTH_LONG
            ).show()

        }.addOnFailureListener {

            if (progress.isShowing) {
                progress.dismiss()
            }
            Toast.makeText(activity, "La subida del archivo a FRACASADO", Toast.LENGTH_LONG).show()
        }

    }

    private fun selectImage() {

        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT

        startActivityForResult(intent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && resultCode == RESULT_OK) {

            imgUrl = data?.data!!
            binding.ivImage.setImageURI(imgUrl)

        }
    }




}