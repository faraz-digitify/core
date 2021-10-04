package com.yap.base.core.interfaces

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import com.yap.base.core.SingleClickEvent
import com.yap.base.sealed.UIEvent

interface IBase {
    interface View<V : ViewModel<*>> {
        val viewModel: V
    }

    interface ViewModel<S : State> :
        ILifecycle {
        val viewState: S
        val clickEvent: SingleClickEvent
        fun showLoading(onBackGround: Boolean = false)
        fun hideLoading(onBackGround: Boolean = false)
        fun showToast(message: String, onBackGround: Boolean)
        fun showToast(message: String)
        fun showAlertMessage(message: String)
        fun showAlertMessage(message: String, onBackGround: Boolean)
    }

    interface State {
        var toolbarTitle: MutableLiveData<String>
        var toolsBarVisibility: ObservableBoolean
        var uiEvent: MutableLiveData<UIEvent>
    }
}