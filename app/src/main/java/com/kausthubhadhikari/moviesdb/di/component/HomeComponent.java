package com.kausthubhadhikari.moviesdb.di.component;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;

import com.kausthubhadhikari.moviesdb.di.module.HomeModule;
import com.kausthubhadhikari.moviesdb.di.scope.ActivityScope;
import com.kausthubhadhikari.moviesdb.home.HomeActivity;
import com.kausthubhadhikari.moviesdb.home.HomePresenter;

import dagger.Component;

/**
 * Created by kausthubhadhikari on 12/12/16.
 */
@ActivityScope
@Component(modules = {HomeModule.class}, dependencies = {AppComponent.class})
public interface HomeComponent {

    void inject(HomeActivity activity);

    Context providesContext();

    LinearLayoutManager providesLinearLayoutManager();

    DefaultItemAnimator providesItemAnimator();

    HomePresenter providesHomePresenter();
}
