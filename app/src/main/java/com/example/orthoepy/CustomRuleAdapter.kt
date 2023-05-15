package com.example.orthoepy

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class CustomRuleAdapter(context: Context, items:List<String>): BaseAdapter()  {
    private var items:List<String>
    private var context:Context
    init {
        this.items = items
        this.context = context
    }
    override fun getCount(): Int {
        return items.size
    }

    override fun getItem(position: Int): Any {
        return items[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val oneItem = LayoutInflater.from(context).inflate(R.layout.one_rule_item, parent, false)
        val text: TextView = oneItem.findViewById(R.id.rule_text)
        text.text = items[position]
        return oneItem
    }
}