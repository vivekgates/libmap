package org.unyde.mapintegrationlib.permission;

import android.content.Context;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import java.util.List;

public class TedRx2Permission extends TedPermissionBase {

    public static Builder with(Context context) {
        return new Builder(context);
    }

    public static class Builder extends PermissionBuilder<Builder> {

        private Builder(Context context) {
            super(context);
        }

        public Single<TedPermissionResult> request() {
            return Single.create(new SingleOnSubscribe<TedPermissionResult>() {
                @Override
                public void subscribe(final SingleEmitter<TedPermissionResult> emitter) throws Exception {
                    PermissionListener listener = new PermissionListener() {
                        @Override
                        public void onPermissionGranted() {
                            emitter.onSuccess(new TedPermissionResult(null));
                        }

                        @Override
                        public void onPermissionDenied(List<String> deniedPermissions) {
                            emitter.onSuccess(new TedPermissionResult(deniedPermissions));
                        }
                    };

                    try {
                        setPermissionListener(listener);
                        checkPermissions();
                    } catch (Exception exception) {
                        emitter.onError(exception);
                    }
                }
            });
        }
    }

}
