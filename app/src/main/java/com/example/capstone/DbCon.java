package com.example.capstone;


import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class DbCon extends AppCompatActivity {
    private static String TAG = "phpquerytest";
    private static final String TAG_JSON="webnautes";
    private static final String TAG_ID = "id";
    private static final String TAG_S_ID = "s_id";
    private static final String TAG_STAR = "star";
    private static final String TAG_NAME = "name";
    private static final String TAG_ADDRESS ="address";
    private static final String TAG_CATEGORY ="category";
    private static final String TAG_TEL ="tel";
    private static final String TAG_LATITUDE ="latitude";
    private static final String TAG_LONGITUDE ="longitude";
    public static ArrayList<FranchiseDTO> Franchises = new ArrayList<>();
    static ArrayList<HashMap<String, String>> mArrayList = new ArrayList<>();;
    static String mJsonString;
    static ArrayList<String> DBString = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    public static class Search extends AsyncTask<String, Void, String>{
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d(TAG, "response ------ " + result);
            if (result == null){
            }
            else {
                mJsonString = result;
                showResult();
            }
        }
        public static void showResult(){
            try {
                System.out.println("과여어어어언?");
                JSONObject jsonObject = new JSONObject(mJsonString);
                JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject item = jsonArray.getJSONObject(i);
                    String id = item.getString(TAG_ID);
                    String name = item.getString(TAG_NAME);
                    String address = item.getString(TAG_ADDRESS);
                    String category = item.getString(TAG_CATEGORY);
                    String tel = item.getString(TAG_TEL);
                    String latitude = item.getString(TAG_LATITUDE);
                    String longitude = item.getString(TAG_LONGITUDE);
                    HashMap<String,String> hashMap = new HashMap<>();
                    Franchises.add(new FranchiseDTO(Integer.parseInt(id),name,address,category,tel,Double.parseDouble(latitude),Double.parseDouble(longitude)));
                    mArrayList.add(hashMap);
                }
                System.out.println("**************");
                System.out.println(Franchises);
                System.out.println("**************");
            } catch (JSONException e) {
                Log.d(TAG, "showResult : ", e);
            }
        }
        @Override
        protected String doInBackground(String... params) {
            String searchKeyword1 = params[0];
            String searchKeyword2 = params[1];
            System.out.println(searchKeyword1);
            System.out.println(searchKeyword2);
            String serverURL = "http://rtemd.suwon.ac.kr/capstone/search.php";
            System.out.println(serverURL);
            String postParameters = "name=" + searchKeyword1 + "&tel=" + searchKeyword2; // tel 쓰면 안되면 변수 새로만들어서 가능, city명 일치한거 하려면 인자 하나더받기
            System.out.println(postParameters);
            try {
                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();
                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();
                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);
                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuilder sb = new StringBuilder();
                String line;
                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }
                bufferedReader.close();
                return sb.toString().trim();
            } catch (Exception e) {
                Log.d(TAG, "InsertData: Error ", e);
                errorString = e.toString();
                return null;
            }
        }
    }

    public static class Zzim extends AsyncTask<String, Void, String>{
        String errorString = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d(TAG, "response ------ " + result);
            if (result == null){
            }
            else {
                mJsonString = result;
                showResult();
            }
        }
        public static void showResult(){
            try {
                System.out.println("믿고있었다구");
                System.out.println("111111");
                JSONObject jsonObject = new JSONObject(mJsonString);
                System.out.println("222222");
                JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
                System.out.println("333333");
                for(int i=0;i<jsonArray.length();i++){
                    System.out.println(i);
                    JSONObject item = jsonArray.getJSONObject(i);
                    DBString.add(item.getString(TAG_S_ID));
                    System.out.println(DBString.get(i));
                }
            } catch (JSONException e) {
                Log.d(TAG, "showResult : ", e);
            }
            /*finally {
                if(DBString.isEmpty()){
                    System.out.println("하얀별");
                    InfoPopupActivity.star.setChecked(false);
                }else{
                    System.out.println("검은별");
                    InfoPopupActivity.star.setChecked(true);
                }
            }*/
        }
        @Override
        protected String doInBackground(String... params) {
            String searchKeyword1 = params[0];
            String searchKeyword2 = params[1];
            String searchKeyword3 = params[2];
            System.out.println(searchKeyword1);
            System.out.println(searchKeyword2);
            System.out.println(searchKeyword3);
            String serverURL = "http://rtemd.suwon.ac.kr/capstone/0_zzim.php";
            System.out.println(serverURL);
            String postParameters = "member_id=" + searchKeyword1 + "&store_id=" + searchKeyword2 + "&function=" + searchKeyword3 ; // tel 쓰면 안되면 변수 새로만들어서 가능, city명 일치한거 하려면 인자 하나더받기
            System.out.println(postParameters);
            try {
                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();
                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();
                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);
                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuilder sb = new StringBuilder();
                String line;
                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }
                bufferedReader.close();
                return sb.toString().trim();
            } catch (Exception e) {
                Log.d(TAG, "InsertData: Error ", e);
                errorString = e.toString();
                return null;
            }
        }
    }




}
