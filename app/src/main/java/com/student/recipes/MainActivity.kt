package com.student.recipes

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tvRecipeList = findViewById<TextView>(R.id.tvRecipeList)

        try {
            val jsonString = readJsonFromAssets("recipes.json")
            val jsonArray = JSONArray(jsonString)
            val sb = StringBuilder()

            for (i in 0 until jsonArray.length()) {
                val recipe = jsonArray.getJSONObject(i)

                val id = recipe.optInt("id")
                val title = recipe.optString("title")
                val category = recipe.optString("category")
                val prepTime = recipe.optString("prepTime")
                val calories = recipe.optInt("calories")
                val protein = recipe.optString("protein")
                val carbs = recipe.optString("carbs")
                val fats = recipe.optString("fats")

                val tags = jsonArrayToList(recipe, "tags")
                val quantities = jsonArrayToList(recipe, "quantities")
                val ingredients = jsonArrayToList(recipe, "ingredients")
                val instructions = jsonArrayToList(recipe, "instructions")

                sb.append("#$id • $title ($category)\n")
                sb.append("  Süre: $prepTime | Kalori: $calories kcal\n")
                sb.append("  Protein: $protein | Karbonhidrat: $carbs | Yağ: $fats\n")

                if (tags.isNotEmpty()) {
                    sb.append("  Etiketler: ${tags.joinToString(", ")}\n")
                }

                // Miktarlı liste varsa onu, yoksa sade malzeme listesini göster
                val ingredientLines = if (quantities.isNotEmpty()) quantities else ingredients
                if (ingredientLines.isNotEmpty()) {
                    sb.append("  Malzemeler:\n")
                    ingredientLines.forEach { sb.append("    - $it\n") }
                }

                if (instructions.isNotEmpty()) {
                    sb.append("  Hazırlanışı:\n")
                    instructions.forEachIndexed { index, step ->
                        sb.append("    ${index + 1}. $step\n")
                    }
                }

                sb.append("\n")
            }

            tvRecipeList.text = sb.toString()
        } catch (e: Exception) {
            tvRecipeList.text = "Tarifler yüklenirken hata oluştu: ${e.message}"
        }
    }

    private fun jsonArrayToList(obj: JSONObject, key: String): List<String> {
        val arr = obj.optJSONArray(key) ?: return emptyList()
        return List(arr.length()) { arr.optString(it) }
    }

    private fun readJsonFromAssets(fileName: String): String {
        return assets.open(fileName).bufferedReader().use { it.readText() }
    }
}
