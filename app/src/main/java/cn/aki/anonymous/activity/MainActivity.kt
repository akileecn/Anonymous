package cn.aki.anonymous.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.annotation.UiThread
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.BaseAdapter
import android.widget.TextView
import cn.aki.anonymous.R
import cn.aki.anonymous.entity.Forum
import cn.aki.anonymous.entity.Notice
import cn.aki.anonymous.entity.PostThread
import cn.aki.anonymous.utils.*
import com.alibaba.fastjson.JSON
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.dialog_notice.view.*
import kotlinx.android.synthetic.main.item_content.view.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private var mCurrentForumId: Int = 0 // 当前版块ID
    @Volatile private var mLoading: Boolean = false // 是否正在加载
    private var mCurrentPage: Int = 0 // 当前页数
    private var mThreadList = mutableListOf<PostThread>()
    private var mMenuItemRefresh: MenuItem? = null // 菜单刷新键
    private var mMenuItemEditForum: MenuItem? = null // 编辑版块

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

    // 加载action bar菜单
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        mMenuItemRefresh = menu.findItem(R.id.menu_item_refresh)
        mMenuItemEditForum = menu.findItem(R.id.menu_item_edit_forum)
        mMenuItemEditForum!!.isVisible = false
        return true
    }

    // 绑定菜单按钮事件
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            // 刷新
            R.id.menu_item_refresh -> {
                loadThread(true)
                return true
            }
            // 编辑版块
            R.id.menu_item_edit_forum -> {
                startActivity(Intent(this, EditForumActivity::class.java))
                return true
            }
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

        // 隐藏刷新菜单
        content_dl.addDrawerListener(object : ActionBarDrawerToggle(this, main_layout,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close) {
            override fun onDrawerClosed(drawerView: View?) {
                super.onDrawerClosed(drawerView)
                mMenuItemRefresh!!.isVisible = true
                mMenuItemEditForum!!.isVisible = false
            }

            override fun onDrawerOpened(drawerView: View?) {
                super.onDrawerOpened(drawerView)
                mMenuItemRefresh!!.isVisible = false
                mMenuItemEditForum!!.isVisible = true
            }
        })
    }

    /**
     * 初始化监听器
     */
    private fun initListener() {
        // 加载版块串列表
        forum_list.setOnItemClickListener { _, view, _, _ ->
            val forum = view.tag as Forum
            content_dl.closeDrawer(GravityCompat.END)
            if (forum.id != mCurrentForumId) {
                mCurrentForumId = forum.id
                loadThread(true)
            }
        }
        // 下拉刷新
        thread_srl.setOnRefreshListener {
            loadThread(true)
        }
        // 滚动加载
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
        // 打开子串列表
        thread_list.setOnItemClickListener { _, view, _, _ ->
            val id = view.tag as Int
            val intent = Intent(this, ThreadActivity::class.java)
            intent.putExtra(C.Extra.THREAD_ID, id)
            startActivity(intent)
        }
    }

    /**
     * 初始化数据
     */
    private fun initData() {
        loadNotice()
        loadForum()
        // 打开图片展示页面
        val imageClickListener = ImageActivity.OpenOnClickListener(this)
        thread_list.adapter = object : MyBaseAdapter<PostThread>(mThreadList) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
                val item = getItem(position)
                val view = convertView ?: View.inflate(this@MainActivity, R.layout.item_content, null)
                view.tag = item.id // 保存ID，打开子串列表时用
                view.text_user_id.text = item.userid
                view.text_id.text = item.recodeId
                view.text_now.text = item.recodeNow
                view.text_content.text = Html.fromHtml(item.content)
                item.bindThumb(view.image)
                view.image.tag = item.imageUrl // 保存大图链接，展示大图时用
                view.image.setOnClickListener(imageClickListener)
                return view
            }
        }
    }

    /**
     * 加载版块列表
     */
    private fun loadForum() {
        DataClient.listForum {
            if (it.success) {
                forum_list.adapter = object : MyBaseAdapter<Forum>(it.data!!) {
                    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
                        val item = getItem(position)
                        val view = (convertView ?: View.inflate(this@MainActivity, R.layout.item_forum, null)) as TextView
                        view.text = item.name
                        view.tag = item
                        return view
                    }
                }
                for (item in it.data) {
                    // 加载第一个非时间线版块
                    if (item.id != -1) {
                        mCurrentForumId = item.id
                        loadThread()
                        break
                    }
                }
            } else {
                MessageUtils.showToast(this@MainActivity, it.message!!)
            }
        }
    }

    /**
     * 加载串列表
     */
    @UiThread
    private fun loadThread(refresh: Boolean = false) {
        if (mLoading) return
        mLoading = true
        thread_srl.isRefreshing = true
        if (refresh) mCurrentPage = 1 else mCurrentPage++
        DataClient.listThread(mCurrentForumId, mCurrentPage) {
            thread_srl.isRefreshing = false
            if (it.success) {
                thread_main.hideError()
                if (!it.data!!.isEmpty()) {
                    if (refresh) mThreadList.clear()
                    mThreadList.addAll(mThreadList.size, it.data)
                    (thread_list.adapter as BaseAdapter).notifyDataSetChanged()
                    // 在notifyDataSetChanged之后调用
                    if (refresh) thread_list.setSelection(0)
                }
            } else {
                thread_main.showError(it.message!!) {
                    loadThread(true)
                }
            }
            mLoading = false
        }
    }

    /**
     * 加载通知
     */
    private fun loadNotice() {
        JsonHttpTask(C.Api.NOTICE) {
            if (it.success) {
                val notice = JSON.parseObject(it.data, Notice::class.java)
                val mSp = getSharedPreferences(C.SP.NAME, Context.MODE_PRIVATE)
                val timestamp = mSp.getLong(C.SP.NOTICE_TIMESTAMP, 0L)
                if (notice.date != timestamp) {
                    val view = View.inflate(this, R.layout.dialog_notice, null)
                    view.text_notice.text = Html.fromHtml(notice.content)
                    // text中的链接可点击
                    view.text_notice.movementMethod = LinkMovementMethod.getInstance()
                    val dialog = AlertDialog.Builder(this).setView(view).setTitle("通知").create()
                    view.btn_sure.setOnClickListener {
                        if (view.cb_known.isChecked) {
                            mSp.edit().putLong(C.SP.NOTICE_TIMESTAMP, notice.date).apply()
                        }
                        dialog.hide()
                    }
                    dialog.show()
                }
            }
        }.execute()
    }
}
