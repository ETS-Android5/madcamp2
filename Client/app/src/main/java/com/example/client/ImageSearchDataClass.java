package com.example.client;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ImageSearchDataClass {

    private List<Image> documents;

    public List<Image> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Image> documents) {
        this.documents = documents;
    }

    class Image
    {
        private String thumbnail_url;
        private String image_url;
        private String doc_url;

        public String getThumbnail_url() {
            return thumbnail_url;
        }

        public void setThumbnail_url(String thumbnail_url) {
            this.thumbnail_url = thumbnail_url;
        }

        public String getImage_url() {
            return image_url;
        }

        public void setImage_url(String image_url) {
            this.image_url = image_url;
        }

        public String getDoc_url() {
            return doc_url;
        }

        public void setDoc_url(String doc_url) {
            this.doc_url = doc_url;
        }
    }

}
