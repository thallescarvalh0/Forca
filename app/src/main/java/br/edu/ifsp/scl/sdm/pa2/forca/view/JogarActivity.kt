package br.edu.ifsp.scl.sdm.pa2.forca.view

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.toColor
import androidx.lifecycle.ViewModelProvider
import br.edu.ifsp.scl.sdm.pa2.forca.R
import br.edu.ifsp.scl.sdm.pa2.forca.databinding.ActivityJogarBinding
import br.edu.ifsp.scl.sdm.pa2.forca.viewmodel.ForcaViewModel
import java.text.Normalizer

class JogarActivity : AppCompatActivity(){

    companion object{
        const val  NIVEL_DIFICULDADE = "DIFICULDADE"
        const val  QUANTIDADE_RODADAS = "RODADAS"
        const val  MAX_TENTATIVAS = 6
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

    private var tentativasAdivinhar: Int = 0

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
                    activityJogarBinding.relatorioTv.text = ""
                    relatorioRodadaAcerto = ""
                    relatorioRodadaErro = ""
                    rodadaAtual = 0
                    tentativasAdivinhar = 0
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

            btnTentativaAcerto.setOnClickListener {
                val palavraTentativa = removerAcentos(tentativaAcertoEt.text.toString()).toString()
                if (palavraTentativa != "" ){verificaPalavra(palavraTentativa, true)}
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
                    Log.i("palavraFORCA", palavraRodada)
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

            verificaPalavra(letra,false)
        }
    }

    fun verificaPalavra(palavra: String, palavraCompleta: Boolean){
        if (jogandoRodada){
            if (tentativasAdivinhar < MAX_TENTATIVAS ){
                if (!palavraCompleta){
                    if (qtdeLetrasRodadaAtual <= qtdeLetrasRodada) {
                        if (palavraRodada.contains(palavra, true)){
                            Toast.makeText(applicationContext,
                                "Correta $qtdeLetrasRodada - $qtdeLetrasRodadaAtual",
                                Toast.LENGTH_SHORT).show()
                            verificaAcertoPalavra++
                            adicionaLetraSelecionada(palavra)
                        }
                        else{
                            Toast.makeText(applicationContext,
                                "Errada $qtdeLetrasRodada - $qtdeLetrasRodadaAtual",
                                Toast.LENGTH_SHORT).show()
                        }

                        qtdeLetrasRodadaAtual++

                        if (verificaAcertoPalavra == qtdeLetrasRodada){
                            Toast.makeText(applicationContext,
                                "Acertou letra",
                                Toast.LENGTH_SHORT).show()
                            //activityJogarBinding.palavraTv.text = palavraRodada

                            reiniciarRodada(true)
                            //exibe e adiciona palavra na listagem de acertos
                        }
                    }
                    else{
                        Toast.makeText(applicationContext,
                            "Sem letras para selecionar!\nLetras rodada: $qtdeLetrasRodada - Tentativas ${qtdeLetrasRodadaAtual - 1}",
                            Toast.LENGTH_SHORT).show()
                    }
                }
                else{
                    if (palavraRodada.contains(palavra, true)){
                        Toast.makeText(applicationContext,
                            "Acertou palavra",
                            Toast.LENGTH_SHORT).show()
                        reiniciarRodada(true)
                    }
                    else{
                        tentativasAdivinhar ++
                        desenhaCorpo()
                    }
                }
            }
            else
            {
                reiniciarRodada(false)
            }
        }
    }

    fun desenhaCorpo(){
        when(tentativasAdivinhar){
            1 -> {activityJogarBinding.cabecaJogarTv.setTextColor(Color.RED)}
            2 -> {activityJogarBinding.troncoTv.setTextColor(Color.RED)}
            3 -> {activityJogarBinding.braco1Tv.setTextColor(Color.RED)}
            4 -> {activityJogarBinding.braco2Tv.setTextColor(Color.RED)}
            5 -> {activityJogarBinding.perna1Tv.setTextColor(Color.RED)}
            6 -> {activityJogarBinding.perna2Tv.setTextColor(Color.RED)}
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

            activityJogarBinding.cabecaJogarTv.setTextColor(Color.BLACK)
            activityJogarBinding.troncoTv.setTextColor(Color.BLACK)
            activityJogarBinding.braco1Tv.setTextColor(Color.BLACK)
            activityJogarBinding.braco2Tv.setTextColor(Color.BLACK)
            activityJogarBinding.perna1Tv.setTextColor(Color.BLACK)
            activityJogarBinding.perna2Tv.setTextColor(Color.BLACK)

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
            tentativasAdivinhar = 0

            activityJogarBinding.relatorioTv.text = "Relatório:\n $relatorioRodadaAcerto\n $relatorioRodadaErro"
        }
    }
}

