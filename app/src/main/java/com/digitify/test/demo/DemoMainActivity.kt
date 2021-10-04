package com.digitify.test.demo

import androidx.activity.viewModels
import com.digitify.test.BR
import com.digitify.test.databinding.DemoActivityMainBinding
import com.digitify.test.R
import com.yap.core.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DemoMainActivity : BaseActivity<DemoActivityMainBinding, IMain.State, IMain.ViewModel>(),
    IMain.View {
    override fun getBindingVariable(): Int =  BR.viewModel
    override val viewModel: IMain.ViewModel by viewModels<MainViewModel>()
    override fun getLayoutId(): Int  = R.layout.demo_activity_main

}