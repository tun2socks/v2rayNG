package com.tun2socks.ui

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.tun2socks.R
import com.tun2socks.dto.AngConfig
import com.tun2socks.util.AngConfigManager
import kotlinx.android.synthetic.main.item_recycler_sub_setting.view.*
import org.jetbrains.anko.*

class SubSettingRecyclerAdapter(val activity: SubSettingActivity) : RecyclerView.Adapter<SubSettingRecyclerAdapter.BaseViewHolder>() {

    private var mActivity: SubSettingActivity = activity
    private lateinit var configs: AngConfig

    init {
        updateConfigList()
    }

    override fun getItemCount() = configs.subItem.count()

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        if (holder is MainViewHolder) {
            val remarks = configs.subItem[position].remarks
            val url = configs.subItem[position].url

            holder.name.text = remarks
            holder.url.text = url
            holder.itemView.backgroundColor = Color.TRANSPARENT

            holder.layout_edit.setOnClickListener {
                mActivity.startActivity<SubEditActivity>("position" to position)
            }
        } else {
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return MainViewHolder(parent.context.layoutInflater
                .inflate(R.layout.item_recycler_sub_setting, parent, false))
    }

    fun updateConfigList() {
        configs = AngConfigManager.configs
        notifyDataSetChanged()
    }

//    fun updateSelectedItem() {
//        notifyItemChanged(configs.index)
//    }

    open class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    class MainViewHolder(itemView: View) : BaseViewHolder(itemView) {
        val name = itemView.tv_name!!
        val url = itemView.tv_url!!
        val layout_edit = itemView.layout_edit!!
    }

}
