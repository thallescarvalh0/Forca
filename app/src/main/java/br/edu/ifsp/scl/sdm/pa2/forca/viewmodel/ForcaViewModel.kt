package br.edu.ifsp.scl.sdm.pa2.forca.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import android.util.MutableInt
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import br.edu.ifsp.scl.sdm.pa2.forca.model.Dificuldade
import br.edu.ifsp.scl.sdm.pa2.forca.model.ForcaApi
import br.edu.ifsp.scl.sdm.pa2.forca.model.Palavras
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import kotlin.collections.ArrayList


class ForcaViewModel(application: Application): AndroidViewModel(application) {
    companion object {
        val BASE_URL = "http://nobile.pro.br/forcaws/"
    }

    val identificadoresPalavrasDificuldade: MutableLiveData<Dificuldade> = MutableLiveData()
    val palavraMld: MutableLiveData<Palavras> = MutableLiveData()
    var idPalavraLista: Int = 0
    val palavrasLista : MutableList<Int> = ArrayList()

    private val escopoCorrotinas = CoroutineScope(Dispatchers.IO + Job())

    private val retrofit: Retrofit = Retrofit
        .Builder()
        .baseUrl("${BASE_URL}")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val forcaApi: ForcaApi = retrofit.create(ForcaApi::class.java)

    fun getIdentificadoresPorDificuldade(id: Int){
        escopoCorrotinas.launch {
            forcaApi.retrieveIdentificadores(id).enqueue(object: Callback<Dificuldade> {
                override fun onResponse(
                    call: Call<Dificuldade>,
                    response: Response<Dificuldade>
                ) {
                    identificadoresPalavrasDificuldade.postValue(response.body())
                }

                @SuppressLint("LongLogTag")
                override fun onFailure(call: Call<Dificuldade>, t: Throwable) {
                    Log.e("$BASE_URL", t.message.toString())
                }
            })
        }
    }

    fun getIdentificadorPalavra(){
        val random = Random()
        //val identificadorPalavraDif = random.nextInt(identificadoresPalavrasDificuldade.value!!.identificador.size - 1)
        //val idPalavra = identificadoresPalavrasDificuldade.value!!.identificador[identificadorPalavraDif]
       // palavrasLista.add(idPalavra)
        //idPalavraLista = idPalavra
    }

    fun getPalavra(id: Int){
        escopoCorrotinas.launch {
            forcaApi.retrievePalavra(id).enqueue(object: Callback<Palavras>{
                override fun onResponse(call: Call<Palavras>, response: Response<Palavras>) {
                    palavraMld.postValue(response.body())
                }

                @SuppressLint("LongLogTag")
                override fun onFailure(call: Call<Palavras>, t: Throwable) {
                    Log.e("$BASE_URL", t.message.toString())
                }

            })
        }
    }

    fun startGame(dificuldade: Int){
        getIdentificadoresPorDificuldade(dificuldade)
        //getIdentificadorPalavra()
        //getPalavra(idPalavraLista)
    }

}