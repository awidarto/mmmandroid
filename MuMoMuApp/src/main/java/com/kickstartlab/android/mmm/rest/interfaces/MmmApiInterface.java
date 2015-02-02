package com.kickstartlab.android.mmm.rest.interfaces;

import com.kickstartlab.android.mmm.rest.models.Asset;
import com.kickstartlab.android.mmm.rest.models.Feed;
import com.kickstartlab.android.mmm.rest.models.Location;
import com.kickstartlab.android.mmm.rest.models.MemberData;
import com.kickstartlab.android.mmm.rest.models.Rack;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Created by awidarto on 10/14/14.
 */
public interface MmmApiInterface {

    @FormUrlEncoded
    @POST("/auth/login")
    public void doLogin(@Field("user") String user, @Field("pwd") String pwd, Callback<MemberData> response);

    @GET("/auth/logout/{key}")
    public void logout(
            @Path("key") String key,
            Callback callback
    );

    @GET("/feed")
    public void getFeed(Callback<List<Feed>> response);

}
