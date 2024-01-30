package com.exe.fitlifetaskmanager.shoppinglist;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.exe.fitlifetaskmanager.ItemToBuy;
import com.exe.fitlifetaskmanager.ItemViewModel;
import com.exe.fitlifetaskmanager.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.Arrays;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    private ItemViewModel itemViewModel;
    public static final int NEW_ITEM_REQUEST_CODE = 1;
    public static final int EDIT_ITEM_REQUEST_CODE = 2;
    private ItemToBuy editedItem= null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final ItemAdapter adapter = new ItemAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        itemViewModel = new ViewModelProvider(this).get(ItemViewModel.class);
        itemViewModel.findAll().observe(this, new Observer<List<ItemToBuy>>(){

            @Override
            public void onChanged(@Nullable final List<ItemToBuy> items) {
                Log.d("ListActivity", "Observed items: " + items);
                adapter.setItems(items);
            }
        });
        FloatingActionButton addBookButton = findViewById(R.id.add_button);
        addBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListActivity.this, EditListActivity.class);
                startActivityForResult(intent,NEW_ITEM_REQUEST_CODE);
            }
        });

    }

    private class ItemsHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private TextView itemName;
        private TextView itemQuantity;
        private Spinner spinnerUnit;
        private ItemToBuy itemToBuy;
        private List<String> unitOptions;


        public ItemsHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item, parent, false));
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

            itemName = itemView.findViewById(R.id.item_name);
            itemQuantity = itemView.findViewById(R.id.item_quantity);
            spinnerUnit = itemView.findViewById(R.id.spinner_unit);

            unitOptions = Arrays.asList(itemView.getResources().getStringArray(R.array.unit_options));
            ArrayAdapter<String> unitAdapter = new ArrayAdapter<>(itemView.getContext(), android.R.layout.simple_spinner_item, unitOptions);
            unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerUnit.setAdapter(unitAdapter);
        }

        public void bind(final ItemToBuy itemToBuy) {
            if (itemToBuy != null) {
                this.itemToBuy = itemToBuy;
                itemName.setText(itemToBuy.getItemName());
                itemQuantity.setText(itemToBuy.getItemQuantity());

                // Ustaw pozycję tylko jeśli jednostka jest nie-null
                if (itemToBuy.getUnit() != null) {
                    int position = unitOptions.indexOf(itemToBuy.getUnit());
                    spinnerUnit.setSelection(position);
                }
            } else {
                Log.e("ItemsHolder", "Received null itemToBuy");
            }
        }
        @Override
        public void onClick(View v) {
            ListActivity.this.editedItem = this.itemToBuy;
            Intent intent = new Intent(ListActivity.this, EditListActivity.class);
            intent.putExtra(EditListActivity.EXTRA_EDIT_NAME, itemToBuy.getItemName());
            intent.putExtra(EditListActivity.EXTRA_EDIT_QUANTITY, itemToBuy.getItemQuantity());
            startActivityForResult(intent, EDIT_ITEM_REQUEST_CODE);
        }

        @Override
        public boolean onLongClick(View v) {
            showDeleteConfirmationDialog();
            return true;
        }

        private void showDeleteConfirmationDialog() {
            AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
            builder.setTitle("Potwierdź usunięcie");
            builder.setMessage("Czy na pewno chcesz usunąć ten element?");

            builder.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Usuń element po potwierdzeniu
                    ListActivity.this.itemViewModel.delete(ItemsHolder.this.itemToBuy);
                }
            });

            builder.setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss(); // Zamknij okno dialogowe bez usuwania
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }

    }
    private class ItemAdapter extends RecyclerView.Adapter<ItemsHolder>{
        private List<ItemToBuy> items;

        @NonNull
        @Override
        public ItemsHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType){
            return new ItemsHolder(getLayoutInflater(), parent);
        }

        @Override
        public void onBindViewHolder(@NonNull ItemsHolder holder, int position){
            if(items != null){
                ItemToBuy itemToBuy = items.get(position);
                holder.bind(itemToBuy);
            }else{
                Log.d("ListActivity", "No items");
            }
        }

        @Override
        public int getItemCount() {
            if(items !=null){
                return items.size();
            }else {
                return 0;
            }
        }

        void setItems(List<ItemToBuy> items){
            this.items = items;
            notifyDataSetChanged();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_ITEM_REQUEST_CODE && resultCode == RESULT_OK) {
            // Dodaj nowy produkt
            ItemToBuy itemToBuy = new ItemToBuy(data.getStringExtra(EditListActivity.EXTRA_EDIT_NAME),
                    data.getStringExtra(EditListActivity.EXTRA_EDIT_QUANTITY),
                    data.getStringExtra(EditListActivity.EXTRA_EDIT_UNIT));
            itemViewModel.insert(itemToBuy);
            Snackbar.make(findViewById(R.id.coordinator_layout),
                            getString(R.string.item_added),
                            Snackbar.LENGTH_LONG)
                    .show();
        }
        else if (requestCode == EDIT_ITEM_REQUEST_CODE && resultCode == RESULT_OK) {
            //zaktualizuj produkt
            editedItem.setItemName(data.getStringExtra(EditListActivity.EXTRA_EDIT_NAME));
            editedItem.setItemQuantity(data.getStringExtra(EditListActivity.EXTRA_EDIT_QUANTITY));
            editedItem.setUnit(data.getStringExtra(EditListActivity.EXTRA_EDIT_UNIT));
            itemViewModel.update(editedItem);

            editedItem = null;

            Snackbar.make(findViewById(R.id.coordinator_layout),
                    getString(R.string.item_updated),
                    Snackbar.LENGTH_LONG).show();
        }
        else {
            Snackbar.make(findViewById(R.id.main_layout),
                            getString(R.string.empty_not_saved),
                            Snackbar.LENGTH_LONG)
                    .show();
        }
    }

}