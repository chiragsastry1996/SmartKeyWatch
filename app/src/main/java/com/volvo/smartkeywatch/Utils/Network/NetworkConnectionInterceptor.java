package com.volvo.smartkeywatch.Utils.Network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.RelativeLayout;

import com.volvo.smartkeywatch.Utils.GlobalClass;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class NetworkConnectionInterceptor implements Interceptor {

    private Context mContext;
    RelativeLayout mRelativeLayout;

    public NetworkConnectionInterceptor(Context context, RelativeLayout relativeLayout) {
        mContext = context;
        mRelativeLayout = relativeLayout;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        if (!isConnected()) {
            GlobalClass.snackbar(mRelativeLayout, "No internet, please try again after connecting to internet");
            throw new NoConnectivityException();
//             Throwing our custom exception 'NoConnectivityException'

        }

        Request.Builder builder = chain.request().newBuilder();
        return chain.proceed(builder.build());
    }

    private boolean isConnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
        return (netInfo != null && netInfo.isConnected());
    }

    public class NoConnectivityException extends IOException {
        @Override
        public String getMessage() {
            return "No internet, please try again with internet";
        }
    }
}