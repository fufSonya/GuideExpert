package com.example.GuideExpert.data.mappers

import com.example.GuideExpert.data.remote.pojo.BannerPOJO
import com.example.GuideExpert.data.remote.pojo.ConfigPOJO
import com.example.GuideExpert.domain.models.Banner
import com.example.GuideExpert.domain.models.Config

fun ConfigPOJO.toConfig() = Config(banners.map { it.toBanner() })
fun BannerPOJO.toBanner() = Banner(id, photo)
