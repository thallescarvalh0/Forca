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
    private var rodadaAtual: Int = 0

    private var qtdeLetrasRodada: Int = 0
    private var qtdeLetrasRodadaAtual: Int = 1
    private var palavraRodada : String = ""

    private var jogandoRodada : Boolean = false
    private lateinit var forcaViewModel: ForcaViewModel


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
                activityJogarBinding.rodadaAtualTv.text = "Rodada atual: 1"
                jogandoRodada = true
                btnIniciarRodadas.isEnabled = false
            }
        }
        forcaViewModel.identificadoresPalavrasDificuldade.observe(this){
            forcaViewModel.getIdentificadorPalavra(rodadas)
        }

        forcaViewModel.idPalavraLista.observe(this){
            if (palavraRodada == ""){
                forcaViewModel.getPalavra(forcaViewModel.palavrasLista[rodadaAtual])

            }
        }

        forcaViewModel.palavraMld.observe(this){ lista ->
            lista.forEach { palavra ->
                palavra.palavra.also { palavra ->
                    palavraRodada = palavra
                    activityJogarBinding.palavraTv.text = palavraRodada
                }
                palavra.letras.also { letras ->
                    qtdeLetrasRodada = letras
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

            if (qtdeLetrasRodadaAtual <= 6) {
                if (palavraRodada.contains(letra, false)){
                    Toast.makeText(applicationContext,
                        "Correta $qtdeLetrasRodada - $qtdeLetrasRodadaAtual",
                        Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(applicationContext,
                        "Errada $qtdeLetrasRodada - $qtdeLetrasRodadaAtual",
                        Toast.LENGTH_SHORT).show()
                }
                qtdeLetrasRodadaAtual++
            }
            else{
                //Vou adicionar apenas se o cara perder ou ganhar a rodada
                rodadaAtual ++
            }



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