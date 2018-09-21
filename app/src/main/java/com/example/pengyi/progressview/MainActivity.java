package com.example.pengyi.progressview;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Created by pengyi on 2018/9/21.
 */

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ProgressView progressView = findViewById(R.id.progress_view);
        progressView.setOnFinishListner(new ProgressView.OnFinishListner() {
            @Override
            public void onFinish() {
                Toast.makeText(MainActivity.this,"finish !!!!!",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
