package com.sarath.demo.readfileexample;

import android.app.ListActivity;
import android.os.Bundle;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class MainActivity extends ListActivity {

    File sdcard;
    File file;
    String filepath;
    Map<String, Integer> countByWords;
    int header_count = 1000, initial_val = 0;
    private CustomAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        filepath = getIntent().getStringExtra("filePath").toString();
        initialize();
        readFile();
        prepareListData();
    }

    private void prepareListData() {

        Set<Map.Entry<String, Integer>> set = countByWords.entrySet();
        List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, java.lang.Integer>>(set);
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });

        mAdapter = new CustomAdapter(this);

        for (int i = 0; i < list.size(); i++) {
            if (header_count > list.get(i).getValue()) {
                int result = roundUp(list.get(i).getValue());

                int lower_limit = roundDown(list.get(i).getValue());

                if (initial_val != result) {
                    initial_val = result;
                    mAdapter.addSectionHeaderItem(lower_limit + "-" + result);
                }

                header_count = list.get(i).getValue();
            }
            mAdapter.addItem(list.get(i).getKey() + " ( No of occurance : " + list.get(i).getValue() + " ) ");
        }

        setListAdapter(mAdapter);
    }

    int roundUp(int n) {
        return (n + 9) / 10 * 10;
    }

    int roundDown(int n) {
        return (n - 9) / 10 * 10;
    }

    private void initialize() {
        sdcard = Environment.getExternalStorageDirectory();
        file = new File(filepath);
    }

    private void readFile() {
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
        }
        try {
            countWords();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void countWords() throws IOException {
        countByWords = new HashMap<>();
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
        s.close();
    }
}
