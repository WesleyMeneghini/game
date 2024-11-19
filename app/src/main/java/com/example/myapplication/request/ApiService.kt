package com.example.myapplication.request

import com.example.myapplication.model.ApiResponse
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("competition/307/category/7/leaderboard/full?&page=1&orderCol=&noCache=false")
    fun getLeaderboard(): Call<ApiResponse>
}