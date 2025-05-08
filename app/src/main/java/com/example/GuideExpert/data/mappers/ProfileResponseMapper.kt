package com.example.GuideExpert.data.mappers

import com.example.GuideExpert.data.remote.pojo.RemoveAvatarProfileResponsePOJO
import com.example.GuideExpert.data.remote.pojo.UpdateAvatarProfileResponsePOJO
import com.example.GuideExpert.data.remote.pojo.UpdateProfileResponsePOJO
import com.example.GuideExpert.domain.models.Avatar
import com.example.GuideExpert.domain.models.MessageResponse

fun UpdateAvatarProfileResponsePOJO.toAvatar(profileId:Int) = Avatar(id=idAvatar!!, profileId = profileId, url = imageUrl!!)

fun RemoveAvatarProfileResponsePOJO.toRemoveAvatarProfileResponse() = MessageResponse(success=success,message=message)

fun UpdateProfileResponsePOJO.toUpdateProfileResponse() = MessageResponse(success=success,message=message)
