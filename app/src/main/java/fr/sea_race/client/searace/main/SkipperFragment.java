package fr.sea_race.client.searace.main;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlayOptions;

import java.util.List;

import fr.sea_race.client.searace.R;
import fr.sea_race.client.searace.component.Compass;
import fr.sea_race.client.searace.component.DisplaySails;
import fr.sea_race.client.searace.component.DisplayFeature;
import fr.sea_race.client.searace.component.OnCompassEventListener;
import fr.sea_race.client.searace.component.OnSailChangeListener;
import fr.sea_race.client.searace.model.Polar;
import fr.sea_race.client.searace.model.Sail;
import fr.sea_race.client.searace.model.Skipper;
import fr.sea_race.client.searace.model.wind.WindForecast;
import fr.sea_race.client.searace.service.SailService;
import fr.sea_race.client.searace.service.SkipperService;
import fr.sea_race.client.searace.service.WindService;
import fr.sea_race.client.searace.task.Poller;
import fr.sea_race.client.searace.task.TaskReport;
import fr.sea_race.client.searace.utils.CustomTileProvider;

/**
 * Created by cmeichel on 12/5/17.
 */

public class SkipperFragment extends Fragment {

    private Context currentContext;
    private String skipperId;
    private Skipper skipper;
    private List<Sail> sails;
    private GoogleMap mMap;
    private MapFragment mapFragment;
    private Poller skipperPoller;
    private Marker boatMarker;
    private WindForecast windForecast;
    private boolean isCompassRunning = false;
    private Polar polar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            skipperId = bundle.getString("skipperId", "");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main_skipper, container, false);

        currentContext = rootView.getContext();

        FragmentManager fm = getChildFragmentManager();
        mapFragment =  MapFragment.newInstance();
        fm.beginTransaction().replace(R.id.map_container, mapFragment).commit();

        mapFragment.getMapAsync(new OnMapReadyCallback () {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                setCustomTiles(mMap);
                addBoatMarker(mMap);
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        loadSkipper();
        loadWinds();


        manageCompass();
        manageSails();
    }

    @Override
    public void onPause() {
        super.onPause();

        skipperPoller.stop();
    }

    private void setCustomTiles(GoogleMap map) {
        map.setMapType(GoogleMap.MAP_TYPE_NONE);
        CustomTileProvider mTileProvider = new CustomTileProvider();
        map.addTileOverlay(
                new TileOverlayOptions()
                        .tileProvider(mTileProvider)
                        .zIndex(-1)
        );
    }

    private Bitmap createBoatIcon(double angle) {
        Bitmap icon = BitmapFactory.decodeResource(getResources(),
                R.drawable.boat_marker);

        float ratio = (float)80 / (float)icon.getHeight();
        Bitmap bmp = Bitmap.createBitmap((int)((float)icon.getHeight() * ratio), (int)((float)icon.getHeight() * ratio), Bitmap.Config.ARGB_8888);
        int shift = icon.getHeight() / 2 - icon.getWidth() / 2;
        Canvas canvas = new Canvas(bmp);

        Matrix matrix = new Matrix();
        matrix.postTranslate(-(float)icon.getWidth()/2, -(float)icon.getHeight()/2);
        matrix.postRotate((float)angle);
        matrix.postTranslate((float)icon.getWidth()/2 + shift, (float)icon.getHeight()/2);
        matrix.postScale(ratio, ratio);

        canvas.drawBitmap(icon, matrix, new Paint());
        return bmp;
    }

    private void estimateSpeed(float angle, LatLng position) {

    }

    private void addBoatMarker(GoogleMap map) {
        boatMarker = map.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromBitmap(createBoatIcon(0)))
                .anchor(0.5f, 0.5f)
                .position(new LatLng(0, 0))
        );
    }

    private void manageSails() {
        DisplaySails sailSelector = (DisplaySails)getView().findViewById(R.id.sail);
        sailSelector.setOnChangeListener(new OnSailChangeListener() {
            @Override
            public void onChange(Sail sail) {
                SailService.setSail(skipper, sail, new TaskReport<Skipper>() {
                    @Override
                    public void onSuccess(Skipper mSkipper) {
                        skipper = mSkipper;
                        loadPolars();
                        updateView();
                    }
                });
            }
        });
    }

    private void manageCompass() {
        Compass compass = (Compass)getView().findViewById(R.id.compass);
        compass.setOnCompassEventListener(new OnCompassEventListener() {
            @Override
            public void onAngleUpdate(float angle) {
                skipper.direction = angle;
                DisplayFeature direction = (DisplayFeature)getView().findViewById(R.id.direction);
                direction.setValue(String.format("%.2f", skipper.direction));
                SkipperService.setBearing(skipper, angle, new TaskReport<Skipper> () {
                    @Override
                    public void onSuccess(Skipper mSkipper) {
                        skipper = mSkipper;
                        updateView();
                    }

                    @Override
                    public void onComplete() {
                        isCompassRunning = false;
                    }
                });
                if (boatMarker != null) {
                    boatMarker.setIcon(BitmapDescriptorFactory.fromBitmap(createBoatIcon(angle)));
                }
            }

            @Override
            public void onStartAngle(float angle) {
                isCompassRunning = true;
            }

            @Override
            public void onProcessAngle(float angle) {
                if (boatMarker != null) {
                    boatMarker.setIcon(BitmapDescriptorFactory.fromBitmap(createBoatIcon(angle)));
                }
                if (skipper != null) {
                    estimateSpeed(angle, skipper.position);
                }
            }
        });
    }

    private void loadSkipper() {
        Log.i("SKIPPER", "starting poller");
        if (skipperPoller == null) {
            Log.i("SKIPPER", "Creating poller");
            skipperPoller = SkipperService.startPoller(skipperId, new TaskReport<Skipper>() {
                @Override
                public void onSuccess(Skipper mSkipper) {
                    skipper = mSkipper;
                    if (sails == null || sails.isEmpty()) {
                        loadSails();
                    }
                    loadPolars();
                    updateView();
                }

                @Override
                public void onFailure(String reason) {
                    Toast.makeText(currentContext, getString(R.string.skipper_fail), Toast.LENGTH_LONG).show();
                }
            });
        } else {
            if (!skipperPoller.running) {
                Log.i("SKIPPER", "restarting poller");
                skipperPoller.start(0);
            } else {
                Log.i("SKIPPER", "poller already running");
            }
        }
    }

    private void loadSails () {
        SailService.loadSails(skipper.boat, new TaskReport<List<Sail>>() {
            @Override
            public void onSuccess(List<Sail> sailList) {
                sails = sailList;
                updateView();
            }

            @Override
            public void onFailure(String reason) {
                Toast.makeText(currentContext, getString(R.string.sail_fail), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadWinds () {
        Log.i("WINDS", "Load winds");
        WindService.loadAllForecast(new TaskReport<WindForecast>() {
            @Override
            public void onSuccess(WindForecast forecast) {
                windForecast = forecast;
            }

            @Override
            public void onFailure(String reason) {
                Toast.makeText(currentContext, getString(R.string.wind_fail), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadPolars () {
        SailService.loadPolar(skipper.sail, new TaskReport<Polar>() {
            @Override
            public void onSuccess(Polar newPolar) {
                polar = newPolar;
            }

            @Override
            public void onFailure(String reason) {
                Toast.makeText(currentContext, getString(R.string.polar_fail), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateView() {
        if (skipper != null && !isCompassRunning) {
            DisplayFeature speed = (DisplayFeature)getView().findViewById(R.id.speed);
            if (skipper.isSpeedValid()) {
                speed.setValue(String.format("%.2f %s", skipper.speed, getString(R.string.speed_unit)));
            } else {
                speed.setValue("--");
            }

            DisplayFeature direction = (DisplayFeature)getView().findViewById(R.id.direction);
            direction.setValue(String.format("%.0f°", skipper.direction));

            DisplayFeature windDirection = (DisplayFeature)getView().findViewById(R.id.wind_direction);
            windDirection.setValue(String.format("%.0f°", skipper.windRelativeAngle));

            DisplayFeature windSpeed = (DisplayFeature)getView().findViewById(R.id.wind_speed);
            windSpeed.setValue(String.format("%.0f %s", skipper.windSpeed, getString(R.string.speed_unit)));

            DisplayFeature finished = (DisplayFeature)getView().findViewById(R.id.finished);
            DisplayFeature rank = (DisplayFeature)getView().findViewById(R.id.rank);
            if (skipper.hasFinished()) {
                finished.setValue(String.format(getString(R.string.skipper_finished_value), skipper.getFinished(currentContext)));
                rank.setValue(String.format("%d", skipper.rank));
                finished.setVisibility(View.VISIBLE);
                rank.setVisibility(View.VISIBLE);
            } else {
                finished.setVisibility(View.INVISIBLE);
                rank.setVisibility(View.INVISIBLE);
            }

            Compass compass = (Compass)getView().findViewById(R.id.compass);
            compass.setAngle((float)skipper.direction);

            if (boatMarker != null) {
                boatMarker.setPosition(skipper.position);
                boatMarker.setIcon(BitmapDescriptorFactory.fromBitmap(createBoatIcon(skipper.direction)));
            }

        }

        if (sails != null) {
            DisplaySails sailSelector = (DisplaySails)getView().findViewById(R.id.sail);
            if (sailSelector.getSails().size() == 0) {
                sailSelector.setSails(sails);
            }
            if (skipper != null) {
                sailSelector.setValue(skipper.sail);
            }
        }
    }

}
