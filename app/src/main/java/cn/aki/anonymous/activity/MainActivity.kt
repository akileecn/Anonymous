package cn.aki.anonymous.activity

import android.os.Bundle
import android.os.Handler
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.text.Html
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.TextView
import android.widget.Toast
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
    private var mCurrentForumId: Int = 0 // 当前版块ID
    @Volatile private var mLoading: Boolean = false // 是否正在加载
    private var mCurrentPage: Int = 0 // 当前页数
    private var mThreadList = mutableListOf<PostThread>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initListener()
        initData()
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

    }

    /**
     * 初始化监听器
     */
    private fun initListener() {
        forum_list.setOnItemClickListener { _, view, _, _ ->
            val forum = view.tag as Forum
            if (forum.id != mCurrentForumId) {
                mCurrentForumId = forum.id
                content_dl.closeDrawer(GravityCompat.END)
                loadThread(true)
            }
        }
        thread_srl.setOnRefreshListener {
            loadThread(true)
        }
        thread_list.setOnScrollListener(object : AbsListView.OnScrollListener {
            override fun onScroll(view: AbsListView?, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {
            }

            override fun onScrollStateChanged(view: AbsListView?, scrollState: Int) {
                if ((scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                        || scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING
                        ) && view!!.lastVisiblePosition >= view.count - 1) {
                    loadThread()
                }
            }

        })
    }

    /**
     * 初始化数据
     */
    private fun initData() {
        loadForum()
        thread_list.adapter = object : MyBaseAdapter<PostThread>(mThreadList) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
                val item = getItem(position)
                val view = convertView ?: View.inflate(this@MainActivity, R.layout.item_content, null)
                view.text_user_id.text = item.userid
                view.text_id.text = item.recodeId
                view.text_now.text = item.recodeNow
                view.text_content.text = Html.fromHtml(item.content)
                return view
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
                for ((id) in it) {
                    // 加载第一个非时间线版块
                    if (id != -1) {
                        mCurrentForumId = id
                        loadThread()
                        return@post
                    }
                }
            }
        }
    }

    /**
     * 加载串列表
     */
    private fun loadThread(refresh: Boolean = false) {
        if (mLoading) return
        mLoading = true
        if (refresh) {
            mCurrentPage = 1
        } else {
            mCurrentPage++
        }
        DataClient.listThread(mCurrentForumId, mCurrentPage) {
            mHandler.post {
                thread_srl.isRefreshing = false
                mLoading = false
                if (it.isEmpty()) {
                    Toast.makeText(this@MainActivity, "无更多数据", Toast.LENGTH_SHORT).show()
                    return@post
                }
                if (refresh) mThreadList.clear()
                mThreadList.addAll(mThreadList.size, it)
                thread_list.requestLayout()
            }
        }
    }

}
