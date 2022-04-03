package com.mycompany.myapp3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.mycompany.myapp3.R;
import java.util.ArrayList;

public class BookAdapter extends ArrayAdapter<Book> {
	public BookAdapter(Context context, ArrayList<Book> books){
		super(context,0,books);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		Book book = getItem(position);
		if(convertView == null){
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.list,parent,false);
		}

		TextView titleV = convertView.findViewById(R.id.titleView);
		
		TextView yearV = convertView.findViewById(R.id.item_year);
		titleV.setText(book.title);
		yearV.setText("("+book.year+")");
		titleV.setTextColor(getContext().getResources().getColor(R.color.white));
		yearV.setTextColor(getContext().getResources().getColor(R.color.white));
		return convertView;
	}
}
