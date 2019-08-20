package org.unyde.mapintegrationlib.InternalNavigation.indoornav.mapview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Picture;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewTreeObserver;

/**
 * MapLayer
 *
 * @author: onlylemi
 */
public class MapLayer extends MapBaseLayer {

    private static final String TAG = "MapLayer";
    private Boolean isStarted = false;
    private Picture image;
    private boolean hasMeasured;
    public Context context;

    public MapLayer(MapView mapView) {
        super(mapView);

        level = MAP_LEVEL;
    }

    public void setImage(Picture image) {
        this.image = image;

        if (mapView.getWidth() == 0) {
            ViewTreeObserver vto = mapView.getViewTreeObserver();
            vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                public boolean onPreDraw() {
                    if (!hasMeasured) {
                        initMapLayer();
                        hasMeasured = true;
                    }
                    return true;
                }
            });
        } else {
            initMapLayer();
        }
    }


    /**
     * init map image layer
     */
    private void initMapLayer() {
        float zoom = getInitZoom(mapView.getWidth(), mapView.getHeight(), image.getWidth(), image
                .getHeight())*6;
        Log.i(TAG, Float.toString(zoom));
        mapView.setCurrentZoom(zoom, 0, 0);

        float width = mapView.getWidth() - zoom * image.getWidth();
        float height = mapView.getHeight() - zoom * image.getHeight();

        mapView.imagewidth = zoom;
        float[] v = new float[9];
        mapView.currentMatrix.getValues(v);
        float tx = v[mapView.currentMatrix.MTRANS_X];
        float ty = v[mapView.currentMatrix.MTRANS_Y];
        //float sx = v[currentMatrix.MSCALE_X];
        // float sy = v[currentMatrix.MSCALE_Y];
        v[mapView.currentMatrix.MTRANS_X] =width/2;
        v[mapView.currentMatrix.MTRANS_Y] =height/2;
        mapView.currentMatrix.setValues(v);

        Log.w("WARNING", String.valueOf(width/2));

       /* if((ClusterMapFragment.Companion.isFirstTimeForMap()==false) || (ClusterLocateMapFragment.Companion.isFirstTimeForMap()==false))
        {
            // mapView.translate(width/2, height/2);
        }*/



    }

    /**
     * calculate init zoom
     *
     * @param viewWidth
     * @param viewHeight
     * @param imageWidth
     * @param imageHeight
     * @return
     */
    private float getInitZoom(float viewWidth, float viewHeight, float imageWidth,
                              float imageHeight) {
        float widthRatio = viewWidth / imageWidth;
        float heightRatio = viewHeight / imageHeight;

        Log.i(TAG, "widthRatio:" + widthRatio);
        Log.i(TAG, "widthRatio:" + heightRatio);

        if (widthRatio * imageHeight <= viewHeight) {
            return widthRatio;
            //return heightRatio;
        } else if (heightRatio * imageWidth <= viewWidth) {
            return heightRatio;
        }
        return 0;
    }

    @Override
    public void onTouch(MotionEvent event) {

    }

    @Override
    public void draw(Canvas canvas, Matrix currentMatrix, float currentZoom, float
            currentRotateDegrees) {
        canvas.save();
        canvas.setMatrix(currentMatrix);
        if (image != null) {
            canvas.drawPicture(image);

            //yaha likha tha
//            try {
//                SVG svg = SVG.getFromResource(mapView.getContext(),R.drawable.mapone_svg);
//                svg.setDocumentHeight(4096f);
//                svg.setDocumentWidth(4096f);
//                svg.renderToCanvas(canvas);
//            } catch (SVGParseException e) {
//                e.printStackTrace();
//            }
            //mark name
        }
        canvas.restore();
    }

    public Picture getImage() {
        return image;
    }
}
