package cn.aki.anonymous.activity

import android.os.Bundle
import android.os.Handler
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import cn.aki.anonymous.R
import cn.aki.anonymous.dto.ForumListDto
import cn.aki.anonymous.entity.Forum
import cn.aki.anonymous.utils.C
import cn.aki.anonymous.utils.HttpUtils
import cn.aki.anonymous.utils.MyBaseAdapter
import com.alibaba.fastjson.JSON
import com.google.common.base.Throwables
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private val mHandler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        val toggle = ActionBarDrawerToggle(
                this, main_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        main_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)
        loadForum()
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
    private fun loadForum() {
        val request = Request.Builder().url(C.Api.FORUM_LIST).build()
        HttpUtils.client.newCall(request).enqueue(object :Callback{
            override fun onResponse(call: Call?, response: Response?) {
                if(response == null || !response.isSuccessful){
                    Log.e("loadForum", "no response")
                    return
                }
                val listDto = JSON.parseObject(response.body()!!.string(), ForumListDto::class.java)
                if(!listDto.success || listDto.forum == null){
                    Log.e("loadForum", "fail")
                    return
                }
                mHandler.post {
                    channel_list.adapter = object : MyBaseAdapter<Forum>(listDto.forum!!) {
                        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
                            val item = getItem(position)
                            if (convertView == null || convertView !is TextView) {
                                val view = View.inflate(this@MainActivity, R.layout.item_forum, null) as TextView
                                view.text = item.name
                                view.tag = item
                                return view
                            } else {
                                convertView.text = item.name
                                return convertView
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call?, e: IOException?) {
                Log.e("loadForum", Throwables.getStackTraceAsString(e))
            }
        })
    }
}
