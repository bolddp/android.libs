package idimsoftware.www.androidcommon;

import android.graphics.Bitmap;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.idimsoftware.www.androidcommon.web.BitmapTask;
import com.idimsoftware.www.androidcommon.web.BitmapTaskListener;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by Daniel on 2016-08-20.
 */
@RunWith(AndroidJUnit4.class)
public class BitmapTaskTests {

    private static final String TAG = "BitmapTaskTests";
    private static Bitmap assertBitmap;
    private static String errorMessage;

    @Test
    public void shouldDownloadBitmap() throws Throwable {

        final CountDownLatch signal = new CountDownLatch(1);
        assertBitmap = null;

        BitmapTask task = new BitmapTask(new BitmapTaskListener() {
            @Override
            public void onBitmapDownloaded(Bitmap bitmap) {
                assertBitmap = bitmap;
                signal.countDown();
            }

            @Override
            public void onError(int statusCode, String message) {
                Log.d(TAG, message);
                signal.countDown();
            }
        });

        task.execute("https://s3.eu-central-1.amazonaws.com/gpsquiz-teststorage/images/unittest_image");

        signal.await(5, TimeUnit.SECONDS);
        Assert.assertNotNull(assertBitmap);
        Assert.assertEquals(144, assertBitmap.getWidth());
        Assert.assertEquals(144, assertBitmap.getHeight());
    }

    @Test
    public void shouldHandleMissingImage() throws Throwable {
        final CountDownLatch signal = new CountDownLatch(1);
        assertBitmap = null;
        errorMessage = "";

        BitmapTask task = new BitmapTask(new BitmapTaskListener() {
            @Override
            public void onBitmapDownloaded(Bitmap bitmap) {
                assertBitmap = bitmap;
                signal.countDown();
            }

            @Override
            public void onError(int statusCode, String message) {
                Log.d(TAG, message);
                errorMessage = message;
                signal.countDown();
            }
        });

        task.execute("https://s3.eu-central-1.amazonaws.com/gpsquiz-teststorage/images/unittest_image_fail");

        signal.await(5, TimeUnit.SECONDS);
        Assert.assertNull(assertBitmap);
        Assert.assertTrue(errorMessage.length() > 0);
    }
}
