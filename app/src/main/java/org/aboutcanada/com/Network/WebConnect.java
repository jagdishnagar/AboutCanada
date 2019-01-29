package org.aboutcanada.com.Network;

import org.aboutcanada.com.Model.About;
import retrofit2.Call;
import retrofit2.http.GET;

public interface WebConnect {

    @GET("s/2iodh4vg0eortkl/facts.json")
    Call<About> getCanadata();
}
