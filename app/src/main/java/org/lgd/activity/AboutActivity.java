package org.lgd.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import org.lgd.ioc.InjectUtils;
import org.lgd.ioc.annonation.InjectView;
import org.lgd.util.StatusBarUtil;

import lgd.org.R;

/**
 * 应用关于页面
 * Created by wukai on 2016/6/9.
 */
public class AboutActivity extends AppCompatActivity {

    @InjectView(value = R.id.id_about_toolbar)
    private Toolbar mToolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        InjectUtils.injectEvents(this);
        StatusBarUtil.setColor(this, Color.parseColor("#273339"), 0);
        setSupportActionBar(mToolbar);
    }

    /**
     * 点击事件
     *
     * @param v
     */
    public void onClickEvents(View v) {
        this.finish();
    }
}
