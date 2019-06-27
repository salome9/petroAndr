package com.example.tp3;

import android.support.v7.app.ActionBarActivity;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;

import android.app.Activity;
import android.content.ContentValues;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Images.Media;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;

public class Photo extends Activity implements SurfaceHolder.Callback{
	private Camera camera;
	private SurfaceView surfaceCamera;
	private Boolean isPreview;
	private FileOutputStream stream;
	private PictureCallback pictureCallback;
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,int height) {
		// Si le mode preview est lancé alors nous le stoppons
	    if (isPreview) {
	        //camera.stopPreview();
	    }
	    // Nous récupérons les paramètres de la caméra
	    Camera.Parameters parameters = camera.getParameters();

	    // Nous changeons la taille
	    parameters.setPreviewSize(width, height);

	    // Nous appliquons nos nouveaux paramètres
	    camera.setParameters(parameters);

	    try {
	        // Nous attachons notre prévisualisation de la caméra au holder de la
	        // surface
	        camera.setPreviewDisplay(surfaceCamera.getHolder());
	    } catch (IOException e) {
	    }

	    // Nous lançons la preview
	    camera.startPreview();

	    isPreview = true;
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// Nous prenons le contrôle de la camera
	    if (camera == null)
	        camera = Camera.open();
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// Nous arrêtons la camera et nous rendons la main
	    if (camera != null) {
	        camera.stopPreview();
	        isPreview = false;
	        camera.release();
	    }
		
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);

	    // Nous mettons l'application en plein écran et sans barre de titre
	    getWindow().setFormat(PixelFormat.TRANSLUCENT);
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
	            WindowManager.LayoutParams.FLAG_FULLSCREEN);

	    isPreview = false;

	    // Nous appliquons notre layout
	    setContentView(R.layout.activity_photo);

	    // Nous récupérons notre surface pour le preview
	    surfaceCamera = (SurfaceView) findViewById(R.id.surfaceViewCamera);

	    // Méthode d'initialisation de la caméra
	    InitializeCamera();
	 // Quand nous cliquons sur notre surface
	    surfaceCamera.setOnClickListener(new OnClickListener() {

	        public void onClick(View v) {
	            // Nous prenons une photo
	            if (camera != null) {
	                SavePicture();
	            }

	        }

	    });
	}
	public void InitializeCamera() {
		// Nous attachons nos retours du holder à notre activité
		surfaceCamera.getHolder().addCallback(this);
		// Nous spécifiions le type du holder en mode SURFACE_TYPE_PUSH_BUFFERS
		surfaceCamera.getHolder().setType(
		SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
	// Retour sur l'application
	@Override
	public void onResume() {
	    super.onResume();
	    camera = Camera.open();
	}

	// Mise en pause de l'application
	@Override
	public void onPause() {
	    super.onPause();

	    if (camera != null) {
	        camera.release();
	        camera = null;
	    }
	}
	private void SavePicture() {
	    try {
	        SimpleDateFormat timeStampFormat = new SimpleDateFormat(
	                "yyyy-MM-dd-HH.mm.ss");
	        String fileName = "photo_" + timeStampFormat.format(new Date(0))
	                + ".jpg";

	        // Metadata pour la photo
	        ContentValues values = new ContentValues();
	        values.put(Media.TITLE, fileName);
	        values.put(Media.DISPLAY_NAME, fileName);
	        values.put(Media.DESCRIPTION, "Image prise par FormationCamera");
	        values.put(Media.DATE_TAKEN, new Date(0).getTime());
	        values.put(Media.MIME_TYPE, "image/jpeg");

	        // Support de stockage
	        Uri taken = getContentResolver().insert(Media.EXTERNAL_CONTENT_URI,
	                values);

	        //Ouverture du flux pour la sauvegarde
	        stream = (FileOutputStream) getContentResolver().openOutputStream(
	                taken);

	        camera.takePicture(null, pictureCallback, pictureCallback);
	    } catch (Exception e) {
	        // TODO: handle exception
	    }

	}
	
}
