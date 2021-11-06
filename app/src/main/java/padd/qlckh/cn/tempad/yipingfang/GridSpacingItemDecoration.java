package padd.qlckh.cn.tempad.yipingfang;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

/**
 * @author Andy
 * @date   2021/11/5 17:02
 * @link   {http://blog.csdn.net/andy_l1}
 * Desc:    GridSpacingItemDecoration.java
 */
public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {


    private int spanCount;
    private int spacing;
    private boolean includeEdge;

    public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
        this.spanCount = spanCount;
        this.spacing = spacing;
        this.includeEdge = includeEdge;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        int position = parent.getChildAdapterPosition(view);
        if (layoutManager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager.LayoutParams layoutParams =
                    (StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams();
            //0 左边 1 右边
            int spanIndex = layoutParams.getSpanIndex();
            if (includeEdge) {
                if (spanIndex == 0) {
                    outRect.left = spacing;
                } else {
                    outRect.left = spacing;
                    outRect.right = spacing;
                }
                if (position < spanCount) {
                    outRect.top = spacing;
                }
                // item bottom
                outRect.bottom = spacing;
            } else {

                if (spanIndex == 0) {
                    outRect.right = spacing;
                    outRect.left=0;
                }else if (spanIndex==1){
                    outRect.right=0;
                    outRect.left=0;
                }
                // item top
                if (position >= spanCount) {
                    outRect.top = spacing;
                }

            }


        } else {
            // item column
            int column = position % spanCount;

            if (includeEdge) {
                // spacing - column * ((1f / spanCount) * spacing)
                outRect.left = spacing - column * spacing / spanCount;
                // (column + 1) * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount;
                // item column
                if (position < spanCount) {
                    outRect.top = spacing;
                }
                // item bottom
                outRect.bottom = spacing;
            } else {
                // column * ((1f / spanCount) * spacing)
                outRect.left = column * spacing / spanCount;
                // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount;
                // item top
                if (position >= spanCount) {
                    outRect.top = spacing;
                }
            }

        }

    }

}
