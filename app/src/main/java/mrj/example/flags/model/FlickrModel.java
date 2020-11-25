package mrj.example.flags.model;

import android.graphics.Bitmap;

/**
 * Created by JavohirAI
 */

public class FlickrModel {

    private String name;
    private String link;
    private Bitmap mBitmap;

    public FlickrModel(String name) {
        this.name = name;
    }

    public FlickrModel(String name, String link, Bitmap bitmap) {
        this.name = name;
        mBitmap = bitmap;
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
