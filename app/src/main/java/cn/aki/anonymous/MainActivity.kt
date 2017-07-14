package cn.aki.anonymous

import android.database.DataSetObserver
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListAdapter
import android.widget.SimpleAdapter
import cn.aki.anonymous.utils.MyBaseAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.item_content.view.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        val toggle = ActionBarDrawerToggle(
                this, main_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        main_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)
        loadChannel()
    }

    override fun onBackPressed() {
        if (main_layout.isDrawerOpen(GravityCompat.START)) {
            main_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId


        if (id == R.id.action_settings) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val id = item.itemId

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
        main_layout.closeDrawer(GravityCompat.START)
        return true
    }

    /**
     * 加载版块列表
     */
    private fun loadChannel() {
        val list = listOf("xxx", "yyy", "zzz"
                , "xxx", "yyy", "zzz"
                , "xxx", "yyy", "zzz"
                , "xxx", "yyy", "zzz"
                , "xxx", "yyy", "zzz")
        channel_list.adapter = object : MyBaseAdapter<String>(list) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
                val item = getItem(position)
                if (convertView == null) {
                    val view = View.inflate(this@MainActivity, R.layout.item_content, null)
                    view.text_user.text = item
                    return view
                } else {
                    convertView.text_user.text = item
                    return convertView
                }
            }
        }
    }
}
