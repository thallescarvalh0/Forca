package br.edu.ifsp.scl.sdm.pa2.forca.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import br.edu.ifsp.scl.sdm.pa2.forca.R
import br.edu.ifsp.scl.sdm.pa2.forca.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val activityMainBinding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityMainBinding.root)

        activityMainBinding.btnJogar.setOnClickListener {
            val intentJogar: Intent = Intent(baseContext, JogarActivity::class.java)
            startActivity(intentJogar)
        }

    }

}