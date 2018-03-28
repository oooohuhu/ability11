package com.ustcinfo.mobile.platform.ability.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2018\3\23 0023.
 */

public class SaveBitMapUtils {
    public static String  savebitmap(Context context,Bitmap bitmap, String url){
        File file =null;
            try {
                file= new File(url);
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
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), url, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
       context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE
                , Uri.parse(file.getAbsolutePath())));
        return file.getAbsolutePath();
    }
}
