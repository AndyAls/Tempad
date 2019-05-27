package padd.qlckh.cn.tempad;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author Andy
 * @date 2018/9/27 14:55
 * Desc: 温度条
 */
public class TmepView extends View {

    private Paint mPaint;
    private int mWidth;
    private int mHeight;
    private float maxCount=100f;
    private float currentCount=20f;
    /**
     * 分段颜色
     */
    private static final int[] SECTION_COLORS = {Color.GREEN, Color.YELLOW, Color.RED};
    private Context mContext;
    private float selction;
    private Paint textPaint;
    private Path path;
    private Paint paint;


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
        selction = (int) (currentCount/maxCount);
        this.mContext=context;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(30);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setColor(mContext.getResources().getColor(R.color.theme_color));
        path = new Path();
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float round = mHeight/2-15;
        RectF rectProgressBg = new RectF(0, mHeight-25, mWidth, mHeight);
        LinearGradient shader = new LinearGradient(0, mHeight-25, mWidth, mHeight, SECTION_COLORS, null, Shader.TileMode.MIRROR);
        mPaint.setShader(shader);
        canvas.drawRoundRect(rectProgressBg, 15, 15, mPaint);
        selction = currentCount/maxCount;
        path.moveTo(mWidth*selction-10, mHeight-25);
        path.lineTo(mWidth*selction+10, mHeight-25);
        path.lineTo(mWidth*selction, mHeight-25-15);
        path.close();
        paint.setShader(shader);
        canvas.drawPath(path, paint);
        String text = (int)currentCount+"°c";
        canvas.drawText(text,mWidth*selction,mHeight-25-15-5,textPaint);

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
        if (heightSpecMode == MeasureSpec.AT_MOST || heightSpecMode == MeasureSpec.UNSPECIFIED) {
            mHeight = 70;
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
     * 设置最大的进度值
     * @param maxCount
     */
    public void setMaxCount(float maxCount) {
        this.maxCount = maxCount;
    }

    /***
     * 设置当前的进度值
     * @param currentCount
     */
    public void setCurrentCount(float currentCount) {
        if (currentCount > maxCount) {
            this.currentCount = maxCount - 5;
        }else if (currentCount<0f){
            currentCount=0f+5;
        }else {
            this.currentCount = currentCount;
        }
        invalidate();
    }

    public float getMaxCount() {
        return maxCount;
    }

    public float getCurrentCount() {
        return currentCount;
    }
}
