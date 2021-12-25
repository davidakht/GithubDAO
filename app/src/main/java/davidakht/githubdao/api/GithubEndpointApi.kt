package davidakht.githubdao.api

import davidakht.githubdao.data.User
import davidakht.githubdao.data.UserDetailResponse
import davidakht.githubdao.data.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubEndpointApi {
    @GET("search/users")
    @Headers("Authorization: token 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")
    fun getSearchUsers(
        @Query("q") query: String
    ): Call<UserResponse>

    @GET("users/{username}")
    @Headers("Authorization: token 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")
    fun getUserDetail(
        @Path("username") username: String
    ): Call<UserDetailResponse>

    @GET("users/{username}/followers")
    @Headers("Authorization: token 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")
    fun getFollowers(
        @Path("username") username: String
    ): Call<ArrayList<User>>

    @GET("users/{username}/following")
    @Headers("Authorization: token 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")
    fun getFollowing(
        @Path("username") username: String
    ): Call<ArrayList<User>>

}