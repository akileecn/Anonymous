package cn.aki.anonymous.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.SpinnerAdapter
import android.widget.TextView

import cn.aki.anonymous.R
import cn.aki.anonymous.base.C
import cn.aki.anonymous.base.MyBaseAdapter
import cn.aki.anonymous.dao.ForumDao
import cn.aki.anonymous.entity.Forum
import kotlinx.android.synthetic.main.activity_do_post.*

/**
 * 发串
 */
class DoPostActivity : AppCompatActivity() {
    private val mForumDao: ForumDao = ForumDao(this)
    private var mCurrentForumId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_do_post)
        initData()
    }

    private fun initData(){
        // 获取原始版块号
        mCurrentForumId = intent.getIntExtra(C.Extra.FORUM_ID, 0)
        mForumDao.list {
            var currentPos: Int = 0
            for((index, value) in it.data!!.withIndex()){
                if(value.id == mCurrentForumId){
                    currentPos = index
                    break
                }
            }
            spinner_forum.adapter = object : MyBaseAdapter<Forum>(it.data) {
                val selectedPostId = 0
                override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
                    val forum = getItem(position)
                    val view = (convertView ?: TextView(this@DoPostActivity)) as TextView
                    view.text = forum.name
                    view.tag = forum.id
                    return view
                }
            }
            spinner_forum.setSelection(currentPos, true)
        }
        spinner_forum.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                mCurrentForumId = view!!.tag as Int
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

        }
    }
}
