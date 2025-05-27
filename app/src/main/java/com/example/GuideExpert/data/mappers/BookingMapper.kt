package com.example.GuideExpert.data.mappers

import com.example.GuideExpert.domain.models.MessageResponse
import com.example.GuideExpert.data.remote.pojo.BookingExcursionPOJO

fun BookingExcursionPOJO.toMessageResponse() = MessageResponse(success=success,message=message)
