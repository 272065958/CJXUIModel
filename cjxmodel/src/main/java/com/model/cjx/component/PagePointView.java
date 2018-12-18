package com.model.cjx.component;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * create by cjx on 2018/12/17
 */
public class PagePointView extends View {
    private float selectXOff = 0f;
    private float xOffUnit = 0f;

    private int count = 0;
    private int pointSize = 0;
    private int pointSpace = 0;

    private Paint unSelectPaint = null;
    private Paint selectPaint = null;

    public PagePointView(Context context) {
        super(context);
    }

    public PagePointView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < count; i++) {
            canvas.drawCircle(pointSize / 2f + i * (pointSize + pointSpace) + 2f,
                    pointSize / 2f + 2f,
                    pointSize / 2f, unSelectPaint);
        }
        canvas.drawCircle(selectXOff, pointSize / 2f + 2f, pointSize / 2f - 1f, selectPaint);
    }

    /**
     * 初始化总页数和, 和标志的大小和素材
     */
    public void initPoint(int pointSize, int pointSpace, int count, int selectColor, int unSelectColor) {
        int unit = pointSize + pointSpace;
        int width = count * unit - pointSpace;
        xOffUnit = (float) unit;
        if (getLayoutParams() == null) {
            setLayoutParams(new ViewGroup.LayoutParams(width + 6, pointSize + 6));
        } else {
            ViewGroup.LayoutParams lp = getLayoutParams();
            lp.height = pointSize + 6;
            lp.width = width + count + 6;
            setLayoutParams(lp);
        }

        this.count = count;
        this.pointSize = pointSize;
        this.pointSpace = pointSpace;

        if (unSelectPaint != null) {
            return;
        }
        unSelectPaint = new Paint();
        unSelectPaint.setStyle(Paint.Style.STROKE);
        unSelectPaint.setStrokeWidth(1f);
        unSelectPaint.setColor(unSelectColor);
        unSelectPaint.setAntiAlias(true);

        selectPaint = new Paint();
        selectPaint.setStyle(Paint.Style.FILL);
        selectPaint.setColor(selectColor);
        selectPaint.setAntiAlias(true);
    }

    /**
     * 设置当前位置
     */
    void setPosition(int position) {
        float xOff = position * xOffUnit + pointSize / 2f + 2f;
        if(selectXOff != xOff){
            selectXOff = xOff;
        }
        invalidate();
    }
}
