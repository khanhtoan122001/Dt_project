package com.example.dt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

public class Eng_Vie extends AppCompatActivity {

    SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Context context = this;
        setContentView(R.layout.activity_eng_vie);

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);

            Intent intentResult = new Intent(Eng_Vie.this, SearchResult.class);
            intentResult.putExtra("eng", query);

            SearchRecentSuggestions suggestions = new SearchRecentSuggestions(Eng_Vie.this,
                    MySuggestionProvider.AUTHORITY, MySuggestionProvider.MODE);

            suggestions.saveRecentQuery(query, null);

            startActivity(intentResult);
        }

        /*SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) findViewById(R.id.search_view);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(Eng_Vie.this, SearchResult.class);
                intent.putExtra("eng", query);

                SearchRecentSuggestions suggestions = new SearchRecentSuggestions(context,
                        MySuggestionProvider.AUTHORITY, MySuggestionProvider.MODE);
                suggestions.saveRecentQuery(query, null);

                startActivity(intent);
                return false;
            }


            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setIconified(false);
            }
        });*/
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.search_menu);
        SearchView searchView = (SearchView)
                MenuItemCompat.getActionView(searchItem);



        /*searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(Eng_Vie.this, SearchResult.class);
                intent.putExtra("eng", query);

                SearchRecentSuggestions suggestions = new SearchRecentSuggestions(Eng_Vie.this,
                        MySuggestionProvider.AUTHORITY, MySuggestionProvider.MODE);

                suggestions.saveRecentQuery(query, null);

                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });*/



        SearchManager searchManager = (SearchManager)
                getSystemService(Context.SEARCH_SERVICE);
        ComponentName componentName =
                new ComponentName(Eng_Vie.this, Eng_Vie.class);
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(componentName)
        );

        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setIconified(false);
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

}