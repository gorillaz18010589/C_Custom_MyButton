package android.rockchip.c_custom_mybutton;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.widget.Button;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

@SuppressLint("AppCompatCustomView")
public class MyButton extends Button {
    public static int[] mNormalState = new int[]{};
    public static int[] mPressState = new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled};

    private int mRadius = 39;    //默認的圓角半徑

    private int mBgNormalColor = R.color.teal_200;  //未點按的按鈕背景顏色
    private int mBgPressedColor = R.color.teal_700; //點按後的按鈕背景顏色
    private int mTextNormalColor = R.color.white;   //未點按的文字顏色
    private int mTextPressedColor = R.color.black;  //點按後的文字顏色

    private int mDrawableMarginTextRight = 0;//drawableCenter的Icon,距離右邊文字多少距離
    private int mDrawableCenterIcon = 0;//drawableCenter的Icon
    private Context mContext;

    public MyButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initStyle(attrs);
        init();

        Log.v("hank", "MyButton(Context context, AttributeSet attrs)");
    }

    private void init() {
//        setGravity(Gravity.CENTER); //設定文字顯示在按鈕中間
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        setButtonDrawableCenter(0);
        Log.v("hank", "onDraw()");

    }

    @SuppressLint("NonConstantResourceId")
    private void initStyle(AttributeSet attrs) {
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.MyButton);//取得在屬性設置的屬性陣列
        int count = typedArray.getIndexCount(); //取得設定的屬性總數
        for (int i = 0; i < count; i++) {
            int attr = typedArray.getIndex(i); //取得屬性(int index)
            switch (attr) {
                case R.styleable.MyButton_mBgRadius:
                    mRadius = typedArray.getInteger(attr, 0);
                    break;
                case R.styleable.MyButton_mTextNormalColor:
                    mTextNormalColor = typedArray.getResourceId(attr, mTextNormalColor);
                    break;
                case R.styleable.MyButton_mTextPressedColor:
                    mTextPressedColor = typedArray.getResourceId(attr, mTextPressedColor);
                    break;
                case R.styleable.MyButton_mBgNormalColor:
                    mBgNormalColor = typedArray.getResourceId(attr, mBgNormalColor);
                    break;
                case R.styleable.MyButton_mBgPressedColor:
                    mBgPressedColor = typedArray.getResourceId(attr, mBgPressedColor);
                    break;
                case R.styleable.MyButton_mDrawableCenter:
                    mDrawableCenterIcon = typedArray.getResourceId(attr, mDrawableCenterIcon);
                    setButtonDrawableCenter(mDrawableMarginTextRight, mDrawableCenterIcon);
                    break;
            }
        }
        buildColorDrawableState();
        buildDrawableState();
        typedArray.recycle();
    }

    /**
     * 改變按鈕背景圓角,顏色
     */
    private void buildDrawableState() {
        float outRect[] = new float[]{mRadius, mRadius, mRadius, mRadius, mRadius, mRadius, mRadius, mRadius};

        //創建圓弧形狀
        RoundRectShape roundRectShape = new RoundRectShape(
                outRect               // float：一個包含 8 個半徑值的數組，用於外圓矩形。 前兩個浮點數用於左上角（其餘對順時針對應）。 對於外部矩形上沒有圓角，傳遞 null。
                , null          //RectF：一個 RectF，指定從內部矩形到外部矩形每一側的距離。 如果沒有內部，則傳遞 null。
                , null      //float：一個包含 8 個半徑值的數組，用於內部圓形矩形。 前兩個浮點數用於左上角（其餘對順時針對應）。 對於內部矩形上沒有圓角，傳遞 null。 如果 inset 參數為 null，則忽略此參數。
        );

        /*
         * 繪製原始形狀的 Drawable 對象。 ShapeDrawable 接受一個 Shape 對象並管理它在屏幕上的存在。
         * 如果沒有給出 Shape，那麼 ShapeDrawable 將默認為 RectShape。
         * 該對象可以在帶有 <shape> 元素的 XML 文件中定義。
         * */
        //創建點按下去的Drawable,並且設定背景顏色
        ShapeDrawable pressedShapeDrawable = new ShapeDrawable(roundRectShape);
        pressedShapeDrawable.getPaint().setColor(getResColor(mBgPressedColor));//設定背景顏色
        StateListDrawable stateListDrawable = new StateListDrawable();//創建狀態管理器
        stateListDrawable.addState(mPressState, pressedShapeDrawable);//加入管理

        //創建尚未按下去的Drawable,並且設定背景顏色
        ShapeDrawable normalDrawable = new ShapeDrawable(roundRectShape);
        normalDrawable.getPaint().setColor(ContextCompat.getColor(mContext, mBgNormalColor));
        stateListDrawable.addState(mNormalState, normalDrawable);
        //设置我们的背景，就是xml里面的selector
        setBackgroundDrawable(stateListDrawable);
    }

    /**
     * 建構按下去得反應與顏色
     * ColorStateList => 處理點案特效顏色的類
     * <selector xmlns:android="http://schemas.android.com/apk/res/android">
     * <item android:state_focused="true"
     * android:color="@color/sample_focused" />
     * <item android:state_pressed="true"
     * android:state_enabled="false"
     * android:color="@color/sample_disabled_pressed" />
     * <item android:state_enabled="false"
     * android:color="@color/sample_disabled_not_pressed" />
     * <item android:color="@color/sample_default" />
     * </selector>
     */
    private void buildColorDrawableState() {
        ColorStateList colorStateList = new ColorStateList(
                new int[][]{mPressState, mNormalState},
                new int[]{getResColor(mTextPressedColor), getResColor(mTextNormalColor)}
        );
        setTextColor(colorStateList); //設定selector,文字顏色,效果
    }

    /**
     * 設置圓角矩形
     *
     * @param radius
     */
    public void setRadius(int radius) {
        this.mRadius = radius;
        buildDrawableState();
    }

    /**
     * 設定按鈕尚未點按時文字顏色,及點按後的文字顏色
     *
     * @param normalColor  未點按時文字顏色
     * @param pressedColor 點按時文字顏色
     */
    public void setTextNormalPressedColor(@ColorRes int normalColor, @ColorRes int pressedColor) {
        mTextNormalColor = normalColor;
        mTextPressedColor = pressedColor;
        buildColorDrawableState();
    }

    /**
     * 設置按鈕背景顏色
     *
     * @param normalColor  設定位點按時按鈕背景顏色
     * @param pressedColor 設定點按時按鈕背景顏色
     */
    public void setBgNormalPressedColor(@ColorRes int normalColor, @ColorRes int pressedColor) {
        mBgNormalColor = normalColor;
        mBgPressedColor = pressedColor;
        buildDrawableState();
    }


    /**
     * 將Drawable圖片置中,marginTextRight
     *
     * @param marginTextRight 為Drawable距離右邊文字多少距離
     * @param drawableIcon    DrawableCenter的icon
     */
    public void setButtonDrawableCenter(int marginTextRight, @DrawableRes int drawableIcon) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mDrawableMarginTextRight = marginTextRight;
                Drawable drawable = mContext.getResources().getDrawable(drawableIcon);
                int width = getMeasuredWidth();
                int height = getMeasuredHeight();

                int txt_width = (int) (getTextSize() * getText().length());
                int txt_height = (int) (getLineCount() * getLineHeight());

                int img_width = drawable.getIntrinsicWidth();
                int img_height = drawable.getIntrinsicHeight();
                int content_height = txt_height + img_height + mDrawableMarginTextRight;
                int content_width = txt_width + img_width + mDrawableMarginTextRight;
                int padding_w = width / 2 - content_width / 2;
                int padding_h = height / 2 - content_height / 2;

                setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
                setPadding(padding_w, 0, 0, 0);
                setCompoundDrawablePadding(-padding_w);
                Log.v("hank","setButtonDrawableCenter()");
            }
        }, 50);
    }

    /**
     * 設定DrawableCenter圖案
     *
     * @param drawableIcon DrawableCenter的icon
     */
    public void setButtonDrawableCenter(@DrawableRes int drawableIcon) {
        mDrawableCenterIcon = drawableIcon;
        setButtonDrawableCenter(mDrawableMarginTextRight, mDrawableCenterIcon);
    }


    /**
     * 將R.color.xx => @ColorRes
     *
     * @param color R
     * @return
     */
    private int getResColor(@ColorRes int color) {
        return ContextCompat.getColor(mContext, color);
    }


}
