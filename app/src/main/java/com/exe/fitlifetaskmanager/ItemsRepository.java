package com.exe.fitlifetaskmanager;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.exe.fitlifetaskmanager.dao.ItemDao;

import java.util.List;

public class ItemsRepository {
    private final ItemDao itemDao;

    private final LiveData<List<ItemToBuy>> items;


    ItemsRepository(Application application) {
        AppDatabase database = AppDatabase.getDatabase(application);
        itemDao = database.itemDao();
        items = itemDao.findAll();
    }

    LiveData<List<ItemToBuy>> findAllItems() {
        return items;
    }
    public List<ItemToBuy> findItemWithName(String itemName) {
        return itemDao.findItemsWithName(itemName);
    }

    void insert(ItemToBuy item) {
        AppDatabase.databaseWriteExecutor.execute(() -> itemDao.insert(item));
    }

    void update(ItemToBuy item) {
        AppDatabase.databaseWriteExecutor.execute(() -> itemDao.update(item));
    }

    void delete(ItemToBuy item) {
        AppDatabase.databaseWriteExecutor.execute(() -> itemDao.delete(item));
    }
}



