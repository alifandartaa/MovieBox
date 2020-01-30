package com.example.secondappcataloguemovie.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.secondappcataloguemovie.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView navView;
    public static final String EXTRA_SEARCH_MOVIE = "extras_movie";
    public static final String EXTRA_SEARCH_TV_SHOW = "extras_tv_show";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_movie, R.id.navigation_tv_show, R.id.navigation_favorite)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        search(menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void search(Menu menu) {
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        if (searchManager != null) {
            SearchView searchView = (SearchView) (menu.findItem(R.id.search)).getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setQueryHint(getResources().getString(R.string.search_hint));
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    int bottomNavigationPosition = navView.getSelectedItemId();
                    if (bottomNavigationPosition == R.id.navigation_movie) {
                        Toast.makeText(MainActivity.this, R.string.movie, Toast.LENGTH_SHORT).show();
                        Intent intent1 = new Intent(MainActivity.this, SearchMovieActivity.class);
                        intent1.putExtra(EXTRA_SEARCH_MOVIE, query);
                        startActivity(intent1);
                    } else if (bottomNavigationPosition == R.id.navigation_tv_show) {
                        Toast.makeText(MainActivity.this, R.string.tv_show, Toast.LENGTH_SHORT).show();
                        Intent intent2 = new Intent(MainActivity.this, SearchTvShowActivity.class);
                        intent2.putExtra(EXTRA_SEARCH_TV_SHOW, query);
                        startActivity(intent2);
                    }
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_change_settings) {
            Intent mIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            startActivity(mIntent);
        } else if (item.getItemId() == R.id.reminder_setting) {
            Intent intent = new Intent(this, SettingActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
