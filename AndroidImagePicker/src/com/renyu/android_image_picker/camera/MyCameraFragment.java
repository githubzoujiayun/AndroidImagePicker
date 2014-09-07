package com.renyu.android_image_picker.camera;

import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.Face;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.commonsware.cwac.camera.CameraFragment;
import com.commonsware.cwac.camera.CameraUtils;
import com.commonsware.cwac.camera.PictureTransaction;
import com.commonsware.cwac.camera.SimpleCameraHost;
import com.renyu.android_image_picker.R;
import com.renyu.android_image_picker.crop.FinalImageActivity;

public class MyCameraFragment extends CameraFragment {
	
	//是否使用前置摄像头
	boolean useFFC=false;
	//闪光灯是否开启
	boolean isFlash=false;
	//闪光灯类型
	String flashMode=null;
	
	ImageView change_camera=null;
	ImageView change_flash=null;
	ImageView take_pic=null;
	
	static MyCameraFragment getInstance(boolean useFFC) {
		MyCameraFragment fragment=new MyCameraFragment();
		Bundle bundle=new Bundle();
		bundle.putBoolean("useFFC", useFFC);
		fragment.setArguments(bundle);
		return fragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		SimpleCameraHost.Builder build=new SimpleCameraHost.Builder(new MyCameraHost(getActivity()));
		setHost(build.useFullBleedPreview(true).build());
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View cameraView=super.onCreateView(inflater, container, savedInstanceState);
		View view=LayoutInflater.from(getActivity()).inflate(R.layout.fragment_camera, container, false);
		((ViewGroup)(view.findViewById(R.id.camera))).addView(cameraView);
		change_camera=(ImageView) view.findViewById(R.id.change_camera);
		change_camera.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				((CameraActivity) getActivity()).changeFFC();
			}});
		change_flash=(ImageView) view.findViewById(R.id.change_flash);
		change_flash.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(isFlash) {
					change_flash.setImageResource(R.drawable.touxiang_shanguangdeng2);
					isFlash=false;
				}
				else {
					change_flash.setImageResource(R.drawable.touxiang_shanguangdeng1);
					isFlash=true;
				}
			}});
		take_pic=(ImageView) view.findViewById(R.id.take_pic);
		take_pic.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				takeSimplePicture();
			}});
		return view;
	}
	
	private void takeSimplePicture() {
		PictureTransaction tran=new PictureTransaction(getHost());
		if(isFlash) {
			tran.flashMode(flashMode);
		}
		takePicture(tran);
	}
	
	class MyCameraHost extends SimpleCameraHost implements Camera.FaceDetectionListener {
		
		//是否支持对焦
		boolean supportsFaces=false;

		public MyCameraHost(Context _ctxt) {
			super(_ctxt);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onFaceDetection(Face[] faces, Camera camera) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		protected boolean useFrontFacingCamera() {
			// TODO Auto-generated method stub
			if(getArguments()==null) {
				return false;
			}
			return getArguments().getBoolean("useFFC");
		}
		
		@Override
		public String saveImage(PictureTransaction xact, byte[] image) {
			// TODO Auto-generated method stub
			String path=super.saveImage(xact, image);
			Intent intent=new Intent(getActivity(), FinalImageActivity.class);
			Bundle bundle=new Bundle();
			bundle.putString("path", path);
			intent.putExtras(bundle);
			startActivity(intent);
			return path;
		}
		
		@Override
		public void autoFocusAvailable() {
			// TODO Auto-generated method stub
			super.autoFocusAvailable();
			if(supportsFaces) {
				startFaceDetection();
			}
		}
		
		@Override
		public void autoFocusUnavailable() {
			// TODO Auto-generated method stub
			super.autoFocusUnavailable();
		}
		
		@Override
		public void onCameraFail(FailureReason reason) {
			// TODO Auto-generated method stub
			super.onCameraFail(reason);
			Toast.makeText(getActivity(), "您得照相机去哪了？", 3000).show();
		}
		
		@Override
		public Parameters adjustPreviewParameters(Parameters parameters) {
			// TODO Auto-generated method stub
			flashMode=CameraUtils.findBestFlashModeMatch(parameters, Camera.Parameters.FLASH_MODE_RED_EYE, Camera.Parameters.FLASH_MODE_AUTO, Camera.Parameters.FLASH_MODE_ON);
			if(parameters.getMaxNumDetectedFaces()>0) {
				supportsFaces=true;
			}
			return super.adjustPreviewParameters(parameters);
		}
		
	};

}
