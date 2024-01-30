package com.exe.fitlifetaskmanager;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "item_to_buy")
public class ItemToBuy {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String itemName;
    private String itemQuantity;
    private String unit;
    public ItemToBuy (String itemName, String itemQuantity, String unit){
        this.itemName = itemName;
        this.itemQuantity = itemQuantity;
        this.unit = unit;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(String itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}

