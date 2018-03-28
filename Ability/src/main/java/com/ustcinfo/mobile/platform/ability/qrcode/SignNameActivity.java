package com.ustcinfo.mobile.platform.ability.qrcode;

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
import com.ustcinfo.mobile.platform.ability.R;
import com.ustcinfo.mobile.platform.ability.utils.BitmapHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2018\3\26 0026.
 */

public class SignNameActivity extends AppCompatActivity {

    private SignaturePad signature_pad;
    private Button btnDelete;
    private Button btnOk;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signname);
        initView();
    }

    private void initView() {
        signature_pad = (SignaturePad) findViewById(R.id.signature_pad);
        btnDelete = (Button) findViewById(R.id.btnDelete);
        btnOk = (Button) findViewById(R.id.btnOk);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signature_pad.clear();
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ok();
            }
        });
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

    private void ok() {
        if (signature_pad.isEmpty()) {
            Toast.makeText(SignNameActivity.this, "请签名", Toast.LENGTH_SHORT).show();
            return;
        }
        Bitmap bitmap = signature_pad.getSignatureBitmap();
        if (bitmap == null) {
            Toast.makeText(SignNameActivity.this, "请书写后确定", Toast.LENGTH_SHORT).show();
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putByteArray("signame", BitmapHelper.BitmapToByte(bitmap));
        Intent intent = new Intent();
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }
}
