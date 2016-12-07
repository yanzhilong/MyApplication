package com.englishlearn.myapplication.core;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.englishlearn.myapplication.data.DownloadStatus;
import com.englishlearn.myapplication.data.source.remote.RemoteCode;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobDefaultError;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobRequestException;
import com.englishlearn.myapplication.data.source.remote.bmob.service.RetrofitService;
import com.englishlearn.myapplication.data.source.remote.bmob.service.ServiceFactory;
import com.englishlearn.myapplication.observer.DownloadObserver;
import com.englishlearn.myapplication.observer.DownloadUtilObserver;
import com.englishlearn.myapplication.util.RxUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by yanzl on 16-12-4.
 * 下载管理类
 */

public class DownloadUtil {

    private final static String TAG = DownloadUtil.class.getSimpleName();


    private static DownloadUtil downloadUtil;
    private DownloadManager downloadManager;
    private DownloadObserver mDownloadObserver;
    private Context context;

    public DownloadUtil(Context context) {
        this.context = context;
    }

    public static synchronized DownloadUtil newInstance(Context context) {
        if(downloadUtil == null){
            downloadUtil = new DownloadUtil(context);
        }
        return downloadUtil;
    }


    public static void downLoadFile(final String targetFile, final String url){

        Log.d(TAG,"downLoadFile:" + url);
        Observable.create(new Observable.OnSubscribe<DownloadStatus>() {
            @Override
            public void call(Subscriber<? super DownloadStatus> subscriber) {

                RetrofitService bmobService = ServiceFactory.getInstance().createRetrofitService();
                Call<ResponseBody> call = bmobService.downloadFile(url);
                try {
                    Response<ResponseBody> responseBodyResponse = call.execute();
                    if(responseBodyResponse.isSuccessful()){
                        ResponseBody body = responseBodyResponse.body();
                        try {
                            InputStream inputStream = null;
                            OutputStream outputStream = null;
                            DownloadStatus downloadStatus = new DownloadStatus();

                            try {
                                byte[] fileReader = new byte[4096];

                                long fileSize = body.contentLength();//文件大小
                                long fileSizeDownloaded = 0;//当前已经下载的数据　
                                //设置文件大小
                                downloadStatus.setSize(fileSize);
                                downloadStatus.setSizeStr(humanReadableByteCount(fileSize,false));

                                inputStream = body.byteStream();
                                outputStream = new FileOutputStream(targetFile);

                                int olepercent = 0;//百分比
                                long time = 10;//下载时间，总的（毫秒）
                                long start = System.currentTimeMillis();//当前时间　
                                while (true) {
                                    int read = inputStream.read(fileReader);
                                    if (read == -1) {
                                        break;
                                    }
                                    outputStream.write(fileReader, 0, read);
                                    fileSizeDownloaded += read;
                                    int percent = (int)(((float)fileSizeDownloaded / (float)fileSize) * 100);//计算百分比
                                    Log.d(TAG, "file download: " + fileSizeDownloaded + Thread.currentThread().getName());
                                    //500毫秒刷新一次
                                    if(percent != olepercent && (System.currentTimeMillis() - start) > 20 || percent == 100){

                                        long end = System.currentTimeMillis();//当前时间　
                                        time=((end-start) < 1000 ? 1000 : (end-start));
                                        //计算下载速度
                                        float fSpeed=0;
                                        fSpeed=(float)fileSizeDownloaded;  //dwBytes是目前已读取的字节数
                                        fSpeed/=((float)time)/1000.0f;
                                        //fSpeed/=1024.0f;//这些bit代码用于根据所花时间计算下载速度和读取的数据量
                                        downloadStatus.setSpeed(humanReadableByteCount((long) fSpeed,true) + "/S");

                                        Log.d(TAG, "file download: " + fileSizeDownloaded + " of " + fileSize);
                                        start = System.currentTimeMillis();
                                        downloadStatus.setCurrentsize(fileSizeDownloaded);
                                        downloadStatus.setCurrentsizestr(humanReadableByteCount(fileSizeDownloaded,false));
                                        DownloadStatus downloadStatus1 = (DownloadStatus) downloadStatus.clone();
                                        downloadStatus1.setPercent(percent);
                                        subscriber.onNext(downloadStatus1);
                                        olepercent = percent;
                                    }
                                }

                                outputStream.flush();

                                subscriber.onCompleted();
                            } catch (IOException e) {
                                subscriber.onError(e);
                            } finally {
                                if (inputStream != null) {
                                    inputStream.close();
                                }

                                if (outputStream != null) {
                                    outputStream.close();
                                }
                            }
                        } catch (IOException e) {
                            subscriber.onError(e);
                        }
                    }else{
                        String errjson =  responseBodyResponse.errorBody().string();
                        Gson gson = new GsonBuilder().create();
                        BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                        RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                        BmobRequestException bmobRequestException = new BmobRequestException(createuser.getMessage());
                        subscriber.onError(bmobRequestException);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }
        }).compose(RxUtil.<DownloadStatus>applySchedulers()).subscribe(new Subscriber<DownloadStatus>() {
            @Override
            public void onCompleted() {
                DownloadStatus downloadStatus = new DownloadStatus();
                downloadStatus.setUrl(url);
                downloadStatus.setDownloading(false);
                downloadStatus.setSuccess(true);
                DownloadUtilObserver.newInstance().notifyObservers(downloadStatus);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                DownloadStatus downloadStatus = new DownloadStatus();
                downloadStatus.setDownloading(false);
                downloadStatus.setUrl(url);
                downloadStatus.setException(true);
                DownloadUtilObserver.newInstance().notifyObservers(downloadStatus);
            }

            @Override
            public void onNext(DownloadStatus downloadStatus) {
                Log.d(TAG, "onNext: " + downloadStatus + Thread.currentThread().getName());

                downloadStatus.setDownloading(true);
                downloadStatus.setUrl(url);
                DownloadUtilObserver.newInstance().notifyObservers(downloadStatus);
            }
        });

        
    }
    public static String humanReadableByteCount(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }


    public void downLoadFile(String dirType, String subPath, final String url){


        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        /**设置用于下载时的网络状态*/
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        /**设置通知栏是否可见*/
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
        /**设置漫游状态下是否可以下载*/
        request.setAllowedOverRoaming(false);
        /**如果我们希望下载的文件可以被系统的Downloads应用扫描到并管理，
        我们需要调用Request对象的setVisibleInDownloadsUi方法，传递参数true.*/
        request.setVisibleInDownloadsUi(true);
        /**设置文件保存路径*/
        request.setDestinationInExternalFilesDir(context, dirType, subPath);
        /**将下载请求放入队列， return下载任务的ID*/
        long downloadId = downloadManager.enqueue(request);

        mDownloadObserver = new DownloadObserver(mHandler,this, downId);

        //在执行下载前注册内容监听者
        context.getContentResolver().registerContentObserver(Uri.parse("content://downloads/"), true, mDownloadObserver);

        Log.d(TAG,"downLoadFile:" + url);
        Observable.create(new Observable.OnSubscribe<DownloadStatus>() {
            @Override
            public void call(Subscriber<? super DownloadStatus> subscriber) {

                RetrofitService bmobService = ServiceFactory.getInstance().createRetrofitService();
                Call<ResponseBody> call = bmobService.downloadFile(url);
                try {
                    Response<ResponseBody> responseBodyResponse = call.execute();
                    if(responseBodyResponse.isSuccessful()){
                        ResponseBody body = responseBodyResponse.body();
                        try {
                            InputStream inputStream = null;
                            OutputStream outputStream = null;
                            DownloadStatus downloadStatus = new DownloadStatus();

                            try {
                                byte[] fileReader = new byte[4096];

                                long fileSize = body.contentLength();//文件大小
                                long fileSizeDownloaded = 0;//当前已经下载的数据　
                                //设置文件大小
                                downloadStatus.setSize(fileSize);
                                downloadStatus.setSizeStr(humanReadableByteCount(fileSize,false));

                                inputStream = body.byteStream();
                                outputStream = new FileOutputStream(targetFile);

                                int olepercent = 0;//百分比
                                long time = 10;//下载时间，总的（毫秒）
                                long start = System.currentTimeMillis();//当前时间　
                                while (true) {
                                    int read = inputStream.read(fileReader);
                                    if (read == -1) {
                                        break;
                                    }
                                    outputStream.write(fileReader, 0, read);
                                    fileSizeDownloaded += read;
                                    int percent = (int)(((float)fileSizeDownloaded / (float)fileSize) * 100);//计算百分比
                                    Log.d(TAG, "file download: " + fileSizeDownloaded + Thread.currentThread().getName());
                                    //500毫秒刷新一次
                                    if(percent != olepercent && (System.currentTimeMillis() - start) > 20 || percent == 100){

                                        long end = System.currentTimeMillis();//当前时间　
                                        time=((end-start) < 1000 ? 1000 : (end-start));
                                        //计算下载速度
                                        float fSpeed=0;
                                        fSpeed=(float)fileSizeDownloaded;  //dwBytes是目前已读取的字节数
                                        fSpeed/=((float)time)/1000.0f;
                                        //fSpeed/=1024.0f;//这些bit代码用于根据所花时间计算下载速度和读取的数据量
                                        downloadStatus.setSpeed(humanReadableByteCount((long) fSpeed,true) + "/S");

                                        Log.d(TAG, "file download: " + fileSizeDownloaded + " of " + fileSize);
                                        start = System.currentTimeMillis();
                                        downloadStatus.setCurrentsize(fileSizeDownloaded);
                                        downloadStatus.setCurrentsizestr(humanReadableByteCount(fileSizeDownloaded,false));
                                        DownloadStatus downloadStatus1 = (DownloadStatus) downloadStatus.clone();
                                        downloadStatus1.setPercent(percent);
                                        subscriber.onNext(downloadStatus1);
                                        olepercent = percent;
                                    }
                                }

                                outputStream.flush();

                                subscriber.onCompleted();
                            } catch (IOException e) {
                                subscriber.onError(e);
                            } finally {
                                if (inputStream != null) {
                                    inputStream.close();
                                }

                                if (outputStream != null) {
                                    outputStream.close();
                                }
                            }
                        } catch (IOException e) {
                            subscriber.onError(e);
                        }
                    }else{
                        String errjson =  responseBodyResponse.errorBody().string();
                        Gson gson = new GsonBuilder().create();
                        BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                        RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                        BmobRequestException bmobRequestException = new BmobRequestException(createuser.getMessage());
                        subscriber.onError(bmobRequestException);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }
        }).compose(RxUtil.<DownloadStatus>applySchedulers()).subscribe(new Subscriber<DownloadStatus>() {
            @Override
            public void onCompleted() {
                DownloadStatus downloadStatus = new DownloadStatus();
                downloadStatus.setUrl(url);
                downloadStatus.setDownloading(false);
                downloadStatus.setSuccess(true);
                DownloadUtilObserver.newInstance().notifyObservers(downloadStatus);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                DownloadStatus downloadStatus = new DownloadStatus();
                downloadStatus.setDownloading(false);
                downloadStatus.setUrl(url);
                downloadStatus.setException(true);
                DownloadUtilObserver.newInstance().notifyObservers(downloadStatus);
            }

            @Override
            public void onNext(DownloadStatus downloadStatus) {
                Log.d(TAG, "onNext: " + downloadStatus + Thread.currentThread().getName());

                downloadStatus.setDownloading(true);
                downloadStatus.setUrl(url);
                DownloadUtilObserver.newInstance().notifyObservers(downloadStatus);
            }
        });


    }

    /*private Handler mHandler = new Handler() {
        public void handleMessage(final Message msg) {
            super.handleMessage(msg);
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    mDownloadFileBtn.setText(msg.what + "%");

                }
            });


        }
    };*/
}
