package com.example.myapplication

import android.app.DownloadManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Request.*
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.myapplication.R
import org.json.JSONException

class MainActivity : AppCompatActivity() {
    lateinit var worldcasesTV:TextView
    lateinit var worldRecoverdTV:TextView
    lateinit var worldDeathsTV:TextView
    lateinit var IndiaCasesTV:TextView
    lateinit var IndiaRecoveredTV:TextView
    lateinit var IndiaDeathsTV:TextView
   lateinit var stateRV:RecyclerView
   lateinit var stateRVAdapter: StateRVAdapter
   lateinit var stateList: List<StateModel>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        worldcasesTV=findViewById(R.id.idTVWorldCases)
        worldRecoverdTV=findViewById(R.id.idTVRecovered)
        worldDeathsTV=findViewById(R.id.idTVWorldDeaths)
        IndiaCasesTV=findViewById(R.id.idTVIndiaCases)
        IndiaDeathsTV=findViewById(R.id.idTVIndiaDeaths)
        IndiaRecoveredTV=findViewById(R.id.idTVIndiaRecovered)
        stateRV=findViewById(R.id.idRVStates)
        stateList=ArrayList<StateModel>()
        getStateInfo()
        getWorldInfo()
    }
    private fun getStateInfo() {
        val url = "https://api.rootnet.in/covid19-in/stats/latest"
        val queue = Volley.newRequestQueue(this@MainActivity);
        val request =
                JsonObjectRequest(Method.GET, url, null, {

                    response ->
                    try {
                        val dataObj = response.getJSONObject("data")
                        val summaryobj = dataObj.getJSONObject("summary")
                        val cases: Int = summaryobj.getInt("total")
                        val recovered: Int = summaryobj.getInt("discharged")
                        val deaths: Int = summaryobj.getInt("deaths")

                        IndiaCasesTV.text = cases.toString()
                        IndiaRecoveredTV.text = recovered.toString()
                        IndiaDeathsTV.text = deaths.toString()

                        val regionalArray = dataObj.getJSONArray("regional")
                        for (i in 0 until regionalArray.length()) {
                            val regionalObj = regionalArray.getJSONObject(i)
                            val stateName: String = regionalObj.getString("loc")
                            val cases: Int = regionalObj.getInt("totalConfirmed")
                            val deaths: Int = regionalObj.getInt("deaths")
                            val recovered: Int = regionalObj.getInt("discharged")
                            val stateModel = StateModel(stateName, recovered, deaths, cases)
                            stateList = stateList + stateModel
                        }

                        stateRVAdapter = StateRVAdapter(stateList)
                        stateRV.layoutManager = LinearLayoutManager(this)
                        stateRV.adapter = stateRVAdapter
                    } catch (e: JSONException) {
                        e.printStackTrace();
                    }
                }, { error ->
                    {

                        Toast.makeText(this, "Fail to get data", Toast.LENGTH_SHORT).show()

                    }
                })
        queue.add(request)
    }

    private fun getWorldInfo(){
        val url = "https://corona.lmao.ninja/v3/covid-19/all"
        val queue= Volley.newRequestQueue(this@MainActivity)
        val request =
                JsonObjectRequest(Request.Method.GET, url,null,{
                    response->
                    try{
                    val worldcases : Int = response.getInt("cases")
                    val worldDeaths : Int = response.getInt("deaths")
                    val worldRecoverd : Int = response.getInt("recovered")
                    worldDeathsTV.text= worldDeaths.toString()
                    worldRecoverdTV.text=worldRecoverd.toString()
                    worldcasesTV.text=worldcases.toString()
                }
                    catch(e:JSONException){
                    e.printStackTrace()
                    }
                },
                    { error ->

                    Toast.makeText(this, "Fail to get data", Toast.LENGTH_SHORT).show()
                }
                )
        queue.add(request)
                }
    }