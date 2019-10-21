package org.sana.simpleapp.di.module


import org.sana.simpleapp.viewmodel.FeedViewModel
import org.sana.simpleapp.viewmodel.UserViewModel
import org.sana.simpleapp.viewmodel.ViewModelFactory
import org.sana.simpleapp.di.ViewModelKey

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by mehdi on 19/10/2019.
 */


@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(FeedViewModel::class)
    abstract fun feedViewModel(feedViewModel: FeedViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(UserViewModel::class)
    abstract fun userViewModel(userViewModel: UserViewModel): ViewModel

}