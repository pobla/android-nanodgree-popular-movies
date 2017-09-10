package com.android.pobla.popularmovies.data.model;


import com.google.gson.annotations.SerializedName;

public class MovieVideos {

  @SerializedName("id")
  public String id;
  @SerializedName("iso_639_1")
  public String iso6391;
  @SerializedName("iso_3166_1")
  public String iso31661;
  @SerializedName("key")
  public String key;
  @SerializedName("name")
  public String name;
  @SerializedName("site")
  public String site;
  @SerializedName("size")
  public int size;
  @SerializedName("type")
  public String type;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getIso6391() {
    return iso6391;
  }

  public void setIso6391(String iso6391) {
    this.iso6391 = iso6391;
  }

  public String getIso31661() {
    return iso31661;
  }

  public void setIso31661(String iso31661) {
    this.iso31661 = iso31661;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getSite() {
    return site;
  }

  public void setSite(String site) {
    this.site = site;
  }

  public int getSize() {
    return size;
  }

  public void setSize(int size) {
    this.size = size;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  @Override
  public String toString() {
    return "MovieVideos{" +
             "id='" + id + '\'' +
             ", iso6391='" + iso6391 + '\'' +
             ", iso31661='" + iso31661 + '\'' +
             ", key='" + key + '\'' +
             ", name='" + name + '\'' +
             ", site='" + site + '\'' +
             ", size=" + size +
             ", type='" + type + '\'' +
             '}';
  }
}
