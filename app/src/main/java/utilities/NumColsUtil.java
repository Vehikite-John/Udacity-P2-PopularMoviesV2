package utilities;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by jdavet on 12/31/2016.
 */

public class NumColsUtil {
    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (dpWidth / 180);
        return noOfColumns;
    }
}
