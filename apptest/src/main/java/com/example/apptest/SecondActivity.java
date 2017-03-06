package com.example.apptest;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created by wukai on 2016/6/22.
 */
public class SecondActivity extends AppCompatActivity {

    private Button mChooseBtn;
    private ImageView mChoosedImg;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initEvents();
    }

    private void initEvents() {
        mChooseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(intent, 22);
            }
        });
    }

    private void initViews() {
        mChooseBtn = (Button) this.findViewById(R.id.id_btn);
        mChoosedImg = (ImageView) this.findViewById(R.id.id_img);
    }

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);
        if (resultCode == 22) {
            Uri uri = data.getData();
            mChoosedImg.setImageURI(uri);
        }

    }
}
