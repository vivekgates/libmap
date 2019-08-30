package org.unyde.mapintegrationlib.InternalNavigation.view;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;
import org.unyde.mapintegrationlib.InternalNavigation.Cluster3DMap;
import org.unyde.mapintegrationlib.InternalNavigation.android.GLUtil;
import org.unyde.mapintegrationlib.InternalNavigation.animation.Animator;
import org.unyde.mapintegrationlib.InternalNavigation.demo.SceneLoader;
import org.unyde.mapintegrationlib.InternalNavigation.drawer.DrawerFactory;
import org.unyde.mapintegrationlib.InternalNavigation.drawer.Object3DImpl;
import org.unyde.mapintegrationlib.InternalNavigation.model.AnimatedModel;
import org.unyde.mapintegrationlib.InternalNavigation.model.Camera;
import org.unyde.mapintegrationlib.InternalNavigation.model.Object3D;
import org.unyde.mapintegrationlib.InternalNavigation.model.Object3DData;
import org.unyde.mapintegrationlib.InternalNavigation.services.Object3DBuilder;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Math.PI;


public class ModelRenderer implements GLSurfaceView.Renderer {

    private final static String TAG = ModelRenderer.class.getName();

    // 3D window (parent component)
    private ModelSurfaceView main;
    // width of the screen
    private int width;
    // height of the screen
    private int height;
    // frustrum - nearest pixel
    private static final float near = 1f;
    // frustrum - fartest pixel
    private static final float far = 100f;

    private DrawerFactory drawer;
    // The wireframe associated shape (it should be made of lines only)
    private Map<Object3DData, Object3DData> wireframes = new HashMap<Object3DData, Object3DData>();
    // The loaded textures
    private Map<byte[], Integer> textures = new HashMap<byte[], Integer>();
    // The corresponding opengl bounding boxes and drawer
    private Map<Object3DData, Object3DData> boundingBoxes = new HashMap<Object3DData, Object3DData>();
    // The corresponding opengl bounding boxes
    private Map<Object3DData, Object3DData> normals = new HashMap<Object3DData, Object3DData>();
    private Map<Object3DData, Object3DData> skeleton = new HashMap<>();

    // 3D matrices to project our 3D world
    private final float[] modelProjectionMatrix = new float[16];
    private final float[] modelViewMatrix = new float[16];
    // mvpMatrix is an abbreviation for "Model View Projection Matrix"
    private final float[] mvpMatrix = new float[16];

    // light position required to render with lighting
    private final float[] lightPosInEyeSpace = new float[4];
    /**
     * Whether the info of the model has been written to console log
     */
    private boolean infoLogged = false;

    /**
     * Skeleton Animator
     */
    private Animator animator = new Animator();
    Context context;
    private GLText glText;

    /**
     * Construct a new renderer for the specified surface view
     *
     * @param modelSurfaceView the 3D window
     */
    public ModelRenderer(ModelSurfaceView modelSurfaceView, Context context) {
        this.main = modelSurfaceView;
        this.context = context;
    }

    public float getNear() {
        return near;
    }

    public float getFar() {
        return far;
    }

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        // Set the background frame color
        float[] backgroundColor = new float[]{0.2f, 0.2f, 0.2f, 1.0f};
        //float[] backgroundColor = main.getModelActivity().getBackgroundColor();
        GLES20.glClearColor(backgroundColor[0], backgroundColor[1], backgroundColor[2], backgroundColor[3]);

        // Use culling to remove back faces.
        // Don't remove back faces so we can see them
        GLES20.glEnable(GLES20.GL_CULL_FACE);

        // Enable depth testing for hidden-surface elimination.
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
// Create the GLText
		/*glText = new GLText(unused,context.getAssets());

		// Load the font from file (set size + padding), creates the texture
		// NOTE: after a successful call to this the font is ready for rendering!
		glText.load( "Roboto-Regular.ttf", 14, 2, 2 );  // Create Font (Height: 14 Pixels / X+Y Padding 2 Pixels)
*/
        // Enable blending for combining colors when there is transparency
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);

        // This component will draw the actual models using OpenGL
        drawer = new DrawerFactory();
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        this.width = width;
        this.height = height;

        // Adjust the viewport based on geometry changes, such as screen rotation
        GLES20.glViewport(0, 0, width, height);

        // INFO: Set the camera position (View matrix)
        // The camera has 3 vectors (the position, the vector where we are looking at, and the up position (sky)
        SceneLoader scene = null;
        scene = Cluster3DMap.Companion.getScene();
       /* if (Cluster3DMap.Companion.getScene() != null) {
            scene = Cluster3DMap.Companion.getScene();
        } else if (Cluster3DLocateMapFragment.Companion.getScene() != null) {
            scene = Cluster3DLocateMapFragment.Companion.getScene();
        }*/
        //SceneLoader scene = Cluster3DLocateMapFragment.Companion.getScene();


        if (scene == null) {
            // scene not ready
            return;
        }
        if (scene != null) {
            Camera camera = scene.getCamera();
            Matrix.setLookAtM(modelViewMatrix, 0, camera.xPos, camera.yPos, camera.zPos, camera.xView, camera.yView,
                    camera.zView, camera.xUp, camera.yUp, camera.zUp);

            // the projection matrix is the 3D virtual space (cube) that we want to project
            float ratio = (float) width / height;
            Log.d(TAG, "projection: [" + -ratio + "," + ratio + ",-1,1]-near/far[1,10]");
            float fov = 45; // degrees, try also 45, or different number if you like
            float top =(float) Math.tan(fov * PI / 360.0f) * near;
            float bottom = -top;
            float left = ratio * bottom;
            float right = ratio * top;
            // Matrix.frustumM(modelProjectionMatrix, 0, -ratio, ratio, -1, 1, getNear(), getFar());
            Matrix.frustumM(modelProjectionMatrix, 0, left, right, bottom,top, getNear(), getFar());


            // Calculate the projection and view transformation
            Matrix.multiplyMM(mvpMatrix, 0, modelProjectionMatrix, 0, modelViewMatrix, 0);
        }


    }

    @Override
    public void onDrawFrame(GL10 unused) {

        // Draw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
	/*	unused.glMatrixMode( GL10.GL_MODELVIEW );           // Activate Model View Matrix
		unused.glLoadIdentity();                            // Load Identity Matrix

		// enable texture + alpha blending
		// NOTE: this is required for text rendering! we could incorporate it into
		// the GLText class, but then it would be called multiple times (which impacts performance).
		unused.glEnable( GL10.GL_TEXTURE_2D );              // Enable Texture Mapping
		unused.glEnable( GL10.GL_BLEND );                   // Enable Alpha Blend
		unused.glBlendFunc( GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA );  // Set Alpha Blend Function

		// TEST: render the entire font texture
		unused.glColor4f( 1.0f, 1.0f, 1.0f, 1.0f );         // Set Color to Use
		glText.drawTexture( width, height );            // Draw the Entire Texture

		// TEST: render some strings with the font
		glText.begin( 1.0f, 1.0f, 1.0f, 1.0f );         // Begin Text Rendering (Set Color WHITE)
		glText.draw( "Test String :)", 0, 0 );          // Draw Test String
		glText.draw( "Line 1", 50, 50 );                // Draw Test String
		glText.draw( "Line 2", 100, 100 );              // Draw Test String
		glText.end();                                   // End Text Rendering

		glText.begin( 0.0f, 0.0f, 1.0f, 1.0f );         // Begin Text Rendering (Set Color BLUE)
		glText.draw( "More Lines...", 50, 150 );        // Draw Test String
		glText.draw( "The End.", 50, 150 + glText.getCharHeight() );  // Draw Test String
		glText.end();*/
        SceneLoader scene = null;
        scene = Cluster3DMap.Companion.getScene();
       /* if (Cluster3DMapFragment.Companion.getScene() != null) {
            scene = Cluster3DMapFragment.Companion.getScene();
        } else if (Cluster3DLocateMapFragment.Companion.getScene() != null) {
            scene = Cluster3DLocateMapFragment.Companion.getScene();
        }*/
        //SceneLoader scene = Cluster3DLocateMapFragment.Companion.getScene();

        if (scene == null) {
            // scene not ready
            return;
        }

        // animate scene
        scene.onDrawFrame();

        // recalculate mvp matrix according to where we are looking at now
        Camera camera = scene.getCamera();
        if (camera.hasChanged()) {
            Matrix.setLookAtM(modelViewMatrix, 0, camera.xPos, camera.yPos, camera.zPos, camera.xView, camera.yView,
                    camera.zView, camera.xUp, camera.yUp, camera.zUp);
            // Log.d("Camera", "Changed! :"+camera.ToStringVector());
            Matrix.multiplyMM(mvpMatrix, 0, modelProjectionMatrix, 0, modelViewMatrix, 0);
            camera.setChanged(false);
        }

        // draw light
        if (scene.isDrawLighting()) {

            Object3DImpl lightBulbDrawer = (Object3DImpl) drawer.getPointDrawer();

            float[] lightModelViewMatrix = lightBulbDrawer.getMvMatrix(lightBulbDrawer.getMMatrix(scene.getLightBulb()), modelViewMatrix);

            // Calculate position of the light in eye space to support lighting
            Matrix.multiplyMV(lightPosInEyeSpace, 0, lightModelViewMatrix, 0, scene.getLightPosition(), 0);

            // Draw a point that represents the light bulb
            lightBulbDrawer.draw(scene.getLightBulb(), modelProjectionMatrix, modelViewMatrix, -1, lightPosInEyeSpace);
        }

        List<Object3DData> objects = scene.getObjects();
        for (int i = 0; i < objects.size(); i++) {
            Object3DData objData = null;
            try {
                objData = objects.get(i);
                boolean changed = objData.isChanged();

                Object3D drawerObject = drawer.getDrawer(objData, scene.isDrawTextures(), scene.isDrawLighting(),
                        scene.isDrawAnimation());

                if (!infoLogged) {
                    Log.i("ModelRenderer", "Using drawer " + drawerObject.getClass());
                    infoLogged = true;
                }

                Integer textureId = textures.get(objData.getTextureData());
                if (textureId == null && objData.getTextureData() != null) {
                    Log.i("ModelRenderer", "Loading GL Texture...");
                    ByteArrayInputStream textureIs = new ByteArrayInputStream(objData.getTextureData());
                    textureId = GLUtil.loadTexture(textureIs);
                    textureIs.close();
                    textures.put(objData.getTextureData(), textureId);
                }

                if (objData.getDrawMode() == GLES20.GL_POINTS) {
                    Object3DImpl lightBulbDrawer = (Object3DImpl) drawer.getPointDrawer();
                    lightBulbDrawer.draw(objData, modelProjectionMatrix, modelViewMatrix, GLES20.GL_POINTS, lightPosInEyeSpace);
                } else if (scene.isAnaglyph()) {
                    // TODO: implement anaglyph
                } else if (scene.isDrawWireframe() && objData.getDrawMode() != GLES20.GL_POINTS
                        && objData.getDrawMode() != GLES20.GL_LINES && objData.getDrawMode() != GLES20.GL_LINE_STRIP
                        && objData.getDrawMode() != GLES20.GL_LINE_LOOP) {
                    // Log.d("ModelRenderer","Drawing wireframe model...");
                    try {
                        // Only draw wireframes for objects having faces (triangles)
                        Object3DData wireframe = wireframes.get(objData);
                        if (wireframe == null || changed) {
                            Log.i("ModelRenderer", "Generating wireframe model...");
                            wireframe = Object3DBuilder.buildWireframe(objData);
                            wireframes.put(objData, wireframe);
                        }
                        drawerObject.draw(wireframe, modelProjectionMatrix, modelViewMatrix, wireframe.getDrawMode(),
                                wireframe.getDrawSize(), textureId != null ? textureId : -1, lightPosInEyeSpace);
                    } catch (Error e) {
                        Log.e("ModelRenderer", e.getMessage(), e);
                    }
                } else if (scene.isDrawPoints() || objData.getFaces() == null || !objData.getFaces().loaded()) {
                    drawerObject.draw(objData, modelProjectionMatrix, modelViewMatrix
                            , GLES20.GL_POINTS, objData.getDrawSize(),
                            textureId != null ? textureId : -1, lightPosInEyeSpace);
                } else if (scene.isDrawSkeleton() && objData instanceof AnimatedModel && ((AnimatedModel) objData)
                        .getAnimation() != null) {
                    Object3DData skeleton = this.skeleton.get(objData);
                    if (skeleton == null) {
                        skeleton = Object3DBuilder.buildSkeleton((AnimatedModel) objData);
                        this.skeleton.put(objData, skeleton);
                    }
                    animator.update(skeleton);
                    drawerObject = drawer.getDrawer(skeleton, false, scene.isDrawLighting(), scene
                            .isDrawAnimation());
                    drawerObject.draw(skeleton, modelProjectionMatrix, modelViewMatrix, -1, lightPosInEyeSpace);
                } else {
                    drawerObject.draw(objData, modelProjectionMatrix, modelViewMatrix,
                            textureId != null ? textureId : -1, lightPosInEyeSpace);
                }

                // Draw bounding box
                if (scene.isDrawBoundingBox() || scene.getSelectedObject() == objData) {
                    Object3DData boundingBoxData = boundingBoxes.get(objData);
                    if (boundingBoxData == null || changed) {
                        boundingBoxData = Object3DBuilder.buildBoundingBox(objData);
                        boundingBoxes.put(objData, boundingBoxData);
                    }
                    Object3D boundingBoxDrawer = drawer.getBoundingBoxDrawer();
                    boundingBoxDrawer.draw(boundingBoxData, modelProjectionMatrix, modelViewMatrix, -1, null);
                }

                // Draw normals
                if (scene.isDrawNormals()) {
                    Object3DData normalData = normals.get(objData);
                    if (normalData == null || changed) {
                        normalData = Object3DBuilder.buildFaceNormals(objData);
                        if (normalData != null) {
                            // it can be null if object isnt made of triangles
                            normals.put(objData, normalData);
                        }
                    }
                    if (normalData != null) {
                        Object3D normalsDrawer = drawer.getFaceNormalsDrawer();
                        normalsDrawer.draw(normalData, modelProjectionMatrix, modelViewMatrix, -1, null);
                    }
                }
                // TODO: enable this only when user wants it
                // obj3D.drawVectorNormals(result, modelViewMatrix);
            } catch (Exception ex) {
                Log.e("ModelRenderer", "There was a problem rendering the object '" + "':" + ex.getMessage(), ex);
            }
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public float[] getModelProjectionMatrix() {
        return modelProjectionMatrix;
    }

    public float[] getModelViewMatrix() {
        return modelViewMatrix;
    }
}