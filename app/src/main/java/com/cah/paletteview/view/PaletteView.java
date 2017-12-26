package com.cah.paletteview.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.cah.paletteview.util.Build32Code;

import java.util.LinkedList;

/**
 * Created by chengaihua on 2017/10/4.
 */

public class PaletteView extends View {

    public static final String TAG = "CAH";

    private static final int MAX_PATH_SIZE = 100;

    private Paint paint;
    private Paint eraser;

    private Bitmap bufferBitmap;
    private Canvas bufferCanvas;
    private Path path;

    private ShapeInfo shapeInfo;

    private LinkedList<ShapeInfo> shapeInfoList;
    private LinkedList<ShapeInfo> removedInfoList;


    private float paintSize;
    private int paintColor;
    private float eraserSize;
    private int eraserColor;

    private float lastX;
    private float lastY;

    private boolean isClearStatus;

    private Mode mMode = Mode.BEZIER;

    public enum Mode {
        BEZIER, ERASER
    }


    public PaletteView(Context context) {
        this(context, null);
    }

    public PaletteView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setLayerType(View.LAYER_TYPE_HARDWARE, null);

        paintSize = 30;
        paintColor = Color.WHITE;

        eraserSize = 50;
        eraserColor = Color.GREEN;

        initPaint();
    }

    private void initPaint() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(paintColor);
        paint.setStrokeWidth(paintSize);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStyle(Paint.Style.STROKE);

        eraser = new Paint();
        eraser.setColor(eraserColor);
        eraser.setStrokeWidth(eraserSize);
        eraser.setStyle(Paint.Style.FILL);
        eraser.setStrokeCap(Paint.Cap.ROUND);
        eraser.setAntiAlias(true);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (bufferBitmap != null) {
            canvas.drawBitmap(bufferBitmap, 0, 0, null);
        }

//        Log.e("CAH", "onDraw: " + mMode);

        if (isClearStatus) {
            isClearStatus = false;
            return;
        }

        if (mMode == Mode.BEZIER) {
            if (shapeInfo != null) {
                switch (shapeInfo.type) {
                    case 0:
                        DrawUtil.drawBezier(canvas, shapeInfo);
                        break;
                }

            }
        }

        if (mMode == Mode.ERASER) {
            if (path != null)
                canvas.drawPath(path, eraser);
            DrawUtil.drawAllShapeInfo(canvas, shapeInfoList);

            return;
        }


    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction() & MotionEvent.ACTION_MASK;
        float x = event.getX();
        float y = event.getY();
        switch (action) {
            case MotionEvent.ACTION_DOWN:

                initCanvas();//将之前的线重新绘制上

                if (mMode == Mode.BEZIER) {//贝塞尔曲线
                    shapeInfo = new ShapeInfo();
                    shapeInfo.id = Build32Code.createGUID();
                    shapeInfo.type = 0;
                    shapeInfo.paint = paint;
                    shapeInfo.pointList.add(new PointF(x, y));
                }


                lastX = x;
                lastY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mMode == Mode.BEZIER) {//贝塞尔曲线
                    shapeInfo.pointList.add(new PointF(lastX, lastY));
                    shapeInfo.pointList.add(new PointF((lastX + x) / 2, (lastY + y) / 2));
                }

                if (mMode == Mode.ERASER) {//橡皮擦
                    path = new Path();
                    path.addCircle(x, y, eraserSize, Path.Direction.CW);

                    if (shapeInfoList != null && shapeInfoList.size() > 0) {
                        LinkedList<ShapeInfo> shapeInfos = EraserUtil.eraser(shapeInfoList, new PointF(x, y), eraserSize);
//                        shapeInfoList.clear();
//                        initCanvas();
//                        shapeInfoList.addAll(shapeInfos);
                    }
                }


                invalidate();

                lastX = x;
                lastY = y;

                break;
            case MotionEvent.ACTION_UP:
                if (mMode == Mode.BEZIER) {
                    if (shapeInfoList == null) {
                        shapeInfoList = new LinkedList<>();
                    } else if (shapeInfoList.size() == MAX_PATH_SIZE) {
                        shapeInfoList.remove(0);
                    }
                    shapeInfoList.add(shapeInfo);
                }

                break;
        }

        return true;
    }

    public Mode getMode() {
        return mMode;
    }

    public void setMode(Mode mode) {
        if (mMode != mode) {
            mMode = mode;
//            Log.e(TAG, "setMode: " + mMode);

            switch (mMode) {
                case BEZIER:
                    paint.setXfermode(null);
                    paint.setStrokeWidth(paintSize);
                    break;
                case ERASER:

                    break;
            }


        }
    }

    private void initCanvas() {
        if (bufferBitmap != null) {
            bufferBitmap.recycle();
            bufferBitmap = null;
        }
        bufferBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_4444);
        bufferCanvas = new Canvas(bufferBitmap);
        if (shapeInfoList != null && shapeInfoList.size() > 0) {
            DrawUtil.drawAllShapeInfo(bufferCanvas, shapeInfoList);
        }
    }

    public void clear() {
        if (bufferBitmap != null) {
            if (shapeInfoList != null)
                shapeInfoList.clear();

            if (removedInfoList != null)
                removedInfoList.clear();

            initCanvas();

            isClearStatus = true;

            invalidate();
        }
    }


    public void setPaintColor(String color) {
        paint.setColor(Color.parseColor(color));
    }

    public void setPaintWidth(float width) {
        paint.setStrokeWidth(width);
    }

    public void setEraserColor(String color) {
        eraser.setColor(Color.parseColor(color));
    }

    public void setEraserWidth(float width) {
        eraser.setStrokeWidth(width);
    }

}
