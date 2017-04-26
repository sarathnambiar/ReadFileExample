package com.sarath.demo.readfileexample;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.thebrownarrow.permissionhelper.ActivityManagePermission;
import com.thebrownarrow.permissionhelper.PermissionResult;
import com.thebrownarrow.permissionhelper.PermissionUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ListFilesActivity extends ActivityManagePermission {

    List<File> files;
    ListView listFiles;
    ArrayList SavedFiles;

    Button btn_show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_files);

        btn_show = (Button) findViewById(R.id.btn_files);
        listFiles = (ListView) findViewById(R.id.list);
        SavedFiles = new ArrayList();

        btn_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_show.setVisibility(View.GONE);
                listFiles.setVisibility(View.VISIBLE);
                askPermission();
            }
        });


        listFiles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent mainIntent = new Intent(ListFilesActivity.this, MainActivity.class);
                mainIntent.putExtra("filePath", SavedFiles.get(position).toString());
                startActivity(mainIntent);
            }
        });

    }

    private void askPermission() {
        askCompactPermission(PermissionUtils.Manifest_READ_EXTERNAL_STORAGE, new PermissionResult() {
            @Override
            public void permissionGranted() {
                //permission granted
                //replace with your action

                files = getListFiles(new File("/sdcard"));
                Log.e("list", " " + files);

                for (int j = 0; j < files.size(); j++) {
                    SavedFiles.add(files.get(j).toString());
                }

                ArrayAdapter<String> adapter
                        = new ArrayAdapter<String>(getApplicationContext(),
                        android.R.layout.simple_list_item_1,
                        SavedFiles);

                listFiles.setAdapter(adapter);
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
                openSettingsApp(ListFilesActivity.this);
            }
        });
    }

    private List<File> getListFiles(File parentDir) {
        ArrayList<File> inFiles = new ArrayList<File>();
        File[] files = parentDir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                inFiles.addAll(getListFiles(file));
            } else {
                if (file.getName().endsWith(".txt")) {
                    inFiles.add(file);
                }
            }
        }
        return inFiles;
    }
}
