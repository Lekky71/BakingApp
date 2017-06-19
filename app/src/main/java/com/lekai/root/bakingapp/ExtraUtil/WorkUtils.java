package com.lekai.root.bakingapp.ExtraUtil;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by oluwalekefakorede on 6/15/17.
 */

public class WorkUtils {

    public static boolean isOnline(Context context){
        ConnectivityManager connectivityManager= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                || connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;
    }

    public static void loadImageFromResourceInto(Context c, ImageView imageView, String url)
    {
        Picasso.with(c).load(url).into(imageView);
    }
}
