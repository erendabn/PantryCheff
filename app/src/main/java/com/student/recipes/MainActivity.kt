package com.student.recipes

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tvRecipeList = findViewById<TextView>(R.id.tvRecipeList)
        
        try {
            val jsonString = readJsonFromAssets("recipes.json")
            val jsonArray = JSONArray(jsonString)
            val stringBuilder = StringBuilder()

            for (i in 0 until jsonArray.length()) {
                val recipe = jsonArray.getJSONObject(i)
                val name = recipe.getString("name")
                val category = recipe.getString("category")
                val calories = recipe.getInt("calories")
                
                stringBuilder.append("• $name ($category)\n  Kalori: $calories kcal\n\n")
            }
            
            tvRecipeList.text = stringBuilder.toString()
        } catch (e: Exception) {
            tvRecipeList.text = "Tarifler yüklenirken hata oluştu: ${e.message}"
        }
    }

    private fun readJsonFromAssets(fileName: String): String {
        val stringBuilder = StringBuilder()
        val inputStream = assets.open(fileName)
        val reader = BufferedReader(InputStreamReader(inputStream))
        var line: String? = reader.readLine()
        while (line != null) {
            stringBuilder.append(line)
            line = reader.readLine()
        }
        reader.close()
        return stringBuilder.toString()
    }
}