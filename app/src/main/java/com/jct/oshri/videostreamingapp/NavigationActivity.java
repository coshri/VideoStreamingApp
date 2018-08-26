package com.jct.oshri.videostreamingapp;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.smoothstreaming.DefaultSsChunkSource;
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
//import com.google.android.exoplayer2.upstream.DataSource;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private final String STATE_RESUME_POSITION = "resumePosition";
    private final String STATE_CONTENT_URL = "resumeContentUrl";


    PlayerView playerView;
    private SimpleExoPlayer player;

    private long contentPosition;
    private String contentUrl;


    private DataSource.Factory manifestDataSourceFactory;
    private DataSource.Factory mediaDataSourceFactory;


    private String user;
    private String urlContetn;

    TextView userNameTextView;
    ImageButton startImageButton;


//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//       // super.onConfigurationChanged(newConfig);
//
//        // Checks the orientation of the screen
//        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
//        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
//            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
//        }else
//        {
//            super.onConfigurationChanged(newConfig);
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_navigation);
        // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //   setSupportActionBar(toolbar);


//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.addDrawerListener(toggle);
//        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        playerView = findViewById(R.id.player_view);
//        startImageButton = findViewById(R.id.startImageButton);
//        startImageButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                initExoPlayer(getBaseContext());
//            }
//        });


        initExoPlayer(getBaseContext());


//            SignInDialogFragment signInDialogFragment = new SignInDialogFragment();
//       signInDialogFragment.show(getFragmentManager(),"signIn Dialog");

        Intent intent = getIntent();
        if (intent != null) {
            user = intent.getStringExtra(SignInActivity.USER_NAME);
            if (user != null) {


                urlContetn = intent.getStringExtra(SignInActivity.PLAY_URL);


                userNameTextView = findViewById(R.id.userNameTextView);

                switch (user.toUpperCase()) {
                    case "YELLOW":
                        userNameTextView.setBackgroundColor(Color.YELLOW);
                        break;
                    case "BLUE":
                        userNameTextView.setBackgroundColor(Color.BLUE);
                        break;
                    case "PINK":
                        userNameTextView.setBackgroundColor(Color.RED);
                        break;

                    default:
                        userNameTextView.setBackgroundColor(Color.TRANSPARENT);
                }


                if (userNameTextView != null)
                    userNameTextView.setText(user);


                String contentUrl = urlContetn; //getString(R.string.content_url_hls);
                MediaSource contentMediaSource = buildMediaSource(Uri.parse(contentUrl), getBaseContext());


                // Prepare the player with the source.
                player.seekTo(contentPosition);
                player.prepare(contentMediaSource);
                player.setPlayWhenReady(true);

                // new  sessionAsync().execute("http://192.168.1.100:8081/sessions/open?id=" + user);


                player.addListener(new Player.DefaultEventListener() {
                    @Override
                    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                        if (playWhenReady && playbackState == Player.STATE_READY) {
                            // media actually playing
                            new sessionAsync().execute("http://192.168.1.100:8081/sessions/open?id=" + user);
                        } else if (playWhenReady) {
                            // might be idle (plays after prepare()),
                            // buffering (plays when data available)
                            // or ended (plays when seek away from end)
                        } else {
                            // player paused in any state
                        }
                    }
                });


            }

        }


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
        resetPlayer();
    }


    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        outState.putLong(STATE_RESUME_POSITION, contentPosition);
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.clear_mp4) {
            String contentUrl = getString(R.string.content_url_mp4);
            MediaSource contentMediaSource = buildMediaSource(Uri.parse(contentUrl), getBaseContext());


            // Prepare the player with the source.
            player.seekTo(contentPosition);
            player.prepare(contentMediaSource);
            player.setPlayWhenReady(true);

        } else if (id == R.id.hls_nba) {
            String contentUrl = getString(R.string.content_url_hls);
            MediaSource contentMediaSource = buildMediaSource(Uri.parse(contentUrl), getBaseContext());


            // Prepare the player with the source.
            player.seekTo(contentPosition);
            player.prepare(contentMediaSource);
            player.setPlayWhenReady(true);

        } else if (id == R.id.uploadVideo) {
            uploadFile();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void initExoPlayer(Context context) {
        // Create a default track selector.
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

        // Create a player instance.
        player = ExoPlayerFactory.newSimpleInstance(context, trackSelector);

        // Bind the player to the view.
        playerView.setPlayer(player);


        // This is the MediaSource representing the content media (i.e. not the ad).
        //   String contentUrl = context.getString(R.string.content_url_hls);
//        MediaSource contentMediaSource = buildMediaSource(Uri.parse(contentUrl), context);
//
//
//
//        // Prepare the player with the source.
//        player.seekTo(contentPosition);
//        player.prepare(contentMediaSource);
//        player.setPlayWhenReady(true);


        playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
        player.setVideoScalingMode(C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
    }


    private MediaSource buildMediaSource(Uri uri, Context context) {
        @C.ContentType int type = Util.inferContentType(uri);

        manifestDataSourceFactory =
                new DefaultDataSourceFactory(
                        context, Util.getUserAgent(context, context.getString(R.string.app_name)));
        mediaDataSourceFactory =
                new DefaultDataSourceFactory(
                        context,
                        Util.getUserAgent(context, context.getString(R.string.app_name)),
                        new DefaultBandwidthMeter());

        switch (type) {
            case C.TYPE_DASH:
                return new DashMediaSource.Factory(
                        new DefaultDashChunkSource.Factory(mediaDataSourceFactory),
                        manifestDataSourceFactory)
                        .createMediaSource(uri);
            case C.TYPE_SS:
                return new SsMediaSource.Factory(
                        new DefaultSsChunkSource.Factory(mediaDataSourceFactory), manifestDataSourceFactory)
                        .createMediaSource(uri);
            case C.TYPE_HLS:
                return new HlsMediaSource.Factory(mediaDataSourceFactory).createMediaSource(uri);
            case C.TYPE_OTHER:
                return new ExtractorMediaSource.Factory(mediaDataSourceFactory).createMediaSource(uri);
            default:
                throw new IllegalStateException("Unsupported type: " + type);
        }
    }

    public void resetPlayer() {
        if (player != null) {
            contentPosition = player.getContentPosition();
            // contentUrl = player.pre
            player.release();
            player = null;

            new sessionAsync().execute("http://192.168.1.100:8081/sessions/close?id=" + user);
        }
    }

    public void releasePlayer() {
        if (player != null) {
            player.release();
            player = null;

            new sessionAsync().execute("http://192.168.1.100:8081/sessions/close?id=" + user);

        }
    }


    private static final int REQUEST_TAKE_GALLERY_VIDEO = 33;

    void uploadFile() {

        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Video"), REQUEST_TAKE_GALLERY_VIDEO);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_TAKE_GALLERY_VIDEO) {
                Uri selectedImageUri = data.getData();

                // OI FILE Manager
                String filemanagerstring = selectedImageUri.getPath();

                // MEDIA GALLERY
                String selectedImagePath = FilePath.getPath(this, selectedImageUri);//.getPath(); //getPath2(this,selectedImageUri);
                if (selectedImagePath != null) {

                    HttpTools.uploadFileToServer("http:192.168.1.100:8083/content?name=" + user, selectedImagePath);
                }
            }
        }
    }

    // UPDATED!
    public String getPath(Uri uri) {

        String[] proj = {MediaStore.MediaColumns.DATA};
        @SuppressWarnings("deprecation")
        Cursor cursor = managedQuery(uri, proj, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();

        String mString = cursor.getString(column_index);

        return mString;


//        String[] projection = { MediaStore.Video.Media.DATA };
//        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
//        if (cursor != null) {
//            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
//            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
//            int column_index = cursor
//                    .getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
//            cursor.moveToFirst();
//            return cursor.getString(column_index);
//        } else
//            return null;
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String getPath2(final Context context, final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            System.out.println("getPath() uri: " + uri.toString());
            System.out.println("getPath() uri authority: " + uri.getAuthority());
            System.out.println("getPath() uri path: " + uri.getPath());

            // ExternalStorageProvider
            //   if ("com.android.externalstorage.documents".equals(uri.getAuthority())) {
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                System.out.println("getPath() docId: " + docId + ", split: " + split.length + ", type: " + type);

                // This is for checking Main Memory
                if ("primary".equalsIgnoreCase(type)) {
                    if (split.length > 1) {
                        return Environment.getExternalStorageDirectory() + "/" + split[1] + "/";
                    } else {
                        return Environment.getExternalStorageDirectory() + "/";
                    }
                    // This is for checking SD Card
                } else {
                    return "storage" + "/" + docId.replace(":", "/");
                }

            }
        }
        return null;
    }

    public String getRealPathFromURI(Uri contentUri) {
        String path = null;
        String[] proj = {MediaStore.MediaColumns.DATA};

        if ("content".equalsIgnoreCase(contentUri.getScheme())) {
            Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
            if (cursor.moveToFirst()) {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                path = cursor.getString(column_index);
            }
            cursor.close();
            return path;
        } else if ("file".equalsIgnoreCase(contentUri.getScheme())) {
            return contentUri.getPath();
        }
        return null;
    }


    class sessionAsync extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                return HttpTools.GET(strings[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}
