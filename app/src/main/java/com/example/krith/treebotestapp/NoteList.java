package com.example.krith.treebotestapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class NoteList extends AppCompatActivity implements View.OnClickListener {

    private CoordinatorLayout mainLayout;
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private LinearLayoutManager linearLayoutManager;
    private FloatingActionButton addNote;
    private TextView emptyList;
    private TextView toolbarText;
    public Typeface Lato_Black;
    public Typeface Lato_Bold;
    public Typeface Lato_Regular;
    public Typeface Lato_light;
    public static final String MyPREFERENCES = "LocalCache";
    private SharedPreferences sharedPreferences = null;
    private List<NoteTO> list;
    private RVAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        Lato_Black = Typeface.createFromAsset(getAssets(), "fonts/Lato-Black.ttf");
        Lato_Bold = Typeface.createFromAsset(getAssets(), "fonts/Lato-Bold.ttf");
        Lato_Regular = Typeface.createFromAsset(getAssets(), "fonts/Lato-Regular.ttf");
        Lato_light = Typeface.createFromAsset(getAssets(), "fonts/Lato-Light.ttf");

        mainLayout = (CoordinatorLayout) findViewById(R.id.mainLayout);
        emptyList = (TextView) findViewById(R.id.emptyList);
        toolbarText = (TextView) findViewById(R.id.toolbarText);
        toolbarText.setTypeface(Lato_Regular);
        emptyList.setTypeface(Lato_Regular);
        addNote = (FloatingActionButton) findViewById(R.id.fab);
        recyclerView = (RecyclerView) findViewById(R.id.rv);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(false);

        if (sharedPreferences.getString("NOTES", "").length() != 0) {
            Gson gson = new Gson();
            String json = sharedPreferences.getString("NOTES", "");
            Type type = new TypeToken<List<NoteTO>>() {
            }.getType();
            list = gson.fromJson(json, type);
            if (list.size() != 0) {
                emptyList.setVisibility(View.INVISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
                adapter = new RVAdapter(this, this, list);
                recyclerView.setAdapter(adapter);
                adapter.SetOnItemClickListener(new RVAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(getApplicationContext(), Note_Edit.class);
                        intent.putExtra("position", position);
                        startActivity(intent);
                    }
                });
            } else {
                emptyList.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.INVISIBLE);
            }
        }
        addNote.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_note_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                startActivity(new Intent(this, Note_Edit.class));
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
