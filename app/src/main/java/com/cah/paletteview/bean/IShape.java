package com.cah.paletteview.bean;

import android.graphics.PointF;

import java.util.LinkedList;

/**
 * Created by chengaihua on 2017/11/15.
 */

public abstract class IShape {

    public float width;

    public abstract boolean isHit(PointF point);

    public abstract boolean isHit(PointF point, float er);

    public abstract LinkedList<IShape> breakShape(PointF point, float er);


}
