package org.lgd.activity;

import android.support.v7.app.AppCompatActivity;
import org.lgd.util.StatusBarUtil;

import lgd.org.R;

/**
 * Created by Jaeger on 16/2/14.
 *
 * Email: chjie.jaeger@gamil.com
 * GitHub: https://github.com/laobie
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        setStatusBar();
    }

    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.colorPrimary));
    }

}
