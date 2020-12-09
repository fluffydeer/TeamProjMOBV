package com.example.teamprojmobv.data.db.model

//class MediaItem (
//                 var media_url: String? = null,
//){
//
//}
data class MediaItem (
    val postid: Int,
    val created: String,
    var videourl:String,
    val username: String,
    val profile: String
){

}