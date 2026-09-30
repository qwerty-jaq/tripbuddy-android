package com.example.tripbuddy_eduv4909634;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.helper.widget.Grid;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GalleryActivity extends AppCompatActivity {

    private GridView gridView;
    private Button btAdd;
    private final ArrayList<Uri> imageUris = new ArrayList<>();
    private ImageGridAdapter adapter;
    private MemoriesDBHelper dbHelper;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_gallery);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbHelper = new MemoriesDBHelper(this);

        gridView = findViewById(R.id.galleryGrid);
        btAdd = findViewById(R.id.btAdd);

        adapter = new ImageGridAdapter();
        gridView.setAdapter(adapter);

        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              startActivity(new Intent(GalleryActivity.this, MemoriesActivity.class));
            }
        });

        gridView.setOnItemClickListener((parent, view, position, id) -> {
            Uri uri = imageUris.get(position);
            Intent i = new Intent(GalleryActivity.this, FullScreenImageActivity.class);
            i.setData(uri);
            startActivity(i);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadImagesFromDB();
    }

    private void loadImagesFromDB() {
        imageUris.clear();
        try (Cursor c = dbHelper.getMemoriesDesc()) {
            if (c != null && c.moveToFirst()) {
                int idxUri = c.getColumnIndex("imageUri");
                do {
                    String uriStr = c.getString(idxUri);
                    if (uriStr != null && !uriStr.isEmpty()) {
                        imageUris.add(Uri.parse(uriStr));
                    }
                } while (c.moveToNext());
            }
        }
        if (adapter != null) adapter.notifyDataSetChanged();
    }

    private class ImageGridAdapter extends BaseAdapter {
        @Override public int getCount() {return imageUris.size(); }
        @Override public Object getItem (int position) {return imageUris.get(position); }
        @Override public long getItemId (int position) {return position; }

        @Override
        public android.view.View getView (int position, android.view.View convertView, ViewGroup parent) {
            ImageView iv;
            if (convertView == null ) {
                iv = new ImageView(GalleryActivity.this);
                iv.setLayoutParams(new GridView.LayoutParams(300, 300));
                iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            } else {
                iv = (ImageView) convertView;
            }
            iv.setImageURI(imageUris.get(position));
            iv.startAnimation(AnimationUtils.loadAnimation(GalleryActivity.this, R.anim.fade_in));
            return iv;
        }
    }

}