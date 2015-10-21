package co.vulcanus.dux.util;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by ryan_turner on 10/20/15.
 */
public final class InternalStorage{
    //        try {
//            // Save the list of entries to internal storage
//            //InternalStorage.writeObject(this, Constants.SETTINGS_KEY, settings);
//
//            // Retrieve the list from internal storage
//            Settings settings = (Settings) InternalStorage.readObject(getContext(), Constants.SETTINGS_KEY);
//
//            Log.d(Constants.LOG_TAG, "Got settings verison: " + settings.getSettingsVersion());
//        } catch (IOException e) {
//            Log.e(Constants.LOG_TAG, e.getMessage());
//        } catch (ClassNotFoundException e) {
//            Log.e(Constants.LOG_TAG, e.getMessage());
//        }
    private InternalStorage() {}

    public static void writeObject(Context context, String key, Object object) throws IOException {
        FileOutputStream fos = context.openFileOutput(key, Context.MODE_PRIVATE);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(object);
        oos.close();
        fos.close();
    }

    public static Object readObject(Context context, String key) throws IOException,
            ClassNotFoundException {
        FileInputStream fis = context.openFileInput(key);
        ObjectInputStream ois = new ObjectInputStream(fis);
        Object object = ois.readObject();
        return object;
    }
}