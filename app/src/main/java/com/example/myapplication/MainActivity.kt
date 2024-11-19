package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.adapter.LeaderboardAdapter
import com.example.myapplication.model.ApiResponse
import com.example.myapplication.model.Line
import com.example.myapplication.request.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {

    private val items: MutableList<Line> = mutableListOf()

    private lateinit var adapter: LeaderboardAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Configuração do RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapter = LeaderboardAdapter(items)
        recyclerView.adapter = adapter

        // Atualiza o RecyclerView para refletir os dados iniciais
        adapter.notifyDataSetChanged()

        fetchData()



        // Configura o botão para atualizar os dados
        val button = findViewById<Button>(R.id.btnUpdate)
        button.setOnClickListener {
            Toast.makeText(this, "Atualizando dados...", Toast.LENGTH_SHORT).show()

            fetchData()
        }


    }

    private fun fetchData() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api-leaderboard.azurewebsites.net/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)
        apiService.getLeaderboard().enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()

                    // Loga o resultado completo da API
                    Log.d("API Response", "Header: ${apiResponse?.header}")
                    apiResponse?.lines?.forEach { line ->
                        Log.d("API Response", "Line Info: ${line.info}")
                    }

                    // Atualiza a lista de itens
                    items.clear()
                    items.addAll(apiResponse?.lines ?: emptyList())

                    Log.d("teste", "items: ${items.size}")

                    // Atualiza a RecyclerView
                    adapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(this@MainActivity, "Erro ao buscar dados!", Toast.LENGTH_SHORT).show()
                    Log.e("API Error", "Erro ao buscar dados: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Erro: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.e("API Failure", "Erro ao fazer a requisição: ${t.message}")
            }
        })
    }

}


