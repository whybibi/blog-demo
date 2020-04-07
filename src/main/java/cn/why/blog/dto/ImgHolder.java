package cn.why.blog.dto;

import lombok.Data;

import java.io.InputStream;

@Data
public class ImgHolder {

    private String imgName;
    private InputStream img;

    public ImgHolder(String imgName, InputStream img) {
        this.imgName = imgName;
        this.img = img;
    }
}
