package com.example.simpletodo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

public class EditItemActivity extends AppCompatActivity {
    private EditText etEditItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        etEditItem = findViewById(R.id.etEditItem);

        findViewById(R.id.btnSave).setOnClickListener(lambda -> onSave());

        String itemBody = getIntent().getStringExtra("item_body");
        etEditItem.setText(itemBody);
        etEditItem.setSelection(itemBody.length());
        etEditItem.requestFocus();
    }

    private void onSave() {
        Intent data = new Intent();

        String itemBody = etEditItem.getText().toString().trim();
        if (!itemBody.isEmpty()) {
            data.putExtra("item_body", etEditItem.getText().toString());
            data.putExtra("position", getIntent().getIntExtra("position", 0));

            setResult(RESULT_OK, data);
            finish();
        } else Toast.makeText(this, "Item body is empty", Toast.LENGTH_LONG).show();
    }
}