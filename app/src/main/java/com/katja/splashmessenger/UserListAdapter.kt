package com.katja.splashmessenger
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class UserListAdapter(context: Context, users: List<User>) :
    ArrayAdapter<User>(context, 0, users) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var listItemView = convertView
        if (listItemView == null) {
            listItemView = LayoutInflater.from(context).inflate(
                R.layout.list_layout, parent, false
            )
        }

        val currentUser = getItem(position)

        val textView = listItemView?.findViewById<TextView>(R.id.textView)
        textView?.text = currentUser?.fullName // Visa användarnas fulla namn i listan

        return listItemView!!
    }
}
