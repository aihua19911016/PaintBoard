package com.cah.paletteview.bean;

import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;

import com.cah.paletteview.util.DistanceUtil;

import java.util.LinkedList;

/**
 * Created by chengaihua on 2017/11/15.
 */

public class Bezier extends IShape {

    public PointF startPoint;
    public PointF endPoint;
    public PointF controlPoint;

    public Bezier(PointF startPoint, PointF endPoint, PointF controlPoint, float width) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.controlPoint = controlPoint;
        this.width = width;
    }

    @Override
    public boolean isHit(PointF point) {
        return isHit(point, 0) || (DistanceUtil.getDistance(point, startPoint) == width / 2)
                || (DistanceUtil.getDistance(point, endPoint) == width / 2);
    }

    @Override
    public boolean isHit(PointF point, float er) {
        if (isCover(point, er))
            return true;

        RectF area = getArea(point, er);

        if (area != null) {
            if ((area.left != -1 && area.right != -1) || (area.top != -1 && area.bottom != -1)) {
                if (getPointF(point, er, area.left, area.right, area.top, area.bottom) != null)
                    return true;
            }
        }


        return (DistanceUtil.getDistance(point, startPoint) < (width / 2 + er))
                || (DistanceUtil.getDistance(point, endPoint) < (width / 2 + er));
    }

    @Override
    public LinkedList<IShape> breakShape(PointF point, float er) {
        LinkedList<IShape> shapeList = new LinkedList<>();
        LinkedList<String> pointList = new LinkedList<>();

        float squareLeft = point.x - width - er;
        float squareRight = point.x + width + er;
        float squareTop = point.y - width - er;
        float squareBottom = point.y + width + er;

        Bezier bez = new Bezier(startPoint, endPoint, controlPoint, 2);

        for (float x = squareLeft; x < squareRight; x++) {
            float t1 = 0, t2 = 0;
            float y1, y2;
            float a = startPoint.x - 2 * controlPoint.x + endPoint.x;
            float b = 2 * controlPoint.x - 2 * startPoint.x;
            float c = startPoint.x - x;

            PointF point1 = null;
            PointF point2 = null;

            if (a == 0) {
                t1 = -c / b;
                y1 = (float) (Math.pow((1 - t1), 2) * startPoint.y + 2 * t1 * (1 - t1) * controlPoint.y + Math.pow(t1, 2) * endPoint.y);
                point1 = new PointF(x, y1);
            } else {
                t1 = (float) ((-b + Math.sqrt(Math.pow(b, 2) - 4 * a * c)) / (2 * a));
                t2 = (float) ((-b - Math.sqrt(Math.pow(b, 2) - 4 * a * c)) / (2 * a));
                if ((t1 < 1 || t1 == 1) && (t1 > 0 || t1 == 0)) {
                    y1 = (float) (Math.pow((1 - t1), 2) * startPoint.y + 2 * t1 * (1 - t1) * controlPoint.y + Math.pow(t1, 2) * endPoint.y);
                    point1 = new PointF(x, y1);
                }

                if ((t2 < 1 || t2 == 1) && (t2 > 0 || t2 == 0)) {
                    y2 = (float) (Math.pow((1 - t2), 2) * startPoint.y + 2 * t2 * (1 - t2) * controlPoint.y + Math.pow(t2, 2) * endPoint.y);
                    point2 = new PointF(x, y2);
                }

            }

            float distance;
            if (point1 != null) {
                distance = DistanceUtil.getDistance(point, point1);
                if (bez.isHit(point1) && (Math.abs(distance - (width / 2 + er)) < 1) && distance > er) {
                    String pointStr = t1 + "," + point1.x + "," + point1.y + "," + DistanceUtil.getDistance(point1, point);
                    pointList.add(pointStr);
                }
            }

            if (point2 != null) {
                distance = DistanceUtil.getDistance(point, point2);
                if (bez.isHit(point2) && (Math.abs(distance - (width / 2 + er)) < 1) && distance > er) {
                    String pointStr = t2 + "," + point2.x + "," + point2.y + "," + DistanceUtil.getDistance(point2, point);
                    pointList.add(pointStr);
                }
            }

        }


        for (float y = squareTop; y < squareBottom; y++) {
            float t1, t2 = 0;
            float x1, x2;
            float a = startPoint.y - 2 * controlPoint.y + endPoint.y;
            float b = 2 * controlPoint.y - 2 * startPoint.y;
            float c = startPoint.y - y;

            PointF point1 = null, point2 = null;

            if (a == 0) {
                t1 = -c / b;
                x1 = (float) (Math.pow((1 - t1), 2) * startPoint.x + 2 * t1 * (1 - t1) * controlPoint.x + Math.pow(t1, 2) * endPoint.x);
                point1 = new PointF(x1, y);
            } else {
                t1 = (float) ((-b + Math.sqrt(Math.pow(b, 2) - 4 * a * c)) / (2 * a));
                t2 = (float) ((-b - Math.sqrt(Math.pow(b, 2) - 4 * a * c)) / (2 * a));
                if ((t1 < 1 || t1 == 1) && (t1 > 0 || t1 == 0)) {
                    x1 = (float) (Math.pow((1 - t1), 2) * startPoint.x + 2 * t1 * (1 - t1) * controlPoint.x + Math.pow(t1, 2) * endPoint.x);
                    point1 = new PointF(x1, y);
                }
                if ((t2 < 1 || t2 == 1) && (t2 > 0 || t2 == 0)) {
                    x2 = (float) (Math.pow((1 - t2), 2) * startPoint.x + 2 * t2 * (1 - t2) * controlPoint.x + Math.pow(t2, 2) * endPoint.x);
                    point2 = new PointF(x2, y);
                }

            }


            float distance;
            if (point1 != null) {
                distance = DistanceUtil.getDistance(point, point1);
                if (bez.isHit(point1) && (Math.abs(distance - (width / 2 + er)) < 1) && distance > er) {
                    String pointStr = t1 + "," + point1.x + "," + point1.y + "," + DistanceUtil.getDistance(point1, point);
                    pointList.add(pointStr);
                }
            }

            if (point2 != null) {
                distance = DistanceUtil.getDistance(point, point2);
                if (bez.isHit(point2) && (Math.abs(distance - (width / 2 + er)) < 1) && distance > er) {
                    String pointStr = t2 + "," + point2.x + "," + point2.y + "," + DistanceUtil.getDistance(point2, point);
                    pointList.add(pointStr);
                }
            }
        }


        if (pointList.size() > 0) {
            if ((DistanceUtil.getDistance(startPoint, point) < (er + width / 2) && !(DistanceUtil.getDistance(endPoint, point) < (er + width / 2)))
                    || (!((DistanceUtil.getDistance(startPoint, point) < (er + width / 2)) && DistanceUtil.getDistance(endPoint, point) < (er + width / 2)))) {//起始点打断
                String[] pointInfo = pointList.get(0).split(",");
                float t = Float.parseFloat(pointInfo[0]);
                PointF point1 = new PointF(Float.parseFloat(pointInfo[1]), Float.parseFloat(pointInfo[2]));
                float d = Float.parseFloat(pointInfo[3]);

                for (int i = 1; i < pointList.size(); i++) {
                    pointInfo = pointList.get(i).split(",");
                    if (Float.parseFloat(pointInfo[3]) > d) {
                        t = Float.parseFloat(pointInfo[0]);
                        point1 = new PointF(Float.parseFloat(pointInfo[1]), Float.parseFloat(pointInfo[2]));
                        d = Float.parseFloat(pointInfo[3]);

                    }
                }

                PointF cp = new PointF();
                if ((DistanceUtil.getDistance(startPoint, point) < (er + width / 2) && !(DistanceUtil.getDistance(endPoint, point) < (er + width / 2)))) {
                    cp.x = (1 - t) * controlPoint.x + t * endPoint.x;
                    cp.y = (1 - t) * controlPoint.y + t * endPoint.y;

                    shapeList.add(new Bezier(point1, endPoint, cp, width));

                    return shapeList;
                } else {
                    cp.x = (1 - t) * startPoint.x + t * controlPoint.x;
                    cp.y = (1 - t) * startPoint.y + t * controlPoint.y;

                    shapeList.add(new Bezier(startPoint, point1, cp, width));

                    return shapeList;
                }
            } else if (pointList.size() > 1) {
                String[] result1 = new String[4];
                String[] result2 = new String[4];

                float distance = 0;
                for (int k = 0; k < pointList.size() - 1; k++) {
                    PointF ePoint1 = new PointF(Float.parseFloat(pointList.get(k).split(",")[1]), Float.parseFloat(pointList.get(k).split(",")[2]));
                    for (int kk = k + 1; kk < pointList.size(); kk++) {
                        PointF ePoint2 = new PointF(Float.parseFloat(pointList.get(kk).split(",")[1]), Float.parseFloat(pointList.get(kk).split(",")[2]));
                        float d = DistanceUtil.getDistance(ePoint1, ePoint2);
                        if (d > distance) {
                            distance = d;
                            result1 = pointList.get(k).split(",");
                            result2 = pointList.get(kk).split(",");
                        }
                    }
                }

                if (result1[0] != null && result2[0] != null) {
                    String[] pointInfo1, pointInfo2;
                    float t1 = Float.parseFloat(result1[0]);
                    float t2 = Float.parseFloat(result2[0]);

                    PointF point1;
                    PointF point2;

                    if (t1 < t2) {
                        pointInfo1 = result1;
                        pointInfo2 = result2;
                    } else {
                        pointInfo1 = result2;
                        pointInfo2 = result1;
                    }


                    t1 = Float.parseFloat(pointInfo1[0]);
                    point1 = new PointF(Float.parseFloat(pointInfo1[1]), Float.parseFloat(pointInfo1[2]));

                    t2 = Float.parseFloat(pointInfo2[0]);
                    point2 = new PointF(Float.parseFloat(pointInfo2[1]), Float.parseFloat(pointInfo2[2]));


                    PointF cp1 = new PointF();
                    cp1.x = (1 - t1) * startPoint.x + t1 * controlPoint.x;
                    cp1.y = (1 - t1) * startPoint.y + t1 * controlPoint.y;

                    PointF cp2 = new PointF();
                    cp2.x = (1 - t2) * controlPoint.x + t2 * endPoint.x;
                    cp2.y = (1 - t2) * controlPoint.y + t2 * endPoint.y;

                    shapeList.add(new Bezier(startPoint, point1, cp1, width));
                    shapeList.add(new Bezier(point2, endPoint, cp2, width));

                    return shapeList;

                }


            }

        }

        return null;
    }


    public PointF getPointF(PointF point, float er, float startXVal, float endXVal, float startYVal, float endYVal) {
        PointF point1 = null, point2 = null;
        float distance;
        if (startPoint.x != controlPoint.x || startPoint.y != controlPoint.y) {
            for (float x = startXVal; x < endXVal; x++) {
                float t1, t2;
                float y1, y2;

                float a = (startPoint.x - 2 * controlPoint.x + endPoint.x);
                float b = 2 * controlPoint.x - 2 * startPoint.x;
                float c = startPoint.x - x;

                if (a != 0) {
                    t1 = (float) ((-b + Math.sqrt(Math.pow(b, 2) - 4 * a * c)) / (2 * a));
                    t2 = (float) ((-b - Math.sqrt(Math.pow(b, 2) - 4 * a * c)) / (2 * a));

                    if ((t1 < 1 || t1 == 1) && (t1 > 0 || t1 == 0)) {
                        y1 = (float) (Math.pow((1 - t1), 2) * startPoint.y + 2 * t1 * (1 - t1) * controlPoint.y + Math.pow(t1, 2) * endPoint.y);
                        point1 = new PointF(x, y1);
                    }

                    if ((t2 < 1 || t2 == 1) && (t2 > 0 || t2 == 0)) {
                        y2 = (float) (Math.pow((1 - t2), 2) * startPoint.y + 2 * t2 * (1 - t2) * controlPoint.y + Math.pow(t2, 2) * endPoint.y);
                        point2 = new PointF(x, y2);
                    }

                } else {
                    t1 = -c / b;
                    y1 = (float) (Math.pow((1 - t1), 2) * startPoint.y + 2 * t1 * (1 - t1) * controlPoint.y + Math.pow(t1, 2) * endPoint.y);
                    point1 = new PointF(x, y1);
                }

                if (point1 != null) {
                    distance = DistanceUtil.getDistance(point, point1);
                    if (distance < (width / 2 + er) || distance == (width / 2 + er)) {
                        return point1;
                    }
                }

                if (point2 != null) {
                    distance = DistanceUtil.getDistance(point, point2);
                    if (distance < (width / 2 + er) || distance == (width / 2 + er)) {
                        return point2;
                    }
                }

            }


            for (float y = startYVal; y < endYVal; y++) {
                float t1, t2;
                float x1, x2;

                float a = (startPoint.y - 2 * controlPoint.y + endPoint.y);
                float b = 2 * controlPoint.y - 2 * startPoint.y;
                float c = startPoint.y - y;

                if (a != 0) {
                    t1 = (float) ((-b + Math.sqrt(Math.pow(b, 2) - 4 * a * c)) / (2 * a));
                    t2 = (float) ((-b - Math.sqrt(Math.pow(b, 2) - 4 * a * c)) / (2 * a));
                    if ((t1 < 1 || t1 == 1) && (t1 > 0 || t1 == 0)) {
                        x1 = (float) (Math.pow((1 - t1), 2) * startPoint.x + 2 * t1 * (1 - t1) * controlPoint.x + Math.pow(t1, 2) * endPoint.x);
                        point1 = new PointF(x1, y);
                    }

                    if ((t2 < 1 || t2 == 1) && (t2 > 0 || t2 == 0)) {
                        x2 = (float) (Math.pow((1 - t2), 2) * startPoint.x + 2 * t2 * (1 - t2) * controlPoint.x + Math.pow(t2, 2) * endPoint.x);
                        point2 = new PointF(x2, y);
                    }

                } else {
                    t1 = -c / b;
                    x1 = (float) (Math.pow((1 - t1), 2) * startPoint.x + 2 * t1 * (1 - t1) * controlPoint.x + Math.pow(t1, 2) * endPoint.x);
                    point1 = new PointF(x1, y);
                }


                if (point1 != null) {
                    distance = DistanceUtil.getDistance(point, point1);
                    if (distance < (width / 2 + er) || distance == (width / 2 + er)) {
                        return point1;
                    }
                }

                if (point2 != null) {
                    distance = DistanceUtil.getDistance(point, point2);
                    if (distance < (width / 2 + er) || distance == (width / 2 + er)) {
                        return point2;
                    }
                }
            }
        } else {
            if (endPoint.x != startPoint.x) {
                float k = (endPoint.y - startPoint.y) / (endPoint.x - startPoint.x);
                float b = startPoint.y - k * startPoint.x;
                for (float x = startXVal; x < endXVal; x++) {
                    point1 = new PointF(x, (k * x + b));
                    distance = DistanceUtil.getDistance(point, point1);
                    if (distance < (width / 2 + er) || distance == (width / 2 + er)) {
                        return point1;
                    }
                }
            } else {
                for (float y = startYVal; y < endYVal; y++) {
                    point2 = new PointF(startPoint.x, y);

                    distance = DistanceUtil.getDistance(point, point2);
                    if (distance < (width / 2 + er) || distance == (width / 2 + er)) {
                        return point2;
                    }
                }
            }
        }

        return null;
    }

    public boolean isCover(PointF point, float er) {
        Triangle triangle = getTriangle();
        if (DistanceUtil.getDistance(triangle.startPoint, point) < (er + width / 2) && DistanceUtil.getDistance(triangle.endPoint, point) < (er + width / 2)
                && DistanceUtil.getDistance(triangle.topPoint, point) < (er + width / 2)) {
            return true;

        }
        return false;
    }

    /**
     * 获取矩形
     *
     * @return
     */
    public RectF getRect() {
        float t = 0.5f;
        float middleX = (float) (Math.pow((1 - t), 2) * startPoint.x + 2 * t * (1 - t) * controlPoint.x + Math.pow(t, 2) * endPoint.x);
        float middleY = (float) (Math.pow((1 - t), 2) * startPoint.y + 2 * t * (1 - t) * controlPoint.y + Math.pow(t, 2) * endPoint.y);

        RectF rectF = new RectF();
        rectF.left = (startPoint.x < endPoint.x ? startPoint.x : endPoint.x) < middleX ? (startPoint.x < endPoint.x ? startPoint.x : endPoint.x) : middleX;
        rectF.right = (startPoint.x > endPoint.x ? startPoint.x : endPoint.x) > middleX ? (startPoint.x > endPoint.x ? startPoint.x : endPoint.x) : middleX;
        rectF.top = (startPoint.y < endPoint.y ? startPoint.y : endPoint.y) < middleY ? (startPoint.y < endPoint.y ? startPoint.y : endPoint.y) : middleY;
        rectF.bottom = (startPoint.y > endPoint.y ? startPoint.y : endPoint.y) > middleY ? (startPoint.y > endPoint.y ? startPoint.y : endPoint.y) : middleY;

        return rectF;
    }

    public Triangle getTriangle() {
        PointF topPoint = new PointF();
        topPoint.x = (float) (Math.pow(0.5, 2) * startPoint.x + 2 * 0.5 * 0.5 * controlPoint.x + Math.pow(0.5, 2) * endPoint.x);
        topPoint.y = (float) (Math.pow(0.5, 2) * startPoint.y + 2 * 0.5 * 0.5 * controlPoint.y + Math.pow(0.5, 2) * endPoint.y);


        PointF centerPoint = new PointF();
        centerPoint.x = (startPoint.x + endPoint.x + topPoint.x) / 3;
        centerPoint.y = (startPoint.y + endPoint.y + topPoint.y) / 3;

        PointF startPoint_ = new PointF(), endPoint_ = new PointF(), topPoint_ = new PointF();
        float k1 = (width / 2) / (DistanceUtil.getDistance(centerPoint, startPoint) + (width / 2));
        startPoint_.x = (startPoint.x - k1 * centerPoint.x) / (1 - k1);
        startPoint_.y = (startPoint.y - k1 * centerPoint.y) / (1 - k1);

        float k2 = (width / 2) / (DistanceUtil.getDistance(centerPoint, endPoint) + (width / 2));
        endPoint_.x = (endPoint.x - k1 * centerPoint.x) / (1 - k2);
        endPoint_.y = (endPoint.y - k1 * centerPoint.y) / (1 - k2);

        float k3 = (width / 2) / (DistanceUtil.getDistance(centerPoint, topPoint) + (width / 2));
        topPoint_.x = (topPoint.x - k3 * centerPoint.x) / (1 - k3);
        topPoint_.y = (topPoint.y - k3 * centerPoint.y) / (1 - k3);

        return new Triangle(startPoint_, endPoint_, topPoint_);
    }

    public class Triangle {

        public PointF startPoint;
        public PointF endPoint;
        public PointF topPoint;

        public Triangle(PointF startPoint, PointF endPoint, PointF topPoint) {
            this.startPoint = startPoint;
            this.endPoint = endPoint;
            this.topPoint = topPoint;
        }
    }

    public RectF getArea(PointF point, float er) {
        float squareLeft = point.x - width - er;
        float squareRight = point.x + width + er;
        float squareTop = point.y - width - er;
        float squareBottom = point.y + width + er;

        RectF rectF = getRect();
        if (squareRight < rectF.left || squareLeft > rectF.right || squareBottom < rectF.top || squareTop > rectF.bottom) {
            return null;
        }

        RectF area = new RectF();

        float startX = -1, endX = -1;
        float startY = -1, endY = -1;

        if ((squareLeft < rectF.left || squareLeft == rectF.left) && (squareRight > rectF.left && (squareRight < rectF.right || squareRight == rectF.right))) {
            startX = rectF.left;
            endX = squareRight;
        } else if ((squareLeft > rectF.left) && (squareRight < rectF.right || squareRight == rectF.right)) {
            startX = squareLeft;
            endX = squareRight;
        } else if ((squareLeft > rectF.left && (squareLeft < rectF.right || squareLeft == rectF.right)) && squareRight > rectF.right) {
            startX = squareLeft;
            endX = rectF.right;
        }

        if ((squareTop < rectF.top || squareTop == rectF.top) && (squareBottom > rectF.top && (squareBottom < rectF.bottom || squareBottom == rectF.bottom))) {
            startY = rectF.top;
            endY = squareBottom;
        } else if (squareTop > rectF.top && (squareBottom < rectF.bottom || squareBottom == rectF.bottom)) {
            startY = squareTop;
            endY = squareBottom;
        } else if ((squareTop > rectF.top && (squareTop < rectF.bottom || squareTop == rectF.bottom)) && squareBottom > rectF.bottom) {
            startY = squareTop;
            endY = rectF.bottom;
        }


        area.left = startX;
        area.right = endX;
        area.top = startY;
        area.bottom = endY;

        return area;
    }

}
