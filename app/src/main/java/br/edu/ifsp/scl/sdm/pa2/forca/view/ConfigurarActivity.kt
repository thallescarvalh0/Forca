package br.edu.ifsp.scl.sdm.pa2.forca.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import br.edu.ifsp.scl.sdm.pa2.forca.R
import br.edu.ifsp.scl.sdm.pa2.forca.databinding.ActivityConfigurarBinding

class ConfigurarActivity : AppCompatActivity() {

    private val configurarActivityBinding : ActivityConfigurarBinding by lazy {
        ActivityConfigurarBinding.inflate(layoutInflater)
    }
    private var nivelDificildade: Int = 0
    private var rodadas: Int = 0

    companion object{
        const val  NIVEL_DIFICULDADE = "DIFICULDADE"
        const val  QUANTIDADE_RODADAS = "RODADAS"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(configurarActivityBinding.root)
        title = "Configuração"

        configurarActivityBinding.opcoesDificuldadeRg.setOnCheckedChangeListener { radioGroup, _ ->
            when(radioGroup.checkedRadioButtonId){
                R.id.rbFacil -> {
                    nivelDificildade = 1
                }
                R.id.rbMedio -> {
                    nivelDificildade = 2
                }
                R.id.rbDificil -> {
                    nivelDificildade = 3
                }
            }
        }
        configurarActivityBinding.btnSalvarConfig.setOnClickListener {
            with(configurarActivityBinding){
                val quantidadeRodadas = quantidadeRodadaEt.text.toString()
                rodadas = convertStringToInt(quantidadeRodadas)

                if (((rodadas > 0) && (rodadas <= 15)) && (nivelDificildade > 0)) {
                    val retornoIntent = Intent()
                    retornoIntent.putExtra(NIVEL_DIFICULDADE, nivelDificildade)
                    retornoIntent.putExtra(QUANTIDADE_RODADAS, rodadas)

                    setResult(RESULT_OK, retornoIntent)
                    finish()
                }else{
                    Toast.makeText(applicationContext, "Selecione o nível de Dificuldade e Rodadas (1 a 15) para Iniciar", Toast.LENGTH_SHORT).show()
                }

            }
        }
    }
    fun convertStringToInt(string: String) : Int{
        if (string != "") { return string.toInt()}
        return 0
    }
}