package com.cah.paletteview.view;

import android.graphics.PointF;
import android.nfc.Tag;
import android.util.Log;

import com.cah.paletteview.bean.Bezier;
import com.cah.paletteview.bean.IShape;
import com.cah.paletteview.util.Build32Code;
import com.orhanobut.logger.Logger;

import java.util.LinkedList;

import static com.cah.paletteview.view.PaletteView.TAG;

/**
 * Created by chengaihua on 2017/11/15.
 */

public class EraserUtil {

    public static LinkedList<ShapeInfo> eraser(LinkedList<ShapeInfo> shapeInfoList, PointF point, float er) {
        for (int i = 0; i < shapeInfoList.size(); i++) {
            ShapeInfo shapeInfo = shapeInfoList.get(i);


            if (shapeInfo.pointList.size() > 1) {
                switch (shapeInfo.type) {
                    case 0://贝塞尔曲线
                        if (breakBezier(shapeInfo, point, er) != null) {
                            LinkedList<ShapeInfo> infos = breakBezier(shapeInfo, point, er);
                            shapeInfoList.remove(shapeInfo);
                            for (int j = 0; j < infos.size(); j++) {
                                shapeInfoList.add(infos.get(j));
                            }

                        }
                        Log.e(TAG, "eraser: " + shapeInfoList.size());
                        return shapeInfoList;

                }
            }


        }

        return shapeInfoList;
    }

    private static LinkedList<ShapeInfo> breakBezier(ShapeInfo shapeInfo, PointF point, float er) {
        LinkedList<ShapeInfo> shapeInfoList = new LinkedList<>();

        float width = shapeInfo.paint.getStrokeWidth();

        LinkedList<PointF> pointList1 = new LinkedList<>();
        LinkedList<PointF> pointList2 = new LinkedList<>();


        boolean isHit = false;
        boolean isBreak = false;
        boolean isFirst = true;
        for (int i = 1; i < shapeInfo.pointList.size(); i += 2) {
            PointF prePoint = shapeInfo.pointList.get(i - 1);
            PointF currentPoint = shapeInfo.pointList.get(i);
            PointF nextPoint = shapeInfo.pointList.get(i + 1);

            Bezier bezier = new Bezier(prePoint, nextPoint, currentPoint, width);

            if (!bezier.isHit(point, er)) {
                if (isFirst) {
                    pointList1.add(bezier.startPoint);
                    pointList1.add(bezier.controlPoint);
                    if (i == shapeInfo.pointList.size() - 2) {
//                        Log.e(TAG, "不会有");
                        pointList1.add(bezier.endPoint);
//                        Logger.e(" 第一段 " + pointList1.toString());
                    }
                } else {
                    pointList2.add(bezier.startPoint);
                    pointList2.add(bezier.controlPoint);
                    if (i == shapeInfo.pointList.size() - 2) {
                        pointList2.add(bezier.endPoint);
//                        Logger.e(" 第二段 " + pointList1.toString());
                    }
                }
                continue;
            }

            isHit = true;

            if (bezier.isCover(point, er)) {
                continue;
            }

            LinkedList<IShape> beziers = bezier.breakShape(point, er);
            if (beziers != null && beziers.size() > 0) {
                isBreak = true;
                Bezier bezier0 = (Bezier) beziers.get(0);
                Bezier bezier1;
                if (beziers.size() == 1) {
                    if (bezier0.startPoint.x == bezier.startPoint.x && bezier0.startPoint.y == bezier.startPoint.y) {
                        pointList1.add(bezier0.startPoint);
                        pointList1.add(bezier0.controlPoint);
                        pointList1.add(bezier0.endPoint);
//                        Logger.e(" 第一段 " + pointList1.toString());
                    }

                    if (bezier0.endPoint.x == bezier.endPoint.x && bezier0.endPoint.y == bezier.endPoint.y) {
                        isFirst = false;
                        pointList2.add(bezier0.startPoint);
                        pointList2.add(bezier0.controlPoint);
                    }

                } else if (beziers.size() == 2) {
                    bezier1 = (Bezier) beziers.get(1);
                    pointList1.add(bezier0.startPoint);
                    pointList1.add(bezier0.controlPoint);
                    pointList1.add(bezier0.endPoint);
//                    Logger.e(" 第一段 " + info.pointList.toString());
                    pointList2.add(bezier1.startPoint);
                    pointList2.add(bezier1.controlPoint);
                }


            }
        }


//        if (isHit) {
//            Log.e(TAG, "命中:" + isHit);
//        }

        if (isBreak) {
//            Log.e(TAG, "打断:" + isBreak);
//            Log.e(TAG, "线段：" + shapeInfoList.size());
//            Logger.e(shapeInfo.pointList.toString());

            ShapeInfo info1 = new ShapeInfo();
            info1.id = Build32Code.createGUID();
            info1.type = shapeInfo.type;
            info1.paint = shapeInfo.paint;
            if (pointList1.size() != 0) {
                info1.pointList.addAll(pointList1);
                shapeInfoList.add(info1);
            }

            ShapeInfo info2 = new ShapeInfo();
            info2.id = Build32Code.createGUID();
            info2.type = shapeInfo.type;
            info2.paint = shapeInfo.paint;
            if (pointList2.size() != 0) {
                info2.pointList.addAll(pointList2);
                shapeInfoList.add(info2);
            }
            Log.e(TAG, "breakBezier: " + shapeInfoList.size());

            return shapeInfoList;

        } else {
            return null;
        }
    }

}
