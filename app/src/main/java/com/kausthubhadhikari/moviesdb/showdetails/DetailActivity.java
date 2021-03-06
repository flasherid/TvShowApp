package com.kausthubhadhikari.moviesdb.showdetails;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.kausthubhadhikari.moviesdb.AppController;
import com.kausthubhadhikari.moviesdb.R;
import com.kausthubhadhikari.moviesdb.di.injector.Injector;
import com.kausthubhadhikari.moviesdb.model.manager.HorizontalItemDecorator;
import com.kausthubhadhikari.moviesdb.model.pojo.detail.TVShowDetails;
import com.kausthubhadhikari.moviesdb.utils.base.BaseActivity;
import com.kausthubhadhikari.moviesdb.utils.misc.AppConstants;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import icepick.Icepick;
import icepick.State;

public class DetailActivity extends BaseActivity implements DetailView, AppBarLayout.OnOffsetChangedListener {

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.backdrop)
    ImageView backdrop;

    @Bind(R.id.imageView)
    ImageView profilePicture;

    @Bind(R.id.showName)
    AppCompatTextView showName;

    @Bind(R.id.genre)
    AppCompatTextView genre;

    @Bind(R.id.desc_data)
    AppCompatTextView description;

    @Bind(R.id.rate)
    Button no_rate;

    @Bind(R.id.seasons)
    Button no_seasons;

    @Bind(R.id.episodes)
    Button no_episodes;

    @Bind(R.id.season_recyclerView)
    RecyclerView seasonsRecyclerView;

    @Bind(R.id.runtime_Val)
    AppCompatTextView runtimeVal;

    @Bind(R.id.network_val)
    AppCompatTextView networkVal;

    @Bind(R.id.lang_val)
    AppCompatTextView langVal;

    @Bind(R.id.homepage_val)
    AppCompatTextView homepageVal;

    @Bind(R.id.appBar)
    AppBarLayout appBarLayout;

    @Bind(R.id.collapsingToolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @Inject
    DetailPresenter presenter;

    @Inject
    MaterialDialog progressDialog;

    @Inject
    LinearLayoutManager seasonsLinearLayout;

    @Inject
    DefaultItemAnimator defaultItemAnimator;

    @Inject
    Picasso picasso;

    @Inject
    HorizontalItemDecorator horizontalItemDecorator;

    @State
    public int showId;

    @State
    public TVShowDetails data;

    private boolean isShow = false;

    private int scrollRange = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        if (getIntent().hasExtra(AppConstants.INTENT_KEY_SHOW_ID)) {
            showId = getIntent().getExtras().getInt(AppConstants.INTENT_KEY_SHOW_ID);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public void inject(Injector injector) {
        injector.inject(this);
    }

    @NonNull
    @Override
    public DetailPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void setupView() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        seasonsRecyclerView.setItemAnimator(defaultItemAnimator);
        seasonsRecyclerView.setLayoutManager(seasonsLinearLayout);
        appBarLayout.addOnOffsetChangedListener(this);

    }

    @Override
    public void showProgress() {
        progressDialog.show();
    }

    @Override
    public void hideProgress() {
        progressDialog.dismiss();
    }

    @Override
    public void fetchError(Throwable throwable) {

    }

    @Override
    public void deliverData(TVShowDetails data) {
        this.data = data;
        picasso.load(((AppController) getApplicationContext()).getBaseURL() + AppConstants.BACKDROP_BASE_URL_HIGH + data.backdropPath).into(backdrop);
        picasso.load(((AppController) getApplicationContext()).getBaseURL() + AppConstants.POSTER_BASE_URL_HIGH + data.posterPath).into(profilePicture);
        showName.setText(data.name);
        genre.setText(data.genres.get(0).name);
        description.setText(data.overview);
        no_rate.setText("" + data.voteAverage);
        no_episodes.setText("" + data.numberOfEpisodes);
        no_seasons.setText("" + data.numberOfSeasons);
        runtimeVal.setText(TextUtils.join(",", data.episodeRunTime));
        networkVal.setText(data.networks.get(0).name);
        homepageVal.setText(data.homepage);
        seasonsRecyclerView.addItemDecoration(horizontalItemDecorator);
        seasonsRecyclerView.setAdapter(new SeasonsAdapter(this, data.seasons, picasso));
    }

    @Override
    public int retrieveShowId() {
        return showId;
    }

    @Override
    public void showNetworkError() {

    }

    @Override
    public void showHttpError() {

    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (scrollRange == -1) {
            scrollRange = appBarLayout.getTotalScrollRange();
        }
        if (scrollRange + verticalOffset == 0) {
            collapsingToolbarLayout.setTitle("Telegator");
            isShow = true;
        } else if (isShow) {
            collapsingToolbarLayout.setTitle("");
            isShow = false;
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
        presenter.onRestoreState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
        presenter.onSaveState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
