package benten.puzzle.games.data;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by falcon on 24/10/2017.
 */

public interface Json_data_Interface {
    @GET("api/v1/otherapps/all")
    Call<ArrayList<json_data>> getJSON();
}
