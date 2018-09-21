package com.example.pengyi.progressview;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
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
    progressView.setOnClickListener(new ProgressView.OnClickListener() {
      @Override
      public void onClick(View view) {
        Toast.makeText(MainActivity.this, "onClick !!!!!", Toast.LENGTH_SHORT).show();
      }
    });

    ProgressView progressView2 = findViewById(R.id.progress_view2);
    progressView2.setOnFinishListener(new ProgressView.OnFinishListener() {
      @Override
      public void onFinish() {
        Toast.makeText(MainActivity.this, "onfinish !!!!!", Toast.LENGTH_SHORT).show();
      }
    });
  }
}
