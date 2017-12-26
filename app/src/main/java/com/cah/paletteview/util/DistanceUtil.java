package com.cah.paletteview.util;

import android.graphics.PointF;

/**
 * Created by chengaihua on 2017/11/15.
 */

public class DistanceUtil {

    public static float getDistance(PointF centerPoint, PointF currentPoint) {
        return (int) (Math.sqrt((currentPoint.x - centerPoint.x) * (currentPoint.x - centerPoint.x)
                + (currentPoint.y - centerPoint.y) * (currentPoint.y - centerPoint.y)));

    }

}
