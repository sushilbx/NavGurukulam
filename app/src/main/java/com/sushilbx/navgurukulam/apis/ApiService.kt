package com.sushilbx.navgurukulam.apis

import com.sushilbx.navgurukulam.room.ScoreCard
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @GET("students")
    suspend fun getStudentsSince(@Query("since") sinceIso: String?): List<StudentDto>

    @POST("scorecards/sync")
    suspend fun syncScoreCard(@Body scoreCard: ScoreCard): Response<ScoreCard>


    @GET("scorecards")
    suspend fun getScoreCardsSince(@Query("since") sinceIso: String?): List<ScoreCardDto>

    @POST("students")
    suspend fun upsertStudent(@Body body: StudentDto): PushResult

    @POST("scorecards")
    suspend fun upsertScoreCard(@Body body: ScoreCardDto): PushResult

    companion object {
        fun create(baseUrl: String = "https://example.com/api/"): ApiService {
            val logger = okhttp3.logging.HttpLoggingInterceptor().apply {
                level = okhttp3.logging.HttpLoggingInterceptor.Level.BODY
            }
            val ok = okhttp3.OkHttpClient.Builder().addInterceptor(logger).build()
            return retrofit2.Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(ok)
                .addConverterFactory(retrofit2.converter.moshi.MoshiConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }
    }
}
