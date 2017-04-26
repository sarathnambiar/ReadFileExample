package com.sarath.demo.readfileexample;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.ExpandableListView;

import com.thebrownarrow.permissionhelper.ActivityManagePermission;
import com.thebrownarrow.permissionhelper.PermissionResult;
import com.thebrownarrow.permissionhelper.PermissionUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class MainActivity extends ActivityManagePermission {

    File sdcard;
    File file;
    String filepath;

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        filepath = getIntent().getStringExtra("filePath").toString();
        Log.e("filepath",""+ filepath);

        initialize();

        askPermission();

        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);

    }

    private void prepareListData() {

        listDataHeader.add("0-10");
        listDataHeader.add("10-20");

    }

    private void askPermission() {
        askCompactPermission(PermissionUtils.Manifest_READ_EXTERNAL_STORAGE, new PermissionResult() {
            @Override
            public void permissionGranted() {
                //permission granted
                //replace with your action
                readFile();
            }

            @Override
            public void permissionDenied() {
                //permission denied
                //replace with your action
            }

            @Override
            public void permissionForeverDenied() {
                // user has check never ask again
                // you need to open setting manually
                openSettingsApp(MainActivity.this);
            }
        });
    }

    private void initialize() {
        sdcard = Environment.getExternalStorageDirectory();
        //Get the text file
        file = new File(filepath);
    }

    private void readFile() {


        //Read text from file
        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
        } catch (IOException e) {
            //You'll need to add proper error handling here
        }

        Log.e("text-->", "" + text);

        try {
            countWords();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void countWords() throws IOException {
        Map<String, Integer> countByWords = new HashMap<>();
        Scanner s = new Scanner(new File(filepath));
        while (s.hasNext()) {
            String next = s.next();
            Integer count = countByWords.get(next);
            if (count != null) {
                countByWords.put(next, count + 1);
            } else {
                countByWords.put(next, 1);
            }
        }

        Log.e("tag", countByWords.toString());


        s.close();
    }


}
