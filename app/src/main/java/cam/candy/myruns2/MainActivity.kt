package cam.candy.myruns2

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.tabs.TabLayoutMediator.TabConfigurationStrategy
import java.util.ArrayList

class MainActivity : AppCompatActivity() {
    private lateinit var fragmentA: FragmentA
    private lateinit var fragmentB: FragmentB
    private lateinit var fragmentC: FragmentC
    private lateinit var viewPager2: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var myMyFragmentStateAdapter: MyFragmentStateAdapter
    private lateinit var fragments: ArrayList<Fragment>
    private lateinit var tabTitles: Array<String>
    private lateinit var tabConfigurationStrategy: TabConfigurationStrategy
    private lateinit var tabLayoutMediator: TabLayoutMediator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Util.checkPermissions(this)

        supportActionBar?.title= getString(R.string.app_name)
        tabTitles = resources.getStringArray(R.array.tab_titles);


        viewPager2 = findViewById(R.id.viewpager)
        tabLayout = findViewById(R.id.tab)
        tabLayout.tabMode = TabLayout.MODE_FIXED
        tabLayout.tabGravity = TabLayout.GRAVITY_FILL

        fragmentA = FragmentA()
        fragmentB = FragmentB()
        fragmentC = FragmentC()

        fragments = ArrayList()
        fragments.add(fragmentA)
        fragments.add(fragmentB)
        fragments.add(fragmentC)

        myMyFragmentStateAdapter = MyFragmentStateAdapter(this, fragments)
        viewPager2.adapter = myMyFragmentStateAdapter

        tabConfigurationStrategy = TabConfigurationStrategy {
                tab: TabLayout.Tab, position: Int ->
            tab.text = tabTitles[position] }
        tabLayoutMediator = TabLayoutMediator(tabLayout, viewPager2, tabConfigurationStrategy)
        tabLayoutMediator.attach()




    }

    override fun onDestroy() {
        super.onDestroy()
        tabLayoutMediator.detach()
    }
}