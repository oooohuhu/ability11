package com.example.administrator.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.github.gcacace.signaturepad.views.SignaturePad;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2018\3\26 0026.
 */

public class SignNameActivity extends AppCompatActivity implements View.OnClickListener{

    private SignaturePad signature_pad;
    private Button btnDelete;
    private Button btnOk;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signname);
        initView();
    }
    private void initView(){
        signature_pad = findViewById(R.id.signature_pad);
        btnDelete = (Button)findViewById(R.id.btnDelete);
        btnOk = (Button)findViewById(R.id.btnOk);
        btnDelete.setOnClickListener(this);
        btnOk.setOnClickListener(this);
        signature_pad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onSigned() {
                //Event triggered when the pad is signed
            }

            @Override
            public void onClear() {
                //Event triggered when the pad is cleared
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnDelete:
                signature_pad.clear();
                break;
            case R.id.btnOk:
                ok();
                break;
        }
    }
    private void ok() {
        if (signature_pad.isEmpty())
        {
            Toast.makeText(SignNameActivity.this,"请签名",Toast.LENGTH_SHORT).show();
            return;
        }
        Bitmap bitmap= signature_pad.getSignatureBitmap();
        if(bitmap==null){
            Toast.makeText(SignNameActivity.this,"请书写后确定",Toast.LENGTH_SHORT).show();
            return;
        }
        File file =null;
        try {
            file= new File(Environment.getExternalStorageDirectory()+"/"+"signname"+".jpg");
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(this.getContentResolver(),
                    file.getAbsolutePath(), Environment.getExternalStorageDirectory()+"/"+"signname"+".jpg", null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE
                , Uri.parse(file.getAbsolutePath())));
        Toast.makeText(this,file.getAbsolutePath()+"",Toast.LENGTH_LONG).show();
        finish();
    }
}
