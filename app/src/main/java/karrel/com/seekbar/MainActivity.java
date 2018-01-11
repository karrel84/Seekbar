package karrel.com.seekbar;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.karrel.mylibrary.RLog;

import karrel.com.seekbar.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements Seekbar.OnSeekbarListener {

    private ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mBinding.seekbar.setOnSeekbarListener(this);
        mBinding.seekbar.setThumbBackground(R.drawable.selector_button_drawer_control);

        mBinding.seekbar.setThumbTextColor(Color.WHITE);
    }

    @Override
    public void onChangedTick(int tick) {
//        RLog.d("tick : " + tick);
    }
}
