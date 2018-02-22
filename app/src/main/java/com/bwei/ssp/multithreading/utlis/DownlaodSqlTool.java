package com.bwei.ssp.multithreading.utlis;

import android.util.Log;

import com.bwei.ssp.multithreading.greenDao.User;
import com.bwei.ssp.multithreading.greenDao.UserDao;

import java.util.ArrayList;
import java.util.List;

import static com.bwei.ssp.multithreading.utlis.App.userDao;

/**
 * Created by lenovo on 2018/2/22.
 */

public class DownlaodSqlTool {
    public void insertInfos(List<DownloadInfo> infos){
        for (DownloadInfo info :infos){
            User user = new User(null, info.getThreadId(), info.getStartPos(), info.getEndPos(), info.getCompeleteSize(), info.getUrl());
           userDao.insert(user);
        }
    }

    public  List<DownloadInfo> getInfos(String urlstr){
        List<DownloadInfo> list = new ArrayList<>();
        //新创建一个存储信息的集合对象 保存得到下载的信息
        List<User> list1 = userDao.queryBuilder().where(UserDao.Properties.Url.eq(urlstr)).build().list();
         //查找数据，把符合下载网址的对象拿出来
        for (User user : list1) {
            DownloadInfo infoss = new DownloadInfo(
                    user.getThread_id(), user.getStart_pos(), user.getEnd_pos(),
                    user.getCompelete_size(), user.getUrl());
            Log.d("main-----", infoss.toString());
            list.add(infoss);
        }
        return list;
        //及时得到最新的状态
    }
    public void updataInfos(int threadId, int compeleteSize, String urlstr) {
        User user = userDao.queryBuilder()
                .where(UserDao.Properties.Thread_id.eq(threadId), UserDao.Properties.Url.eq(urlstr)).build().unique();
        user.setCompelete_size(compeleteSize);
        userDao.update(user);
        //当切换线程下载时就更新数据库的数据
    }
    public void delete(String url) {
        userDao.deleteAll();
        //下载完成后删除所有的数据
    }
}
