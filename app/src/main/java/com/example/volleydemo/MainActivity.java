package com.example.volleydemo;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.LruCache;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private TextView mShow;
    private ImageView mShowImg;
    private ImageView mLoaderImg;
    private NetworkImageView mNetImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mShow = findViewById(R.id.tv_show);
        mShowImg = findViewById(R.id.img_show);
        mLoaderImg = findViewById(R.id.img_loader);
        mNetImg = findViewById(R.id.img_net);
        requst();
    }

    private void requst() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://www.baidu.com";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("","");
                return map;
            }
        };

        String urlImg = "https://gss1.bdstatic.com/-vo3dSag_xI4khGkpoWK1HF6hhy/baike/s%3D220/sign=571122a7b07eca8016053ee5a1229712/8d5494eef01f3a29c8f5514a9925bc315c607c71.jpg";
        ImageRequest imageRequest = new ImageRequest(urlImg, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                mShowImg.setImageBitmap(response);
            }
        }, 0, 0, ImageView.ScaleType.FIT_CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mShow.setText(error.getMessage());
            }
        });
        queue.add(imageRequest);
        ImageLoader imageLoader = new ImageLoader(queue, new ImageLoader.ImageCache() {

            private final LruCache<String, Bitmap> mCache = new LruCache<String, Bitmap>(20);

            @Override
            public Bitmap getBitmap(String url) {
                return mCache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                mCache.put(url, bitmap);
            }
        });
        imageLoader.get(urlImg, ImageLoader.getImageListener(mLoaderImg, R.mipmap.ic_launcher, R.mipmap.ic_launcher));
        mNetImg.setImageUrl(urlImg, imageLoader);
    }
}
