package com.example.dell.spotify_clone_main.youtube_files;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.spotify_clone_main.R;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class playvideoActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener{
    int REQUEST_VIDEO = 123;
    YouTubePlayerView youTubePlayerView;
    int lengthms=270000;

    private MyPlayerStateChangeListener playerStateChangeListener;
    private MyPlaybackEventListener playbackEventListener;

    private YouTubePlayer player;
    Handler handler;
    Runnable my;
    boolean killMe = false;
    boolean seekusedbyuser=false;
    SeekBar seekbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playvideo);
        youTubePlayerView = findViewById(R.id.myYoutube);
        youTubePlayerView.initialize(youtube_search_activity.API_KEY, this);

        handler = new Handler();
        my=new Runnable() {
            @Override
            public void run() {
                //Do something after 20 seconds

                if(killMe)
                    return;

                handler.postDelayed(this, 1000);

                lengthms = player.getDurationMillis();
                float current=player.getCurrentTimeMillis();
                float wowInt= ((current/lengthms)*100);
                if (seekusedbyuser==false)
                { seekbar.setProgress((int) wowInt);}
//                Toast.makeText(playvideoActivity.this, "God is awesome", Toast.LENGTH_SHORT).show();
            }
        };
        handler.postDelayed(my, 2000);

        playerStateChangeListener = new MyPlayerStateChangeListener();
        playbackEventListener = new MyPlaybackEventListener();



        seekbar =(SeekBar) findViewById(R.id.seekBar);
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {



            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                seekusedbyuser=true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                seekusedbyuser=false;
                int progress=seekBar.getProgress();
                lengthms = player.getDurationMillis();
                float current=player.getCurrentTimeMillis();
                int to= (int) (lengthms*progress/100);

                player.seekToMillis(to);

            }
        });

        final Button Play_Pause =(Button) findViewById(R.id.button2);
        Play_Pause.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                if (player.isPlaying())
                {
                    player.pause();
                    Play_Pause.setBackground(getDrawable(R.drawable.ic_play_circle_filled_black_24dp));
                }
                else {
                    player.play();
                    Play_Pause.setBackground(getDrawable(R.drawable.ic_pause_circle_filled_black_24dp));
                }


            }
        });




    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {

        this.player = youTubePlayer;
        youTubePlayer.setPlayerStateChangeListener(playerStateChangeListener);
        youTubePlayer.setPlaybackEventListener(playbackEventListener);

        youTubePlayer.cueVideo(youtube_search_activity.idvideo);
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        if (youTubeInitializationResult.isUserRecoverableError()) {
            youTubeInitializationResult.getErrorDialog(playvideoActivity.this, REQUEST_VIDEO);
        } else {
            Toast.makeText(this, "ERROR!", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_VIDEO) {
            youTubePlayerView.initialize(youtube_search_activity.API_KEY, playvideoActivity.this);

        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private final class MyPlaybackEventListener implements YouTubePlayer.PlaybackEventListener {

        @Override
        public void onPlaying() {
            // Called when playback starts, either due to user action or call to play().
//            showMessage("Playing");
        }

        @Override
        public void onPaused() {
            // Called when playback is paused, either due to user action or call to pause().
//            showMessage("Paused");
        }

        @Override
        public void onStopped() {
            // Called when playback stops for a reason other than being paused.
//            showMessage("Stopped");
        }

        @Override
        public void onBuffering(boolean b) {
            // Called when buffering starts or ends.
        }

        @Override
        public void onSeekTo(int i) {
            // Called when a jump in playback position occurs, either
            // due to user scrubbing or call to seekRelativeMillis() or seekToMillis()
        }
    }

    private final class MyPlayerStateChangeListener implements YouTubePlayer.PlayerStateChangeListener {

        @Override
        public void onLoading() {
            // Called when the player is loading a video
            // At this point, it's not ready to accept commands affecting playback such as play() or pause()
        }

        @Override
        public void onLoaded(String s) {
            // Called when a video is done loading.
            player.play();
            // Playback methods such as play(), pause() or seekToMillis(int) may be called after this callback.
        }

        @Override
        public void onAdStarted() {
            // Called when playback of an advertisement starts.
        }

        @Override
        public void onVideoStarted() {
            // Called when playback of the video starts.
        }

        @Override
        public void onVideoEnded() {
            // Called when the video reaches its end.
        }

        @Override
        public void onError(YouTubePlayer.ErrorReason errorReason) {
            // Called when an error occurs.
        }

    }
    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        if (handler!=null)
            handler.removeCallbacks(my);
        killMe=true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(my);
        killMe=true;

    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacksAndMessages(my);
        killMe=true;
    }
}