package com.chilik1020.resourcekeeper.model.telegrambot.types;

public class UserProfilePhotos {
    int total_count;
    PhotoSize[][] photos;

    public UserProfilePhotos(int total_count, PhotoSize[][] photos) {
        this.total_count = total_count;
        this.photos = photos;
    }
//
//
// @Override
// public String toString(){
//  StringBuilder strb = new StringBuilder();
//  strb.append("[[" + photos[0][0]);
//  for(int j = 1; j < photos[0].length - 1; j++){
//   strb.append(", " + photos[0][j]);
//  }
//  strb.append("]");
//  for(int i = 1; i < photos.length - 1; i++){
//   strb.append(", [" + photos[i][0]);
//   for(int j = 1; j < photos[i].length - 1; j++){
//    strb.append(", " + photos[i][j]);
//   }
//   strb.append("]");
//  }
//  strb.append("]");
//  String po = strb.toString();
//
//  return "{\"total_count\": " + total_count + ", \"photos\": " + po + "}";
// }
}
