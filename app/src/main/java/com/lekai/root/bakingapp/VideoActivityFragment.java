package com.lekai.root.bakingapp;

import android.net.Uri;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.lekai.root.bakingapp.ExtraUtil.Constants;
import com.lekai.root.bakingapp.ExtraUtil.WorkUtils;
import com.lekai.root.bakingapp.Recipes.Step;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * A placeholder fragment containing a simple view.
 */
public class VideoActivityFragment extends Fragment {

    @InjectView(R.id.txt_instruction)
    TextView txt_instruction;
    @InjectView(R.id.stepThumbnail)
    ImageView stepThumbnail;
    @InjectView(R.id.mExoPlayerView)
    SimpleExoPlayerView mExoPlayerView;

    private String videoUrl;
    private SimpleExoPlayer player;
    private long currentPosition=0;
    private Step step;

    public VideoActivityFragment() {
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         step=getArguments().getParcelable(Constants.STEP_EXTRA);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_video, container, false);

        ButterKnife.inject(this,view);

        if (savedInstanceState!=null){
            currentPosition=savedInstanceState.getLong(Constants.PLAYER_POSITION);
        }

        if (step!=null) {
            txt_instruction.setText(step.getDescription());
        }
        if (!step.getThumbnailURL().equals("")){
            String url=step.getThumbnailURL();
            WorkUtils.loadImageFromResourceInto(getActivity(),stepThumbnail,url);
            stepThumbnail.setVisibility(View.VISIBLE);
        }

        //Get Video Url
        videoUrl=step.getVideoURL();

        if (!videoUrl.isEmpty()) {
            setUpExoPlayer();
        }
        else if(videoUrl.length() < 7) {
            mExoPlayerView.setBackgroundResource(R.mipmap.no_video_image);
        }
        return view;
    }

    private void setUpExoPlayer() {
        // 1. Create a default TrackSelector
        Handler mainHandler = new Handler();
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new  AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector =
                new DefaultTrackSelector(videoTrackSelectionFactory);

        // 2. Create the player
        player = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector);

        // Bind the player to the view.
        mExoPlayerView.setPlayer(player);


        // Measures bandwidth during playback. Can be null if not required.
        DefaultBandwidthMeter bandwidthMeter1 = new DefaultBandwidthMeter();
        // Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getActivity(),
                Util.getUserAgent(getActivity(), "BookingApp"), bandwidthMeter1);
        // Produces Extractor instances for parsing the media data.
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        // This is the MediaSource representing the media to be played.
        MediaSource videoSource = new ExtractorMediaSource(Uri.parse(videoUrl),
                dataSourceFactory, extractorsFactory, mainHandler, null);
        // Prepare the player with the source.
        player.prepare(videoSource);

        if (currentPosition!=0) player.seekTo(currentPosition);

        player.setPlayWhenReady(true);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (player!=null) {
            outState.putLong(Constants.PLAYER_POSITION, player.getCurrentPosition());
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        if (player!=null) {
            player.stop();
            player.release();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.reset(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(player != null){
            currentPosition = player.getCurrentPosition();
        }
    }
}
