package com.cah.paletteview.view;

import android.graphics.Paint;
import android.graphics.PointF;

import com.cah.paletteview.bean.IShape;

import java.util.LinkedList;

/**
 * Created by chengaihua on 2017/10/25.
 */

public class ShapeInfo {

    public String id;
    public int type;//0:贝塞尔曲线
    public Paint paint;
    public LinkedList<PointF> pointList = new LinkedList<>();

}
