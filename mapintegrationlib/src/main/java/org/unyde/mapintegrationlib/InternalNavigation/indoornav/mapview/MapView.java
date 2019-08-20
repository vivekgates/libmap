package org.unyde.mapintegrationlib.InternalNavigation.indoornav.mapview;


import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;

/**
 * MapView
 *
 * @author: onlylemi
 */
public class MapView extends SurfaceView implements SurfaceHolder.Callback {

    private static final String TAG = "MapView";

    private SurfaceHolder holder;
    private MapViewListener mapViewListener = null;
    private boolean isMapLoadFinish = false;
    private List<MapBaseLayer> layers; // all layers
    private MapLayer mapLayer;

    private Canvas canvas;

    private float minZoom = 1.0f;
    private float maxZoom = 3.0f;

    private PointF startTouch = new PointF();
    private PointF mid = new PointF();

    private Matrix saveMatrix = new Matrix();
    public Matrix currentMatrix = new Matrix();
    private float currentZoom = 1.0f;
    private float saveZoom = 0f;
    private float currentRotateDegrees = 0.0f;
    private float saveRotateDegrees = 0.0f;

    private static final int TOUCH_STATE_NO = 0; // no touch
    private static final int TOUCH_STATE_SCROLL = 1; // scroll(one point)
    private static final int TOUCH_STATE_SCALE = 2; // scale(two points)
    private static final int TOUCH_STATE_ROTATE = 3; // rotate(two points)
    private static final int TOUCH_STATE_TWO_POINTED = 4; // two points touch
    private int currentTouchState = MapView.TOUCH_STATE_NO; // default touch state
    public float imagewidth = 0.0f;

    private float oldDist = 0, oldDegree = 0;
    private boolean isScaleAndRotateTogether = false;

    public MapView(Context context) {
        this(context, null);
    }

    public MapView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MapView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initMapView();
    }

    /**
     * init mapview
     */

    public void resetPosition(){

        float[] v = new float[9];
        float sx = v[currentMatrix.MSCALE_X];
        float sy = v[currentMatrix.MSCALE_Y];
        currentMatrix.getValues(v);
        v[currentMatrix.MTRANS_X] =0;//-(sx*4096)/2;
        v[currentMatrix.MTRANS_Y] =0;//-(sy*4096)/2;
        currentMatrix.setValues(v);

    }
    private void initMapView() {
        getHolder().addCallback(this);

        layers = new ArrayList<MapBaseLayer>() {
            @Override
            public boolean add(MapBaseLayer layer) {
                if (layers.size() != 0) {
                    if (layer.level >= this.get(this.size() - 1).level) {
                        super.add(layer);
                    } else {
                        for (int i = 0; i < layers.size(); i++) {
                            if (layer.level < this.get(i).level) {
                                super.add(i, layer);
                                break;
                            }
                        }
                    }
                } else {
                    super.add(layer);
                }
                return true;
            }
        };
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        this.holder = holder;
        refresh();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        this.holder = holder;
        refresh();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.e(TAG, "surfaceDestroyed: ");
    }

    /**
     * reload mapview
     */
    public void refresh() {
        try {
            if (holder != null) {
                canvas = holder.lockCanvas();
                synchronized (holder) {
                    canvas.drawColor(-1);
                    if (isMapLoadFinish) {
                        for (MapBaseLayer layer : layers) {
                            if (layer.isVisible) {
                                layer.draw(canvas, currentMatrix, currentZoom, currentRotateDegrees);
                            }
                        }
                    }
                }

            }
        } catch (Exception e) {

            Log.e(TAG + "HI", "" + canvas + "" + e.getMessage());
        } finally {

            if (canvas != null) {

                holder.unlockCanvasAndPost(canvas);
            }

        }
    }

    public void loadMap(Bitmap bitmap) {
        loadMap(MapUtils.getPictureFromBitmap(bitmap,getContext()));
    }

    /**
     * load map image
     *
     * @param picture
     */
    public void loadMap(final Picture picture) {
        isMapLoadFinish = false;

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (picture != null) {
                    if (mapLayer == null) {
                        mapLayer = new MapLayer(MapView.this);
                        // add map image layer
                        layers.add(mapLayer);
                    }
                    mapLayer.setImage(picture);
                    if (mapViewListener != null) {
                        // load map success, and callback
                        mapViewListener.onMapLoadSuccess();
                    }
                    isMapLoadFinish = true;
                    refresh();
                } else {
                    if (mapViewListener != null) {
                        mapViewListener.onMapLoadFail();
                    }
                }
            }
        }).start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isMapLoadFinish) {
            return false;
        }

        float newDist;
        float newDegree;

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                if (holder.getSurface().isValid()) {
                    saveMatrix.set(currentMatrix);
                    startTouch.set(event.getX(), event.getY());
                    currentTouchState = MapView.TOUCH_STATE_SCROLL;
                }

                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                if (event.getPointerCount() == 2) {
                    if (holder.getSurface().isValid()) {
                        saveMatrix.set(currentMatrix);
                        saveZoom = currentZoom;
                        saveRotateDegrees = currentRotateDegrees;
                        startTouch.set(event.getX(0), event.getY(0));
                        currentTouchState = MapView.TOUCH_STATE_TWO_POINTED;

                        mid = midPoint(event);
                        oldDist = distance(event, mid);
                        oldDegree = rotation(event, mid);
                    }

                }
                break;
            case MotionEvent.ACTION_UP:
                if (holder.getSurface().isValid()) {
                    if (withFloorPlan(event.getX(), event.getY())) {
//                    Log.i(TAG, event.getX() + " " + event.getY());
                        // layers on touch
                        for (MapBaseLayer layer : layers) {
                            layer.onTouch(event);
                        }
                    }
                    currentTouchState = MapView.TOUCH_STATE_NO;
                }

                break;
            case MotionEvent.ACTION_POINTER_UP:
                if (holder.getSurface().isValid()) {
                    currentTouchState = MapView.TOUCH_STATE_NO;
                }

                break;
            case MotionEvent.ACTION_MOVE:
                switch (currentTouchState) {
                    case MapView.TOUCH_STATE_SCROLL:
                        if (holder.getSurface().isValid()) {
                            currentMatrix.set(saveMatrix);
                            int test1[] = new int[2];
                            float[] v = new float[9];
                            currentMatrix.getValues(v);
                            float tx = v[currentMatrix.MTRANS_X];
                            float ty = v[currentMatrix.MTRANS_Y];
                            float sx = v[currentMatrix.MSCALE_X];
                            float sy = v[currentMatrix.MSCALE_Y];
                            float translate_x = this.getTranslationX();
                            float paddingLeft = this.getPaddingLeft();


                                if (((tx+(event.getX() - startTouch.x))<0) && ((tx+(event.getX() - startTouch.x))>-(sx*3500)) ){
                                    currentMatrix.postTranslate(event.getX() - startTouch.x, 0);
                                }
                            if (((ty+(event.getY() - startTouch.y))<sy*-800) && ((ty+(event.getY() - startTouch.y))>-(sy*3500)) ){
                                currentMatrix.postTranslate(0, event.getY() -
                                        startTouch.y);
                            }




                            Log.e("matric",currentMatrix.toString());

                            //Matrix{[1.5820312, 0.0, -2507.1785][0.0, 1.5820312, -2353.4722][0.0, 0.0, 1.0]}
                            //Matrix{[1.5820312, 0.0, -2835.874][0.0, 1.5820312, -2176.5542][0.0, 0.0, 1.0]}




                            Log.e("scale","sx "+(sx*4096)+" sy "+(sy*4096)+" drag"+" x "+tx+" y "+ty+" sx "+sx);

                            refresh();
                        }

                        break;
                    case MapView.TOUCH_STATE_TWO_POINTED:
                        if (holder.getSurface().isValid()) {
                            if (!isScaleAndRotateTogether) {
                                float x = oldDist;
                                float y = MapMath.getDistanceBetweenTwoPoints(event.getX(0),
                                        event.getY(0), startTouch.x, startTouch.y);
                                float z = distance(event, mid);
                                float cos = (x * x + y * y - z * z) / (2 * x * y);
                                float degree = (float) Math.toDegrees(Math.acos(cos));

                                if (degree < 120 && degree > 45) {
                                    oldDegree = rotation(event, mid);
                                    currentTouchState = MapView.TOUCH_STATE_ROTATE;
                                } else {
                                    oldDist = distance(event, mid);
                                    currentTouchState = MapView.TOUCH_STATE_SCALE;
                                }
                            } else {
                               /* currentMatrix.set(saveMatrix);
                                newDist = distance(event, mid);
                                newDegree = rotation(event, mid);

                                float rotate = newDegree - oldDegree;
                                float scale = newDist / oldDist;
                                if (scale * saveZoom < minZoom) {
                                    scale = minZoom / saveZoom;
                                } else if (scale * saveZoom > maxZoom) {
                                    scale = maxZoom / saveZoom;
                                }
                                currentZoom = scale * saveZoom;
                                currentRotateDegrees = (newDegree - oldDegree + currentRotateDegrees)
                                        % 360;
                                currentMatrix.postScale(scale, scale, mid.x, mid.y);
                                currentMatrix.postRotate(rotate, mid.x, mid.y);
                                refresh();*/
                            }
                        }

                        break;
                    case MapView.TOUCH_STATE_SCALE:
                        if (holder.getSurface().isValid()) {
                            currentMatrix.set(saveMatrix);
                            newDist = distance(event, mid);
                            float scale = newDist / oldDist;
                            if (scale * saveZoom < minZoom) {
                                scale = minZoom / saveZoom;
                            } else if (scale * saveZoom > maxZoom) {
                                scale = maxZoom / saveZoom;
                            }
                            currentZoom = scale * saveZoom;
                            currentMatrix.postScale(scale, scale, mid.x, mid.y);
                            refresh();
                        }

                        break;
                    case MapView.TOUCH_STATE_ROTATE:
                        if (holder.getSurface().isValid()) {
                           /* currentMatrix.set(saveMatrix);
                            newDegree = rotation(event, mid);
                            float rotate = newDegree - oldDegree;
                            currentRotateDegrees = (rotate + saveRotateDegrees) % 360;
                            currentRotateDegrees = currentRotateDegrees > 0 ? currentRotateDegrees :
                                    currentRotateDegrees + 360;
                            currentMatrix.postRotate(rotate, mid.x, mid.y);
                            refresh();*/
                        }

//                        Log.i(TAG, "rotate:" + currentRotateDegrees);
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * set mapview listener
     *
     * @param mapViewListener
     */
    public void setMapViewListener(MapViewListener mapViewListener) {
        this.mapViewListener = mapViewListener;
    }

    /**
     * convert coordinate of map to coordinate of screen
     *
     * @param x
     * @param y
     * @return
     */
    public float[] convertMapXYToScreenXY(float x, float y) {
        Matrix invertMatrix = new Matrix();
        float[] value = {x, y};
        currentMatrix.invert(invertMatrix);
        invertMatrix.mapPoints(value);
        return value;
    }

    /**
     * map is/not load finish
     *
     * @return
     */
    public boolean isMapLoadFinish() {
        return isMapLoadFinish;
    }

    /**
     * add layer
     *
     * @param layer
     */
    public void addLayer(MapBaseLayer layer) {
        if (layer != null) {
            layers.add(layer);
        }
    }

    /**
     * get all layers
     *
     * @return
     */
    public List<MapBaseLayer> getLayers() {
        return layers;
    }

    public void translate(float x, float y) {
        currentMatrix.postTranslate(x, y);
    }

    /**
     * set point to map center
     *
     * @param x
     * @param y
     */
    public void mapCenterWithPoint(float x, float y) {
        float[] goal = {x, y};
        currentMatrix.mapPoints(goal);

        float deltaX = getWidth() / 2 - goal[0];
        float deltaY = getHeight() / 2 - goal[1];
        currentMatrix.postTranslate(deltaX, deltaY);
    }

    public float getCurrentRotateDegrees() {
        return currentRotateDegrees;
    }

    /**
     * set rotate degrees
     *
     * @param degrees
     */
    public void setCurrentRotateDegrees(float degrees) {
        mapCenterWithPoint(getMapWidth() / 2, getMapHeight() / 2);
        setCurrentRotateDegrees(degrees, getWidth() / 2, getHeight() / 2);
    }

    /**
     * set rotate degrees
     *
     * @param degrees
     * @param x
     * @param y
     */
    public void setCurrentRotateDegrees(float degrees, float x, float y) {
        currentMatrix.postRotate(degrees - currentRotateDegrees, x, y);

        currentRotateDegrees = degrees % 360;
        currentRotateDegrees = currentRotateDegrees > 0 ? currentRotateDegrees :
                currentRotateDegrees + 360;
    }

    public float getCurrentZoom() {
        return currentZoom;
    }

    public boolean isScaleAndRotateTogether() {
        return isScaleAndRotateTogether;
    }

    /**
     * setting scale&rotate is/not together on touch
     *
     * @param scaleAndRotateTogether
     */
    public void setScaleAndRotateTogether(boolean scaleAndRotateTogether) {
        isScaleAndRotateTogether = scaleAndRotateTogether;
    }

    public void setMaxZoom(float maxZoom) {
        this.maxZoom = maxZoom;
    }

    public void setMinZoom(float minZoom) {
        this.minZoom = minZoom;
    }

    public void setCurrentZoom(float zoom) {
        setCurrentZoom(zoom, getWidth() / 2, getHeight() / 2);
    }

    public void setCurrentZoom(float zoom, float x, float y) {
        currentMatrix.postScale(zoom / this.currentZoom, zoom / this.currentZoom, x, y);
        this.currentZoom = zoom;
    }

    private PointF midPoint(MotionEvent event) {
        return MapMath.getMidPointBetweenTwoPoints(event.getX(0), event.getY(0)
                , event.getX(1), event.getY(1));
    }

    private float distance(MotionEvent event, PointF mid) {
        return MapMath.getDistanceBetweenTwoPoints(event.getX(0), event.getY(0)
                , mid.x, mid.y);
    }

    private float rotation(MotionEvent event, PointF mid) {
        return MapMath.getDegreeBetweenTwoPoints(event.getX(0), event.getY(0)
                , mid.x, mid.y);
    }

    /**
     * point is/not in floor plan
     *
     * @param x
     * @param y
     * @return
     */
    public boolean withFloorPlan(float x, float y) {
        float[] goal = convertMapXYToScreenXY(x, y);
        return goal[0] > 0 && goal[0] < mapLayer.getImage().getWidth() && goal[1] > 0
                && goal[1] < mapLayer.getImage().getHeight();
    }

    public float getMapWidth() {
        return mapLayer.getImage().getWidth();
    }

    public float getMapHeight() {
        return mapLayer.getImage().getHeight();
    }


}
