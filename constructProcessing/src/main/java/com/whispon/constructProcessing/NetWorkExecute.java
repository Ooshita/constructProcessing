package com.whispon.constructProcessing;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by noriakioshita on 2016/09/04.
 */
public class NetWorkExecute extends AsyncTask<String, Void, String> {

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // doInBackground前処理
    }
    private OnResult onResult;
    private OnResult onError;
    private View view;
    public NetWorkExecute(OnResult onResult, View view) {
        this.onResult = onResult;
        this.view = view;
    }
    public NetWorkException exception;

    @Override
    protected String doInBackground(String... param) {
        HttpURLConnection connect = null;
        URL url = null;
        String result = null;

        try {
            url = new URL(param[0]);
            // 接続用オブジェクトの作成
            connect = (HttpURLConnection)url.openConnection();
            //restTypeでgetやputを分ける
            connect.setRequestMethod("GET");
            // リダイレクトを自動でしない設定
            connect.setInstanceFollowRedirects(false);
            // URL接続からデータを読み取る場合はtrue
            connect.setDoInput(true);
            // URL接続にデータを書き込まない場合はfalse
            connect.setDoOutput(false);
            // 接続
            connect.connect();
            result = InputStreamToString(connect.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            exception = new NetWorkException(e);
        }
        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        // doInBackground後処理
        if(result != null) onResult.onSuccess(result);
        else onResult.onError(exception);

        if(this.view != null) this.view.setVisibility(View.GONE);
    }

    // InputStream -> String
    static String InputStreamToString(InputStream is) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        return sb.toString();
    }
}
