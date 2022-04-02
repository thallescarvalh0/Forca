package br.edu.ifsp.scl.sdm.pa2.forca.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import br.edu.ifsp.scl.sdm.pa2.forca.R
import br.edu.ifsp.scl.sdm.pa2.forca.databinding.ActivityJogarBinding

class JogarActivity : AppCompatActivity(), View.OnClickListener{

    private val activityJogarBinding: ActivityJogarBinding by lazy {
        ActivityJogarBinding.inflate(layoutInflater)
    }
    private lateinit var configurarActivityResultLauncher: ActivityResultLauncher<Intent>
    private var nivelDificuldade : Int = 0
    private var rodadas : Int = 0

    companion object{
        const val  NIVEL_DIFICULDADE = "DIFICULDADE"
        const val  QUANTIDADE_RODADAS = "RODADAS"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityJogarBinding.root)

        activityJogarBinding.btnIniciarRodadas.isEnabled = false

        configurarActivityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()){ result ->
            if (result?.resultCode == RESULT_OK){
                with(result){
                    data?.getIntExtra(NIVEL_DIFICULDADE, 0).takeIf { it!=null }.let {
                        nivelDificuldade = it.toString().toInt()
                        Log.v("dificuldade", "$nivelDificuldade")
                    }
                    data?.getIntExtra(QUANTIDADE_RODADAS, 0).takeIf { it!=null }.let {
                        rodadas = it.toString().toInt()
                        Log.v("rodadae", "$rodadas")
                    }
                }

                if ((nivelDificuldade > 0) && (rodadas > 0)) {
                    activityJogarBinding.btnIniciarRodadas.isEnabled = true
                    activityJogarBinding.nivelDificuldadeTv.text = "Dificuldade: $nivelDificuldade"
                    activityJogarBinding.quantidadeRodadasTv.text = "Quantidade Rodadas: $rodadas"
                }
            }
        }

        activityJogarBinding.btnIniciarRodadas.setOnClickListener {
            //todo iniciar rodada chamando as rotinas do view model para buscar a palavra na api
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater: MenuInflater = menuInflater
        menuInflater.inflate(R.menu.menu_jogar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.configurarJogo -> {
                val intentConfigurar: Intent = Intent(this, ConfigurarActivity::class.java)
                configurarActivityResultLauncher.launch(intentConfigurar)
                true
            }
            else -> false
        }
    }

    override fun onClick(p0: View?) {
        TODO("Not yet implemented")
    }


}