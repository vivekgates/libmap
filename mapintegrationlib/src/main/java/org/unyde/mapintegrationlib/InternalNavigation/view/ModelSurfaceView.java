package org.unyde.mapintegrationlib.InternalNavigation.view;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import androidx.fragment.app.Fragment;
import org.unyde.mapintegrationlib.InternalNavigation.controller.TouchController;


public class ModelSurfaceView extends GLSurfaceView {

	private Fragment parent;
	private ModelRenderer mRenderer;
	private TouchController touchHandler;
	public static Context context;

	public ModelSurfaceView(Context context, AttributeSet attribs) {
		super(context, attribs);
		// Create an OpenGL ES 2.0 context.
		setEGLContextClientVersion(2);

		// This is the actual renderer of the 3D space
		mRenderer = new ModelRenderer(this,context);
		setRenderer(mRenderer);

		// Render the view only when there is a change in the drawing data
		// TODO: enable this?
		// setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

		touchHandler = new TouchController(this, mRenderer);
	}

	public ModelSurfaceView(Fragment parent) {
		super(parent.getActivity());

		// parent component
		//this.parent = parent;


	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return touchHandler.onTouchEvent(event);
	}

	public Fragment getModelActivity() {
		return parent;
	}

	public ModelRenderer getModelRenderer(){
		return mRenderer;
	}

}