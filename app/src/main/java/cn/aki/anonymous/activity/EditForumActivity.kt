package cn.aki.anonymous.activity

import android.app.Activity
import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.support.v7.widget.helper.ItemTouchHelper.ACTION_STATE_DRAG
import android.support.v7.widget.helper.ItemTouchHelper.ACTION_STATE_SWIPE
import android.text.Editable
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import cn.aki.anonymous.R
import cn.aki.anonymous.dao.ForumDao
import cn.aki.anonymous.entity.Forum
import kotlinx.android.synthetic.main.activity_edit_forum.*
import kotlinx.android.synthetic.main.dialog_edit_forum.view.*
import java.util.*


class EditForumActivity : AppCompatActivity() {
    private val mForumList = mutableListOf<Forum>()
    private val mDisabledForumList = mutableListOf<Forum>()
    private val mForumDao = ForumDao(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_forum)
        // recyclerView拖动滑动帮助类
        ItemTouchHelper(object : ItemTouchHelper.Callback() {
            // 允许操作
            override fun getMovementFlags(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?): Int {
                return makeFlag(ACTION_STATE_SWIPE, ItemTouchHelper.RIGHT) or
                        makeFlag(ACTION_STATE_DRAG, ItemTouchHelper.UP or ItemTouchHelper.DOWN)
            }

            // 上下拖动
            override fun onMove(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, target: RecyclerView.ViewHolder?): Boolean {
                // 改变数据集中的位置
                val movedPos = viewHolder!!.adapterPosition
                val targetPos = target!!.adapterPosition
                Collections.swap(mForumList, movedPos, targetPos)
                forum_recycler.adapter.notifyItemMoved(movedPos, targetPos)
                return true
            }

            // 左右滑动
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int) {
                mForumList.removeAt(viewHolder!!.adapterPosition)
                mDisabledForumList.add(viewHolder.itemView.tag as Forum)
                forum_recycler.adapter.notifyItemRemoved(viewHolder.adapterPosition)
            }

        }).attachToRecyclerView(forum_recycler)
        // 必须设置布局管理器，不然view不显示
        forum_recycler.layoutManager = LinearLayoutManager(this)
        mForumDao.list {
            if (it.success) {
                mForumList.addAll(0, it.data!!)
                forum_recycler.adapter = object : MyRecyclerViewAdapter(mForumList) {
                    override fun onClick(v: View?) {
                        showDialog(v!!.tag as Forum)
                    }
                }
            }
        }
    }

    override fun onBackPressed() {
        // 保存修改
        mForumDao.update(mForumList, mDisabledForumList)
        setResult(Activity.RESULT_OK)
        finish()
    }

    private abstract class MyRecyclerViewAdapter(val list: MutableList<Forum>) : RecyclerView.Adapter<MyRecyclerViewAdapter.MyViewHolder>(), View.OnClickListener {

        override fun getItemCount(): Int {
            return list.size
        }

        override fun onBindViewHolder(holder: MyViewHolder?, position: Int) {
            val forum = list[position]
            forum.orderBy = position // 保存序号
            holder!!.name.text = forum.name
            holder.itemView.tag = forum
            holder.itemView.setOnClickListener(this)
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MyViewHolder {
            // item match_parent生效但不添加到parent
            val view = LayoutInflater.from(parent!!.context).inflate(R.layout.item_edit_forum, parent, false)
            return MyViewHolder(view)
        }

        inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val name: TextView = view.findViewById(R.id.text_name) as TextView
        }

    }

    private fun showDialog(forum: Forum) {
        val view = View.inflate(this, R.layout.dialog_edit_forum, null)
        view.tag = forum
        view.edit_text_id.text = Editable.Factory().newEditable(forum.id.toString())
        view.edit_text_name.text = Editable.Factory().newEditable(forum.name)
        view.edit_text_order_by.text = Editable.Factory().newEditable(forum.orderBy.toString())
        val dialog = AlertDialog.Builder(this).setView(view).create()
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, resources.getText(R.string.sure), {
            _, _ ->
            val idString = view.edit_text_id.text.toString()
            if (!TextUtils.isEmpty(idString)) {
                forum.id = idString.toInt()
            }
            forum.name = view.edit_text_name.text.toString()
            val orderByString = view.edit_text_order_by.text.toString()
            if (!TextUtils.isEmpty(orderByString)) {
                val orderBy = orderByString.toInt()
                changeForum(forum.orderBy, orderBy)
            }
            forum_recycler.adapter.notifyDataSetChanged()
            dialog.cancel()
        })
        dialog.show()
    }

    /**
     * 移动版块
     */
    private fun changeForum(movedPos: Int, targetPos: Int) {
        var realTargetPos = targetPos
        if (realTargetPos > mForumList.size - 1) {
            realTargetPos = mForumList.size - 1
        } else if (realTargetPos < 0) {
            realTargetPos = 0
        }
        if (realTargetPos != movedPos) {
            mForumList.add(if (realTargetPos > movedPos) realTargetPos + 1 else realTargetPos, mForumList[movedPos])
            mForumList.removeAt(if (realTargetPos > movedPos) movedPos else movedPos + 1)
        }
    }

}
