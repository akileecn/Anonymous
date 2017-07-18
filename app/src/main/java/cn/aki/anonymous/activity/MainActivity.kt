package cn.aki.anonymous.activity

import android.os.Bundle
import android.os.Handler
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import cn.aki.anonymous.R
import cn.aki.anonymous.entity.Forum
import cn.aki.anonymous.entity.PostThread
import cn.aki.anonymous.utils.DataClient
import cn.aki.anonymous.utils.MyBaseAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.item_content.view.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private val mHandler = Handler()
    private var mCurrentForumId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
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
     * 初始化视图
     */
    private fun initView() {
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        val toggle = ActionBarDrawerToggle(
                this, main_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        main_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)
        forum_list.setOnItemClickListener { _, view, _, _ ->
            val forum = view.tag as Forum
            if(forum.id != mCurrentForumId){
                mCurrentForumId = forum.id
                loadThread(mCurrentForumId, 1)
            }
        }
    }

    /**
     * 加载版块列表
     */
    private fun loadForum() {
        DataClient.listForum {
            mHandler.post {
                forum_list.adapter = object : MyBaseAdapter<Forum>(it) {
                    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
                        val item = getItem(position)
                        val view = (convertView ?: View.inflate(this@MainActivity, R.layout.item_forum, null)) as TextView
                        view.text = item.name
                        view.tag = item
                        return view
                    }
                }
                for((id) in it){
                    // 加载第一个非时间线版块
                    if(id != -1){
                        mCurrentForumId = id
                        loadThread(id, 1)
                        return@post
                    }
                }
            }
        }
    }

    /**
     * 加载串列表
     */
    private fun loadThread(forumId: Int, page: Int){
        DataClient.listThread(forumId, page){
            mHandler.post {
                thread_list.adapter = object : MyBaseAdapter<PostThread>(it) {
                    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
                        val item = getItem(position)
                        val view = convertView ?: View.inflate(this@MainActivity, R.layout.item_content, null)
                        view.text_user_id.text = item.userid
                        view.text_id.text = item.id
                        view.text_now.text = item.now
                        view.text_content.loadDataWithBaseURL(null, item.content, "text/html", "utf-8", null)
                        return view
                    }
                }
            }
        }
    }

}
