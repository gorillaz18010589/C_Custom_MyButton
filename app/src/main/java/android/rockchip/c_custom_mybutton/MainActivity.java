package android.rockchip.c_custom_mybutton;
//https://blog.csdn.net/codebob/article/details/42916321?spm=1001.2101.3001.6650.2&utm_medium=distribute.pc_relevant.none-task-blog-2%7Edefault%7ECTRLIST%7ERate-2.pc_relevant_antiscan_v2&depth_1-utm_source=distribute.pc_relevant.none-task-blog-2%7Edefault%7ECTRLIST%7ERate-2.pc_relevant_antiscan_v2&utm_relevant_index=4
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    private MyButton myButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v("hank","MainActivity -> setContentView前");
        setContentView(R.layout.activity_main);
        Log.v("hank","MainActivity -> setContentView後");
        myButton = findViewById(R.id.myButton);
        Log.v("hank","MainActivity -> setContentView後 findViewById");
        setMyBtnView();
    }

    private void setMyBtnView() {
        myButton.setTextNormalPressedColor(R.color.white, R.color.black);
        myButton.setRadius(42);
        myButton.setBgNormalPressedColor(R.color.color_C72D2D, R.color.color_80C72D2D);
//        myButton.setButtonDrawableCenter(50,R.drawable.icon_start);
//        myButton.setButtonDrawableCenter(R.drawable.icon_start);
        Log.v("hank","MainActivity -> setContentView後 setMyBtnView後");
    }
}