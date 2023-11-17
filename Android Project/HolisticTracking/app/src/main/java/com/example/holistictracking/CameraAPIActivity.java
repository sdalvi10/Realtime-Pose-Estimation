package com.example.holistictracking;

import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Base64;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;
public class CameraAPIActivity extends AppCompatActivity implements SurfaceHolder.Callback {
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final String API_BASE_URL = "http://example.com/api/";

    private Camera camera;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private boolean isPreviewing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_apiactivity);

        // Request camera permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
        }

        // Initialize the surface view and holder
        surfaceView = findViewById(R.id.surface_view);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Open the camera if permission has been granted and the surface view is ready
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && isPreviewing) {
            openCamera();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Release the camera when the activity is paused
        if (camera != null) {
            camera.stopPreview();
            camera.release();
            camera = null;
            isPreviewing = false;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // The surface view has been created, start the camera preview if permission has been granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            openCamera();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // The surface view has changed, adjust the camera preview size if necessary
        if (camera != null && isPreviewing) {
            camera.stopPreview();
            Camera.Parameters parameters = camera.getParameters();
            Camera.Size previewSize = getBestPreviewSize(parameters, width, height);
            parameters.setPreviewSize(previewSize.width, previewSize.height);
            camera.setParameters(parameters);
            try {
                camera.setPreviewDisplay(surfaceHolder);
                camera.startPreview();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // The surface view has been destroyed, release the camera
        if (camera != null) {

            camera.stopPreview();
            camera.release();
            camera = null;
            isPreviewing = false;
        }
    }

    private void openCamera() {
        // Open the camera and start the preview
        camera = Camera.open();
        Camera.Parameters parameters = camera.getParameters();
        Camera.Size previewSize = getBestPreviewSize(parameters, surfaceView.getWidth(), surfaceView.getHeight());
        parameters.setPreviewSize(previewSize.width, previewSize.height);
        camera.setParameters(parameters);
        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
            isPreviewing = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Camera.Size getBestPreviewSize(Camera.Parameters parameters, int width, int height) {
        // Find the best camera preview size for the given width and height
        Camera.Size bestSize = null;
        for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
            if (size.width <= width && size.height <= height) {
                if (bestSize == null || size.width > bestSize.width) {
                    bestSize = size;
                }
            }
        }
        return bestSize;
    }

    private void sendCameraOutputToApi(Bitmap bitmap) {
        // Convert the bitmap to a Base64-encoded string
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);

        // Create a Retrofit instance for the API endpoint
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        ApiService apiService = retrofit.create(ApiService.class);

        // Send the camera output to the API endpoint
        Call<String> call = apiService.sendCameraOutput(encodedImage);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                // Check if the API request was successful
                if (response.isSuccessful()) {
                    // Parse the API response JSON
                    String jsonResponse = response.body();
//                    JsonObject jsonObject = new JsonParser().parse(jsonResponse).getAsJsonObject();
//                    String message = jsonObject.get("message").getAsString();
                    Toast.makeText(CameraAPIActivity.this, "Successfull", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CameraAPIActivity.this, "Failed to send camera output to API", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(CameraAPIActivity.this, "Failed to send camera output to API", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private class CaptureCallback implements Camera.PictureCallback {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            // Convert the camera output data to a bitmap
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

            // Send the camera output to the API endpoint
            sendCameraOutputToApi(bitmap);

            // Restart the camera preview
            camera.startPreview();
        }
    }
}