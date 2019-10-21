package org.sana.simpleapp.di

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

import javax.inject.Qualifier

/**
 * Created by mehdi on 19/10/2019.
 */

@Qualifier
@Retention(RetentionPolicy.RUNTIME)
annotation class AppContext