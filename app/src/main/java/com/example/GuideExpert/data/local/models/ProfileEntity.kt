package com.example.GuideExpert.data.local.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.example.GuideExpert.domain.models.Avatar
import java.util.Date

@Entity
data class ProfileEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo val login: String,
    @ColumnInfo val realName: String,
    @ColumnInfo val firstName: String,
    @ColumnInfo val lastName: String,
    @ColumnInfo val sex: String?,
    @ColumnInfo val email: String,
    @ColumnInfo val birthday: Date,
    @ColumnInfo val phone: String,
    @Ignore val avatar: Avatar?
){
    constructor(id: Int, login: String, realName: String,firstName:String,lastName: String,
                sex: String,email: String,birthday: Date,phone: String) : this(id, login, realName,firstName, lastName,sex,email,birthday,phone,null)
}
