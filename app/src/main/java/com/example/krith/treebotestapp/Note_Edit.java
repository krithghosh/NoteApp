package com.example.krith.treebotestapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Note_Edit extends AppCompatActivity implements View.OnClickListener {

    private FloatingActionButton edit;
    private TextView title;
    private TextView description;
    private TextView heading;
    private boolean add;
    CoordinatorLayout mainLayout;
    public static final String MyPREFERENCES = "LocalCache";
    private SharedPreferences sharedPreferences = null;
    private List<NoteTO> list;
    private Boolean flag_edit = false;
    public Typeface Lato_Black;
    public Typeface Lato_Bold;
    public Typeface Lato_Regular;
    public Typeface Lato_light;
    private int position;
    private ImageView delete_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note__edit);
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        Lato_Black = Typeface.createFromAsset(getAssets(), "fonts/Lato-Black.ttf");
        Lato_Bold = Typeface.createFromAsset(getAssets(), "fonts/Lato-Bold.ttf");
        Lato_Regular = Typeface.createFromAsset(getAssets(), "fonts/Lato-Regular.ttf");
        Lato_light = Typeface.createFromAsset(getAssets(), "fonts/Lato-Light.ttf");

        position = -1;
        list = new ArrayList<NoteTO>();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        delete_btn = (ImageView) findViewById(R.id.delete_btn);
        mainLayout = (CoordinatorLayout) findViewById(R.id.mainLayout);
        title = (TextView) findViewById(R.id.title);
        title.setTypeface(Lato_Bold);
        description = (TextView) findViewById(R.id.description);
        description.setTypeface(Lato_Regular);
        edit = (FloatingActionButton) findViewById(R.id.edit);
        heading = (TextView) findViewById(R.id.toolbarText);
        heading.setTypeface(Lato_Regular);

        if (sharedPreferences.getString("NOTES", "").length() != 0) {
            Gson gson = new Gson();
            String json = sharedPreferences.getString("NOTES", "");
            Type type = new TypeToken<List<NoteTO>>() {
            }.getType();
            list = gson.fromJson(json, type);
        }

        Intent intent = getIntent();
        if (getIntent().hasExtra("position")) {
            flag_edit = true;
            position = intent.getIntExtra("position", 0);
            edit.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
            edit.setImageDrawable(getResources().getDrawable(R.drawable.ic_edit_24px));
            title.setBackground(getResources().getDrawable(R.drawable.textbox_disabled));
            description.setBackground(getResources().getDrawable(R.drawable.textbox_disabled));
            title.setEnabled(false);
            description.setEnabled(false);
            title.setText(list.get(position).getTitle());
            description.setText(list.get(position).getDescription());
            heading.setText("EDIT NOTE");
        } else {
            edit.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
            edit.setImageDrawable(getResources().getDrawable(R.drawable.ic_done_24px));
            heading.setText("ADD NOTE");
        }
        edit.setOnClickListener(this);
        delete_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.edit:
                if (flag_edit) {
                    title.setEnabled(true);
                    description.setEnabled(true);
                    description.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(description, InputMethodManager.SHOW_IMPLICIT);
                    edit.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                    edit.setImageDrawable(getResources().getDrawable(R.drawable.ic_done_24px));
                    flag_edit = false;
                } else {
                    if (title.getText().toString().length() != 0 && description.getText().toString().length() != 0) {
                        if (flag_edit || position != -1) {
                            list.get(position).setDescription(description.getText().toString());
                            list.get(position).setTitle(title.getText().toString());
                            list.get(position).setDate(getCurrentTimeStamp());
                        } else {
                            NoteTO noteTO = new NoteTO();
                            noteTO.setDate(getCurrentTimeStamp());
                            noteTO.setDescription(description.getText().toString());
                            noteTO.setTitle(title.getText().toString());
                            list.add(noteTO);
                        }
                        setListOfObjects("NOTES", list);
                        startActivity(new Intent(this, NoteList.class));
                    } else {
                        Snackbar.make(mainLayout, "Please fill the title & the description", Snackbar.LENGTH_SHORT).show();
                    }
                }
                break;

            case R.id.delete_btn:
                if (flag_edit || position != -1) {
                    list.remove(position);
                    setListOfObjects("NOTES", list);
                    startActivity(new Intent(this, NoteList.class));
                } else {
                    startActivity(new Intent(this, NoteList.class));
                }
                break;
        }
    }

    public <T> void setListOfObjects(String key, List<T> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        set(key, json);
    }

    public void set(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String getCurrentTimeStamp() {
        return new SimpleDateFormat("dd MMM yyyy h:mm a").format(new Date());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        return true;
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, NoteList.class));
    }
}