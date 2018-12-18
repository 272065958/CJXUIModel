package com.model.cjx.component;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

/**
 * create by cjx on 2018/12/17
 */
public class RecyclerViewGridDidiver extends RecyclerView.ItemDecoration {
    private int dividerHeight;

    private Paint paint;

    public RecyclerViewGridDidiver(int dividerHeight, int dividerColor) {
        this.dividerHeight = dividerHeight;

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(dividerColor);
        paint.setStyle(Paint.Style.FILL);
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        //        val position = parent.getChildAdapterPosition(view)
//        val spanCount = getSpanCount(parent)
//        val itemCount = parent.adapter?.itemCount
        // 如果是最后一行, 不绘制底部

        // 如果是最后一列, 不绘制右边
    }

    private int getSpanCount(RecyclerView parent) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            return ((GridLayoutManager) layoutManager).getSpanCount();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            return ((StaggeredGridLayoutManager) layoutManager).getSpanCount();
        } else{
            return -1;
        }
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        drawRect(c, parent);
    }


    private void drawRect(Canvas c, RecyclerView parent) {
        int count = parent.getChildCount();
        for (int i=0; i<count; i++){
            View view = parent.getChildAt(i);
            if(view == null){
                continue;
            }
            RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams)view.getLayoutParams();

            // draw horizontal
            int leftH = view.getLeft() - lp.leftMargin;
            int rightH = view.getRight() + lp.rightMargin;
            int topH = view.getBottom() + lp.bottomMargin;
            int bottomH = topH + dividerHeight;
            c.drawRect(leftH, topH, rightH, bottomH, paint);

            // draw vertical
            int leftV = view.getRight() + lp.rightMargin;
            int rightV = leftV + dividerHeight;
            int topV = view.getTop() - lp.topMargin;
            int bottomV = view.getBottom() + lp.bottomMargin;
            c.drawRect(leftV, topV, rightV, bottomV, paint);
        }
    }
}
