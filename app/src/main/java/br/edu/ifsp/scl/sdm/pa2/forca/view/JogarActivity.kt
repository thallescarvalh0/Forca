package br.edu.ifsp.scl.sdm.pa2.forca.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import br.edu.ifsp.scl.sdm.pa2.forca.R
import br.edu.ifsp.scl.sdm.pa2.forca.databinding.ActivityJogarBinding
import br.edu.ifsp.scl.sdm.pa2.forca.model.Palavra
import br.edu.ifsp.scl.sdm.pa2.forca.viewmodel.ForcaViewModel

class JogarActivity : AppCompatActivity(){

    companion object{
        const val  NIVEL_DIFICULDADE = "DIFICULDADE"
        const val  QUANTIDADE_RODADAS = "RODADAS"
    }

    private val activityJogarBinding: ActivityJogarBinding by lazy {
        ActivityJogarBinding.inflate(layoutInflater)
    }
    private lateinit var configurarActivityResultLauncher: ActivityResultLauncher<Intent>
    private var nivelDificuldade : Int = 0
    private var rodadas : Int = 0

    private var jogandoRodada : Boolean = false
    private lateinit var forcaViewModel: ForcaViewModel

    private var idPalavra = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityJogarBinding.root)

        forcaViewModel = ViewModelProvider
            .AndroidViewModelFactory(this.application)
            .create(ForcaViewModel::class.java)

        configurarActivityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()){ result ->
            if (result?.resultCode == RESULT_OK){
                with(result){
                    data?.getIntExtra(NIVEL_DIFICULDADE, 0).takeIf { it!=null }.let {
                        nivelDificuldade = it.toString().toInt()
                    }
                    data?.getIntExtra(QUANTIDADE_RODADAS, 0).takeIf { it!=null }.let {
                        rodadas = it.toString().toInt()
                    }
                }

                if ((nivelDificuldade > 0) && (rodadas > 0)) {
                    activityJogarBinding.btnIniciarRodadas.isEnabled = true
                    activityJogarBinding.nivelDificuldadeTv.text = "Dificuldade: $nivelDificuldade"
                    activityJogarBinding.quantidadeRodadasTv.text = "Quantidade Rodadas: $rodadas"
                }
            }
        }

        cliqueBotoesAlfabeto()

        with(activityJogarBinding){
            btnIniciarRodadas.isEnabled = false

            btnIniciarRodadas.setOnClickListener {
                forcaViewModel.getIdentificadoresPorDificuldade(nivelDificuldade)
            }
        }

        forcaViewModel.identificadoresPalavrasDificuldade.observe(this){
            forcaViewModel.getIdentificadorPalavra()
        }

        forcaViewModel.idPalavraLista.observe(this){
            forcaViewModel.getPalavra(it.toInt())
        }

        forcaViewModel.palavraMld.observe(this){ lista ->
            lista.forEach { palavra ->
                palavra.palavra.also { palavra ->
                    activityJogarBinding.palavraTv.text = palavra
                }
            }
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
                val intentConfigurar = Intent(this, ConfigurarActivity::class.java)
                configurarActivityResultLauncher.launch(intentConfigurar)
                true
            }
            else -> false
        }
    }

    fun cliqueBotoesAlfabeto(){
        with(activityJogarBinding){
            aBtn.setOnClickListener { letraSelecionada("a", aBtn ) }
            bBtn.setOnClickListener { letraSelecionada("b", bBtn ) }
            cBtn.setOnClickListener { letraSelecionada("c", cBtn ) }
            dBtn.setOnClickListener { letraSelecionada("d", dBtn ) }
            eBtn.setOnClickListener { letraSelecionada("e", eBtn ) }
            fBtn.setOnClickListener { letraSelecionada("f", fBtn ) }
            gBtn.setOnClickListener { letraSelecionada("g", gBtn ) }
            hBtn.setOnClickListener { letraSelecionada("h", hBtn ) }
            iBtn.setOnClickListener { letraSelecionada("i", iBtn ) }
            jBtn.setOnClickListener { letraSelecionada("j", jBtn ) }
            kBtn.setOnClickListener { letraSelecionada("k", kBtn ) }
            lBtn.setOnClickListener { letraSelecionada("l", lBtn ) }
            mBtn.setOnClickListener { letraSelecionada("m", mBtn ) }
            nBtn.setOnClickListener { letraSelecionada("n", nBtn ) }
            oBtn.setOnClickListener { letraSelecionada("o", oBtn ) }
            pBtn.setOnClickListener { letraSelecionada("p", pBtn ) }
            qBtn.setOnClickListener { letraSelecionada("q", qBtn ) }
            rBtn.setOnClickListener { letraSelecionada("r", rBtn ) }
            sBtn.setOnClickListener { letraSelecionada("s", sBtn ) }
            tBtn.setOnClickListener { letraSelecionada("t", tBtn ) }
            uBtn.setOnClickListener { letraSelecionada("u", uBtn ) }
            vBtn.setOnClickListener { letraSelecionada("v", vBtn ) }
            wBtn.setOnClickListener { letraSelecionada("w", wBtn ) }
            xBtn.setOnClickListener { letraSelecionada("x", xBtn ) }
            yBtn.setOnClickListener { letraSelecionada("y", yBtn ) }
            zBtn.setOnClickListener { letraSelecionada("z", zBtn ) }
        }
    }
    fun letraSelecionada(letra: String, button: Button) {
        if (jogandoRodada){
            button.isEnabled = false
            adicionaLetraSelecionada(letra)
        }
    }
    fun adicionaLetraSelecionada(letra: String){
        var letrasSelecionadas: String
        with(activityJogarBinding){
            letrasSelecionadas = letrasSelecionadasTv.text.toString()
            if (letrasSelecionadas == ""){
                letrasSelecionadas = letra
            }
            else{
                letrasSelecionadas += " - $letra"
            }
            letrasSelecionadasTv.text = letrasSelecionadas
        }
        Toast.makeText(applicationContext, letra,Toast.LENGTH_SHORT).show()
    }


}