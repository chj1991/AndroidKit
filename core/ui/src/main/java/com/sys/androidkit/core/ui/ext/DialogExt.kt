package com.sys.androidkit.core.ui.ext

import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder

fun Fragment.showConfirmDialog(
    title: CharSequence,
    message: CharSequence,
    positive: CharSequence,
    negative: CharSequence = getString(android.R.string.cancel),
    onPositive: () -> Unit,
) {
    MaterialAlertDialogBuilder(requireContext())
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(positive) { _, _ -> onPositive() }
        .setNegativeButton(negative, null)
        .show()
}

fun Fragment.showConfirmDialog(
    @StringRes titleRes: Int,
    @StringRes messageRes: Int,
    @StringRes positiveRes: Int,
    @StringRes negativeRes: Int = android.R.string.cancel,
    onPositive: () -> Unit,
) {
    showConfirmDialog(
        title = getString(titleRes),
        message = getString(messageRes),
        positive = getString(positiveRes),
        negative = getString(negativeRes),
        onPositive = onPositive,
    )
}
