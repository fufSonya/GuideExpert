package com.example.GuideExpert.utils

import android.util.Patterns
import java.util.Date

fun CharSequence?.isValidEmail() = !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun Long?.isValidBirthday() =  this !== null && this < Date().time
