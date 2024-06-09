package com.example.dietcalculator.dao.fooddatadownloader

import com.example.dietcalculator.model.Food
import com.example.dietcalculator.utility.AppConstants
import com.example.dietcalculator.utility.Utility
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import java.util.Collections
import java.util.Locale

class OpenFoodDataDownloader: IRemoteFoodDataDownloader {

    private val client = OkHttpClient()
    private val delegates : MutableList<IRemoteFoodDataDownloaderDelegate> = mutableListOf()
    override fun downloadFoodData(n: Int, chunckSize: Int) {
        val countryCode = Locale.getDefault().getDisplayCountry(Locale.US)
        val country = Utility.formatCountryName(countryCode)
        val requestBuilder = Request.Builder().addHeader("User-Agent", "Android")
        val foodsForRequest = Math.max(chunckSize, 100)
        this.executeRequests(country, foodsForRequest, requestBuilder, n)
    }

    private fun executeRequests(country: String,pageSize: Int, requestBuilder: Request.Builder, numberFood: Int){
        var foodPages = -1
        var currentPage = 1
        var foods = 0
        val foodNames : MutableSet<String> = mutableSetOf()
        while( foodPages<0 || (currentPage<=foodPages && foods<numberFood )){
            val nextEndpoint = AppConstants.getFoodFactsEndpoint(country, pageSize, currentPage++)
            val request = requestBuilder.url(nextEndpoint).get().build()
            try {
                val response = client.newCall(request).execute()
                if (!response.isSuccessful){
                    this.delegates.forEach{
                        it.errorRaised(this, ResponseException("Request failed: Status code='%s'".format(response.code())))
                    }
                    break
                }
                val chunck = computeFoodChunck(response, foodNames)
                this.delegates.forEach{
                    it.foodDownloaded(this, chunck)
                }
                foods+=chunck.size
            }catch (e: Throwable){
                this.delegates.forEach{
                    it.errorRaised(this, e)
                }
                break
            }
        }
    }

    private fun computeFoodChunck(response: Response, foodNames: MutableSet<String>): Collection<Food>{
        val jsonObj = JSONObject(response.body()?.string())
        val jsonArray = jsonObj.getJSONArray("products")
        if(jsonArray.length()==0){
            return Collections.emptyList()
        }
        val result = mutableListOf<Food>()
        for( index in 0..<jsonArray.length() ){
            try {
                val currentProduct = jsonArray.getJSONObject(index)
                val productName = currentProduct.getString("product_name")
                if( foodNames.contains(productName) ){
                    continue
                }
                val nutriments = currentProduct.getJSONObject("nutriments")
                val kcal = if( nutriments.has("energy-kcal_100g") )nutriments.getInt("energy-kcal_100g") else 0
                val alcol = if(nutriments.has("alcohol_100g")) nutriments.getInt("alcohol_100g")  else 0
                val carbo = if( nutriments.has("carbohydrates_100g") )nutriments.getInt("carbohydrates_100g") else 0.0
                val fat = if(nutriments.has("fat_100g")) nutriments.getInt("fat_100g") else 0
                val proteins = if(nutriments.has("proteins_100g")) nutriments.getInt("proteins_100g") else 0
                val salt = if(nutriments.has("salt_100g")) nutriments.getDouble("salt_100g") else 0.0
                val isVegan = getVegan(currentProduct.getJSONArray("ingredients_analysis_tags"))
                val food = Food(name = productName, kcal=kcal.toDouble(), protein = proteins.toDouble(), fat=fat.toDouble(), carbo=carbo.toDouble(), alcol=alcol.toDouble(), salt=salt, isVegan=isVegan)
                foodNames.add(productName)
                result.add(food)
            }catch (e: Exception){
                println()
            }
        }
        return result
    }

    private fun getVegan(keywordsArray: JSONArray): Boolean{
        val target = ":vegan"
        for( i in 0..<keywordsArray.length() ){
            val tag = keywordsArray.getString(i)
            if(tag.contains(target)){
                return true
            }
        }
        return false
    }


    override fun addDelegate(delegate: IRemoteFoodDataDownloaderDelegate) {
        this.delegates.add(delegate)
    }
}

fun Math.max(vararg integers: Int): Int{
    var max = integers[0]
    for( i in 1..<integers.size){
        if( integers[i]>max ){
            max = integers[i]
        }
    }
    return max
}