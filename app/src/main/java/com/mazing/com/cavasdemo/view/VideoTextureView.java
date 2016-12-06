package com.mazing.com.cavasdemo.view;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.TextureView;
import android.widget.ImageView;

import com.mazing.com.cavasdemo.R;

import java.io.File;

/**
 * Created by toma on 16/10/16.
 */

public class VideoTextureView extends TextureView implements TextureView.SurfaceTextureListener {


    private Surface videosurface;
    private MediaPlayer mediaPlayer;
    private boolean isPlaying;
    private String videoPath = "";
    private ImageView ivTip;
    private boolean isTextureAvailable;
    public VideoTextureView(Context context) {
        super(context);
        init(context);
    }

    public VideoTextureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public VideoTextureView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }


    public void init(Context context) {
        setSurfaceTextureListener(this);
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
        videosurface = new Surface(surfaceTexture);
        isTextureAvailable = true;
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        videosurface = null;
        onVideoTextureViewDestroy();
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    public void startMediaPlayer() {
        if (isPlaying || !isTextureAvailable)
            return;

//        if(TextUtils.isEmpty(videoPath))
//        return;

        try {
            if (videoPath.equals(""))
                mediaPlayer = MediaPlayer.create(getContext(), R.raw.sf1);
            else {
                File mediafile = new File(videoPath);
                if (!mediafile.exists()) {

                }
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setDataSource(mediafile.getAbsolutePath());

            }
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setVolume(0, 0);
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer.start();
                    showIvTip(false);
                    isPlaying = true;
                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    stopMediaPlayer();
                }
            });

//            mediaPlayer.prepare();


            mediaPlayer.setSurface(videosurface);

        } catch (Exception ex) {

        }
    }

    public void stopMediaPlayer() {
        if (mediaPlayer != null && isPlaying) {
            mediaPlayer.stop();
            mediaPlayer.reset();
        }
        showIvTip(true);
        isPlaying = false;
    }

    public void showIvTip(boolean show) {
        if (ivTip != null) {
            ivTip.setVisibility(show ? VISIBLE : GONE);
        }
    }

    public void setIvTip(ImageView ivTip) {
        this.ivTip = ivTip;
    }

    public boolean getPlayStatus() {
        return isPlaying;
    }

    public void setVideoPath(String path) {
        videoPath = path;
    }

    public void onVideoTextureViewDestroy() {
        isPlaying = false;
        if (mediaPlayer != null) {
            stopMediaPlayer();
            mediaPlayer.release();
        }
    }
}
