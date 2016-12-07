package com.englishlearn.myapplication.core;

import android.util.Log;

import com.englishlearn.myapplication.data.DownloadStatus;
import com.englishlearn.myapplication.data.source.remote.RemoteCode;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobDefaultError;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobRequestException;
import com.englishlearn.myapplication.data.source.remote.bmob.service.RetrofitService;
import com.englishlearn.myapplication.data.source.remote.bmob.service.ServiceFactory;
import com.englishlearn.myapplication.observer.DownloadObserver;
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

public class DownloadManager {

    private final static String TAG = DownloadManager.class.getSimpleName();

    public static void downLoadFile(final String targetFile, final String url){

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

                                long fileSize = body.contentLength();
                                long fileSizeDownloaded = 0;

                                downloadStatus.setSize(fileSize);
                                downloadStatus.setSizeStr(humanReadableByteCount(fileSize,false));
                                inputStream = body.byteStream();
                                outputStream = new FileOutputStream(targetFile);

                                int olepercent = 0;

                                long currenttime = System.currentTimeMillis();
                                boolean isInit = false;
                                while (true) {
                                    int read = inputStream.read(fileReader);

                                    if (read == -1) {
                                        break;
                                    }

                                    outputStream.write(fileReader, 0, read);

                                    fileSizeDownloaded += read;

                                    int percent = (int)(((float)fileSizeDownloaded / (float)fileSize) * 100);
                                    if(percent != olepercent && (System.currentTimeMillis() - currenttime) > 5 || percent == 100){

                                        Log.d(TAG, "file download: " + fileSizeDownloaded + " of " + fileSize);
                                        currenttime = System.currentTimeMillis();
                                        downloadStatus.setCurrentsize(fileSizeDownloaded);
                                        downloadStatus.setCurrentsizestr(humanReadableByteCount(fileSizeDownloaded,false));
                                        DownloadStatus downloadStatus1 = (DownloadStatus) downloadStatus.clone();
                                        downloadStatus1.setPercent(percent);
                                        subscriber.onNext(downloadStatus1);
                                        olepercent = percent;
                                    }else if(!isInit){
                                        //第一次发送
                                        isInit = true;
                                        downloadStatus.setCurrentsize(fileSizeDownloaded);
                                        downloadStatus.setCurrentsizestr(humanReadableByteCount(fileSizeDownloaded,false));
                                        DownloadStatus downloadStatus1 = (DownloadStatus) downloadStatus.clone();
                                        downloadStatus1.setPercent(percent);
                                        subscriber.onNext(downloadStatus1);
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
                DownloadObserver.newInstance().notifyObservers(downloadStatus);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                DownloadStatus downloadStatus = new DownloadStatus();
                downloadStatus.setDownloading(false);
                downloadStatus.setUrl(url);
                downloadStatus.setException(true);
                DownloadObserver.newInstance().notifyObservers(downloadStatus);
            }

            @Override
            public void onNext(DownloadStatus downloadStatus) {
                downloadStatus.setDownloading(true);
                downloadStatus.setUrl(url);
                DownloadObserver.newInstance().notifyObservers(downloadStatus);
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
}
