package com.example.GuideExpert.data.mappers

import com.example.GuideExpert.data.local.models.AvatarEntity
import com.example.GuideExpert.data.local.models.ProfileEntity
import com.example.GuideExpert.data.local.models.ProfileWithAvatar
import com.example.GuideExpert.data.remote.pojo.AvatarPOJO
import com.example.GuideExpert.data.remote.pojo.ProfilePOJO
import com.example.GuideExpert.domain.models.Avatar
import com.example.GuideExpert.domain.models.Profile
import java.util.Date

fun ProfileEntity.toProfile() = Profile(
    id = id,login=login,realName=realName,firstName=firstName,lastName=lastName,sex=sex,email=email,birthday=birthday,phone=phone,avatar=avatar
)

fun ProfilePOJO.toProfile():Profile {
    var _birthday = birthday
    if (birthday.time < -2202202619) _birthday = Date(6786181)
   return Profile(
        id = id,
        login = login,
        realName = realName,
        firstName = firstName,
        lastName = lastName,
        sex = sex,
        email = email,
        birthday = _birthday,
        phone = phone,
        avatar = avatar?.toAvatar()
    )
}

fun Profile.toProfileEntity() = ProfileEntity(
    id = id,login=login,realName=realName,firstName=firstName,lastName=lastName,sex=sex,email=email,birthday=birthday,phone=phone,avatar=avatar
)

fun Profile.toProfileWithAvatar():ProfileWithAvatar {
    return ProfileWithAvatar(profile = this.toProfileEntity(), avatar = (if (this.avatar != null)  this.avatar.toAvatarEntity() else null))
}

fun AvatarPOJO.toAvatar() =  Avatar(id = id, profileId = profileId, url = url)

fun Avatar.toAvatarEntity() =  AvatarEntity(id = id!!, profileId = profileId, url = url)

fun ProfileWithAvatar.toProfile() = Profile(
    id = profile.id, login=profile.login,realName=profile.realName,firstName=profile.firstName,
    lastName=profile.lastName,sex=profile.sex,email=profile.email,birthday=profile.birthday,phone=profile.phone,
    avatar = avatar?.toAvatar()
)

fun AvatarEntity.toAvatar() =  Avatar(id = id, profileId = profileId, url = url)

