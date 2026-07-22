package com.sys.androidkit.core.ui.ext

import android.view.View
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

fun View.showSnackbar(
    message: CharSequence,
    length: Int = Snackbar.LENGTH_SHORT,
    actionLabel: CharSequence? = null,
    action: (() -> Unit)? = null,
): Snackbar {
    return Snackbar.make(this, message, length).also { bar ->
        if (!actionLabel.isNullOrEmpty() && action != null) {
            bar.setAction(actionLabel) { action() }
        }
        bar.show()
    }
}

fun View.showSnackbar(
    @StringRes messageRes: Int,
    length: Int = Snackbar.LENGTH_SHORT,
    actionLabel: CharSequence? = null,
    action: (() -> Unit)? = null,
): Snackbar = showSnackbar(context.getString(messageRes), length, actionLabel, action)

fun Fragment.showSnackbar(
    message: CharSequence,
    length: Int = Snackbar.LENGTH_SHORT,
    actionLabel: CharSequence? = null,
    action: (() -> Unit)? = null,
): Snackbar = requireView().showSnackbar(message, length, actionLabel, action)

fun Fragment.showSnackbar(
    @StringRes messageRes: Int,
    length: Int = Snackbar.LENGTH_SHORT,
    actionLabel: CharSequence? = null,
    action: (() -> Unit)? = null,
): Snackbar = requireView().showSnackbar(messageRes, length, actionLabel, action)
