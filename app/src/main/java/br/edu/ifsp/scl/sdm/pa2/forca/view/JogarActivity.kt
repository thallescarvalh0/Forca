package br.edu.ifsp.scl.sdm.pa2.forca.view

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import br.edu.ifsp.scl.sdm.pa2.forca.R
import br.edu.ifsp.scl.sdm.pa2.forca.databinding.ActivityJogarBinding
import br.edu.ifsp.scl.sdm.pa2.forca.viewmodel.ForcaViewModel
import java.text.Normalizer

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

    private var verificaAcertoPalavra: Int = 0
    private var jogandoRodada : Boolean = false
    private lateinit var forcaViewModel: ForcaViewModel

    private var relatorioRodadaAcerto: String = ""
    private var relatorioRodadaErro: String = ""

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
                if (rodadaAtual < rodadas){
                    forcaViewModel.getIdentificadoresPorDificuldade(nivelDificuldade)
                    activityJogarBinding.rodadaAtualTv.text = "Rodada atual: ${rodadaAtual + 1}"
                    jogandoRodada = true
                    btnIniciarRodadas.isEnabled = false
                }
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
                    palavraRodada = removerAcentos(palavra).toString()
                }
                palavra.letras.also { letras ->
                    qtdeLetrasRodada = letras
                }
            }
            activityJogarBinding.palavraTv.text = "Dica: $qtdeLetrasRodada letras"
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

            if (qtdeLetrasRodadaAtual <= qtdeLetrasRodada) {
                if (palavraRodada.contains(letra, true)){
                    Toast.makeText(applicationContext,
                        "Correta $qtdeLetrasRodada - $qtdeLetrasRodadaAtual",
                        Toast.LENGTH_SHORT).show()
                    verificaAcertoPalavra++
                    adicionaLetraSelecionada(letra)
                }
                else{
                    Toast.makeText(applicationContext,
                        "Errada $qtdeLetrasRodada - $qtdeLetrasRodadaAtual",
                        Toast.LENGTH_SHORT).show()
                }

                qtdeLetrasRodadaAtual++

                if (verificaAcertoPalavra == qtdeLetrasRodada){
                    Toast.makeText(applicationContext,
                        "Acertou palavra",
                        Toast.LENGTH_LONG).show()
                    activityJogarBinding.palavraTv.text = palavraRodada

                    reiniciarRodada(true)
                    //exibe e adiciona palavra na listagem de acertos
                }
            }
            else{
                Toast.makeText(applicationContext,
                    "Errou palavra",
                    Toast.LENGTH_LONG).show()
                activityJogarBinding.palavraTv.text = "Rodada $rodadaAtual: $palavraRodada"

               reiniciarRodada(false)
                //exibe e adiciona palavra na listagem de acertos
            }
        }
            /*else{
                //Vou adicionar apenas se o cara perder ou ganhar a rodada
                rodadaAtual ++
            }*/
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

    fun removerAcentos(str: String?): String? {
        return Normalizer.normalize(str, Normalizer.Form.NFD).replace("[^\\p{ASCII}]", "")
    }

    fun reiniciarRodada(acerto: Boolean){
        with(activityJogarBinding){
            aBtn.isEnabled = true
            bBtn.isEnabled = true
            cBtn.isEnabled = true
            dBtn.isEnabled = true
            eBtn.isEnabled = true
            fBtn.isEnabled = true
            gBtn.isEnabled = true
            hBtn.isEnabled = true
            iBtn.isEnabled = true
            jBtn.isEnabled = true
            kBtn.isEnabled = true
            lBtn.isEnabled = true
            mBtn.isEnabled = true
            nBtn.isEnabled = true
            oBtn.isEnabled = true
            pBtn.isEnabled = true
            qBtn.isEnabled = true
            rBtn.isEnabled = true
            sBtn.isEnabled = true
            tBtn.isEnabled = true
            uBtn.isEnabled = true
            vBtn.isEnabled = true
            wBtn.isEnabled = true
            xBtn.isEnabled = true
            yBtn.isEnabled = true
            zBtn.isEnabled = true
            btnIniciarRodadas.isEnabled = true
            jogandoRodada = false

            if (acerto) {
                if (relatorioRodadaAcerto == ""){
                    relatorioRodadaAcerto = "Acertos: $palavraRodada"
                }
                else{
                    relatorioRodadaAcerto += ", $palavraRodada"
                }
            }else{
                if (relatorioRodadaErro == ""){
                    relatorioRodadaErro = "Erros: $palavraRodada"
                }
                else{
                    relatorioRodadaErro += ", $palavraRodada"
                }
            }
            if (rodadaAtual < rodadas) {
                rodadaAtual ++
            }else{
                rodadaAtual = 0
            }
            activityJogarBinding.rodadaAtualTv.text = "Rodada atual: $rodadaAtual"
            letrasSelecionadasTv.text = ""
            palavraRodada = ""
            qtdeLetrasRodadaAtual = 1

            activityJogarBinding.relatorioTv.text = "Relatório:\n $relatorioRodadaAcerto\n $relatorioRodadaErro"
        }
    }
}

