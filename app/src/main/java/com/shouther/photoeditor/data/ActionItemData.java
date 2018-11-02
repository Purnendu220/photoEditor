package com.shouther.photoeditor.data;

public class ActionItemData {
    String itemName;
    int itemResourceId;
    int imageResourceId;

    public ActionItemData(String itemName, int itemResourceId, int imageResourceId) {
        this.itemName = itemName;
        this.itemResourceId = itemResourceId;
        this.imageResourceId = imageResourceId;

    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getItemResourceId() {
        return itemResourceId;
    }

    public void setItemResourceId(int itemResourceId) {
        this.itemResourceId = itemResourceId;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public void setImageResourceId(int imageResourceId) {
        this.imageResourceId = imageResourceId;
    }
}
