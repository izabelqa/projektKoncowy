package com.exe.fitlifetaskmanager.shoppinglist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.exe.fitlifetaskmanager.ItemViewModel;
import com.exe.fitlifetaskmanager.R;

import java.util.Arrays;
import java.util.List;

public class EditListActivity extends AppCompatActivity {

    public static final String EXTRA_EDIT_NAME = "ITEM_NAME";
    public static final String EXTRA_EDIT_QUANTITY = "ITEM_QUANTITY";
    public static final String EXTRA_EDIT_UNIT = "ITEM_UNIT";

    private EditText itemNameEditText;
    private EditText itemQuantityEditText;
    private Spinner unitSpinner;
    private ItemViewModel itemViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_list);

        itemNameEditText = findViewById(R.id.editTextItemName);
        itemQuantityEditText = findViewById(R.id.editTextItemQuantity);
        unitSpinner = findViewById(R.id.spinnerUnit);

        if (getIntent().hasExtra(EXTRA_EDIT_NAME)) {
            itemNameEditText.setText(getIntent().getStringExtra(EXTRA_EDIT_NAME));
        }
        if(getIntent().hasExtra(EXTRA_EDIT_QUANTITY)) {
            itemQuantityEditText.setText(getIntent().getStringExtra(EXTRA_EDIT_QUANTITY));
        }

        List<String> unitOptions = Arrays.asList(getResources().getStringArray(R.array.unit_options));

        // Inicjalizacja adaptera dla Spinnera
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                unitOptions
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unitSpinner.setAdapter(adapter);

        // Ustawienie wybranej jednostki, jeśli została przekazana
        if (getIntent().hasExtra(EXTRA_EDIT_UNIT)) {
            String selectedUnit = getIntent().getStringExtra(EXTRA_EDIT_UNIT);
            int position = adapter.getPosition(selectedUnit);
            unitSpinner.setSelection(position);
        }

        final Button button = findViewById(R.id.buttonSave);
        button.setOnClickListener(view -> {
            Intent replyIntent = new Intent();
            if (TextUtils.isEmpty(itemNameEditText.getText()) || TextUtils.isEmpty(itemQuantityEditText.getText())) {
                setResult(RESULT_CANCELED, replyIntent);
            } else {
                String itemName = itemNameEditText.getText().toString();
                replyIntent.putExtra(EXTRA_EDIT_NAME, itemName);
                String itemQuantity = itemQuantityEditText.getText().toString();
                replyIntent.putExtra(EXTRA_EDIT_QUANTITY, itemQuantity);
                String selectedUnit = unitSpinner.getSelectedItem().toString();
                replyIntent.putExtra(EXTRA_EDIT_UNIT, selectedUnit);
                setResult(RESULT_OK, replyIntent);
            }
            finish();
        });

    }
}
