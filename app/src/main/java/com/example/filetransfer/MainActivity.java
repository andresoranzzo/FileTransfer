package com.example.filetransfer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Array;

public class MainActivity extends AppCompatActivity {

    private static final int BUFFER = 2048;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public int copyToDirectory(String old, String newDir) throws IOException {
        File old_file = new File(Environment.getExternalStorageDirectory() + "/" + old);
        File temp_dir = new File(Environment.getExternalStorageDirectory() + "/" + newDir);
        if(old_file.exists() && temp_dir.isDirectory()){
            byte[] data = new byte[BUFFER];
            int read = 0;
            if(old_file.isFile() && temp_dir.canWrite()) {
                String file_name = old.substring(old.lastIndexOf("/"), old.length());
                File cp_file = new File(Environment.getExternalStorageDirectory() + "/" + newDir + file_name);
                cp_file.createNewFile();
                if(cp_file.canWrite()){
                    copy(old_file, cp_file);
                }
                if(cp_file.exists()){
                    openDialog("Copiado no diretório " + cp_file.getCanonicalPath());
                    return -1;
                }

            } else if(old_file.isDirectory() && temp_dir.isDirectory() && temp_dir.canWrite()) {
                String files[] = old_file.list();
                String dir = newDir + old.substring(old.lastIndexOf("/"), old.length());
                int len = files.length;

                if(!new File(dir).mkdir())
                    return -1;

                for(int i = 0; i < len; i++)
                    copyToDirectory(old + "/" + files[i], dir);

            } else if(!temp_dir.canWrite())
                return -1;
        } else {
            return 1;
        }
        return 0;

    }
    
    public static void copy(File src, File dst) throws IOException {
        try (InputStream in = new FileInputStream(src)) {
            try (OutputStream out = new FileOutputStream(dst)) {
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            }
        }
    }

    public void sendMessage(View view) throws IOException {
        if(copyToDirectory("Downloads/teste.txt", "Pictures") == 1){
            openDialog("Arquivo não existe!");
        }
    }

    public void openDialog(String message) {
        String title = "";
        if(message == "Arquivo não existe!"){
            title = "Erro";
        } else {
            title = "Transferência concluída";
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage(message)
                .setTitle(title);
        AlertDialog dialog = builder.create();
        dialog.show();
        /*
        final Dialog dialog = new Dialog(this); // Context, this, etc.
        dialog.setContentView(R.layout.dialog_demo);
        dialog.setDismissMessage(message);
        dialog.show();

         */
    }
}