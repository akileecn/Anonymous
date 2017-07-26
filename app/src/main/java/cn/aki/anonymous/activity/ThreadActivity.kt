package cn.aki.anonymous.activity

import android.os.Bundle
import android.support.annotation.UiThread
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.BaseAdapter
import cn.aki.anonymous.R
import cn.aki.anonymous.base.C
import cn.aki.anonymous.base.MyBaseAdapter
import cn.aki.anonymous.dao.PostDao
import cn.aki.anonymous.entity.Post
import cn.aki.anonymous.utils.DataUtils
import kotlinx.android.synthetic.main.activity_thread.*
import kotlinx.android.synthetic.main.item_post.view.*

class ThreadActivity : AppCompatActivity() {
    @Volatile private var mLoading: Boolean = false // 是否正在加载
    private var mCurrentPage: Int = 0 // 当前页数
    private val mPostList = mutableListOf<Post>()
    private var mThreadId: Int = 0 // 当前串号
    private val mPostDao = PostDao()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_thread)
        initListener()
        initData()
    }

    fun initListener() {
        // 下拉刷新
        thread_srl.setOnRefreshListener {
            loadPost(true)
        }
        // 滚动加载
        post_list.setOnScrollListener(object : AbsListView.OnScrollListener {
            override fun onScroll(view: AbsListView?, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {
            }

            override fun onScrollStateChanged(view: AbsListView?, scrollState: Int) {
                if ((scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                        || scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING
                        ) && view!!.lastVisiblePosition >= view.count - 1) {
                    loadPost()
                }
            }

        })
    }

    fun initData() {
        mThreadId = intent.getIntExtra(C.Extra.THREAD_ID, 0)
        val imageClickListener = ImageActivity.OpenOnClickListener(this)
        post_list.adapter = object : MyBaseAdapter<Post>(mPostList) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
                val item = getItem(position)
                val view = convertView ?: View.inflate(this@ThreadActivity, R.layout.item_post, null)
                view.text_user_id.text = item.userid
                view.text_id.text = item.recodeId
                view.text_now.text = item.recodeNow
                view.text_content.text = DataUtils.fromHtml(item.content)
                item.bindThumb(view.image)
                view.image.tag = item.imageUrl
                view.image.setOnClickListener(imageClickListener)
                return view
            }
        }
        loadPost(true)
    }

    /**
     * 加载串列表
     */
    @UiThread
    private fun loadPost(refresh: Boolean = false) {
        if (mLoading) return
        mLoading = true
        thread_srl.isRefreshing = true
        if (refresh) mCurrentPage = 1 else mCurrentPage++
        mPostDao.listPost(mThreadId, mCurrentPage) {
            thread_srl.isRefreshing = false
            if (it.success) {
                thread_main.hideError()
                if (refresh || !it.data!!.replys.isEmpty()) {
                    if (refresh) {
                        mPostList.clear()
                        mPostList.add(it.data!!)
                    }
                    mPostList.addAll(mPostList.size, it.data!!.replys)
                    (post_list.adapter as BaseAdapter).notifyDataSetChanged()
                }
            } else {
                thread_main.showError(it.message!!) {
                    loadPost(true)
                }
            }
            mLoading = false
        }
    }
}
