package org.unyde.mapintegrationlib.InternalNavigation.demo;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import org.unyde.mapintegrationlib.ApplicationContext;
import org.unyde.mapintegrationlib.InternalNavigation.android.ContentUtils;
import org.unyde.mapintegrationlib.InternalNavigation.model.Object3DData;
import org.unyde.mapintegrationlib.InternalNavigation.services.Object3DBuilder;
import org.unyde.mapintegrationlib.InternalNavigation.view.ModelSurfaceView;
import org.unyde.mapintegrationlib.database.DatabaseClient;
import org.unyde.mapintegrationlib.database.entity.MallMapMain;
import org.unyde.mapintegrationlib.util.Constants;

import java.util.ArrayList;
import java.util.List;

public class MainSceneLoader extends SceneLoader {


    public float[][] pin_pointer = {{-1, 5, 0}, {0, .5f, 0}};

    Callback callaback;
    private List<MallMapMain> add_data;
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

                            add_data = DatabaseClient.Companion.getInstance(ApplicationContext.get().getApplicationContext()).getDb().mallMapMain().all(clusterId);
                            for (int j = 0; j < add_data.size(); j++) {

                                try {
                                    Object3DData obj_parking2_floor = Object3DBuilder.loadV5(parent, Uri.parse(add_data.get(j).getLocal_pathImage()));
                                    obj_parking2_floor.centerAndScale(28.0f);
                                    obj_parking2_floor.setobjClass(add_data.get(j).getFloor_alias());
                                    obj_parking2_floor.setId(add_data.get(j).getFloor_alias());
                                    obj_parking2_floor.setIndex(add_data.get(j).getFloor_number());
                                    obj_parking2_floor.setPath( add_data.get(j).getLocal_pathImage());
                                    Constants.floor_model.add(obj_parking2_floor);
                                    addObject(obj_parking2_floor);
                                } catch (Exception e) {
                                    Log.e("MAINSCENELOADER", "" + e.getMessage());
                                }

                            }


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
