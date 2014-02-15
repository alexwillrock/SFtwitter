package com.example.SFtwitter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;

import java.util.Arrays;

public class TwitterSearched extends Activity {

    private SharedPreferences savedSearches;
    private TableLayout queryTableLayout;
    private EditText queryEditText;
    private EditText tagEditText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        savedSearches = getSharedPreferences("searches", MODE_PRIVATE);
        queryTableLayout = (TableLayout) findViewById(R.id.queryTableLayout);
        queryEditText = (EditText) findViewById(R.id.queryEditText);
        tagEditText = (EditText) findViewById(R.id.tagEditText);

        Button saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(saveButtonListener);

        Button clearTagsButton = (Button) findViewById(R.id.clearTagsButton);
        //clearTagsButton.setOnClickListener(clearTagsButtonListener);

        refreshButtons(null);
    }

    private void refreshButtons(String newTag) {
        String[] tags = savedSearches.getAll().keySet().toArray(new String[0]);
        Arrays.sort(tags, String.CASE_INSENSITIVE_ORDER);
        if(newTag != null){
            makeTagGui(newTag, Arrays.binarySearch(tags, newTag));
        }
        else
        {
            for(int index = 0; index < tags.length; ++index){
                makeTagGui(tags[index], index);
            }
        }
    }

    private void makeTag(String query, String tag) {
        String originalQuery = savedSearches.getString(tag, null);

        SharedPreferences.Editor prefenceEditor = savedSearches.edit();
        prefenceEditor.putString(tag, query);
        prefenceEditor.apply();

        if(originalQuery == null){
            refreshButtons(tag);
        }
    }

    private void makeTagGui(String tag, int index){
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View newTagView = inflater.inflate(R.layout.new_tag_view, null);

        Button newTagButton = (Button) newTagView.findViewById(R.id.newTagButton);
        newTagButton.setText(tag);
        //newTagButton.setOnClickListener(queryButtonListener);

        Button newEditButton = (Button) newTagButton.findViewById(R.id.newEditButton);
        //newEditButton.setOnClickListener(editButtonListener);

        queryTableLayout.addView(newTagView, index);
    }

    private void clearButtons(){
        queryTableLayout.removeAllViews();
    }

    public View.OnClickListener saveButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(queryEditText.getText().length() > 0 && tagEditText.getText().length() > 0){
                makeTag(queryEditText.getText().toString(), tagEditText.getText().toString());
                queryEditText.setText("");
                tagEditText.setText("");

                ((InputMethodManager)getSystemService(
                        Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                        tagEditText.getWindowToken(), 0);
            }
            else{
                AlertDialog.Builder builder = new AlertDialog.Builder(TwitterSearched.this);

            }

        }
    };

}
