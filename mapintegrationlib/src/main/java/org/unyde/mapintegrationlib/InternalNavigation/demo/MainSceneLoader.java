package org.unyde.mapintegrationlib.InternalNavigation.demo;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import org.unyde.mapintegrationlib.InternalNavigation.android.ContentUtils;
import org.unyde.mapintegrationlib.InternalNavigation.model.Object3DData;
import org.unyde.mapintegrationlib.InternalNavigation.services.Object3DBuilder;
import org.unyde.mapintegrationlib.InternalNavigation.view.ModelSurfaceView;
import org.unyde.mapintegrationlib.util.Constants;

import java.util.ArrayList;
import java.util.List;

public class MainSceneLoader extends SceneLoader {


    public float[][] pin_pointer = {{-1, 5, 0}, {0, .5f, 0}};

    Callback callaback;
   // private List<FilePathListMainDevice> add_data;
    String clusterId;
    public MainSceneLoader(AppCompatActivity fragment) {
        super(fragment);
    }

    public MainSceneLoader(AppCompatActivity fragment, Uri paramUri, int paramType, ModelSurfaceView gLView, Callback callback, String cluster_id) {
        super(fragment, paramUri, paramType, gLView, callback,cluster_id);
        this.callaback = callback;
        this.clusterId = cluster_id;
    }

    // TODO: fix this warning
    @SuppressLint("StaticFieldLeak")
    public void init() {
        super.init();

        try {
            Log.e("MAINSCENELOADER_En", "ENTER");
            Constants.floor_model.clear();
            Constants.floor_bg = null;
            Constants.dbuilding = null;
            Constants.compass = null;
            Constants.i_m_here_marker = null;
            Constants.char_byte_data.clear();
        } catch (Exception e) {
            Log.e("MainScenceLoader", e.getMessage());
        }

        if (Constants.floor_model.size() <= 0) {

            new AsyncTask<Void, Void, Void>() {

                List<Exception> errors = new ArrayList<>();

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                }

                @Override
                protected Void doInBackground(Void... params) {
                    try {

                        Log.e("MAINSCENELOADER_STA", "START");
                        try {
                            ContentUtils.setThreadActivity(parent);
                            ContentUtils.provideAssets(parent,clusterId);

                            scene_bg_plane = Object3DBuilder.loadV5_bg(parent, Uri.parse("models/back_plane.obj"));
                            scene_bg_plane.setPosition(new float[]{0f, -.1f, 0f});
                            Constants.floor_bg = scene_bg_plane;


                            Log.e("MAINSCENELOADER_FI", "Finish");

                        } catch (Exception ex) {
                            errors.add(ex);
                        }

                    } catch (Exception ex) {
                        errors.add(ex);
                    } finally {
                        ContentUtils.setThreadActivity(null);
                        ContentUtils.clearDocumentsProvided();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void result) {
                    super.onPostExecute(result);
                    pin_drop_anim_status = true;
                    callaback.onStoreClick("", false);
                    if (!errors.isEmpty()) {
                        StringBuilder msg = new StringBuilder("There was a problem loading the data");
                        for (Exception error : errors) {
                            Log.e("Example", error.getMessage(), error);
                            msg.append("\n" + error.getMessage());
                        }
                        Toast.makeText(parent.getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                    }
                }
            }.execute();
        } else {
            callaback.onStoreClick("", false);
        }

    }


}
