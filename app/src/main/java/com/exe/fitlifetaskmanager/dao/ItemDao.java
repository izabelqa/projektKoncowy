package com.exe.fitlifetaskmanager.dao;

import androidx.lifecycle.LiveData;

import java.util.List;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.exe.fitlifetaskmanager.ItemToBuy;

@Dao
public interface ItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ItemToBuy item_to_buy);

    @Update
    void update(ItemToBuy item_to_buy);

    @Delete
    void delete(ItemToBuy item_to_buy);

    @Query("DELETE FROM item_to_buy")
    void deleteAll();

    @Query("SELECT * FROM item_to_buy ORDER BY itemName")
    LiveData<List<ItemToBuy>> findAll();

    @Query("SELECT * FROM item_to_buy WHERE itemName LIKE :itemName")
    List<ItemToBuy> findItemsWithName(String itemName);
}

