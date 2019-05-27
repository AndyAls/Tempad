package padd.qlckh.cn.tempad.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import padd.qlckh.cn.tempad.R;

/**
 * @author Andy
 * @date 2018/9/30 11:06
 * @link {http://blog.csdn.net/andy_l1}
 * Desc:    自定义温度指示条
 */
public class TmepView extends View {

    private Paint mPaint;
    private int mWidth;
    private int mHeight;
    /**
     * 设置温度的最大范围
     */
    private float maxCount = 100f;
    /**
     * 设置当前温度
     */
    private float currentCount = 20f;
    /**
     * 分段颜色
     */
    private static final int[] SECTION_COLORS = {Color.GREEN, Color.YELLOW, Color.RED};
    private Context mContext;

    private float selction;
    private Paint textPaint;
    private Path path;
    private Paint paint;
    /**
     * 指针的宽高
     */
    private int mDefaultIndicatorWidth = dipToPx(10);
    private int mDefaultIndicatorHeight = dipToPx(8);
    /**
     * 圆角矩形的高度
     */
    private int mDefaultTempHeight = dipToPx(20);
    private int mDefaultTextSize = 30;
    private int textSpace = dipToPx(5);
    private RectF rectProgressBg;
    private LinearGradient shader;


    public TmepView(Context context) {
        this(context, null);
    }

    public TmepView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public TmepView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView(context);

    }

    private void initView(Context context) {
        this.mContext = context;
        //圆角矩形paint
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        //文本paint
        textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(mDefaultTextSize);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setColor(mContext.getResources().getColor(R.color.theme_color));
        //三角形指针paint
        path = new Path();
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //确定圆角矩形的范围,在TmepView的最底部,top位置为总高度-圆角矩形的高度
        rectProgressBg = new RectF(0, mHeight - mDefaultTempHeight, mWidth, mHeight);
        shader = new LinearGradient(0, mHeight - mDefaultTempHeight, mWidth, mHeight, SECTION_COLORS, null, Shader.TileMode.MIRROR);
        mPaint.setShader(shader);
        //绘制圆角矩形 mDefaultTempHeight / 2确定圆角的圆心位置
        canvas.drawRoundRect(rectProgressBg, mDefaultTempHeight / 2, mDefaultTempHeight / 2, mPaint);
        //当前位置占比
        selction = currentCount / maxCount;
        //绘制指针 指针的位置在当前温度的位置 也就是三角形的顶点落在当前温度的位置

        //定义三角形的左边点的坐标 x= tempView的宽度*当前位置占比-三角形的宽度/2  y=tempView的高度-圆角矩形的高度
        path.moveTo(mWidth * selction - mDefaultIndicatorWidth / 2, mHeight - mDefaultTempHeight);
        //定义三角形的右边点的坐标 = tempView的宽度*当前位置占比+三角形的宽度/2  y=tempView的高度-圆角矩形的高度
        path.lineTo(mWidth * selction + mDefaultIndicatorWidth / 2, mHeight - mDefaultTempHeight);
        //定义三角形的左边点的坐标 x= tempView的宽度*当前位置占比  y=tempView的高度-圆角矩形的高度-三角形的高度
        path.lineTo(mWidth * selction, mHeight - mDefaultTempHeight - mDefaultIndicatorHeight);
        path.close();
        paint.setShader(shader);
        canvas.drawPath(path, paint);
        //绘制文本
        String text = (int) currentCount + "°c";
        //确定文本的位置 x=tempViwe的宽度*当前位置占比 y=tempView的高度-圆角矩形的高度-三角形的高度-文本的间隙
        canvas.drawText(text, mWidth * selction, mHeight - mDefaultTempHeight - mDefaultIndicatorHeight - textSpace, textPaint);

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthSpecMode == MeasureSpec.EXACTLY || widthSpecMode == MeasureSpec.AT_MOST) {
            mWidth = widthSpecSize;
        } else {
            mWidth = 0;
        }
        //主要确定view的整体高度,渐变长条的高度+指针的高度+文本的高度+文本与指针的间隙
        if (heightSpecMode == MeasureSpec.AT_MOST || heightSpecMode == MeasureSpec.UNSPECIFIED) {
            mHeight = mDefaultTextSize+mDefaultTempHeight+mDefaultIndicatorHeight+textSpace;
        } else {
            mHeight = heightSpecSize;
        }
        setMeasuredDimension(mWidth, mHeight);
    }



    private int dipToPx(int dip) {
        float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f * (dip >= 0 ? 1 : -1));
    }


    /***
     * 设置最大的温度值
     * @param maxCount
     */
    public void setMaxCount(float maxCount) {
        this.maxCount = maxCount;
    }

    /***
     * 设置当前的温度
     * @param currentCount
     */
    public void setCurrentCount(float currentCount) {
        if (currentCount > maxCount) {
            this.currentCount = maxCount - 5;
        } else if (currentCount < 0f) {
            currentCount = 0f + 5;
        } else {
            this.currentCount = currentCount;
        }
        invalidate();
    }

    /**
     * 设置温度指针的大小
     *
     * @param width
     * @param height
     */
    public void setIndicatorSize(int width, int height) {

        this.mDefaultIndicatorWidth = width;
        this.mDefaultIndicatorHeight = height;
    }

    public void setTempHeight(int height) {
        this.mDefaultTempHeight = height;
    }

    public void setTextSize(int textSize) {
        this.mDefaultTextSize = textSize;
    }

    public float getMaxCount() {
        return maxCount;
    }

    public float getCurrentCount() {
        return currentCount;
    }
}
