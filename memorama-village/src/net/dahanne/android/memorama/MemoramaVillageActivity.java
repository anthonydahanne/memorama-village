/**
 *  Copyright (c) 2012 Anthony Dahanne
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package net.dahanne.android.memorama;

import java.io.InputStream;
import java.net.URL;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import net.dahanne.memorama.client.AjaxInterfaceClient;
import net.dahanne.memorama.client.ImageClient;
import net.dahanne.memorama.client.model.ImageElement;
import net.dahanne.memorama.client.model.PhotoElement;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

/**
 * 
 * @author Anthony Dahanne
 * 
 */
public class MemoramaVillageActivity extends Activity {

    private TouchImageView imageView;
    private ProgressDialog progressDialog;
    private Toast toast;

    private int currentCamera;
    private int mYear;
    private int mMonth;
    private int mDay;
    private int mHour;
    private int mMinute;
    private AjaxInterfaceClient ajaxInterfaceClient;
    private ImageClient imageClient;
    private ImageElement imageElement;

    private static final String TEXT_PLAIN = "text/plain";
    private static final int DATE_DIALOG_ID = 0;
    private static final int TIME_DIALOG_ID = 1;
    private static final String TAG = "MVA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ajaxInterfaceClient = new AjaxInterfaceClient();
        imageClient = new ImageClient();
        imageView = new TouchImageView(this);
        imageView.setMaxZoom(4f);
        imageView.setFullImage(this);
        setContentView(imageView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("MemoramaVillageActivity", "onResuming");
        currentCamera = 1;
        loadingPicture();

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("MemoramaVillageActivity", "onPausing");
    }

    private void loadingPicture() {
        progressDialog = ProgressDialog.show(MemoramaVillageActivity.this, getString(R.string.please_wait), getString(R.string.loading_latest_photo)
                + currentCamera, true);
        new GetLatestPhotoElementElementTask().execute();

    }

    private final class DownloadImageTask extends AsyncTask<Object, Void, Bitmap> {
        private String exceptionMessage = null;
        private Date timestamp;

        @Override
        protected Bitmap doInBackground(Object... photoElements) {
            String fullUrl = (String) photoElements[0];
            timestamp = (Date) photoElements[1];
            Log.i(TAG, "Downloading picture from " + fullUrl);
            Bitmap result = null;
            try {
                URL url = new URL(fullUrl);
                InputStream openStream = url.openStream();
                result = BitmapFactory.decodeStream(openStream);
            } catch (Exception e) {
                exceptionMessage = e.getMessage() + " url : " + fullUrl;
                Log.d(TAG, exceptionMessage);
            }
            return result;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            progressDialog.dismiss();
            if (exceptionMessage != null) {
                alertConnectionProblem(exceptionMessage);
                return;
            }
            imageView.setImageBitmap(result);
            String formattedTime = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.LONG).format(timestamp);
            toast = Toast.makeText(MemoramaVillageActivity.this, formattedTime, Toast.LENGTH_LONG);
            toast.show();
        }
    }

    private final class GetLatestPhotoElementElementTask extends AsyncTask<Object, Void, PhotoElement> {
        private String exceptionMessage = null;

        @Override
        protected PhotoElement doInBackground(Object... urls) {
            PhotoElement result = null;
            Log.i(TAG, "Loading lastest photoElement for camera " + currentCamera);
            try {
                PhotoElement photo = ajaxInterfaceClient.getLatestPhoto(currentCamera);
                if (photo == null) {
                    exceptionMessage = "No photo returned when trying to load camera " + currentCamera + " with latest shot.";
                    return null;
                }
                result = photo;
            } catch (Exception e) {
                exceptionMessage = e.getMessage();
                Log.d(TAG, e.getMessage());
            }
            return result;
        }

        @Override
        protected void onPostExecute(PhotoElement result) {
            progressDialog.dismiss();
            if (exceptionMessage != null) {
                alertConnectionProblem(exceptionMessage);
                return;
            }
            progressDialog = ProgressDialog.show(MemoramaVillageActivity.this, getString(R.string.please_wait), getString(R.string.loading_metadata),
                    true);
            new GetImageElementFromIdTask().execute(result.getId());

        }
    }

    private final class FindPhotoElementByDateTask extends AsyncTask<Object, Void, PhotoElement> {
        private String exceptionMessage = null;

        @Override
        protected PhotoElement doInBackground(Object... urls) {
            Date date = (Date) urls[0];
            Log.i(TAG, "Loading photoElement with date " + date + " and camera " + currentCamera);
            PhotoElement result = null;
            try {
                PhotoElement photoElement = ajaxInterfaceClient.findPhotoByDate(currentCamera, date);
                if (photoElement == null) {
                    exceptionMessage = "No photo returned when trying to load camera " + currentCamera + " with date" + date;
                    return null;
                }
                result = photoElement;
            } catch (Exception e) {
                exceptionMessage = e.getMessage();
                Log.d(TAG, e.getMessage());
            }
            return result;
        }

        @Override
        protected void onPostExecute(PhotoElement result) {
            progressDialog.dismiss();
            if (exceptionMessage != null) {
                alertConnectionProblem(exceptionMessage);
                return;
            }
            progressDialog = ProgressDialog.show(MemoramaVillageActivity.this, getString(R.string.please_wait), getString(R.string.loading_metadata),
                    true);
            new GetImageElementFromIdTask().execute(result.getId());
        }
    }

    private final class GetImageElementFromIdTask extends AsyncTask<Object, Void, Void> {
        private String exceptionMessage = null;

        @Override
        protected Void doInBackground(Object... urls) {
            int id = (Integer) urls[0];
            Log.i(TAG, "Loading imageElement with id" + id);
            try {
                imageElement = imageClient.getImageById(id);
                if (imageElement == null) {
                    exceptionMessage = "No photo returned when trying to load camera " + currentCamera + " with photo id" + id;
                }
            } catch (Exception e) {
                exceptionMessage = e.getMessage();
                Log.d(TAG, e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            progressDialog.dismiss();
            if (exceptionMessage != null) {
                alertConnectionProblem(exceptionMessage);
                return;
            }
            progressDialog = ProgressDialog.show(MemoramaVillageActivity.this, getString(R.string.please_wait),
                    getString(R.string.downloading_photo),
                    true);
            new DownloadImageTask().execute(imageElement.getFullUrl(), imageElement.getTimestamp());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_full_image, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        switch (item.getItemId()) {

        case R.id.camera1:
            toast = Toast.makeText(MemoramaVillageActivity.this, getString(R.string.camera1), Toast.LENGTH_LONG);
            toast.show();
            currentCamera = 1;
            loadingPicture();
            break;

        case R.id.camera2:
            toast = Toast.makeText(MemoramaVillageActivity.this, getString(R.string.camera2), Toast.LENGTH_LONG);
            toast.show();
            currentCamera = 2;
            loadingPicture();
            break;

        case R.id.camera3:
            toast = Toast.makeText(MemoramaVillageActivity.this, getString(R.string.camera3), Toast.LENGTH_LONG);
            toast.show();
            currentCamera = 3;
            loadingPicture();
            break;

        case R.id.camera4:
            toast = Toast.makeText(MemoramaVillageActivity.this, getString(R.string.camera4), Toast.LENGTH_LONG);
            toast.show();
            currentCamera = 4;
            loadingPicture();
            break;

        case R.id.search:
            // get the current date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            showDialog(DATE_DIALOG_ID);
            System.out.println(mYear + mMonth + mDay);
            break;

        case R.id.about:
            intent = new Intent(this, About.class);
            startActivity(intent);
            break;

        case R.id.share_image:
            intent.setType(TEXT_PLAIN);
            intent.putExtra(Intent.EXTRA_TEXT, imageElement.getFullUrl());
            startActivity(Intent.createChooser(intent, getString(R.string.choose_action)));
            break;
        }
        return false;
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
        case DATE_DIALOG_ID:
            return new DatePickerDialog(this, mDateSetListener, mYear, mMonth, mDay);
        case TIME_DIALOG_ID:
            return new TimePickerDialog(this, mTimeSetListener, mHour, mMinute, true);
        }
        return null;
    }

    // the callback received when the user "sets" the time in the dialog
    private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            mHour = hourOfDay;
            mMinute = minute;
            System.out.println(new StringBuilder().append(pad(mHour)).append(":").append(pad(mMinute)));
            GregorianCalendar calendar = new GregorianCalendar(mYear, mMonth, mDay, mHour, mMinute);

            new FindPhotoElementByDateTask().execute(calendar.getTime());
        }
    }; // the callback received when the user "sets" the date in the dialog
    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            System.out.println(new StringBuilder()
            // Month is 0 based so add 1
                    .append(mMonth + 1).append("-").append(mDay).append("-").append(mYear).append(" "));
            showDialog(TIME_DIALOG_ID);
        }
    };

    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }


    private void alertConnectionProblem(String exceptionMessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MemoramaVillageActivity.this);
        // if there was an exception thrown, show it, or say to verify
        // settings
        String message = exceptionMessage;
        builder.setTitle(R.string.problem).setMessage(message).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            setResult(RESULT_OK);
            this.finish();
            return true;
        }
        return false;
    }

    void moveRight() {
        progressDialog = ProgressDialog.show(MemoramaVillageActivity.this, getString(R.string.please_wait), getString(R.string.loading_metadata),
                true);
        new GetImageElementFromIdTask().execute(imageElement.getPrevious());
    }

    void moveLeft() {
        if (imageElement.getNext() == 0) {
            if (toast == null) {
                toast = Toast.makeText(MemoramaVillageActivity.this, getString(R.string.no_latest_photo), Toast.LENGTH_SHORT);
            } else {
                toast.setText(getString(R.string.no_latest_photo));
            }
            toast.show();

            return;
        }
        progressDialog = ProgressDialog.show(MemoramaVillageActivity.this, getString(R.string.please_wait), getString(R.string.loading_metadata),
                true);
        new GetImageElementFromIdTask().execute(imageElement.getNext());

    }

}
