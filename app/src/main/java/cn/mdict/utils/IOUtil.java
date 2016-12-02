/*
 * Copyright (C) 2013. Rayman Zhang <raymanzhang@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cn.mdict.utils;

import android.content.res.AssetManager;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import cn.mdict.mdx.MdxEngine;


/**
 * User: rayman
 * Date: 13-2-20
 * Time: 上午11:20
 */
public class IOUtil {
    public static final String HttpUserAgent="MDict-Android";

    public static boolean createDir(String dir) {
        File dirFile = new File(dir);
        return dirFile.mkdir();
    }

    public static void forceClose(InputStream is){
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void forceClose(OutputStream os){
        try {
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public interface StatusReport{
        public void onProgressUpdate(long count);
        public void onStart();
        public void onGetTotal(long total);
        public void onComplete();
        public void onError(Exception e);
        public void onInterrupted();
        public boolean isCanceled();
    }

    public static boolean streamDuplicate(InputStream is, OutputStream os, int bufferSize, StatusReport statusReport){
        is= new BufferedInputStream(is);
        os= new BufferedOutputStream(os);
        byte[] buffer = new byte[bufferSize];
        int length;
        int count=0;
        boolean result=false;
        try{
            while ( ((statusReport==null) || !statusReport.isCanceled()) && (length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
                count+=length;
                if ( statusReport!=null )
                    statusReport.onProgressUpdate(count);
            }
            os.flush();
            result = true;
            if ( statusReport!=null ){
                if (statusReport.isCanceled())
                    statusReport.onInterrupted();
                else
                    statusReport.onComplete();
            }
        } catch (Exception e) {
            if ( statusReport!=null )
                statusReport.onError(e);
            else
                e.printStackTrace();
        }finally {
            forceClose(is);
            forceClose(os);
        }
        return result;
    }

    public static boolean streamDuplicate(InputStream is, OutputStream os, StatusReport statusReport){
        return streamDuplicate(is, os, 8*1024, statusReport);
    }

    public static boolean streamToFile(InputStream is, String fileName, boolean overwrite, StatusReport statusReport){
        // Check if the dest exists before copying
        File file = new File(fileName);
        if (file.exists() && !overwrite)
            return true;
        try {
            OutputStream os = new FileOutputStream(fileName);
            return streamDuplicate(is,os, statusReport);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean copyAssetToFile(AssetManager assets, String filename, boolean overwrite, String targetDir, String newFileName) {

        boolean result = false;
        try {
            final String destfilename = targetDir + "/" + ((newFileName != null && newFileName.length() > 0) ? newFileName : filename);
            // Open the source file in your assets directory
            InputStream is = new BufferedInputStream(assets.open(filename));
            result=streamToFile(is, destfilename, overwrite, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static byte[] loadBinFromAsset(AssetManager am, String fileName, boolean checkDocExist) {
        try {
            InputStream is = null;
            if (checkDocExist) {
                File resFile = new File(MdxEngine.getDocDir() + fileName);
                if (resFile.exists() && resFile.isFile()) {
                    is = new FileInputStream(resFile);
                }
            }
            if (is == null)
                is = am.open(fileName);
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            if ( streamDuplicate(is, buffer, null) ){
                return buffer.toByteArray();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //AssetFileDescriptor afd=am.openFd(fileName);
        return null;
    }

    public static boolean loadStringFromStream(InputStream inputStream, String encoding, StringBuffer str) {
        boolean result = true;
        int buffer_size = 512;
        char[] char_buf = new char[buffer_size];
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(inputStream, encoding));
            int read_size;
            while ((read_size = in.read(char_buf)) != -1) {
                str.append(char_buf, 0, read_size);
            }
        } catch (UnsupportedEncodingException e) {
            result = false;
            e.printStackTrace();
        } catch (IOException e) {
            result = false;
            e.printStackTrace();
        } finally {
            if (in != null)
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        return result;
    }


    public static boolean loadStringFromFile(String filename, StringBuffer str) {
        try {
            return loadStringFromStream(new FileInputStream(filename), "UTF8", str);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean loadStringFromAsset(AssetManager assets, String filename, StringBuffer str, boolean overwriteByFile) {
        try {
            if (overwriteByFile) {
                if (loadStringFromFile(MdxEngine.getDocDir() + filename, str))
                    return true;
            }
            return loadStringFromStream(assets.open(filename), "UTF8", str);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean saveStringToFile(String filename, String str, String charset) {
        try {
            InputStream is= new ByteArrayInputStream(str.getBytes(charset));
            return streamToFile(is, filename, true, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean saveBytesToFile(String fileName, byte[] data) {
        return streamToFile(new ByteArrayInputStream(data), fileName, true, null);
    }


    public static boolean httpGetFile(String url, String target, StatusReport statusReport){
        try {
            OutputStream os = new FileOutputStream(target);
            return httpGetFile(url, os, statusReport);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean httpGetFile(String url, OutputStream os, StatusReport statusReport){
       /* try{
            HttpClient client = new DefaultHttpClient();
            client.getParams().setParameter(CoreProtocolPNames.USER_AGENT, HttpUserAgent);
            HttpGet get = new HttpGet(url);
            if (statusReport!=null)
                statusReport.onStart();
            HttpResponse response = client.execute(get);
            if ( response.getStatusLine()!=null && response.getStatusLine().getStatusCode()!=200 ){
                get.abort();
                return false;
            }
            HttpEntity entity = response.getEntity();
            long length = entity.getContentLength();
            if (statusReport!=null)
                statusReport.onGetTotal(length);
            boolean result=streamDuplicate(entity.getContent(), os, statusReport);
            return result;
        }
        catch (Exception e){
            if (statusReport!=null)
                statusReport.onError(e);
            else
                e.printStackTrace();
        }*/
        return false;
    }

}
