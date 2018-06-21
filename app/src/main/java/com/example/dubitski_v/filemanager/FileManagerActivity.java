package com.example.dubitski_v.filemanager;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileManagerActivity extends ListActivity {
    private List<String> directoryEntries = new ArrayList<String>();
    private File currentDirectory = new File("/");
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        //установить основной макет
        setContentView(R.layout.activity_file_manager);
        //перейти в корневой каталог
        browseTo(new File("/"));
    }

    //перейти к родительскому каталогу
    private void upOneLevel(){
        if(this.currentDirectory.getParent() != null) {
            this.browseTo(this.currentDirectory.getParentFile());
        }
    }

    // перейти к файлу или каталогу
    private void browseTo(final File aDirectory){
        ///если мы хотим просмотреть каталог
        if (aDirectory.isDirectory()){
            //заполнить список файлами из этого каталога
            this.currentDirectory = aDirectory;
            fill(aDirectory.listFiles());

            //задать текст диспетчера заголовков
            TextView titleManager = (TextView) findViewById(R.id.titleManager);
            titleManager.setText(aDirectory.getAbsolutePath());
        } else {
            //если мы хотим открыть файл, покажите это диалоговое окно:
            //прослушиватель при нажатии кнопки да
            DialogInterface.OnClickListener okButtonListener = new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface arg0, int arg1) {
                    //навигация по файлу
                    Intent i = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("file://" + aDirectory.getAbsolutePath()));
                    //start this activity
                    startActivity(i);
                }
            };
            //прослушивать, когда кнопка не нажата
            DialogInterface.OnClickListener cancelButtonListener = new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    //ничего не делать
                    //или добавить то, что вы хотите
                }
            };

            //создать диалог
            new AlertDialog.Builder(this)
                    .setTitle("Подтверждение") //название
                    .setMessage("Хотите открыть файл "+ aDirectory.getName() + "?") //message
                    .setPositiveButton("Да", okButtonListener) //positive button
                    .setNegativeButton("Нет", cancelButtonListener) //negative button
                    .show(); //показать диалог
        }
    }
    //полный список
    private void fill(File[] files) {
        //очистить список
        this.directoryEntries.clear();

        if (this.currentDirectory.getParent() != null)
            this.directoryEntries.add("..");

        //добавить каждый файл в список
        for (File file : files) {
            this.directoryEntries.add(file.getAbsolutePath());
        }

        //создать адаптер массива, чтобы показать все
        ArrayAdapter<String> directoryList = new ArrayAdapter<String>(this, R.layout.row, this.directoryEntries);
        this.setListAdapter(directoryList);
    }
    //при нажатии на элемент
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        //получить выбранное Имя файла
        int selectionRowID = position;
        String selectedFileString = this.directoryEntries.get(selectionRowID);

        //если мы выберем".."тогда иди наверх
        if(selectedFileString.equals("..")){
            this.upOneLevel();
        } else {
            //перейдите к выбранному файлу или каталогу с помощью()
            File clickedFile = null;
            clickedFile = new File(selectedFileString);
            if (clickedFile != null)
                this.browseTo(clickedFile);
        }
    }
}

