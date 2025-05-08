package com.example.GuideExpert.data.di

import javax.inject.Qualifier


@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Auth

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class NoAuth