package com.cah.paletteview.view;

import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.PointF;

import java.util.LinkedList;

/**
 * Created by chengaihua on 2017/10/25.
 */

public class DrawUtil {

    public static void drawAllShapeInfo(Canvas canvas, LinkedList<ShapeInfo> shapeInfoList) {
        for (int i = 0; i < shapeInfoList.size(); i++) {
            ShapeInfo shapeInfo = shapeInfoList.get(i);
            if (shapeInfo != null && shapeInfo.pointList.size() > 1)
                drawShapeInfo(canvas, shapeInfo);

        }
    }

    public static void drawShapeInfo(Canvas canvas, ShapeInfo shapeInfo) {
        switch (shapeInfo.type) {
            case 0://贝塞尔
                drawBezier(canvas, shapeInfo);
                break;

        }
    }

    public static void drawBezier(Canvas canvas, ShapeInfo shapeInfo) {
        Path path = new Path();
        path.moveTo(shapeInfo.pointList.get(0).x, shapeInfo.pointList.get(0).y);
        for (int i = 1; i < shapeInfo.pointList.size(); i += 2) {
            PointF cp = shapeInfo.pointList.get(i);
            PointF p2 = shapeInfo.pointList.get(i + 1);
            path.quadTo(cp.x, cp.y, p2.x, p2.y);
        }

        canvas.drawPath(path, shapeInfo.paint);

    }
}
