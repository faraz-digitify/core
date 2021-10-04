package com.yap.base.core.interfaces

import android.view.View

interface OnItemClickListener {
    fun onItemClick(view: View, data: Any, pos: Int)
}