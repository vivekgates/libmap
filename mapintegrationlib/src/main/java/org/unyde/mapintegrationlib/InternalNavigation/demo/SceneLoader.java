package org.unyde.mapintegrationlib.InternalNavigation.demo;

import android.net.Uri;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import org.unyde.mapintegrationlib.ApplicationContext;
import org.unyde.mapintegrationlib.InternalNavigation.Cluster3DMap;
import org.unyde.mapintegrationlib.InternalNavigation.android.ContentUtils;
import org.unyde.mapintegrationlib.InternalNavigation.animation.Animator;
import org.unyde.mapintegrationlib.InternalNavigation.collision.CollisionDetection;
import org.unyde.mapintegrationlib.InternalNavigation.indoornav.Marker_Internal_Nav;
import org.unyde.mapintegrationlib.InternalNavigation.io.IOUtils;
import org.unyde.mapintegrationlib.InternalNavigation.model.Camera;
import org.unyde.mapintegrationlib.InternalNavigation.model.Object3DData;
import org.unyde.mapintegrationlib.InternalNavigation.services.LoaderTask;
import org.unyde.mapintegrationlib.InternalNavigation.services.Object3DBuilder;
import org.unyde.mapintegrationlib.InternalNavigation.services.collada.ColladaLoaderTask;
import org.unyde.mapintegrationlib.InternalNavigation.services.stl.STLLoaderTask;
import org.unyde.mapintegrationlib.InternalNavigation.services.wavefront.WavefrontLoader;
import org.unyde.mapintegrationlib.InternalNavigation.services.wavefront.WavefrontLoaderTask;
import org.unyde.mapintegrationlib.InternalNavigation.view.ModelRenderer;
import org.unyde.mapintegrationlib.InternalNavigation.view.ModelSurfaceView;
import org.unyde.mapintegrationlib.util.Constants;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * This class loads a 3D scena as an example of what can be done with the app
 *
 * @author andresoviedo
 */
public class SceneLoader implements LoaderTask.Callback {

    /**
     * Default model color: yellow
     */
    private static float[] DEFAULT_COLOR = {1.0f, 1.0f, 0, 1.0f};
    /**
     * Parent component
     */
    protected final AppCompatActivity parent;
    /**
     * List of data objects containing info for building the opengl objects
     */
    private List<Object3DData> objects = new ArrayList<Object3DData>();

    public Object3DData scene_bg_plane;
    public List<Marker_Internal_Nav> marker;
    public List<Marker_Internal_Nav> marker_i_m_here;
    public Marker_Internal_Nav updated_i_m_here_marker = null;
    public String destination_floor_number = "";

    /**
     * Point of view camera
     */
    private Camera camera;
    /**
     * Whether to draw objects as wireframes
     */
    private boolean drawWireframe = false;
    /**
     * Whether to draw using points
     */
    private boolean drawingPoints = false;
    /**
     * Whether to draw bounding boxes around objects
     */
    private boolean drawBoundingBox = false;
    /**
     * Whether to draw face normals. Normally used to debug models
     */
    private boolean drawNormals = false;
    /**
     * Whether to draw using textures
     */
    private boolean drawTextures = true;
    /**
     * Light toggle feature: we have 3 states: no light, light, light + rotation
     */
    private boolean rotatingLight = true;
    /**
     * Light toggle feature: whether to draw using lights
     */
    private boolean drawLighting = true;
    /**
     * Animate model (dae only) or not
     */
    private boolean animateModel = true;
    /**
     * Draw skeleton or not
     */
    private boolean drawSkeleton = false;
    /**
     * Toggle collision detection
     */
    private boolean isCollision = false;
    /**
     * Toggle 3d anaglyph
     */
    private boolean isAnaglyph = false;
    /**
     * Object selected by the user
     */
    private Object3DData selectedObject = null;
    /**
     * Initial light position
     */
    private final float[] lightPosition = new float[]{0, 6, 0, 1};
    /**
     * Light bulb 3d data
     */
    private final Object3DData lightPoint = Object3DBuilder.buildPoint(lightPosition).setId("light");
    /**
     * Animator
     */
    private Animator animator = new Animator();
    /**
     * Did the user touched the model for the first time?
     */
    private boolean userHasInteracted;
    /**
     * time when model loading has started (for stats)
     */
    private long startTime;

    //vi
    // List<Object3DData> floor_model;
    Object3DData camera_view_to_object = null;
    Object3DData pin_marker = null;

    boolean pin_drop_anim_status = false;

    public boolean block_pan_status = false;
    public boolean block_zoom_status = false;

    float pin_drop_timer = 0, pin_drop_start_timer = 0;
    boolean pin_drop_timer_status = true;
    float camera_y_pos_anim = 0;

    float camera_anim_timer = 0, camera_anim_start_timer = 0;
    boolean camera_anim_timer_status = false;
    int paramType;

    boolean navigation_mode_status = false;

    public List<Object3DData> created_pin;
    public List<float[]> created_pin_pos;
    public List<Object3DData> created_pin_imhere;
    public ArrayList<String> distict_floor_array;


    /**
     * The file to load. Passed as input parameter
     */
    Uri paramUri;
    ModelSurfaceView gLView;
    private Callback callback;


    public boolean camera_animation1_status = false;
    public float camera_animation1_timer = 0;
    float[] target_camera_animation1_position;
    public float[] source_camera_position1;

    public boolean camera_animation2_status = false;
    public float camera_animation2_timer = 0;
    float[] target_camera_animation2_position;
    public float[] source_camera_position2;

    public float pin_anim_timer = 0;
    public boolean pin_anim_timer_status;

    public Object3DData selected_floor_object = null;


    public boolean camera_im_animation_played_status = false;

    List<Marker_Internal_Nav> path_pos_scene;
    public Object3DData src_pin;
    public Object3DData dest_pin = null;

    public float[] dest_pin_position;
    public Object3DData wave_circle1wave_circle1 = null, wave_circle2 = null, wave_circle3 = null;
    public boolean animate_foot_status = false;

    public float animate_foot_timer = 0;
    public float orientation = 0;

    public float initial_orientation;//= Float.parseFloat(Pref_manager.Companion.getClusterOrientation(MyApplication.Companion.getInstance().getApplicationContext()));

    public int path_pos_index = 0;

    public boolean third_person_camera_status = true;

    private final float[] mCurrentRotation = new float[16];

    public boolean isdeleted_marker = true;
    public Object3DData washroom_obj = null;
    public Object3DData compass_obj = null;
    public Object3DData dummy_data = null;
    Calendar c;
    String clusterId;


    public float[] marker_old_position = new float[]{0, 0, 0};
    public float[] marker_new_position = new float[]{0, 0, 0};
    public boolean marker_transition_status = false;
    public float imhere_transition_timer = 0;

    public SceneLoader(AppCompatActivity main) {
        this.parent = main;
    }

    public SceneLoader(AppCompatActivity main, Uri paramUri, int paramType, ModelSurfaceView gLView, Callback callback, String clusterId) {
        this.parent = main;
        this.paramUri = paramUri;
        this.paramType = paramType;
        this.gLView = gLView;
        this.callback = callback;
        // marker = new ArrayList<Marker_Internal_Nav>();
        // marker_i_m_here = new ArrayList<Marker_Internal_Nav>();
        this.clusterId = clusterId;

        // initial_orientation=initial_orientation;
        //  Log.e("sdf", "" + initial_orientation);
    }

    public void init() {
        //mSensorManager = (SensorManager) MyApplication.Companion.getINSTANCE().getApplicationContext().getSystemService(SENSOR_SERVICE);
        c = Calendar.getInstance();
        //lightPoint.centerAndScale(.01f);

        //  delete_object_by_class("pin_i_m_here");
        //   delete_object_by_class("pin");
        //   delete_object_by_class("parking");
        dot_node_list = new ArrayList<Object3DData>();
        navigation_mode_status = false;
        created_pin = new ArrayList<Object3DData>();
        created_pin_pos = new ArrayList<float[]>();
        distict_floor_array = new ArrayList<String>();

        created_pin_imhere = new ArrayList<Object3DData>();
        // Camera to show a point of view
        camera = new Camera();

        if (paramUri == null) {
            return;
        }


        startTime = SystemClock.uptimeMillis();

        //  Uri uri = parent.getParamUri();
        Log.i("Object3DBuilder", "Loading model " + paramUri + ". async and parallel..");
        if (paramUri.toString().toLowerCase().endsWith(".obj") || paramType == 0) {
            new WavefrontLoaderTask(parent, paramUri, this).execute();
        } else if (paramUri.toString().toLowerCase().endsWith(".stl") || paramType == 1) {
            Log.i("Object3DBuilder", "Loading STL object from: " + paramUri);
            new STLLoaderTask(parent, paramUri, this).execute();
        } else if (paramUri.toString().toLowerCase().endsWith(".dae") || paramType == 2) {
            Log.i("Object3DBuilder", "Loading Collada object from: " + paramUri);
            new ColladaLoaderTask(parent, paramUri, this).execute();
        }

        //dummy_data = Object3DBuilder.loadV5_bg(parent, Uri.parse("models/cube.obj"));
        //addObject(dummy_data);
    }


   /* public void setMarkerForNavigation(Integer floor) {
        this.marker = marker;
        try {
            ContentUtils.setThreadActivity(parent.getActivity());
            ContentUtils.provideAssets(parent.getActivity());
            Object3DData marker11 = null;
            if (floor == 0) {
                marker11 = Object3DBuilder.loadV5_bg(parent.getActivity(), Uri.parse("models/plane1.obj"));
            }
            if (floor == 1) {
                marker11 = Object3DBuilder.loadV5_bg(parent.getActivity(), Uri.parse("models/planeff.obj"));
            }
            if (floor == -1) {
                marker11 = Object3DBuilder.loadV5_bg(parent.getActivity(), Uri.parse("models/planelgf.obj"));
            }
            if (floor == -2 || floor == -3) {
                marker11 = Object3DBuilder.loadV5_bg(parent.getActivity(), Uri.parse("models/planeparking.obj"));
            }
            if (floor == 2) {
                marker11 = Object3DBuilder.loadV5_bg(parent.getActivity(), Uri.parse("models/planesf.obj"));
            }
            if (floor == 3) {
                marker11 = Object3DBuilder.loadV5_bg(parent.getActivity(), Uri.parse("models/planetf.obj"));
            }
            if (floor == 5) {
                marker11 = Object3DBuilder.loadV5_bg(parent.getActivity(), Uri.parse("models/planetopf.obj"));
            }

            //Object3DData marker1 = Object3DBuilder.loadV5(parent.getActivity(), Uri.parse("assets://assets/models/cube.obj"));
            marker11.centerAndScale(6.0f);
            marker11.setPosition(new float[]{0, .15f, -0.1f});
            marker11.setobjClass("name");
            addObject(marker11);


        } catch (Exception ex) {

            Log.e("Scene Loader", "setMarker: " + ex.getMessage());

        }
    }
*/

    public void compass_rotation(float angle) {


        try {
            float[] compass_position = new float[]{Constants.compass.getPositionX(), Constants.compass.getPositionY(), Constants.compass.getPositionZ()};

            float angle_rad = (float) Math.toRadians(angle);

            Constants.compass.rotation[1] = (float) angle;
            //Constants.compass.setRotation(new float[]{Constants.compass.getRotationX(),angle,Constants.compass.getRotationZ()});
            //float[] position = new float[]{(compass_position[0]*(float)Math.cos(angle_rad))-(compass_position[2]*(float)Math.sin(angle_rad)),compass_position[1],(compass_position[2]*(float)Math.cos(angle))+(compass_position[0]*(float)Math.sin(angle))};
            //Constants.compass.setPosition(position);
            Constants.compass.position[0] = (compass_position[0] * (float) Math.cos(angle_rad)) - (compass_position[2] * (float) Math.sin(angle_rad));
            Constants.compass.position[1] = .29f;
            Constants.compass.position[2] = (compass_position[2] * (float) Math.cos(angle)) + (compass_position[0] * (float) Math.sin(angle));

        } catch (Exception e) {

        }

    }


  /*  public void setMarker(List<Marker_Internal_Nav> marker, Integer floor) {

        this.marker = marker;
        try {
            ContentUtils.setThreadActivity(parent);
            ContentUtils.provideAssets(parent, clusterId);
            created_pin.clear();
            created_pin_pos.clear();
            *//*Object3DData obj41 = Object3DBuilder.buildplane3V4(Constants.char_byte_data.get('!'));
            obj41.setPosition(new float[]{.88f,.1f,.682f});
            obj41.setobjClass("name");
            obj41.setScale(new float[]{7.15f,.13f,7.15f});
            addObject(obj41);

            Object3DData obj42 = Object3DBuilder.buildplane3V4(Constants.char_byte_data.get('@'));
            obj42.setPosition(new float[]{-.81f,.1f,.72f});
            obj42.setobjClass("name");
            obj42.setScale(new float[]{7.15f,.13f,7.15f});
            addObject(obj42);

            Object3DData obj43 = Object3DBuilder.buildplane3V4(Constants.char_byte_data.get('#'));
            obj43.setPosition(new float[]{-.432f,.1f,-1.535f});
            obj43.setobjClass("name");
            obj43.setScale(new float[]{8.85f,.13f,5.15f});
            addObject(obj43);*//*

     *//*Object3DData marker11 = null;
            marker11 = Object3DBuilder.loadV5_bg(parent, Uri.parse("models/hall9n10_storename_plane.obj"));
            //marker11.setPosition(new float[]{-4.5f, .1f,0});
            marker11.setScale(new float[]{.175f,.175f,.175f});
            //marker11.centerAndScale(1f);
            marker11.setobjClass("name");
            addObject(marker11);

            Object3DData marker12 = null;
            marker12 = Object3DBuilder.loadV5_bg(parent, Uri.parse("models/hall11n12_storename_plane.obj"));
            marker12.setPosition(new float[]{0, .1f,0});
            marker12.setScale(new float[]{.2f,.5f,.2f});
            //marker11.centerAndScale(1f);
            marker12.setobjClass("name");
            addObject(marker12);

            Object3DData marker13 = null;
            marker13 = Object3DBuilder.loadV5_bg(parent, Uri.parse("models/hall14n15_storename_plane.obj"));
            marker13.setPosition(new float[]{0, .1f,0});
            marker13.setScale(new float[]{.2f,.5f,.2f});
            //marker11.centerAndScale(1f);
            marker13.setobjClass("name");
            addObject(marker13);
            *//*

            //  InputStream open =  ContentUtils.getInputStream(Uri.parse("models/h.jpg"));

            //  AssetManager assetManager = parent.getAssets();

            // InputStream istr = assetManager.open("models/h.jpg");


            //draw_texture(0.0f,0.0f,"HUSH PUPPIES",1f);

            //InputStream openp = MyApplication.Companion.getINSTANCE().getApplicationContext().getAssets().open(Uri.parse("models/character/P.jpg").toString());

            //Object3DData obj4 = Object3DBuilder.buildplaneV4(IOUtils.read(openp));
            //Object3DData obj4 = Object3DBuilder.buildplaneV4(Constants.char_byte_data.get('P'));
            //obj4.setPosition(new float[]{0,.5f,0});
            //addObject(obj4);
            //openp.close();

            *//*if (Pref_manager.Companion.getCheckInClusterStoreId(MyApplication.Companion.getInstance().getApplicationContext()).startsWith("011C00065")) {

                Object3DData marker11 = null;
                if (floor == 0) {
                    marker11 = Object3DBuilder.loadV5_bg(parent, Uri.parse("models/plane1.obj"));
                }
                if (floor == 1) {
                    marker11 = Object3DBuilder.loadV5_bg(parent, Uri.parse("models/planeff.obj"));
                }
                if (floor == -1) {
                    marker11 = Object3DBuilder.loadV5_bg(parent, Uri.parse("models/planelgf.obj"));
                }
                if (floor == -2 || floor == -3) {
                    marker11 = Object3DBuilder.loadV5_bg(parent, Uri.parse("models/planeparking.obj"));
                }
                if (floor == 2) {
                    marker11 = Object3DBuilder.loadV5_bg(parent, Uri.parse("models/planesf.obj"));
                }
                if (floor == 3) {
                    marker11 = Object3DBuilder.loadV5_bg(parent, Uri.parse("models/planetf.obj"));
                }
                if (floor == 5) {
                    marker11 = Object3DBuilder.loadV5_bg(parent, Uri.parse("models/planetopf.obj"));
                }
                marker11.centerAndScale(6.0f);
                marker11.setPosition(new float[]{0, .1f, -.2f});
                marker11.setobjClass("name");
                addObject(marker11);
            }
*//*
            // compass loaded  vivek

            *//*Object3DData obj53 = Object3DBuilder.loadV5_bg(parent, Uri.parse("models/path_arrow.obj"));
            obj53.centerAndScale(1);
            obj53.setColor(new float[]{1.0f, 1.0f, 1f, 1.0f});
            obj53.setId("compass1");
            addObject(obj53);

            compass_obj = obj53; *//*
     *//*Constants.popupbg= Object3DBuilder.buildplanepopupV4(Constants.char_byte_data.get("popup"));
            Constants.popupbg.setPosition(new float[]{0,.2f,0});
            Constants.popupbg.setobjClass("popup");
            Constants.popupbg.setId("popupbg");
            addObject(Constants.popupbg);*//*
            //Object3DData obj4 = Object3DBuilder.buildplane2V4(Constants.char_byte_data.get("hotspot_icon"));
            Constants.popupbg = Object3DBuilder.buildplane2V4(Constants.char_byte_data.get("pin_selected"));
            Constants.popupbg.setPosition(new float[]{0, .2f, 0});
            Constants.popupbg.setobjClass("pin_green");
            Constants.popupbg.setId("pin_green");
           // addObject(Constants.popupbg);

            Object3DData marker1111 = Object3DBuilder.loadV5_bg(parent, Uri.parse("models/environment_cylinder.obj"));
            //marker1111.setPosition(new float[]{0,50,0});
            marker1111.centerAndScale(0.001f);

            marker1111.setId("building");
            Constants.dbuilding = marker1111;
            addObject(marker1111);
            //marker1111.getMaterials().materials.get("lambert2SG").setD(.8f);
            //Object3DData marker1 = Object3DBuilder.loadV5(parent, Uri.parse("assets://assets/models/cube.obj"));

        *//*    if (marker.size() > 0) {
                for (int i = 0; i < marker.size(); i++) {
                    if (marker.get(i).getFloor_name().equalsIgnoreCase("N")) {
                        *//**//*Object3DData marker1 = Object3DBuilder.loadV5_bg(parent, Uri.parse("models/path_arrow.obj"));
                        marker1.centerAndScale(1);
                        marker1.setColor(new float[]{1.0f, 1.0f, 1f, 1.0f});
                        marker1.setId("compass1");
                        addObject(marker1);
                        created_pin.add(marker1);
                        created_pin.add(marker1);
                        created_pin_pos.add(new float[]{marker.get(i).getX(), marker.get(i).getY() - .1f, marker.get(i).getZ()});
*//**//*
                        Object3DData marker1_boundingbox = Object3DBuilder.loadV5_bg(parent, Uri.parse("models/storemarker_plane.obj"));
                        marker1_boundingbox.setPosition(new float[]{marker.get(i).getX(), marker.get(i).getY() - .1f, marker.get(i).getZ()});
                        marker1_boundingbox.setobjClass("pin_cube");
                        marker1_boundingbox.setId(marker.get(i).getSite_id());
                       // addObject(marker1_boundingbox);

                        *//**//*Object3DData marker1 = Object3DBuilder.loadV5_bg(parent, Uri.parse("models/storemarker.obj"));
                        marker1.setPosition(new float[]{marker.get(i).getX(), marker.get(i).getY() - .1f, marker.get(i).getZ()});
                        marker1.setobjClass("pin");
                        marker1.setId(marker.get(i).getSite_id() + "_marker");
                        addObject(marker1);*//**//*
                        Object3DData obj4 = Object3DBuilder.buildplane2V4(Constants.char_byte_data.get("hotspot_icon"));
                        obj4.setPosition(new float[]{marker.get(i).getX(), .2f, marker.get(i).getZ()});
                        obj4.setobjClass("pin");
                        obj4.setId(marker.get(i).getSite_id() + "_marker");
                        addObject(obj4);
                        created_pin.add(obj4);
                        created_pin_pos.add(new float[]{marker.get(i).getX(), marker.get(i).getY() - .1f, marker.get(i).getZ()});


                        // Log.e("Vivek",marker.get(i).store_name.toUpperCase());
                        //draw_texture(marker.get(i).getX(),marker.get(i).getZ(),marker.get(i).store_name.toUpperCase(),1f);


                        ///marker1.centerAndScale(1);
                        //Object3DData marker1 = Object3DBuilder.loadV5_bg(parent, Uri.parse("models/.obj"));
                        //marker1.centerAndScale(.02f);
                        //marker1.scale

                        //marker1
                        //marker1.setScale(new float[]{2,2,1});
                        //marker1.setPosition(new float[]{marker.get(i).getX(), marker.get(i).getY() - .1f, marker.get(i).getZ()});

                      *//**//*  int temp_count = created_pin.size();
                        int temp_count1 = gLView.getModelRenderer().boundingBoxes.size();
                        //Log.w("Warn",)
                        System.out.print(temp_count);
                        System.out.print(temp_count1);
*//**//*
     *//**//* Object3DData marker2 = Object3DBuilder.loadV5_bg(parent, Uri.parse("models/pointer_shadow_patch.obj"));
                        marker2.centerAndScale(.1f);
                        marker2.setPosition(new float[]{marker.get(i).getX(), .1f, marker.get(i).getZ()});
                        marker2.setobjClass("pin");
                        marker2.setId(marker.get(i).getSite_id());
                        created_pin.add(marker2);
                        addObject(marker2);*//**//*
                    } else if (marker.get(i).getFloor_name().equalsIgnoreCase("P")) {
                        Object3DData marker1 = Object3DBuilder.loadV5_bg(parent, Uri.parse("models/parking_car.obj"));
                        marker1.centerAndScale(.2f);
                        marker1.setPosition(new float[]{marker.get(i).getX(), marker.get(i).getY(), marker.get(i).getZ()});
                        marker1.setobjClass("parking");
                        marker1.setId(marker.get(i).getSite_id());
                        addObject(marker1);
                        created_pin.add(marker1);
                        created_pin_pos.add(new float[]{marker.get(i).getX(), marker.get(i).getY() - .1f, marker.get(i).getZ()});


                        *//**//*Object3DData marker2 = Object3DBuilder.loadV5_bg(parent, Uri.parse("models/pointer_shadow_patch.obj"));
                        //Object3DData marker1 = Object3DBuilder.loadV5(parent, Uri.parse("assets://assets/models/cube.obj"));
                        marker2.centerAndScale(.1f);
                        marker2.setPosition(new float[]{marker.get(i).getX(), .1f, marker.get(i).getZ()});
                        marker2.setobjClass("parking");
                        marker2.setId(marker.get(i).getSite_id());
                        created_pin.add(marker2);
                        addObject(marker2);*//**//*
                    }


                }
                //pin_drop_anim_status = true;
                //pin_drop_timer_status = true;
            }*//*


        } catch (Exception ex) {

            Log.e("Scene Loader", "setMarker: " + ex.getMessage());

        }
    }*/

    public void draw_plane(float pos_x, float pos_z, String text, float scale) {
       /* Object3DData obj4 = Object3DBuilder.buildplaneV4(Constants.char_byte_data.get('O'));
        obj4.setPosition(new float[]{pos_x,.1f,pos_z});
        obj4.setobjClass("pin");
        obj4.setId(marker.get(i).getSite_id() + "_marker");
        addObject(obj4);*/
    }

    public void draw_texture(float pos_x, float pos_z, String text, float scale) {

        int length = text.length();
        float start_point = pos_x - length * .03f / 2;
        for (int i = 0; i < length; i++) {
            //text.charAt(i);
            //char_byte_data.get(text.charAt(i));
            Object3DData obj4 = Object3DBuilder.buildplaneV4(Constants.char_byte_data.get(text.charAt(i)));
            obj4.setPosition(new float[]{start_point + (.03f) * i, .1f, pos_z});
            addObject(obj4);
        }

        /* try {
            InputStream open = MyApplication.Companion.getINSTANCE().getApplicationContext().getAssets().open(Uri.parse("models/character/E.jpg").toString());
            open.close();
            Object3DData obj4 = Object3DBuilder.buildplaneV4(IOUtils.read(open));

            obj4.setPosition(new float[]{0f, .1f, .5f});
            addObject(obj4);

            InputStream open1 = MyApplication.Companion.getINSTANCE().getApplicationContext().getAssets().open(Uri.parse("models/character/U.jpg").toString());
            Object3DData obj41 = Object3DBuilder.buildplaneV4(IOUtils.read(open1));
            open1.close();
            obj41.setPosition(new float[]{.06f, .1f, .5f});
            addObject(obj41);
        }
        catch (Exception ex) {

        Log.e("Scene Loader", "setMarker: " + ex.getMessage());

    }
    */


    }


    public float calculate_camera_face_angle() {

        float[] camera_vector = new float[]{camera.xView - camera.xPos, camera.yView - camera.yPos, camera.zView - camera.zPos};
        float delta_x = camera_vector[0];
        float delta_z = camera_vector[2];
        float delta_y = 0;


        float vlen = Matrix.length(delta_x, delta_y, delta_z);
        delta_x /= vlen;
        delta_y /= vlen;
        delta_z /= vlen;

        float[] base_vector = new float[]{0, 0, 1};
        float[] generated_vector = new float[]{delta_x, delta_y, delta_z};

        //angle
        float var1 = (base_vector[0] * generated_vector[0]) + (base_vector[2] * generated_vector[2]);
        float var2 = (float) (Math.sqrt((base_vector[0] * base_vector[0]) + (base_vector[2] * base_vector[2])) * Math.sqrt((generated_vector[0] * generated_vector[0]) + (generated_vector[2] * generated_vector[2])));

        double angla = Math.acos(var1 / var2);
        double angle_degree = Math.toDegrees(angla);
        //float degree = (float)(-angle_degree-45);
        return (float) angle_degree;
    }

    public void delete_object_by_class(String class_name) {
        try {
            if (objects != null) {
                for (int i = 0; i < objects.size(); i++) {
                    if (objects.get(i).getobjClass() == class_name) {
                        objects.remove(i);
                    }
                }
            }
        } catch (Exception e) {

        }


    }

    public void setImHereMarker(List<Marker_Internal_Nav> marker) {
        try {

            if (marker.size() > 0) {
                updated_i_m_here_marker = marker.get(0);
            }
            // this.marker_i_m_here = marker;
            // delete_object_by_class("pin_i_m_here");

            //     Log.e("Selected site id", marker.get(0).site_id);

            boolean store_change_status = false;


            if ((Constants.i_m_here_marker != null) && (marker.size() > 0) && (!Constants.i_m_here_marker.getId().equalsIgnoreCase(marker.get(0).getSite_id()))) {
                store_change_status = true;
                callback.onStoreChange();
                if (Cluster3DMap.Companion.getScene() != null) {
                    if (Cluster3DMap.Companion.getMActionMode() == Cluster3DMap.IndoorMode.NAVIGATION) {
                        if (store_change_status) {
                            //  setthirdpersoncamera();
                        }
                    }
                }
            }

            if (Constants.i_m_here_marker == null) {
                //     Log.e("Sceneloder", "Constants.i_m_here_marker == null");
                ContentUtils.setThreadActivity(parent);
                ContentUtils.provideAssets(parent, clusterId);
                created_pin_imhere.clear();
                if (marker.size() > 0) {

                    for (int i = 0; i < marker.size(); i++) {
                        // Object3DData marker1 = Object3DBuilder.loadV5(parent, Uri.parse("models/map_pointer02.obj"));
                        //Object3DData marker1 = Object3DBuilder.loadV5(parent, Uri.parse("models/pointer01.obj"));
                        Object3DData marker1 = Object3DBuilder.loadV5_bg(parent, Uri.parse("models/source_dot.obj"));
                        Object3DData marker1_child = Object3DBuilder.loadV5_bg(parent, Uri.parse("models/source_marker_wave.obj"));
                        marker1.centerAndScale(0);
                        marker1_child.centerAndScale(0);
                        marker1_child.setId("i_m_here_waves");
                        marker1_child.setobjClass("pin_i_m_here");
                        //Object3DData marker1 = Object3DBuilder.loadV5(parent, Uri.parse("assets://assets/models/cube.obj"));

                        //marker1.centerAndScale(.2f);

                        marker_old_position = new float[]{marker.get(i).getX(), marker.get(i).getY(), marker.get(i).getZ()};
                        marker_new_position = new float[]{marker.get(i).getX(), marker.get(i).getY(), marker.get(i).getZ()};


                        marker1.setPosition(new float[]{marker.get(i).getX(), marker.get(i).getY(), marker.get(i).getZ()});
                        marker1_child.setPosition(new float[]{marker.get(i).getX(), marker.get(i).getY(), marker.get(i).getZ()});
                        marker1.setobjClass("pin_i_m_here");
                        //  Log.e("Sceneloder", "Constants.i_m_here_marker == Printed");

                        marker1.setId(marker.get(i).getSite_id());
                        Constants.i_m_here_marker = marker1;
                        //Constants.i_m_here_marker.centerAndScale(0);
                        Constants.i_m_here_marker.child = marker1_child;

                        addObject(Constants.i_m_here_marker);
                        camera_focus_imhere_trigger();
                        InputStream gyropin = ApplicationContext.get().getApplicationContext().getAssets().open(Uri.parse("models/current_pin.png").toString());

                        //Object3DData obj53 = null;
                        Object3DData obj53 = Object3DBuilder.buildplanemall2V5(IOUtils.read(gyropin));

                        //Object3DData obj53 = Object3DBuilder.loadV5_bg(parent, Uri.parse("models/path_arrow.obj"));
                        // Log.e("Sceneloder", "Constants.i_m_here_marker == PATH ARRAOW");
                        // Object3DData obj53 = Object3DBuilder.buildplane2V4(Constants.char_byte_data.get("gyropin"));
                        // obj53.setPosition(new float[]{marker.get(i).getX(), marker.get(i).getY(), marker.get(i).getZ()});
                        obj53.centerAndScale(.2f);
                        //  obj53.setColor(new float[]{1.0f, 1.0f, 1f, 1.0f});
                        obj53.setId("compass1");
                        addObject(obj53);

                        Constants.compass_obj = obj53;

                        Object3DData compass = Object3DBuilder.loadV5_bg(parent, Uri.parse("models/path_arrow.obj"));
                        compass.setId("compass");
                        compass.setPosition(new float[]{marker.get(i).getX(), .29f, marker.get(i).getZ()});
                        compass.setobjClass("pin_i_m_here");
                        Constants.compass = compass;

                        // created_pin_imhere.add(marker1);


                    }
                    store_change_status = true;
                    //pin_drop_anim_status = true;
                    //pin_drop_timer_status = true;
                }
            } else {
                // store_change_status = true;
                if (store_change_status || isdeleted_marker) {
                    delete_object_by_class("pin_i_m_here");
                    //    Log.e("Sceneloder", "Else Mark pin_i_m_here");
                    if (marker.size() > 0) {
                        for (int i = 0; i < marker.size(); i++) {


                            marker_old_position = new float[]{Constants.i_m_here_marker.getPositionX(), Constants.i_m_here_marker.getPositionY(), Constants.i_m_here_marker.getPositionZ()};
                            marker_new_position = new float[]{marker.get(i).getX(), marker.get(i).getY(), marker.get(i).getZ()};
                            imhere_transition_timer = 0;
                            marker_transition_status = true;


                            //Constants.i_m_here_marker.centerAndScale(0);
                            //Constants.i_m_here_marker.setPosition(new float[]{marker.get(i).getX(), marker.get(i).getY(), marker.get(i).getZ()});
                            Constants.i_m_here_marker.setobjClass("pin_i_m_here");
                            Constants.i_m_here_marker.setId(marker.get(i).getSite_id());
                            addObject(Constants.i_m_here_marker);


                            //Constants.i_m_here_marker.setPosition(new float[]{marker.get(i).getX(), marker.get(i).getY(), marker.get(i).getZ()});
                            //Constants.compass_obj.setobjClass("compass1");
                            Constants.compass_obj.setId("compass1");
                            addObject(Constants.compass_obj);


                            Constants.i_m_here_marker.child.setPosition(new float[]{marker.get(i).getX(), marker.get(i).getY(), marker.get(i).getZ()});
                            Constants.i_m_here_marker.child.setobjClass("pin_i_m_here");
                            Constants.i_m_here_marker.child.setId("i_m_here_waves");
                            addObject(Constants.i_m_here_marker.child);

                            Constants.compass.setPosition(new float[]{marker.get(i).getX(), .29f, marker.get(i).getZ()});
                            Constants.compass.setobjClass("pin_i_m_here");
                            Constants.compass.setId("compass");
                            //addObject(Constants.compass);

                            isdeleted_marker = false;
                            if (Cluster3DMap.Companion.getMActionMode() != Cluster3DMap.IndoorMode.NAVIGATION) {
                                camera_focus_imhere_trigger();
                            }
                            if (Cluster3DMap.Companion.getMActionMode() == Cluster3DMap.IndoorMode.NAVIGATION) {
                                //setthirdpersoncamera();
                            }

                        }
                    }
                }

            }


        } catch (Exception ex) {

            Log.e("Scence Loader", "setImHereMarker: " + ex.getMessage());

        }
    }


   /* public void animate_foot(float timer) {
        try {
            if (timer <= 1) {
                if (path_pos_index + 1 < path_pos_scene.size() - 1) {
                    float x = lerp(path_pos_scene.get(path_pos_index).x, path_pos_scene.get(path_pos_index + 1).x, timer);
                    float y = lerp(path_pos_scene.get(path_pos_index).y, path_pos_scene.get(path_pos_index + 1).y, timer);
                    float z = lerp(path_pos_scene.get(path_pos_index).z, path_pos_scene.get(path_pos_index + 1).z, timer);
                    if (src_pin != null) {
                        src_pin.setPosition(new float[]{x, .1f, z});
                    }
                }
            } else {
                if (path_pos_scene.size() > 1) {
                    path_pos_index = (path_pos_index + 1) % (path_pos_scene.size());
                    animate_foot_timer = 0;
                }
            }
        } catch (Exception e) {

        }

    }*/

    public void set_transparency(Object3DData o3d_data, float val) {

        WavefrontLoader.Faces faces = o3d_data.getFaces();
        FloatBuffer colorArrayBuffer = null;
        colorArrayBuffer = createNativeByteBuffer(4 * faces.getVerticesReferencesCount() * 4)
                .asFloatBuffer();
        float[] currentColor = {0f, 1.0f, 0.0f, val};
        for (int i = 0; i < faces.getSize(); i++) {
            /*if (faceMats.findMaterial(i) != null) {
               WavefrontLoader.Material mat = materials.getMaterial(faceMats.findMaterial(i));
               if (mat != null) {
                  currentColor = mat.getKdColor() != null ? mat.getKdColor() : currentColor;
                  anyOk = anyOk || mat.getKdColor() != null;
               }
            }*/
            colorArrayBuffer.put(currentColor);
            colorArrayBuffer.put(currentColor);
            colorArrayBuffer.put(currentColor);
        }

        o3d_data.setVertexColorsArrayBuffer(colorArrayBuffer);

    }

    private static ByteBuffer createNativeByteBuffer(int length) {
        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(length);
        // use the device hardware's native byte order
        bb.order(ByteOrder.nativeOrder());
        return bb;
    }


    public void loadFloorImage(String path, String floor, String clusterId) {
        try {
            initial_orientation = Float.parseFloat("45");
            InputStream opena = new FileInputStream(new File(path));
            Object3DData obj53 = null;
            obj53 = Object3DBuilder.buildplanemall2V4(IOUtils.read(opena));
            obj53.setPosition(new float[]{0, 0, 0});
            Float width = Float.parseFloat("3.84");
            Float height = Float.parseFloat("2.333");
            obj53.setScale(new float[]{width, .01f, height});
            addObject(obj53);
            opena.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void set_camera_animation1() {
        camera.set_camera(0, 10, 0, 0, 0, 1, 0, 1, 0);
        camera_animation1_status = true;
        camera_animation1_timer = 0;
        source_camera_position1 = new float[]{camera.xPos, camera.yPos, camera.zPos};
        //target_camera_animation1_position = new float[]{0, 5f, -8};
        target_camera_animation1_position = new float[]{0, 5f, 0};
    }


    boolean camera_transition_status = false;
    float camera_transition_timer = 0;

    float[] target_camera_position;
    float[] target_camera_view;


    public boolean camera_direction_animation = false;
    public float camera_direction_timer = 0;

    public float[] source_camera_direction_position;
    public float[] target_camera_direction_position;
    public float[] source_camera_view_position;
    public float[] target_camera_view_position;

    public float[] source_camera_view_position2;
    public float[] target_camera_view_position2;
    public List<Object3DData> dot_node_list;
    public int dot_node_index = 0;

   /* public void set_directional_camera_trigger() {

        //setthirdpersoncamera();
        camera_direction_animation = true;
        camera_direction_timer = 0;
        source_camera_direction_position = new float[]{camera.xPos, camera.yPos, camera.zPos};
        target_camera_direction_position = new float[]{0, 5, 0};
        source_camera_view_position = new float[]{camera.xView, camera.yView, camera.zView};
        if (path_pos_scene != null && path_pos_scene.size() > 1) {
            float temp_x = (path_pos_scene.get(0).getX() + path_pos_scene.get(path_pos_scene.size() - 1).getX()) / 2;
            float temp_z = (path_pos_scene.get(0).getZ() + path_pos_scene.get(path_pos_scene.size() - 1).getZ()) / 2;
            target_camera_view_position = new float[]{temp_x, camera.yView, temp_z};
        }


    }


    public void setdirectionalcamera() {
        try {

            int selected_index = -1;
            for (int i = 0; i < path_pos_scene.size(); i++) {
                String site_id = "";
                if (Constants.i_m_here_marker != null) {
                    site_id = Constants.i_m_here_marker.id;
                }
                if (site_id != "") {
                    if (path_pos_scene.get(i).site_id.equals(site_id)) {
                        selected_index = i;
                    }
                }

            }
            if (selected_index != -1 && (selected_index != path_pos_scene.size() - 1)) {

                float[] current_floor_source = new float[]{path_pos_scene.get(selected_index).x, path_pos_scene.get(selected_index).y, path_pos_scene.get(selected_index).z};

                int last_index = path_pos_scene.size() - 1;
                float[] current_floor_destination = new float[]{path_pos_scene.get(selected_index + 1).x, path_pos_scene.get(selected_index + 1).y, path_pos_scene.get(selected_index + 1).z};

                float[] resultant_vector = new float[3];

                resultant_vector[0] = current_floor_destination[0] - current_floor_source[0];
                resultant_vector[1] = current_floor_destination[1] - current_floor_source[1];
                resultant_vector[2] = current_floor_destination[2] - current_floor_source[2];


                double vec_length = Matrix.length(resultant_vector[0], resultant_vector[1], resultant_vector[2]);

                resultant_vector[0] /= vec_length;
                resultant_vector[1] /= vec_length;
                resultant_vector[2] /= vec_length;

                double ang = orientation;
                float angle_rad = (float) Math.toRadians(ang);

                //target_camera_view = new float[]{current_floor_destination[0], current_floor_destination[1], current_floor_destination[2]};
                //target_camera_position = new float[]{current_floor_source[0] - resultant_vector[0] * 2, current_floor_source[1] + 2.5f, current_floor_source[2] - resultant_vector[2] * 2};
                target_camera_position = new float[3];
                target_camera_view = new float[3];
                target_camera_position[0] = Constants.i_m_here_marker.getPositionX() + (float) (2 * Math.cos(angle_rad)); //- centre_dir[0]*.2f;
                target_camera_position[1] = Constants.i_m_here_marker.getPositionY() + 2.5f;
                target_camera_position[2] = Constants.i_m_here_marker.getPositionZ() + (float) (2 * Math.sin(angle_rad));
                ;
                target_camera_view = Constants.i_m_here_marker.getPosition();

                camera_transition_status = true;
                third_person_camera_status = true;
                camera_transition_timer = 0;

            }

            *//*if (path_pos_scene.size() > 1)

            {
                float[] current_floor_source = new float[]{path_pos_scene.get(0).x, path_pos_scene.get(0).y, path_pos_scene.get(0).z};

                int last_index = path_pos_scene.size() - 1;
                float[] current_floor_destination = new float[]{path_pos_scene.get(1).x, path_pos_scene.get(1).y, path_pos_scene.get(1).z};

                float[] resultant_vector = new float[3];

                resultant_vector[0] = current_floor_destination[0] - current_floor_source[0];
                resultant_vector[1] = current_floor_destination[1] - current_floor_source[1];
                resultant_vector[2] = current_floor_destination[2] - current_floor_source[2];


                double vec_length = Matrix.length(resultant_vector[0], resultant_vector[1], resultant_vector[2]);

                resultant_vector[0] /= vec_length;
                resultant_vector[1] /= vec_length;
                resultant_vector[2] /= vec_length;


                target_camera_view = new float[]{current_floor_destination[0], current_floor_destination[1], current_floor_destination[2]};
                target_camera_position = new float[]{current_floor_source[0] - resultant_vector[0] * 2, current_floor_source[1] + 2.5f, current_floor_source[2] - resultant_vector[2] * 2};
                camera_transition_status = true;
                camera_transition_timer = 0;
            }*//*

        } catch (Exception e) {
            Log.e("SceneLoader", "+" + e.getMessage());
        }

        //Camera current floor destination

        //path_pos_scene.get(0).
    }*/


    public void setthirdpersoncamera() {
        try {
            // Camera current floor source
            if (path_pos_scene.size() > 1) {
                float[] current_floor_source = new float[]{path_pos_scene.get(0).x, path_pos_scene.get(0).y, path_pos_scene.get(0).z};

                int last_index = path_pos_scene.size() - 1;
                float[] current_floor_destination = new float[]{path_pos_scene.get(last_index).x, path_pos_scene.get(last_index).y, path_pos_scene.get(last_index).z};

                float[] resultant_vector = new float[3];

                resultant_vector[0] = current_floor_destination[0] - current_floor_source[0];
                resultant_vector[1] = current_floor_destination[1] - current_floor_source[1];
                resultant_vector[2] = current_floor_destination[2] - current_floor_source[2];


                double vec_length = Matrix.length(resultant_vector[0], resultant_vector[1], resultant_vector[2]);

                resultant_vector[0] /= vec_length;
                resultant_vector[1] /= vec_length;
                resultant_vector[2] /= vec_length;


                target_camera_view = new float[]{current_floor_destination[0], current_floor_destination[1], current_floor_destination[2]};
                target_camera_position = new float[]{current_floor_source[0] - resultant_vector[0] * 2, current_floor_source[1] + 1.5f, current_floor_source[2] - resultant_vector[2] * 2};
                camera_transition_status = true;
                camera_transition_timer = 0;
            }
            // Constants.i_m_here_marker.getId()
            //third_person_camera_status = false;
            int selected_index = -1;
            for (int i = 0; i < path_pos_scene.size(); i++) {
                String site_id = "";
                if (Constants.i_m_here_marker != null) {
                    site_id = Constants.i_m_here_marker.id;
                }
                if (site_id != "") {
                    if (path_pos_scene.get(i).site_id.equals(site_id)) {
                        selected_index = i;
                    }
                }

            }
            if (selected_index != -1 && (selected_index != path_pos_scene.size() - 1)) {

                float[] current_floor_source = new float[]{path_pos_scene.get(selected_index).x, path_pos_scene.get(selected_index).y, path_pos_scene.get(selected_index).z};

                int last_index = path_pos_scene.size() - 1;
                float[] current_floor_destination = new float[]{path_pos_scene.get(selected_index + 1).x, path_pos_scene.get(selected_index + 1).y, path_pos_scene.get(selected_index + 1).z};

                float[] resultant_vector = new float[3];

                resultant_vector[0] = current_floor_destination[0] - current_floor_source[0];
                resultant_vector[1] = current_floor_destination[1] - current_floor_source[1];
                resultant_vector[2] = current_floor_destination[2] - current_floor_source[2];


                double vec_length = Matrix.length(resultant_vector[0], resultant_vector[1], resultant_vector[2]);

                resultant_vector[0] /= vec_length;
                resultant_vector[1] /= vec_length;
                resultant_vector[2] /= vec_length;

                double ang = orientation;
                float angle_rad = (float) Math.toRadians(ang);

                //target_camera_view = new float[]{current_floor_destination[0], current_floor_destination[1], current_floor_destination[2]};
                //target_camera_position = new float[]{current_floor_source[0] - resultant_vector[0] * 2, current_floor_source[1] + 2.5f, current_floor_source[2] - resultant_vector[2] * 2};
                target_camera_position = new float[3];
                target_camera_view = new float[3];
                target_camera_position[0] = Constants.i_m_here_marker.getPositionX() + (float) (2 * Math.cos(angle_rad)); //- centre_dir[0]*.2f;
                target_camera_position[1] = Constants.i_m_here_marker.getPositionY() + 2.5f;
                target_camera_position[2] = Constants.i_m_here_marker.getPositionZ() + (float) (2 * Math.sin(angle_rad));
                ;
                target_camera_view = Constants.i_m_here_marker.getPosition();

                camera_transition_status = true;
                third_person_camera_status = true;
                camera_transition_timer = 0;

            }

            if (path_pos_scene.size() > 1) {
                float[] current_floor_source = new float[]{path_pos_scene.get(0).x, path_pos_scene.get(0).y, path_pos_scene.get(0).z};

                int last_index = path_pos_scene.size() - 1;
                float[] current_floor_destination = new float[]{path_pos_scene.get(1).x, path_pos_scene.get(1).y, path_pos_scene.get(1).z};

                float[] resultant_vector = new float[3];

                resultant_vector[0] = current_floor_destination[0] - current_floor_source[0];
                resultant_vector[1] = current_floor_destination[1] - current_floor_source[1];
                resultant_vector[2] = current_floor_destination[2] - current_floor_source[2];


                double vec_length = Matrix.length(resultant_vector[0], resultant_vector[1], resultant_vector[2]);

                resultant_vector[0] /= vec_length;
                resultant_vector[1] /= vec_length;
                resultant_vector[2] /= vec_length;


                target_camera_view = new float[]{current_floor_destination[0], current_floor_destination[1], current_floor_destination[2]};
                target_camera_position = new float[]{current_floor_source[0] - resultant_vector[0] * 2, current_floor_source[1] + 2.5f, current_floor_source[2] - resultant_vector[2] * 2};
                camera_transition_status = true;
                camera_transition_timer = 0;
            }

        } catch (Exception e) {
            Log.e("SceneLoader", "+" + e.getMessage());
        }

    }

    public Camera getCamera() {
        return camera;
    }

    private void makeToastText(final String text, final int toastDuration) {
        parent.runOnUiThread(() -> Toast.makeText(parent.getApplicationContext(), text, toastDuration).show());
    }

    public Object3DData getLightBulb() {
        return lightPoint;
    }

    public float[] getLightPosition() {
        return lightPosition;
    }

    /**
     * Hook for animating the objects before the rendering
     */
    public void onDrawFrame() {

        //  Log.e("Camera",""+camera.yPos);
        try {
            if (objects.isEmpty()) return;

            if (dest_pin != null) {

                Matrix.setIdentityM(dest_pin.modelMatrix, 0);
                float new_pos_x = dest_pin_position[0];
                float new_pos_y = dest_pin_position[1];
                float new_pos_z = dest_pin_position[2];
                dest_pin.position[0] = new_pos_x;
                dest_pin.position[1] = new_pos_y;
                dest_pin.position[2] = new_pos_z;

                double ang = calculate_camera_face_angle();//-orientation + initial_orientation;
                float angle = (float) Math.toRadians(ang);
                dest_pin.rotation[1] = (float) ang;


                dest_pin.position[0] = (new_pos_x * (float) Math.cos(angle)) - (new_pos_z * (float) Math.sin(angle));
                dest_pin.position[1] = new_pos_y;
                dest_pin.position[2] = (new_pos_z * (float) Math.cos(angle)) + (new_pos_x * (float) Math.sin(angle));

            }

            if ((Cluster3DMap.Companion.getMActionMode() == Cluster3DMap.IndoorMode.NAVIGATION) && !third_person_camera_status) {

                calulate_live_camera_direction(true);
            }


            if (marker_transition_status && Constants.i_m_here_marker != null) {
                iamhere_transition();
            }

            if (created_pin_pos.size() > 0) {
                for (int i = 0; i < created_pin.size(); i++) {
                    if (created_pin.get(i) != null) {
                        Matrix.setIdentityM(created_pin.get(i).modelMatrix, 0);
                        float new_pos_x = created_pin_pos.get(i)[0];
                        float new_pos_y = created_pin_pos.get(i)[1];
                        float new_pos_z = created_pin_pos.get(i)[2];
                        created_pin.get(i).position[0] = new_pos_x;
                        created_pin.get(i).position[1] = new_pos_y;
                        created_pin.get(i).position[2] = new_pos_z;

                        double ang = calculate_camera_face_angle();//-orientation + initial_orientation;
                        float angle = (float) Math.toRadians(ang);
                        created_pin.get(i).rotation[1] = (float) ang;


                        created_pin.get(i).position[0] = (new_pos_x * (float) Math.cos(angle)) - (new_pos_z * (float) Math.sin(angle));
                        created_pin.get(i).position[1] = new_pos_y;
                        created_pin.get(i).position[2] = (new_pos_z * (float) Math.cos(angle)) + (new_pos_x * (float) Math.sin(angle));
                        //created_pin.get(i).changed =true;

                    }
                }

            }


            if (Constants.compass_obj != null && Constants.i_m_here_marker != null) {

                Matrix.setIdentityM(Constants.compass_obj.modelMatrix, 0);
                Matrix.setIdentityM(mCurrentRotation, 0);
                Matrix.translateM(mCurrentRotation, 0, 0, .3f, 0);
                Constants.compass_obj.position[0] = Constants.i_m_here_marker.getPositionX() - .02f;
                Constants.compass_obj.position[1] = .05f;
                Constants.compass_obj.position[2] = Constants.i_m_here_marker.getPositionZ();
                //  Constants.compass_obj.centerAndScale(.5f);
                //addObject(washroom_obj);
                //washroom_obj.rotation[1] = washroom_obj.rotation[1]+1;
                double ang = -orientation + initial_orientation;
                float angle = (float) Math.toRadians(ang);

                //Compass rotation code
                Constants.compass_obj.rotation[1] = (float) ang;

                float rot_vector_x = (Constants.i_m_here_marker.getPositionX() * (float) Math.cos(angle)) - (Constants.i_m_here_marker.getPositionZ() * (float) Math.sin(angle));
                float rot_vector_z = (Constants.i_m_here_marker.getPositionZ() * (float) Math.cos(angle)) + (Constants.i_m_here_marker.getPositionX() * (float) Math.sin(angle));
                //   Log.e("Rot Vector", String.valueOf(rot_vector_x) + String.valueOf(rot_vector_z));


                Constants.compass_obj.position[0] = (Constants.i_m_here_marker.getPositionX() * (float) Math.cos(angle)) - (Constants.i_m_here_marker.getPositionZ() * (float) Math.sin(angle));
                Constants.compass_obj.position[1] = .05f;
                Constants.compass_obj.position[2] = (Constants.i_m_here_marker.getPositionZ() * (float) Math.cos(angle)) + (Constants.i_m_here_marker.getPositionX() * (float) Math.sin(angle));

                if (Cluster3DMap.Companion.getMActionMode() == Cluster3DMap.IndoorMode.NAVIGATION) {
                    Constants.i_m_here_marker.position[1] = .15f;
                    Constants.compass_obj.position[1] = .05f;
                } else {
                    Constants.i_m_here_marker.position[1] = .3f;
                    Constants.compass_obj.position[1] = .05f;
                }
            }


            if (Cluster3DMap.Companion.getMActionMode() != null) {
                if (Cluster3DMap.Companion.getMActionMode() == Cluster3DMap.IndoorMode.NAVIGATION) {

                    if (!animate_foot_status) {
                        path_pos_index = 0;
                    }
                    animate_foot_status = true;

                } else {
                    animate_foot_status = false;
                }

            }


          /*  if (path_pos_scene != null && animate_foot_status) {
                animate_foot_timer += .02f;
                animate_foot(animate_foot_timer);
                if (src_pin != null && path_pos_scene != null && path_pos_scene.size() > 0) {
//int index =(int)Math.floor(animate_foot_timer)%path_pos_scene.size();
//.setPosition(new float[]{path_pos_scene.get(path_pos_index).x, .1f, path_pos_scene.get(path_pos_index).z});
                }
            }*/
            if (camera_animation1_status) {
                run_camera_animation1();
            }

            if (camera_animation2_status) {
                run_camera_animation2();
            }
            if (camera_direction_animation) {
                run_camera_direction_animation();
            }
            if (dot_node_list.size() > 1) {
                c.setTime(new Date());
                long ct_time = SystemClock.uptimeMillis();
                float diff;
                diff = ct_time - last_blink;
                if ((diff) > 200) {
                    last_blink = ct_time;
                    dot_node_index = (dot_node_index + 1) % dot_node_list.size();
                    if (dot_node_index > 0) {
                        // dot_node_list.get(dot_node_index - 1).centerAndScale(.02f);
                    }

                    // dot_node_list.get(dot_node_index).centerAndScale(.05f);
                }


            }


            if (Constants.i_m_here_marker != null) {
                compass_rotation(1);
                pin_anim_timer_status = true;
                float jump_value_a = jump();
                //Constants.i_m_here_marker.setPosition(new float[]{Constants.i_m_here_marker.getPositionX(), jump_value_a, Constants.i_m_here_marker.getPositionZ()});
                float[] marker_pos = new float[]{Constants.i_m_here_marker.getPositionX(), jump_value_a, Constants.i_m_here_marker.getPositionZ()};
                float jump_value = (.4f - jump_value_a) * 1.5f;
                Constants.i_m_here_marker.child.centerAndScale(jump_value * 0.3f);

                //Constants.i_m_here_marker.setRotation(new float[]{0,orientation,0});
                if (dest_pin != null) {
                    //dest_pin.setPosition(new float[]{dest_pin.getPositionX(), jump_value_a, dest_pin.getPositionZ()});
                    if (dest_pin.child != null) {
                        if (pin_anim_timer >= 0 && pin_anim_timer <= 1) {
                            dest_pin.child.centerAndScale(pin_anim_timer * .3f);

                            set_transparency(dest_pin.child, 1 - pin_anim_timer);
                        }
                        if (wave_circle2 != null && wave_circle3 != null) {
                            if (pin_anim_timer >= .2f && pin_anim_timer <= 1.2f) {
                                wave_circle2.centerAndScale((pin_anim_timer - .2f) * .3f);

                                set_transparency(wave_circle2, 1.2f - (pin_anim_timer - .2f));
                            }
                            if (pin_anim_timer >= .4f && pin_anim_timer <= 1.4f) {
                                wave_circle3.centerAndScale((pin_anim_timer - .4f) * .3f);

                                set_transparency(wave_circle3, 1.4f - (pin_anim_timer - .4f));
                            }
                            if (pin_anim_timer > 1f) {
                                dest_pin.child.centerAndScale(.001f);
                                //set_transparency(dest_pin.child, 1);
                            }
                            if (pin_anim_timer > 1.2f) {
                                wave_circle2.centerAndScale(.001f);
                                //set_transparency(dest_pin.child, 1);
                            }
                            if (pin_anim_timer > 1.4f) {
                                wave_circle3.centerAndScale(.001f);
                                //set_transparency(dest_pin.child, 1);
                            }
                        }

                    }
                    // dest_pin.centerAndScale(jump_value*1.3f);
                }
                //Constants.i_m_here_marker.setRotation(new float[]{0,Constants.i_m_here_marker.getRotationY()+1,0});

            } else {
                pin_anim_timer_status = false;
                pin_anim_timer = 0;
            }

            if (camera_transition_status) {
                third_person_camera_status = true;
                camera_transition_timer += .015f;
                if (camera_transition_timer <= 1) {
                    camera.xView = lerp(camera.xView, target_camera_view[0], camera_transition_timer);
                    camera.yView = lerp(camera.yView, target_camera_view[1], camera_transition_timer);
                    camera.zView = lerp(camera.zView, target_camera_view[2], camera_transition_timer);

                    camera.xPos = lerp(camera.xPos, target_camera_position[0], camera_transition_timer); //- centre_dir[0]*.2f;
                    camera.yPos = lerp(camera.yPos, target_camera_position[1], camera_transition_timer);
                    camera.zPos = lerp(camera.zPos, target_camera_position[2], camera_transition_timer);// - centre_dir[2]*.2f;


                    camera.xUp = 0;
                    camera.yUp = 1;
                    camera.zUp = 0;

                    camera.setChanged(true);
                } else {
                    camera_transition_status = false;
                    third_person_camera_status = false;
                    camera_transition_timer = 0;

                }
            }
            if (animateModel) {
                for (int i = 0; i < objects.size(); i++) {
                    // Object3DData obj = objects.get(i);
                    //animator.update(obj);
                }
            }
            // anim_clock_controller();
      /*  if (pin_drop_anim_status) {

            start_pin_drop_animation(2);
        }*/
            if (camera_animation_status) {
                start_camera_animation(1.5f);
            }


            if (camera_view_to_object != null) {
                camera_view_to_object.setPosition(new float[]{camera.xView, camera.yView, camera.zView});
            }
        } catch (Exception e) {
            Log.e("SceneLoader", "+" + e.getMessage());
        }

        //System.out.print(orientation);

        //cameara_rotate_aroundx();

    }  // Draw Frame End

    public void calulate_live_camera_direction(Boolean pan_status) {
        if (Constants.compass_obj != null && Constants.i_m_here_marker != null) {
            float[] target_dest = new float[3];
            float[] source_dest = new float[]{Constants.compass_obj.getPositionX(), Constants.compass_obj.getPositionY(), Constants.compass_obj.getPositionZ()};
            double ang = orientation + initial_orientation;
            float angle_rad = (float) Math.toRadians(ang);

            //float angle_rad = (float) Math.toRadians(angle);

            target_dest[0] = (source_dest[0] * (float) Math.cos(angle_rad)) - (source_dest[2] * (float) Math.sin(angle_rad));
            target_dest[1] = source_dest[1];
            target_dest[2] = (source_dest[2] * (float) Math.cos(angle_rad)) + (source_dest[0] * (float) Math.sin(angle_rad));

            float[] resultant_vector = new float[3];

            /*resultant_vector[0] = target_dest[0] - source_dest[0];
            resultant_vector[1] = target_dest[1] - source_dest[1];
            resultant_vector[2] = target_dest[2] - source_dest[2];*/

            resultant_vector[0] = source_dest[0] - target_dest[0];
            resultant_vector[1] = source_dest[1] - target_dest[1];
            resultant_vector[2] = source_dest[2] - target_dest[2];


            double vec_length = Matrix.length(resultant_vector[0], resultant_vector[1], resultant_vector[2]);

            resultant_vector[0] /= vec_length;
            resultant_vector[1] /= vec_length;
            resultant_vector[2] /= vec_length;


            ///target_camera_view = new float[]{target_dest[0], target_dest[1], target_dest[2]};
            ///target_camera_position = new float[]{source_dest[0] - resultant_vector[0] * 2, source_dest[1] + 2.5f, source_dest[2] - resultant_vector[2] * 2};
            // if (!pan_status) {

            camera.xView = Constants.i_m_here_marker.getPositionX();
            camera.yView = Constants.i_m_here_marker.getPositionY();
            camera.zView = Constants.i_m_here_marker.getPositionZ();
            //  }

            //target_dest[0] = (source_dest[0] * (float) Math.cos(angle_rad)) - (source_dest[2] * (float) Math.sin(angle_rad));
            //target_dest[1] = source_dest[1];
            //target_dest[2] = (source_dest[2] * (float) Math.cos(angle_rad)) + (source_dest[0] * (float) Math.sin(angle_rad));

            camera.xPos = Constants.i_m_here_marker.getPositionX() + (float) ((2 * Math.cos(angle_rad))/*-2*Math.sin(angle_rad)*/); //- centre_dir[0]*.2f;
            camera.yPos = Constants.i_m_here_marker.getPositionY() + 2.5f;
            camera.zPos = Constants.i_m_here_marker.getPositionZ() + (float) ((2 * Math.sin(angle_rad))/*+2*Math.cos(angle_rad)*/);

            /*camera.xPos = source_dest[0] - resultant_vector[0] * 2; //- centre_dir[0]*.2f;
            camera.yPos = source_dest[1] + 2.5f;
            camera.zPos = source_dest[2] - resultant_vector[2] * 2;// - centre_dir[2]*.2f;*/

            // dummy_data.setPosition(target_dest);


            camera.xUp = 0;
            camera.yUp = 1;
            camera.zUp = 0;

            camera.setChanged(true);


            //marker_i_m_here.
           /* camera.xView = 0;
            camera.yView = 0;
            camera.zView = 0;*/

            /*camera.xPos = 0; //- centre_dir[0]*.2f;
            camera.yPos = 10;
            camera.zPos = 0;// - centre_dir[2]*.2f;*/


           /* camera.xUp = 0;
            camera.yUp = 1;
            camera.zUp = 0;*/

        }


    }

    public float jump() {
        float a = .01f;

        if (pin_anim_timer <= 1.4) {
            pin_anim_timer += .02f;
            a = lerp(.01f, 1, pin_anim_timer);
        } else {
            pin_anim_timer = 0.3f;
        }


        return a;
    }


    public void run_camera_direction_animation() {
        try {
            camera_animation2_status = false;
            if (camera_direction_timer <= 1) {
                camera_direction_timer += .02f;
                camera.xPos = lerp(source_camera_direction_position[0], target_camera_direction_position[0], camera_direction_timer);
                camera.yPos = lerp(source_camera_direction_position[1], target_camera_direction_position[1], camera_direction_timer);
                camera.zPos = lerp(source_camera_direction_position[2], target_camera_direction_position[2], camera_direction_timer);

            /*camera.xView = 0;
            camera.yView = 0;
            camera.zView = 1;*/

                camera.xView = lerp(source_camera_view_position[0], target_camera_view_position[0], camera_direction_timer);
                camera.yView = lerp(source_camera_view_position[2], target_camera_view_position[2], camera_direction_timer);
                ;
                camera.zView = lerp(source_camera_view_position[2], target_camera_view_position[2], camera_direction_timer);
                ;

                camera.xUp = 0;
                camera.yUp = 1;
                camera.zUp = 0;

                camera.setChanged(true);

            } else {
                camera_direction_animation = false;
                camera_direction_timer = 0;

            }
        } catch (Exception e) {

        }


    }


    public void run_camera_animation1() {
        try {
            if (camera_animation1_timer <= 1) {
                camera_animation1_timer += .02f;
                camera.xPos = lerp(source_camera_position1[0], target_camera_animation1_position[0], camera_animation1_timer);
                camera.yPos = lerp(source_camera_position1[1], target_camera_animation1_position[1], camera_animation1_timer);
                camera.zPos = lerp(source_camera_position1[2], target_camera_animation1_position[2], camera_animation1_timer);
            /*if(selected_floor_object!=null)
            {
                float temp_y = lerp(0,30,camera_animation1_timer);
                //selected_floor_object.setScale(new float[] {1f,temp_y,1f});
                selected_floor_object.setRotation(new float[]{0,temp_y,0});
            }*/


                camera.xView = 0;
                camera.yView = 0;
                camera.zView = 1;

                //camera.xUp = 0;
                //camera.yUp = 1;
                // camera.zUp = 0;

                camera.setChanged(true);

            } else {
                camera_animation1_status = false;
                camera_animation1_timer = 0;


            }
        } catch (Exception e) {

        }


    }

    public void camera_focus_imhere_trigger() {

        try {
            if (!camera_im_animation_played_status) {
                camera_im_animation_played_status = true;
                if (Constants.i_m_here_marker != null) {


                    camera_animation2_timer = 0;
                    source_camera_position2 = new float[]{camera.xPos, camera.yPos, camera.zPos};
                    //float[] dir_vec = new float[] {updated_i_m_here_marker.getX(), 0-updated_i_m_here_marker.getY(),0- updated_i_m_here_marker.getZ()};
                    float[] dir_vec = new float[]{0 - Constants.i_m_here_marker.getPositionX(), 0 - Constants.i_m_here_marker.getPositionY(), 0 - Constants.i_m_here_marker.getPositionZ()};
                    float len = Matrix.length(dir_vec[0], dir_vec[1], dir_vec[2]);
                    dir_vec[0] /= len;
                    dir_vec[1] /= len;
                    dir_vec[2] /= len;

                    //target_camera_animation2_position =new float[] {updated_i_m_here_marker.getX(), updated_i_m_here_marker.getY()+1, updated_i_m_here_marker.getZ()-dir_vec[2]};
                    //target_camera_animation2_position = new float[]{Constants.i_m_here_marker.getPositionX() - (1.5f * dir_vec[0]), Constants.i_m_here_marker.getPositionY() + 3, Constants.i_m_here_marker.getPositionZ() - (1.5f * dir_vec[2])};
                    target_camera_animation2_position = new float[]{Constants.i_m_here_marker.getPositionX(), 5, Constants.i_m_here_marker.getPositionZ()};
                    //scene!!.target_camera_animation1_view =floatArrayOf(marker_i_m_here.get(0).getX(), marker_i_m_here.get(0).getY(), marker_i_m_here.get(0).getZ());
                    //camera_animation1_status = false;
                    source_camera_view_position2 = new float[]{camera.xView, camera.yView, camera.zView};
                    target_camera_view_position2 = new float[]{Constants.i_m_here_marker.getPositionX(), 5, Constants.i_m_here_marker.getPositionZ()};
                    camera_animation2_status = true;
                }

            }
        } catch (Exception e) {

        }


    }

    public void run_camera_animation2() {

        try {

            if (camera_animation2_timer <= 1) {
                camera_animation2_timer += .02f;
                /*camera.xPos = lerp(source_camera_position2[0], target_camera_animation2_position[0], camera_animation2_timer);
                camera.yPos = lerp(source_camera_position2[1], target_camera_animation2_position[1], camera_animation2_timer);
                camera.zPos = lerp(source_camera_position2[2], target_camera_animation2_position[2], camera_animation2_timer);



                camera.xView = 0;
                camera.yView = 0;
                camera.zView = 1;

                camera.xUp = 0;
                camera.yUp = 1;
                camera.zUp = 0;*/
                camera.xPos = lerp(source_camera_position2[0], Constants.i_m_here_marker.getPositionX(), camera_animation2_timer);
                camera.yPos = lerp(camera.yPos, 7, camera_animation2_timer);
                camera.xView = lerp(source_camera_view_position2[0], target_camera_view_position2[0], camera_animation2_timer);

                //camera.zPos = lerp(source_camera_position2[2],Constants.i_m_here_marker.getPositionZ(), camera_animation2_timer);
                camera.zView = lerp(source_camera_view_position2[2], target_camera_view_position2[2], camera_animation2_timer);
                camera.zPos = camera.zView - .1f;
                // camera.xPos = .266f;
                // camera.xView = .266f;

                //camera.zPos += .03f;
                //camera.zView +=.03 ;
                //camera.zPos = camera.zView-1;


                camera.setChanged(true);

            } else {
                camera_animation2_status = false;
                camera_animation2_timer = 0;
            }
        } catch (Exception e) {

        }


    }


    void anim_clock_controller() {
       /* if (pin_drop_timer_status) {
            pin_drop_start_timer = SystemClock.uptimeMillis();
            pin_drop_timer_status = false;
            block_pan_status = true;
            block_zoom_status = true;
        }
        if (pin_drop_anim_status) {
            pin_drop_timer = (SystemClock.uptimeMillis() - pin_drop_start_timer) / 1000;
        }*/
        if (camera_anim_timer_status) {
            camera_y_pos_anim = camera.yPos;
            camera_anim_start_timer = SystemClock.uptimeMillis();
            block_zoom_status = true;
            block_pan_status = true;
            camera_anim_timer_status = false;

        }
        if (camera_animation_status) {
            camera_anim_timer = (SystemClock.uptimeMillis() - camera_anim_start_timer) / 1000;
        }

    }

    public void start_camera_animation(float duration) {


        try {
            if (camera_anim_timer <= duration) {
                float y = lerp(camera_y_pos_anim, 3, camera_anim_timer / duration);
                camera.yPos = y;
            /*camera.xPos -= 0.03f;
            camera.zPos += 0.02f;
            camera.MoveCameraZImpl(.005f);*/
                //camera.translateCamera(0f,0.005f/*(float) (Math.signum(.2f / Math.PI) / 4)*/);
            } else {
                block_pan_status = false;
                block_zoom_status = false;
                camera_animation_status = false;
                camera_anim_timer_status = false;
                navigation_mode_status = true;
                //draw_path();
            }
            camera.setChanged(true);
        } catch (Exception e) {

        }


    }

    /*public void draw_path(List<Marker_Internal_Nav> path_pos) {


        //create_source_pin(new float[]{path_pos.get(0).x, .01f, path_pos.get(0).z});
        for(int i=0;i<path_pos.size()-1;i++)
        {
            Log.w("Warn",String.valueOf(path_pos.get(i).x)+","+String.valueOf(path_pos.get(i).y)+","+String.valueOf(path_pos.get(i).z));
            Log.w("Test Dynamic",String.valueOf(path_pos.get(i).x)+","+String.valueOf(path_pos.get(i).z)+","+String.valueOf(path_pos.get(i+1).x)+","+String.valueOf(path_pos.get(i).z));

            Object3DData obj201 = Object3DBuilder.buildcustomPlane(draw_custom_plane(new float[]{path_pos.get(i).x, .01f, path_pos.get(i).z, path_pos.get(i+1).x, .01f, path_pos.get(i+1).z}));
            obj201.setColor(new float[]{1f, 0f, 0, 0.25f});
            obj201.setId("custom_plane_"+String.valueOf(i));
            addObject(obj201);
        }
        int last_index = path_pos.size()-1;
        //create_destination_pin(new float[]{path_pos.get(last_index).x, .01f, path_pos.get(last_index).z});




    }*/

    public void clear_pin_marker() {
        for (int i = 0; i <= objects.size(); i++) {
            if (objects.get(i).getobjClass().toString() == "pin") {
                objects.remove(i);
            }
        }
        created_pin.clear();
    }

    float last_blink;

    public void draw_path(List<Marker_Internal_Nav> path_pos) {


        try {
            ContentUtils.setThreadActivity(parent);
            ContentUtils.provideAssets(parent, clusterId);
            c.setTime(new Date());
            last_blink = c.get(Calendar.MILLISECOND);
            dot_node_list.clear();
            dot_node_index = 0;
            path_pos_scene = path_pos;
            InputStream lift_icon = ApplicationContext.get().getApplicationContext().getAssets().open(Uri.parse("models/current_lift.png").toString());
            Object3DData lift_icon_obj = Object3DBuilder.buildplanemall2V4(IOUtils.read(lift_icon));
            lift_icon_obj.centerAndScale(.2f);
            lift_icon_obj.setobjClass("src_pin");

//////////Source
            InputStream source_icon = ApplicationContext.get().getApplicationContext().getAssets().open(Uri.parse("models/source_pin.png").toString());
            Object3DData source_icon_obj = Object3DBuilder.buildplanemall2V4(IOUtils.read(source_icon));
            source_icon_obj.centerAndScale(.1f);
            source_icon_obj.setPosition(new float[]{path_pos.get(0).x, .03f, path_pos.get(0).z});
            addObject(source_icon_obj);


            if (!distict_floor_array.get(0).equals(distict_floor_array.get(1))) {
                if (distict_floor_array.get(1).equals(destination_floor_number)) {
                    lift_icon_obj.setPosition(new float[]{path_pos.get(0).x, .1f, path_pos.get(0).z});
                    addObject(lift_icon_obj);
                    target_camera_animation2_position = new float[]{lift_icon_obj.getPositionX(), 8, lift_icon_obj.getPositionZ()};
                    source_camera_view_position2 = new float[]{camera.xView, camera.yView, camera.zView};
                    target_camera_view_position2 = new float[]{lift_icon_obj.getPositionX(), 8, lift_icon_obj.getPositionZ()};
                    camera_animation2_status = true;
                    camera_animation2_timer = 0;
                    Cluster3DMap.Companion.setPan_value(.15f);
                }
            }
            //src_pin = marker11;
            //addObject(marker11);
            for (int i = 0; i < path_pos.size() - 1; i++) {
                Log.w("Warn1", String.valueOf(path_pos.get(0).x) + "," + String.valueOf(path_pos.get(0).y) + "," + String.valueOf(path_pos.get(0).z));
                float[] start_point1 = new float[]{path_pos.get(i).x, path_pos.get(i).y, path_pos.get(i).z};//pin_pointer[1];
                float[] end_point1 = new float[]{path_pos.get(i + 1).x, path_pos.get(i + 1).y, path_pos.get(i + 1).z};//pin_pointer[2];
                float x_multiply_factor = (start_point1[0] - end_point1[0]) * .02f;
                float z_multiply_factor = (start_point1[2] - end_point1[2]) * .02f;
                float[] squareLineData_mod1 = new float[]{
                        // @formatter:off
                        start_point1[0] - z_multiply_factor, 0.05f, start_point1[2] - x_multiply_factor, // top left front
                        end_point1[0] - z_multiply_factor, 0.05f, end_point1[2] - x_multiply_factor, // bottom left front
                        end_point1[0] + z_multiply_factor, 0.05f, end_point1[2] + x_multiply_factor, // bottom right front
                        start_point1[0] + z_multiply_factor, 0.05f, start_point1[2] + x_multiply_factor, // upper right front
                        start_point1[0] - z_multiply_factor, 0.03f, start_point1[2] - x_multiply_factor, // top left back
                        end_point1[0] - z_multiply_factor, 0.03f, end_point1[2] - x_multiply_factor, // bottom left back
                        end_point1[0] + z_multiply_factor, 0.03f, end_point1[2] + x_multiply_factor, // bottom right back
                        start_point1[0] + z_multiply_factor, 0.03f, start_point1[2] + x_multiply_factor// upper right back
                        // @formatter:on
                };

                float len = Matrix.length(end_point1[0] - start_point1[0], end_point1[1] - start_point1[1], end_point1[2] - start_point1[2]);

                for (float k = .007f; k <= len; ) {
                    float a = lerp(start_point1[0], end_point1[0], k / len);
                    float c = lerp(start_point1[2], end_point1[2], k / len);
                    Object3DData marker1 = Object3DBuilder.loadV5_bg(parent, Uri.parse("models/path_dot.obj"));
                    marker1.centerAndScale(.025f);
                    marker1.setPosition(new float[]{a, .03f, c});
                    addObject(marker1);
                    dot_node_list.add(marker1);
                    k = k + .007f;
                }

            }

            if ((!distict_floor_array.get(0).equals(distict_floor_array.get(1))) && (distict_floor_array.get(0).equals(destination_floor_number))) {
                lift_icon_obj.setPosition(new float[]{path_pos.get(path_pos.size() - 1).x, .02f, path_pos.get(path_pos.size() - 1).z});
                addObject(lift_icon_obj);
                target_camera_animation2_position = new float[]{lift_icon_obj.getPositionX(), 8, lift_icon_obj.getPositionZ()};
                source_camera_view_position2 = new float[]{camera.xView, camera.yView, camera.zView};
                target_camera_view_position2 = new float[]{lift_icon_obj.getPositionX(), 8, lift_icon_obj.getPositionZ()};
                camera_animation2_status = true;
                camera_animation2_timer = 0;

                Cluster3DMap.Companion.setPan_value(2.0f);

            } else {
                Object3DData marker1 = Object3DBuilder.loadV5_bg(parent, Uri.parse("models/destination_marker.obj"));
                marker1.setobjClass("dest_pin");
                dest_pin_position = new float[]{path_pos.get(path_pos.size() - 1).x, .02f, path_pos.get(path_pos.size() - 1).z};
                dest_pin = marker1;
                Object3DData marker1_child = Object3DBuilder.loadV5_bg(parent, Uri.parse("models/wave_circle4.obj"));
                marker1_child.setPosition(new float[]{path_pos.get(path_pos.size() - 1).x, .02f, path_pos.get(path_pos.size() - 1).z});
                marker1_child.setId("dest_pin_waves");
                marker1_child.setobjClass("dest_pin");
                dest_pin.child = marker1_child;
                addObject(dest_pin.child);
                addObject(marker1);


                Object3DData marker1_child1 = Object3DBuilder.loadV5_bg(parent, Uri.parse("models/wave_circle2.obj"));
                marker1_child1.setPosition(new float[]{path_pos.get(path_pos.size() - 1).x, .02f, path_pos.get(path_pos.size() - 1).z});
                marker1_child1.setId("dest_pin_waves1");
                marker1_child1.setobjClass("dest_pin");
                addObject(marker1_child1);
                wave_circle2 = marker1_child1;

                Object3DData marker1_child2 = Object3DBuilder.loadV5_bg(parent, Uri.parse("models/wave_circle3.obj"));
                marker1_child2.setPosition(new float[]{path_pos.get(path_pos.size() - 1).x, .02f, path_pos.get(path_pos.size() - 1).z});
                marker1_child2.setId("dest_pin_waves2");
                marker1_child2.setobjClass("dest_pin");
                addObject(marker1_child2);
                wave_circle3 = marker1_child2;
            }

            if (Cluster3DMap.Companion.getMActionMode() != Cluster3DMap.IndoorMode.NAVIGATION) {
                camera_im_animation_played_status = false;
                camera_focus_imhere_trigger();
            }
        } catch (Exception e) {

        }


    }


    public void create_source_pin(float[] loc) {
        Object3DData marker1 = Object3DBuilder.loadV5(parent, Uri.parse("models/source_pin.obj"));
        //Object3DData marker1 = Object3DBuilder.loadV5(parent, Uri.parse("assets://assets/models/cube.obj"));
        marker1.centerAndScale(.3f);
        marker1.setPosition(new float[]{loc[0], loc[1], loc[2]});
        marker1.setobjClass("path_src_marker");
        marker1.setId("source_pin");
        addObject(marker1);
        // created_pin.add(marker1);
    }

    public void create_destination_pin(float[] loc) {
       /* Object3DData marker1 = Object3DBuilder.loadV5(parent, Uri.parse("models/dest_pin.obj"));
        //Object3DData marker1 = Object3DBuilder.loadV5(parent, Uri.parse("assets://assets/models/cube.obj"));
        marker1.centerAndScale(.3f);
        marker1.setPosition(new float[]{loc[0], loc[1], loc[2]});
        marker1.setobjClass("path_dest_marker");
        marker1.setId("dest_pin");
        addObject(marker1);
        created_pin.add(marker1);*/

    }


    public float[] draw_custom_plane(float[] line3dpoint) {
        float[] custom_cubePositionData = {

                // Top face
                line3dpoint[0], 0.01f, line3dpoint[2],
                line3dpoint[0] + .01f, 0.01f, line3dpoint[2] + .01f,
                line3dpoint[3], 0.01f, line3dpoint[5],
                line3dpoint[0] + .01f, 0.01f, line3dpoint[2] + .01f,
                line3dpoint[3] + .01f, 0.01f, line3dpoint[5] + .01f,
                line3dpoint[3], 0.01f, line3dpoint[5],
        };
        return custom_cubePositionData;
    }

    public void update_path() {

    }

    public void start_pin_drop_animation(float duration) {
        if (pin_drop_timer <= duration) {
            float y = lerp(5, .2f, pin_drop_timer / duration);
            for (int i = 0; i < created_pin.size(); i++)
                created_pin.get(i).setPosition(new float[]{created_pin.get(i).getPositionX(), y, created_pin.get(i).getPositionZ()});
        } else {
            block_pan_status = false;
            block_zoom_status = false;
            pin_drop_anim_status = false;
            pin_drop_timer_status = false;
               /*for(int i = 0;i<objects.size();i++)
               {
                   if (objects.get(i).getId()=="pin")
                   {
                       objects.remove(i);
                   }
               }*/
        }
    }

    boolean camera_animation_status = false;

    public void switch_to_navigation_mode() {
        camera_animation_status = true;
        camera_anim_timer_status = true;
    }


    public float lerp(float start, float end, float timer) {
        float result;
        result = (start + ((end - start) * timer));
        Log.i("Lerp", String.valueOf(result) + "," + String.valueOf(timer));
        return result;

    }

    void cameara_rotate_aroundx() {
        //camera.RotateImpl(.005f);
        //camera.translateCamera(0,-.005f);

        camera.xView += .005;

        camera.setChanged(true);
    }

    public void camera_debug_value() {
        Log.i("Camera Position", String.valueOf(camera.xPos) + "," + String.valueOf(camera.yPos) + "," + String.valueOf(camera.zPos));
        Log.i("Camera View to", String.valueOf(camera.xView) + "," + String.valueOf(camera.yView) + "," + String.valueOf(camera.zView));
        Log.i("Camera Axis", String.valueOf(camera.xUp) + "," + String.valueOf(camera.yUp) + "," + String.valueOf(camera.zUp));

    }

    private void animateLight() {
        if (!rotatingLight) return;

        // animate light - Do a complete rotation every 5 seconds.
        long time = SystemClock.uptimeMillis() % 5000L;
        float angleInDegrees = (360.0f / 5000.0f) * ((int) time);
        lightPoint.setRotationY(angleInDegrees);
    }

    private void animateCamera() {
        camera.translateCamera(0.0025f, 0f);
    }

    public synchronized void addObject(Object3DData obj) {
        List<Object3DData> newList = new ArrayList<Object3DData>(objects);
        newList.add(obj);
        this.objects = newList;
        requestRender();
    }

    private void requestRender() {
        // request render only if GL view is already initialized
        if (gLView != null) {
            gLView.requestRender();
        }
    }

    public synchronized List<Object3DData> getObjects() {
        return objects;
    }

    public void toggleWireframe() {
        if (this.drawWireframe && !this.drawingPoints) {
            this.drawWireframe = false;
            this.drawingPoints = true;
            makeToastText("Points", Toast.LENGTH_SHORT);
        } else if (this.drawingPoints) {
            this.drawingPoints = false;
            makeToastText("Faces", Toast.LENGTH_SHORT);
        } else {
            makeToastText("Wireframe", Toast.LENGTH_SHORT);
            this.drawWireframe = true;
        }
        requestRender();
    }

    public boolean isDrawWireframe() {
        return this.drawWireframe;
    }

    public boolean isDrawPoints() {
        return this.drawingPoints;
    }

    public void toggleBoundingBox() {
        this.drawBoundingBox = !drawBoundingBox;
        requestRender();
    }

    public boolean isDrawBoundingBox() {
        return drawBoundingBox;
    }

    public boolean isDrawNormals() {
        return drawNormals;
    }

    public void toggleTextures() {
        this.drawTextures = !drawTextures;
        makeToastText("Textures " + this.drawTextures, Toast.LENGTH_SHORT);
    }

    public void toggleLighting() {
        if (this.drawLighting && this.rotatingLight) {
            this.rotatingLight = false;
            makeToastText("Light stopped", Toast.LENGTH_SHORT);
        } else if (this.drawLighting && !this.rotatingLight) {
            this.drawLighting = false;
            makeToastText("Lights off", Toast.LENGTH_SHORT);
        } else {
            this.drawLighting = true;
            this.rotatingLight = true;
            makeToastText("Light on", Toast.LENGTH_SHORT);
        }
        requestRender();
    }

    public void toggleAnimation() {
        if (animateModel && !drawSkeleton) {
            this.drawSkeleton = true;
            makeToastText("Skeleton on", Toast.LENGTH_SHORT);
        } else if (animateModel) {
            this.drawSkeleton = false;
            this.animateModel = false;
            makeToastText("Animation off", Toast.LENGTH_SHORT);
        } else {
            animateModel = true;
            makeToastText("Animation on", Toast.LENGTH_SHORT);
        }
    }

    public boolean isDrawAnimation() {
        return animateModel;
    }

    public void toggleCollision() {
        this.isCollision = !isCollision;
        makeToastText("Collisions: " + isCollision, Toast.LENGTH_SHORT);
    }

    public boolean isDrawTextures() {
        return drawTextures;
    }

    public boolean isDrawLighting() {
        return drawLighting;
    }

    public boolean isDrawSkeleton() {
        return drawSkeleton;
    }

    public boolean isCollision() {
        return isCollision;
    }

    public boolean isAnaglyph() {
        return isAnaglyph;
    }

    @Override
    public void onStart() {
        ContentUtils.setThreadActivity(parent);
    }

    @Override
    public void onLoadComplete(List<Object3DData> datas) {
        // TODO: move texture load to LoaderTask
        for (Object3DData data : datas) {
            if (data.getTextureData() == null && data.getTextureFile() != null) {
                Log.i("LoaderTask", "Loading texture... " + data.getTextureFile());
                try (InputStream stream = ContentUtils.getInputStream(data.getTextureFile())) {
                    if (stream != null) {
                        data.setTextureData(IOUtils.read(stream));
                    }
                } catch (IOException ex) {
                    data.addError("Problem loading texture " + data.getTextureFile());
                }
            }
        }
        // TODO: move error alert to LoaderTask
        List<String> allErrors = new ArrayList<>();
        for (Object3DData data : datas) {
            addObject(data);
            allErrors.addAll(data.getErrors());
        }
        if (!allErrors.isEmpty()) {
            makeToastText(allErrors.toString(), Toast.LENGTH_LONG);
        }
        final String elapsed = (SystemClock.uptimeMillis() - startTime) / 1000 + " secs";
        makeToastText("Build complete (" + elapsed + ")", Toast.LENGTH_LONG);
        ContentUtils.setThreadActivity(null);
    }

    @Override
    public void onLoadError(Exception ex) {
        Log.e("SceneLoader", ex.getMessage(), ex);
        makeToastText("There was a problem building the model: " + ex.getMessage(), Toast.LENGTH_LONG);
        ContentUtils.setThreadActivity(null);
    }

    public Object3DData getSelectedObject() {
        return selectedObject;
    }

    private void setSelectedObject(Object3DData selectedObject) {
        this.selectedObject = selectedObject;

    }

    public void loadTexture(Object3DData obj, Uri uri) throws IOException {
        if (obj == null && objects.size() != 1) {
            makeToastText("Unavailable", Toast.LENGTH_SHORT);
            return;
        }
        obj = obj != null ? obj : objects.get(0);
        obj.setTextureData(IOUtils.read(ContentUtils.getInputStream(uri)));
        this.drawTextures = true;
    }

    public void processTouch(float x, float y) {


        try {

            //  Log.e("Camera",""+camera.yPos);
            if (!block_pan_status && !block_zoom_status && !navigation_mode_status) {
                ModelRenderer mr = gLView.getModelRenderer();
                Object3DData objectToSelect = CollisionDetection.getBoxIntersection(getObjects(), mr.getWidth(), mr.getHeight
                        (), mr.getModelViewMatrix(), mr.getModelProjectionMatrix(), x, y);
                if (objectToSelect != null) {
                    setSelectedObject(null);
                    if (getSelectedObject() == objectToSelect) {
                        Log.i("SceneLoader", "Unselected object " + objectToSelect.getId());
                        //setSelectedObject(null);
                    } else {
                        Log.i("SceneLoader", "Selected object " + objectToSelect.getId());
                        setSelectedObject(objectToSelect);
                        if (selectedObject.getobjClass() == "pin_cube" || selectedObject.getobjClass() == "pin_i_m_here") {
                            Constants.popupbg.setPosition(new float[]{selectedObject.getPositionX(), .2f, selectedObject.getPositionZ()});

                            //   makeToastText("Pin Selected", Toast.LENGTH_SHORT);

                            // selectedObject.setScale(new float[]{selectedObject.getScaleX()+.2f,selectedObject.getScaleY()+.2f,selectedObject.getScaleZ()+.2f});

                            //selectedObject.getPosition()
                            callback.onStoreClick(selectedObject.getId(), false);
                            setSelectedObject(null);
                        } else if (selectedObject.getobjClass() == "parking") {
                            callback.onStoreClick(selectedObject.getId(), true);
                            setSelectedObject(null);
                        }
                    }
                /*if (isCollision()) {
                    Log.d("SceneLoader", "Detecting collision...");

                    float[] point = CollisionDetection.getTriangleIntersection(getObjects(), mr.getWidth(), mr.getHeight
                            (), mr.getModelViewMatrix(), mr.getModelProjectionMatrix(), x, y);
                    if (point != null) {
                        Log.i("SceneLoader", "Drawing intersection point: " + Arrays.toString(point));
                        addObject(Object3DBuilder.buildPoint(point).setColor(new float[]{1.0f, 0f, 0f, 1f}));
                    }
                }*/
                }
            }
        } catch (Exception e) {

        }


    }

    public void processMove(float dx1, float dy1) {
        userHasInteracted = true;
    }


    public interface Callback {

        void onStoreClick(String store_id, Boolean isParking);

        void onStoreChange();

    }


    public void iamhere_transition() {
        try {
            if (imhere_transition_timer <= 1) {

                imhere_transition_timer += .002f;

                float temp_x = lerp(Constants.i_m_here_marker.getPositionX(), marker_new_position[0], imhere_transition_timer);
                float temp_y = lerp(Constants.i_m_here_marker.getPositionY(), marker_new_position[1], imhere_transition_timer);
                float temp_z = lerp(Constants.i_m_here_marker.getPositionZ(), marker_new_position[2], imhere_transition_timer);
                Constants.i_m_here_marker.setPosition(new float[]{temp_x, temp_y, temp_z});

            } else {
                marker_transition_status = false;
                imhere_transition_timer = 0;
            }
        } catch (Exception e) {

        }

    }

}
