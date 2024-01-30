package com.exe.fitlifetaskmanager;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ItemViewModel extends AndroidViewModel {
    private final ItemsRepository itemsRepository;
    private final LiveData<List<ItemToBuy>> items;

    public ItemViewModel(@NonNull Application application) {
        super(application);
        itemsRepository = new ItemsRepository(application);
        items = itemsRepository.findAllItems();
    }

    public LiveData<List<ItemToBuy>> findAll() {
        return items;
    }

    public void insert(ItemToBuy itemToBuy) {
        itemsRepository.insert(itemToBuy);
    }

    public void update(ItemToBuy itemToBuy) {
        itemsRepository.update(itemToBuy);
    }

    public void delete(ItemToBuy itemToBuy) {
        itemsRepository.delete(itemToBuy);
    }
}


