package com.bwei.ssp.multithreading.utlis;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * Created by lenovo on 2018/2/22.
 */

public class DownloadUtil {

    private OnDownloadListener onDownloadListener;
    private DownloadHttpTool mDownloadHttpTool;

    private int fileSize;
    private int downloadedSize = 0;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            int length = msg.arg1;
            synchronized (this) {//加锁保证已下载的正确性
                downloadedSize += length;
            }
            if (onDownloadListener != null) {
                onDownloadListener.downloadProgress(downloadedSize);
            }
            if (downloadedSize >= fileSize) {
                mDownloadHttpTool.compelete();
                if (onDownloadListener != null) {
                    onDownloadListener.downloadEnd();
                }
            }
        }

    };
    public DownloadUtil(int threadCount, String filePath, String filename,
                        String urlString, Context context) {

        mDownloadHttpTool = new DownloadHttpTool(threadCount, urlString,
                filePath, filename, context, mHandler);
    }


    public void start() {
        new AsyncTask<Void,Void,Void>() {

            @Override
            protected Void doInBackground(Void... arg0) {
                // TODO Auto-generated method stub
                mDownloadHttpTool.ready();
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                fileSize = mDownloadHttpTool.getFileSize();
                downloadedSize = mDownloadHttpTool.getCompeleteSize();
                Log.w("Tag", "downloadedSize::" + downloadedSize);
                if (onDownloadListener != null) {
                    onDownloadListener.downloadStart(fileSize);
                }
                mDownloadHttpTool.start();
            }
        }.execute();
    }

    public void pause() {
        mDownloadHttpTool.pause();
    }

    public void delete(){
        mDownloadHttpTool.delete();
    }

    public void reset(){
        mDownloadHttpTool.delete();
        start();
    }


    public void setOnDownloadListener(OnDownloadListener onDownloadListener) {
        this.onDownloadListener = onDownloadListener;
    }




    public interface OnDownloadListener{
        public void downloadStart(int fileSize); //记录下载的位置

        public void downloadProgress(int downloadedSize);//记录当前所有线程下总和

        public void downloadEnd(); //记录结束的位置
    }
}
