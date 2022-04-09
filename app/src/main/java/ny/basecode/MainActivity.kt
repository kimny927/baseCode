package ny.basecode

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ny.base.databinding.dataBindings
import ny.basecode.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    val binding by dataBindings<ActivityMainBinding>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}