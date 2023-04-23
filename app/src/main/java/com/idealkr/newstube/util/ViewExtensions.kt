package com.idealkr.newstube.util

import android.view.View
import com.google.android.material.snackbar.Snackbar

fun View.showSnackBar(
    message: String,
    actionText: String? = null,
    onActionListener: View.OnClickListener? = null
) {
    val snackBar = Snackbar.make(this, message, Snackbar.LENGTH_SHORT)
    if (onActionListener != null) snackBar.setAction(actionText, onActionListener)

    snackBar.show()
}

