package smooth.plugins.cordova.crop;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;

import android.database.Cursor;

import android.view.View;
import android.graphics.Color;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;

import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageActivity;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class Crop extends CordovaPlugin {

    private String imageUrl;
    private String activityTitle;
    private boolean isZoomable;
    private boolean showCropOverlay;
    private boolean multiTouchEnabled;
    private boolean setFixAspectRatio;
    private boolean allowRotation;
    private boolean allowFlipping;
    private int aspectRatioX;
    private int aspectRatioY;
    private int reqWidth;
    private int reqHeight;
    private int outputCompressQuality;
    private int cropShape;
    private int guideline;
    private int scaleType;
    private int borderCornerColor;
    private int guidelinesColor;
    private int backgroundColor;
    private int activityMenuIconColor;
    private CropImageView.CropShape[] cropShapes = {
        CropImageView.CropShape.RECTANGLE,
        CropImageView.CropShape.OVAL
    };
    private CropImageView.Guidelines[] guidelines = {
        CropImageView.Guidelines.OFF,
        CropImageView.Guidelines.ON_TOUCH,
        CropImageView.Guidelines.ON
    };
    private CropImageView.ScaleType[] scaleTypes = {
        CropImageView.ScaleType.FIT_CENTER,
        CropImageView.ScaleType.CENTER,
        CropImageView.ScaleType.CENTER_CROP,
        CropImageView.ScaleType.CENTER_INSIDE
    };

    private CallbackContext callbackContext;

    @Override
    public boolean execute(String action, final JSONArray args, final CallbackContext callbackContext) throws JSONException {
        if (action.equals("open")) {
            this.callbackContext = callbackContext;
            parseOptions(args);
            return true;
        } else if (action.equals("getImageDimension")) {
            this.callbackContext = callbackContext;
            parseImagePaths(args);
            return true;
        }
        return false;
    }

    private void parseOptions(JSONArray jsonArray) throws JSONException {
        JSONObject options = jsonArray.getJSONObject(0);
        imageUrl = options.getString("url");
        activityTitle = options.getString("title");
        cropShape = options.getInt("cropShape");
        guideline = options.getInt("guideline");
        scaleType = options.getInt("scaleType");
        aspectRatioX = options.getInt("ratioX");
        aspectRatioY = options.getInt("ratioY");
        reqWidth = options.getInt("reqWidth");
        reqHeight = options.getInt("reqHeight");
        isZoomable = options.getBoolean("autoZoomEnabled");
        showCropOverlay = options.getBoolean("showCropOverlay");
        multiTouchEnabled = options.getBoolean("multiTouchEnabled");
        setFixAspectRatio = options.getBoolean("setFixAspectRatio");
        outputCompressQuality = options.getInt("outputCompressQuality");
        allowRotation = options.getBoolean("allowRotation");
        allowFlipping = options.getBoolean("allowFlipping");
        borderCornerColor = Color.parseColor("#FF0000");
        guidelinesColor = Color.argb(175, 0, 0,0);
        backgroundColor = Color.argb(175, 0, 0,0);
        activityMenuIconColor = Color.rgb(0, 0,0);
        Resources activityRes = cordova.getActivity().getResources();
        String packageName = cordova.getActivity().getPackageName();
        int cropIconId = activityRes.getIdentifier("crop", "drawable", packageName);
        Drawable cropIconDrawable = activityRes.getDrawable(cropIconId);

        cordova.setActivityResultCallback(this);
        CropImage.activity(Uri.parse(imageUrl))
                .setCropShape(cropShapes[cropShape])
                .setScaleType(scaleTypes[scaleType])
                .setGuidelines(guidelines[guideline])
                .setShowCropOverlay(showCropOverlay)
                .setAutoZoomEnabled(isZoomable)
                .setMultiTouchEnabled(multiTouchEnabled)
                .setFixAspectRatio(setFixAspectRatio)
                .setAspectRatio(aspectRatioX, aspectRatioY)
                .setOutputCompressQuality(outputCompressQuality)
                .setRequestedSize(reqWidth,reqHeight)
                .setAllowRotation(allowRotation)
                .setAllowFlipping(allowFlipping)
                .setCropMenuCropButtonIcon(cropIconId)
                .setBorderCornerColor(borderCornerColor)
                .setActivityTitle(activityTitle)
                .setGuidelinesColor(guidelinesColor)
                .setBackgroundColor(backgroundColor)
                .setActivityMenuIconColor(activityMenuIconColor)
                //.setSnapRadius(5)
                //.setTouchRadius(50)
                //.setNoOutputImage(false)
                //.setMaxZoom(5)
                //.setInitialCropWindowPaddingRatio(0.2)
                //.setBorderLineColor()
                //.setBorderCornerThickness(3)
                //.setBorderCornerOffset(6)
                //.setBorderCornerLength(16)
                //.setGuidelinesThickness(2)
                //.setMinCropWindowSize(125,125)
                //.setMinCropResultSize(125,125)
                //.setMaxCropResultSize(99999, 99999)
                //.setOutputUri()
                //.setOutputCompressFormat()
                //.setInitialCropWindowRectangle()
                //.setInitialRotation()
                //.setAllowCounterRotation(false)
                //.setRotationDegrees(90)
                //.setFlipHorizontally()
                //.setFlipVertically()
                //.setCropMenuCropButtonTitle()
                .start(cordova.getActivity());

    }

    private void parseImagePaths(JSONArray jsonArray) throws JSONException {
        JSONArray imagePathsArr = jsonArray.getJSONArray(0);
        JSONArray successArr = new JSONArray();

        for (int i = 0; i < imagePathsArr.length(); i++) {
            JSONObject img = new JSONObject();
            String imgPath = imagePathsArr.getString(i);
            String finalImgPath = getRealPathFromURI(cordova.getActivity().getApplicationContext(), Uri.parse(imgPath));
            img = imageRatioCalculation(Uri.parse(finalImgPath),false);
            img.put("imgPath", imgPath);
            img.put("imgPathAbsolute", finalImgPath);
            successArr.put(img);
        }

        callbackContext.success(successArr);
    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == Activity.RESULT_OK) {
                Uri resultUri = result.getUri();
                try {
                    JSONObject successJson = imageRatioCalculation(resultUri,true);
                    successJson.put("imgPath", resultUri.toString());
                    callbackContext.success(successJson);
                } catch (JSONException e) {
                    callbackContext.error(e.getMessage());
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                callbackContext.error(error.getMessage());
            }
        }
    }
    private JSONObject imageRatioCalculation(Uri uri,Boolean base64) throws JSONException {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(new File(uri.getPath()).getAbsolutePath(), options);
        int imageHeight = options.outHeight;
        int imageWidth = options.outWidth;

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("width", imageWidth);
        jsonObject.put("height", imageHeight);
        if(base64){
            try{
                InputStream inputStream = cordova.getActivity().getApplicationContext().getContentResolver().openInputStream(uri);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] imageBytes = baos.toByteArray();
                String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);

                jsonObject.put("base64", imageString);
            }catch(FileNotFoundException e){

            }
        }
        return jsonObject;
    }
}
