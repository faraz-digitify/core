package com.digitify.test.demo

import com.yap.base.biometric.interfaces.IBase


interface IMain {
    interface View : IBase.View<ViewModel>
    interface ViewModel : IBase.ViewModel<State>
    interface State : IBase.State
}