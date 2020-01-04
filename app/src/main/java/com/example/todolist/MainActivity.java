package com.example.todolist;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    final List<String> stringList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ListView listView = findViewById(R.id.listView);
        final TextAdapter adapter = new TextAdapter();

        readInfo();

        adapter.setData(stringList);
        listView.setAdapter(adapter);

        // Deletion of listItems
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Delete?")
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                stringList.remove(position);
                                adapter.setData(stringList);
                            }
                        })
                        .setNegativeButton("No",null)
                        .create();
                dialog.show();
            }
        });

        // new task button handling process for adding new tasks
        final Button newTaskButton = findViewById(R.id.newTask);
        newTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText taskInput = new EditText(MainActivity.this);
                taskInput.setSingleLine();
                AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("new task")
                        .setMessage("What is your new Task?")
                        .setView(taskInput)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                stringList.add(taskInput.getText().toString());
                                adapter.setData(stringList);
                                saveInfo();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();

                dialog.show();
            }
        });

        // clear all button handling
        final Button clearAllbutton = findViewById(R.id.clearAll);
        clearAllbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Delete All?")
                        .setMessage("Are you Sure?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                stringList.clear();
                                adapter.setData(stringList);
                                saveInfo();
                            }
                        })
                        .setNegativeButton("No", null)
                        .create();
                dialog.show();

            }
        });
    }


    // saving tasks into a file
    private void saveInfo(){
        try {
            File file = new File(this.getFilesDir(),"savedTasks");

            FileOutputStream fos = new FileOutputStream(file);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

            for (int i = 0; i<stringList.size();i++)
            {
                bw.write(stringList.get(i));
                bw.newLine();
            }
            bw.close();
            fos.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    // reading tasks from an existing file
    private void readInfo(){
        File file = new File(this.getFilesDir(),"savedTasks");
        if(!file.exists()){
            return;
        }
        try {
            FileInputStream fis = new FileInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));

            String line = br.readLine();
            while (line != null){
                stringList.add(line);
                line = br.readLine();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    // Text adapter
    class TextAdapter extends BaseAdapter{

        List<String> list= new ArrayList<>();

        void setData(List<String> mList){
            list.clear();
            list.addAll(mList);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if(convertView == null){
                LayoutInflater inflater = (LayoutInflater)
                        MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.item, parent, false);
            }



            TextView textView = convertView.findViewById(R.id.task);

            //applying some shit to the even numbers of list items
            if(position % 3 == 0){
                textView.setBackgroundColor(Color.GRAY);
                textView.setTextColor(Color.WHITE);
            }
            textView.setText(list.get(position));
            return convertView;

        }

    }
}

/*
Additional features to add after WW3 :'D
- add date and time
- notification
- alarm turn on/off
- add more options to the bar above that i don't remember its name :D
 */