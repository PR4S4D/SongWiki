package com.slp.songwiki.ui.fragment;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.danielstone.materialaboutlibrary.ConvenienceBuilder;
import com.danielstone.materialaboutlibrary.MaterialAboutActivity;
import com.danielstone.materialaboutlibrary.items.MaterialAboutActionItem;
import com.danielstone.materialaboutlibrary.items.MaterialAboutTitleItem;
import com.danielstone.materialaboutlibrary.model.MaterialAboutCard;
import com.danielstone.materialaboutlibrary.model.MaterialAboutList;
import com.slp.songwiki.BuildConfig;
import com.slp.songwiki.R;

public class AboutActivity extends MaterialAboutActivity {


    public static final String SONGWIKI_GITHUB_LINK = "https://github.com/sLakshmiprasad/SongWiki";
    public static final String LINKED_IN_LINK = "https://www.linkedin.com/in/slakshmiprasad/";
    private static final String GITHUB_LINK = "https://github.com/sLakshmiprasad/";

    @NonNull
    @Override
    protected MaterialAboutList getMaterialAboutList(@NonNull Context context) {
        return getAboutList(context);
    }

    @Nullable
    @Override
    protected CharSequence getActivityTitle() {
        return getString(R.string.about);
    }

    private MaterialAboutList getAboutList(@NonNull Context context) {


        return new MaterialAboutList(getAppCardBuilder(context), getAuthorCardBuilder(context));
    }

    private MaterialAboutCard getAppCardBuilder(Context context) {
        MaterialAboutCard.Builder appCardBuilder = new MaterialAboutCard.Builder();
        appCardBuilder.addItem(new MaterialAboutTitleItem.Builder().text(getString(R.string.song_wiki)).icon(R.drawable.app_icon).build());
        appCardBuilder.addItem(new MaterialAboutActionItem.Builder().text(getString(R.string.version)).subText(BuildConfig.VERSION_NAME).icon(R.drawable.ic_info).build());
        appCardBuilder.addItem(ConvenienceBuilder.createRateActionItem(context,getDrawable(R.drawable.ic_rating),getString(R.string.rate_this_app),null));
        appCardBuilder.addItem(new MaterialAboutActionItem.Builder().text(getString(R.string.fork_on_github))
                .icon(R.drawable.ic_github_logo)
                .setOnClickAction(ConvenienceBuilder.createWebsiteOnClickAction(context, Uri.parse(SONGWIKI_GITHUB_LINK))).build());
        appCardBuilder.addItem(ConvenienceBuilder.createEmailItem(context,getDrawable(R.drawable.ic_email),getString(R.string.give_feedback),true,"slakshmiprasad93@gmail.com","Feedback on SongWiki"));
        return appCardBuilder.build();
    }

    private MaterialAboutCard getAuthorCardBuilder(Context context) {
        MaterialAboutCard.Builder authorCardBuilder = new MaterialAboutCard.Builder();
        authorCardBuilder.title(getString(R.string.author));
        authorCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text(getString(R.string.lp)).subText(getString(R.string.author_location)).icon(R.drawable.ic_author).build());
        authorCardBuilder.addItem(new MaterialAboutActionItem.Builder().text(getString(R.string.github))
                .icon(R.drawable.ic_github_logo)
                .setOnClickAction(ConvenienceBuilder.createWebsiteOnClickAction(context, Uri.parse(GITHUB_LINK))).build());
        authorCardBuilder.addItem(new MaterialAboutActionItem.Builder().text(getString(R.string.linkedIn))
                .icon(R.drawable.ic_linkedin)
                .setOnClickAction(ConvenienceBuilder.createWebsiteOnClickAction(context, Uri.parse(LINKED_IN_LINK))).build());
        return authorCardBuilder.build();
    }


}
