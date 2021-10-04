package com.yap.base.biometric

import androidx.databinding.BaseObservable
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import com.yap.base.biometric.interfaces.IBase
import com.yap.base.sealed.UIEvent

abstract class BaseState : BaseObservable(), IBase.State {
    override var toolbarTitle: MutableLiveData<String> = MutableLiveData("")
    override var toolsBarVisibility: ObservableBoolean = ObservableBoolean()
    override var uiEvent: MutableLiveData<UIEvent> = MutableLiveData()
}