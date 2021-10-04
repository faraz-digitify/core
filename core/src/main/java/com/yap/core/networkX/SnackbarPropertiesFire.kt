package com.yap.core.networkX

import com.google.android.material.snackbar.BaseTransientBottomBar

class SnackbarPropertiesFire(
    var connectionCallback: NetworkXCallback?,
    @BaseTransientBottomBar.Duration var duration: Int,
    var noInternetConnectionMessage: String,
    var onAirplaneModeMessage: String,
    var snackbarActionText: String,
    var showActionToDismiss: Boolean,
    var snackbarDismissActionText: String
)