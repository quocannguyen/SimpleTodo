package com.example.simpletodo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import org.apache.commons.io.FileUtils;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private final int requestEditItem = 2;

    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvItems = findViewById(R.id.lvItems);

        findViewById(R.id.btnAddItem).setOnClickListener(lambda -> onAddItem());

        readItems();
        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);

        setupListViewListener();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == requestEditItem && resultCode == RESULT_OK && data != null) {
            String itemBody = data.getExtras().getString("item_body");
            int position = data.getExtras().getInt("position");

            items.set(position, itemBody);
            itemsAdapter.notifyDataSetChanged();
            writeItems();
            Toast.makeText(this, "Item edited", Toast.LENGTH_LONG).show();
        }
    }

    private void onAddItem() {
        EditText etNewItem = findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString().trim();

        if (!itemText.isEmpty()) {
            items.add(itemText);
            itemsAdapter.notifyDataSetChanged();
            etNewItem.setText("");
            writeItems();
            Toast.makeText(this, "Item added", Toast.LENGTH_LONG).show();
        } else Toast.makeText(this, "Item text is empty", Toast.LENGTH_LONG).show();
    }

    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(
                // Attach a LongClickListener to each Item for ListView
                (adapterView, view, position, id) -> {
                    // removes the item
                    items.remove(position);
                    // refreshes the adapter
                    itemsAdapter.notifyDataSetChanged();

                    writeItems();
                    return true;
                }
        );

        lvItems.setOnItemClickListener(
                (adapterView, view, position, id) -> {
                    Intent intent = new Intent(MainActivity.this, EditItemActivity.class);
                    intent.putExtra("item_body", items.get(position));
                    intent.putExtra("position", position);

                    startActivityForResult(intent, requestEditItem);
                }
        );
    }

    private void readItems() {
        File fileDir = getFilesDir();
        File todoFile = new File(fileDir, "todo.txt");
        try {
            items = new ArrayList<>(FileUtils.readLines(todoFile));
        } catch (IOException e) {
            items = new ArrayList<>();
        }
    }

    private void writeItems() {
        File fileDir = getFilesDir();
        File todoFile = new File(fileDir, "todo.txt");
        try {
            FileUtils.writeLines(todoFile, items);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}