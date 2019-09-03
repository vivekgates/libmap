package org.unyde.mapintegrationlib.InternalNavigation.controller;

import android.graphics.PointF;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import org.unyde.mapintegrationlib.InternalNavigation.Cluster3DMap;
import org.unyde.mapintegrationlib.InternalNavigation.demo.SceneLoader;
import org.unyde.mapintegrationlib.InternalNavigation.math.Math3DUtils;
import org.unyde.mapintegrationlib.InternalNavigation.model.Camera;
import org.unyde.mapintegrationlib.InternalNavigation.view.ModelRenderer;
import org.unyde.mapintegrationlib.InternalNavigation.view.ModelSurfaceView;


public class TouchController {

    private static final String TAG = TouchController.class.getName();

    private static final int TOUCH_STATUS_ZOOMING_CAMERA = 1;
    private static final int TOUCH_STATUS_ROTATING_CAMERA = 4;
    private static final int TOUCH_STATUS_MOVING_WORLD = 5;

    private final ModelSurfaceView view;
    private final ModelRenderer mRenderer;

    private int pointerCount = 0;
    private float x1 = Float.MIN_VALUE;
    private float y1 = Float.MIN_VALUE;
    private float x2 = Float.MIN_VALUE;
    private float y2 = Float.MIN_VALUE;
    private float dx1 = Float.MIN_VALUE;
    private float dy1 = Float.MIN_VALUE;
    private float dx2 = Float.MIN_VALUE;
    private float dy2 = Float.MIN_VALUE;

    private float length = Float.MIN_VALUE;
    private float previousLength = Float.MIN_VALUE;
    private float currentPress1 = Float.MIN_VALUE;
    private float currentPress2 = Float.MIN_VALUE;

    private float rotation = 0;
    private int currentSquare = Integer.MIN_VALUE;

    private boolean isOneFixedAndOneMoving = false;
    private boolean fingersAreClosing = false;
    private boolean isRotating = false;

    private boolean gestureChanged = false;
    private boolean moving = false;
    private boolean simpleTouch = false;
    private long lastActionTime;
    private int touchDelay = -2;
    private int touchStatus = -1;

    private float previousX1;
    private float previousY1;
    private float previousX2;
    private float previousY2;
    private float[] previousVector = new float[4];
    private float[] vector = new float[4];
    private float[] rotationVector = new float[4];
    private float previousRotationSquare;

    public float two_finger_last_action;

    public float sensitivity = 0.2f;

    public TouchController(ModelSurfaceView view, ModelRenderer renderer) {
        super();
        this.view = view;
        this.mRenderer = renderer;
    }

    public synchronized boolean onTouchEvent(MotionEvent motionEvent) {
        // MotionEvent reports input details from the touch screen
        // and other input controls. In this case, you are only
        // interested in events where the touch position changed.

        switch (motionEvent.getActionMasked()) {
            case MotionEvent.ACTION_POINTER_UP:
                Log.d("Vivek", "Pointer Up");
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_HOVER_EXIT:
            case MotionEvent.ACTION_OUTSIDE:
                // this to handle "1 simple touch"
                if (lastActionTime > SystemClock.uptimeMillis() - 250) {
                    simpleTouch = true;
                } else {
                    gestureChanged = true;
                    touchDelay = 0;
                    lastActionTime = SystemClock.uptimeMillis();
                    simpleTouch = false;
                }
                moving = false;
                break;
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_HOVER_ENTER:
                Log.d(TAG, "Gesture changed...");
                gestureChanged = true;
                touchDelay = 0;
                lastActionTime = SystemClock.uptimeMillis();
                simpleTouch = false;
                break;
            case MotionEvent.ACTION_MOVE:
                moving = true;
                simpleTouch = false;
                touchDelay++;
                break;
            default:
                Log.w(TAG, "Unknown state: " + motionEvent.getAction());
                gestureChanged = true;
        }

        pointerCount = motionEvent.getPointerCount();

        if (pointerCount == 1) {
            x1 = motionEvent.getX();
            y1 = motionEvent.getY();
            if (gestureChanged) {
                Log.d(TAG, "x:" + x1 + ",y:" + y1);
                previousX1 = x1;
                previousY1 = y1;
            }
            dx1 = x1 - previousX1;
            dy1 = y1 - previousY1;
        } else if (pointerCount == 2) {
            two_finger_last_action = SystemClock.elapsedRealtime();
            x1 = motionEvent.getX(0);
            y1 = motionEvent.getY(0);
            x2 = motionEvent.getX(1);
            y2 = motionEvent.getY(1);
            vector[0] = x2 - x1;
            vector[1] = y2 - y1;
            vector[2] = 0;
            vector[3] = 1;
            float len = Matrix.length(vector[0], vector[1], vector[2]);
            vector[0] /= len;
            vector[1] /= len;

            // Log.d(TAG, "x1:" + x1 + ",y1:" + y1 + ",x2:" + x2 + ",y2:" + y2);
            if (gestureChanged) {
                previousX1 = x1;
                previousY1 = y1;
                previousX2 = x2;
                previousY2 = y2;
                System.arraycopy(vector, 0, previousVector, 0, vector.length);
            }

            dx1 = x1 - previousX1;
            dy1 = y1 - previousY1;
            dx2 = x2 - previousX2;
            dy2 = y2 - previousY2;

            rotationVector[0] = (previousVector[1] * vector[2]) - (previousVector[2] * vector[1]);
            rotationVector[1] = (previousVector[2] * vector[0]) - (previousVector[0] * vector[2]);
            rotationVector[2] = (previousVector[0] * vector[1]) - (previousVector[1] * vector[0]);
            len = Matrix.length(rotationVector[0], rotationVector[1], rotationVector[2]);
            rotationVector[0] /= len;
            rotationVector[1] /= len;
            rotationVector[2] /= len;

            previousLength = (float) Math
                    .sqrt(Math.pow(previousX2 - previousX1, 2) + Math.pow(previousY2 - previousY1, 2));
            length = (float) Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));

            currentPress1 = motionEvent.getPressure(0);
            currentPress2 = motionEvent.getPressure(1);
            rotation = 0;
            rotation = TouchScreen.getRotation360(motionEvent);
            currentSquare = TouchScreen.getSquare(motionEvent);
            if (currentSquare == 1 && previousRotationSquare == 4) {
                rotation = 0;
            } else if (currentSquare == 4 && previousRotationSquare == 1) {
                rotation = 360;
            }

            // gesture detection
            isOneFixedAndOneMoving = ((dx1 + dy1) == 0) != (((dx2 + dy2) == 0));
            fingersAreClosing = !isOneFixedAndOneMoving && (Math.abs(dx1 + dx2) < 10 && Math.abs(dy1 + dy2) < 10);
            isRotating = !isOneFixedAndOneMoving && (dx1 != 0 && dy1 != 0 && dx2 != 0 && dy2 != 0)
                    && rotationVector[2] != 0;
        }

        if (pointerCount == 1 && simpleTouch) {

            // SceneLoader scene = Cluster3DLocateMapFragment.Companion.getScene();
            SceneLoader scene = null;
            scene = Cluster3DMap.Companion.getScene();
          /*  if (Cluster3DMapFragment.Companion.getScene() != null) {
                scene = Cluster3DMapFragment.Companion.getScene();
            } else if (Cluster3DLocateMapFragment.Companion.getScene() != null) {
                scene = Cluster3DLocateMapFragment.Companion.getScene();
            }*/
            if(SystemClock.elapsedRealtime()-two_finger_last_action<2000) {

            }
            else
            {
                // scene.processTouch(x1, y1);
            }
        }

        int max = Math.max(mRenderer.getWidth(), mRenderer.getHeight());
        if (touchDelay > 1) {
            // INFO: Process gesture
            SceneLoader scene = null;
            scene = Cluster3DMap.Companion.getScene();
            //SceneLoader scene = Cluster3DLocateMapFragment.Companion.getScene();
            scene.processMove(dx1, dy1);
            Log.e("Tag", String.valueOf(dx1) + "," + String.valueOf(dy1));
            Camera camera = scene.getCamera();
            if (pointerCount == 1 && currentPress1 > 4.0f) {
            } else if (pointerCount == 1 /*&& !scene.block_pan_status*/) {
                touchStatus = TOUCH_STATUS_MOVING_WORLD;
                float[] up = new float[]{camera.xUp, camera.yUp, camera.zUp};
                // Log.d(TAG, "Translating camera (dx,dy) '" + dx1 + "','" + dy1 + "'...");
                //dx1 = (float)(dx1 / max * Math.PI * 2);
                //dy1 = (float)(dy1 / max * Math.PI * 2);
                //camera.translateCamera(dx1*sensitivity,dy1*sensitivity);
            /*double my_data_x = camera.xView - camera.xPos;
            double my_data_y = camera.yView - camera.yPos;
            double my_data_z = camera.zView - camera.zPos;
            float xLook = 0, yLook = 0, zLook = 0;
            xLook = camera.xView - camera.xPos;
            yLook = camera.yView - camera.yPos;
            zLook = camera.zView - camera.zPos;
            double  vlen = Matrix.length(xLook, yLook, zLook);
            xLook /= vlen;
            yLook /= vlen;
            zLook /= vlen;
            float xArriba = 0, yArriba = 0, zArriba = 0;
            xArriba = camera.xUp - camera.xPos;
            yArriba = camera.yUp - camera.yPos;
            zArriba = camera.zUp - camera.zPos;
            // Normalize the Right.
            vlen = Matrix.length(xArriba, yArriba, zArriba);
            xArriba /= vlen;
            yArriba /= vlen;
            zArriba /= vlen;

            // Right is the 3D vector that is equivalent to the 2D user X vector
            // In order to calculate the Right vector, we have to calculate the cross product of the
            // previously calculated vectors...

            // The cross product is defined like:
            // A x B = (a1, a2, a3) x (b1, b2, b3) = (a2 * b3 - b2 * a3 , - a1 * b3 + b1 * a3 , a1 * b2 - b1 * a2)
            float xRight = 0, yRight = 0, zRight = 0;
            xRight = (yLook * zArriba) - (zLook * yArriba);
            yRight = (zLook * xArriba) - (xLook * zArriba);
            zRight = (xLook * yArriba) - (yLook * xArriba);
            // Normalize the Right.
            vlen = Matrix.length(xRight, yRight, zRight);
            xRight /= vlen;
            yRight /= vlen;
            zRight /= vlen;*/
                //camera.xPos +=dx1*(camera.xView - camera.xPos);
                //camera.xView +=dx1*(camera.xView - camera.xPos);
                //camera.zPos += dx1*(camera.zView - camera.zPos)*.1f;
                //camera.xView +=dx1*(camera.xView - camera.xPos);
                //camera.zView += dx1*(camera.zView - camera.zPos);;
                //camera.translateCamera(dx1,dy1);
            /*camera.xPos +=dx1*xRight*-.01f;
            camera.xView +=dx1*xRight*-.01f;
            camera.yPos +=dx1*yRight*-.01f;
            camera.yView +=dx1*yRight*-.01f;
            camera.zPos +=dx1*zRight*-.01f;
            camera.zView +=dx1*zRight*-.01f;


            float[] right = new float[] {xRight,yRight,zRight};
            float[] forward=Math3DUtils.crossProduct(up,right); */

            /*camera.xPos +=dy1*forward[0]*.01f;
            camera.xView +=dy1*forward[0]*.01f;
            camera.yPos +=dy1*forward[1]*.01f;
            camera.yView +=dy1*forward[1]*.01f;
            camera.zPos +=dy1*forward[2]*.01f;
            camera.zView +=dy1*forward[2]*.01f;*/
            /*if(camera.xPos+dx1*.01f<=3 && camera.xPos+dx1*.01f>=-3)
            {
               camera.xPos +=dx1*.01f;
               camera.xView +=dx1*.01f;
            }
            if(camera.zPos+dy1*.01f<=2 && camera.zPos+dy1*.01f>=-9) {
               camera.zPos += dy1 * .01f;
               camera.zView += dy1 * .01f;
            }*/
                float y_pos = camera.yPos;
                System.out.print(y_pos);
                float xLookDirection = 0, yLookDirection = 0, zLookDirection = 0;

                xLookDirection = camera.xView - camera.xPos;
                yLookDirection = camera.yView - camera.yPos;
                zLookDirection = camera.zView - camera.zPos;

                // Normalize the direction.
                float dp = Matrix.length(xLookDirection, yLookDirection, zLookDirection);
                xLookDirection /= dp;
                yLookDirection /= dp;
                zLookDirection /= dp;
                float[] front = new float[]{xLookDirection, yLookDirection, zLookDirection};
                float[] right = Math3DUtils.crossProduct(up, front);

                float scroll_speed = camera.yPos* Cluster3DMap.Companion. getPan_value();
            /*
                float scroll_speed = camera.yPos*2.0f;
                if(Cluster3DMap.Companion.getMActionMode() == Cluster3DMap.IndoorMode.DIRECTION)
                {
                    scroll_speed = camera.yPos*2.0f;
                }
                if(Cluster3DMap.Companion.getMActionMode() == Cluster3DMap.IndoorMode.NAVIGATION)
                {
                    scroll_speed = camera.yPos*.5f;
                }*/

                //panning restriction

                //if (camera.xPos + dx1 * .01f <= 3 && camera.xPos + dx1 * .01f >= -3) {


                camera.xPos += (dx1 * right[0] * +.01f)*(scroll_speed);
                camera.xView += (dx1 * right[0] * +.01f)*(scroll_speed);
                camera.yPos += (dx1 * right[1] * +.01f)*(scroll_speed);
                camera.yView += (dx1 * right[1] * +.01f)*(scroll_speed);
                camera.zPos += (dx1 * right[2] * +.01f)*(scroll_speed);
                camera.zView += (dx1 * right[2] * +.01f)*(scroll_speed);
                // }
                float[] forward = new float[]{-right[2], right[1], right[0]};
                //if (camera.zPos + dy1 * .01f <= 6 && camera.zPos + dy1 * .01f >= -6) {
                camera.xPos += (dy1 * forward[0] * +.01f)*(scroll_speed);
                camera.xView += (dy1 * forward[0] * +.01f)*(scroll_speed);
                camera.yPos += (dy1 * forward[1] * +.01f)*(scroll_speed);
                camera.yView += (dy1 * forward[1] * +.01f)*(scroll_speed);
                camera.zPos += (dy1 * forward[2] * +.01f)*(scroll_speed);
                camera.zView += (dy1 * forward[2] * +.01f)*(scroll_speed);

                //}

                camera.setChanged(true);
            } else if (pointerCount == 2 /* && !scene.block_zoom_status*/) {



                //data3D.center(data3D.getPosition());
                if (fingersAreClosing) {
                    touchStatus = TOUCH_STATUS_ZOOMING_CAMERA;
                    float zoomFactor = (length - previousLength) / max * mRenderer.getFar();
                    Log.i(TAG, "Zooming '" + zoomFactor + "'...");
                    //camera.MoveCameraZ(zoomFactor*sensitivity);
                    float xLookDirection = 0, yLookDirection = 0, zLookDirection = 0;
                    double cam_distance = Math3DUtils.calculate_distance_two_point(new float[]{camera.xPos + xLookDirection * zoomFactor * 2 * sensitivity, camera.yPos + yLookDirection * zoomFactor * 2 * sensitivity, camera.zPos + zLookDirection * zoomFactor * 2 * sensitivity}, new float[]{camera.xView, camera.yView, camera.zView});
                    Log.i("Cam Distance", String.valueOf(cam_distance));
                    for(int i = 0;i<scene.created_pin.size();i++)
                    {
                        /// float[] prev_pos =new float[] {scene.created_pin.get(i).getPositionX(),scene.created_pin.get(i).getPositionY(),scene.created_pin.get(i).getPositionZ()};
                        //scene.created_pin.get(i).setScale(new float[]{(float) cam_distance/3.6f,(float)cam_distance/3.6f,(float)cam_distance/3.6f});
                        //scene.created_pin.get(i).centerAndScale(new float[]{(float) cam_distance/2.5f,(float)cam_distance/2.5f,(float)cam_distance/2.5f},prev_pos);
                        //scene.created_pin.get(i).centerAndScale((float)cam_distance/2.5f,prev_pos);
                        // float[] temp_position = scene.created_pin.get(i).getPosition();
                        // scene.created_pin.get(i).position =new float[]{temp_position[0]+(float)(cam_distance/9f),temp_position[1]+(float)(cam_distance/9f),temp_position[2]+(float)(cam_distance/9f)};
                    }




                    if ((cam_distance < 2.5f && zoomFactor < 0)) {
                        xLookDirection = camera.xView - camera.xPos;
                        yLookDirection = camera.yView - camera.yPos;
                        zLookDirection = camera.zView - camera.zPos;

                        // Normalize the direction.
                        float dp = Matrix.length(xLookDirection, yLookDirection, zLookDirection);
                        xLookDirection /= dp;
                        yLookDirection /= dp;
                        zLookDirection /= dp;
                        camera.xPos += xLookDirection * zoomFactor * 2 * sensitivity;
                        camera.yPos += yLookDirection * zoomFactor * 2 * sensitivity;
                        camera.zPos += zLookDirection * zoomFactor * 2 * sensitivity;
                        scene.camera_debug_value();
                        camera.setChanged(true);
                    } else if ((cam_distance > 2.5f) && (cam_distance < 18.5f)) {
                        // The look direction is the view minus the position (where we are).
                        xLookDirection = camera.xView - camera.xPos;
                        yLookDirection = camera.yView - camera.yPos;
                        zLookDirection = camera.zView - camera.zPos;

                        // Normalize the direction.
                        float dp = Matrix.length(xLookDirection, yLookDirection, zLookDirection);
                        xLookDirection /= dp;
                        yLookDirection /= dp;
                        zLookDirection /= dp;
                        camera.xPos += xLookDirection * zoomFactor * 2 * sensitivity;
                        camera.yPos += yLookDirection * zoomFactor * 2 * sensitivity;
                        camera.zPos += zLookDirection * zoomFactor * 2 * sensitivity;
                        scene.camera_debug_value();
                        camera.setChanged(true);
                    }
                    else if ((cam_distance >18.5f)&& zoomFactor >= 0) {
                        // The look direction is the view minus the position (where we are).
                        xLookDirection = camera.xView - camera.xPos;
                        yLookDirection = camera.yView - camera.yPos;
                        zLookDirection = camera.zView - camera.zPos;

                        // Normalize the direction.
                        float dp = Matrix.length(xLookDirection, yLookDirection, zLookDirection);
                        xLookDirection /= dp;
                        yLookDirection /= dp;
                        zLookDirection /= dp;
                        camera.xPos += xLookDirection * zoomFactor * 2 * sensitivity;
                        camera.yPos += yLookDirection * zoomFactor * 2 * sensitivity;
                        camera.zPos += zLookDirection * zoomFactor * 2 * sensitivity;
                        scene.camera_debug_value();
                        camera.setChanged(true);
                    }
                }
                if (isRotating) {
                    touchStatus = TOUCH_STATUS_ROTATING_CAMERA;
                    Log.i(TAG, "Rotating camera '" + Math.signum(rotationVector[2]) + "'...");
                    //camera.Rotate((float) (Math.signum(rotationVector[2]) / Math.PI) / 15);
                }
            }


        }

        previousX1 = x1;
        previousY1 = y1;
        previousX2 = x2;
        previousY2 = y2;

        previousRotationSquare = currentSquare;

        System.arraycopy(vector, 0, previousVector, 0, vector.length);

        if (gestureChanged && touchDelay > 1) {
            gestureChanged = false;
            Log.v(TAG, "Fin");
        }

        view.requestRender();

        return true;

    }
}

class TouchScreen {

    // these matrices will be used to move and zoom image
    private android.graphics.Matrix matrix = new android.graphics.Matrix();
    private android.graphics.Matrix savedMatrix = new android.graphics.Matrix();
    // we can be in one of these 3 states
    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int ZOOM = 2;
    private int mode = NONE;
    // remember some things for zooming
    private PointF start = new PointF();
    private PointF mid = new PointF();
    private float oldDist = 1f;
    private float d = 0f;
    private float newRot = 0f;
    private float[] lastEvent = null;

    public boolean onTouch(View v, MotionEvent event) {
        // handle touch events here
        ImageView view = (ImageView) v;
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                savedMatrix.set(matrix);
                start.set(event.getX(), event.getY());
                mode = DRAG;
                lastEvent = null;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                oldDist = spacing(event);
                if (oldDist > 10f) {
                    savedMatrix.set(matrix);
                    midPoint(mid, event);
                    mode = ZOOM;
                }
                lastEvent = new float[4];
                lastEvent[0] = event.getX(0);
                lastEvent[1] = event.getX(1);
                lastEvent[2] = event.getY(0);
                lastEvent[3] = event.getY(1);
                d = getRotation(event);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
                lastEvent = null;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mode == DRAG) {
                    matrix.set(savedMatrix);
                    float dx = event.getX() - start.x;
                    float dy = event.getY() - start.y;
                    matrix.postTranslate(dx, dy);
                } else if (mode == ZOOM) {
                    float newDist = spacing(event);
                    if (newDist > 10f) {
                        matrix.set(savedMatrix);
                        float scale = (newDist / oldDist);
                        matrix.postScale(scale, scale, mid.x, mid.y);
                    }
                    if (lastEvent != null && event.getPointerCount() == 3) {
                        newRot = getRotation(event);
                        float r = newRot - d;
                        float[] values = new float[9];
                        matrix.getValues(values);
                        float tx = values[2];
                        float ty = values[5];
                        float sx = values[0];
                        float xc = (view.getWidth() / 2) * sx;
                        float yc = (view.getHeight() / 2) * sx;
                        matrix.postRotate(r, tx + xc, ty + yc);
                    }
                }
                break;
        }

        view.setImageMatrix(matrix);
        return true;
    }

    /**
     * Determine the space between the first two fingers
     */
    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    /**
     * Calculate the mid point of the first two fingers
     */
    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    /**
     * Calculate the degree to be rotated by.
     *
     * @param event
     * @return Degrees
     */
    public static float getRotation(MotionEvent event) {
        double dx = (event.getX(0) - event.getX(1));
        double dy = (event.getY(0) - event.getY(1));
        double radians = Math.atan2(Math.abs(dy), Math.abs(dx));
        double degrees = Math.toDegrees(radians);
        return (float) degrees;
    }

    public static float getRotation360(MotionEvent event) {
        double dx = (event.getX(0) - event.getX(1));
        double dy = (event.getY(0) - event.getY(1));
        double radians = Math.atan2(Math.abs(dy), Math.abs(dx));
        double degrees = Math.toDegrees(radians);
        int square = 1;
        if (dx > 0 && dy == 0) {
            square = 1;
        } else if (dx > 0 && dy < 0) {
            square = 1;
        } else if (dx == 0 && dy < 0) {
            square = 2;
            degrees = 180 - degrees;
        } else if (dx < 0 && dy < 0) {
            square = 2;
            degrees = 180 - degrees;
        } else if (dx < 0 && dy == 0) {
            square = 3;
            degrees = 180 + degrees;
        } else if (dx < 0 && dy > 0) {
            square = 3;
            degrees = 180 + degrees;
        } else if (dx == 0 && dy > 0) {
            square = 4;
            degrees = 360 - degrees;
        } else if (dx > 0 && dy > 0) {
            square = 4;
            degrees = 360 - degrees;
        }
        return (float) degrees;
    }

    public static int getSquare(MotionEvent event) {
        double dx = (event.getX(0) - event.getX(1));
        double dy = (event.getY(0) - event.getY(1));
        int square = 1;
        if (dx > 0 && dy == 0) {
            square = 1;
        } else if (dx > 0 && dy < 0) {
            square = 1;
        } else if (dx == 0 && dy < 0) {
            square = 2;
        } else if (dx < 0 && dy < 0) {
            square = 2;
        } else if (dx < 0 && dy == 0) {
            square = 3;
        } else if (dx < 0 && dy > 0) {
            square = 3;
        } else if (dx == 0 && dy > 0) {
            square = 4;
        } else if (dx > 0 && dy > 0) {
            square = 4;
        }
        return square;
    }
}