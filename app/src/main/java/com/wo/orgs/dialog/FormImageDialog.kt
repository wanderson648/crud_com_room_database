package com.wo.orgs.dialog

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.wo.orgs.R
import com.wo.orgs.databinding.FormImageUrlBinding
import com.wo.orgs.extensions.tryRefreshImage

class FormImageDialog(private val context: Context) {

    fun showDialog(
        urlDefault: String? = null,
        whenRefreshImage: (image: String) -> Unit
    ) {
        FormImageUrlBinding.inflate(
            LayoutInflater.from(context)
        ).apply {

            urlDefault?.let {
                imgForm.tryRefreshImage(it)
                editUrlRefresh.setText(it)
            }

            btnRefresh.setOnClickListener {
                val url = editUrlRefresh.text.toString()
                imgForm.tryRefreshImage(url)
            }

            AlertDialog.Builder(context)
                .setView(root)
                .setPositiveButton(context.getString(R.string.confirm)) { _, _ ->
                    val url = editUrlRefresh.text.toString()
                    whenRefreshImage(url)
                }
                .setNegativeButton(context.getString(R.string.cancel)) { _, _ -> }
                .create()
                .show()
        }

    }
}