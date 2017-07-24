package cn.aki.anonymous.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.support.v7.widget.helper.ItemTouchHelper.ACTION_STATE_IDLE
import android.support.v7.widget.helper.ItemTouchHelper.ACTION_STATE_SWIPE
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import cn.aki.anonymous.R
import cn.aki.anonymous.entity.Forum
import cn.aki.anonymous.utils.DataClient
import kotlinx.android.synthetic.main.activity_edit_forum.*


class EditForumActivity : AppCompatActivity() {
    private val mForumList = mutableListOf<Forum>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_forum)
        //TODO 上下拖动
        val itemHelper = ItemTouchHelper(object : ItemTouchHelper.Callback() {
            override fun getMovementFlags(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?): Int {
                return makeFlag(ACTION_STATE_IDLE, ItemTouchHelper.RIGHT) or makeFlag(ACTION_STATE_SWIPE, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)
            }

            override fun onMove(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, target: RecyclerView.ViewHolder?): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int) {
            }

        })
        itemHelper.attachToRecyclerView(forum_recycler)
        // 必须设置布局管理器，不然view不显示
        forum_recycler.layoutManager = LinearLayoutManager(this)
        DataClient.listForum {
            if (it.success) {
                mForumList.addAll(0, it.data!!)
                forum_recycler.adapter = MyRecyclerViewAdapter(mForumList)
            }
        }
    }

    class MyRecyclerViewAdapter(val list: MutableList<Forum>) : RecyclerView.Adapter<MyRecyclerViewAdapter.MyViewHolder>() {
        override fun getItemCount(): Int {
            return list.size
        }

        override fun onBindViewHolder(holder: MyViewHolder?, position: Int) {
            val forum = list[position]
            holder!!.name.text = forum.name
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MyViewHolder {
            val view = View.inflate(parent!!.context, R.layout.item_edit_forum, null)
            return MyViewHolder(view)
        }

        class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val name: TextView = view.findViewById(R.id.text_name) as TextView
        }

    }
}
