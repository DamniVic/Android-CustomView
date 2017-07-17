package com.hs.map.shunloc.util;

import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by DAMNICOMNIPLUSVIC on 2017/3/7.
 * (c) 2017 DAMNICOMNIPLUSVIC Inc,All Rights Reserved.
 */

public class FileConverter implements Converter<ResponseBody, File> {

    static final FileConverter INSTANCE = new FileConverter();

    @Override
    public File convert(ResponseBody value) throws IOException {
        String directoryPath = Environment.getExternalStoragePublicDirectory("") + "/DCIM/Camera/test.png";
        return writeResponseBodyToDisk(value, directoryPath);
    }

    /**
     * 将文件写入本地
     *
     * @param body http响应体
     * @param path 保存路径
     * @return 保存file
     */
    private File writeResponseBodyToDisk(ResponseBody body, String path) {

        File futureStudioIconFile = null;
        try {

            futureStudioIconFile = new File(path);

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                }

                outputStream.flush();

                return futureStudioIconFile;
            } catch (IOException e) {
                return futureStudioIconFile;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return futureStudioIconFile;
        }
    }
}
