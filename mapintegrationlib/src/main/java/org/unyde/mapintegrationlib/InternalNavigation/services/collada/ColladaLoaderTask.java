package org.unyde.mapintegrationlib.InternalNavigation.services.collada;

import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import org.unyde.mapintegrationlib.InternalNavigation.model.Object3DData;
import org.unyde.mapintegrationlib.InternalNavigation.services.LoaderTask;
import org.unyde.mapintegrationlib.InternalNavigation.services.collada.entities.AnimatedModelData;
import org.unyde.mapintegrationlib.InternalNavigation.services.collada.loader.ColladaLoader;

import java.io.IOException;
import java.net.URL;
import java.util.List;


public class ColladaLoaderTask extends LoaderTask {

    AnimatedModelData modelData;

    public ColladaLoaderTask(AppCompatActivity parent, Uri uri, Callback callback) {
        super(parent, uri, callback);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected List<Object3DData> build() throws IOException {
        // Parse STL
        Object[] ret = ColladaLoader.buildAnimatedModel(new URL(uri.toString()));
        List<Object3DData> datas = (List<Object3DData>) ret[1];
        modelData = (AnimatedModelData) ret[0];
        return datas;
    }

    @Override
    protected void build(List<Object3DData> datas) throws Exception {
        ColladaLoader.populateAnimatedModel(new URL(uri.toString()), datas, modelData);
        if (datas.size() == 1) {
            datas.get(0).centerAndScale(5, new float[]{0, 0, 0});
        } else {
            Object3DData.centerAndScale(datas, 5, new float[]{0, 0, 0});
        }
    }

}
